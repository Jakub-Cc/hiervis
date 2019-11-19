package pl.pwr.hiervis.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import basic_hierarchy.common.Constants;
import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.implementation.BasicHierarchy;
import basic_hierarchy.implementation.BasicInstance;
import basic_hierarchy.implementation.BasicNode;
import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.interfaces.Instance;
import basic_hierarchy.interfaces.Node;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class LoadedHierarchyUtils {
	private LoadedHierarchyUtils() {
		// Static class -- disallow instantiation.
		throw new RuntimeException("Attempted to instantiate a static class: " + getClass().getName());
	}

	/**
	 * Merges the two hierarchies into a single, new hierarchy, with the specified
	 * merging node id.
	 * 
	 * @param source the source hierarchy that is to be merged into destination
	 *               hierarchy
	 * @param dest   the destination hierarchy that will receive nodes from source
	 *               hierarchy
	 * @param nodeId id of the node in the destination hierarchy that will serve as
	 *               the merging point for the two hierarchies. Nodes from the
	 *               source hierarchy will be rebased to have this id as root.
	 * @return the new, merged hierarchy (shallow copy of both source and dest
	 *         hierarchies)
	 */
	public static LoadedHierarchy merge(LoadedHierarchy source, LoadedHierarchy dest, String nodeId) {
		// Check parameters...
		if (source == null) {
			throw new IllegalArgumentException("Source hierarchy is null.");
		}
		if (dest == null) {
			throw new IllegalArgumentException("Destination hierarchy is null.");
		}

		int sourceDims = HierarchyUtils.getFeatureCount(source.getMainHierarchy());
		int destDims = HierarchyUtils.getFeatureCount(dest.getMainHierarchy());

		if (sourceDims != destDims) {
			throw new IllegalArgumentException(String.format(
					"Cannot merge hierarchies, because they have different feature counts - source: %s, dest: %s",
					sourceDims, destDims));
		}

		// Invoke the actual merging method...
		return new LoadedHierarchy(HierarchyUtils.merge(source.getMainHierarchy(), dest.getMainHierarchy(), nodeId),
				source.options);
	}

	/**
	 * Returns a copy of the specified hierarchy. The returned copy is a hierarchy
	 * containing all instances but with removed dimensions
	 * 
	 * @param source           the hierarchy to remove dimensions
	 * @param dimensionNumbers list of dimensions indexes to remove
	 * @return new hierarchy
	 */
	public static LoadedHierarchy removeDimensions(LoadedHierarchy source, List<Integer> dimensionNumbers) {
		final boolean useSubtree = false;

		Hierarchy newHierarchy = HierarchyUtils.clone(source.getMainHierarchy(), useSubtree, null);
		for (Instance instance : newHierarchy.getRoot().getSubtreeInstances()) {
			double[] data = instance.getData();
			double[] newData = HierarchyUtils.dropDimensions(data, dimensionNumbers);
			instance.setData(newData);
		}

		String[] newNames = null;
		String[] names = source.getMainHierarchy().getDataNames();
		if (names == null) {
			names = new String[source.getMainHierarchy().getRoot().getSubtreeInstances().getFirst().getData().length];
			for (int i = 0; i < names.length; i++) {
				names[i] = "dimension " + (i + 1);
			}
		}
		newNames = HierarchyUtils.dropDimensions(names, dimensionNumbers);
		List<Node> nodes = HierarchyUtils.getAllNodes(newHierarchy);
		newHierarchy = new BasicHierarchy(nodes, newNames);

		return new LoadedHierarchy(newHierarchy, source.options);
	}

	/**
	 * Returns a copy of the specified hierarchy. The returned copy is a flat
	 * hierarchy containing all instances from the source hierarchy in the root
	 * node.
	 * 
	 * @param source the hierarchy to flatten
	 * @return flattened hierarchy
	 */
	public static LoadedHierarchy flattenHierarchy(LoadedHierarchy source) {
		final boolean useSubtree = false;

		BasicNode root = new BasicNode(Constants.ROOT_ID, null, false);

		List<BasicNode> nodes = new LinkedList<>();
		nodes.add(root);

		source.getMainHierarchy().getRoot().getSubtreeInstances().forEach(in -> {
			root.addInstance(
					new BasicInstance(in.getInstanceName(), Constants.ROOT_ID, in.getData(), in.getTrueClass()));
		});

		Hierarchy flatHierarchy = HierarchyUtils.buildHierarchy(nodes, source.getMainHierarchy().getDataNames(),
				useSubtree);
		return new LoadedHierarchy(flatHierarchy, source.options);
	}

	/**
	 * Returns a copy of the specified loadedHierarchy and all calculated dimension
	 * reduction. The returned copy is a flat hierarchy containing all instances
	 * from the source hierarchy in the root node.
	 * 
	 * @param source the hierarchy to flatten
	 * @return flattened hierarchy
	 */
	public static LoadedHierarchy flattenHierarchyKeepReductons(LoadedHierarchy source) {

		Hierarchy mainHierarchy = HierarchyUtils.flattenHierarchy(source.getHierarchyWraper().getOriginalHierarchy());
		LoadedHierarchy baseLoadedHierarchy = new LoadedHierarchy(mainHierarchy, source.options);

		Hierarchy[] reducedHierarchy = new Hierarchy[source.getHierarchyWraper().getReducedHierarchy().length];

		for (int i = 0; i < reducedHierarchy.length; i++) {
			if (source.getHierarchyWraper().getReducedHierarchy(i) != null)
				reducedHierarchy[i] = HierarchyUtils
						.flattenHierarchy(source.getHierarchyWraper().getReducedHierarchy(i));
			else
				reducedHierarchy[i] = null;
		}

		baseLoadedHierarchy.getHierarchyWraper().setReducedHierarchy(reducedHierarchy);

		return baseLoadedHierarchy;
	}

	/**
	 * Finds the hierarchy group at the specified row.
	 * 
	 * @param h   the hierarchy to search in
	 * @param row the row in the data table at which the node is located.
	 * @return the group at the specified row, or null if not found.
	 */
	public static Node findGroup(LoadedHierarchy h, int row) {
		Node group = h.getMainHierarchy().getRoot();

		if (row == 0) {
			return group;
		}

		Queue<Node> stack = new LinkedList<>();
		for (Node child : group.getChildren()) {
			stack.add(child);
		}

		int currentRow = 0;
		while (!stack.isEmpty()) {
			group = stack.remove();

			++currentRow;
			if (currentRow == row) {
				return group;
			}

			for (Node child : group.getChildren()) {
				stack.add(child);
			}
		}

		return null;
	}

	/**
	 * Searches the specified hierarchy for a group with the specified name.
	 * 
	 * @param lh     the hierarchy to look in
	 * @param nodeId name of the hierarchy group
	 * @return the hierarchy group with the specified name, or null if not found.
	 */
	public static Node findGroup(LoadedHierarchy lh, String nodeId) {
		for (Node n : lh.getMainHierarchy().getGroups()) {
			if (n.getId().equals(nodeId))
				return n;
		}

		return null;
	}
}
