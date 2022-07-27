package com.github.clevernucleus.armorrenderlib.api;

@FunctionalInterface
public interface ArmorRenderData {
	
	/**
	 * 
	 * @param texturePath
	 * @param color
	 * @param hasGlint
	 */
	void accept(final String texturePath, final int color, final boolean hasGlint);
}
