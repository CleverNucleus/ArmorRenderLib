package com.github.clevernucleus.armorrenderlib.impl;

import net.minecraft.util.Identifier;

public interface ArmorTextureCache {
	Identifier getOrCache(final String path);
}
