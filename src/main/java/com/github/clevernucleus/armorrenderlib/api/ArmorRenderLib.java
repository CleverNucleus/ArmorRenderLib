package com.github.clevernucleus.armorrenderlib.api;

import net.minecraft.item.ItemConvertible;

/**
 * Use this to register armor render layers!
 * 
 * @author CleverNucleus
 *
 */
public interface ArmorRenderLib {
	/**
	 * <p>
	 * Registers an armor render layer for the input item(s). The same armor render layer can be registered for multiple items. The same item(s) can have multiple armor render layers. The armor render layer specifies what armor texture should be used, what color should be applied (if any) and whether it has a glint.
	 * </p>
	 * <p>
	 * This is an extension of {@link net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer#render(net.minecraft.client.util.math.MatrixStack, net.minecraft.client.render.VertexConsumerProvider, net.minecraft.item.ItemStack, net.minecraft.entity.LivingEntity, net.minecraft.entity.EquipmentSlot, int, net.minecraft.client.render.entity.model.BipedEntityModel)}.
	 * </p>
	 * 
	 * @param layer
	 * @param items
	 */
	static void register(ArmorRenderLayer layer, ItemConvertible ... items) {
		com.github.clevernucleus.armorrenderlib.impl.ArmorRenderLibImpl.register(layer, items);
	}
}
