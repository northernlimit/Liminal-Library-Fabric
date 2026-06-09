package net.ludocrypt.limlib.api.effects.post;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Pool;
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

	@Environment(EnvType.CLIENT)
	@Override
	public void render(PostEffectProcessor postEffectProcessor, Framebuffer framebuffer,
					   Pool pool, RenderTickCounter tickCounter, boolean tick) {

	}

	@Override
	public Identifier getShaderLocation() {
		return null;
	}

}
