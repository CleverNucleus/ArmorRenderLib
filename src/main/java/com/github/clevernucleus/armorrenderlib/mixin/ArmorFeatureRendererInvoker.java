package com.github.clevernucleus.armorrenderlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

@Mixin(ArmorFeatureRenderer.class)
public interface ArmorFeatureRendererInvoker<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
	
	@Invoker("setVisible")
	public void invokeSetVisible(A bipedModel, EquipmentSlot slot);
}
