package pl.pwr.hiervis.hierarchy;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import basic_hierarchy.common.HierarchyBuilder;
import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.interfaces.Node;
import pl.pwr.hiervis.core.HVConfig;
import pl.pwr.hiervis.core.HVConstants;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimension_reduction.HierarchyWraper;
import pl.pwr.hiervis.measures.MeasureResultHolder;
import pl.pwr.hiervis.prefuse.TableEx;
import pl.pwr.hiervis.prefuse.visualization.TreeLayoutData;
import prefuse.data.Tree;

/**
 * Container class used to associate a {@link Hierarchy} instance with various
 * other objects pertaining to that hierarchy, like loading options, and
 * processed data used for visualizations.
 * 
 * @author Tomasz Bachmiński
 *
 */
public class LoadedHierarchy {
	public final LoadedHierarchy.Options options;
	public final MeasureResultHolder measureHolder;

	private final Map<Pair<Node, Boolean>, Hierarchy> nodeHierarchyMap;
	private final Map<Pair<Integer, Integer>, VisualizationState> visStateMap;

	private final HierarchyWraper mainWrapedHierarchy;

	private Tree hierarchyTree;
	private TreeLayoutData hierarchyTreeLayout;
	private TableEx instanceTable;

	private int selectedRow = 0;

	public LoadedHierarchy(Hierarchy h, LoadedHierarchy.Options o) {
		if (h == null)
			throw new IllegalArgumentException("Hierarchy must not be null!");
		if (o == null)
			throw new IllegalArgumentException("Options must not be null!");

		this.options = o;
		this.measureHolder = new MeasureResultHolder();

		this.mainWrapedHierarchy = new HierarchyWraper(h);

		this.nodeHierarchyMap = new HashMap<>();
		this.visStateMap = new HashMap<>();
	}

	/**
	 * Processes the specified hierarchy, building hierarchy tree and creating
	 * instance table used in visualizations.
	 * @param hierarchy the hierarchy to process
	 */
	public void processHierarchy() {
		// TODO:
		// Might want to use some kind of algorithm to figure out optimal tree layout
		// area?
		// 1024x1024 seems to work well enough for now.
		Pair<Tree, TreeLayoutData> treeData = HierarchyProcessor.buildHierarchyTree(
				// mainHierarchy.getRoot(), 2048, 2048
				mainWrapedHierarchy.getHierarchy().getRoot(), 2048, 2048);
		hierarchyTree = treeData.getLeft();
		hierarchyTreeLayout = treeData.getRight();

		instanceTable = HierarchyProcessor.createInstanceTable(this, hierarchyTree);
	}

	/**
	 * @return true if the hierarchy has been processed, and is ready to be
	 *         visualized.
	 */
	public boolean isProcessed() {
		return hierarchyTree != null && hierarchyTreeLayout != null && instanceTable != null;
	}

	public HierarchyWraper getHierarchyWraper() {
		return mainWrapedHierarchy;
	}

	public void setDispHierarchy(int index) {
		mainWrapedHierarchy.setHierarchy(index);
	}

	public Hierarchy getMainHierarchy() {
		return mainWrapedHierarchy.getHierarchy();
	}

	public Hierarchy getNodeHierarchy(Node n, boolean withSubtree) {
		if (n == null) {
			throw new IllegalArgumentException("Node must not be null!");
		}

		Pair<Node, Boolean> pair = Pair.of(n, withSubtree);

		if (!nodeHierarchyMap.containsKey(pair)) {
			if (!HierarchyUtils.contains(mainWrapedHierarchy.getHierarchy(), n)) {
				throw new IllegalArgumentException("Node does not belong to the hierarchy!");
			}
			Hierarchy h = HierarchyUtils.wrapNode(mainWrapedHierarchy.getHierarchy(), n, withSubtree);
			nodeHierarchyMap.put(pair, h);
			return h;
		}
		return nodeHierarchyMap.get(pair);
	}

	public boolean isOwnerOf(Hierarchy h) {
		if (h == null) {
			throw new IllegalArgumentException("Hierarchy must not be null!");
		}
		if (h == mainWrapedHierarchy.getHierarchy()) {
			return true;
		}
		return nodeHierarchyMap.containsValue(h);
	}

	public VisualizationState getVisualizationStateFor(int dimensionIndexX, int dimensionIndexY) {
		return getVisualizationStateFor(Pair.of(dimensionIndexX, dimensionIndexY));
	}

	public VisualizationState getVisualizationStateFor(Pair<Integer, Integer> key) {
		int x = key.getLeft();
		int y = key.getRight();
		if (y > x) {
			key = Pair.of(y, x);
		}

		if (visStateMap.containsKey(key)) {
			return visStateMap.get(key);
		}
		VisualizationState result = new VisualizationState();
		visStateMap.put(key, result);
		return result;
	}

