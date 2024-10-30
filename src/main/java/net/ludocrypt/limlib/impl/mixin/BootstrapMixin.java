package net.ludocrypt.limlib.impl.mixin;

import net.ludocrypt.limlib.impl.Limlib;
import net.minecraft.Bootstrap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bootstrap.class)
public class BootstrapMixin {

	@Inject(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/command/EntitySelectorOptions;register()V", shift = Shift.BEFORE))
	private static void limlib$initialize(CallbackInfo ci) {
		Limlib.onInitialize();
	}

}
