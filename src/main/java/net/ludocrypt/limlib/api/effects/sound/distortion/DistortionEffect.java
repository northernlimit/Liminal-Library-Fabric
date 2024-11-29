package net.ludocrypt.limlib.api.effects.sound.distortion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.ludocrypt.limlib.impl.mixin.RegistriesAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Function;

/**
 * A Distortion effect controls
 */
public abstract class DistortionEffect {

	public static final RegistryKey<Registry<MapCodec<? extends DistortionEffect>>> DISTORTION_EFFECT_CODEC_KEY = RegistryKey
		.ofRegistry(Identifier.of("limlib/codec/distortion_effect"));
	public static final Registry<MapCodec<? extends DistortionEffect>> DISTORTION_EFFECT_CODEC = RegistriesAccessor
		.callCreate(DISTORTION_EFFECT_CODEC_KEY, (registry) -> StaticDistortionEffect.CODEC);
	public static final Codec<DistortionEffect> CODEC = DISTORTION_EFFECT_CODEC
		.getCodec()
		.dispatchStable(DistortionEffect::getCodec, Function.identity());

	public abstract MapCodec<? extends DistortionEffect> getCodec();

	/**
	 * Whether or not a Sound Event should be ignored
	 * 
	 * @param identifier the Identifier of the Sound Event
	 */
	public abstract boolean shouldIgnore(Identifier identifier);

	public abstract boolean isEnabled(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getEdge(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getGain(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getLowpassCutoff(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getEQCenter(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getEQBandWidth(MinecraftClient client, SoundInstance soundInstance);

}
