package net.ludocrypt.limlib.impl.mixin.client;

import net.ludocrypt.limlib.api.effects.sky.LDimensionEffects;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {

	protected ClientWorldMixin(MutableWorldProperties worldProperties, RegistryKey<World> registryKey,
							   DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimension, Supplier<Profiler> profiler,
							   boolean client, boolean debug, long seed, int maxChainedNeighborUpdates) {
		super(worldProperties, registryKey, registryManager, dimension, profiler, client, debug, seed,
			maxChainedNeighborUpdates);
	}

	@Inject(method = "<init>(Lnet/minecraft/client/network/ClientPlayNetworkHandler;Lnet/minecraft/client/world/ClientWorld$Properties;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/registry/entry/RegistryEntry;IILjava/util/function/Supplier;Lnet/minecraft/client/render/WorldRenderer;ZJ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/DimensionEffects;byDimensionType(Lnet/minecraft/world/dimension/DimensionType;)Lnet/minecraft/client/render/DimensionEffects;", shift = Shift.BEFORE))
	private void limlib$init(ClientPlayNetworkHandler netHandler, ClientWorld.Properties clientWorldProperties,
			RegistryKey<World> registryKey, RegistryEntry<DimensionType> dimensionType, int chunkManager, int simulationDistance,
			Supplier<Profiler> profiler, WorldRenderer worldRenderer, boolean debugWorld, long seed, CallbackInfo ci) {

		LDimensionEffects.MIXIN_WORLD_LOOKUP
				.set(this.getRegistryManager().getOptionalWrapper(LDimensionEffects.DIMENSION_EFFECTS_KEY).get());
	}

}
