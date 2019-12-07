package pl.pwr.hiervis.dimension_reduction.methods.feature_extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.interfaces.Hierarchy;
import mdsj.MDSJ;
import pl.pwr.hiervis.dimension_reduction.distance_measures.DistanceMeasure;
import pl.pwr.hiervis.dimension_reduction.distance_measures.Euclidean;
import pl.pwr.hiervis.dimension_reduction.distance_measures.Manhattan;
import pl.pwr.hiervis.dimension_reduction.distance_measures.Minkowski;
import pl.pwr.hiervis.dimension_reduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimension_reduction.methods.core.MatrixUtils;
import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.DoubleType;
import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.FunctionParameters;
import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.ListType;

public class MultidimensionalScaling implements FeatureExtraction {
	private static final Logger log = LogManager.getLogger(MultidimensionalScaling.class);
	public static final String DESCRIPTION = "";
	public static final String MDS = "MDS";
	public static final String MULTIDIMENSIONAL_SCALING = "Multidimensional Scaling";
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

		log.trace("Calculating MDS");
		output = MDSJ.classicalScaling(output); // apply MDS
		log.trace("Finished Calculating MDS");

		output = MatrixUtils.transposeMatrix(output);

		Hierarchy newHier = HierarchyUtils.clone(source, true, null);

		for (int i = 0; i < newHier.getOverallNumberOfInstances(); i++) {
			newHier.getRoot().getSubtreeInstances().get(i).setData(output[i]);
		}
		newHier.deleteDataNames();

		return newHier;

	}

	private double[][] generateDissimilarityMatrix(double[][] matrix) {
		log.trace("Calculating Dissimilarity Matrix");

		double[][] output = new double[matrix.length][matrix.length];

		for (int i = 0; i < matrix.length; i++) {
			for (int j = i + 1; j < matrix.length; j++) {
				output[i][j] = distanceMeasure.getDistance(matrix[i], matrix[j]);
				output[j][i] = output[i][j];
			}
		}
		log.trace("Finishing Calculating Dissimilarity Matrix");
		return output;
	}

	@Override
	public String getName() {
		return MULTIDIMENSIONAL_SCALING;
	}

	@Override
	public String getSimpleName() {
		return MDS;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	public static String sGetName() {
		return MULTIDIMENSIONAL_SCALING;
	}

	public static String sGetSimpleName() {
		return MDS;
	}

	public static String sGetDescription() {
		return DESCRIPTION;
	}

	@Override
	public List<FunctionParameters> getParameters() {
		List<FunctionParameters> functionParameters = new ArrayList<>();

		List<DistanceMeasure> list = new ArrayList<>();
		list.add(new Euclidean());
		list.add(new Manhattan());
		list.add(new Minkowski());

		Map<DistanceMeasure, String[]> enables = new HashMap<>();
		enables.put(new Minkowski(), new String[] { "Minkowski p value" });

		functionParameters.add(FunctionParameters.createFunctionParameter("Distance Measure",
				"Distance measure for calculation of disimilarity matrix",
				new ListType<DistanceMeasure>(list, enables)));

		functionParameters.add(FunctionParameters.createFunctionParameter("Minkowski p value",
				"Power value for Minkowski distance measure",
				new DoubleType((points, dimensions) -> -10.0, (points, dimensions) -> 10.0)));

		return functionParameters;
	}

	@Override
	public DimensionReductionI createInstance(Map<String, Object> parameters) {
		DistanceMeasure measure = (DistanceMeasure) parameters.get(getParameters().get(0).getValueName());
		if (measure == null) {
			log.error("Error when parsing parameters returning default");
			return this;
		}

		if (measure.getClass() == Minkowski.class) {
			double power = (double) parameters.get(getParameters().get(1).getValueName());
			measure = new Minkowski(power);
		}
		return new MultidimensionalScaling(measure);
	}

	@Override
	public Long getMinimumMemmory(int pointsNumber, int dimensionSize) {
		return 0l;
	}
}
