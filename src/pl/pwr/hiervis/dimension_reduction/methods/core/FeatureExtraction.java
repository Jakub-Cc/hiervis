package pl.pwr.hiervis.dimension_reduction.methods.core;

import basic_hierarchy.interfaces.Hierarchy;

public interface FeatureExtraction extends DimensionReductionI {

	public abstract Hierarchy reduceHierarchy(Hierarchy source);

}
