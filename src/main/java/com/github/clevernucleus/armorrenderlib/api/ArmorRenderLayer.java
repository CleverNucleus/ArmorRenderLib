package com.github.clevernucleus.armorrenderlib.api;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ArmorRenderLayer {
	
	/**
	 * 
	 * @param itemStack
	 * @param livingEntity
	 * @param slot
	 * @return
	 */
	ArmorRenderProvider render(final ItemStack itemStack, final LivingEntity livingEntity, final EquipmentSlot slot);
}
