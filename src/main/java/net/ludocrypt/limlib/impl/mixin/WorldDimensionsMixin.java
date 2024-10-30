package net.ludocrypt.limlib.impl.mixin;

import com.google.common.collect.Sets;
import net.ludocrypt.limlib.api.LimlibWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionOptionsRegistryHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(DimensionOptionsRegistryHolder.class)
public class WorldDimensionsMixin {

	@Shadow
	@Final
	@Mutable
	private static Set<RegistryKey<DimensionOptions>> VANILLA_KEYS;
	static {
		Set<RegistryKey<DimensionOptions>> dimensionOps = Sets.newHashSet();
		dimensionOps.addAll(VANILLA_KEYS);
		LimlibWorld.LIMLIB_WORLD
				.getEntrySet()
				.forEach((entry) -> dimensionOps.add(RegistryKey.of(RegistryKeys.DIMENSION, entry.getKey().getValue())));
		VANILLA_KEYS = dimensionOps;
	}
}
