package pl.pwr.hiervis.hk.ui;

import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.text.NumberFormatter;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;

import basic_hierarchy.interfaces.Node;
import pl.pwr.hiervis.HierarchyVisualizer;
import pl.pwr.hiervis.core.HVConfig;
import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.hk.HKPlusPlusWrapper;
import pl.pwr.hiervis.util.HierarchyUtils;
import pl.pwr.hiervis.util.ui.GridBagConstraintsBuilder;

@SuppressWarnings("serial")
public class HKOptionsPanel extends JPanel {
    private Logger logger = null;
    private HVContext context;

    private LoadedHierarchy hierarchy;
    private Node node;

    private JFormattedTextField txtClusters = null;
    private JFormattedTextField txtIterations = null;
    private JFormattedTextField txtRepeats = null;
    private JFormattedTextField txtDendrogram = null;
    private JFormattedTextField txtMaxNodes = null;
    private JFormattedTextField txtEpsilon = null;
    private JFormattedTextField txtLittleVal = null;
    private JCheckBox cboxTrueClass = null;
    private JCheckBox cboxInstanceNames = null;
    private JCheckBox cboxDiagonalMatrix = null;
    private JCheckBox cboxNoStaticCenter = null;
    private JCheckBox cboxGenerateImages = null;

    private JCheckBox cboxVerbose = null;

    private ActionListener actionListener;

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
	txtClusters = buildNumberTextField(1, Integer.MAX_VALUE);
	add(lblClusters, builder.insets(5).anchorWest().position(0, 0).build());
	add(txtClusters, builder.insets(5).anchorEast().position(1, 0).build());

	JLabel lblIterations = new JLabel("[-n] Iterations:");
	txtIterations = buildNumberTextField(1, Integer.MAX_VALUE);
	add(lblIterations, builder.insets(5).anchorWest().position(0, 1).build());
	add(txtIterations, builder.insets(5).anchorEast().position(1, 1).build());

	JLabel lblRepeats = new JLabel("[-r] Repeats:");
	txtRepeats = buildNumberTextField(1, Integer.MAX_VALUE);
	add(lblRepeats, builder.insets(5).anchorWest().position(0, 2).build());
	add(txtRepeats, builder.insets(5).anchorEast().position(1, 2).build());

	JLabel lblDendrogram = new JLabel("[-s] Max Dendrogram Height:");
	txtDendrogram = buildNumberTextField(1, Integer.MAX_VALUE);
	add(lblDendrogram, builder.insets(5).anchorWest().position(0, 3).build());
	add(txtDendrogram, builder.insets(5).anchorEast().position(1, 3).build());

	JLabel lblMaxNodes = new JLabel("[-w] Max Generated Nodes:");
	txtMaxNodes = buildNumberTextField(-1, Integer.MAX_VALUE);
	add(lblMaxNodes, builder.insets(5).anchorWest().position(0, 4).build());
	add(txtMaxNodes, builder.insets(5).anchorEast().position(1, 4).build());

	JLabel lblEpsilon = new JLabel("[-e] Epsilon:");
	txtEpsilon = buildNumberTextField(Integer.MIN_VALUE, Integer.MAX_VALUE);
	add(lblEpsilon, builder.insets(5).anchorWest().position(0, 5).build());
	add(txtEpsilon, builder.insets(5).anchorEast().position(1, 5).build());

	JLabel lblLittleVal = new JLabel("[-l] Little Value:");
	txtLittleVal = buildNumberTextField(Integer.MIN_VALUE, Integer.MAX_VALUE);
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

    public void setupDefaultValues(HVConfig cfg) {
	cboxTrueClass.setEnabled(hierarchy.options.hasTrueClassAttribute);
	cboxInstanceNames.setEnabled(hierarchy.options.hasInstanceNameAttribute);

	// txtClusters.setValue(cfg.getHkClusters());
	txtClusters.setValue(hierarchy.options.HkClusters);

	// txtIterations.setValue(cfg.getHkIterations());
	txtIterations.setValue(hierarchy.options.HkIterations);

	// txtRepeats.setValue(cfg.getHkRepetitions());
	txtRepeats.setValue(hierarchy.options.HkRepetitions);

	// txtDendrogram.setValue(cfg.getHkDendrogramHeight());
	txtDendrogram.setValue(hierarchy.options.HkDendrogramHeight);

	// txtMaxNodes.setValue(cfg.getHkMaxNodes());
	txtMaxNodes.setValue(hierarchy.options.HkMaxNodes);

	// txtEpsilon.setValue(cfg.getHkEpsilon());
	txtEpsilon.setValue(hierarchy.options.HkEpsilon);

	// txtLittleVal.setValue(cfg.getHkLittleValue());
	txtLittleVal.setValue(hierarchy.options.HkLittleValue);

	// cboxTrueClass.setSelected(cfg.isHkWithTrueClass() && cboxTrueClass.isEnabled());
	cboxTrueClass.setSelected(hierarchy.options.useTrueClassAtribute);

	// cboxInstanceNames.setSelected(cfg.isHkWithInstanceNames() &&
	// cboxInstanceNames.isEnabled());

	cboxInstanceNames.setSelected(hierarchy.options.useInstanceNameAttribute);

	// cboxDiagonalMatrix.setSelected(cfg.isHkWithDiagonalMatrix());
	cboxDiagonalMatrix.setSelected(hierarchy.options.useHkWithDiagonalMatrix);
	// cboxNoStaticCenter.setSelected(cfg.isHkNoStaticCenter());
	cboxNoStaticCenter.setSelected(hierarchy.options.useHkNoStaticCenter);
	// cboxGenerateImages.setSelected(cfg.isHkGenerateImages());
	cboxGenerateImages.setSelected(hierarchy.options.useHkGenerateImages);
	// cboxVerbose.setSelected(cfg.isHkVerbose());
	cboxVerbose.setSelected(hierarchy.options.useHkVerbose);
    }