	/**
	 * @return tree structure representing relationships between groups (nodes) in
	 *         the hierarchy. Used to create the main hierarchy visualization.
	 */
	public Tree getTree() {
		return hierarchyTree;
	}

	/**
	 * @return helper layout data for drawing the tree.
	 */
	public TreeLayoutData getTreeLayoutData() {
		return hierarchyTreeLayout;
	}

	/**
	 * @return table containing processed instance data
	 */
	public TableEx getInstanceTable() {
		return instanceTable;
	}

	public void recalculateInstanceTable() {
		if (instanceTable != null) {
			instanceTable.removeAllTableListeners();
			instanceTable.clear();
			instanceTable.dispose();
			instanceTable = null;
		}
		instanceTable = HierarchyProcessor.createInstanceTable(this, hierarchyTree);
	}

	public void clerIns() {
		instanceTable.removeAllTableListeners();
		instanceTable.clear();
		instanceTable.dispose();
		instanceTable = null;
	}

	/**
	 * Sets the row of the currently selected node in the {@link #getTree()
	 * hierarchy tree}
	 * 
	 * @param row the selected row
	 */
	public void setSelectedRow(int row) {
		selectedRow = row;
	}

	/**
	 * @return row of the currently selected node in the {@link #getTree() hierarchy
	 *         tree}
	 */
	public int getSelectedRow() {
		return selectedRow;
	}

	public void dispose() {
		measureHolder.clear();

		nodeHierarchyMap.clear();
		visStateMap.clear();

		hierarchyTree.dispose();
		hierarchyTree.removeAllSets();
		hierarchyTree.clear();
		hierarchyTree = null;

		hierarchyTreeLayout = null;

		if (instanceTable != null) {
			instanceTable.removeAllTableListeners();
			instanceTable.clear();
			instanceTable.dispose();
			instanceTable = null;
		}
	}

	/**
	 * Container class for load options for each {@link Hierarchy} object loaded in
	 * the program.
	 * 
	 * @author Tomasz Bachmiński
	 *
	 */
	public static class Options {
		public static final Options DEFAULT = new Options(false, false, false, false, false);

		public final boolean hasInstanceNameAttribute;
		public final boolean hasTrueClassAttribute;
		public final boolean hasColumnHeader;
		public final boolean isFillBreadthGaps;
		public final boolean isUseSubtree;

		private boolean useTrueClassAtribute;
		private boolean useInstanceNameAttribute;

		private boolean useHkWithDiagonalMatrix;
		private boolean useHkNoStaticCenter;
		private boolean useHkGenerateImages;
		private boolean useHkVerbose;

		private int hkClusters;
		private int hkIterations;
		private int hkRepetitions;
		private int hkDendrogramHeight;
		private int hkMaxNodes;
		private int hkEpsilon;
		private int hkLittleValue;

		/**
		 * @param withInstanceNameAttribute if true, the reader will assume that the
		 *                                  file includes a column containing instance
		 *                                  names
		 * @param withTrueClassAttribute    if true, the reader will assume that the
		 *                                  file includes a column containing true class
		 * @param withHeader                if true, the reader will assume that the
		 *                                  first row contains column headers,
		 *                                  specifying the name for each column
		 * @param fillBreadthGaps           if true, the {@link HierarchyBuilder} will
		 *                                  attempt to fix the raw hierarchy built from
		 *                                  the file.
		 * @param useSubtree                whether the centroid calculation should also
		 *                                  include child groups' instances.
		 */
		public Options(boolean withInstanceNameAttribute, boolean withTrueClassAttribute, boolean withHeader,
				boolean fillBreadthGaps, boolean useSubtree) {
			this.hasInstanceNameAttribute = withInstanceNameAttribute;
			this.hasTrueClassAttribute = withTrueClassAttribute;
			this.hasColumnHeader = withHeader;
			this.isFillBreadthGaps = fillBreadthGaps;
			this.isUseSubtree = useSubtree;

			this.setUseTrueClassAtribute(withTrueClassAttribute);
			this.setUseInstanceNameAttribute(withInstanceNameAttribute);

			HVConfig cfg = HVContext.getContext().getConfig();

			this.setUseHkWithDiagonalMatrix(cfg.isHkWithDiagonalMatrix());
			this.setUseHkNoStaticCenter(cfg.isHkNoStaticCenter());
			this.setUseHkGenerateImages(cfg.isHkGenerateImages());
			this.setUseHkVerbose(cfg.isHkVerbose());

			this.setHkClusters(cfg.getHkClusters());
			this.setHkIterations(cfg.getHkIterations());
			this.setHkDendrogramHeight(cfg.getHkDendrogramHeight());
			this.setHkMaxNodes(cfg.getHkMaxNodes());
			this.setHkEpsilon(cfg.getHkEpsilon());
			this.setHkLittleValue(cfg.getHkLittleValue());
			this.setHkRepetitions(cfg.getHkRepetitions());
		}

