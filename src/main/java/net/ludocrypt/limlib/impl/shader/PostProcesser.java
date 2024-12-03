package net.ludocrypt.limlib.impl.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.ludocrypt.limlib.impl.Limlib;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class PostProcesser {

	private final Identifier location;
	protected PostEffectProcessor shader;

	public PostProcesser(Identifier location) {
		this.location = location;
	}

	public void init() {
		try {
			this.release();
            this.shader = parseShader();
		} catch (IOException e) {
			Limlib.LOGGER.error("Could not create screen shader {}", this.location, e);
		}
	}

	protected PostEffectProcessor parseShader()
			throws IOException {
		return MinecraftClient.getInstance().getShaderLoader().loadPostEffect(this.location, DefaultFramebufferSet.MAIN_ONLY);
	}

	public void release() {

		if (this.isInitialized()) {

			try {
				assert this.shader != null;
				MinecraftClient.getInstance().getShaderLoader().close();
				this.shader = null;
			} catch (Exception e) {
				throw new RuntimeException("Failed to release shader: " + this.location, e);
			}

		}

	}

	public void render(Framebuffer framebuffer, ObjectAllocator objectAllocator) {
		try {
			RenderSystem.disableBlend();
			RenderSystem.disableDepthTest();
			RenderSystem.resetTextureMatrix();

			PostEffectProcessor shader = this.parseShader();
			FrameGraphBuilder frameGraphBuilder = new FrameGraphBuilder();

			shader.render(frameGraphBuilder, framebuffer.textureWidth, framebuffer.textureHeight, PostEffectProcessor.FramebufferSet
					.singleton(PostEffectProcessor.MAIN, frameGraphBuilder.createObjectNode("main", framebuffer)));

			frameGraphBuilder.run(objectAllocator);
		} catch (IOException e) {
			Limlib.LOGGER.error("Failed to render post processing shader: {}", this.location);
			e.printStackTrace();
		}
	}

	public boolean isInitialized() {
		return this.shader != null;
	}

	public PostEffectProcessor getShader() {
		return shader;
	}

	public void setUniform(String name, float value) {
		shader.setUniforms(name, value);
	}
}
