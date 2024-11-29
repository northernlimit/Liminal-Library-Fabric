package net.ludocrypt.limlib.impl.shader;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public final class PostProcesserManager implements SynchronousResourceReloader {

	public static final PostProcesserManager INSTANCE = new PostProcesserManager();
	public static final Identifier RESOURCE_KEY = Identifier.of("limlib:post_effect");

	private final Map<Identifier, PostProcesser> shaders = new HashMap<>();


	public PostProcesser find(Identifier location) {
		if (shaders.containsKey(location)) {
			return shaders.get(location);
		} else {
			PostProcesser ret = new PostProcesser(location);
			shaders.put(location, ret);
			return ret;
		}
	}

	@Override
	public void reload(ResourceManager manager) {
		for (PostProcesser shader : shaders.values()) {
			shader.init();
		}
	}

}
