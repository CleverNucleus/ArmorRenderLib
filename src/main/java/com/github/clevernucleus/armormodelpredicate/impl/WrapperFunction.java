package com.github.clevernucleus.armormodelpredicate.impl;

@FunctionalInterface
public interface WrapperFunction {
	boolean apply(final ArmorModelPredicate predicate, final InvertedProvider prodiver);
}
