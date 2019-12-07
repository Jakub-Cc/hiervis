package pl.pwr.hiervis.hk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.core.HVConfig;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class HKPlusPlusWrapperTest {

	HVConfig hvConfig;
	HKPlusPlusWrapper wrapper;
	Hierarchy hierarchy;

	@Before
	public void init() {
		hvConfig = new HVConfig();
		wrapper = new HKPlusPlusWrapper(hvConfig);
		hierarchy = TestCommon.getFourGroupsHierarchy();
	}

	@Test
	public void testHKPlusPlusWrapper() {
		assertNotNull(wrapper);
	}

	@Test
	public void testPrepareInputFile() {
		try {
			wrapper.prepareInputFile(hierarchy, hierarchy.getRoot(), false, false);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetOutputHierarchy() {
		try {
			LoadedHierarchy l = wrapper.getOutputHierarchy(false, false, false);
			assertNotNull(l);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetConfig() {
		assertEquals(hvConfig, wrapper.getConfig());
	}

	@Test
	public void testDestroy() {
		run();
		wrapper.destroy();

	}

	private void run() {
		LoadedHierarchy loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
		HKPlusPlusParameter parameter = new HKPlusPlusParameter(loadedHierarchy, hierarchy.getRoot(), null, false,
				false, false, false, false, 1, 1, 1, 1, 1, 1, 1, false);
		try {
			wrapper.start(parameter);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
