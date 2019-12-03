package pl.pwr.hiervis.hk;

import java.awt.Window;

import basic_hierarchy.interfaces.Node;
import pl.pwr.hiervis.core.HVConfig;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class HKPlusPlusParameter {
	private LoadedHierarchy hierarchy;
	private Node node;
	private Window owner;
	private boolean trueClassAttribute;
	private boolean instanceNames;
	private boolean diagonalMatrix;
	private boolean disableStaticCenter;
	private boolean generateImages;
	private int epsilon;
	private int littleValue;
	private int clusters;
	private int iterations;
	private int repeats;
	private int dendrogramSize;
	private int maxNodeCount;
	private boolean verbose;

	HKPlusPlusParameter(LoadedHierarchy hierarchy, Node node, Window owner, boolean trueClassAttribute,
			boolean instanceNames, boolean diagonalMatrix, boolean disableStaticCenter, boolean generateImages,
			int epsilon, int littleValue, int clusters, int iterations, int repeats, int dendrogramSize,
			int maxNodeCount, boolean verbose) {
		this.setHierarchy(hierarchy);
		this.setNode(node);
		this.setOwner(owner);
		this.setTrueClassAttribute(trueClassAttribute);
		this.setInstanceNames(instanceNames);
		this.setDiagonalMatrix(diagonalMatrix);
		this.setDisableStaticCenter(disableStaticCenter);
		this.setGenerateImages(generateImages);
		this.setEpsilon(epsilon);
		this.setLittleValue(littleValue);
		this.setClusters(clusters);
		this.setIterations(iterations);
		this.setRepeats(repeats);
		this.setDendrogramSize(dendrogramSize);
		this.setMaxNodeCount(maxNodeCount);
		this.setVerbose(verbose);
	}

	public void saveParrameterToHierarchy() {
		this.getHierarchy().options.setHkClusters(this.getClusters());
		this.getHierarchy().options.setHkIterations(this.getIterations());
		this.getHierarchy().options.setHkRepetitions(this.getRepeats());
		this.getHierarchy().options.setHkDendrogramHeight(this.getDendrogramSize());
		this.getHierarchy().options.setHkMaxNodes(this.getMaxNodeCount());
		this.getHierarchy().options.setHkEpsilon(this.getEpsilon());
		this.getHierarchy().options.setHkLittleValue(this.getLittleValue());
		this.getHierarchy().options.setUseTrueClassAtribute(this.isTrueClassAttribute());
		this.getHierarchy().options.setUseInstanceNameAttribute(this.isInstanceNames());
		this.getHierarchy().options.setUseHkWithDiagonalMatrix(this.isDiagonalMatrix());
		this.getHierarchy().options.setUseHkNoStaticCenter(this.isDisableStaticCenter());
		this.getHierarchy().options.setUseHkGenerateImages(this.isGenerateImages());
		this.getHierarchy().options.setUseHkVerbose(this.isVerbose());
	}

	public void saveSettingsToConfig() {
		HVConfig cfg = HVContext.getContext().getConfig();
		cfg.setHkClusters(this.getClusters());
		cfg.setHkIterations(this.getIterations());
		cfg.setHkRepetitions(this.getRepeats());
		cfg.setHkDendrogramHeight(this.getDendrogramSize());
		cfg.setHkMaxNodes(this.getMaxNodeCount());
		cfg.setHkEpsilon(this.getEpsilon());
		cfg.setHkLittleValue(this.getLittleValue());
		cfg.setHkWithTrueClass(this.isTrueClassAttribute());
		cfg.setHkWithInstanceNames(this.isInstanceNames());
		cfg.setHkWithDiagonalMatrix(this.isDiagonalMatrix());
		cfg.setHkNoStaticCenter(this.isDisableStaticCenter());
		cfg.setHkGenerateImages(this.isGenerateImages());
		cfg.setHkVerbose(this.isVerbose());
	}

	public LoadedHierarchy getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(LoadedHierarchy hierarchy) {
		this.hierarchy = hierarchy;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Window getOwner() {
		return owner;
	}

	public void setOwner(Window owner) {
		this.owner = owner;
	}

	public boolean isTrueClassAttribute() {
		return trueClassAttribute;
	}

	public void setTrueClassAttribute(boolean trueClassAttribute) {
		this.trueClassAttribute = trueClassAttribute;
	}

	public boolean isInstanceNames() {
		return instanceNames;
	}

	public void setInstanceNames(boolean instanceNames) {
		this.instanceNames = instanceNames;
	}

	public boolean isDiagonalMatrix() {
		return diagonalMatrix;
	}

	public void setDiagonalMatrix(boolean diagonalMatrix) {
		this.diagonalMatrix = diagonalMatrix;
	}

	public boolean isDisableStaticCenter() {
		return disableStaticCenter;
	}

	public void setDisableStaticCenter(boolean disableStaticCenter) {
		this.disableStaticCenter = disableStaticCenter;
	}

	public boolean isGenerateImages() {
		return generateImages;
	}

	public void setGenerateImages(boolean generateImages) {
		this.generateImages = generateImages;
	}

	public int getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(int epsilon) {
		this.epsilon = epsilon;
	}

	public int getLittleValue() {
		return littleValue;
	}

	public void setLittleValue(int littleValue) {
		this.littleValue = littleValue;
	}

	public int getClusters() {
		return clusters;
	}

	public void setClusters(int clusters) {
		this.clusters = clusters;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public int getRepeats() {
		return repeats;
	}

	public void setRepeats(int repeats) {
		this.repeats = repeats;
	}

	public int getDendrogramSize() {
		return dendrogramSize;
	}

	public void setDendrogramSize(int dendrogramSize) {
		this.dendrogramSize = dendrogramSize;
	}

	public int getMaxNodeCount() {
		return maxNodeCount;
	}

	public void setMaxNodeCount(int maxNodeCount) {
		this.maxNodeCount = maxNodeCount;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
}
