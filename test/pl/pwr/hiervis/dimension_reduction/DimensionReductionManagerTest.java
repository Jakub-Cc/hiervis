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
		assertEquals(3, dimenisonReductionManager.getIndex(new StarCoordinates()));
		assertEquals(-1, dimenisonReductionManager.getIndex(null));

	}

	@Test
	public void testGetSize() {
		assertEquals(4, dimenisonReductionManager.getSize());
	}

	@Test
	public void testGetNames() {
		assertEquals(4, dimenisonReductionManager.getNames().length);
	}

	@Test
	public void testGetSimpleNames() {
		assertEquals(4, dimenisonReductionManager.getSimpleNames().length);
	}

	@Test
	public void testGetResaultClass() {
		assertEquals(null, dimenisonReductionManager.getResaultClass(5));
		assertEquals(null, dimenisonReductionManager.getResaultClass(-1));
		assertEquals(StarCoordinates.class, dimenisonReductionManager.getResaultClass(3));
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
		assertEquals(true, dimenisonReductionManager.isInQueue(loadedHierarchy, 3));

		assertEquals(false, dimenisonReductionManager.isInQueue(null, dimensionReduction.getClass()));
		assertEquals(false, dimenisonReductionManager.isInQueue(loadedHierarchy, MultidimensionalScaling.class));
		assertEquals(false, dimenisonReductionManager.isInQueue(loadedHierarchy, null));
		assertEquals(false, dimenisonReductionManager.isInQueue(null, null));

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
