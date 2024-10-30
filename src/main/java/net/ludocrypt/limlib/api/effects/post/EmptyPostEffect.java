package net.ludocrypt.limlib.api.effects.post;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

public class EmptyPostEffect extends PostEffect {

	public static final MapCodec<EmptyPostEffect> CODEC = RecordCodecBuilder
		.mapCodec((instance) -> instance.stable(new EmptyPostEffect()));

	@Override
	public MapCodec<? extends PostEffect> getCodec() {
		return CODEC;
	}

	@Override
	public boolean shouldRender() {
		return false;
	}

	@Override
	public void beforeRender() {

	}

	@Override
	public Identifier getShaderLocation() {
		return null;
	}

}
