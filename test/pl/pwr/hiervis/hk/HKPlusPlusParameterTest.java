package pl.pwr.hiervis.hk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Window;

import org.junit.Before;
import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.interfaces.Node;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class HKPlusPlusParameterTest {

	HKPlusPlusParameter parameter;
	private Hierarchy hierarchy;
	LoadedHierarchy loadedHierarchy;
	Node node;
	Window owner;
	boolean trueClassAttribute;
	boolean instanceNames;
	boolean diagonalMatrix;
	boolean disableStaticCenter;
	boolean generateImages;
	int epsilon;
	int littleValue;
	int clusters;
	int iterations;
	int repeats;
	int dendrogramSize;
	int maxNodeCount;
	boolean verbose;

	@Before
	public void init() {
		hierarchy = TestCommon.getFourGroupsHierarchy();
		loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
		node = hierarchy.getRoot();
		owner = null;
		trueClassAttribute = false;
		instanceNames = false;
		diagonalMatrix = false;
		disableStaticCenter = false;
		generateImages = false;
		epsilon = 1;
		littleValue = 1;
		clusters = 1;
		iterations = 1;
		repeats = 1;
		dendrogramSize = 1;
		maxNodeCount = 1;
		verbose = false;
		parameter = new HKPlusPlusParameter(loadedHierarchy, node, owner, trueClassAttribute, instanceNames,
				diagonalMatrix, disableStaticCenter, generateImages, epsilon, littleValue, clusters, iterations,
				repeats, dendrogramSize, maxNodeCount, verbose);
	}

	@Test
	public void testHKPlusPlusParameter() {
		assertNotNull(parameter);
	}

	@Test
	public void testSaveParrameterToHierarchy() {
		parameter.setIterations(3);
		assertNotEquals(3, loadedHierarchy.options.getHkIterations());
		parameter.saveParrameterToHierarchy();
		assertEquals(3, loadedHierarchy.options.getHkIterations());
	}

	@Test
	public void testSaveSettingsToConfig() {
		assertNotEquals(2, HVContext.getContext().getConfig().getHkIterations());
		parameter.saveSettingsToConfig();
		assertEquals(1, HVContext.getContext().getConfig().getHkIterations());
		parameter.setIterations(2);
		parameter.saveSettingsToConfig();
		assertEquals(2, HVContext.getContext().getConfig().getHkIterations());
	}

	@Test
	public void testSetHierarchy() {
		assertEquals(loadedHierarchy, parameter.getHierarchy());
		parameter.setHierarchy(null);
		assertEquals(null, parameter.getHierarchy());
	}

	@Test
	public void testSetNode() {
		assertEquals(node, parameter.getNode());
		parameter.setNode(null);
		assertEquals(null, parameter.getNode());
	}

	@Test
	public void testSetOwner() {
		assertEquals(null, parameter.getOwner());
		Window owner2 = new Window(null);
		parameter.setOwner(owner2);
		assertEquals(owner2, parameter.getOwner());
	}

	@Test
	public void testSetTrueClassAttribute() {
		assertEquals(false, parameter.isTrueClassAttribute());
		parameter.setTrueClassAttribute(true);
		assertEquals(true, parameter.isTrueClassAttribute());
	}

	@Test
	public void testSetInstanceNames() {
		assertEquals(false, parameter.isInstanceNames());
		parameter.setInstanceNames(true);
		assertEquals(true, parameter.isInstanceNames());
	}

	@Test
	public void testSetDiagonalMatrix() {
		assertEquals(false, parameter.isDiagonalMatrix());
		parameter.setDiagonalMatrix(true);
		assertEquals(true, parameter.isDiagonalMatrix());
	}

	@Test
	public void testSetDisableStaticCenter() {
		assertEquals(false, parameter.isDisableStaticCenter());
		parameter.setDisableStaticCenter(true);
		assertEquals(true, parameter.isDisableStaticCenter());
	}

	@Test
	public void testSetGenerateImages() {
		assertEquals(false, parameter.isGenerateImages());
		parameter.setGenerateImages(true);
		assertEquals(true, parameter.isGenerateImages());
	}

	@Test
	public void testSetEpsilon() {
		assertEquals(1, parameter.getEpsilon());
		parameter.setEpsilon(2);
		assertEquals(2, parameter.getEpsilon());
	}

	@Test
	public void testSetLittleValue() {
		assertEquals(1, parameter.getLittleValue());
		parameter.setLittleValue(2);
		assertEquals(2, parameter.getLittleValue());
	}

	@Test
	public void testSetClusters() {
		assertEquals(1, parameter.getClusters());
		parameter.setClusters(2);
		assertEquals(2, parameter.getClusters());
	}

	@Test
	public void testSetIterations() {
		assertEquals(1, parameter.getIterations());
		parameter.setIterations(2);
		assertEquals(2, parameter.getIterations());
	}

	@Test
	public void testSetRepeats() {
		assertEquals(1, parameter.getRepeats());
		parameter.setRepeats(2);
		assertEquals(2, parameter.getRepeats());
	}

	@Test
	public void testSetDendrogramSize() {
		assertEquals(1, parameter.getDendrogramSize());
		parameter.setDendrogramSize(2);
		assertEquals(2, parameter.getDendrogramSize());
	}

	@Test
	public void testSetMaxNodeCount() {
		assertEquals(1, parameter.getMaxNodeCount());
		parameter.setMaxNodeCount(2);
		assertEquals(2, parameter.getMaxNodeCount());
	}

	@Test
	public void testSetVerbose() {
		assertEquals(false, parameter.isVerbose());
		parameter.setVerbose(true);
		assertEquals(true, parameter.isVerbose());
	}

}
