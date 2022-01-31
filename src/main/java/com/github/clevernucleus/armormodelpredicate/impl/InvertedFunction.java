package com.github.clevernucleus.armormodelpredicate.impl;

@FunctionalInterface
public interface InvertedFunction {
	boolean invert(final WrapperFunction function);
}
