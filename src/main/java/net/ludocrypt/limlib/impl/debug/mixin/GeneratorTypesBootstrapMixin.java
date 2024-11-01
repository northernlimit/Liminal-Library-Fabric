package net.ludocrypt.limlib.impl.debug.mixin;

import net.ludocrypt.limlib.impl.debug.DebugNbtChunkGenerator;
import net.ludocrypt.limlib.impl.debug.DebugWorld;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.WorldPreset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/world/gen/WorldPresets$Registrar")
public abstract class GeneratorTypesBootstrapMixin {

	@Shadow
	private RegistryEntryLookup<Biome> biomeLookup;
	@Shadow
	private RegistryEntry<DimensionType> overworldDimensionType;

	@Shadow
	protected abstract void register(RegistryKey<WorldPreset> key, DimensionOptions dimensionOptions);

	@Inject(method = "bootstrap()V", at = @At("TAIL"))
	public void limlib$addDimensionOpions(CallbackInfo ci) {
		this.register(DebugWorld.DEBUG_KEY, new DimensionOptions(this.overworldDimensionType,
				new DebugNbtChunkGenerator(this.biomeLookup.getOrThrow(BiomeKeys.THE_VOID))));
	}

}
