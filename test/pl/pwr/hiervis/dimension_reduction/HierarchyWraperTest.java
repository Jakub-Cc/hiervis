package pl.pwr.hiervis.dimension_reduction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelection;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.dimension_reduction.methods.feature_extraction.MultidimensionalScaling;
import pl.pwr.hiervis.dimension_reduction.methods.feature_extraction.StarCoordinates;
import pl.pwr.hiervis.dimension_reduction.methods.feature_extraction.Tsne;
import pl.pwr.hiervis.dimension_reduction.methods.feature_selection.InfiniteFS;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class HierarchyWraperTest {

	Hierarchy hierarchy;
	HierarchyWraper hierachyWraper;

	@Before
	public void initialize() {
		hierarchy = TestCommon.getFourGroupsHierarchy();
		hierachyWraper = new HierarchyWraper(hierarchy);
	}

	@Test
	public void testHierarchyWraperHierarchy() {
		assertNotSame(hierachyWraper, null);
		assertEquals(hierachyWraper.getHierarchy(), hierarchy);
	}

	@Test
	public void testHierarchyWraper() {
		hierachyWraper = null;
		assertEquals(hierachyWraper, null);
		hierachyWraper = new HierarchyWraper();
		assertNotSame(hierachyWraper, null);
		assertEquals(hierachyWraper.getHierarchy(), null);
	}

	@Test
	public void testGetHierarchyWithoutChange() {
		assertEquals(hierarchy, hierachyWraper.getHierarchyWithoutChange(0));
		assertEquals(null, hierachyWraper.getHierarchyWithoutChange(1));
		assertEquals(null, hierachyWraper.getHierarchyWithoutChange(10));
	}

	@Test
	public void testSetHierarchy() {
		assertEquals(hierarchy, hierachyWraper.getHierarchy());
		hierachyWraper.setHierarchy(null);
		assertEquals(null, hierachyWraper.getHierarchy());
		hierachyWraper.getReducedHierarchy()[0] = hierarchy;
		hierachyWraper.setHierarchy(1);
		assertEquals(hierarchy, hierachyWraper.getHierarchy());

		hierachyWraper.setHierarchy(null);
		assertEquals(null, hierachyWraper.getHierarchy());
		hierachyWraper.setHierarchy(0);
		assertEquals(hierarchy, hierachyWraper.getHierarchy());
		hierachyWraper.setHierarchy(-1);
		assertEquals(hierarchy, hierachyWraper.getHierarchy());

		hierachyWraper.setHierarchy(10);
		assertEquals(hierarchy, hierachyWraper.getHierarchy());
	}

	@Test
	public void testGetOriginalHierarchy() {
		assertEquals(hierarchy, hierachyWraper.getOriginalHierarchy());
	}

	@Test
	public void testGetReducedHierarchy() {
		assertNotSame(null, hierachyWraper.getReducedHierarchy());
	}

	@Test
	public void testSetReducedHierarchy() {
		hierachyWraper.setReducedHierarchy(null);
		assertSame(null, hierachyWraper.getReducedHierarchy());

	}

	@Test
	public void testGetReducedHierarchyDimensionReduction() {
		hierachyWraper.getReducedHierarchy()[0] = hierarchy;
		assertEquals(hierarchy, hierachyWraper.getReducedHierarchy(new MultidimensionalScaling()));
		assertEquals(null, hierachyWraper.getReducedHierarchy(null));
	}

	@Test
	public void testGetReducedHierarchyInt() {
		Hierarchy hier = hierachyWraper.getReducedHierarchy(1);
		assertEquals(null, hier);
		assertEquals(null, hierachyWraper.getReducedHierarchy(-11));
		assertEquals(null, hierachyWraper.getReducedHierarchy(10));
	}

	@Test
	public void testAddReducedHierarchy() {
		LoadedHierarchy loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
		FeatureExtraction dimensionReduction = new StarCoordinates();
		CalculatedDimensionReduction calculatedDimensionReduction = new CalculatedDimensionReduction(loadedHierarchy,
				dimensionReduction, hierarchy, null);
		hierachyWraper.addReducedHierarchy(calculatedDimensionReduction);
		assertNotSame(null, hierachyWraper.getHierarchyWithoutChange(0));
	}

	@Test
	public void testSetHierarchyInt() {
		hierachyWraper.setHierarchy(0);
		assertEquals(hierarchy, hierachyWraper.getHierarchy());
		hierachyWraper.setHierarchy(1);
		assertEquals(null, hierachyWraper.getHierarchy());
	}

	@Test
	public void testGetReducedHierarchyFeatureExtraction() {
		FeatureExtraction f = new Tsne();
		assertEquals(null, hierachyWraper.getReducedHierarchy(f));
	}

	@Test
	public void testAddFeatureSelectionResult() {
		LoadedHierarchy loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
		FeatureSelection infFs = new InfiniteFS(0.5);
		List<FeatureSelectionResult> list = infFs.selectFeatures(hierarchy);
		CalculatedDimensionReduction calculatedDimensionReduction = new CalculatedDimensionReduction(loadedHierarchy,
				infFs, hierarchy, list);

		hierachyWraper.addFeatureSelectionResult(calculatedDimensionReduction);
		assertEquals(list, hierachyWraper.getFSResult(5));
		assertEquals(null, hierachyWraper.getFSResult(-5));
		assertEquals(null, hierachyWraper.getFSResult(500));
	}

	@Test
	public void testGetHierarchy() {
		assertEquals(hierarchy, hierachyWraper.getHierarchy());
	}

	@Test
	public void testSetHierarchyHierarchy() {
		assertEquals(hierarchy, hierachyWraper.getHierarchy());
		hierachyWraper.setHierarchy(null);
		assertEquals(null, hierachyWraper.getHierarchy());
	}

	@Test
	public void testGetVisibleDimensionsKeeper() {
		assertNotNull(hierachyWraper.getVisibleDimensionsKeeper());
		assertEquals(8, hierachyWraper.getVisibleDimensionsKeeper().length);
	}

	@Test
	public void testGetVisibleDimensions() {
		assertNotNull(hierachyWraper.getVisibleDimensions());
	}
}
