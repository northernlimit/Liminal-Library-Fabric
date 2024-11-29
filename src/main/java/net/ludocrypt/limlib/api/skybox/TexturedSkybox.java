package net.ludocrypt.limlib.api.skybox;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

public class TexturedSkybox extends Skybox {

	public static final MapCodec<TexturedSkybox> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
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
	public void renderSky(WorldRenderer worldRenderer, MinecraftClient client, MatrixStack matrices, float tickDelta) {
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
		Tessellator tessellator = Tessellator.getInstance();

		int r = 255;
		int g = 255;
		int b = 255;
		int a = 255;

		RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

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

			RenderSystem.setShaderTexture(0, Identifier.of(identifier.toString() + "_" + i + ".png"));
			BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).texture(0.0F, 0.0F).color(r, g, b, a);
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).texture(0.0F, 1.0F).color(r, g, b, a);
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).texture(1.0F, 1.0F).color(r, g, b, a);
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).texture(1.0F, 0.0F).color(r, g, b, a);
			BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
			matrices.pop();

		}

		RenderSystem.depthMask(true);
		RenderSystem.disableBlend();
	}

	@Override
	public MapCodec<? extends Skybox> getCodec() {
		return CODEC;
	}

}
