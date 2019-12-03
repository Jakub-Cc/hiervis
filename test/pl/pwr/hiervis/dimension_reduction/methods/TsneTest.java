package pl.pwr.hiervis.dimension_reduction.methods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimension_reduction.TestCommon;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimension_reduction.methods.feature_extraction.Tsne;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class TsneTest {
	LoadedHierarchy loadedHierarchy;
	FeatureExtraction dimensionReduction;
	Hierarchy hierarchy;

	@Before
	public void initialize() {
		dimensionReduction = new Tsne(100, 1, 0.5, false, false, 3, true, false, 2);
		hierarchy = TestCommon.getTwoTwoGroupsHierarchy();
		loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
	}

	@Test
	public void testTsneParallel() {
		dimensionReduction = new Tsne(100, 1, 0.5, true, false, 3, true, false, 2);
		assertNotEquals(null, dimensionReduction);
		assertEquals(4, loadedHierarchy.getMainHierarchy().getRoot().getNodeInstances().getFirst().getData().length);
		Hierarchy testHierarchy = dimensionReduction.reduceHierarchy(loadedHierarchy.getMainHierarchy());
		assertEquals(2, testHierarchy.getRoot().getNodeInstances().getFirst().getData().length);
	}

	@Test
	public void testTsne() {
		dimensionReduction = null;
		assertEquals(null, dimensionReduction);
		dimensionReduction = new Tsne();
		assertNotEquals(null, dimensionReduction);
	}

	@Test
	public void testTsneBooleanIntIntIntDoubleBooleanDoubleBooleanBoolean() {
		assertNotEquals(null, dimensionReduction);
	}

	@Test
	public void testReduceHierarchy() {
		assertEquals(4, loadedHierarchy.getMainHierarchy().getRoot().getNodeInstances().getFirst().getData().length);
		Hierarchy tstHierarchy = dimensionReduction.reduceHierarchy(loadedHierarchy.getMainHierarchy());
		assertEquals(2, tstHierarchy.getRoot().getNodeInstances().getFirst().getData().length);
	}

	@Test
	public void testGetName() {
		assertEquals(Tsne.T_SNE_FULL_NAME, dimensionReduction.getName());
	}

	@Test
	public void testGetSimpleName() {
		assertEquals(Tsne.T_SNE, dimensionReduction.getSimpleName());
	}

	@Test
	public void testGetDescription() {
		assertEquals(Tsne.DSCRIPTION, dimensionReduction.getDescription());
	}

	@Test
	public void testSGetName() {
		assertEquals(Tsne.T_SNE_FULL_NAME, Tsne.sGetName());
	}

	@Test
	public void testSGetSimpleName() {
		assertEquals(Tsne.T_SNE, Tsne.sGetSimpleName());
	}

	@Test
	public void testSGetDescription() {
		assertEquals(Tsne.DSCRIPTION, Tsne.sGetDescription());
	}
}
