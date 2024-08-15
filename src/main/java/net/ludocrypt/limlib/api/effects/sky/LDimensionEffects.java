package net.ludocrypt.limlib.api.effects.sky;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.impl.mixin.RegistriesAccessor;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * A non-client-side clone of {@link DimensionEffects}
 */
public abstract class LDimensionEffects {

	public static final RegistryKey<Registry<Codec<? extends LDimensionEffects>>> DIMENSION_EFFECTS_CODEC_KEY = RegistryKey
		.ofRegistry(new Identifier("limlib/codec/dimension_effects"));
	public static final Registry<Codec<? extends LDimensionEffects>> DIMENSION_EFFECTS_CODEC = RegistriesAccessor
		.callCreate(DIMENSION_EFFECTS_CODEC_KEY, Lifecycle.stable(), (registry) -> StaticDimensionEffects.CODEC);
	public static final Codec<LDimensionEffects> CODEC = DIMENSION_EFFECTS_CODEC
		.getCodec()
		.dispatchStable(LDimensionEffects::getCodec, Function.identity());
	public static final RegistryKey<Registry<LDimensionEffects>> DIMENSION_EFFECTS_KEY = RegistryKey
		.ofRegistry(new Identifier("limlib/dimension_effects"));

	public static final AtomicReference<RegistryWrapper<LDimensionEffects>> MIXIN_WORLD_LOOKUP = new AtomicReference<RegistryWrapper<LDimensionEffects>>();

	public abstract Codec<? extends LDimensionEffects> getCodec();

	@Environment(EnvType.CLIENT)
	public abstract DimensionEffects getDimensionEffects();

	public abstract float getSkyShading();

}
