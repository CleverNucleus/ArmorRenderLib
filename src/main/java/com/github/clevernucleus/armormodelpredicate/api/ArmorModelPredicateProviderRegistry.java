package com.github.clevernucleus.armormodelpredicate.api;

import java.util.ArrayList;
import java.util.List;

import com.github.clevernucleus.armormodelpredicate.impl.ArmorModelPredicate;
import com.github.clevernucleus.armormodelpredicate.impl.InvertedFunction;
import com.github.clevernucleus.armormodelpredicate.impl.InvertedProvider;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.util.Identifier;

/**
 * Register custom armor texture/colors with this class.
 * 
 * @author CleverNucleus
 *
 */
@Environment(EnvType.CLIENT)
public final class ArmorModelPredicateProviderRegistry {
	private static final List<InvertedFunction> PROVIDERS = new ArrayList<InvertedFunction>();
	
	/**
	 * This method can be used to register armor colors and textures. An example is provided below:
	 * <br></br>
	 * <code>
	 * ArmorModelPredicateProviderRegistry.register((itemStack, entity) -> {
	 *     return itemStack.getItem() == Items.IRON_CHESTPLATE;
	 * }, (itemStack, entity, legs) -> {
	 *     Identifier texture = new Identifier("example_mod:textures/models/armor/custom_iron_layer_" + (legs ? 2 : 1) + ".png");
	 *     return provider -> provider.provide(texture, 0xFF0000);
	 * });
	 * </code>
	 * 
	 * @param predicate
	 * @param provider
	 */
	public static void register(final ArmorModelPredicate predicate, final InvertedProvider provider) {
		PROVIDERS.add(wrapper -> wrapper.apply(predicate, provider));
	}
	
	public static List<InvertedFunction> providers() {
		return PROVIDERS;
	}
	
	static {
		register((itemStack, entity) -> itemStack.getItem() instanceof DyeableArmorItem, (itemStack, entity, legs) -> {
			Identifier texture = new Identifier("minecraft:textures/models/armor/leather_layer_" + (legs ? 2 : 1) + ".png");
			int color = ((DyeableArmorItem)itemStack.getItem()).getColor(itemStack);
			return provider -> provider.provide(texture, color);
		});
		register((itemStack, entity) -> itemStack.getItem() instanceof DyeableArmorItem, (itemStack, entity, legs) -> {
			Identifier texture = new Identifier("minecraft:textures/models/armor/leather_layer_" + (legs ? 2 : 1) + "_overlay.png");
			return provider -> provider.provide(texture, 16777215);
		});
	}
}
