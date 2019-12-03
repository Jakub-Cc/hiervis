package pl.pwr.hiervis.dimension_reduction.methods.core;

import java.util.List;

import basic_hierarchy.interfaces.Hierarchy;

public interface FeatureSelection extends DimensionReductionI {

	public abstract List<FeatureSelectionResult> selectFeatures(Hierarchy source);

}