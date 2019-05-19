package pl.pwr.hiervis.dimensionReduction;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class CalculatedDimensionReduction
{
	public LoadedHierarchy inputLoadedHierarchy;
	public Hierarchy outputHierarchy;
	public FeatureExtraction dimensionReduction;

	public CalculatedDimensionReduction(LoadedHierarchy inputLoadedHierarchy, FeatureExtraction dimensionReduction,
			Hierarchy outputHierarchy)
	{
		this.inputLoadedHierarchy = inputLoadedHierarchy;
		this.dimensionReduction = dimensionReduction;
		this.outputHierarchy = outputHierarchy;
	}

}
