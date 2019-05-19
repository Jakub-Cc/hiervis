package pl.pwr.hiervis.dimensionReduction.methods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters.IntegerType;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.HierarchyUtils;

public class PrincipalComponentAnalysis extends FeatureExtraction {

    private int targerDimension;

    public PrincipalComponentAnalysis(int targetDimension) {
	this.targerDimension = targetDimension;
    }

    public PrincipalComponentAnalysis() {
	targerDimension = 2;
    }

    @Override
    public Hierarchy reduceHierarchy(LoadedHierarchy source) {
	double[][] matrix = HierarchyUtils.toMatrix(source.getHierarchyWraper().getOriginalHierarchy());

	com.jujutsu.tsne.PrincipalComponentAnalysis pca = new com.jujutsu.tsne.PrincipalComponentAnalysis();
	double[][] output = pca.pca(matrix, targerDimension);

	Hierarchy newHier = HierarchyUtils.clone(source.getMainHierarchy(), true, null);

	for (int i = 0; i < newHier.getOverallNumberOfInstances(); i++) {
	    newHier.getRoot().getSubtreeInstances().get(i).setData(output[i]);
	}
	newHier.deleteDataNames();

	return newHier;

    }

    @Override
    public String getName() {
	return "Principal Component Analysis";
    }

    @Override
    public String getSimpleName() {
	return "PCA";
    }

    @Override
    public String getDescription() {
	return "";
    }

    public static String sGetName() {
	return "Principal Component Analysis";
    }

    public static String sGetSimpleName() {
	return "PCA";
    }

    public static String sGetDescription() {
	return "";
    }

    @Override
    public List<FunctionParameters> getParameters() {
	List<FunctionParameters> functionParameters = new ArrayList<FunctionParameters>();

	functionParameters.add(FunctionParameters.CreateFunctionParameter("PCA initial dimensions",
		"Number of initial dimension for predimension reduction.", new IntegerType((points, dimensions) -> 2,
			(points, dimensions) -> dimensions, (points, dimensions) -> (Math.max(2, dimensions / 2)))));

	return functionParameters;
    }

    @Override
    public DimensionReductionI createInstance(Map<String, Object> parameters) {
	Integer initialDims = (Integer) parameters.get(getParameters().get(5).getValueName());
	return new PrincipalComponentAnalysis(initialDims);
    }
}
