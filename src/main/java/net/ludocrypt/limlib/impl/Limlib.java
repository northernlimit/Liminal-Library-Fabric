package net.ludocrypt.limlib.impl;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.loader.api.FabricLoader;
import net.ludocrypt.limlib.api.LimlibRegistrar;
import net.ludocrypt.limlib.api.LimlibWorld;
import net.ludocrypt.limlib.api.effects.post.EmptyPostEffect;
import net.ludocrypt.limlib.api.effects.post.PostEffect;
import net.ludocrypt.limlib.api.effects.post.StaticPostEffect;
import net.ludocrypt.limlib.api.effects.sky.LiminalDimensionEffects;
import net.ludocrypt.limlib.api.effects.sky.EmptyDimensionEffects;
import net.ludocrypt.limlib.api.effects.sky.StaticDimensionEffects;
import net.ludocrypt.limlib.api.effects.sound.SoundEffects;
import net.ludocrypt.limlib.api.effects.sound.distortion.DistortionEffect;
import net.ludocrypt.limlib.api.effects.sound.distortion.StaticDistortionEffect;
import net.ludocrypt.limlib.api.effects.sound.reverb.ReverbEffect;
import net.ludocrypt.limlib.api.effects.sound.reverb.StaticReverbEffect;
import net.ludocrypt.limlib.api.skybox.EmptySkybox;
import net.ludocrypt.limlib.api.skybox.Skybox;
import net.ludocrypt.limlib.api.skybox.TexturedSkybox;
import net.ludocrypt.limlib.impl.debug.DebugNbtChunkGenerator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Limlib {

	public static final Logger LOGGER = LoggerFactory.getLogger("Limlib");

	public static void onInitialize() {
		DynamicRegistries.registerSynced(PostEffect.POST_EFFECT_KEY, PostEffect.CODEC);
		DynamicRegistries.registerSynced(LiminalDimensionEffects.DIMENSION_EFFECTS_KEY, LiminalDimensionEffects.CODEC);
		DynamicRegistries.registerSynced(SoundEffects.SOUND_EFFECTS_KEY, SoundEffects.CODEC);
		DynamicRegistries.registerSynced(Skybox.SKYBOX_KEY, Skybox.CODEC);

		LimlibWorld.load();
		Registry.register(ReverbEffect.REVERB_EFFECT_CODEC, Identifier.of("limlib", "static"),
				StaticReverbEffect.CODEC);
		Registry
			.register(DistortionEffect.DISTORTION_EFFECT_CODEC, Identifier.of("limlib", "static"),
				StaticDistortionEffect.CODEC);
		Registry
			.register(LiminalDimensionEffects.DIMENSION_EFFECTS_CODEC, Identifier.of("limlib", "static"),
				StaticDimensionEffects.CODEC);
		Registry
			.register(LiminalDimensionEffects.DIMENSION_EFFECTS_CODEC, Identifier.of("limlib", "empty"),
				EmptyDimensionEffects.CODEC);
		Registry.register(PostEffect.POST_EFFECT_CODEC, Identifier.of("limlib", "static"), StaticPostEffect.CODEC);
		Registry.register(PostEffect.POST_EFFECT_CODEC, Identifier.of("limlib", "empty"), EmptyPostEffect.CODEC);
		Registry.register(Skybox.SKYBOX_CODEC, Identifier.of("limlib", "empty"), EmptySkybox.CODEC);
		Registry.register(Skybox.SKYBOX_CODEC, Identifier.of("limlib", "textured"), TexturedSkybox.CODEC);
		Registry
				.register(Registries.CHUNK_GENERATOR, Identifier.of("limlib", "debug_nbt_chunk_generator"), DebugNbtChunkGenerator.CODEC);

		FabricLoader.getInstance()
			.getEntrypoints(LimlibRegistrar.ENTRYPOINT_KEY, LimlibRegistrar.class)
			.forEach(LimlibRegistrar::registerHooks);

	}

}
