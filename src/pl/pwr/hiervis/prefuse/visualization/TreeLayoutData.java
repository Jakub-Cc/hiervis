package pl.pwr.hiervis.prefuse.visualization;

/**
 * A simple class containing data and logic used to compute the layout of a
 * tree.
 * 
 * @author Tomasz Bachmiński
 *
 */
public class TreeLayoutData {
	private int nodeSize;
	private int treeOrientation;

	private int layoutWidth;
	private int layoutHeight;

	/** The spacing to maintain between depth levels of the tree. */
	private double depthSpace;
	/** The spacing to maintain between sibling nodes. */
	private double siblingSpace;
	/** The spacing to maintain between neighboring subtrees. */
	private double subtreeSpace;

	public TreeLayoutData(int treeDepth, int treeWidth, int availableWidth, int availableHeight) {
		final double nodeSizeToDepthSpaceRatio = 2.0;
		final double nodeSizeToSiblingsSpaceRatio = 4.0;

		nodeSize = 0;
		int widthBasedSizeOfNodes = 0;
		int heightBasedSizeOfNodes = 0;

		treeOrientation = prefuse.Constants.ORIENT_TOP_BOTTOM;
		depthSpace = 0.0;
		siblingSpace = 0.0;
		subtreeSpace = 0.0;

		layoutWidth = availableWidth;
		layoutHeight = availableHeight;

		depthSpace = layoutHeight / (nodeSizeToDepthSpaceRatio * treeDepth + nodeSizeToDepthSpaceRatio + treeDepth);
		depthSpace = Math.max(1.0, depthSpace);

		// based on above calculation - compute "optimal" size of each node on image
		heightBasedSizeOfNodes = (int) (nodeSizeToDepthSpaceRatio * depthSpace);

		siblingSpace = (layoutWidth) / (treeWidth * nodeSizeToSiblingsSpaceRatio + treeWidth - 1.0);
		siblingSpace = Math.max(1.0, siblingSpace);

		subtreeSpace = siblingSpace;
		widthBasedSizeOfNodes = (int) (nodeSizeToSiblingsSpaceRatio * siblingSpace);

		// below use MAXIMUM height/width
		if (widthBasedSizeOfNodes < heightBasedSizeOfNodes) {
			nodeSize = widthBasedSizeOfNodes;
			// assume maximum possible size
			depthSpace = (layoutHeight - treeDepth * nodeSize - nodeSize) / (double) treeDepth;
			depthSpace = Math.max(1.0, depthSpace);
		} else {
			nodeSize = heightBasedSizeOfNodes;
			// assume maximum possible size
			siblingSpace = (layoutWidth - treeWidth * nodeSize) / (treeWidth - 1.0);
			siblingSpace = Math.max(1.0, siblingSpace);
			subtreeSpace = siblingSpace;
		}
	}

	public int getNodeSize() {
		return nodeSize;
	}

	public int getTreeOrientation() {
		return treeOrientation;
	}

	public int getLayoutWidth() {
		return layoutWidth;
	}

	public int getLayoutHeight() {
		return layoutHeight;
	}

	/**
	 * Returns the spacing to maintain between depth levels of the tree.
	 */
	public double getDepthSpace() {
		return depthSpace;
	}

	/**
	 * Returns the spacing to maintain between sibling nodes.
	 */
	public double getSiblingSpace() {
		return siblingSpace;
	}

	/**
	 * Returns the spacing to maintain between neighboring subtrees.
	 */
	public double getSubtreeSpace() {
		return subtreeSpace;
	}
}
