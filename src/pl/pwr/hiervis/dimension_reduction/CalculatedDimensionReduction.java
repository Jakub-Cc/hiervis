package pl.pwr.hiervis.dimension_reduction;

import java.util.List;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimension_reduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class CalculatedDimensionReduction {
	private LoadedHierarchy inputLoadedHierarchy;
	private Hierarchy outputHierarchy;
	private DimensionReductionI dimensionReduction;
	private List<FeatureSelectionResult> fsResult;

	public CalculatedDimensionReduction(LoadedHierarchy inputLoadedHierarchy, DimensionReductionI dimensionReduction,
			Hierarchy outputHierarchy, List<FeatureSelectionResult> fsResults) {
		this.inputLoadedHierarchy = inputLoadedHierarchy;
		this.dimensionReduction = dimensionReduction;
		this.outputHierarchy = outputHierarchy;
		this.fsResult = fsResults;
	}

	public LoadedHierarchy getInputLoadedHierarchy() {
		return inputLoadedHierarchy;
	}

	public Hierarchy getOutputHierarchy() {
		return outputHierarchy;
	}

	public DimensionReductionI getDimensionReduction() {
		return dimensionReduction;
	}

	public List<FeatureSelectionResult> getFsResult() {
		return fsResult;
	}

}
