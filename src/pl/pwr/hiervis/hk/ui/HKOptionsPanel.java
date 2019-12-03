package pl.pwr.hiervis.hk.ui;

import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.Logger;

import basic_hierarchy.interfaces.Node;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.hk.HKPlusPlusScheaduler;
import pl.pwr.hiervis.util.ui.GridBagConstraintsBuilder;

@SuppressWarnings("serial")
public class HKOptionsPanel extends JPanel {
	private transient Logger logger = null;
	private transient HVContext context;
	private transient LoadedHierarchy hierarchy;
	private transient Node node;
	private transient ActionListener actionListener;

	private MultiIntField txtClusters = null;
	private MultiIntField txtIterations = null;
	private MultiIntField txtRepeats = null;
	private MultiIntField txtDendrogram = null;
	private MultiIntField txtMaxNodes = null;
	private MultiIntField txtEpsilon = null;
	private MultiIntField txtLittleVal = null;
	private JCheckBox cboxTrueClass = null;
	private JCheckBox cboxInstanceNames = null;
	private JCheckBox cboxDiagonalMatrix = null;
	private JCheckBox cboxNoStaticCenter = null;
	private JCheckBox cboxGenerateImages = null;
	private JCheckBox cboxVerbose = null;

	public HKOptionsPanel(HVContext context, Node node, Logger logger, ActionListener actionListener) {
		this.context = context;
		this.hierarchy = context.getHierarchy();
		this.node = node;
		this.logger = logger;
		this.actionListener = actionListener;

		createContent();
	}

	private void createContent() {
		GridBagConstraintsBuilder builder = new GridBagConstraintsBuilder();

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 0, 0 };
		layout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		layout.columnWeights = new double[] { 1.0, 0.0 };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		setLayout(layout);

		JLabel lblClusters = new JLabel("[-k] Clusters:");
		txtClusters = new MultiIntField(1, Integer.MAX_VALUE);
		txtClusters.addActionListener(actionListener);
		add(lblClusters, builder.insets(5).anchorWest().position(0, 0).build());
		add(txtClusters, builder.insets(5).anchorEast().position(1, 0).build());

		JLabel lblIterations = new JLabel("[-n] Iterations:");
		txtIterations = new MultiIntField(1, Integer.MAX_VALUE);
		txtIterations.addActionListener(actionListener);
		add(lblIterations, builder.insets(5).anchorWest().position(0, 1).build());
		add(txtIterations, builder.insets(5).anchorEast().position(1, 1).build());

		JLabel lblRepeats = new JLabel("[-r] Repeats:");
		txtRepeats = new MultiIntField(1, Integer.MAX_VALUE);
		txtRepeats.addActionListener(actionListener);
		add(lblRepeats, builder.insets(5).anchorWest().position(0, 2).build());
		add(txtRepeats, builder.insets(5).anchorEast().position(1, 2).build());

		JLabel lblDendrogram = new JLabel("[-s] Max Dendrogram Height:");
		txtDendrogram = new MultiIntField(1, Integer.MAX_VALUE);
		txtDendrogram.addActionListener(actionListener);
		add(lblDendrogram, builder.insets(5).anchorWest().position(0, 3).build());
		add(txtDendrogram, builder.insets(5).anchorEast().position(1, 3).build());

		JLabel lblMaxNodes = new JLabel("[-w] Max Generated Nodes:");
		txtMaxNodes = new MultiIntField(-1, Integer.MAX_VALUE);
		txtMaxNodes.addActionListener(actionListener);
		add(lblMaxNodes, builder.insets(5).anchorWest().position(0, 4).build());
		add(txtMaxNodes, builder.insets(5).anchorEast().position(1, 4).build());

		JLabel lblEpsilon = new JLabel("[-e] Epsilon:");
		txtEpsilon = new MultiIntField(Integer.MIN_VALUE, Integer.MAX_VALUE);
		txtEpsilon.addActionListener(actionListener);
		add(lblEpsilon, builder.insets(5).anchorWest().position(0, 5).build());
		add(txtEpsilon, builder.insets(5).anchorEast().position(1, 5).build());

		JLabel lblLittleVal = new JLabel("[-l] Little Value:");
		txtLittleVal = new MultiIntField(Integer.MIN_VALUE, Integer.MAX_VALUE);
		txtLittleVal.addActionListener(actionListener);
		add(lblLittleVal, builder.insets(5).anchorWest().position(0, 6).build());
		add(txtLittleVal, builder.insets(5).anchorEast().position(1, 6).build());

