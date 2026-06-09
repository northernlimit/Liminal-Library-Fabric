package net.ludocrypt.limlib.api.effects.post;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Pool;
import net.minecraft.util.Identifier;

public class StaticPostEffect extends PostEffect {

	public static final MapCodec<StaticPostEffect> CODEC = RecordCodecBuilder.mapCodec((instance) ->
			instance.group(Identifier.CODEC.fieldOf("shader_name").stable().forGetter(
					(postEffect) -> postEffect.shaderName)).apply(instance, instance.stable(StaticPostEffect::new)));

	private final Identifier shaderName;

	public StaticPostEffect(Identifier shaderLocation) {
		this.shaderName = shaderLocation;
	}

	@Override
	public MapCodec<? extends PostEffect> getCodec() {
		return CODEC;
	}

	@Override
	public boolean shouldRender() {
		return true;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void beforeRender() {
		RenderSystem.disableBlend();
		RenderSystem.disableDepthTest();
		RenderSystem.resetTextureMatrix();
	}

	@SuppressWarnings("deprecation")
	@Override
	@Environment(EnvType.CLIENT)
	public void render(PostEffectProcessor postEffectProcessor, Framebuffer framebuffer,
					   Pool pool, RenderTickCounter tickCounter, boolean tick) {
		postEffectProcessor.render(framebuffer, pool);
	}

	@Override
	public Identifier getShaderLocation() {
		return Identifier.of(shaderName.getNamespace(), shaderName.getPath());
	}

}
