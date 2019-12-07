package pl.pwr.hiervis.dimension_reduction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class VisibleDimensionsKeeperTest {

	VisibleDimensionsKeeper visibleDimensionsKeeper;

	@Before
	public void init() {
		visibleDimensionsKeeper = new VisibleDimensionsKeeper(2);
	}

	@Test
	public void testGetVisibleVerticalDimensions() {
		assertNotNull(visibleDimensionsKeeper.getVisibleVerticalDimensions());
	}

	@Test
	public void testGetVisibleHorizontalDimensions() {
		assertNotNull(visibleDimensionsKeeper.getVisibleHorizontalDimensions());
	}

	@Test
	public void testToggleVisibility() {
		assertTrue(!visibleDimensionsKeeper.getVisibleHorizontalDimensions()[0]);
		visibleDimensionsKeeper.toggleVisibility(0, true, true);
		assertTrue(visibleDimensionsKeeper.getVisibleHorizontalDimensions()[0]);
	}

	@Test
	public void testGetString() {
		String msg = "\n" + "H: false,false,\n" + "V: false,false,";
		assertEquals(msg, visibleDimensionsKeeper.getString());
	}

}
