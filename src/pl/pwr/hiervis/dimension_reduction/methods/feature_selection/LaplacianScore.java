package pl.pwr.hiervis.dimension_reduction.methods.feature_selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimension_reduction.distance_measures.Euclidean;
import pl.pwr.hiervis.dimension_reduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelection;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.dimension_reduction.methods.core.MatrixUtils;
import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.FunctionParameters;

public class LaplacianScore implements FeatureSelection {

	public static final String NAME = "LaplacianScore";
	public static final String SIMPLE_NAME = "LaplacianScore";
	public static final String DESCRIPTION = "LaplacianScore Description";

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
		return new LaplacianScore();
	}

	@Override
	public List<FeatureSelectionResult> selectFeatures(Hierarchy source) {
		String[] dimensionNames = HierarchyUtils.getHierarchyNames(source);

		double[][] matrix = HierarchyUtils.toMatrix(source);
		double[] score = laplacianScore(matrix);
		int[] ranked = MatrixUtils.sortDescendant(score);

		List<FeatureSelectionResult> set = new ArrayList<>();
		for (int i = 0; i < dimensionNames.length; i++) {
			set.add(new FeatureSelectionResult(ranked[i], dimensionNames[ranked[i]], score[i], i + 1));
		}
		return set;
	}

	private double[] laplacianScore(double[][] matrix) {

		double[][] distanceMatrix = MatrixUtils.generateDistanceMatrix(matrix, new Euclidean());
		double max = MatrixUtils.max(distanceMatrix);
		MatrixUtils.transformValues(distanceMatrix, x -> -(x / max));

		double[] sum = MatrixUtils.sumOfElements(distanceMatrix, false);
		double[][] sumMatrix = MatrixUtils.vectorToMatrix(sum, false);

		double[][] tmpl = MatrixUtils.multiplicateMatrixesWrap(sumMatrix, matrix);

		double sumOfAllElements = MatrixUtils.sumOfElements(sum);
		MatrixUtils.transformValues(tmpl, x -> x * x / sumOfAllElements);

		double[][] dPrime = calculateDPrime(matrix, sum);
		dPrime = MatrixUtils.useFunctionOnMatrixes((a, b) -> a - b, dPrime, tmpl);

		double[][] lPrime = calculateLPrime(matrix, distanceMatrix);
		lPrime = MatrixUtils.useFunctionOnMatrixes((a, b) -> a - b, lPrime, tmpl);

		MatrixUtils.transformValues(dPrime, x -> (x < 1e-12) ? 10000 : x);

		double[][] laplacianScores = MatrixUtils.useFunctionOnMatrixes((a, b) -> a / b, lPrime, dPrime);
		laplacianScores = MatrixUtils.transpose(laplacianScores);

		return MatrixUtils.dropSingleton(laplacianScores);

	}

	private double[][] calculateDPrime(double[][] x, double[] d) {
		double[][] dPrime = MatrixUtils.useFunctionRowWise(x, d, (a, b) -> a * b);

		dPrime = MatrixUtils.useFunctionOnMatrixesTransposeA((a, b) -> a * b, dPrime, x);

		double[] sumOfLPrime = MatrixUtils.sumOfElements(dPrime, false);

		return MatrixUtils.vectorToMatrix(sumOfLPrime, false);
	}

	private double[][] calculateLPrime(double[][] elementsMatrix, double[][] multiplicantMatrix) {
		double[][] elementsMatrixTrans = MatrixUtils.transpose(elementsMatrix); // X'

		double[][] lPrime = MatrixUtils.multiplicateMatrixesWrap(elementsMatrixTrans, multiplicantMatrix);

		lPrime = MatrixUtils.useFunctionOnMatrixesTransposeA((a, b) -> a * b, lPrime, elementsMatrix); // (X'*L)'.*X

		double[] sumOfLPrime = MatrixUtils.sumOfElements(lPrime, false); // sum((X'*D)'.*X)

		return MatrixUtils.vectorToMatrix(sumOfLPrime, false);
	}
}
