package pl.pwr.hiervis.dimension_reduction.methods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimension_reduction.TestCommon;
import pl.pwr.hiervis.dimension_reduction.distance_measures.Euclidean;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimension_reduction.methods.feature_extraction.MultidimensionalScaling;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class MultidimensionalScalingTest {

	LoadedHierarchy loadedHierarchy;
	FeatureExtraction dimensionReduction;
	Hierarchy hierarchy;

	@Before
	public void initialize() {
		dimensionReduction = new MultidimensionalScaling();
		hierarchy = TestCommon.getTwoTwoGroupsHierarchy();
		loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
	}

	@Test
	public void testMultidimensionalScaling() {
		assertNotEquals(null, dimensionReduction);
	}

	@Test
	public void testMultidimensionalScalingDistanceMeasure() {
		assertNotEquals(null, dimensionReduction);
		dimensionReduction = null;
		assertEquals(null, dimensionReduction);
		dimensionReduction = new MultidimensionalScaling(new Euclidean());
		assertNotEquals(null, dimensionReduction);
	}

	@Test
	public void testReduceHierarchy() {
		assertEquals(4, loadedHierarchy.getMainHierarchy().getRoot().getNodeInstances().getFirst().getData().length);
		Hierarchy reducedHierarchy = dimensionReduction.reduceHierarchy(loadedHierarchy.getMainHierarchy());
		assertEquals(2, reducedHierarchy.getRoot().getNodeInstances().getFirst().getData().length);
	}

	@Test
	public void testGetName() {
		assertEquals(MultidimensionalScaling.MULTIDIMENSIONAL_SCALING, dimensionReduction.getName());
	}

	@Test
	public void testGetSimpleName() {
		assertEquals(MultidimensionalScaling.MDS, dimensionReduction.getSimpleName());
	}

	@Test
	public void testGetDescription() {
		assertEquals(MultidimensionalScaling.DESCRIPTION, dimensionReduction.getDescription());
	}

	@Test
	public void testSGetName() {
		assertEquals(MultidimensionalScaling.MULTIDIMENSIONAL_SCALING, MultidimensionalScaling.sGetName());
	}

	@Test
	public void testSGetSimpleName() {
		assertEquals(MultidimensionalScaling.MDS, MultidimensionalScaling.sGetSimpleName());
	}

	@Test
	public void testSGetDescription() {
		assertEquals(" ", MultidimensionalScaling.sGetDescription());
	}
}
