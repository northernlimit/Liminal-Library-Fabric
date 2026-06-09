package net.ludocrypt.limlib.impl.mixin.client;

import net.ludocrypt.limlib.api.effects.LookupGrabber;
import net.ludocrypt.limlib.api.effects.sound.SoundEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow
	public ClientPlayerEntity player;

	@Shadow
	public ClientWorld world;

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

}
