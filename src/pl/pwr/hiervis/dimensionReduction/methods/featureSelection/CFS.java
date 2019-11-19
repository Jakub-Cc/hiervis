package pl.pwr.hiervis.dimensionReduction.methods.featureSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelection;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters;
import pl.pwr.hiervis.dimensionReduction.methods.core.MatrixUtils;

public class CFS extends FeatureSelection {

	@Override
	public String getName() {
		return "CFS";
	}

	@Override
	public String getSimpleName() {
		return "CFS";
	}

	@Override
	public String getDescription() {
		return "CFS";
	}

	@Override
	public Long getMinimumMemmory(int pointsNumber, int dimensionSize) {

		return 0l;
	}

	@Override
	public List<FunctionParameters> getParameters() {
		List<FunctionParameters> functionParameters = new ArrayList<FunctionParameters>();

		return functionParameters;
	}

	@Override
	public DimensionReductionI createInstance(Map<String, Object> parameters) {

		return new CFS();
	}

	@Override
	public List<FeatureSelectionResult> selectFeatures(Hierarchy source) {
		String[] dimensionNames = HierarchyUtils.getHierarchyNames(source);

		double[][] matrix = HierarchyUtils.toMatrix(source);
		double[] score = cfs(matrix);
		int[] ranked = MatrixUtils.sortAscending(score);

		List<FeatureSelectionResult> set = new ArrayList<FeatureSelectionResult>();
		for (int i = 0; i < dimensionNames.length; i++) {
			set.add(new FeatureSelectionResult(ranked[i], dimensionNames[ranked[i]], score[i], i + 1));
		}
		return set;
	}

	private double[] cfs(double[][] matrix) {
		PearsonsCorrelation per = new PearsonsCorrelation(matrix);
		double[][] corr = per.getCorrelationMatrix().getData();
		MatrixUtils.transformValues(corr, e -> Math.abs(e));

		MatrixUtils.replaceUndef(corr, 1);
		double min[] = MatrixUtils.minByRow(corr);

		return min;
	}

}
