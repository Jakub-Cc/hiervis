package pl.pwr.hiervis.dimension_reduction.methods.feature_extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimension_reduction.TestCommon;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class StarCoordinatesTest {

	LoadedHierarchy loadedHierarchy;
	FeatureExtraction dimensionReduction;
	Hierarchy hierarchy;

	@Before
	public void initialize() {
		dimensionReduction = new StarCoordinates();
		hierarchy = TestCommon.getTwoTwoGroupsHierarchy();
		loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
	}

	@Test
	public void testReduceHierarchy() {
		assertEquals(4, loadedHierarchy.getMainHierarchy().getRoot().getNodeInstances().getFirst().getData().length);
		Hierarchy testHierarchy = dimensionReduction.reduceHierarchy(loadedHierarchy.getMainHierarchy());
		assertEquals(2, testHierarchy.getRoot().getNodeInstances().getFirst().getData().length);
	}

	@Test
	public void testGetName() {
		assertEquals(StarCoordinates.STAR_COORDINATES, dimensionReduction.getName());
	}

	@Test
	public void testGetSimpleName() {
		assertEquals(StarCoordinates.STAR_COORDS, dimensionReduction.getSimpleName());
	}

	@Test
	public void testGetDescription() {
		assertEquals(StarCoordinates.DESCRIPTION, dimensionReduction.getDescription());
	}

	@Test
	public void testSGetName() {
		assertEquals(StarCoordinates.STAR_COORDINATES, StarCoordinates.sGetName());
	}

	@Test
	public void testSGetSimpleName() {
		assertEquals(StarCoordinates.STAR_COORDS, StarCoordinates.sGetSimpleName());
	}

	@Test
	public void testSGetDescription() {
		assertEquals(StarCoordinates.DESCRIPTION, StarCoordinates.sGetDescription());
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
	public void testGetMinimumMemmory() {
		assertEquals((Long) 0l, dimensionReduction.getMinimumMemmory(10, 10));
	}
}
