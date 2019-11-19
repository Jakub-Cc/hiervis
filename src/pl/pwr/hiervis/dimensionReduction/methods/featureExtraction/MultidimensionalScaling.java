package pl.pwr.hiervis.dimensionReduction.methods.featureExtraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.interfaces.Hierarchy;
import mdsj.MDSJ;
import pl.pwr.hiervis.dimensionReduction.distanceMeasures.DistanceMeasure;
import pl.pwr.hiervis.dimensionReduction.distanceMeasures.Euclidean;
import pl.pwr.hiervis.dimensionReduction.distanceMeasures.Manhattan;
import pl.pwr.hiervis.dimensionReduction.distanceMeasures.Minkowski;
import pl.pwr.hiervis.dimensionReduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters.DoubleType;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters.ListType;
import pl.pwr.hiervis.dimensionReduction.methods.core.MatrixUtils;

public class MultidimensionalScaling extends FeatureExtraction {
	private DistanceMeasure distanceMeasure;

	public MultidimensionalScaling() {
		distanceMeasure = new Euclidean();
	}

	public MultidimensionalScaling(DistanceMeasure distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}

	@Override
	public Hierarchy reduceHierarchy(Hierarchy source) {

		double[][] output = generateDissimilarityMatrix(HierarchyUtils.toMatrix(source));

		System.out.println("Calculating MDS");
		output = MDSJ.classicalScaling(output); // apply MDS
		System.out.println("Finished Calculating MDS");

		output = MatrixUtils.TransposeMatrix(output);

		Hierarchy newHier = HierarchyUtils.clone(source, true, null);

		for (int i = 0; i < newHier.getOverallNumberOfInstances(); i++) {
			newHier.getRoot().getSubtreeInstances().get(i).setData(output[i]);
		}
		newHier.deleteDataNames();

		return newHier;

	}

	private double[][] generateDissimilarityMatrix(double[][] matrix) {
		System.out.println("Calculating Dissimilarity Matrix");

		double[][] output = new double[matrix.length][matrix.length];

		for (int i = 0; i < matrix.length; i++) {
			for (int j = i + 1; j < matrix.length; j++) {
				output[i][j] = distanceMeasure.getDistance(matrix[i], matrix[j]);
				output[j][i] = output[i][j];
			}
		}
		System.out.println("Finishing Calculating Dissimilarity Matrix");
		return output;
	}

	@Override
	public String getName() {
		return "Multidimensional Scaling";
	}

	@Override
	public String getSimpleName() {
		return "MDS";
	}

	@Override
	public String getDescription() {
		return "";
	}

	public static String sGetName() {
		return "Multidimensional Scaling";
	}

	public static String sGetSimpleName() {
		return "MDS";
	}

	public static String sGetDescription() {
		return "";
	}

	@Override
	public List<FunctionParameters> getParameters() {
		List<FunctionParameters> functionParameters = new ArrayList<FunctionParameters>();

		List<DistanceMeasure> list = new ArrayList<>();
		list.add(new Euclidean());
		list.add(new Manhattan());
		list.add(new Minkowski());

		Map<DistanceMeasure, String[]> enables = new HashMap<>();
		enables.put(new Minkowski(), new String[] { "Minkowski p value" });

		functionParameters.add(FunctionParameters.CreateFunctionParameter("Distance Measure",
				"Distance measure for calculation of disimilarity matrix",
				new ListType<DistanceMeasure>(list, enables)));

		functionParameters.add(FunctionParameters.CreateFunctionParameter("Minkowski p value",
				"Power value for Minkowski distance measure",
				new DoubleType((points, dimensions) -> -10.0, (points, dimensions) -> 10.0)));

		return functionParameters;
	}

	@Override
	public DimensionReductionI createInstance(Map<String, Object> parameters) {

		DistanceMeasure distanceMeasure = (DistanceMeasure) parameters.get(getParameters().get(0).getValueName());

		if (distanceMeasure.getClass() == Minkowski.class) {
			double power = (double) parameters.get(getParameters().get(1).getValueName());
			distanceMeasure = new Minkowski(power);
		}

		return new MultidimensionalScaling(distanceMeasure);
	}
}
