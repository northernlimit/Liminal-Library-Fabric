package net.ludocrypt.limlib.api.effects.post;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

public class StaticPostEffect extends PostEffect {

	public static final MapCodec<StaticPostEffect> CODEC = RecordCodecBuilder.mapCodec((instance) ->
			instance.group(Identifier.CODEC.fieldOf("shader_name").stable().forGetter(
					(postEffect) -> postEffect.shaderName)).apply(instance, instance.stable(StaticPostEffect::new)));

	private final Identifier shaderName;

	public StaticPostEffect(Identifier shaderLocation) {
		this.shaderName = shaderLocation;
	}

	@Override
	public MapCodec<? extends PostEffect> getCodec() {
		return CODEC;
	}

	@Override
	public boolean shouldRender() {
		return true;
	}

	@Override
	public void beforeRender() {

	}

	@Override
	public Identifier getShaderLocation() {
		return Identifier.of(shaderName.getNamespace(), shaderName.getPath());
	}

}
