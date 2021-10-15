package com.github.clevernucleus.armormodelpredicate;

/**
 * 
 * @author CleverNucleus
 *
 */
@FunctionalInterface
public interface Provider {
	
	/**
	 * 
	 * @param identifier
	 */
	void accept(final ColoredIdentifier identifier);
}
