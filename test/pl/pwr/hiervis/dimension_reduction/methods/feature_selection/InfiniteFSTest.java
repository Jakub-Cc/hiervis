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

public class InfiniteFSTest {

	LoadedHierarchy loadedHierarchy;
	FeatureSelection dimensionReduction;
	Hierarchy hierarchy;

	@Before
	public void initialize() {
		dimensionReduction = new InfiniteFS();
		hierarchy = TestCommon.getTwoTwoGroupsHierarchy();
		loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
	}

	@Test
	public void testGetName() {
		assertEquals(InfiniteFS.NAME, dimensionReduction.getName());
	}

	@Test
	public void testGetSimpleName() {
		assertEquals(InfiniteFS.SIMPLE_NAME, dimensionReduction.getSimpleName());
	}

	@Test
	public void testGetDescription() {
		assertEquals(InfiniteFS.DESCRIPTION, dimensionReduction.getDescription());
	}

	@Test
	public void testGetParameters() {
		assertNotNull(dimensionReduction.getParameters());
		assertTrue(!dimensionReduction.getParameters().isEmpty());
	}

	@Test
	public void testCreateInstance() {
		Map<String, Object> parameters = new HashMap<>();
		assertNotNull(dimensionReduction.createInstance(parameters));
	}

	@Test
	public void testGetMinimumMemmory() {
		assertEquals((Long) 0l, dimensionReduction.getMinimumMemmory(10, 10));
	}

	@Test(expected = NullPointerException.class)
	public void testSelectFeaturesThrowsNullExcption() {
		dimensionReduction.selectFeatures(hierarchy);
	}

	@Test
	public void testSelectFeatures() {
		dimensionReduction = new InfiniteFS(0.5);
		assertTrue(!dimensionReduction.selectFeatures(hierarchy).isEmpty());
	}

}
