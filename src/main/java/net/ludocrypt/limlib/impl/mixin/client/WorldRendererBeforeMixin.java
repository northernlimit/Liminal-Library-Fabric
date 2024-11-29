package net.ludocrypt.limlib.impl.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.ludocrypt.limlib.api.effects.LookupGrabber;
import net.ludocrypt.limlib.api.skybox.Skybox;
import net.ludocrypt.limlib.impl.bridge.IrisBridge;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.profiler.Profiler;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(value = WorldRenderer.class, priority = 950)
public abstract class WorldRendererBeforeMixin {

	@Shadow
	@Final
	private DefaultFramebufferSet framebufferSet;

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderMain(Lnet/minecraft/client/render/FrameGraphBuilder;Lnet/minecraft/client/render/Frustum;Lnet/minecraft/client/render/Camera;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lnet/minecraft/client/render/Fog;ZZLnet/minecraft/client/render/RenderTickCounter;Lnet/minecraft/util/profiler/Profiler;)V"))
	private void limlib$render$clear(WorldRenderer instance, FrameGraphBuilder frameGraphBuilder, Frustum frustum,
									 Camera camera, Matrix4f positionMatrix, Matrix4f projectionMatrix, Fog fog,
									 boolean renderBlockOutline, boolean bl3, RenderTickCounter tickCounter,
									 Profiler profiler, Operation<Void> original) {

		if (!IrisBridge.IRIS_LOADED && !IrisBridge.areShadersInUse()) {

			MinecraftClient client = MinecraftClient.getInstance();

			Optional<Skybox> sky = LookupGrabber
					.snatch(client.world.getRegistryManager().getOptional(Skybox.SKYBOX_KEY).get(),
							RegistryKey.of(Skybox.SKYBOX_KEY, client.world.getRegistryKey().getValue()));

			if (sky.isPresent()) {
				RenderPass renderPass = frameGraphBuilder.createPass("skybox_pass");
				this.framebufferSet.mainFramebuffer = renderPass.transfer(this.framebufferSet.mainFramebuffer);
				renderPass.setRenderer(() -> {
					RenderPhase.MAIN_TARGET.startDrawing();
					MatrixStack matrixStack = new MatrixStack();
					sky.get().renderSky(((WorldRenderer) (Object) this), client, matrixStack, tickCounter.getTickDelta(true));

				});
			}
		}

		original.call(instance, frameGraphBuilder, frustum, camera, positionMatrix, projectionMatrix, fog, renderBlockOutline, bl3, tickCounter, profiler);
	}

}
