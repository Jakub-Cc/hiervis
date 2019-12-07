package pl.pwr.hiervis.dimension_reduction.methods.feature_extraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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

public class PrincipalComponentAnalysisTest {
	LoadedHierarchy loadedHierarchy;
	FeatureExtraction dimensionReduction;
	Hierarchy hierarchy;

	@Before
	public void initialize() {
		dimensionReduction = new PrincipalComponentAnalysis();
		hierarchy = TestCommon.getTwoTwoGroupsHierarchy();
		loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
	}

	@Test
	public void testPrincipalComponentAnalysisInt() {
		dimensionReduction = new PrincipalComponentAnalysis(3);
		assertEquals(4, loadedHierarchy.getMainHierarchy().getRoot().getNodeInstances().getFirst().getData().length);
		Hierarchy testHierarchy = dimensionReduction.reduceHierarchy(loadedHierarchy.getMainHierarchy());
		assertEquals(3, testHierarchy.getRoot().getNodeInstances().getFirst().getData().length);
	}

	@Test
	public void testPrincipalComponentAnalysis() {
		assertNotEquals(null, dimensionReduction);
	}

	@Test
	public void testReduceHierarchy() {
		assertEquals(4, loadedHierarchy.getMainHierarchy().getRoot().getNodeInstances().getFirst().getData().length);
		Hierarchy testHierarchy = dimensionReduction.reduceHierarchy(loadedHierarchy.getMainHierarchy());
		assertEquals(2, testHierarchy.getRoot().getNodeInstances().getFirst().getData().length);
	}

	@Test
	public void testGetName() {
		assertEquals(PrincipalComponentAnalysis.PRINCIPAL_COMPONENT_ANALYSIS, dimensionReduction.getName());
	}

	@Test
	public void testGetSimpleName() {
		assertEquals(PrincipalComponentAnalysis.PCA, dimensionReduction.getSimpleName());
	}

	@Test
	public void testGetDescription() {
		assertEquals(PrincipalComponentAnalysis.DESCRIPTION, dimensionReduction.getDescription());
	}

	@Test
	public void testSGetName() {
		assertEquals(PrincipalComponentAnalysis.PRINCIPAL_COMPONENT_ANALYSIS, PrincipalComponentAnalysis.sGetName());
	}

	@Test
	public void testSGetSimpleName() {
		assertEquals(PrincipalComponentAnalysis.PCA, PrincipalComponentAnalysis.sGetSimpleName());
	}

	@Test
	public void testSGetDescription() {
		assertEquals(PrincipalComponentAnalysis.DESCRIPTION, PrincipalComponentAnalysis.sGetDescription());
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
}
