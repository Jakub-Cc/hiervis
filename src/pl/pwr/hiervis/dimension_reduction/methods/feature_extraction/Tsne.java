package pl.pwr.hiervis.dimension_reduction.methods.feature_extraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jujutsu.tsne.TSneConfiguration;
import com.jujutsu.tsne.barneshut.BHTSne;
import com.jujutsu.tsne.barneshut.BarnesHutTSne;
import com.jujutsu.tsne.barneshut.ParallelBHTsne;
import com.jujutsu.utils.TSneUtils;

import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimension_reduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimension_reduction.methods.core.FunctionParameters;
import pl.pwr.hiervis.dimension_reduction.methods.core.FunctionParameters.BooleanType;
import pl.pwr.hiervis.dimension_reduction.methods.core.FunctionParameters.DoubleType;
import pl.pwr.hiervis.dimension_reduction.methods.core.FunctionParameters.IntegerType;

public class Tsne implements FeatureExtraction {
	public static final String DSCRIPTION = "";
	public static final String T_SNE_FULL_NAME = "(t-SNE) t-Distributed Stochastic Neighbor Embedding";
	public static final String T_SNE = "t-SNE";
	BarnesHutTSne barnesHutTSne;
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

	public Tsne(int maxIter, double perplexity, double tetha, boolean parallel, boolean usePCa, int initialDims,
			boolean silent, boolean printError, int outputDims) {
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
			barnesHutTSne = new ParallelBHTsne();
		} else {
			barnesHutTSne = new BHTSne();
		}
	}

	@Override
	public Hierarchy reduceHierarchy(Hierarchy source) {
		double[][] matrix = HierarchyUtils.toMatrix(source);

		TSneConfiguration newConfig = TSneUtils.buildConfig(matrix, outputDims, initialDims, perplexity, maxIter,
				usePCA, tetha, silent, printError);

		double[][] outputMatrix = barnesHutTSne.tsne(newConfig);

		Hierarchy newHier = HierarchyUtils.clone(source, true, null);

		for (int i = 0; i < newHier.getOverallNumberOfInstances(); i++) {
			newHier.getRoot().getSubtreeInstances().get(i).setData(outputMatrix[i]);
		}
		newHier.deleteDataNames();

		return newHier;
	}

	@Override
	public String getName() {
		return T_SNE_FULL_NAME;
	}

	@Override
	public String getSimpleName() {
		return T_SNE;
	}

	@Override
	public String getDescription() {
		return DSCRIPTION;
	}

	public static String sGetName() {
		return T_SNE_FULL_NAME;
	}

	public static String sGetSimpleName() {
		return T_SNE;
	}

	public static String sGetDescription() {
		return DSCRIPTION;
	}

	@Override
	public List<FunctionParameters> getParameters() {
		List<FunctionParameters> functionParameters = new ArrayList<>();

		functionParameters.add(FunctionParameters.createFunctionParameter("Max iterations",
				"Positive integer specifying the maximum number of optimization iterations.", new IntegerType(
						(points, dimensions) -> 100, (points, dimensions) -> 10000, (points, dimensions) -> 1000)));

		functionParameters.add(FunctionParameters.createFunctionParameter("Perplexity",
				"The perplexity is related to the number of nearest neighbors that is used in other manifold learning algorithms."
						+ "\nLarger datasets usually require a larger perplexity. Consider selecting a value between 5 and 50. "
						+ "\nThe choice is not extremely critical since t-SNE is quite insensitive to this parameter.",
				new DoubleType((points, dimensions) -> 0.5, (points, dimensions) -> (Math.floor((points - 1) / 3)),
						(points, dimensions) -> Math.max(0.5, Math.min(20.0, Math.floor((points - 1) / 3))))));

		functionParameters.add(FunctionParameters.createFunctionParameter("Theta",
				"Barnes-Hut tradeoff parameter, specified as a scalar from 0 through 1."
						+ "\nHighervalues give a faster but less accurate optimization.",
				new DoubleType((points, dimensions) -> 0.0, (points, dimensions) -> 1.0)));

		functionParameters.add(FunctionParameters.createFunctionParameter("Paraller calculations",
				"Perform parraller calculations using multithreading.",
				new BooleanType(true, (n, d) -> ((n >= Runtime.getRuntime().availableProcessors() * 10) ? 1 : 0))));

		functionParameters.add(FunctionParameters.createFunctionParameter("Use PCA for pre reduction",
				"Using PCA for predimension reduction before t-SNE.",
				new BooleanType(false, "PCA initial dimensions")));

		functionParameters.add(FunctionParameters.createFunctionParameter("PCA initial dimensions",
				"Number of initial dimension for predimension reduction.", new IntegerType((points, dimensions) -> 2,
						(points, dimensions) -> dimensions, (points, dimensions) -> (Math.max(2, dimensions / 2)))));

		return functionParameters;
	}

	@Override
	public DimensionReductionI createInstance(Map<String, Object> parameters) {

		Integer newMaxIter = (Integer) parameters.get(getParameters().get(0).getValueName());
		Double newPerplexity = (Double) parameters.get(getParameters().get(1).getValueName());
		Double newTetha = (Double) parameters.get(getParameters().get(2).getValueName());
		Boolean newParallel = (Boolean) parameters.get(getParameters().get(3).getValueName());
		Boolean usePCa = (Boolean) parameters.get(getParameters().get(4).getValueName());
		Integer newInitialDims = (Integer) parameters.get(getParameters().get(5).getValueName());

		return new Tsne(newMaxIter, newPerplexity, newTetha, newParallel, usePCa, newInitialDims, false, true, 2);
	}

	@Override
	public Long getMinimumMemmory(int pointsNumber, int dimensionSize) {
		// TODO Auto-generated method stub
		return 0l;
	}
}
