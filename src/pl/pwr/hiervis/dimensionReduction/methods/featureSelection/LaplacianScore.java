package pl.pwr.hiervis.dimensionReduction.methods.featureSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.distanceMeasures.Euclidean;
import pl.pwr.hiervis.dimensionReduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelection;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters;
import pl.pwr.hiervis.dimensionReduction.methods.core.MatrixUtils;

public class LaplacianScore extends FeatureSelection {

	@Override
	public String getName() {
		return "LaplacianScore";
	}

	@Override
	public String getSimpleName() {
		return "LaplacianScore";
	}

	@Override
	public String getDescription() {
		return "LaplacianScore Description";
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
		return new LaplacianScore();
	}

	@Override
	public List<FeatureSelectionResult> selectFeatures(Hierarchy source) {
		String[] dimensionNames = HierarchyUtils.getHierarchyNames(source);

		double[][] matrix = HierarchyUtils.toMatrix(source);
		double[] score = laplacianScore(matrix);
		int[] ranked = MatrixUtils.sortDescendant(score);

		List<FeatureSelectionResult> set = new ArrayList<FeatureSelectionResult>();
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

		double[][] DPrime = calculateDPrime(matrix, sum);
		DPrime = MatrixUtils.useFunctionOnMatrixes((a, b) -> a - b, DPrime, tmpl);

		double[][] LPrime = calculateLPrime(matrix, distanceMatrix);
		LPrime = MatrixUtils.useFunctionOnMatrixes((a, b) -> a - b, LPrime, tmpl);

		MatrixUtils.transformValues(DPrime, x -> (x < 1e-12) ? 10000 : x);

		double[][] laplacianScores = MatrixUtils.useFunctionOnMatrixes((a, b) -> a / b, LPrime, DPrime);
		laplacianScores = MatrixUtils.transpose(laplacianScores);

		return MatrixUtils.dropSingleton(laplacianScores);

	}

	private double[][] calculateDPrime(double[][] X, double[] D) {
		double[][] DPrime = MatrixUtils.useFunctionRowWise(X, D, (a, b) -> a * b);

		DPrime = MatrixUtils.useFunctionOnMatrixesTransposeA((a, b) -> a * b, DPrime, X);

		double[] sumOfLPrime = MatrixUtils.sumOfElements(DPrime, false);

		return MatrixUtils.vectorToMatrix(sumOfLPrime, false);
	}

	private double[][] calculateLPrime(double[][] elementsMatrix, double[][] multiplicantMatrix) {
		double[][] elementsMatrixTrans = MatrixUtils.transpose(elementsMatrix); // X'

		double[][] LPrime = MatrixUtils.multiplicateMatrixesWrap(elementsMatrixTrans, multiplicantMatrix);

		LPrime = MatrixUtils.useFunctionOnMatrixesTransposeA((a, b) -> a * b, LPrime, elementsMatrix); // (X'*L)'.*X

		double[] sumOfLPrime = MatrixUtils.sumOfElements(LPrime, false); // sum((X'*D)'.*X)

		return MatrixUtils.vectorToMatrix(sumOfLPrime, false);
	}
}
