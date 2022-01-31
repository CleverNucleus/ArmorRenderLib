package com.github.clevernucleus.armormodelpredicate.impl;

import java.util.function.Consumer;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface InvertedProvider {
	Consumer<ArmorModelProvider> invert(final ItemStack itemStack, final LivingEntity entity, final boolean legs);
}
