package net.ludocrypt.limlib.impl.mixin.client;

import com.mojang.serialization.Lifecycle;
import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.world.SaveProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(IntegratedServerLoader.class)
public class IntegratedServerLoaderMixin {

    @Redirect(method = "checkBackupAndStart", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SaveProperties;getLifecycle()Lcom/mojang/serialization/Lifecycle;"))
    private Lifecycle shutUpOnStart(SaveProperties instance) {
        return Lifecycle.stable();
    }

    @ModifyVariable(method = "tryLoad", at = @At("HEAD"), argsOnly = true, index = 4)
    private static boolean shutUpOnTryLoad(boolean bypassWarnings) {
        return true;
    }
}
