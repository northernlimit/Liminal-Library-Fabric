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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(DimensionOptionsRegistryHolder.class)
public class WorldDimensionsMixin {

	@Shadow
	@Final
	@Mutable
	private static Set<RegistryKey<DimensionOptions>> VANILLA_KEYS;

	@Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/util/Set;size()I", shift = Shift.BEFORE, ordinal = 0))
	private static void limlib$clinit(CallbackInfo ci) {
		Set<RegistryKey<DimensionOptions>> dimensions = Sets.newHashSet();
		dimensions.addAll(VANILLA_KEYS);
		LimlibWorld.LIMLIB_WORLD
			.getEntrySet()
			.forEach((entry) -> dimensions.add(RegistryKey.of(RegistryKeys.DIMENSION, entry.getKey().getValue())));
		VANILLA_KEYS = dimensions;
	}

}
