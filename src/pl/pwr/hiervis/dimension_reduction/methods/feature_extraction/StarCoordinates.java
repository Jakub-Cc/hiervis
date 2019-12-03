package pl.pwr.hiervis.dimension_reduction.methods.feature_extraction;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimension_reduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimension_reduction.methods.core.FunctionParameters;
import pl.pwr.hiervis.dimension_reduction.methods.core.MatrixUtils;

public class StarCoordinates implements FeatureExtraction {
	public static final String DESCRIPTION = "";
	public static final String STAR_COORDINATES = "Star Coordinates";
	public static final String STAR_COORDS = "StarCoords";

	@Override
	public Hierarchy reduceHierarchy(Hierarchy source) {
		double[][] matrix = MatrixUtils.deepCopy(HierarchyUtils.toMatrix(source));

		MatrixUtils.linearlyTransformMatrix(matrix);
		int dimensions = matrix[0].length;

		double[][] projectionVector = new double[dimensions][2];

		for (int i = 1; i <= dimensions; i++) {
			projectionVector[i - 1][0] = Math.cos(2 * Math.PI * i / dimensions);
			projectionVector[i - 1][1] = Math.sin(2 * Math.PI * i / dimensions);
		}

		double[][] newMatrix = MatrixUtils.multiplicateMatrix(matrix, projectionVector);

		Hierarchy newHier = HierarchyUtils.clone(source, true, null);

		for (int i = 0; i < newHier.getOverallNumberOfInstances(); i++) {
			newHier.getRoot().getSubtreeInstances().get(i).setData(newMatrix[i]);
		}
		newHier.deleteDataNames();
		return newHier;
	}

	@Override
	public String getName() {
		return STAR_COORDINATES;
	}

	@Override
	public String getSimpleName() {
		return STAR_COORDS;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	public static String sGetName() {
		return STAR_COORDINATES;
	}

	public static String sGetSimpleName() {
		return STAR_COORDS;
	}

	public static String sGetDescription() {
		return DESCRIPTION;
	}

	@Override
	public List<FunctionParameters> getParameters() {
		return Collections.emptyList();
	}

	@Override
	public DimensionReductionI createInstance(Map<String, Object> parameters) {
		return new StarCoordinates();
	}

	@Override
	public Long getMinimumMemmory(int pointsNumber, int dimensionSize) {
		// TODO Auto-generated method stub
		return 0l;
	}
}
