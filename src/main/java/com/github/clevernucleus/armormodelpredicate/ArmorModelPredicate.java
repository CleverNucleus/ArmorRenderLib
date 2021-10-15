package com.github.clevernucleus.armormodelpredicate;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/**
 * 
 * @author CleverNucleus
 *
 */
@FunctionalInterface
public interface ArmorModelPredicate {
	
	/**
	 * 
	 * @param defaultIdentifier Default armor layer texture -> doesn't work for leather. Uses 
	 * <code>
	 * 	"textures/models/armor/" + material + "_layer_" + (legs ? 2 : 1) + ".png";
	 * </code>
	 * @param entity The entity wearing the armor.
	 * @param itemStack The itemstack representing the armor item.
	 * @param material the armor item's material.
	 * @param legs whether this is legs or not -> upper body (head, chest) or lower body (legs, boots).
	 * @return Provider; this is a functional interface that holds an identifier and rgb colours.
	 */
	Provider accept(final Identifier defaultIdentifier, final LivingEntity entity, final ItemStack itemStack, final ArmorMaterial material, final boolean legs);
}
