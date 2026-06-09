package net.ludocrypt.limlib.api.effects.post;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.impl.mixin.RegistriesAccessor;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Pool;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public abstract class PostEffect {

	public static final RegistryKey<Registry<MapCodec<? extends PostEffect>>> POST_EFFECT_CODEC_KEY = RegistryKey
		.ofRegistry(Identifier.of("limlib/codec/limlib_post"));
	public static final Registry<MapCodec<? extends PostEffect>> POST_EFFECT_CODEC = RegistriesAccessor
		.callCreate(POST_EFFECT_CODEC_KEY, (registry) -> StaticPostEffect.CODEC);
	public static final Codec<PostEffect> CODEC = POST_EFFECT_CODEC
		.getCodec()
		.dispatchStable(PostEffect::getCodec, Function.identity());
	public static final RegistryKey<Registry<PostEffect>> POST_EFFECT_KEY = RegistryKey
		.ofRegistry(Identifier.of("limlib/limlib_post"));

	public abstract MapCodec<? extends PostEffect> getCodec();

	public abstract boolean shouldRender();

	public abstract void beforeRender();

	@Environment(EnvType.CLIENT)
	public abstract void render(PostEffectProcessor postEffectProcessor, Framebuffer framebuffer,
								Pool pool, RenderTickCounter tickCounter, boolean tick);

	public abstract Identifier getShaderLocation();

}
