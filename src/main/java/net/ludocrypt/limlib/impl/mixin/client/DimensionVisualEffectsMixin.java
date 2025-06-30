package net.ludocrypt.limlib.impl.mixin.client;

import net.ludocrypt.limlib.api.effects.LookupGrabber;
import net.ludocrypt.limlib.api.effects.sky.LiminalDimensionEffects;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(DimensionEffects.class)
public class DimensionVisualEffectsMixin {

	@Inject(method = "byDimensionType", at = @At("HEAD"), cancellable = true)
	private static void limlib$byDimensionType(DimensionType dimensionType,
			CallbackInfoReturnable<DimensionEffects> ci) {
		Optional<LiminalDimensionEffects> dimensionEffects = LookupGrabber
			.snatch(LiminalDimensionEffects.MIXIN_WORLD_LOOKUP.get(),
				RegistryKey.of(LiminalDimensionEffects.DIMENSION_EFFECTS_KEY, dimensionType.effects()));

        dimensionEffects.ifPresent(lDimensionEffects -> ci.setReturnValue(lDimensionEffects.getDimensionEffects()));
	}
}
