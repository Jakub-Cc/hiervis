package pl.pwr.hiervis.dimension_reduction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimension_reduction.methods.feature_extraction.MultidimensionalScaling;
import pl.pwr.hiervis.dimension_reduction.methods.feature_extraction.StarCoordinates;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class DimensionReductionManagerTest {

	DimensionReductionManager dimenisonReductionManager;
	LoadedHierarchy loadedHierarchy;
	FeatureExtraction dimensionReduction;
	Hierarchy hierarchy;

	@Before
	public void initialize() {
		dimenisonReductionManager = new DimensionReductionManager();
		hierarchy = TestCommon.getFourGroupsHierarchy();
		loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
		dimensionReduction = new StarCoordinates();
	}

	@Test
	public void testDimensionReductionManager() {
		assertNotEquals(null, dimenisonReductionManager);
	}

	@Test
	public void testGetIndex() {
		assertEquals(0, dimenisonReductionManager.getIndex(new MultidimensionalScaling()));
		assertEquals(1, dimenisonReductionManager.getIndex(new StarCoordinates()));
		assertEquals(-1, dimenisonReductionManager.getIndex(null));

	}

	@Test
	public void testGetSize() {
		assertEquals(7, dimenisonReductionManager.getSize());
	}

	@Test
	public void testGetNames() {
		assertEquals(7, dimenisonReductionManager.getNames().length);
	}

	@Test
	public void testGetSimpleNames() {
		assertEquals(7, dimenisonReductionManager.getSimpleNames().length);
	}

	@Test
	public void testGetResaultClass() {
		assertEquals(null, dimenisonReductionManager.getResaultClass(10));
		assertEquals(null, dimenisonReductionManager.getResaultClass(-1));
		assertEquals(StarCoordinates.class, dimenisonReductionManager.getResaultClass(1));
	}

	@Test
	public void testAddToQueue() {
		assertEquals(false, dimenisonReductionManager.isInQueue(loadedHierarchy, dimensionReduction.getClass()));
		dimenisonReductionManager.addToQueue(loadedHierarchy, dimensionReduction.getClass());
		assertEquals(true, dimenisonReductionManager.isInQueue(loadedHierarchy, dimensionReduction.getClass()));

	}

	@Test
	public void testIsInQueueLoadedHierarchyInt() {
		assertEquals(false, dimenisonReductionManager.isInQueue(loadedHierarchy, dimensionReduction.getClass()));
		dimenisonReductionManager.addToQueue(loadedHierarchy, dimensionReduction.getClass());
		assertEquals(true, dimenisonReductionManager.isInQueue(loadedHierarchy, 1));

		assertEquals(false, dimenisonReductionManager.isInQueue(null, dimensionReduction.getClass()));
		assertEquals(false, dimenisonReductionManager.isInQueue(loadedHierarchy, MultidimensionalScaling.class));
		assertEquals(false, dimenisonReductionManager.isInQueue(loadedHierarchy, null));
		assertEquals(false, dimenisonReductionManager.isInQueue(null, null));
		assertEquals(false, dimenisonReductionManager.isInQueue(loadedHierarchy, 0));
	}

	@Test
	public void testRemoveFromQueue() {
		assertEquals(false, dimenisonReductionManager.isInQueue(loadedHierarchy, dimensionReduction.getClass()));
		dimenisonReductionManager.addToQueue(loadedHierarchy, dimensionReduction.getClass());
		assertEquals(true, dimenisonReductionManager.isInQueue(loadedHierarchy, dimensionReduction.getClass()));
		dimenisonReductionManager.removeFromQueue(loadedHierarchy, dimensionReduction.getClass());
		assertEquals(false, dimenisonReductionManager.isInQueue(loadedHierarchy, dimensionReduction.getClass()));

		assertEquals(false, dimenisonReductionManager.removeFromQueue(loadedHierarchy, dimensionReduction.getClass()));

	}

}
