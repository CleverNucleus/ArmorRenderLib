package com.github.clevernucleus.armormodelpredicate.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.github.clevernucleus.armormodelpredicate.api.ArmorModelPredicateProviderRegistry;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Mixin(ArmorFeatureRenderer.class)
abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
	private ArmorFeatureRendererMixin(FeatureRendererContext<T, M> context, A leggingsModel, A bodyModel) { super(context); }
	
	private void amp_renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Identifier texture, boolean usesSecondLayer, A model, float red, float green, float blue) {
		VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(texture), false, usesSecondLayer);
        ((AnimalModel<T>)model).render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);
	}
	
	@Inject(method = "renderArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;hasGlint()Z", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void onRenderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo info, ItemStack itemStack, ArmorItem armorItem, boolean bl) {
		boolean bl2 = itemStack.hasGlint();
		boolean check = true;
		
		for(var function : ArmorModelPredicateProviderRegistry.providers()) {
			if(function.invert((predicate, provider) -> {
				if(predicate.test(itemStack, entity)) {
					provider.invert(itemStack, entity, bl).accept((texture, color) -> {
						float r = (float)(color >> 16 & 0xFF) / 255.0F;
						float g = (float)(color >> 8 & 0xFF) / 255.0F;
						float b = (float)(color & 0xFF) / 255.0F;
						this.amp_renderArmorParts(matrices, vertexConsumers, light, texture, bl2, model, r, g, b);
					});
					
					return true;
				} else {
					return false;
				}
			})) {
				check = false;
			}
		}
		
		if(check) {
			this.amp_renderArmorParts(matrices, vertexConsumers, light, new Identifier("minecraft:textures/models/armor/" + armorItem.getMaterial().getName() + "_layer_" + (bl ? 2 : 1) + ".png"), bl2, model, 1.0F, 1.0F, 1.0F);
		}
		
		info.cancel();
	}
}
