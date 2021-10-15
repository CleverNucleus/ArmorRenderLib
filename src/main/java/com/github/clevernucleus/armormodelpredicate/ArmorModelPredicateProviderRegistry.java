package com.github.clevernucleus.armormodelpredicate;

import java.util.function.Consumer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;

/**
 * Predicate Registry class.
 * 
 * @author CleverNucleus
 *
 */
@Environment(EnvType.CLIENT)
public final class ArmorModelPredicateProviderRegistry {
	private static final Multimap<Item, ArmorModelPredicate> PREDICATES = HashMultimap.create();
	
	/**
	 * Use this method to register an armor model predicate.
	 * @param item
	 * @param armorModelPredicate
	 */
	public static void register(final Item item, ArmorModelPredicate armorModelPredicate) {
		PREDICATES.put(item, armorModelPredicate);
	}
	
	/**
	 * Loops through each armor model predicate for every item, with Collection::forEach. If there is no item match, runs orDefault.
	 * @param item
	 * @param consumer
	 * @param orDefault
	 */
	public static void forEachOrDefault(final Item item, final Consumer<ArmorModelPredicate> consumer, final VoidConsumer orDefault) {
		if(PREDICATES.containsKey(item)) {
			PREDICATES.get(item).forEach(consumer);
		} else {
			orDefault.accept();
		}
	}
}
