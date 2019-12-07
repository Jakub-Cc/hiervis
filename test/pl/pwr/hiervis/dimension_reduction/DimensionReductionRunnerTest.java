package pl.pwr.hiervis.dimension_reduction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.dimension_reduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimension_reduction.methods.feature_extraction.StarCoordinates;
import pl.pwr.hiervis.dimension_reduction.methods.feature_selection.InfiniteFS;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.Event;

public class DimensionReductionRunnerTest {

	Hierarchy hierarchy;
	LoadedHierarchy loadedHierarchy;
	DimensionReductionI dimensionReduction;
	Event<CalculatedDimensionReduction> brodcastEvent;
	DimensionReductionRunner reductionRunner;

	@Before
	public void init() {
		hierarchy = TestCommon.getFourGroupsHierarchy();
		loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
		dimensionReduction = new StarCoordinates();
		brodcastEvent = new Event<>();
		reductionRunner = new DimensionReductionRunner(loadedHierarchy, dimensionReduction, brodcastEvent);
	}

	@Test
	public void testRun() {
		brodcastEvent.addListener(e -> assertTrue(true));
		reductionRunner.start();
	}

	@Test
	public void testDimensionReductionRunner() {
		assertNotNull(reductionRunner);
		LoadedHierarchy loadedHierarchy2 = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
		assertFalse(reductionRunner.isTheSame(loadedHierarchy2, StarCoordinates.class));
		assertFalse(reductionRunner.isTheSame(loadedHierarchy, InfiniteFS.class));
		assertTrue(reductionRunner.isTheSame(loadedHierarchy, StarCoordinates.class));
	}

}
