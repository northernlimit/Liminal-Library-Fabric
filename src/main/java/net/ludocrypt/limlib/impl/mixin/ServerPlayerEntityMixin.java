package net.ludocrypt.limlib.impl.mixin;

import com.mojang.authlib.GameProfile;
import net.ludocrypt.limlib.api.LimlibTravelling;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

	@Shadow
	public abstract void playSoundToPlayer(SoundEvent sound, SoundCategory category, float volume, float pitch);

	public ServerPlayerEntityMixin(World world, BlockPos pos, float f, GameProfile gameProfile) {
		super(world, pos, f, gameProfile);
	}

	@Inject(method = "teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/server/network/ServerPlayerEntity;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;sendStatusEffects(Lnet/minecraft/server/network/ServerPlayerEntity;)V", shift = Shift.AFTER))
	public void limlib$moveToWorld(TeleportTarget teleportTarget, CallbackInfoReturnable<ServerPlayerEntity> cir) {

		if (LimlibTravelling.travelingSound != null) {
			this
				.playSoundToPlayer(LimlibTravelling.travelingSound, SoundCategory.AMBIENT, LimlibTravelling.travelingVolume,
					LimlibTravelling.travelingPitch);
		}

	}

}
