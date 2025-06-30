package net.ludocrypt.limlib.impl.mixin.client;

import net.ludocrypt.limlib.api.effects.LookupGrabber;
import net.ludocrypt.limlib.api.effects.sky.LiminalDimensionEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {

	@ModifyVariable(method = "getFogColor", at = @At(value = "STORE", ordinal = 3), ordinal = 2)
	private static float limlib$modifySkyColor(float in) {
		MinecraftClient client = MinecraftClient.getInstance();

		Optional<LiminalDimensionEffects> dimensionEffects = LookupGrabber
				.snatch(client.world.getRegistryManager().getOptional(LiminalDimensionEffects.DIMENSION_EFFECTS_KEY).get(),
						RegistryKey.of(LiminalDimensionEffects.DIMENSION_EFFECTS_KEY, client.world.getRegistryKey().getValue()));

        return dimensionEffects.map(LiminalDimensionEffects::getSkyShading).orElse(in);

    }
}
