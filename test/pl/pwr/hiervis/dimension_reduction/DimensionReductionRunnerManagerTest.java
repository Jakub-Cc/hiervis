package pl.pwr.hiervis.dimension_reduction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimension_reduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimension_reduction.methods.feature_extraction.StarCoordinates;
import pl.pwr.hiervis.dimension_reduction.methods.feature_selection.InfiniteFS;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.Event;

public class DimensionReductionRunnerManagerTest {

	Hierarchy hierarchy;
	LoadedHierarchy loadedHierarchy;
	DimensionReductionI dimensionReduction;
	Event<CalculatedDimensionReduction> brodcastEvent;
	DimensionReductionRunner reductionRunner;
	DimensionReductionRunnerManager reductionRunnerManager;

	@Before
	public void init() {
		hierarchy = TestCommon.getFourGroupsHierarchy();
		loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
		dimensionReduction = new StarCoordinates();
		brodcastEvent = new Event<>();
		reductionRunner = new DimensionReductionRunner(loadedHierarchy, dimensionReduction, brodcastEvent);

		reductionRunnerManager = new DimensionReductionRunnerManager(HVContext.getContext());
	}

	@Test
	public void testDimensionReductionRunnerManager() {
		assertNotNull(reductionRunnerManager);
	}

	@Test
	public void testAddTask() {
		reductionRunnerManager.addTask(loadedHierarchy, dimensionReduction);
		assertFalse(reductionRunnerManager.removeTask(loadedHierarchy, InfiniteFS.class));
		assertTrue(reductionRunnerManager.removeTask(loadedHierarchy, dimensionReduction.getClass()));
		assertFalse(reductionRunnerManager.removeTask(loadedHierarchy, dimensionReduction.getClass()));
	}

	@Test
	public void testOnDimensionReductionCalculated() {
		reductionRunnerManager.addTask(loadedHierarchy, dimensionReduction);
		CalculatedDimensionReduction calculatedDimensionReduction = new CalculatedDimensionReduction(loadedHierarchy,
				dimensionReduction, hierarchy, null);
		reductionRunnerManager.onDimensionReductionCalculated(calculatedDimensionReduction);
		assertFalse(reductionRunnerManager.removeTask(loadedHierarchy, dimensionReduction.getClass()));
	}

	@Test
	public void testInteruptTask() {
		reductionRunnerManager.addTask(loadedHierarchy, dimensionReduction);
		assertFalse(reductionRunnerManager.interuptTask(loadedHierarchy, InfiniteFS.class));
		assertTrue(reductionRunnerManager.interuptTask(loadedHierarchy, dimensionReduction.getClass()));
		assertFalse(reductionRunnerManager.interuptTask(loadedHierarchy, dimensionReduction.getClass()));
	}

}
