package com.github.clevernucleus.armormodelpredicate.mixin;

import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.clevernucleus.armormodelpredicate.ArmorModelPredicateProviderRegistry;
import com.github.clevernucleus.armormodelpredicate.Provider;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Mixin(ArmorFeatureRenderer.class)
abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
	public ArmorFeatureRendererMixin(FeatureRendererContext<T, M> context) { super(context); }
	
	@Shadow
	@Final
	private static Map<String, Identifier> ARMOR_TEXTURE_CACHE;
	
	@Shadow
	protected void setVisible(A bipedModel, EquipmentSlot slot) {}
	
	@ModifyVariable(method = "renderArmor", at = @At("STORE"), name = "itemStack", ordinal = 0)
	private ItemStack modifyGetEquippedStack(ItemStack itemStack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model) {
		Item item = itemStack.getItem();
		
		if(item instanceof ArmorItem) {
			ArmorItem armorItem = (ArmorItem)item;
			
			if(armorItem.getSlotType() == armorSlot) {
				((BipedEntityModel<T>)this.getContextModel()).setAttributes(model);
				this.setVisible(model, armorSlot);
				
				boolean bl = (armorSlot == EquipmentSlot.LEGS);
				boolean bl2 = itemStack.hasGlint();
				
				ArmorModelPredicateProviderRegistry.forEachOrDefault(item, predicate -> {
					ArmorMaterial material = armorItem.getMaterial();
					String var10000 = material.getName();
					String string = "textures/models/armor/" + var10000 + "_layer_" + (bl ? 2 : 1) + ".png";
					Identifier identifier = new Identifier(string);
					
					this.dynamicRender(matrices, vertexConsumers, light, bl2, model, predicate.accept(identifier, entity, itemStack, material, bl));
				}, () -> {
					if(armorItem instanceof DyeableArmorItem) {
						int i = ((DyeableArmorItem)armorItem).getColor(itemStack);
						float f = (float)(i >> 16 & 255) / 255.0F;
						float g = (float)(i >> 8 & 255) / 255.0F;
						float h = (float)(i & 255) / 255.0F;
						
						this.basicRender(matrices, vertexConsumers, light, armorItem, bl2, model, bl, f, g, h, (String)null);
						this.basicRender(matrices, vertexConsumers, light, armorItem, bl2, model, bl, 1.0F, 1.0F, 1.0F, "overlay");
					} else {
						this.basicRender(matrices, vertexConsumers, light, armorItem, bl2, model, bl, 1.0F, 1.0F, 1.0F, (String)null);
					}
				});
			}
		}
		
		return itemStack;
	}
	
	@Inject(method = "renderArmorParts", at = @At("HEAD"), cancellable = true)
	private void renderArmorPartsModified(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item, boolean usesSecondLayer, A model, boolean legs, float red, float green, float blue, @Nullable String overlay, CallbackInfo info) {
		info.cancel();
	}
	
	private void dynamicRender(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, boolean usesSecondLayer, A model, Provider provider) {
		provider.accept((identifier, red, green, blue) -> {
			VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull((Identifier)ARMOR_TEXTURE_CACHE.computeIfAbsent(identifier.getPath(), id -> identifier)), false, usesSecondLayer);
			model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);
		});
	}
	
	private void basicRender(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item, boolean usesSecondLayer, A model, boolean legs, float red, float green, float blue, @Nullable String overlay) {
		VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(this.defaultArmorTexture(item, legs, overlay)), false, usesSecondLayer);
		model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);
	}
	
	private Identifier defaultArmorTexture(ArmorItem item, boolean legs, @Nullable String overlay) {
		String var10000 = item.getMaterial().getName();
		String string = "textures/models/armor/" + var10000 + "_layer_" + (legs ? 2 : 1) + (overlay == null ? "" : "_" + overlay) + ".png";
		return (Identifier)ARMOR_TEXTURE_CACHE.computeIfAbsent(string, Identifier::new);
	}
}
