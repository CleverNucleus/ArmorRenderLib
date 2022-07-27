package com.github.clevernucleus.armorrenderlib.impl;

import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

public interface FeatureRendererAccessor<T extends LivingEntity, M extends EntityModel<T>> {
	ArmorFeatureRenderer<?, ?, ?> getFeatureRenderer(Object key);
}
