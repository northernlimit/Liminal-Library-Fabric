package net.ludocrypt.limlib.api.effects;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;

public class LookupGrabber {

	public static <T> Optional<T> snatch(RegistryWrapper<T> lookup, RegistryKey<T> key) {
		Optional<RegistryEntry.Reference<T>> holderOptional = lookup.getOptional(key);

		if (holderOptional.isPresent()) {
			RegistryEntry.Reference<T> holder = holderOptional.get();
			try {
				T held = holder.value();
				return Optional.of(held);
			} catch (IllegalStateException e) {
				return Optional.empty();
			}
		}

		return Optional.empty();
	}

}