		JLabel lblTrueClass = new JLabel("[-c] Include True Class:");
		cboxTrueClass = new JCheckBox();
		cboxTrueClass.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblTrueClass, builder.insets(5).anchorWest().position(0, 7).build());
		add(cboxTrueClass, builder.insets(5).anchorCenter().fill().position(1, 7).build());

		JLabel lblInstanceNames = new JLabel("[-in] Include Instance Names:");
		cboxInstanceNames = new JCheckBox();
		cboxInstanceNames.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblInstanceNames, builder.insets(5).anchorWest().position(0, 8).build());
		add(cboxInstanceNames, builder.insets(5).anchorCenter().fill().position(1, 8).build());

		JLabel lblDiagonalMatrix = new JLabel("[-dm] Use Diagonal Matrix:");
		cboxDiagonalMatrix = new JCheckBox();
		cboxDiagonalMatrix.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblDiagonalMatrix, builder.insets(5).anchorWest().position(0, 9).build());
		add(cboxDiagonalMatrix, builder.insets(5).anchorCenter().fill().position(1, 9).build());

		JLabel lblNoStaticCenter = new JLabel("[-ds] Disable Static Center:");
		cboxNoStaticCenter = new JCheckBox();
		cboxNoStaticCenter.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblNoStaticCenter, builder.insets(5).anchorWest().position(0, 10).build());
		add(cboxNoStaticCenter, builder.insets(5).anchorCenter().fill().position(1, 10).build());

		JLabel lblGenerateImages = new JLabel("[-gi] Generate Images:");
		cboxGenerateImages = new JCheckBox();
		cboxGenerateImages.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblGenerateImages, builder.insets(5).anchorWest().position(0, 11).build());
		add(cboxGenerateImages, builder.insets(5).anchorCenter().fill().position(1, 11).build());

		JLabel lblVerbose = new JLabel("<html>[-v] Verbose Mode<br> (Output will be visible from the command line):");
		cboxVerbose = new JCheckBox();
		cboxVerbose.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblVerbose, builder.insets(5).anchorWest().position(0, 12).build());
		add(cboxVerbose, builder.insets(5).anchorCenter().fill().position(1, 12).build());
	}

	public void setupDefaultValues() {
		cboxTrueClass.setEnabled(hierarchy.options.hasTrueClassAttribute);
		cboxInstanceNames.setEnabled(hierarchy.options.hasInstanceNameAttribute);
		txtClusters.setText("" + hierarchy.options.getHkClusters());
		txtIterations.setText("" + hierarchy.options.getHkIterations());
		txtRepeats.setText("" + hierarchy.options.getHkRepetitions());
		txtDendrogram.setText("" + hierarchy.options.getHkDendrogramHeight());
		txtMaxNodes.setText("" + hierarchy.options.getHkMaxNodes());
		txtEpsilon.setText("" + hierarchy.options.getHkEpsilon());
		txtLittleVal.setText("" + hierarchy.options.getHkLittleValue());
		cboxTrueClass.setSelected(hierarchy.options.isUseTrueClassAtribute());
		cboxInstanceNames.setSelected(hierarchy.options.isUseInstanceNameAttribute());
		cboxDiagonalMatrix.setSelected(hierarchy.options.isUseHkWithDiagonalMatrix());
		cboxNoStaticCenter.setSelected(hierarchy.options.isUseHkNoStaticCenter());
		cboxGenerateImages.setSelected(hierarchy.options.isUseHkGenerateImages());
		cboxVerbose.setSelected(hierarchy.options.isUseHkVerbose());
	}

	public void setCarret() {
		txtClusters.grabFocus();
	}

	/**
	 * Launches the HK generation method.
	 * 
	 * @param window the window to attach the modal dialog to. Can null to attach
	 *               the dialog to the window the HK options panel is placed in.
	 * @return if the process had been scheduled
	 */
	public boolean generate(Window window) {
		if (!txtClusters.areValidFieldValues() || !txtIterations.areValidFieldValues()
				|| !txtRepeats.areValidFieldValues() || !txtDendrogram.areValidFieldValues()
				|| !txtMaxNodes.areValidFieldValues() || !txtEpsilon.areValidFieldValues()
				|| !txtLittleVal.areValidFieldValues()) {
			return false;
		}

		List<Integer> clustersList = txtClusters.getValues();
		List<Integer> iterationsList = txtIterations.getValues();
		List<Integer> repeatsList = txtRepeats.getValues();
		List<Integer> dendrogramHeightList = txtDendrogram.getValues();
		List<Integer> maxNodesList = txtMaxNodes.getValues();
		List<Integer> epsilonList = txtEpsilon.getValues();
		List<Integer> littleValList = txtLittleVal.getValues();

		boolean trueClassAtribute = cboxTrueClass.isSelected();
		boolean instanceNameAttribute = cboxInstanceNames.isSelected();
		boolean hkWithDiagonalMatrix = cboxDiagonalMatrix.isSelected();
		boolean hkNoStaticCenter = cboxNoStaticCenter.isSelected();
		boolean hkGenerateImages = cboxGenerateImages.isSelected();
		boolean hkVerbose = cboxVerbose.isSelected();
		Window owner = window == null ? SwingUtilities.getWindowAncestor(this) : window;
		LoadedHierarchy contextHierarchy = context.getHierarchy();

		if (clustersList.isEmpty() || iterationsList.isEmpty() || repeatsList.isEmpty()
				|| dendrogramHeightList.isEmpty() || maxNodesList.isEmpty() || epsilonList.isEmpty()
				|| littleValList.isEmpty())
			return false;

		for (int clusters : clustersList)
			for (int iterations : iterationsList)
				for (int repeats : repeatsList)
					for (int dendrogramHeight : dendrogramHeightList)
						for (int maxNodes : maxNodesList)
							for (int epsilon : epsilonList)
								for (int littleValue : littleValList) {
									logger.debug("scheadule loop");
									HKPlusPlusScheaduler.getHKPlusPlusScheaduler().addToQue(contextHierarchy, node,
											owner, trueClassAtribute, instanceNameAttribute, hkWithDiagonalMatrix,
											hkNoStaticCenter, hkGenerateImages, epsilon, littleValue, clusters,
											iterations, repeats, dendrogramHeight, maxNodes, hkVerbose);
								}
		return true;
	}

}
