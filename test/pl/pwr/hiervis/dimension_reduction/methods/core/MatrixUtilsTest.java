package pl.pwr.hiervis.dimension_reduction.methods.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.pwr.hiervis.dimension_reduction.distance_measures.Euclidean;

public class MatrixUtilsTest {

	double[][] matrix1;
	double[][] matrix2;
	double[][] matrix3;
	double epsilon;

	@Before
	public void initialize() {
		matrix1 = new double[][] { { 1 }, { 2 } };
		matrix2 = new double[][] { { 1, 1 }, { 1, 1 } };
		matrix3 = new double[][] { { 1, 2 }, { 0, 0 }, { 2, 4 } };
		epsilon = 0;
	}

	@Test
	public void testTransposeMatrix() {
		matrix2 = MatrixUtils.transposeMatrix(matrix1);
		assertEquals(matrix2.length, 1);
		assertEquals(matrix2[0].length, 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMatrixNull() {
		MatrixUtils.transposeMatrix(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMatrix0Length() {
		MatrixUtils.transposeMatrix(new double[0][1]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMatrixSecondNull() {
		double[][] mat = new double[1][0];
		mat[0] = null;
		MatrixUtils.transposeMatrix(mat);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMatrixSecond0Length() {
		MatrixUtils.transposeMatrix(new double[1][0]);
	}

	@Test
	public void testStandaryzeMatrix() {
		MatrixUtils.standaryzeMatrix(matrix2);
		MatrixUtils.standaryzeMatrix(matrix3);
		assertEquals(matrix2[0][0], 0, epsilon);
		assertEquals(matrix2[0][1], 0, epsilon);
		assertEquals(matrix3[0][0], 0, epsilon);
		assertEquals(matrix3[0][1], 0, epsilon);
	}

	@Test
	public void testLinearlyTransformMatrix() {
		MatrixUtils.linearlyTransformMatrix(matrix2);
		MatrixUtils.linearlyTransformMatrix(matrix3);
		assertEquals(matrix2[0][0], 0, epsilon);
		assertEquals(matrix2[0][1], 0, epsilon);
		assertEquals(matrix3[0][0], 0.5, epsilon);
		assertEquals(matrix3[0][1], 0.5, epsilon);
	}

	@Test
	public void testDeepCopy() {
		matrix2 = matrix1;
		matrix3 = MatrixUtils.deepCopy(matrix1);

		assertSame(matrix1, matrix2);
		assertNotSame(matrix1, matrix3);
	}

	@Test
	public void testMultiplicateMatrix() {
		matrix3 = MatrixUtils.multiplicateMatrix(matrix2, matrix1);
		assertEquals(matrix3.length, 2, epsilon);
		assertEquals(matrix3[0].length, 1, epsilon);

		assertEquals(matrix3[0][0], 3, epsilon);
		assertEquals(matrix3[1][0], 3, epsilon);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMultiplicateMatrixThrowException() {
		MatrixUtils.multiplicateMatrix(matrix1, matrix2);
	}

	@Test
	public void testSpearmanCorrelation() {
		compareMatrix(new double[][] { { 1, Double.NaN }, { Double.NaN, 1 } },
				MatrixUtils.spearmanCorrelation(matrix2));
	}

	@Test
	public void testUseFunctionRowWise() {
		compareMatrix(new double[][] { { 0, -1 }, { 0, -1 } },
				MatrixUtils.useFunctionRowWise(matrix2, new double[] { 1, 2 }, (a, b) -> a - b));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUseFunctionRowWiseThrowException() {
		MatrixUtils.useFunctionRowWise(matrix2, new double[] { 1 }, (a, b) -> a - b);
	}

	@Test
	public void testUseFunctionOnMatrixesTransposeA() {
		compareMatrix(new double[][] { { 0, 0 }, { 0, 0 } },
				MatrixUtils.useFunctionOnMatrixesTransposeA((a, b) -> a - b, matrix2, matrix2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUseFunctionOnMatrixesTransposeAThrowsError1() {
		MatrixUtils.useFunctionOnMatrixesTransposeA((a, b) -> a - b, matrix2, matrix1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUseFunctionOnMatrixesTransposeAThrowsError2() {
		MatrixUtils.useFunctionOnMatrixesTransposeA((a, b) -> a - b, matrix2, matrix3);
	}

	@Test
	public void testUseFunctionOnMatrixes() {
		compareMatrix(new double[][] { { 0, 0 }, { 0, 0 } },
				MatrixUtils.useFunctionOnMatrixes((a, b) -> a - b, matrix2, matrix2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUseFunctionOnMatrixesThrowsError1() {
		MatrixUtils.useFunctionOnMatrixes((a, b) -> a - b, matrix2, matrix1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUseFunctionOnMatrixesThrowsError2() {
		MatrixUtils.useFunctionOnMatrixes((a, b) -> a - b, matrix2, matrix3);
	}

	@Test
	public void testEigenValues() {
		assertArrayEquals(new double[] { 2, 0 }, MatrixUtils.eigenValues(matrix2), 0.1);
	}

	@Test
	public void testEye() {
		compareMatrix(new double[][] { { 1, 0 }, { 0, 1 } }, MatrixUtils.eye(2));
	}

	@Test
	public void testAddMatrixes() {
		compareMatrix(new double[][] { { 2 }, { 4 } }, MatrixUtils.addMatrixes(matrix1, matrix1));
	}

	@Test
	public void testReplaceValue() {
		List<Double> valuesToReplace = new ArrayList<>();
		valuesToReplace.add(2.0);
		MatrixUtils.replaceValue(matrix1, 0, valuesToReplace);
		compareMatrix(new double[][] { { 1 }, { 0 } }, matrix1);
	}

	@Test
	public void testSumOfElementsDoubleArray() {
		assertEquals(3, MatrixUtils.sumOfElements(new double[] { 1, 2 }), 0.0);
	}

	@Test
	public void testSumOfElementsDoubleArrayArrayBoolean() {
		assertArrayEquals(new double[] { 3, 0, 6 }, MatrixUtils.sumOfElements(matrix3, true), 0.0);
		assertArrayEquals(new double[] { 3, 6 }, MatrixUtils.sumOfElements(matrix3, false), 0.0);
	}

	@Test
	public void testSortDescendant() {
		double[] v = new double[] { 1, 2 };
		assertArrayEquals(new int[] { 1, 0 }, MatrixUtils.sortDescendant(v));
		assertArrayEquals(new double[] { 2, 1 }, v, 0.0);
	}

	@Test
	public void testSortAscending() {
		double[] v = new double[] { 2, 1 };
		assertArrayEquals(new int[] { 1, 0 }, MatrixUtils.sortAscending(v));
		assertArrayEquals(new double[] { 1, 2 }, v, 0.0);
	}

	@Test
	public void testInverse() {
		double[][] d = new double[][] { { 1, 0 }, { 0, 1 } };
		compareMatrix(new double[][] { { 1, 0 }, { 0, 1 } }, MatrixUtils.inverse(d));
	}

	@Test
	public void testReplaceUndef() {
		double[][] d = new double[][] { { Double.NaN }, { Double.NEGATIVE_INFINITY }, { Double.POSITIVE_INFINITY },
				{ 1 } };
		MatrixUtils.replaceUndef(d, 2);
		compareMatrix(new double[][] { { 2 }, { 2 }, { 2 }, { 1 } }, d);
	}

	@Test
	public void testOnes() {
		compareMatrix(new double[][] { { 1, 1 }, { 1, 1 } }, MatrixUtils.ones(2, 2));
	}

	@Test
	public void testTransformValues() {
		MatrixUtils.transformValues(matrix2, e -> 2 * e);
		compareMatrix(new double[][] { { 2, 2 }, { 2, 2 } }, matrix2);
	}

	@Test
	public void testVectorToMatrix() {
		compareMatrix(new double[][] { { 1 }, { 2 } }, MatrixUtils.vectorToMatrix(new double[] { 1, 2 }, true));
		compareMatrix(new double[][] { { 1, 2 } }, MatrixUtils.vectorToMatrix(new double[] { 1, 2 }, false));
	}

	@Test
	public void testMultiplicateMatrixesWrap() {
		compareMatrix(new double[][] { { 2, 2 }, { 2, 2 } }, MatrixUtils.multiplicateMatrixesWrap(matrix2, matrix2));
	}

	@Test
	public void testTransformToDiagonalMatrix() {
		compareMatrix(new double[][] { { 1, 0 }, { 0, 2 } },
				MatrixUtils.transformToDiagonalMatrix(new double[] { 1, 2 }));
	}

	@Test
	public void testTranspose() {
		assertArrayEquals(new double[][] { { 0, 1 } }[0], MatrixUtils.transpose(new double[][] { { 0 }, { 1 } })[0],
				0.0);
	}

	@Test
	public void testStandardDeviation() {
		assertArrayEquals(new double[] { 0, 0 }, MatrixUtils.standardDeviation(matrix1, true), 0.0);
		assertArrayEquals(new double[] { 0.6 }, MatrixUtils.standardDeviation(matrix1, false), 0.2);
	}

	@Test
	public void testMean() {
		assertArrayEquals(new double[] { 1, 2 }, MatrixUtils.mean(matrix1, true), 0.0);
		assertArrayEquals(new double[] { 1.5 }, MatrixUtils.mean(matrix1, false), 0.0);
	}

	@Test
	public void testGenerateDistanceMatrix() {
		assertArrayEquals(new double[][] { { 0, 1 }, { 1, 0 } }[0],
				MatrixUtils.generateDistanceMatrix(matrix1, new Euclidean())[0], 0.0);
		assertArrayEquals(new double[][] { { 0, 1 }, { 1, 0 } }[1],
				MatrixUtils.generateDistanceMatrix(matrix1, new Euclidean())[1], 0.0);
	}

	@Test
	public void testCrossProduct() {
		assertArrayEquals(new double[][] { { 2 }, { 2 } }[0],
				MatrixUtils.crossProduct((a, b) -> a * b, new double[] { 1 }, new double[] { 2 })[0], 0.0);

	}

	@Test
	public void testMinDoubleArrayArray() {
		assertEquals((Double) 1.0, (Double) MatrixUtils.min(matrix1));
		assertEquals(0.0, MatrixUtils.min(new double[][] { { 1, 0, 1 }, { 1, 0, 1 } }), 0.0);
		assertEquals(0.0, MatrixUtils.min(new double[][] { { 0, 2, 1 }, { 1, 0, 1 } }), 0.0);
	}

	@Test
	public void testMinDoubleArray() {
		assertEquals(0.0, MatrixUtils.min(new double[] { 1, 0, 1 }), 0.0);
		assertEquals(0.0, MatrixUtils.min(new double[] { 0, 2, 1 }), 0.0);
	}

	@Test
	public void testMinByRow() {
		assertArrayEquals(new double[] { 1, 2 }, MatrixUtils.minByRow(matrix1), 0.0);

	}

	@Test
	public void testMaxDoubleArray() {
		assertEquals(2.0, MatrixUtils.max(new double[] { 1, 2, 1 }), 0.0);
		assertEquals(2.0, MatrixUtils.max(new double[] { 2, 2, 1 }), 0.0);
	}

	@Test
	public void testMaxDoubleArrayArray() {
		assertEquals((Double) 2.0, (Double) MatrixUtils.max(matrix1));
	}

	@Test
	public void testDropSingleton() {
		assertEquals(2, MatrixUtils.dropSingleton(matrix1).length);
		double[][] matrix = new double[][] { { 2 } };
		assertEquals(1, MatrixUtils.dropSingleton(matrix).length);
		double[][] matrix5 = new double[][] { { 1 }, { 2, 1 } };
		assertEquals(2, MatrixUtils.dropSingleton(matrix5).length);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDropSingletonThrowException() {
		assertEquals(1, MatrixUtils.dropSingleton(matrix2));
	}

	private void compareMatrix(double[][] a, double[][] b) {
		if (a.length != b.length)
			fail("a.length!=b.length");
		if (a[0].length != b[0].length)
			fail("a[0].length!=b[0].length");

		for (int i = 0; i < a.length; i++) {
			assertArrayEquals(a[i], b[i], 0.0);
		}
	}
}
