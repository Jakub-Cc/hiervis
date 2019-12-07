package pl.pwr.hiervis.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static pl.pwr.hiervis.TestConst.NOT_YET_IMPLEMENTED;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class LoadedHierarchyUtilsTest {

	private static final String GEN_0_0_0 = "gen.0.0.0";
	private static final String GEN_0_1 = "gen.0.1";
	private static final String GEN_0_0 = "gen.0.0";
	private static final String GEN_0 = "gen.0";
	Hierarchy h;
	LoadedHierarchy l;

	@Before
	public void init() {
		h = TestCommon.getFourGroupsHierarchy();
		l = new LoadedHierarchy(h, new LoadedHierarchy.Options(false, false, false, false, false));
	}

	@Test
	public void testFlattenHierarchy() {
		assertEquals(4, l.getMainHierarchy().getNumberOfGroups());
		LoadedHierarchy newL = LoadedHierarchyUtils.flattenHierarchy(l);
		assertEquals(1, newL.getMainHierarchy().getNumberOfGroups());
	}

	@Test
	public void testFindGroupLoadedHierarchyInt() {
		assertEquals(GEN_0, LoadedHierarchyUtils.findGroup(l, 0).getId());
		assertEquals(GEN_0_0, LoadedHierarchyUtils.findGroup(l, 1).getId());
		assertEquals(GEN_0_1, LoadedHierarchyUtils.findGroup(l, 2).getId());
		assertEquals(GEN_0_0_0, LoadedHierarchyUtils.findGroup(l, 3).getId());
		assertEquals(null, LoadedHierarchyUtils.findGroup(l, 4));
	}

	@Test
	public void testFindGroupLoadedHierarchyString() {
		assertEquals(GEN_0, LoadedHierarchyUtils.findGroup(l, GEN_0).getId());
		assertEquals(GEN_0_0, LoadedHierarchyUtils.findGroup(l, GEN_0_0).getId());
		assertEquals(GEN_0_1, LoadedHierarchyUtils.findGroup(l, GEN_0_1).getId());
		assertEquals(GEN_0_0_0, LoadedHierarchyUtils.findGroup(l, GEN_0_0_0).getId());
		assertEquals(null, LoadedHierarchyUtils.findGroup(l, "gen.0.2"));
	}

	@Test
	public void testMerge() {
		fail(NOT_YET_IMPLEMENTED);
	}

	@Test
	public void testRemoveDimensions() {
		fail(NOT_YET_IMPLEMENTED);
	}

	@Test
	public void testFlattenHierarchyKeepReductons() {
		fail(NOT_YET_IMPLEMENTED);
	}

}
