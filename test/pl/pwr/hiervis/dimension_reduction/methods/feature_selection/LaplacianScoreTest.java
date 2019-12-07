package pl.pwr.hiervis.dimension_reduction.methods.feature_selection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimension_reduction.TestCommon;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelection;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class LaplacianScoreTest {

	LoadedHierarchy loadedHierarchy;
	FeatureSelection dimensionReduction;
	Hierarchy hierarchy;

	@Before
	public void initialize() {
		dimensionReduction = new LaplacianScore();
		hierarchy = TestCommon.getTwoTwoGroupsHierarchy();
		loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
	}

	@Test
	public void testGetName() {
		assertEquals(LaplacianScore.NAME, dimensionReduction.getName());
	}

	@Test
	public void testGetSimpleName() {
		assertEquals(LaplacianScore.SIMPLE_NAME, dimensionReduction.getSimpleName());
	}

	@Test
	public void testGetDescription() {
		assertEquals(LaplacianScore.DESCRIPTION, dimensionReduction.getDescription());
	}

	@Test
	public void testGetMinimumMemmory() {
		assertEquals((Long) 0l, dimensionReduction.getMinimumMemmory(10, 10));
	}

	@Test
	public void testGetParameters() {
		assertNotNull(dimensionReduction.getParameters());
		assertTrue(dimensionReduction.getParameters().isEmpty());
	}

	@Test
	public void testCreateInstance() {
		Map<String, Object> parameters = new HashMap<>();
		assertNotNull(dimensionReduction.createInstance(parameters));
	}

	@Test
	public void testSelectFeatures() {
		assertTrue(!dimensionReduction.selectFeatures(hierarchy).isEmpty());
	}

}
