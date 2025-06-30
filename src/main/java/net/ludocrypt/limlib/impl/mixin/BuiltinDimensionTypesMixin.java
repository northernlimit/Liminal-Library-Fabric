package net.ludocrypt.limlib.impl.mixin;

import net.ludocrypt.limlib.api.LimlibWorld;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypeRegistrar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DimensionTypeRegistrar.class)
public class BuiltinDimensionTypesMixin {

	@Inject(method = "bootstrap(Lnet/minecraft/registry/Registerable;)V", at = @At("RETURN"))
	private static void limlib$initAndGetDefault(Registerable<DimensionType> bootstrapContext, CallbackInfo ci) {
		LimlibWorld.LIMLIB_WORLD
			.getEntrySet()
			.forEach((entry) -> bootstrapContext
				.register(RegistryKey.of(RegistryKeys.DIMENSION_TYPE, entry.getKey().getValue()),
					entry.getValue().dimensionTypeSupplier().get()));
	}

}
