package pl.pwr.hiervis.ui;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import pl.pwr.hiervis.core.HVContext;

public class InstanceVisualizationsFrameTest {
	InstanceVisualizationsFrame instanceVisualizationsFrame;

	@Before
	public void init() {
		instanceVisualizationsFrame = new InstanceVisualizationsFrame(HVContext.getContext(), "sub");
	}

	@Test
	public void testInstanceVisualizationsFrame() {
		assertNotNull(instanceVisualizationsFrame);
	}

	@Test
	public void testDisposeDisplays() {
		instanceVisualizationsFrame.disposeDisplays();
		// void method?
	}

	@Test
	public void testClearUI() {
		instanceVisualizationsFrame.clearUI();
		// void method?
	}

}
