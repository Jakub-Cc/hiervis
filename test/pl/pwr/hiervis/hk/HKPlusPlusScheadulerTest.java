package pl.pwr.hiervis.hk;

import static org.junit.Assert.assertNotNull;

import java.awt.Window;

import org.junit.Test;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.interfaces.Node;
import basic_hierarchy.test.TestCommon;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class HKPlusPlusScheadulerTest {

	@Test
	public void testGetHKPlusPlusScheaduler() {
		assertNotNull(HKPlusPlusScheaduler.getHKPlusPlusScheaduler());
	}

	@Test
	public void testAddToQue() {

		Hierarchy hierarchy = TestCommon.getFourGroupsHierarchy();
		LoadedHierarchy loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));
		Node node = hierarchy.getRoot();
		Window owner = null;
		boolean trueClassAttribute = false;
		boolean instanceNames = false;
		boolean diagonalMatrix = false;
		boolean disableStaticCenter = false;
		boolean generateImages = false;
		int epsilon = 1;
		int littleValue = 1;
		int clusters = 1;
		int iterations = 1;
		int repeats = 1;
		int dendrogramSize = 1;
		int maxNodeCount = 1;
		boolean verbose = false;
		HKPlusPlusScheaduler.getHKPlusPlusScheaduler().addToQue(loadedHierarchy, node, owner, trueClassAttribute,
				instanceNames, diagonalMatrix, disableStaticCenter, generateImages, epsilon, littleValue, clusters,
				iterations, repeats, dendrogramSize, maxNodeCount, verbose);
	}

}
