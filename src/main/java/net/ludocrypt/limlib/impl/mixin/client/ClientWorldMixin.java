package net.ludocrypt.limlib.impl.mixin.client;

import net.ludocrypt.limlib.api.effects.sky.LiminalDimensionEffects;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
	protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef,
							   DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry,
							   boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
		super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
	}


	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/DimensionEffects;byDimensionType(Lnet/minecraft/world/dimension/DimensionType;)Lnet/minecraft/client/render/DimensionEffects;", shift = Shift.BEFORE))
	private void limlib$init(ClientPlayNetworkHandler networkHandler, ClientWorld.Properties properties,
							 RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimensionType,
							 int loadDistance, int simulationDistance, WorldRenderer worldRenderer,
							 boolean debugWorld, long seed, int seaLevel, CallbackInfo ci) {

		LiminalDimensionEffects.MIXIN_WORLD_LOOKUP
				.set(this.getRegistryManager().getOptional(LiminalDimensionEffects.DIMENSION_EFFECTS_KEY).get());
	}

}
