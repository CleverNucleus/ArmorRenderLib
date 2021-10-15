package com.github.clevernucleus.armormodelpredicate;

import net.minecraft.util.Identifier;

/**
 * 
 * @author CleverNucleus
 *
 */
@FunctionalInterface
public interface ColoredIdentifier {
	
	/**
	 * functional wrapper to hold an identifier for armor texture and the armor's colour: rgb.
	 * @param identifier
	 * @param red
	 * @param green
	 * @param blue
	 */
	void accept(final Identifier identifier, final float red, final float green, final float blue);
}