		/**
		 * Attempts to detect the appropriate loading options for the specified
		 * hierarchy file.
		 * 
		 * @param file a hierarchy file in CSV format.
		 * @return hopefully appropriate options that will load the file correctly
		 * @throws IOException
		 */
		public static Options detect(File file) throws IOException {
			Path path = file.toPath();
			try (Stream<String> files = Files.lines(path, StandardCharsets.UTF_8)) {
				Object[] lines = files.limit(2).toArray();

				String firstLine = (String) lines[0];
				String secondLine = (String) lines[1];
				String[] columns = firstLine.split(HVConstants.CSV_FILE_SEPARATOR);

				boolean withHeader = !columns[0].startsWith("gen.");
				if (withHeader) {
					columns = secondLine.split(HVConstants.CSV_FILE_SEPARATOR);
				}
				boolean withTrueClass = columns[1].startsWith("gen.");
				boolean withInstanceName = false;
				try {
					Double.valueOf(withTrueClass ? columns[2] : columns[1]);
					// Successfully parsed, meaning that this column contains feature value = no
					// instance name
					withInstanceName = false;
				} catch (NumberFormatException e) {
					// Failed to parse, meaning that this column contains some non-digit chars =
					// likely instance name
					withInstanceName = true;
				}

				return new Options(withInstanceName, withTrueClass, withHeader, DEFAULT.isFillBreadthGaps,
						DEFAULT.isUseSubtree);
			}
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Options) {
				return equals((Options) o);
			}
			return false;
		}

		public boolean equals(Options o) {
			return hasInstanceNameAttribute == o.hasInstanceNameAttribute
					&& hasTrueClassAttribute == o.hasTrueClassAttribute && hasColumnHeader == o.hasColumnHeader
					&& isFillBreadthGaps == o.isFillBreadthGaps && isUseSubtree == o.isUseSubtree;
		}

		public boolean isUseTrueClassAtribute() {
			return useTrueClassAtribute;
		}

		public void setUseTrueClassAtribute(boolean useTrueClassAtribute) {
			this.useTrueClassAtribute = useTrueClassAtribute;
		}

		public boolean isUseInstanceNameAttribute() {
			return useInstanceNameAttribute;
		}

		public void setUseInstanceNameAttribute(boolean useInstanceNameAttribute) {
			this.useInstanceNameAttribute = useInstanceNameAttribute;
		}

		public boolean isUseHkWithDiagonalMatrix() {
			return useHkWithDiagonalMatrix;
		}

		public void setUseHkWithDiagonalMatrix(boolean useHkWithDiagonalMatrix) {
			this.useHkWithDiagonalMatrix = useHkWithDiagonalMatrix;
		}

		public boolean isUseHkNoStaticCenter() {
			return useHkNoStaticCenter;
		}

		public void setUseHkNoStaticCenter(boolean useHkNoStaticCenter) {
			this.useHkNoStaticCenter = useHkNoStaticCenter;
		}

		public boolean isUseHkGenerateImages() {
			return useHkGenerateImages;
		}

		public void setUseHkGenerateImages(boolean useHkGenerateImages) {
			this.useHkGenerateImages = useHkGenerateImages;
		}

		public boolean isUseHkVerbose() {
			return useHkVerbose;
		}

		public void setUseHkVerbose(boolean useHkVerbose) {
			this.useHkVerbose = useHkVerbose;
		}

		public int getHkClusters() {
			return hkClusters;
		}

		public void setHkClusters(int hkClusters) {
			this.hkClusters = hkClusters;
		}

		public int getHkIterations() {
			return hkIterations;
		}

		public void setHkIterations(int hkIterations) {
			this.hkIterations = hkIterations;
		}

		public int getHkRepetitions() {
			return hkRepetitions;
		}

		public void setHkRepetitions(int hkRepetitions) {
			this.hkRepetitions = hkRepetitions;
		}

		public int getHkDendrogramHeight() {
			return hkDendrogramHeight;
		}

		public void setHkDendrogramHeight(int hkDendrogramHeight) {
			this.hkDendrogramHeight = hkDendrogramHeight;
		}

		public int getHkMaxNodes() {
			return hkMaxNodes;
		}

		public void setHkMaxNodes(int hkMaxNodes) {
			this.hkMaxNodes = hkMaxNodes;
		}

		public int getHkEpsilon() {
			return hkEpsilon;
		}

		public void setHkEpsilon(int hkEpsilon) {
			this.hkEpsilon = hkEpsilon;
		}

		public int getHkLittleValue() {
			return hkLittleValue;
		}

		public void setHkLittleValue(int hkLittleValue) {
			this.hkLittleValue = hkLittleValue;
		}
	}

}
