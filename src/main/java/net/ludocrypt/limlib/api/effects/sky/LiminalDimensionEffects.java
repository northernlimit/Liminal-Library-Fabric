package net.ludocrypt.limlib.api.effects.sky;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
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
public abstract class LiminalDimensionEffects {

	public static final RegistryKey<Registry<MapCodec<? extends LiminalDimensionEffects>>> DIMENSION_EFFECTS_CODEC_KEY = RegistryKey
		.ofRegistry(Identifier.of("limlib/codec/dimension_effects"));
	public static final Registry<MapCodec<? extends LiminalDimensionEffects>> DIMENSION_EFFECTS_CODEC = RegistriesAccessor
		.callCreate(DIMENSION_EFFECTS_CODEC_KEY, (registry) -> StaticDimensionEffects.CODEC);
	public static final Codec<LiminalDimensionEffects> CODEC = DIMENSION_EFFECTS_CODEC
		.getCodec()
		.dispatchStable(LiminalDimensionEffects::getCodec, Function.identity());
	public static final RegistryKey<Registry<LiminalDimensionEffects>> DIMENSION_EFFECTS_KEY = RegistryKey
		.ofRegistry(Identifier.of("limlib/dimension_effects"));

	public static final AtomicReference<RegistryWrapper<LiminalDimensionEffects>> MIXIN_WORLD_LOOKUP = new AtomicReference<>();

	public abstract MapCodec<? extends LiminalDimensionEffects> getCodec();

	@Environment(EnvType.CLIENT)
	public abstract DimensionEffects getDimensionEffects();

	public abstract float getSkyShading();

}
