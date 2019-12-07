package pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class FunctionParametersTest {

	FunctionParameters f;

	@Before
	public void init() {
		DataType dataType = new BooleanType(true);
		f = FunctionParameters.createFunctionParameter("name", "desc", dataType);
	}

	@Test
	public void testGetValueClass() {
		assertEquals(FunctionParametersConst.BOOLEAN, f.getValueClass().getClass());
	}

	@Test
	public void testGetValueName() {
		assertEquals("name", f.getValueName());
	}

	@Test
	public void testGetValueDescription() {
		assertEquals("desc", f.getValueDescription());
	}

}
