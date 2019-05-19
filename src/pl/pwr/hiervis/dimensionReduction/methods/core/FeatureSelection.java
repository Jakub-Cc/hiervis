package pl.pwr.hiervis.dimensionReduction.methods.core;

import java.util.List;

import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public abstract class FeatureSelection implements DimensionReductionI {

    public abstract List<FeatureSelectionResult> selectFeatures(LoadedHierarchy source);

}