package com.github.clevernucleus.armormodelpredicate.impl;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ArmorModelPredicate {
	boolean test(final ItemStack itemStack, final LivingEntity entity);
}
