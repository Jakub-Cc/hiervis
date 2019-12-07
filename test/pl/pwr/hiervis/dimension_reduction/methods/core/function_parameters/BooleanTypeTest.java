package pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class BooleanTypeTest {

	BooleanType datatype;

	@Before
	public void init() {
		datatype = new BooleanType(true, "s1");
	}

	@Test
	public void testBooleanTypeBooleanFunction2pOfIntegerStringArray() {
		datatype = new BooleanType(false, (n, d) -> ((Integer) 1), "s1");
		assertNotNull(datatype);
	}

	@Test
	public void testGetDefaultValue() {
		assertEquals(true, datatype.getDefaultValue());
	}

	@Test
	public void testGetEnableParameterNames() {
		assertTrue(Arrays.equals(new String[] { "s1" }, datatype.getEnableParameterNames()));
	}

	@Test
	public void testIsEnabled() {
		assertEquals((Integer) 1, datatype.isEnabled().function(10, 10));
	}

}
