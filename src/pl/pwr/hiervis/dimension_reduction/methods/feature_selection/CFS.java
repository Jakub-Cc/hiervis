package pl.pwr.hiervis.dimension_reduction.methods.feature_selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimension_reduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelection;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.dimension_reduction.methods.core.MatrixUtils;
import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.FunctionParameters;

public class CFS implements FeatureSelection {

	public static final String NAME = "CFS";
	public static final String SIMPLE_NAME = "CFS";
	public static final String DESCRIPTION = "CFS";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getSimpleName() {
		return SIMPLE_NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public Long getMinimumMemmory(int pointsNumber, int dimensionSize) {

		return 0l;
	}

	@Override
	public List<FunctionParameters> getParameters() {
		return new ArrayList<>();
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

		List<FeatureSelectionResult> set = new ArrayList<>();
		for (int i = 0; i < dimensionNames.length; i++) {
			set.add(new FeatureSelectionResult(ranked[i], dimensionNames[ranked[i]], score[i], i + 1));
		}
		return set;
	}

	private double[] cfs(double[][] matrix) {
		PearsonsCorrelation per = new PearsonsCorrelation(matrix);
		double[][] corr = per.getCorrelationMatrix().getData();
		MatrixUtils.transformValues(corr, Math::abs);

		MatrixUtils.replaceUndef(corr, 1);

		return MatrixUtils.minByRow(corr);
	}

}
