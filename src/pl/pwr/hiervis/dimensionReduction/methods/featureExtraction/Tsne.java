package pl.pwr.hiervis.dimensionReduction.methods.featureExtraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jujutsu.tsne.TSneConfiguration;
import com.jujutsu.tsne.barneshut.BHTSne;
import com.jujutsu.tsne.barneshut.BarnesHutTSne;
import com.jujutsu.tsne.barneshut.ParallelBHTsne;
import com.jujutsu.utils.TSneUtils;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters.BooleanType;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters.DoubleType;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters.IntegerType;
import pl.pwr.hiervis.util.HierarchyUtils;

public class Tsne extends FeatureExtraction {
    BarnesHutTSne tsne;
    TSneConfiguration config;
    boolean parallel;
    int initialDims;
    int outputDims;
    int maxIter;
    double perplexity;
    boolean usePCA;
    double tetha;
    boolean silent;
    boolean printError;

    public Tsne() {
	this(1000, 20.0, 0.5, false, true, 5, true, true, 2);
    }

    public Tsne(int maxIter, double perplexity, double tetha, boolean parallel, boolean usePCa, int initialDims, boolean silent, boolean printError,
	    int outputDims) {
	this.parallel = parallel;
	this.initialDims = initialDims;
	this.outputDims = outputDims;
	this.maxIter = maxIter;
	this.perplexity = perplexity;
	this.usePCA = usePCa;
	this.tetha = tetha;
	this.silent = silent;
	this.printError = printError;
	if (parallel) {
	    tsne = new ParallelBHTsne();
	}
	else {
	    tsne = new BHTSne();
	}
    }

    @Override
    public Hierarchy reduceHierarchy(Hierarchy source) {
	double[][] matrix = HierarchyUtils.toMatrix(source);

	TSneConfiguration config = TSneUtils.buildConfig(matrix, outputDims, initialDims, perplexity, maxIter, usePCA, tetha, silent, printError);

	// double[][] outputMatrix = new
	// double[source.getMainHierarchy().getOverallNumberOfInstances()][1];
	double[][] outputMatrix = tsne.tsne(config);

	Hierarchy newHier = HierarchyUtils.clone(source, true, null);

	for (int i = 0; i < newHier.getOverallNumberOfInstances(); i++) {
	    newHier.getRoot().getSubtreeInstances().get(i).setData(outputMatrix[i]);
	}
	newHier.deleteDataNames();

	return newHier;
    }

    @Override
    public String getName() {
	return "(t-SNE) t-Distributed Stochastic Neighbor Embedding";
    }

    @Override
    public String getSimpleName() {
	return "t-SNE";
    }

    @Override
    public String getDescription() {
	return "";
    }

    public static String sGetName() {
	return "(t-SNE) t-Distributed Stochastic Neighbor Embedding";
    }

    public static String sGetSimpleName() {
	return "t-SNE";
    }

    public static String sGetDescription() {
	return "";
    }

    @Override
    public List<FunctionParameters> getParameters() {
	List<FunctionParameters> functionParameters = new ArrayList<FunctionParameters>();

	functionParameters.add(FunctionParameters.CreateFunctionParameter("Max iterations",
		"Positive integer specifying the maximum number of optimization iterations.",
		new IntegerType((points, dimensions) -> 100, (points, dimensions) -> 10000, (points, dimensions) -> 1000)));

	functionParameters.add(FunctionParameters.CreateFunctionParameter("Perplexity",
		"The perplexity is related to the number of nearest neighbors that is used in other manifold learning algorithms."
			+ "\nLarger datasets usually require a larger perplexity. Consider selecting a value between 5 and 50. "
			+ "\nThe choice is not extremely critical since t-SNE is quite insensitive to this parameter.",
		new DoubleType((points, dimensions) -> 0.5, (points, dimensions) -> (new Double(Math.floor((points - 1) / 3))),
			(points, dimensions) -> Math.max(0.5, Math.min(20.0, Math.floor((points - 1) / 3))))));

	functionParameters.add(FunctionParameters.CreateFunctionParameter("Theta",
		"Barnes-Hut tradeoff parameter, specified as a scalar from 0 through 1."
			+ "\nHighervalues give a faster but less accurate optimization.",
		new DoubleType((points, dimensions) -> 0.0, (points, dimensions) -> 1.0)));

	functionParameters
		.add(FunctionParameters.CreateFunctionParameter("Paraller calculations", "Perform parraller calculations using multithreading.",
			new BooleanType(true, (n, d) -> ((n >= Runtime.getRuntime().availableProcessors() * 10) ? 1 : 0))));

	functionParameters.add(FunctionParameters.CreateFunctionParameter("Use PCA for pre reduction",
		"Using PCA for predimension reduction before t-SNE.", new BooleanType(false, "PCA initial dimensions")));

	functionParameters.add(FunctionParameters.CreateFunctionParameter("PCA initial dimensions",
		"Number of initial dimension for predimension reduction.", new IntegerType((points, dimensions) -> 2,
			(points, dimensions) -> dimensions, (points, dimensions) -> (Math.max(2, dimensions / 2)))));

	return functionParameters;
    }

    @Override
    public DimensionReductionI createInstance(Map<String, Object> parameters) {

	Integer maxIter = (Integer) parameters.get(getParameters().get(0).getValueName());
	Double perplexity = (Double) parameters.get(getParameters().get(1).getValueName());
	Double tetha = (Double) parameters.get(getParameters().get(2).getValueName());
	Boolean parallel = (Boolean) parameters.get(getParameters().get(3).getValueName());
	Boolean usePCa = (Boolean) parameters.get(getParameters().get(4).getValueName());
	Integer initialDims = (Integer) parameters.get(getParameters().get(5).getValueName());

	return new Tsne(maxIter, perplexity, tetha, parallel, usePCa, initialDims, false, true, 2);
    }
}
