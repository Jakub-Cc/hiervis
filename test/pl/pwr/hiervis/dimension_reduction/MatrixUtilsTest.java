package pl.pwr.hiervis.dimension_reduction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import pl.pwr.hiervis.dimension_reduction.methods.core.MatrixUtils;

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
}
