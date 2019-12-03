package pl.pwr.hiervis.dimension_reduction.methods.core;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import pl.pwr.hiervis.dimension_reduction.distance_measures.DistanceMeasure;

public class MatrixUtils {

	private static final String DID_NOT_MATCH_B_COLUMNS = " did not match B:Columns ";
	private static final String A_ROWS = "A:Rows: ";

	private MatrixUtils() {
		throw new AssertionError("Static class");
	}

	/**
	 * Checks if given matrix is real a matrix
	 * 
	 * @param matrix
	 * @throws IllegalArgumentException
	 */
	private static void checkIfMatrixCorrect(double[][] matrix) {
		if (matrix == null)
			throw new IllegalArgumentException("Matrix is null");
		if (matrix.length == 0)
			throw new IllegalArgumentException("First input matrix dimension is 0");
		if (matrix[0] == null || matrix[0].length == 0)
			throw new IllegalArgumentException("Second input matrix dimension is 0");
	}

	/**
	 * Transposes given matrix and returns it transposition
	 * 
	 * @param data matrix
	 * @return the transposed matrix
	 */
	public static double[][] transposeMatrix(double[][] matrix) {
		checkIfMatrixCorrect(matrix);

		int n = matrix.length;
		int m = matrix[0].length;

		double[][] newmatrix = new double[m][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				newmatrix[j][i] = matrix[i][j];
			}
		}

