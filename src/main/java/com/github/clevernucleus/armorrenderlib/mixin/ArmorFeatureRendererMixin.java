package com.github.clevernucleus.armorrenderlib.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.github.clevernucleus.armorrenderlib.impl.ArmorTextureCache;

import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.util.Identifier;

@Mixin(value = ArmorFeatureRenderer.class, priority = 900)
abstract class ArmorFeatureRendererMixin implements ArmorTextureCache {
	
	@Shadow
	@Final
	private static Map<String, Identifier> ARMOR_TEXTURE_CACHE;
	
	@Override
	public Identifier getOrCache(final String path) {
		return ARMOR_TEXTURE_CACHE.computeIfAbsent(path, Identifier::new);
	}
}
