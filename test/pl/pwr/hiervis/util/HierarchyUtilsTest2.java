package pl.pwr.hiervis.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class HierarchyUtilsTest2 {

	Hierarchy h;
	LoadedHierarchy l;

	public HierarchyUtilsTest2() {
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
		assertEquals("gen.0", LoadedHierarchyUtils.findGroup(l, 0).getId());
		assertEquals("gen.0.0", LoadedHierarchyUtils.findGroup(l, 1).getId());
		assertEquals("gen.0.1", LoadedHierarchyUtils.findGroup(l, 2).getId());
		assertEquals("gen.0.0.0", LoadedHierarchyUtils.findGroup(l, 3).getId());
		assertEquals(null, LoadedHierarchyUtils.findGroup(l, 4));
	}

	@Test
	public void testFindGroupLoadedHierarchyString() {
		assertEquals("gen.0", LoadedHierarchyUtils.findGroup(l, "gen.0").getId());
		assertEquals("gen.0.0", LoadedHierarchyUtils.findGroup(l, "gen.0.0").getId());
		assertEquals("gen.0.1", LoadedHierarchyUtils.findGroup(l, "gen.0.1").getId());
		assertEquals("gen.0.0.0", LoadedHierarchyUtils.findGroup(l, "gen.0.0.0").getId());
		assertEquals(null, LoadedHierarchyUtils.findGroup(l, "gen.0.2"));
	}

}
