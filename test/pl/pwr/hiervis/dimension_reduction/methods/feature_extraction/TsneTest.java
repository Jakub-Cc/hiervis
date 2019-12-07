package pl.pwr.hiervis.dimension_reduction.methods.feature_extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimension_reduction.TestCommon;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.BooleanType;
import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.DoubleType;
import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.IntegerType;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class TsneTest {
	LoadedHierarchy loadedHierarchy;
	FeatureExtraction dimensionReduction;
	Hierarchy hierarchy;

	@Before
	public void initialize() {
		dimensionReduction = new Tsne(100, 1, 0.5, false, false, 3, true, false, 2);
		hierarchy = TestCommon.getTwoTwoGroupsHierarchy();
		loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
	}

	@Test
	public void testTsneParallel() {
		dimensionReduction = new Tsne(100, 1, 0.5, false, false, 3, true, false, 2);
		assertNotEquals(null, dimensionReduction);
		assertEquals(4, loadedHierarchy.getMainHierarchy().getRoot().getNodeInstances().getFirst().getData().length);
		Hierarchy testHierarchy = dimensionReduction.reduceHierarchy(loadedHierarchy.getMainHierarchy());
		assertEquals(2, testHierarchy.getRoot().getNodeInstances().getFirst().getData().length);
	}

	@Test
	public void testTsne() {
		dimensionReduction = null;
		assertEquals(null, dimensionReduction);
		dimensionReduction = new Tsne();
		assertNotEquals(null, dimensionReduction);
	}

	@Test
	public void testTsneBooleanIntIntIntDoubleBooleanDoubleBooleanBoolean() {
		assertNotEquals(null, dimensionReduction);
	}

	@Test
	public void testReduceHierarchy() {
		assertEquals(4, loadedHierarchy.getMainHierarchy().getRoot().getNodeInstances().getFirst().getData().length);
		Hierarchy tstHierarchy = dimensionReduction.reduceHierarchy(loadedHierarchy.getMainHierarchy());
		assertEquals(2, tstHierarchy.getRoot().getNodeInstances().getFirst().getData().length);
	}

	@Test
	public void testGetName() {
		assertEquals(Tsne.T_SNE_FULL_NAME, dimensionReduction.getName());
	}

	@Test
	public void testGetSimpleName() {
		assertEquals(Tsne.T_SNE, dimensionReduction.getSimpleName());
	}

	@Test
	public void testGetDescription() {
		assertEquals(Tsne.DSCRIPTION, dimensionReduction.getDescription());
	}

	@Test
	public void testSGetName() {
		assertEquals(Tsne.T_SNE_FULL_NAME, Tsne.sGetName());
	}

	@Test
	public void testSGetSimpleName() {
		assertEquals(Tsne.T_SNE, Tsne.sGetSimpleName());
	}

	@Test
	public void testSGetDescription() {
		assertEquals(Tsne.DSCRIPTION, Tsne.sGetDescription());
	}

	@Test
	public void testGetParameters() {
		assertNotNull(dimensionReduction.getParameters());
		assertTrue(!dimensionReduction.getParameters().isEmpty());

		IntegerType p1 = (IntegerType) dimensionReduction.getParameters().get(0).getValueClass();
		assertEquals((Integer) 100, p1.getMinimumF().function(10, 10));
		assertEquals((Integer) 10000, p1.getMaximumF().function(10, 10));
		assertEquals((Integer) 1000, p1.getDefaultValueF().function(10, 10));

		DoubleType p2 = (DoubleType) dimensionReduction.getParameters().get(1).getValueClass();
		assertEquals((Double) 0.5, p2.getMinimumF().function(10.0, 10.0));
		assertEquals((Double) 3.0, p2.getMaximumF().function(10.0, 10.0));
		assertEquals((Double) 3.0, p2.getDefaultValueF().function(10.0, 10.0));

		DoubleType p3 = (DoubleType) dimensionReduction.getParameters().get(2).getValueClass();
		assertEquals((Double) 0.0, p3.getMinimumF().function(10.0, 10.0));
		assertEquals((Double) 1.0, p3.getMaximumF().function(10.0, 10.0));
		assertEquals(null, p3.getDefaultValueF());

		BooleanType p4 = (BooleanType) dimensionReduction.getParameters().get(3).getValueClass();
		assertEquals((Integer) 0, p4.isEnabled().function(10, 10));
		assertEquals((Integer) 1, p4.isEnabled().function(100000, 10));

		BooleanType p5 = (BooleanType) dimensionReduction.getParameters().get(4).getValueClass();
		assertEquals((Integer) 1, p5.isEnabled().function(10, 10));

		IntegerType p6 = (IntegerType) dimensionReduction.getParameters().get(5).getValueClass();
		assertEquals((Integer) 2, p6.getMinimumF().function(10, 10));
		assertEquals((Integer) 10, p6.getMaximumF().function(10, 10));
		assertEquals((Integer) 5, p6.getDefaultValueF().function(10, 10));
	}

	@Test
	public void testCreateInstanceAllNull() {
		Map<String, Object> parameters = new HashMap<>();
		assertNotNull(dimensionReduction.createInstance(parameters));
	}

	@Test
	public void testCreateInstance() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(dimensionReduction.getParameters().get(0).getValueName(), 1);
		parameters.put(dimensionReduction.getParameters().get(1).getValueName(), 1.0);
		parameters.put(dimensionReduction.getParameters().get(2).getValueName(), 1.0);
		parameters.put(dimensionReduction.getParameters().get(3).getValueName(), false);
		parameters.put(dimensionReduction.getParameters().get(4).getValueName(), false);
		parameters.put(dimensionReduction.getParameters().get(5).getValueName(), 1);
		assertNotNull(dimensionReduction.createInstance(parameters));
	}

	@Test
	public void testCreateInstance1null() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(dimensionReduction.getParameters().get(1).getValueName(), 1.0);
		parameters.put(dimensionReduction.getParameters().get(2).getValueName(), 1.0);
		parameters.put(dimensionReduction.getParameters().get(3).getValueName(), false);
		parameters.put(dimensionReduction.getParameters().get(4).getValueName(), false);
		parameters.put(dimensionReduction.getParameters().get(5).getValueName(), 1);
		assertNotNull(dimensionReduction.createInstance(parameters));
	}

	@Test
	public void testCreateInstance2null() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(dimensionReduction.getParameters().get(0).getValueName(), 1);
		parameters.put(dimensionReduction.getParameters().get(2).getValueName(), 1.0);
		parameters.put(dimensionReduction.getParameters().get(3).getValueName(), false);
		parameters.put(dimensionReduction.getParameters().get(4).getValueName(), false);
		parameters.put(dimensionReduction.getParameters().get(5).getValueName(), 1);
		assertNotNull(dimensionReduction.createInstance(parameters));
	}

	@Test
	public void testCreateInstance3null() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(dimensionReduction.getParameters().get(0).getValueName(), 1);
		parameters.put(dimensionReduction.getParameters().get(1).getValueName(), 1.0);
		parameters.put(dimensionReduction.getParameters().get(3).getValueName(), false);
		parameters.put(dimensionReduction.getParameters().get(4).getValueName(), false);
		parameters.put(dimensionReduction.getParameters().get(5).getValueName(), 1);
		assertNotNull(dimensionReduction.createInstance(parameters));
	}

	@Test
	public void testCreateInstance4null() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(dimensionReduction.getParameters().get(0).getValueName(), 1);
		parameters.put(dimensionReduction.getParameters().get(1).getValueName(), 1.0);
		parameters.put(dimensionReduction.getParameters().get(2).getValueName(), 1.0);
		parameters.put(dimensionReduction.getParameters().get(4).getValueName(), false);
		parameters.put(dimensionReduction.getParameters().get(5).getValueName(), 1);
		assertNotNull(dimensionReduction.createInstance(parameters));
	}

	@Test
	public void testCreateInstance5null() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(dimensionReduction.getParameters().get(0).getValueName(), 1);
		parameters.put(dimensionReduction.getParameters().get(1).getValueName(), 1.0);
		parameters.put(dimensionReduction.getParameters().get(2).getValueName(), 1.0);
		parameters.put(dimensionReduction.getParameters().get(3).getValueName(), false);
		parameters.put(dimensionReduction.getParameters().get(5).getValueName(), 1);
		assertNotNull(dimensionReduction.createInstance(parameters));
	}

	@Test
	public void testCreateInstance6null() {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(dimensionReduction.getParameters().get(0).getValueName(), 1);
		parameters.put(dimensionReduction.getParameters().get(1).getValueName(), 1.0);
		parameters.put(dimensionReduction.getParameters().get(2).getValueName(), 1.0);
		parameters.put(dimensionReduction.getParameters().get(3).getValueName(), false);
		parameters.put(dimensionReduction.getParameters().get(4).getValueName(), false);
		assertNotNull(dimensionReduction.createInstance(parameters));
	}

	@Test
	public void testGetMinimumMemmory() {
		assertEquals((Long) 0l, dimensionReduction.getMinimumMemmory(10, 10));
	}
}
