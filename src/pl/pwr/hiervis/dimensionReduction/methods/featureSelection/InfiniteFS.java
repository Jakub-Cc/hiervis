package pl.pwr.hiervis.dimensionReduction.methods.featureSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelection;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters;
import pl.pwr.hiervis.dimensionReduction.methods.core.MatrixUtils;
import pl.pwr.hiervis.util.HierarchyUtils;

public class InfiniteFS extends FeatureSelection {

    private Double alpha;

    public InfiniteFS(double alpha) {
	this.alpha = alpha;
    }

    public InfiniteFS() {
	alpha = null;
	// TODO decide in empty function needed, or default contsructor is enough
    }

    @Override
    public String getName() {
	// TODO decide if sufficient name
	return "Infinite FS";
    }

    @Override
    public String getSimpleName() {
	// TODO decide if sufficient name
	return "InfFS";
    }

    @Override
    public String getDescription() {
	// TODO declare description
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

	List<FeatureSelectionResult> set = new ArrayList<FeatureSelectionResult>();
	for (int i = 0; i < dimensionNames.length; i++) {
	    set.add(new FeatureSelectionResult(ranked[i], dimensionNames[ranked[i]], score[i], i + 1));
	}
	return set;
    }

    @Override
    public List<FunctionParameters> getParameters() {

	List<FunctionParameters> functionParameters = new ArrayList<FunctionParameters>();

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
	// TODO rethinks this initialization
	Double alpha = (Double) parameters.get(getParameters().get(0).getValueName());

	return new InfiniteFS(alpha);
    }

    private double[] infFS(double[][] Xtrain, double alpha) {

	// int[] ranked = new int[Xtrain[0].length];

	double[][] corr = MatrixUtils.spearmanCorrelation(Xtrain);
	MatrixUtils.replaceUndef(corr, 0);
	MatrixUtils.transformValues(corr, x -> 1 - Math.abs(x));

	double[] STD = MatrixUtils.standardDeviation(Xtrain, false);

	double[][] sigma = MatrixUtils.crossProduct((a, b) -> a > b ? a : b, STD, STD);

	double min = MatrixUtils.min(sigma);
	MatrixUtils.transformValues(sigma, e -> e - min);

	double max = MatrixUtils.max(sigma);
	MatrixUtils.transformValues(sigma, e -> e / max);
	MatrixUtils.replaceUndef(sigma, 0);

	MatrixUtils.transformValues(corr, x -> alpha * x);
	MatrixUtils.transformValues(sigma, x -> (1 - alpha) * x);

	double[][] A = MatrixUtils.useFunctionOnMatrixes((a, b) -> a + b, corr, sigma);
	double[][] I = MatrixUtils.eye(A.length);

	double factor = 0.99;
	double r = (factor / MatrixUtils.max(MatrixUtils.eigenValues(A)));

	MatrixUtils.transformValues(A, x -> x * r);
	double[][] y = MatrixUtils.useFunctionOnMatrixes((a, b) -> a - b, I, A);

	double[][] S = MatrixUtils.useFunctionOnMatrixes((a, b) -> a - b, MatrixUtils.inverse(y), I);

	double[] weight = MatrixUtils.sumOfElements(S, false);

	// ranked = MatrixUtils.sortDescendant(weight);

	for (int i = 0; i < weight.length; i++) {
	    System.out.print("" + i + " " + weight[i] + " ;");
	}
	System.out.println();

	return weight;
    }

}
