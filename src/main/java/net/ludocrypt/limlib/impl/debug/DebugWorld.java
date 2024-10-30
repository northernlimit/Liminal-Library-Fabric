package net.ludocrypt.limlib.impl.debug;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ludocrypt.limlib.api.LimlibRegistrar;
import net.ludocrypt.limlib.api.LimlibRegistryHooks;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.registry.tag.WorldPresetTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.WorldPreset;

import java.util.Map;

public class DebugWorld implements LimlibRegistrar {

	public static final RegistryKey<WorldPreset> DEBUG_KEY = RegistryKey
		.of(RegistryKeys.WORLD_PRESET, new Identifier("limlib", "debug_nbt"));

	@Override
	public void registerHooks() {
		LimlibRegistryHooks.hook(RegistryKeys.WORLD_PRESET, (infoLookup, registryKey, registry) -> registry
            .add(DEBUG_KEY, new WorldPreset(Map
                .of(DimensionOptions.OVERWORLD,
                    new DimensionOptions(
                        infoLookup
                            .getRegistryInfo(RegistryKeys.DIMENSION_TYPE)
                            .get()
                            .entryLookup()
                            .getOrThrow(DimensionTypes.OVERWORLD),
                        new DebugNbtChunkGenerator(
                            infoLookup.getRegistryInfo(RegistryKeys.BIOME).get().entryLookup().getOrThrow(BiomeKeys.THE_VOID))))),
                RegistryEntryInfo.DEFAULT));

		LimlibRegistryHooks.hook(WorldPresetTags.EXTENDED, (manager, tag, json) -> {
			JsonObject obj = json.getAsJsonObject();

			if (obj.has("values")) {
				JsonArray values = obj.get("values").getAsJsonArray();
				values.add(DEBUG_KEY.getValue().toString());
			}

		});
	}

}
