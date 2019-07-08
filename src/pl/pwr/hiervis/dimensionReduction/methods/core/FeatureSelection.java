package pl.pwr.hiervis.dimensionReduction.methods.core;

import java.util.List;

import basic_hierarchy.interfaces.Hierarchy;

public abstract class FeatureSelection implements DimensionReductionI {

    public abstract List<FeatureSelectionResult> selectFeatures(Hierarchy source);

}