package net.ludocrypt.limlib.impl.access;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface TagGroupLoaderAccess<O> {

	Optional<RegistryKey<? extends Registry<O>>> getRegistryKey();

	void setRegistryKey(@Nullable RegistryKey<? extends Registry<O>> key);

}
