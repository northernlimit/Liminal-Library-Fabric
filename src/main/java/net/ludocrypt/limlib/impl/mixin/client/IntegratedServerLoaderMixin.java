package net.ludocrypt.limlib.impl.mixin.client;

import net.minecraft.server.integrated.IntegratedServerLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(IntegratedServerLoader.class)
public class IntegratedServerLoaderMixin {

    @ModifyVariable(method = "start(Lnet/minecraft/world/level/storage/LevelStorage$Session;Lcom/mojang/serialization/Dynamic;ZZLjava/lang/Runnable;)V", at = @At("HEAD"), argsOnly = true, index = 4)
    private boolean shutUpOnStart(boolean canShowBackupPrompt) {
        return false;
    }

    @ModifyVariable(method = "tryLoad", at = @At("HEAD"), argsOnly = true, index = 4)
    private static boolean shutUpOnTryLoad(boolean bypassWarnings) {
        return true;
    }
}