    public void setCarret() {
	txtClusters.grabFocus();
	// .setCaretPosition(this.getText().length());
    }

    /**
     * Launches the HK generation method.
     * 
     * @param window the window to attach the modal dialog to. Can null to attach the dialog
     *               to the window the HK options panel is placed in.
     * @return the {@link HKPlusPlusWrapper} instance associated with this subprocess
     */
    public HKPlusPlusWrapper generate(Window window) {
	int clusters = Integer.parseInt(getText(txtClusters)); // -k
	int iterations = Integer.parseInt(getText(txtIterations)); // -n
	int repeats = Integer.parseInt(getText(txtRepeats)); // -r
	int dendrogramHeight = Integer.parseInt(getText(txtDendrogram)); // -s
	int maxNodes = Integer.parseInt(getText(txtMaxNodes)); // -w
	int epsilon = Integer.parseInt(getText(txtEpsilon)); // -e
	int littleVal = Integer.parseInt(getText(txtLittleVal)); // -l

	// Update HK++ config
	HVConfig cfg = context.getConfig();
	cfg.setHkClusters(clusters);
	hierarchy.options.HkClusters = clusters;
	cfg.setHkIterations(iterations);
	hierarchy.options.HkIterations = iterations;
	cfg.setHkRepetitions(repeats);
	hierarchy.options.HkRepetitions = repeats;
	cfg.setHkDendrogramHeight(dendrogramHeight);
	hierarchy.options.HkDendrogramHeight = dendrogramHeight;
	cfg.setHkMaxNodes(maxNodes);
	hierarchy.options.HkMaxNodes = maxNodes;
	cfg.setHkEpsilon(epsilon);
	hierarchy.options.HkEpsilon = epsilon;
	cfg.setHkLittleValue(littleVal);
	hierarchy.options.HkLittleValue = littleVal;
	cfg.setHkWithTrueClass(cboxTrueClass.isSelected());
	hierarchy.options.useTrueClassAtribute = cboxTrueClass.isSelected();
	cfg.setHkWithInstanceNames(cboxInstanceNames.isSelected());
	hierarchy.options.useInstanceNameAttribute = cboxInstanceNames.isSelected();
	cfg.setHkWithDiagonalMatrix(cboxDiagonalMatrix.isSelected());
	hierarchy.options.useHkWithDiagonalMatrix = cboxDiagonalMatrix.isSelected();
	cfg.setHkNoStaticCenter(cboxNoStaticCenter.isSelected());
	hierarchy.options.useHkNoStaticCenter = cboxNoStaticCenter.isSelected();
	cfg.setHkGenerateImages(cboxGenerateImages.isSelected());
	hierarchy.options.useHkGenerateImages = cboxGenerateImages.isSelected();
	cfg.setHkVerbose(cboxVerbose.isSelected());
	hierarchy.options.useHkVerbose = cboxVerbose.isSelected();

	if (maxNodes < 0) {
	    maxNodes = Integer.MAX_VALUE;
	}

	try {
	    HKPlusPlusWrapper wrapper = new HKPlusPlusWrapper(cfg);
	    wrapper.subprocessFinished.addListener(this::onSubprocessFinished);
	    wrapper.subprocessAborted.addListener(this::onSubprocessAborted);

	    logger.trace("Preparing input file...");
	    wrapper.prepareInputFile(hierarchy.getMainHierarchy(), node, cboxTrueClass.isSelected(), cboxInstanceNames.isSelected());

	    logger.trace("Starting...");
	    wrapper.start(window == null ? SwingUtilities.getWindowAncestor(this) : window, cboxTrueClass.isSelected(),
		    cboxInstanceNames.isSelected(), cboxDiagonalMatrix.isSelected(), cboxNoStaticCenter.isSelected(), cboxGenerateImages.isSelected(),
		    epsilon, littleVal, clusters, iterations, repeats, dendrogramHeight, maxNodes, cboxVerbose.isSelected());

	    return wrapper;
	}
	catch (IOException ex) {
	    logger.error(ex);
	}

	return null;
    }

    // ---------------------------------------------------------------------------------------------
    // Helper methods

