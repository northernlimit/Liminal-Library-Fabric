package net.ludocrypt.limlib.api.effects.sky;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

/**
 * A Sky effects controller
 * <p>
 * This is a completely empty, default setting version of
 * {@link StaticDimensionEffects}
 */
public class EmptyDimensionEffects extends StaticDimensionEffects {

	public static final Codec<EmptyDimensionEffects> CODEC = RecordCodecBuilder
		.create((instance) -> instance.stable(new EmptyDimensionEffects()));

	public EmptyDimensionEffects() {
		super(Optional.empty(), false, "NONE", false, false, false, 1.0F);
	}

	@Override
	public Codec<? extends LDimensionEffects> getCodec() {
		return CODEC;
	}

}
