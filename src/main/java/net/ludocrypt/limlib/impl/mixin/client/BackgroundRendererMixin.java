package net.ludocrypt.limlib.impl.mixin.client;

import net.ludocrypt.limlib.api.effects.LookupGrabber;
import net.ludocrypt.limlib.api.effects.sky.LDimensionEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {

	@ModifyVariable(method = "render", at = @At(value = "STORE", ordinal = 3), ordinal = 2)
	private static float limlib$modifySkyColor(float in) {
		MinecraftClient client = MinecraftClient.getInstance();

		Optional<LDimensionEffects> dimensionEffects = LookupGrabber
			.snatch(client.world.getRegistryManager().getOptionalWrapper(LDimensionEffects.DIMENSION_EFFECTS_KEY).get(),
				RegistryKey.of(LDimensionEffects.DIMENSION_EFFECTS_KEY, client.world.getRegistryKey().getValue()));

		if (dimensionEffects.isPresent()) {
			return dimensionEffects.get().getSkyShading();
		}

		return in;
	}

}
