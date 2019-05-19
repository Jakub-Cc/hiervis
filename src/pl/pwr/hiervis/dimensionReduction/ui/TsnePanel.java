package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import pl.pwr.hiervis.dimensionReduction.ui.elements.SpinnerMouseWheelChanger;
import pl.pwr.hiervis.util.ui.GridBagConstraintsBuilder;

public class TsnePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 9017670399493121380L;

    public static final String MaxIterations = "MaxIterations";
    public static final String Perplexity = "Perplexity";
    public static final String Theta = "Theta";
    public static final String ParallerCalculations = "ParallerCalculations";
    public static final String UsePca = "UsePca";
    public static final String PcaDimensions = "PcaDimensions";

    private static GridBagConstraintsBuilder builder = new GridBagConstraintsBuilder();

    private JSpinner maxIterations_Spinner;
    private JSpinner pca_spinner;
    private JSpinner perplexity_Spinner;

    private JLabel pca_Lbl;

    private JLabel pca_Desc;

    private JSpinner theta_spinner;

    private JCheckBox paraller_checkbox;

    private JCheckBox usePca_checkbox;

    public TsnePanel() {

	GridBagLayout layout = new GridBagLayout();
	layout.columnWidths = new int[] { 0, 0 };
	layout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	layout.columnWeights = new double[] { 0.0, 1.0 };
	layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
	setLayout(layout);

	createMaxIterationElements();

	createPerplexityElements();

	createThetaElements();

	createParallerElements();

	createUsePcaElements();

	createPcaElements(2);

    }

    private GridBagConstraints getDesConst(int x, int y) {
	return builder.insets(0, 5, 5, 5).anchorWest().fill().span(2, 1).position(x, y).build();
    }

    private GridBagConstraints getLabelConst(int x, int y) {
	return builder.insets(5, 5, 0, 5).anchorWest().fill().position(x, y).build();
    }

    private GridBagConstraints getValueConst(int x, int y) {
	return builder.insets(5, 5, 0, 5).anchorEast().fill().position(x, y).build();
    }

    private void createMaxIterationElements() {
	JLabel maxIterations_Lbl = new JLabel("Max iterations");
	JLabel maxIterations_Description = new DecriptionLabel(
		"<html>Positive integer specifying the maximum number of optimization iterations.");
	maxIterations_Spinner = createSpinner(1000, 100, 10000, 100);
	add(maxIterations_Lbl, getLabelConst(1, 0));
	add(maxIterations_Spinner, getValueConst(0, 0));
	add(maxIterations_Description, getDesConst(0, 1));
    }

    private void createPerplexityElements() {
	JLabel perplexity_Lbl = new JLabel("Perplexity");
	JLabel perplexity_Description = new DecriptionLabel(
		"<html>The perplexity is related to the number of nearest neighbors that is used in other manifold learning algorithms."
			+ "<br> Larger datasets usually require a larger perplexity. Consider selecting a value between 5 and 50. "
			+ "<br>The choice is not extremely critical since t-SNE is quite insensitive to this parameter.");
	perplexity_Spinner = createSpinner(20.0, 5.0, 50, 5.0);
	add(perplexity_Lbl, getLabelConst(1, 2));
	add(perplexity_Description, getDesConst(0, 3));
	add(perplexity_Spinner, getValueConst(0, 2));
    }

    private void createThetaElements() {
	JLabel theta_Lbl = new JLabel("Theta");
	JLabel theta_Description = new DecriptionLabel(
		"<html>Barnes-Hut tradeoff parameter, specified as a scalar from 0 through 1."
			+ "<br> Highervalues give a faster but less accurate optimization.");
	theta_spinner = createSpinner(0.5, 0.0, 1.0, 0.05);

	add(theta_Lbl, getLabelConst(1, 4));
	add(theta_Description, getDesConst(0, 5));
	add(theta_spinner, getValueConst(0, 4));
    }

    private void createParallerElements() {
	JLabel paraller_Lbl = new JLabel("Paraller calculations");
	JLabel paraller_Description = new DecriptionLabel("<html>Perform parraller calculations using multithreading.");
	paraller_checkbox = new JCheckBox();
	paraller_checkbox.setSelected(true);
	add(paraller_Lbl, getLabelConst(1, 6));
	add(paraller_Description, getDesConst(0, 7));
	add(paraller_checkbox, builder.insets(5).anchorEast().position(0, 6).build());
    }

    private void createUsePcaElements() {
	JLabel usePca_Lbl = new JLabel("Use PCA for pre reduction");
	JLabel usePca_Desc = new DecriptionLabel("<html>Using PCA for predimension reduction before t-SNE.");
	usePca_checkbox = new JCheckBox();
	add(usePca_Lbl, getLabelConst(1, 8));
	add(usePca_Desc, getDesConst(0, 9));
	add(usePca_checkbox, builder.insets(5).anchorEast().position(0, 8).build());

	usePca_checkbox.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		enablePcaElement(usePca_checkbox.isSelected());
	    }
	});
    }

    private void createPcaElements(int maxOutputDimensions) {
	pca_Lbl = new JLabel("PCA initial dimensions");
	pca_Desc = new DecriptionLabel("<html>Number of initial dimension for predimension reduction.");
	pca_spinner = createSpinner(Math.max(2, maxOutputDimensions / 2), 2, maxOutputDimensions, 1);
	add(pca_Lbl, getLabelConst(1, 10));
	add(pca_Desc, builder.insets(0, 5, 20, 5).anchorWest().fill().span(2, 1).position(0, 11).build());
	add(pca_spinner, getValueConst(0, 10));
	enablePcaElement(false);
    }

    private void enablePcaElement(boolean enabled) {
	pca_Lbl.setEnabled(enabled);
	pca_Desc.setEnabled(enabled);
	pca_spinner.setEnabled(enabled);
    }

    class DecriptionLabel extends JLabel {
	private static final long serialVersionUID = 3128095669394990594L;

	public DecriptionLabel(String description) {
	    super(description);
	    setFont(new Font("Tahoma", Font.PLAIN, 10));
	}
    }

    private JSpinner createSpinner(int value, int minimum, int maximum, int stepSize) {
	JSpinner spinner = new JSpinner();
	spinner.setModel(new SpinnerNumberModel(value, minimum, maximum, stepSize));
	spinner.addMouseWheelListener(new SpinnerMouseWheelChanger());
	return spinner;
    }

    private JSpinner createSpinner(double value, double minimum, double maximum, double stepSize) {
	JSpinner spinner = new JSpinner();
	spinner.setModel(new SpinnerNumberModel(value, minimum, maximum, stepSize));
	spinner.addMouseWheelListener(new SpinnerMouseWheelChanger());
	return spinner;
    }

    public Map<String, Object> getValues() {
	HashMap<String, Object> results = new HashMap<String, Object>();
	results.put(MaxIterations, maxIterations_Spinner.getValue());
	results.put(Perplexity, perplexity_Spinner.getValue());
	results.put(Theta, theta_spinner.getValue());
	results.put(ParallerCalculations, paraller_checkbox.isSelected());
	results.put(UsePca, usePca_checkbox.isSelected());
	results.put(PcaDimensions, pca_spinner.getValue());

	return results;
    }

    public void remodel(int maxOutputDimensions, int pointsAmount) {
	pca_spinner.setModel(new SpinnerNumberModel(2, 2, maxOutputDimensions, 1));
	double min = 0.5;
	double max = Math.round((pointsAmount - 1) / 3);
	double value = Math.max(min, Math.min(20.0, max));
	double stepSize = Math.max(1, Math.round((max - min) / 30));
	perplexity_Spinner.setModel(new SpinnerNumberModel(value, min, max, stepSize));
    }
}
