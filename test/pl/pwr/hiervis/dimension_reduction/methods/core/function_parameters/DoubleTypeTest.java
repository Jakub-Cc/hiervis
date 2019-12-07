package pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DoubleTypeTest {

	@Test
	public void testDoubleTypeFunction2pOfDoubleFunction2pOfDouble() {
		DoubleType datatype = new DoubleType((points, dimensions) -> 0.0, (points, dimensions) -> 1.0);
		assertNotNull(datatype);
	}

	@Test
	public void testDoubleTypeFunction2pOfDoubleFunction2pOfDoubleFunction2pOfDouble() {
		DoubleType datatype = new DoubleType((points, dimensions) -> 0.5,
				(points, dimensions) -> (Math.floor((points - 1) / 3)),
				(points, dimensions) -> Math.max(0.5, Math.min(20.0, Math.floor((points - 1) / 3))));
		assertNotNull(datatype);
	}

}
