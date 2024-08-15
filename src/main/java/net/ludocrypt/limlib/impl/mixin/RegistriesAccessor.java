package net.ludocrypt.limlib.impl.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Registries.class)
public interface RegistriesAccessor {

	@Invoker
	static <T> Registry<T> callCreate(RegistryKey<? extends Registry<T>> registryKey, Lifecycle lifecycle,
									  Registries.Initializer<T> bootstrap) {
		throw new UnsupportedOperationException();
	}

}
