package com.github.clevernucleus.armorrenderlib.impl;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public interface ArmorTextureCache<A> {
	A getArmorCustom(EquipmentSlot slot);
	
	Identifier getOrCache(final String path);
}
