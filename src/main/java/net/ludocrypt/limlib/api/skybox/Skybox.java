package net.ludocrypt.limlib.api.skybox;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.limlib.impl.mixin.RegistriesAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.util.function.Function;

public abstract class Skybox {

	public static final RegistryKey<Registry<MapCodec<? extends Skybox>>> SKYBOX_CODEC_KEY = RegistryKey
		.ofRegistry(new Identifier("limlib/codec/skybox"));
	public static final Registry<MapCodec<? extends Skybox>> SKYBOX_CODEC = RegistriesAccessor
		.callCreate(SKYBOX_CODEC_KEY, (registry) -> TexturedSkybox.CODEC);
	public static final Codec<Skybox> CODEC = SKYBOX_CODEC.getCodec().dispatchStable(Skybox::getCodec, Function.identity());
	public static final RegistryKey<Registry<Skybox>> SKYBOX_KEY = RegistryKey.ofRegistry(new Identifier("limlib/skybox"));

	public abstract MapCodec<? extends Skybox> getCodec();

	@Environment(EnvType.CLIENT)
	public abstract void renderSky(WorldRenderer worldRenderer, MinecraftClient client, MatrixStack matrices,
			Matrix4f projectionMatrix, float tickDelta);

}
