package net.ludocrypt.limlib.impl.mixin;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Registries.class)
public interface RegistriesAccessor {

	@Invoker
	static <T> Registry<T> callCreate(RegistryKey<? extends Registry<T>> registryKey, Registries.Initializer<T> initializer) {
		throw new UnsupportedOperationException();
	}

}
