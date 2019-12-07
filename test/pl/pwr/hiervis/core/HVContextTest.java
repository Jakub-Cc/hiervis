package pl.pwr.hiervis.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class HVContextTest {

	HVContext context;
	Hierarchy hierarchy;
	LoadedHierarchy loadedHierarchy;

	@Before
	public void init() {
		context = HVContext.getContext();
		hierarchy = TestCommon.getFourGroupsHierarchy();
		loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
	}

	@Test
	public void testGetContext() {
		assertNotNull(context);
	}

	@Test
	public void testGetConfig() {
		HVConfig currentConfig = context.getConfig();
		assertNotNull(currentConfig);
		context.setConfig(null);
		assertNotNull(context.getConfig());
		context.setConfig(new HVConfig());
		assertEquals(currentConfig, context.getConfig());
		HVConfig nwConfig = new HVConfig();
		nwConfig.setPointSize(100);
		context.setConfig(nwConfig);
		assertNotEquals(currentConfig, context.getConfig());
	}

	@Test
	public void testGetMeasureManager() {
		assertNotNull(context.getMeasureManager());
	}

	@Test
	public void testSetHierarchy() {
		context.setHierarchy(loadedHierarchy);
		assertEquals(loadedHierarchy, context.getHierarchy());
	}

	@Test
	public void testGetHierarchyIndex() {
		assertEquals(-1, context.getHierarchyIndex(loadedHierarchy));
		context.setHierarchy(loadedHierarchy);
		assertEquals(-1, context.getHierarchyIndex(loadedHierarchy));
	}

	@Test
	public void testGetHierarchyList() {
		assertNotNull(context.getHierarchyList());
	}

	@Test
	public void testGetHierarchyOptions() {
		assertNotNull(context.getHierarchyOptions());
	}

	@Test
	public void testSetSelectedRow() {
		assertEquals(0, context.getSelectedRow());
		context.setSelectedRow(1);
		assertEquals(1, context.getSelectedRow());
	}

	@Test
	public void testGetHierarchyFrame() {
		assertEquals(null, context.getHierarchyFrame());
		assertEquals(null, context.getStatisticsFrame());
		assertEquals(null, context.getInstanceFrame());
		context.createGUI("1");
		assertNotNull(context.getHierarchyFrame());
		assertNotNull(context.getStatisticsFrame());
		assertNotNull(context.getInstanceFrame());
	}

	@Test
	public void testCreateHierarchyVisualization() {
		context.setHierarchy(loadedHierarchy);
		assertNotNull(context.createHierarchyVisualization());
	}

	@Test
	public void testGetDimensionReductionMenager() {
		assertNotNull(context.getDimensionReductionMenager());
	}

	@Test
	public void testIsHierarchyDataLoaded() {
		assertEquals(true, context.isHierarchyDataLoaded());
	}

}
