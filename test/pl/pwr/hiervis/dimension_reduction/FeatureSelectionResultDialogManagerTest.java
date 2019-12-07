package pl.pwr.hiervis.dimension_reduction;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class FeatureSelectionResultDialogManagerTest {

	Hierarchy hierarchy;
	LoadedHierarchy loadedHierarchy;

	@Test
	public void testFeatureSelectionResultDialogManager() {
		FeatureSelectionResultDialogManager dialogManager = new FeatureSelectionResultDialogManager();
		assertNotNull(dialogManager);

		hierarchy = TestCommon.getFourGroupsHierarchy();
		loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));

		HVContext.getContext().hierarchyClosing.broadcast(loadedHierarchy);
	}

}
