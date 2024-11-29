package net.ludocrypt.limlib.impl.mixin.client;

import net.ludocrypt.limlib.api.effects.LookupGrabber;
import net.ludocrypt.limlib.api.effects.post.PostEffect;
import net.ludocrypt.limlib.impl.access.GameRendererAccessor;
import net.ludocrypt.limlib.impl.shader.PostProcesserManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Pool;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements GameRendererAccessor {

	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	@Final
	private Pool pool;


	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V", shift = Shift.AFTER))
	private void limlib$render(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {

		Optional<PostEffect> optionalPostEffect = LookupGrabber
				.snatch(client.world.getRegistryManager().getOptional(PostEffect.POST_EFFECT_KEY).get(),
						RegistryKey.of(PostEffect.POST_EFFECT_KEY, client.world.getRegistryKey().getValue()));

		if (optionalPostEffect.isPresent()) {
			PostEffect postEffect = optionalPostEffect.get();

			if (postEffect.shouldRender()) {
				postEffect.beforeRender();
				//memoizedShaders.apply(postEffect.getShaderLocation()).render(client.getFramebuffer(), this.pool);
				PostProcesserManager.INSTANCE.find(postEffect.getShaderLocation()).render(client.getFramebuffer(), this.pool);
			}

		}

	}



	@Override
	public Pool getPool() {
		return this.pool;
	}

}
