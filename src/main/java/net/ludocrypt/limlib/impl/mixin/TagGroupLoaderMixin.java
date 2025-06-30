package net.ludocrypt.limlib.impl.mixin;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import net.ludocrypt.limlib.api.LimlibRegistryHooks;
import net.ludocrypt.limlib.api.LimlibRegistryHooks.LimlibJsonTagHook;
import net.ludocrypt.limlib.impl.access.TagGroupLoaderAccess;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(TagGroupLoader.class)
public class TagGroupLoaderMixin<O> implements TagGroupLoaderAccess<O> {

	@Unique
	Optional<RegistryKey<? extends Registry<O>>> associatedRegistryKey = Optional.empty();

	@SuppressWarnings("unchecked")
	@Inject(method = "loadTags(Lnet/minecraft/resource/ResourceManager;)Ljava/util/Map;", at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;", shift = Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private void limlib$loadTags(ResourceManager manager,
			CallbackInfoReturnable<Map<Identifier, List<TagGroupLoader.TrackedEntry>>> ci,
			Map<Identifier, List<TagGroupLoader.TrackedEntry>> map, ResourceFinder resourceFileNamespace,
			Iterator<Map.Entry<Identifier, List<Resource>>> itr, Map.Entry<Identifier, List<Resource>> entry,
			Identifier identifier, Identifier identifier2, Iterator<Resource> ritr, Resource resource, Reader reader,
			JsonElement jsonElement) {

		if (this.getRegistryKey().isPresent()) {
			TagKey<O> key = TagKey.of(this.getRegistryKey().get(), identifier2);
			LimlibRegistryHooks.TAG_JSON_HOOKS
				.getOrDefault(key, Sets.newHashSet())
				.forEach((hook) -> ((LimlibJsonTagHook<O>) hook).register(manager, key, jsonElement));
		}

	}

	@Override
	public Optional<RegistryKey<? extends Registry<O>>> getRegistryKey() {
		return this.associatedRegistryKey;
	}

	@Override
	public void setRegistryKey(@Nullable RegistryKey<? extends Registry<O>> key) {
		this.associatedRegistryKey = Optional.ofNullable(key);
	}

}
