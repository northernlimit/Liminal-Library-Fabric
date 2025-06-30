package net.ludocrypt.limlib.api.effects.sky;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

/**
 * A Sky effects controller
 * <p>
 * This is a completely empty, default setting version of
 * {@link StaticDimensionEffects}
 */
public class EmptyDimensionEffects extends StaticDimensionEffects {

	public static final MapCodec<EmptyDimensionEffects> CODEC = RecordCodecBuilder
		.mapCodec((instance) -> instance.stable(new EmptyDimensionEffects()));

	public EmptyDimensionEffects() {
		super(Optional.empty(), false, "NONE", false, false, false, 1.0F);
	}

	@Override
	public MapCodec<? extends LiminalDimensionEffects> getCodec() {
		return CODEC;
	}

}
