package net.ludocrypt.limlib.api.effects.sound.reverb;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.ludocrypt.limlib.impl.mixin.RegistriesAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Function;

/**
 * A Reverb effect controls
 */
public abstract class ReverbEffect {

	public static final RegistryKey<Registry<MapCodec<? extends ReverbEffect>>> REVERB_EFFECT_CODEC_KEY = RegistryKey
		.ofRegistry(new Identifier("limlib/codec/reverb_effect"));
	public static final Registry<MapCodec<? extends ReverbEffect>> REVERB_EFFECT_CODEC = RegistriesAccessor
		.callCreate(REVERB_EFFECT_CODEC_KEY, (registry) -> StaticReverbEffect.CODEC);
	public static final Codec<ReverbEffect> CODEC = REVERB_EFFECT_CODEC
		.getCodec()
		.dispatchStable(ReverbEffect::getCodec, Function.identity());

	public abstract MapCodec<? extends ReverbEffect> getCodec();

	/**
	 * Whether or not a Sound Event should be ignored
	 * 
	 * @param identifier the Identifier of the Sound Event
	 */
	public abstract boolean shouldIgnore(Identifier identifier);

	public abstract boolean isEnabled(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getAirAbsorptionGainHF(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getDecayHFRatio(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getDensity(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getDiffusion(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getGain(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getGainHF(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getLateReverbGainBase(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getDecayTime(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getReflectionsGainBase(MinecraftClient client, SoundInstance soundInstance);

	public abstract int getDecayHFLimit(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getLateReverbDelay(MinecraftClient client, SoundInstance soundInstance);

	public abstract float getReflectionsDelay(MinecraftClient client, SoundInstance soundInstance);

}
