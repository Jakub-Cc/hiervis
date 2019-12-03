package pl.pwr.hiervis.dimension_reduction.methods.feature_selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelection;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.dimension_reduction.methods.core.FunctionParameters;
import pl.pwr.hiervis.dimension_reduction.methods.core.MatrixUtils;

public class InfiniteFS implements FeatureSelection {

	private static final Logger log = LogManager.getLogger(InfiniteFS.class);
	private Double alpha;

	public InfiniteFS(double alpha) {
		this.alpha = alpha;
	}

	public InfiniteFS() {
		alpha = null;
	}

	@Override
	public String getName() {
		return "Infinite FS";
	}

	@Override
	public String getSimpleName() {
		return "InfFS";
	}

	@Override
	public String getDescription() {
		return "Infinite FS description";
	}

	@Override
	public Long getMinimumMemmory(int pointsNumber, int dimensionSize) {
		// TODO calculate real memory requirements
		return 0l;
	}

	@Override
	public List<FeatureSelectionResult> selectFeatures(Hierarchy source) {

		String[] dimensionNames = HierarchyUtils.getHierarchyNames(source);

		double[][] matrix = HierarchyUtils.toMatrix(source);
		double[] score = infFS(matrix, alpha);
		int[] ranked = MatrixUtils.sortDescendant(score);

		List<FeatureSelectionResult> set = new ArrayList<>();
		for (int i = 0; i < dimensionNames.length; i++) {
			set.add(new FeatureSelectionResult(ranked[i], dimensionNames[ranked[i]], score[i], i + 1));
		}
		return set;
	}

	@Override
	public List<FunctionParameters> getParameters() {

		List<FunctionParameters> functionParameters = new ArrayList<>();

		functionParameters.add(new FunctionParameters() {
			@Override
			public String getValueName() {
				return "Alpha";
			}

			@Override
			public String getValueDescription() {
				return "Alpha description Alpha descriptionAlpha description<br> Alpha descriptionAlpha descriptionAlpha \ndescriptionAlpha descriptionAlpha description Alpha descriptionAlpha descriptionAlpha description";
			}

			@Override
			public DataType getValueClass() {
				return new DoubleType((points, dimensions) -> 0.0, (points, dimensions) -> 1.0);
			}
		});

		return functionParameters;
	}

	@Override
	public FeatureSelection createInstance(Map<String, Object> parameters) {
		Double newAlpha = (Double) parameters.get(getParameters().get(0).getValueName());
		return new InfiniteFS(newAlpha);
	}

	private double[] infFS(double[][] xTrain, double alpha) {
		double[][] corr = MatrixUtils.spearmanCorrelation(xTrain);
		MatrixUtils.replaceUndef(corr, 0);
		MatrixUtils.transformValues(corr, x -> 1 - Math.abs(x));

		double[] std = MatrixUtils.standardDeviation(xTrain, false);

		double[][] sigma = MatrixUtils.crossProduct((a, b) -> a > b ? a : b, std, std);

		double min = MatrixUtils.min(sigma);
		MatrixUtils.transformValues(sigma, e -> e - min);

		double max = MatrixUtils.max(sigma);
		MatrixUtils.transformValues(sigma, e -> e / max);
		MatrixUtils.replaceUndef(sigma, 0);

		MatrixUtils.transformValues(corr, x -> alpha * x);
		MatrixUtils.transformValues(sigma, x -> (1 - alpha) * x);

		double[][] aMatrix = MatrixUtils.useFunctionOnMatrixes((a, b) -> a + b, corr, sigma);
		double[][] iMatrix = MatrixUtils.eye(aMatrix.length);

		double factor = 0.99;
		double r = (factor / MatrixUtils.max(MatrixUtils.eigenValues(aMatrix)));

		MatrixUtils.transformValues(aMatrix, x -> x * r);
		double[][] y = MatrixUtils.useFunctionOnMatrixes((a, b) -> a - b, iMatrix, aMatrix);

		double[][] sMatrix = MatrixUtils.useFunctionOnMatrixes((a, b) -> a - b, MatrixUtils.inverse(y), iMatrix);

		double[] weight = MatrixUtils.sumOfElements(sMatrix, false);

		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < weight.length; i++) {
			stringBuilder.append(i + " " + weight[i] + " ;");
		}
		if (log.isTraceEnabled())
			log.trace(stringBuilder.toString());

		return weight;
	}

}
