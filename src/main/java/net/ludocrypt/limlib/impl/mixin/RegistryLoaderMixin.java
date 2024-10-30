package net.ludocrypt.limlib.impl.mixin;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Decoder;
import net.ludocrypt.limlib.api.LimlibRegistryHooks;
import net.ludocrypt.limlib.api.LimlibRegistryHooks.LimlibRegistryHook;
import net.ludocrypt.limlib.api.LimlibWorld;
import net.ludocrypt.limlib.api.LimlibWorld.RegistryProvider;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings({ "unchecked"})
@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {

	@Unique
	private static final AtomicReference<DynamicRegistryManager.Immutable> LOADED_REGISTRY = new AtomicReference<>();


	@Inject(method = "parseAndAdd", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Decoder;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;", shift = Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private static <E> void limlib$loadRegistryContents(MutableRegistry<E> registry, Decoder<E> decoder,
														RegistryOps<JsonElement> registryOps, RegistryKey<E> key,
														Resource resource, RegistryEntryInfo entryInfo,
														CallbackInfo ci, Reader reader, JsonElement jsonElement) {
		if (key.isOf(RegistryKeys.WORLD_PRESET)) {
			JsonObject presetType = jsonElement.getAsJsonObject();
			JsonObject dimensions = presetType.get("dimensions").getAsJsonObject();
			LimlibWorld.LIMLIB_WORLD
					.getEntrySet()
					.forEach((world) -> dimensions
							.add(world.getKey().getValue().toString(),
									DimensionOptions.CODEC
											.encodeStart(registryOps,
													world.getValue().getDimensionOptionsSupplier().apply(new RegistryProvider() {

														@Override
														public <T> RegistryEntryLookup<T> get(RegistryKey<Registry<T>> key) {
															return registryOps.getEntryLookup(key).get();
														}

													}))
											.result()
											.get()));
		}
	}



	@Inject(method = "loadFromResource(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V", at = @At("TAIL"))
	private static <E> void limlib$loadRegistryContents(ResourceManager resourceManager, RegistryOps.RegistryInfoGetter infoGetter,
														MutableRegistry<E> registry, Decoder<E> elementDecoder, Map<RegistryKey<?>, Exception> errors, CallbackInfo ci) {

		if (registry.getKey().equals(RegistryKeys.DIMENSION_TYPE)) {
			LimlibWorld.LIMLIB_WORLD
				.getEntrySet()
				.forEach((world) -> ((MutableRegistry<DimensionType>) registry)
					.add(RegistryKey.of(RegistryKeys.DIMENSION_TYPE, world.getKey().getValue()),
						world.getValue().getDimensionTypeSupplier().get(),
							RegistryEntryInfo.DEFAULT));
		}

		LimlibRegistryHooks.REGISTRY_HOOKS
			.getOrDefault(registry.getKey(), Sets.newHashSet())
			.forEach((registrarhook -> ((LimlibRegistryHook<E>) registrarhook).register(infoGetter, registry.getKey(), registry)));
	}

	@Inject(method = "load", at = @At("TAIL"))
	private static void limlib$loadRegistriesIntoManager(RegistryLoader.RegistryLoadable loadable,
														 DynamicRegistryManager registryManager, List<RegistryLoader.Entry<?>> entries,
														 CallbackInfoReturnable<DynamicRegistryManager.Immutable> cir) {
		LOADED_REGISTRY.set(registryManager.toImmutable());
	}

}
