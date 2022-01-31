package com.github.clevernucleus.armormodelpredicate.impl;

import net.minecraft.util.Identifier;

@FunctionalInterface
public interface ArmorModelProvider {
	void provide(final Identifier texture, final int color);
}
