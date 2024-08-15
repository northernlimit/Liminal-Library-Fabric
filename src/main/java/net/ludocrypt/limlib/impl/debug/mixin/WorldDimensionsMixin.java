package net.ludocrypt.limlib.impl.debug.mixin;

import net.ludocrypt.limlib.impl.debug.DebugNbtChunkGenerator;
import net.minecraft.world.dimension.DimensionOptionsRegistryHolder;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionOptionsRegistryHolder.class)
public abstract class WorldDimensionsMixin {

	@Inject(method = "isDebug", at = @At("HEAD"), cancellable = true)
	public void limlib$isDebug(CallbackInfoReturnable<Boolean> ci) {

		if (this.getChunkGenerator() instanceof DebugNbtChunkGenerator) {
			ci.setReturnValue(true);
		}

	}

	@Shadow
	public abstract ChunkGenerator getChunkGenerator();

}
