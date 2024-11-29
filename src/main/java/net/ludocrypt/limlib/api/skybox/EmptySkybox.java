package net.ludocrypt.limlib.api.skybox;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public class EmptySkybox extends Skybox {

	public static final MapCodec<EmptySkybox> CODEC = RecordCodecBuilder
		.mapCodec((instance) -> instance.stable(new EmptySkybox()));

	@Override
	@Environment(EnvType.CLIENT)
	public void renderSky(WorldRenderer worldRenderer, MinecraftClient client, MatrixStack matrices, float tickDelta) {
	}

	@Override
	public MapCodec<? extends Skybox> getCodec() {
		return CODEC;
	}

}
