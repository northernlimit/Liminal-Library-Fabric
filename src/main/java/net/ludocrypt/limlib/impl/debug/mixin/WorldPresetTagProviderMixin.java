package net.ludocrypt.limlib.impl.debug.mixin;

import net.ludocrypt.limlib.impl.debug.DebugWorld;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.data.server.tag.vanilla.VanillaWorldPresetTagProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.WorldPresetTags;
import net.minecraft.world.gen.WorldPreset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(VanillaWorldPresetTagProvider.class)
public abstract class WorldPresetTagProviderMixin extends TagProvider<WorldPreset> {

	protected WorldPresetTagProviderMixin(DataOutput output, RegistryKey<? extends Registry<WorldPreset>> key,
										  CompletableFuture<RegistryWrapper.WrapperLookup> lookupProvider) {
		super(output, key, lookupProvider);
	}

	@Inject(method = "configure", at = @At("TAIL"))
	protected void limlib$configure(RegistryWrapper.WrapperLookup lookup, CallbackInfo ci) {
		this.getOrCreateTagBuilder(WorldPresetTags.EXTENDED).add(DebugWorld.DEBUG_KEY);
	}

}
