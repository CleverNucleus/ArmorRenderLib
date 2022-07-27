package com.github.clevernucleus.armorrenderlib.mixin;

import java.util.List;
import java.util.Map;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.clevernucleus.armorrenderlib.impl.FeatureRendererAccessor;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> implements FeatureRendererAccessor<T, M> {
	
	@Unique
	private final Map<Class<? extends FeatureRenderer<T, M>>, FeatureRenderer<T, M>> arl_features = new Object2ObjectArrayMap<>();
	
	@SuppressWarnings("unchecked")
	@Inject(method = "addFeature", at = @At("HEAD"), cancellable = true)
	private void arl_addFeature(FeatureRenderer<T, M> feature, CallbackInfoReturnable<Boolean> info) {
		this.arl_features.put((Class<? extends FeatureRenderer<T, M>>)feature.getClass(), feature);
		info.setReturnValue(true);
	}
	
	@Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;features:Ljava/util/List;", opcode = Opcodes.GETFIELD))
	private List<FeatureRenderer<T, M>> arl_get(LivingEntityRenderer<T, M> renderer) {
		return this.arl_features.values().stream().toList();
	}
	
	@Override
	public ArmorFeatureRenderer<?, ?, ?> getFeatureRenderer(Object key) {
		return (ArmorFeatureRenderer<?, ?, ?>)this.arl_features.get(key);
	}
}
