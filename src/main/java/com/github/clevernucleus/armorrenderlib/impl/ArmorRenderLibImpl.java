package com.github.clevernucleus.armorrenderlib.impl;

import com.github.clevernucleus.armorrenderlib.api.ArmorRenderLayer;
import com.github.clevernucleus.armorrenderlib.mixin.ArmorFeatureRendererInvoker;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.impl.client.rendering.ArmorRendererRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

public final class ArmorRenderLibImpl {
	private static final Multimap<Item, ArmorRenderLayer> RENDERERS = ArrayListMultimap.create();
	
	@SuppressWarnings("unchecked")
	private static <T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
		if(!(stack.getItem() instanceof ArmorItem)) return;
		ArmorItem item = (ArmorItem)stack.getItem();
		
		if(item.getSlotType() != slot) return;
		var entityRenderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(entity);
		ArmorFeatureRenderer<T, M, A> armorFeatureRenderer = (ArmorFeatureRenderer<T, M, A>)((FeatureRendererAccessor<T, M>)entityRenderer).getFeatureRenderer(ArmorFeatureRenderer.class);
		A model = ((ArmorFeatureRendererInvoker<T, M, A>)armorFeatureRenderer).invokeGetArmor(slot);
		contextModel.setAttributes((BipedEntityModel<LivingEntity>)model);
		((ArmorFeatureRendererInvoker<T, M, A>)armorFeatureRenderer).invokeSetVisible(model, slot);
		
		for(ArmorRenderLayer layer : RENDERERS.get(item)) {
			layer.render(stack, entity, slot).from((path, color, glint) -> {
				float red = (float)(color >> 16 & 0xFF) / 255.0F;
	            float green = (float)(color >> 8 & 0xFF) / 255.0F;
	            float blue = (float)(color & 0xFF) / 255.0F;
	            
	            VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(((ArmorTextureCache)armorFeatureRenderer).getOrCache(path)), false, glint);
	            model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);
			});
		}
	}
	
	public static void register(ArmorRenderLayer layer, ItemConvertible ... items) {
		if(items == null || items.length == 0) return;
		
		for(ItemConvertible item : items) {
			if(ArmorRendererRegistryImpl.get(item.asItem()) == null) {
				ArmorRenderer.register(ArmorRenderLibImpl::render, item);
			}
			
			RENDERERS.put(item.asItem(), layer);
		}
	}
}
