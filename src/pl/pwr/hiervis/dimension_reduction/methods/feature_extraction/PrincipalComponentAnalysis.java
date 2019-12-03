package pl.pwr.hiervis.dimension_reduction.methods.feature_extraction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimension_reduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimension_reduction.methods.core.FunctionParameters;
import pl.pwr.hiervis.dimension_reduction.methods.core.FunctionParameters.IntegerType;

public class PrincipalComponentAnalysis implements FeatureExtraction {

	public static final String DESCRIPTION = "";
	public static final String PCA = "PCA";
	public static final String PRINCIPAL_COMPONENT_ANALYSIS = "Principal Component Analysis";
	private int targerDimension;

	public PrincipalComponentAnalysis(int targetDimension) {
		this.targerDimension = targetDimension;
	}

	public PrincipalComponentAnalysis() {
		targerDimension = 2;
	}

	@Override
	public Hierarchy reduceHierarchy(Hierarchy source) {
		double[][] matrix = HierarchyUtils.toMatrix(source);

		com.jujutsu.tsne.PrincipalComponentAnalysis pca = new com.jujutsu.tsne.PrincipalComponentAnalysis();
		double[][] output = pca.pca(matrix, targerDimension);

		Hierarchy newHier = HierarchyUtils.clone(source, true, null);

		for (int i = 0; i < newHier.getOverallNumberOfInstances(); i++) {
			newHier.getRoot().getSubtreeInstances().get(i).setData(output[i]);
		}
		newHier.deleteDataNames();

		return newHier;

	}

	@Override
	public String getName() {
		return PRINCIPAL_COMPONENT_ANALYSIS;
	}

	@Override
	public String getSimpleName() {
		return PCA;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	public static String sGetName() {
		return PRINCIPAL_COMPONENT_ANALYSIS;
	}

	public static String sGetSimpleName() {
		return PCA;
	}

	public static String sGetDescription() {
		return DESCRIPTION;
	}

	@Override
	public List<FunctionParameters> getParameters() {
		List<FunctionParameters> functionParameters = new ArrayList<>();

		functionParameters.add(FunctionParameters.createFunctionParameter("PCA initial dimensions",
				"Number of initial dimension for predimension reduction.", new IntegerType((points, dimensions) -> 2,
						(points, dimensions) -> dimensions, (points, dimensions) -> (Math.max(2, dimensions / 2)))));

		return functionParameters;
	}

	@Override
	public DimensionReductionI createInstance(Map<String, Object> parameters) {
		Integer initialDims = (Integer) parameters.get(getParameters().get(0).getValueName());
		return new PrincipalComponentAnalysis(initialDims);
	}

	@Override
	public Long getMinimumMemmory(int pointsNumber, int dimensionSize) {
		// TODO Auto-generated method stub
		return 0l;
	}
}
