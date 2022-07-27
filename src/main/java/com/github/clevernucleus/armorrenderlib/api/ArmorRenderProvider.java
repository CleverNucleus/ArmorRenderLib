package com.github.clevernucleus.armorrenderlib.api;

@FunctionalInterface
public interface ArmorRenderProvider {
	
	/**
	 * 
	 * @param data
	 */
	void from(final ArmorRenderData data);
}
