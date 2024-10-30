package net.ludocrypt.limlib.impl.mixin.client;

import net.ludocrypt.limlib.api.effects.LookupGrabber;
import net.ludocrypt.limlib.api.skybox.Skybox;
import net.ludocrypt.limlib.impl.bridge.IrisBridge;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.RegistryKey;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = WorldRenderer.class, priority = 950)
public abstract class WorldRendererBeforeMixin {

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clear(IZ)V", ordinal = 0, shift = At.Shift.AFTER, remap = false))
	private void limlib$render$clear(float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera,
									 GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager,
									 Matrix4f matrix4f, Matrix4f positionMatrix, CallbackInfo ci) {

		if (IrisBridge.IRIS_LOADED && IrisBridge.areShadersInUse()) return;

		MinecraftClient client = MinecraftClient.getInstance();

		Optional<Skybox> sky = LookupGrabber
				.snatch(client.world.getRegistryManager().getOptionalWrapper(Skybox.SKYBOX_KEY).get(),
						RegistryKey.of(Skybox.SKYBOX_KEY, client.world.getRegistryKey().getValue()));

		if (sky.isPresent()) {
			MatrixStack matrixStack = new MatrixStack();
			matrixStack.multiplyPositionMatrix(matrix4f);
			sky.get().renderSky(((WorldRenderer) (Object) this), client, matrixStack, positionMatrix, tickDelta);
		}

	}

}
