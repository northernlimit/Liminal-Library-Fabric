package net.ludocrypt.limlib.api.skybox;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class TexturedSkybox extends Skybox {

	public static final Codec<TexturedSkybox> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Identifier.CODEC.fieldOf("skybox").stable().forGetter((sky) -> {
			return sky.identifier;
		})).apply(instance, instance.stable(TexturedSkybox::new));
	});

	public final Identifier identifier;

	public TexturedSkybox(Identifier identifier) {
		this.identifier = identifier;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void renderSky(WorldRenderer worldRenderer, MinecraftClient client, MatrixStack matrices,
			Matrix4f projectionMatrix, float tickDelta) {
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());

		Vec3d color = client.world.getSkyColor(client.gameRenderer.getCamera().getPos(), tickDelta).multiply(255);
		int r = (int) Math.floor(color.x);
		int g = (int) Math.floor(color.y);
		int b = (int) Math.floor(color.z);
		int a = 255;
		RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

		for (int i = 0; i < 6; ++i) {
			matrices.push();

			if (i == 0) {
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
			}

			if (i == 1) {
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
			}

			if (i == 2) {
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
			}

			if (i == 3) {
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
			}

			if (i == 4) {
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90.0F));
			}

			Matrix4f matrix4f = matrices.peek().getPositionMatrix();

			RenderSystem.setShaderTexture(0, new Identifier(identifier.toString() + "_" + i + ".png"));
			bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).texture(0.0F, 0.0F).color(r, g, b, a).next();
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).texture(0.0F, 1.0F).color(r, g, b, a).next();
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).texture(1.0F, 1.0F).color(r, g, b, a).next();
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).texture(1.0F, 0.0F).color(r, g, b, a).next();
			BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
			matrices.pop();
		}

		RenderSystem.depthMask(true);
		RenderSystem.disableBlend();
	}

	@Override
	public Codec<? extends Skybox> getCodec() {
		return CODEC;
	}

}
