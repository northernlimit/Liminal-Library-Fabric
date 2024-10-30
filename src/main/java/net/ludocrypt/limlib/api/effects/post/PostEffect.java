package net.ludocrypt.limlib.api.effects.post;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.ludocrypt.limlib.impl.mixin.RegistriesAccessor;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public abstract class PostEffect {

	public static final RegistryKey<Registry<MapCodec<? extends PostEffect>>> POST_EFFECT_CODEC_KEY = RegistryKey
		.ofRegistry(new Identifier("limlib/codec/post_effect"));
	public static final Registry<MapCodec<? extends PostEffect>> POST_EFFECT_CODEC = RegistriesAccessor
		.callCreate(POST_EFFECT_CODEC_KEY, (registry) -> StaticPostEffect.CODEC);
	public static final Codec<PostEffect> CODEC = POST_EFFECT_CODEC
		.getCodec()
		.dispatchStable(PostEffect::getCodec, Function.identity());
	public static final RegistryKey<Registry<PostEffect>> POST_EFFECT_KEY = RegistryKey
		.ofRegistry(new Identifier("limlib/post_effect"));

	public abstract MapCodec<? extends PostEffect> getCodec();

	public abstract boolean shouldRender();

	public abstract void beforeRender();

	public abstract Identifier getShaderLocation();

}
