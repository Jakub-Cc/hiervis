package pl.pwr.hiervis.hk;

import java.awt.Window;

import basic_hierarchy.interfaces.Node;
import pl.pwr.hiervis.core.HVConfig;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class HKPlusPlusParameter {
	public LoadedHierarchy hierarchy;
	public Node node;
	public Window owner;
	public boolean trueClassAttribute;
	public boolean instanceNames;
	public boolean diagonalMatrix;
	public boolean disableStaticCenter;
	public boolean generateImages;
	public int epsilon;
	public int littleValue;
	public int clusters;
	public int iterations;
	public int repeats;
	public int dendrogramSize;
	public int maxNodeCount;
	public boolean verbose;

	HKPlusPlusParameter(LoadedHierarchy hierarchy, Node node, Window owner, boolean trueClassAttribute,
			boolean instanceNames, boolean diagonalMatrix, boolean disableStaticCenter, boolean generateImages,
			int epsilon, int littleValue, int clusters, int iterations, int repeats, int dendrogramSize,
			int maxNodeCount, boolean verbose) {
		this.hierarchy = hierarchy;
		this.node = node;
		this.owner = owner;
		this.trueClassAttribute = trueClassAttribute;
		this.instanceNames = instanceNames;
		this.diagonalMatrix = diagonalMatrix;
		this.disableStaticCenter = disableStaticCenter;
		this.generateImages = generateImages;
		this.epsilon = epsilon;
		this.littleValue = littleValue;
		this.clusters = clusters;
		this.iterations = iterations;
		this.repeats = repeats;
		this.dendrogramSize = dendrogramSize;
		this.maxNodeCount = maxNodeCount;
		this.verbose = verbose;
	}

	public void saveParrameterToHierarchy() {
		this.hierarchy.options.HkClusters = this.clusters;
		this.hierarchy.options.HkIterations = this.iterations;
		this.hierarchy.options.HkRepetitions = this.repeats;
		this.hierarchy.options.HkDendrogramHeight = this.dendrogramSize;
		this.hierarchy.options.HkMaxNodes = this.maxNodeCount;
		this.hierarchy.options.HkEpsilon = this.epsilon;
		this.hierarchy.options.HkLittleValue = this.littleValue;
		this.hierarchy.options.useTrueClassAtribute = this.trueClassAttribute;
		this.hierarchy.options.useInstanceNameAttribute = this.instanceNames;
		this.hierarchy.options.useHkWithDiagonalMatrix = this.diagonalMatrix;
		this.hierarchy.options.useHkNoStaticCenter = this.disableStaticCenter;
		this.hierarchy.options.useHkGenerateImages = this.generateImages;
		this.hierarchy.options.useHkVerbose = this.verbose;
	}

	public void saveSettingsToConfig() {
		HVConfig cfg = HVContext.getContext().getConfig();
		cfg.setHkClusters(this.clusters);
		cfg.setHkIterations(this.iterations);
		cfg.setHkRepetitions(this.repeats);
		cfg.setHkDendrogramHeight(this.dendrogramSize);
		cfg.setHkMaxNodes(this.maxNodeCount);
		cfg.setHkEpsilon(this.epsilon);
		cfg.setHkLittleValue(this.littleValue);
		cfg.setHkWithTrueClass(this.trueClassAttribute);
		cfg.setHkWithInstanceNames(this.instanceNames);
		cfg.setHkWithDiagonalMatrix(this.diagonalMatrix);
		cfg.setHkNoStaticCenter(this.disableStaticCenter);
		cfg.setHkGenerateImages(this.generateImages);
		cfg.setHkVerbose(this.verbose);
	}
}
