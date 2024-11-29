package net.ludocrypt.limlib.impl.mixin;

import net.ludocrypt.limlib.api.world.chunk.LiminalChunkGenerator;
import net.minecraft.util.collection.BoundedRegionArray;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(ChunkGenerating.class)
public class ChunkGeneratingMixin {

	@Inject(method = "populateNoise", at = @At("HEAD"), cancellable = true)
	private static void limlib$liminalChunkGenerator(ChunkGenerationContext context, ChunkGenerationStep step,
													 BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk,
													 CallbackInfoReturnable<CompletableFuture<Chunk>> ci) {
		if (context.generator() instanceof LiminalChunkGenerator limChunkGen) {
			ChunkRegion chunkRegion = new ChunkRegion(context.world(), chunks, step, chunk);
			ci
					.setReturnValue(limChunkGen
							.populateNoise(chunkRegion, context, chunks, chunk)
							.thenApply(populated -> {

								if (populated instanceof ProtoChunk protoChunk) {
									BelowZeroRetrogen belowZeroRetrogen = protoChunk.getBelowZeroRetrogen();

									if (belowZeroRetrogen != null) {
										BelowZeroRetrogen.replaceOldBedrock(protoChunk);

										if (belowZeroRetrogen.hasMissingBedrock()) {
											belowZeroRetrogen.fillColumnsWithAirIfMissingBedrock(protoChunk);
										}

									}

								}

								return populated;
							}));
		}
	}

}
