package com.github.clevernucleus.armorrenderlib.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.github.clevernucleus.armorrenderlib.impl.ArmorTextureCache;

import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

@Mixin(ArmorFeatureRenderer.class)
abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> implements ArmorTextureCache<A> {
	
	@Shadow
	@Final
	private static Map<String, Identifier> ARMOR_TEXTURE_CACHE;
	
	@Shadow
	@Final
	private A leggingsModel;
	
	@Shadow
	@Final
    private A bodyModel;
	
	/*
	 * We do this instead of just an invoker because Geckolib Injects into #getArmor and runs an instanced set/get exiting the method, 
	 * which causes the game to crash anytime that method is accessed from anywhere else - and we want to be compatible with Geckolib.
	 */
	@Override
	public A getArmorCustom(EquipmentSlot slot) {
		return slot == EquipmentSlot.LEGS ? this.leggingsModel : this.bodyModel;
	}
	
	@Override
	public Identifier getOrCache(final String path) {
		return ARMOR_TEXTURE_CACHE.computeIfAbsent(path, Identifier::new);
	}
}
