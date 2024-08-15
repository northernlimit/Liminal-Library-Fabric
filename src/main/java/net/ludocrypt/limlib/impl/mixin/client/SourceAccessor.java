package net.ludocrypt.limlib.impl.mixin.client;

import net.minecraft.client.sound.Source;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Source.class)
public interface SourceAccessor {

	@Accessor
	int getPointer();

}
