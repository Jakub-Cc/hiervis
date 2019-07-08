package pl.pwr.hiervis.dimensionReduction;

import java.util.List;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class CalculatedDimensionReduction {
    public LoadedHierarchy inputLoadedHierarchy;
    public Hierarchy outputHierarchy;
    public DimensionReductionI dimensionReduction;
    public List<FeatureSelectionResult> fsResult;

    public CalculatedDimensionReduction(LoadedHierarchy inputLoadedHierarchy, DimensionReductionI dimensionReduction, Hierarchy outputHierarchy,
	    List<FeatureSelectionResult> fsResults) {
	this.inputLoadedHierarchy = inputLoadedHierarchy;
	this.dimensionReduction = dimensionReduction;
	this.outputHierarchy = outputHierarchy;
	this.fsResult = fsResults;
    }

}