    private JFormattedTextField buildNumberTextField(int min, int max) {

	NumberFormatter formatter = new IntegerFormatter(min, max);

	JFormattedTextField result = new JFormattedTextField(formatter);
	result.setHorizontalAlignment(SwingConstants.RIGHT);
	result.setColumns(10);

	result.addActionListener(this.actionListener);

	return result;
    }

    private class IntegerFormatter extends NumberFormatter {
	private int ignoresize;
	private int max;
	private int min;

	public IntegerFormatter(int min, int max) {
	    super(new DecimalFormat("0"));
	    setValueClass(Integer.class);
	    setMinimum(min);
	    setMaximum(max);
	    this.min = min;
	    this.max = max;
	    if (min < 0)
		ignoresize = 1;
	    else
		ignoresize = 0;
	}

	@Override
	public Object stringToValue(String text) throws ParseException {
	    try {
		int value = (Integer) super.stringToValue(text);
		if (text.equals("" + value)) {
		    getFormattedTextField().setValue(value);
		}
		else {
		    validationFailed("Illegal Character Inputed");
		}
		return value;
	    }
	    catch (Exception e) {
		if (text.length() > ignoresize) {
		    if (e.getMessage().equals("Value not within min/max range")) {
			validationFailed("Value must be in [" + this.min + "," + this.max + "] range");
		    }
		    else if (e.getMessage().equals("Format.parseObject(String) failed")) {
			validationFailed("Illegal Character Inputed");
		    }
		    else {
			logger.error(e.getMessage());
		    }

		    throw e;
		}
		return getFormattedTextField().getValue();
	    }
	}

	private void validationFailed(String message) {
	    showPopupMessage(getFormattedTextField(), message);
	    getFormattedTextField().setText("" + getFormattedTextField().getValue());
	}
    }

    private void showPopupMessage(JComponent component, String message) {
	try {
	    JToolTip toolTip = new JToolTip();
	    toolTip.setTipText(message);
	    int x = component.getLocationOnScreen().x + component.getWidth();
	    int y = component.getLocationOnScreen().y;

	    final Popup popup = PopupFactory.getSharedInstance().getPopup(component, toolTip, x, y);
	    Timer timer = new Timer(3000, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
		    popup.hide();
		}
	    });
	    timer.setRepeats(false);
	    timer.start();
	    popup.show();

	}
	catch (java.awt.IllegalComponentStateException e) {
	    //
	}
    }

    /**
     * Gets the text of the specified text field, or "0", if the text field is empty.
     */
    private static String getText(JTextField text) {
	String t = text.getText();
	return t == null || t.equals("") ? "0" : t;
    }

    private String getParameterString(HVConfig cfg) {
	int maxNodes = cfg.getHkMaxNodes();
	String maxNodesStr = maxNodes < 0 ? "MAX_INT" : ("" + maxNodes);

	return String.format("%s / -k %s / -n %s / -r %s / -s %s / -e %s / -l %s / -w %s%s", node.getId(), cfg.getHkClusters(), cfg.getHkIterations(),
		cfg.getHkRepetitions(), cfg.getHkDendrogramHeight(), cfg.getHkEpsilon(), cfg.getHkLittleValue(), maxNodesStr,
		cfg.isHkWithDiagonalMatrix() ? " / DM" : "");
    }

    // ---------------------------------------------------------------------------------------------
    // Listeners

    private void onSubprocessAborted(HKPlusPlusWrapper wrapper) {
	logger.trace("Aborted.");
    }

    private void onSubprocessFinished(Pair<HKPlusPlusWrapper, Integer> args) {
	HKPlusPlusWrapper wrapper = args.getKey();
	int exitCode = args.getValue();

	if (exitCode == 0) {
	    logger.trace("Finished successfully.");

	    try {
		HVConfig cfg = wrapper.getConfig();

		LoadedHierarchy outputHierarchy = wrapper.getOutputHierarchy(cfg.isHkWithTrueClass(), cfg.isHkWithInstanceNames(), false);

		LoadedHierarchy finalHierarchy = HierarchyUtils.merge(outputHierarchy, hierarchy, node.getId());
		context.loadHierarchy(getParameterString(cfg), finalHierarchy);
	    }
	    catch (Throwable ex) {
		logger.error("Subprocess finished successfully, but failed during processing: ", ex);
		ex.printStackTrace();
	    }
	}
	else {
	    logger.error("Failed! Error code: " + exitCode);
	}

	wrapper = null;
    }

    private void openInNewInstance(HVConfig cfg, LoadedHierarchy hierarchy) throws IOException {
	File tmp = File.createTempFile("hv-h-", ".tmp.csv");
	logger.trace("Saving merged hierarchy to: " + tmp.getAbsolutePath());
	HierarchyUtils.save(tmp.getAbsolutePath(), hierarchy.getMainHierarchy(), true, cfg.isHkWithTrueClass(), cfg.isHkWithInstanceNames(), true);

	// TODO: Check if the selection was an internal node or leaf node, and decide
	// where to load the new hierarchy based on that
	HierarchyVisualizer.spawnNewInstance(getParameterString(cfg), tmp, cboxTrueClass.isSelected(), cboxInstanceNames.isSelected());
    }
}
