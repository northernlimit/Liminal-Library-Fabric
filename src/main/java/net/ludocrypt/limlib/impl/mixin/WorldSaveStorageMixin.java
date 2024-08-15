package net.ludocrypt.limlib.impl.mixin;

import com.google.common.collect.Maps;
import com.mojang.serialization.Dynamic;
import net.ludocrypt.limlib.api.LimlibWorld;
import net.ludocrypt.limlib.api.LimlibWorld.RegistryProvider;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.*;
import net.minecraft.resource.DataConfiguration;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;
import java.util.Map.Entry;

@Mixin(LevelStorage.class)
public class WorldSaveStorageMixin {

	@ModifyVariable(method = "Lnet/minecraft/world/level/storage/LevelStorage;parseSaveProperties(Lcom/mojang/serialization/Dynamic;Lnet/minecraft/resource/DataConfiguration;Lnet/minecraft/registry/Registry;Lnet/minecraft/registry/DynamicRegistryManager$Immutable;)Lnet/minecraft/world/level/storage/ParsedSaveProperties;", at = @At(value = "STORE"), ordinal = 2)
	private static <T> Dynamic<T> limlib$readGeneratorProperties$datafix(Dynamic<T> in, Dynamic<?> levelData,
			DataConfiguration featureAndDataSettings, Registry<DimensionOptions> registry,
			DynamicRegistryManager.Immutable frozen) {
		Dynamic<T> dynamic = in;

		for (Entry<RegistryKey<LimlibWorld>, LimlibWorld> entry : LimlibWorld.LIMLIB_WORLD.getEntrySet()) {
			dynamic = limlib$addDimension(entry.getKey(), entry.getValue(), dynamic, frozen);
		}

		return dynamic;
	}

	@Unique
	@SuppressWarnings("unchecked")
	private static <T> Dynamic<T> limlib$addDimension(RegistryKey<LimlibWorld> key, LimlibWorld world, Dynamic<T> in,
			DynamicRegistryManager registryManager) {
		Dynamic<T> dimensions = in.get("dimensions").orElseEmptyMap();

		if (!dimensions.get(key.getValue().toString()).result().isPresent()) {
			Map<Dynamic<T>, Dynamic<T>> dimensionsMap = Maps.newHashMap(dimensions.getMapValues().result().get());

			dimensionsMap
				.put(dimensions.createString(key.getValue().toString()),
					new Dynamic<T>(dimensions.getOps(),
						(T) DimensionOptions.CODEC
							.encodeStart(RegistryOps.of(NbtOps.INSTANCE, registryManager),
								world.getDimensionOptionsSupplier().apply(new RegistryProvider() {

									@Override
									public <Q> RegistryEntryLookup<Q> get(RegistryKey<Registry<Q>> key) {
										return registryManager.getOptionalWrapper(key).get();
									}

								}))
							.result()
							.get()));
			return in.set("dimensions", in.createMap(dimensionsMap));
		}

		return in;
	}

}
