package net.ludocrypt.limlib.impl.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Lifecycle;
import net.ludocrypt.limlib.api.LimlibRegistryHooks;
import net.ludocrypt.limlib.api.LimlibRegistryHooks.LimlibJsonRegistryHook;
import net.ludocrypt.limlib.api.LimlibRegistryHooks.LimlibRegistryHook;
import net.ludocrypt.limlib.api.LimlibWorld;
import net.ludocrypt.limlib.api.LimlibWorld.RegistryProvider;
import net.ludocrypt.limlib.api.effects.post.PostEffect;
import net.ludocrypt.limlib.api.effects.sky.LDimensionEffects;
import net.ludocrypt.limlib.api.effects.sound.SoundEffects;
import net.ludocrypt.limlib.api.skybox.Skybox;
import net.minecraft.registry.*;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Mixin(RegistryLoader.class)
public class RegistryLoaderMixin {

	@Shadow
	@Final
	@Mutable
	public static List<RegistryLoader.Entry<?>> DYNAMIC_REGISTRIES;
	static {
		List<RegistryLoader.Entry<?>> newRegistries = Lists.newArrayList();
		newRegistries.addAll(DYNAMIC_REGISTRIES);
		newRegistries.add(new RegistryLoader.Entry(PostEffect.POST_EFFECT_KEY, PostEffect.CODEC));
		newRegistries.add(new RegistryLoader.Entry(LDimensionEffects.DIMENSION_EFFECTS_KEY, LDimensionEffects.CODEC));
		newRegistries.add(new RegistryLoader.Entry(SoundEffects.SOUND_EFFECTS_KEY, SoundEffects.CODEC));
		newRegistries.add(new RegistryLoader.Entry(Skybox.SKYBOX_KEY, Skybox.CODEC));
		DYNAMIC_REGISTRIES = newRegistries;
	}

	@Unique
	private static final AtomicReference<DynamicRegistryManager.Immutable> LOADED_REGISTRY = new AtomicReference<>();


	@Inject(method = "load(Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Decoder;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;", shift = Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private static <E> void limlib$loadRegistryContents(RegistryOps.RegistryInfoGetter infoLookup,
														ResourceManager resourceManager, RegistryKey<? extends Registry<E>> registryKey, MutableRegistry<E> registry,
														Decoder<E> decoder, Map<RegistryKey<?>, Exception> readFailures, CallbackInfo ci, String string,
														ResourceFinder resourceFileNamespace, RegistryOps<JsonElement> registryOps,
														Iterator<Map.Entry<Identifier, Resource>> var9, Map.Entry<Identifier, Resource> entry, Identifier identifier,
														RegistryKey<E> registryKey2, Resource resource, Reader reader, JsonElement jsonElement) {

		if (registryKey2.isOf(RegistryKeys.WORLD_PRESET)) {
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

		LimlibRegistryHooks.REGISTRY_JSON_HOOKS
			.getOrDefault(registryKey, Sets.newHashSet())
			.forEach((registrarhook -> ((LimlibJsonRegistryHook<E>) registrarhook)
				.register(infoLookup, registryKey, registryOps, jsonElement)));
	}

	@Inject(method = "load(Lnet/minecraft/registry/RegistryOps$RegistryInfoGetter;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/MutableRegistry;Lcom/mojang/serialization/Decoder;Ljava/util/Map;)V", at = @At("TAIL"))
	private static <E> void limlib$loadRegistryContents(RegistryOps.RegistryInfoGetter infoLookup,
			ResourceManager resourceManager, RegistryKey<? extends Registry<E>> registryKey, MutableRegistry<E> registry,
			Decoder<E> decoder, Map<RegistryKey<?>, Exception> readFailures, CallbackInfo ci) {

		if (registryKey.equals(RegistryKeys.DIMENSION_TYPE)) {
			LimlibWorld.LIMLIB_WORLD
				.getEntrySet()
				.forEach((world) -> ((MutableRegistry<DimensionType>) registry)
					.add(RegistryKey.of(RegistryKeys.DIMENSION_TYPE, world.getKey().getValue()),
						world.getValue().getDimensionTypeSupplier().get(), Lifecycle.stable()));
		}

		LimlibRegistryHooks.REGISTRY_HOOKS
			.getOrDefault(registryKey, Sets.newHashSet())
			.forEach((registrarhook -> ((LimlibRegistryHook<E>) registrarhook).register(infoLookup, registryKey, registry)));
	}

	@Inject(method = "load(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/DynamicRegistryManager;Ljava/util/List;)Lnet/minecraft/registry/DynamicRegistryManager$Immutable;", at = @At("TAIL"))
	private static void limlib$loadRegistriesIntoManager(ResourceManager resourceManager,
														 DynamicRegistryManager registryManager, List<RegistryLoader.Entry<?>> decodingData,
														 CallbackInfoReturnable<DynamicRegistryManager.Immutable> ci) {
		LOADED_REGISTRY.set(registryManager.toImmutable());
	}

}
