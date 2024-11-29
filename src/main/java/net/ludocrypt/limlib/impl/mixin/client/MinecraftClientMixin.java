package net.ludocrypt.limlib.impl.mixin.client;

import net.ludocrypt.limlib.api.effects.LookupGrabber;
import net.ludocrypt.limlib.api.effects.sound.SoundEffects;
import net.ludocrypt.limlib.impl.shader.PostProcesserManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow
	public ClientPlayerEntity player;

	@Shadow
	public ClientWorld world;

	@Shadow
	@Final
	private ReloadableResourceManagerImpl resourceManager;

	@Inject(method = "getMusicType", at = @At("HEAD"), cancellable = true)
	private void limlib$getMusic(CallbackInfoReturnable<MusicSound> ci) {

		if (this.player != null) {
			Optional<SoundEffects> soundEffects = LookupGrabber
					.snatch(world.getRegistryManager().getOptional(SoundEffects.SOUND_EFFECTS_KEY).get(),
							RegistryKey.of(SoundEffects.SOUND_EFFECTS_KEY, world.getRegistryKey().getValue()));

			if (soundEffects.isPresent()) {
				Optional<MusicSound> musicSound = soundEffects.get().getMusic();

                musicSound.ifPresent(ci::setReturnValue);

			}

		}

	}

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ReloadableResourceManagerImpl;registerReloader(Lnet/minecraft/resource/ResourceReloader;)V", ordinal = 0))
	private void limlib$init(RunArgs runArgs, CallbackInfo ci) {
		this.resourceManager.registerReloader(PostProcesserManager.INSTANCE);
	}

}
