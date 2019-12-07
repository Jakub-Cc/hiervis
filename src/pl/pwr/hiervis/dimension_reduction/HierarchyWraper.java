package pl.pwr.hiervis.dimension_reduction;

import java.util.ArrayList;
import java.util.List;

import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelectionResult;

public class HierarchyWraper {
	private Hierarchy hierarchy;
	private Hierarchy originalHierarchy;
	private Hierarchy[] reducedHierarchy;
	private DimensionReductionManager dimensionReductionManager;

	private List<FeatureSelectionResult>[] featureSelectionResults;
	private VisibleDimensionsKeeper[] visibleDimensionsKeeper;
	private int curentSelection;

	public HierarchyWraper(Hierarchy hierarchy) {
		this.originalHierarchy = hierarchy;
		curentSelection = 0;
		this.setHierarchy(hierarchy);
		this.dimensionReductionManager = new DimensionReductionManager();
		this.setReducedHierarchy(new Hierarchy[dimensionReductionManager.getSize()]);

		this.featureSelectionResults = new ArrayList[dimensionReductionManager.getSize()];

		this.visibleDimensionsKeeper = new VisibleDimensionsKeeper[reducedHierarchy.length + 1];
		visibleDimensionsKeeper[0] = new VisibleDimensionsKeeper(HierarchyUtils.getFeatureCount(originalHierarchy));
	}

	public HierarchyWraper() {
		setHierarchy(null);
		curentSelection = -1;
		originalHierarchy = null;
		dimensionReductionManager = new DimensionReductionManager();
		setReducedHierarchy(new Hierarchy[dimensionReductionManager.getSize()]);
	}

	public Hierarchy getHierarchyWithoutChange(int index) {
		if (index == 0) {
			return originalHierarchy;
		} else if (index > 0 && index - 1 < dimensionReductionManager.getSize()) {
			return reducedHierarchy[index - 1];
		}
		return null;
	}

	public void setHierarchy(int index) {
		if (index <= 0) {
			hierarchy = originalHierarchy;
			curentSelection = 0;
		} else if (index - 1 < dimensionReductionManager.getSize()) {
			hierarchy = reducedHierarchy[index - 1];
			curentSelection = index;
		}

	}

	public Hierarchy getOriginalHierarchy() {
		return originalHierarchy;
	}

	/**
	 * @return the reducedHierarchy
	 */
	public Hierarchy[] getReducedHierarchy() {
		return reducedHierarchy;
	}

	/**
	 * @param reducedHierarchy the reducedHierarchy to set
	 */
	public void setReducedHierarchy(Hierarchy[] reducedHierarchy) {
		this.reducedHierarchy = reducedHierarchy;
	}

	public Hierarchy getReducedHierarchy(FeatureExtraction dimensionReduction) {
		int index = dimensionReductionManager.getIndex(dimensionReduction);
		if (index != -1) {
			return reducedHierarchy[index];
		}
		return null;
	}

	public Hierarchy getReducedHierarchy(int index) {
		if (index < 0 || index > dimensionReductionManager.getSize())
			return null;
		return reducedHierarchy[index];
	}

	public void addReducedHierarchy(CalculatedDimensionReduction calculatedDimensionReduction) {
		int index = dimensionReductionManager.getIndex(calculatedDimensionReduction.getDimensionReduction());
		if (index != -1) {
			reducedHierarchy[index] = calculatedDimensionReduction.getOutputHierarchy();
			visibleDimensionsKeeper[index + 1] = new VisibleDimensionsKeeper(
					HierarchyUtils.getFeatureCount(reducedHierarchy[index]));
		}
	}

	public void addFeatureSelectionResult(CalculatedDimensionReduction calculatedDimensionReduction) {

		int index = dimensionReductionManager.getIndex(calculatedDimensionReduction.getDimensionReduction());
		if (index != -1) {
			featureSelectionResults[index] = calculatedDimensionReduction.getFsResult();
			visibleDimensionsKeeper[index + 1] = new VisibleDimensionsKeeper(0);
		}
	}

	public List<FeatureSelectionResult> getFSResult(int index) {
		if (index > 0 && index - 1 < dimensionReductionManager.getSize()) {
			return featureSelectionResults[index - 1];
		}
		return null;
	}

	public Hierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Hierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public VisibleDimensionsKeeper[] getVisibleDimensionsKeeper() {
		return visibleDimensionsKeeper;
	}

	public VisibleDimensionsKeeper getVisibleDimensions() {
		return visibleDimensionsKeeper[curentSelection];
	}
}
