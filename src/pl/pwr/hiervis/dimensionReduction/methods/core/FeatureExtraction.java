package pl.pwr.hiervis.dimensionReduction.methods.core;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public abstract class FeatureExtraction implements DimensionReductionI {

    public abstract Hierarchy reduceHierarchy(LoadedHierarchy source);

    public Long getMinimumMemmory(int pointsNumber, int dimensionSize) {
	// TODO implement
	return 0l;
    }

    /**
     * public List<FunctionParameters> getParameters() { // TODO implement return null; }
     * 
     * public DimensionReductionI createInstance(Map<String, Object> parameters) { // TODO
     * implement return null; }
     **/
}
