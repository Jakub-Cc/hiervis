package pl.pwr.hiervis.dimension_reduction;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimension_reduction.methods.feature_extraction.StarCoordinates;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class CalculatedDimensionReductionTest {

	LoadedHierarchy loadedHierarchy;
	FeatureExtraction dimensionReduction;
	Hierarchy hierarchy;

	@Before
	public void initialize() {
		hierarchy = TestCommon.getFourGroupsHierarchy();
		loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
		dimensionReduction = new StarCoordinates();
	}

	@Test
	public void testCalculatedDimensionReduction() {
		CalculatedDimensionReduction calculatedDimensionReduction = new CalculatedDimensionReduction(loadedHierarchy,
				dimensionReduction, hierarchy, null);
		assertNotSame(calculatedDimensionReduction, null);
		assertSame(calculatedDimensionReduction.getDimensionReduction(), dimensionReduction);
		assertSame(calculatedDimensionReduction.getInputLoadedHierarchy(), loadedHierarchy);
		assertSame(calculatedDimensionReduction.getOutputHierarchy(), hierarchy);

	}

}
