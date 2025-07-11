package net.ludocrypt.limlib.impl.mixin;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.Reader;
import java.util.Map;

@SuppressWarnings("unchecked")
@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {

	@Inject(method = "Lnet/minecraft/registry/RegistryLoader;parseAndAdd(Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Lnet/minecraft/registry/RegistryOps;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/resource/Resource;Lnet/minecraft/registry/entry/RegistryEntryInfo;)V", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/gson/JsonParser;parseReader(Ljava/io/Reader;)Lcom/google/gson/JsonElement;", remap = false))
	private static <E> void limlib$loadRegistryContents(MutableRegistry<E> registry, Decoder<E> decoder,
														RegistryOps<JsonElement> registryOps, RegistryKey<E> key,
														Resource resource, RegistryEntryInfo entryInfo, CallbackInfo ci,
														@Local Reader reader, @Local JsonElement jsonElement) {
		if (key.isOf(RegistryKeys.WORLD_PRESET)) {
			JsonObject presetType = jsonElement.getAsJsonObject();
			JsonObject dimensions = presetType.get("dimensions").getAsJsonObject();
			LimlibWorld.LIMLIB_WORLD
					.getEntrySet()
					.forEach((world) -> dimensions
							.add(world.getKey().getValue().toString(),
									DimensionOptions.CODEC
											.encodeStart(registryOps,
													world.getValue().dimensionOptionsSupplier().apply(new RegistryProvider() {

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
						world.getValue().dimensionTypeSupplier().get(),
							RegistryEntryInfo.DEFAULT));
		}

		LimlibRegistryHooks.REGISTRY_HOOKS
			.getOrDefault(registry.getKey(), Sets.newHashSet())
			.forEach((registrarhook -> ((LimlibRegistryHook<E>) registrarhook).register(infoGetter, registry.getKey(), registry)));
	}

}