		return newmatrix;
	}

	/**
	 * Performs standardization on given matrix, changes given matrix
	 * 
	 * @param matrix
	 * @throws IllegalArgumentException
	 */
	public static void standaryzeMatrix(double[][] matrix) {
		checkIfMatrixCorrect(matrix);

		for (int i = 0; i < matrix[0].length; i++) {
			standaryzeColum(matrix, i);
		}
	}

	private static void standaryzeColum(double[][] matrix, int columnIndex) {
		double mean = 0;
		double deviation = 0;
		double variance = 0;

		int pointsAmount = matrix.length;

		for (int i = 0; i < pointsAmount; i++) {
			mean += matrix[i][columnIndex] / pointsAmount;
		}
		for (int i = 0; i < pointsAmount; i++) {
			variance += (matrix[i][columnIndex] - mean) * (matrix[i][columnIndex] - mean) / pointsAmount;
		}

		deviation = Math.pow(variance, 0.5);
		if (deviation != 0)
			for (int i = 0; i < pointsAmount; i++) {
				matrix[i][columnIndex] = (matrix[i][columnIndex] - mean) / deviation;
			}
		else
			for (int i = 0; i < pointsAmount; i++) {
				matrix[i][columnIndex] = 0;
			}
	}

	/**
	 * Performs linearization on given matrix, changes given matrix
	 * 
	 * @param matrix
	 * @throws IllegalArgumentException
	 */
	public static void linearlyTransformMatrix(double[][] matrix) {
		checkIfMatrixCorrect(matrix);

		for (int i = 0; i < matrix[0].length; i++) {
			linearlyTransformColum(matrix, i);
		}
	}

	private static void linearlyTransformColum(double[][] matrix, int index) {
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		int pointsAmount = matrix.length;

		for (int i = 0; i < pointsAmount; i++) {
			if (min > matrix[i][index]) {
				min = matrix[i][index];
			}
			if (max < matrix[i][index]) {
				max = matrix[i][index];
			}
		}

		double range = max - min;
		if (range != 0)
			for (int i = 0; i < pointsAmount; i++) {
				matrix[i][index] = (matrix[i][index] - min) / range;
			}
		else {
			for (int i = 0; i < pointsAmount; i++) {
				matrix[i][index] = 0;
			}
		}
	}

	public static double[][] deepCopy(double[][] matrix) {
		checkIfMatrixCorrect(matrix);

		double[][] output = new double[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			output[i] = matrix[i].clone();
		}

		return output;
	}

	/**
	 * Multiplies matrixes A*B
	 * 
	 * @param a matrix
	 * @param b matrix
	 * @return A*B
	 */
	public static double[][] multiplicateMatrix(double[][] a, double[][] b) {
		int aRows = a.length;
		int aColumns = a[0].length;
		int bRows = b.length;
		int bColumns = b[0].length;

		if (aColumns != bRows) {
			throw new IllegalArgumentException(A_ROWS + aColumns + DID_NOT_MATCH_B_COLUMNS + bRows + ".");
		}

		double[][] c = new double[aRows][bColumns];
		for (int i = 0; i < aRows; i++) {
			for (int j = 0; j < bColumns; j++) {
				c[i][j] = 0.00000;
			}
		}

		for (int i = 0; i < aRows; i++) { // aRow
			for (int j = 0; j < bColumns; j++) { // bColumn
				for (int k = 0; k < aColumns; k++) { // aColumn
					c[i][j] += a[i][k] * b[k][j];
				}
			}
		}

		return c;
	}

	public static double[][] spearmanCorrelation(double[][] matrix) {
		SpearmansCorrelation spearmansCorrelation = new SpearmansCorrelation();
		RealMatrix realMatrix = spearmansCorrelation.computeCorrelationMatrix(matrix);
		return realMatrix.getData();
	}

	/*
	 * result[m,n]=func(matrix[n,m] , vector [n]) performes matrix'*diag(vector) if
	 * function is (a,b)->a*b
	 */
	public static double[][] useFunctionRowWise(double[][] matrix, double[] vector, Function2p<Double> func) {
		int aRows = matrix.length;
		int aColumns = matrix[0].length;
		int bRows = vector.length;

		if (aRows != bRows) {
			throw new IllegalArgumentException("Matrix:Rows: " + aRows + " did not match Vector:Rows " + bRows + ".");
		}

		double[][] result = new double[aColumns][aRows];

		for (int i = 0; i < aColumns; i++) {
			for (int j = 0; j < aRows; j++) {
				result[i][j] = func.function(matrix[j][i], vector[j]);
			}
		}
		return result;
	}

	/*
	 * Same as useFunctionOnMatrixes but A is used A'
	 */
	public static double[][] useFunctionOnMatrixesTransposeA(Function2p<Double> function, double[][] a, double[][] b) {
		int aRows = a.length;
		int aColumns = a[0].length;
		int bRows = b.length;
		int bColumns = b[0].length;

		if (aRows != bColumns) {
			throw new IllegalArgumentException(A_ROWS + aRows + DID_NOT_MATCH_B_COLUMNS + bRows + ".");
		} else if (aColumns != bRows) {
			throw new IllegalArgumentException("A:Columns: " + aColumns + " did not match B:Rows " + bColumns + ".");
		}

		double[][] result = new double[aColumns][aRows];
		for (int i = 0; i < aColumns; i++) {
			for (int j = 0; j < aRows; j++) {
				result[i][j] = function.function(a[j][i], b[i][j]);
			}
		}
		return result;
	}

	public static double[][] useFunctionOnMatrixes(Function2p<Double> function, double[][] a, double[][] b) {
		int aRows = a.length;
		int aColumns = a[0].length;
		int bRows = b.length;
		int bColumns = b[0].length;

		if (aRows != bRows) {
			throw new IllegalArgumentException(A_ROWS + aRows + " did not match B:Rows " + bRows + ".");
		} else if (aColumns != bColumns) {
			throw new IllegalArgumentException("A:Columns: " + aColumns + DID_NOT_MATCH_B_COLUMNS + bColumns + ".");
		}

		double[][] result = new double[aRows][aColumns];
		for (int i = 0; i < aRows; i++) {
			for (int j = 0; j < aColumns; j++) {
				result[i][j] = function.function(a[i][j], b[i][j]);
			}
		}
		return result;
	}

	public static double[] eigenValues(double[][] matrix) {
		RealMatrix realMatrix = org.apache.commons.math3.linear.MatrixUtils.createRealMatrix(matrix);
		EigenDecomposition decomposition = new EigenDecomposition(realMatrix);
		return decomposition.getRealEigenvalues();
	}

	public static double[][] eye(int size) {
		int firstDimSize = size;
		int secondDimSize = size;

		double[][] result = new double[firstDimSize][secondDimSize];
		for (int i = 0; i < firstDimSize; i++) {
			for (int j = 0; j < secondDimSize; j++) {
				result[i][j] = 0;
			}
			result[i][i] = 1;
		}
		return result;
	}

	public static double[][] addMatrixes(double[][] a, double[][] b) {
		int firstDimSize = a.length;
		int secondDimSize = a[0].length;

		double[][] result = new double[firstDimSize][secondDimSize];
		for (int i = 0; i < firstDimSize; i++) {
			for (int j = 0; j < secondDimSize; j++) {
				result[i][j] = a[i][j] + b[i][j];
			}
		}
		return result;
	}

	public static void replaceValue(double[][] matrix, double valueToPut, List<Double> valuesToReplace) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (valuesToReplace.contains(matrix[i][j]))
					matrix[i][j] = valueToPut;
			}
		}
	}

	public static double sumOfElements(double[] vector) {
		double result = 0;
		for (int i = 0; i < vector.length; i++) {
			result += vector[i];
		}
		return result;
	}

	public static double[] sumOfElements(double[][] matrix, boolean firstDim) {
		if (firstDim)
			return sumOfElementsFirstDim(matrix);
		return sumOfElementsSecondDim(matrix);
	}

	private static double[] sumOfElementsFirstDim(double[][] matrix) {
		int firstDimSize = matrix.length;
		int secondDimSize = matrix[0].length;

		double[] sum = new double[firstDimSize];

		for (int i = 0; i < firstDimSize; i++) {
			sum[i] = 0;
			for (int j = 0; j < secondDimSize; j++) {
				sum[i] += matrix[i][j];
			}
		}
		return sum;
	}

	public static int[] sortDescendant(double[] vector) {
		int[] result = new int[vector.length];

		for (int i = 0; i < vector.length; i++) {
			result[i] = i;
		}
		for (int i = 0; i < vector.length; i++) {
			for (int j = i + 1; j < vector.length; j++) {
				if (vector[i] < vector[j]) {
					double temp = vector[i];
					vector[i] = vector[j];
					vector[j] = temp;
					int tmp = result[i];
					result[i] = result[j];
					result[j] = tmp;
				}
			}
		}

		return result;
	}

	/**
	 * Sorts the array ascending, and return array of old indexes based on new order
	 * 
	 * @param vector
	 * @return
	 */
	public static int[] sortAscending(double[] vector) {
		int[] result = new int[vector.length];

		for (int i = 0; i < vector.length; i++) {
			result[i] = i;
		}
		for (int i = 0; i < vector.length; i++) {
			for (int j = i + 1; j < vector.length; j++) {
				if (vector[i] > vector[j]) {
					double temp = vector[i];
					vector[i] = vector[j];
					vector[j] = temp;
					int tmp = result[i];
					result[i] = result[j];
					result[j] = tmp;
				}
			}
		}

		return result;
	}

	public static void printMatrix(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.printf("%.4f", matrix[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	public static void printMatrix(double[] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			System.out.print(matrix[i] + " ");
		}
		System.out.println();
	}

	public static double[][] inverse(double[][] matrix) {
		RealMatrix realMatrix = org.apache.commons.math3.linear.MatrixUtils.createRealMatrix(matrix);
		RealMatrix inversedMatrix = org.apache.commons.math3.linear.MatrixUtils.inverse(realMatrix);

		return inversedMatrix.getData();
	}

	private static double[] sumOfElementsSecondDim(double[][] matrix) {
		int firstDimSize = matrix.length;
		int secondDimSize = matrix[0].length;

		double[] sum = new double[secondDimSize];

		for (int i = 0; i < secondDimSize; i++) {
			sum[i] = 0;
			for (int j = 0; j < firstDimSize; j++) {
				sum[i] += matrix[j][i];
			}
		}
		return sum;
	}

	public static void replaceUndef(double[][] matrix, double valueToPut) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (Double.isNaN(matrix[i][j]) || matrix[i][j] == Double.NEGATIVE_INFINITY
						|| matrix[i][j] == Double.POSITIVE_INFINITY) {
					matrix[i][j] = valueToPut;
				}
			}
		}
	}

	public static double[][] ones(int rows, int columns) {
		double[][] result = new double[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				result[j][i] = 1;
			}
		}
		return result;
	}

	public static void transformValues(double[][] matrix, Function function) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = function.f(matrix[i][j]);
			}
		}

	}

	private static double[][] vectorToMatrixColumn(double[] vector) {
		int size = vector.length;
		double[][] matrix = new double[size][1];
		for (int i = 0; i < size; i++) {
			matrix[i][0] = vector[i];
		}
		return matrix;
	}

	private static double[][] vectorToMatrixRow(double[] vector) {
		int size = vector.length;
		double[][] matrix = new double[1][size];
		matrix[0] = Arrays.copyOf(vector, vector.length);
		return matrix;
	}

	public static double[][] vectorToMatrix(double[] vector, boolean vectorInFirstDim) {
		if (vectorInFirstDim)
			return vectorToMatrixColumn(vector);
		return vectorToMatrixRow(vector);
	}

	/**
	 * Using apache.commons-math3 matrix implementation to wrap given matrixes to
	 * perform multiplication more efficient on big matrixes
	 */
	public static double[][] multiplicateMatrixesWrap(double[][] a, double[][] b) {
		RealMatrix realMatrix = org.apache.commons.math3.linear.MatrixUtils.createRealMatrix(a);
		RealMatrix realMatrix2 = org.apache.commons.math3.linear.MatrixUtils.createRealMatrix(b);
		realMatrix = realMatrix.multiply(realMatrix2);

		return realMatrix.getData();
	}

	public static double[][] transformToDiagonalMatrix(double[] matrix) {
		int size = matrix.length;
		double[][] result = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				result[i][j] = 0;
			}
			result[i][i] = matrix[i];
		}
		return result;
	}

	public static double[][] transpose(double[][] matrix) {
		int firstDimSize = matrix.length;
		int secondDimSize = matrix[0].length;

		double[][] transposed = new double[secondDimSize][firstDimSize];

		for (int i = 0; i < firstDimSize; i++) {
			for (int j = 0; j < secondDimSize; j++) {
				transposed[j][i] = matrix[i][j];
			}
		}
		return transposed;
	}

	public static double[] standardDeviation(double[][] matrix, boolean fistDim) {
		if (fistDim)
			return standardDeviationFirstDim(matrix);
		return standardDeviationSecondDim(matrix);
	}

	private static double[] standardDeviationFirstDim(double[][] matrix) {
		int firstDimSize = matrix.length;
		int secondDimSize = matrix[0].length;

		double[] standardDeviation = new double[firstDimSize];
		double[] mean = mean(matrix, true);

		for (int i = 0; i < firstDimSize; i++) {
			standardDeviation[i] = 0;
			for (int j = 0; j < secondDimSize; j++) {
				standardDeviation[i] += Math.pow(matrix[i][j] - mean[i], 2) / (firstDimSize - 1);
			}
		}
		return standardDeviation;
	}

	public static double[] standardDeviationSecondDim(double[][] matrix) {
		int firstDimSize = matrix.length;
		int secondDimSize = matrix[0].length;

		double[] standardDeviation = new double[secondDimSize];
		double[] mean = mean(matrix, false);

		for (int i = 0; i < secondDimSize; i++) {
			standardDeviation[i] = 0;
			for (int j = 0; j < firstDimSize; j++) {
				standardDeviation[i] += Math.pow(matrix[j][i] - mean[i], 2) / (firstDimSize - 1);
			}
			standardDeviation[i] = Math.sqrt(standardDeviation[i]);
		}

		return standardDeviation;
	}

	/*
	 * calculate mean of matrix, by first or second dimension
	 */
	public static double[] mean(double[][] matrix, boolean firstDim) {
		if (firstDim)
			return meanFirstDim(matrix);
		return meanSecondDim(matrix);
	}

	private static double[] meanFirstDim(double[][] matrix) {
		double[] mean = new double[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			mean[i] = 0;
			for (int j = 0; j < matrix[i].length; j++) {
				mean[i] += matrix[i][j] / matrix[i].length;
			}
		}

		return mean;
	}

	private static double[] meanSecondDim(double[][] matrix) {
		int m = matrix.length;
		int n = matrix[0].length;

		double[] mean = new double[n];

		for (int i = 0; i < n; i++) {
			mean[i] = 0;
			for (int j = 0; j < m; j++) {
				mean[i] += matrix[j][i] / m;
			}
		}
		return mean;
	}

	public static double[][] generateDistanceMatrix(double[][] matrix, DistanceMeasure distanceMeasure) {
		double[][] output = new double[matrix.length][matrix.length];

		for (int i = 0; i < matrix.length; i++) {
			for (int j = i + 1; j < matrix.length; j++) {
				output[i][j] = distanceMeasure.getDistance(matrix[i], matrix[j]);
				output[j][i] = output[i][j];
			}
		}

		return output;
	}

	public static double[][] crossProduct(Function2p<Double> function, double[] a, double[] b) {
		double[][] result = new double[a.length][b.length];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				result[i][j] = function.function(a[i], b[j]);
			}
		}

		return result;
	}

	public static double min(double[][] matrix) {
		double min = matrix[0][0];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (min > matrix[i][j]) {
					min = matrix[i][j];
				}
			}
		}
		return min;
	}

	public static double min(double[] matrix) {
		double min = matrix[0];
		for (int i = 1; i < matrix.length; i++) {
			if (min > matrix[i])
				min = matrix[i];
		}
		return min;
	}

	public static double[] minByRow(double[][] matrix) {

		double[] min = new double[matrix.length];

		for (int i = 0; i < matrix.length; i++) {
			min[i] = min(matrix[i]);
		}
		return min;
	}

	public static double max(double[] vactor) {
		double max = vactor[0];
		for (int i = 1; i < vactor.length; i++) {
			if (max < vactor[i]) {
				max = vactor[i];
			}
		}
		return max;
	}

	public static double max(double[][] matrix) {
		double max = matrix[0][0];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (max < matrix[i][j]) {
					max = matrix[i][j];
				}
			}
		}
		return max;
	}

	public static double[] dropSingleton(double[][] matrix) {
		if (matrix.length == 1) {
			return matrix[0];
		} else if (matrix[0].length == 1) {
			double[] result = new double[matrix.length];
			for (int i = 0; i < matrix.length; i++) {
				result[i] = matrix[i][0];
			}
			return result;
		} else {
			throw new IllegalArgumentException("Singleton dimnsion don't exist");
		}
	}

}
