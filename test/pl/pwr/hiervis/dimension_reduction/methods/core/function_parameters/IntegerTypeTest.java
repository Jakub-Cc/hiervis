package pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class IntegerTypeTest {

	@Test
	public void testIntegerTypeFunction2pOfIntegerFunction2pOfInteger() {
		IntegerType i = new IntegerType((points, dimensions) -> 100, (points, dimensions) -> 10000);
		assertNotNull(i);
	}

	@Test
	public void testIntegerTypeFunction2pOfIntegerFunction2pOfIntegerFunction2pOfInteger() {
		IntegerType i = new IntegerType((points, dimensions) -> 2, (points, dimensions) -> dimensions,
				(points, dimensions) -> (Math.max(2, dimensions / 2)));
		assertNotNull(i);
	}

}
