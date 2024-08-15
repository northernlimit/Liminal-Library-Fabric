package net.ludocrypt.limlib.impl.access;

import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public interface SoundSystemAccess {

	public void stopSoundsAtPosition(double x, double y, double z, @Nullable Identifier id,
			@Nullable SoundCategory category);

	public static SoundSystemAccess get(Object obj) {
		return (SoundSystemAccess) obj;
	}

}
