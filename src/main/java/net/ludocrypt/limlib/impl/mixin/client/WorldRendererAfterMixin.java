package net.ludocrypt.limlib.impl.mixin.client;

import net.ludocrypt.limlib.api.effects.LookupGrabber;
import net.ludocrypt.limlib.api.skybox.Skybox;
import net.ludocrypt.limlib.impl.bridge.IrisBridge;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.RegistryKey;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = WorldRenderer.class, priority = 1050)
public abstract class WorldRendererAfterMixin {

	@Inject(method = "render", at = @At(value = "RETURN", shift = At.Shift.BEFORE))
	private void limlib$render$clear(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline,
									 Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager,
									 Matrix4f matrix4f, Matrix4f projectionMatrix, CallbackInfo ci) {

		if (IrisBridge.IRIS_LOADED) {

			if (!IrisBridge.areShadersInUse()) {
				MinecraftClient client = MinecraftClient.getInstance();

				Optional<Skybox> sky = LookupGrabber
						.snatch(client.world.getRegistryManager().getOptional(Skybox.SKYBOX_KEY).get(),
								RegistryKey.of(Skybox.SKYBOX_KEY, client.world.getRegistryKey().getValue()));

				if (sky.isPresent()) {
					MatrixStack matrixStack = new MatrixStack();
					matrixStack.multiplyPositionMatrix(matrix4f);
					sky.get().renderSky(((WorldRenderer) (Object) this), client, matrixStack, tickCounter.getTickDelta(true));
				}

			}

		}

	}

}
