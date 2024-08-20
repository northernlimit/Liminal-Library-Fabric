package net.ludocrypt.limlib.impl.mixin.client;

import net.ludocrypt.limlib.impl.access.SourceAccessor;
import net.minecraft.client.sound.Source;
import org.lwjgl.openal.AL10;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Source.class)
public class SourceMixin implements SourceAccessor {
    @Shadow
    @Final
    private int pointer;

    @Override
    public int getPointer() {
        return pointer;
    }

    @Inject(method = "setAttenuation", at = @At("RETURN"))
    private void fixLinearAttenuation(float attenuation, CallbackInfo ci) {
        AL10.alSourcef(pointer, AL10.AL_REFERENCE_DISTANCE, attenuation / 2f);
    }
}
