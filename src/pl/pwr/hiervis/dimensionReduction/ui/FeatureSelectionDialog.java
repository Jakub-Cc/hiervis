package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.methods.MultidimensionalScaling;
import pl.pwr.hiervis.dimensionReduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters.BooleanType;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters.DataType;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters.DoubleType;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters.IntegerType;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters.ListType;
import pl.pwr.hiervis.dimensionReduction.ui.elements.HelpIcon;
import pl.pwr.hiervis.dimensionReduction.ui.elements.SpinnerMouseWheelChanger;
import pl.pwr.hiervis.util.HierarchyUtils;
import pl.pwr.hiervis.util.ui.GridBagConstraintsBuilder;

public class FeatureSelectionDialog<T extends DimensionReductionI> extends JDialog {

    public static void main(String[] args) throws Exception {
	// FeatureSelectionDialog<FeatureExtraction> f = new FeatureSelectionDialog<>(new
	// StarCoordinates());
	// FeatureSelectionDialog<FeatureExtraction> f = new FeatureSelectionDialog<>(new Tsne());
	// FeatureSelectionDialog<FeatureSelection> f = new FeatureSelectionDialog<>(new
	// InfiniteFS());
	FeatureSelectionDialog<FeatureExtraction> f = new FeatureSelectionDialog<>(new MultidimensionalScaling());

	System.out.println(f.showDialog(8, 100));
    }

    /**
     * 
     */
    private static final long serialVersionUID = 4278239526977510130L;
    private JButton btnOk;
    private static GridBagConstraintsBuilder builder = new GridBagConstraintsBuilder();

    private int inputDimensions;
    private int pointsAmount;

    private T result;
    private T featureSelection;

    private JLabel parameterNames[];
    private JLabel parameterDescriptions[];
    private JComponent parameterValueGiver[];

    public FeatureSelectionDialog(T featureSelection) {

	this.featureSelection = featureSelection;

	createDialog();
    }

    private void createDialog() {
	getContentPane().setLayout(new BorderLayout(0, 0));

	JPanel buttonPanel = new JPanel();
	getContentPane().add(buttonPanel, BorderLayout.SOUTH);

	btnOk = new JButton("OK");
	buttonPanel.add(btnOk);
	btnOk.setActionCommand("OK");
	btnOk.addActionListener(this::confirmDialog);
	getRootPane().setDefaultButton(btnOk);

	JButton btnCancel = new JButton("Cancel");
	buttonPanel.add(btnCancel);
	btnCancel.setActionCommand("Cancel");

	JLabel lblHelpicon = new HelpIcon();
	lblHelpicon.setHorizontalAlignment(SwingConstants.RIGHT);
	getContentPane().add(lblHelpicon, BorderLayout.NORTH);

	Component horizontalStrut = Box.createHorizontalStrut(20);
	getContentPane().add(horizontalStrut, BorderLayout.WEST);

	Component horizontalStrut_1 = Box.createHorizontalStrut(20);
	getContentPane().add(horizontalStrut_1, BorderLayout.EAST);
	btnCancel.addActionListener(this::closeDialog);

	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

	this.setTitle(featureSelection.getName());

    }

    private JPanel getMainPanel() throws Exception {
	List<FunctionParameters> functionParameters = featureSelection.getParameters();
	int parametersNumber = functionParameters.size();

	JPanel mainPanel = new JPanel();

	GridBagLayout layout = new GridBagLayout();
	layout.columnWidths = new int[] { 0, 0 };
	layout.columnWeights = new double[] { 0.0, 1.0 };

	int rowPerParameter = 2;
	layout.rowHeights = new int[rowPerParameter * parametersNumber + 2];
	layout.rowWeights = new double[rowPerParameter * parametersNumber + 2];
	for (int i = 0; i < rowPerParameter * parametersNumber + 2; i++) {
	    layout.rowHeights[i] = 0;
	    layout.rowWeights[i] = 0.0;
	}
	layout.rowWeights[rowPerParameter * parametersNumber + 1] = 1.0;

	mainPanel.setLayout(layout);

	JLabel title = new JLabel("Do you want to perform " + featureSelection.getName() + "?");
	title.setFont(new Font("Tahoma", Font.BOLD, 15));
	GridBagConstraints titleConst = builder.insets(0, 5, 5, 5).anchorCenter().span(2, 1).position(0, 0).build();
	mainPanel.add(title, titleConst);

	parameterNames = new JLabel[parametersNumber];
	parameterDescriptions = new JLabel[parametersNumber];
	parameterValueGiver = new JComponent[parametersNumber];

	for (int i = 0; i < parametersNumber; i++) {
	    parameterNames[i] = createParameterNameLabel(functionParameters.get(i));
	    parameterValueGiver[i] = createParameterValueGiver(functionParameters.get(i));
	    parameterDescriptions[i] = createParameterDescriptionLabel(functionParameters.get(i));

	    mainPanel.add(parameterNames[i], getLabelConst(1, 1 + rowPerParameter * i, functionParameters.get(i).getValueClass()));
	    mainPanel.add(parameterValueGiver[i], getValueConst(0, 1 + rowPerParameter * i, functionParameters.get(i).getValueClass()));
	    mainPanel.add(parameterDescriptions[i], getDesConst(0, 2 + rowPerParameter * i, functionParameters.get(i).getValueClass()));
	}

	for (int i = 0; i < parametersNumber; i++) {
	    if (parameterValueGiver[i] instanceof JCheckBox && functionParameters.get(i).getValueClass() instanceof BooleanType) {
		initSelectionListners((BooleanType) functionParameters.get(i).getValueClass(), (JCheckBox) parameterValueGiver[i]);
	    }
	    else if (functionParameters.get(i).getValueClass() instanceof ListType<?>) {
		initListSelectionListners(functionParameters.get(i), parameterValueGiver[i]);
	    }
	}

	getContentPane().addKeyListener(new KeyListener() {
	    @Override
	    public void keyTyped(KeyEvent e) {
	    }

	    @Override
	    public void keyReleased(KeyEvent e) {
	    }

	    // To maintain focus in window when ALT is pressed
	    @Override
	    public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 18)
		    e.consume();
	    }
	});
	return mainPanel;
    }

    private GridBagConstraints getDesConst(int x, int y, DataType dataType) {
	return builder.insets(0, 5, 5, 5).anchorWest().fill().span(2, 1).position(x, y).build();
    }

    private GridBagConstraints getLabelConst(int x, int y, DataType dataType) {
	return builder.insets(5, 5, 0, 5).anchorWest().fill().position(x, y).build();
    }

    private GridBagConstraints getValueConst(int x, int y, DataType dataType) {
	if (dataType instanceof BooleanType) {
	    return builder.insets(5, 5, 0, 5).anchorEast().position(x, y).build();
	}
	else if (dataType instanceof ListType<?>) {
	    return builder.insets(5, 5, 0, 5).anchorEast().fill().span(2, 1).position(x, y).build();
	}
	else
	    return builder.insets(5, 5, 0, 5).anchorEast().fill().position(x, y).build();
    }

    private JLabel createParameterNameLabel(FunctionParameters functionParameters) {
	String name = functionParameters.getValueName();
	JLabel jLabel = new JLabel(name);

	if (functionParameters.getValueClass().isType(FunctionParameters.LIST)) {
	    jLabel.setVisible(false);
	}

	return jLabel;
    }

    private JLabel createParameterDescriptionLabel(FunctionParameters functionParameters) {
	String description = functionParameters.getValueDescription();
	JLabel jLabel = new JLabel(transformDescriptionToMultiline(description));
	jLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
	return jLabel;
    }

    private String transformDescriptionToMultiline(String description) {
	return "<html>" + description.replaceAll("\n", "<br>") + "</html>";
    }

    private JComponent createParameterValueGiver(FunctionParameters functionParameters) throws Exception {
	DataType dataType = functionParameters.getValueClass();
	if (dataType.isType(FunctionParameters.DOUBLE)) {
	    return createDoubleSpinner((DoubleType) dataType);
	}
	else if (dataType.isType(FunctionParameters.INTEGER)) {
	    return createIntSpinner((IntegerType) dataType);
	}
	else if (dataType.isType(FunctionParameters.BOOLEAN)) {
	    return createCheckBox((BooleanType) dataType);
	}
	else if (dataType.isType(FunctionParameters.LIST)) {
	    return createList(functionParameters);
	}
	else
	    throw new Exception("Undefined parameter value handling for " + dataType);
    }

    private JComponent createList(FunctionParameters functionParameters) {

	ListType<?> listType = (ListType<?>) functionParameters.getValueClass();
	JScrollPane scrollPane = new JScrollPane();

	JLabel header = new JLabel(functionParameters.getValueName());
	header.setFont(new Font("Tahoma", Font.BOLD, 15));
	scrollPane.setColumnHeaderView(header);
	add(scrollPane, BorderLayout.CENTER);

	JList<Object> measuresList = new JList<Object>();
	measuresList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	scrollPane.setViewportView(measuresList);
	// measuresList.setFont(new Font("Tahoma", Font.BOLD, 20));
	measuresList.setListData(listType.getValues().toArray());

	return scrollPane;
    }

    private JCheckBox createCheckBox(BooleanType booleanType) {
	JCheckBox jCheckBox = new JCheckBox();
	jCheckBox.setSelected(booleanType.getDefaultValue());
	return jCheckBox;
    }

    private void initListSelectionListners(FunctionParameters functionParameters, Component component) {

	ListType<?> l = (ListType<?>) functionParameters.getValueClass();
	JList<?> jList = (JList<?>) ((JScrollPane) component).getViewport().getComponent(0);

	jList.addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e) {
		for (Entry<?, String[]> entrySet : l.getEnableParameterNames().entrySet()) {
		    if (jList.getSelectedValue().getClass() == entrySet.getKey().getClass()) {

			enableParameters(entrySet.getValue(), true);
		    }
		    else
			enableParameters(entrySet.getValue(), false);
		}
	    }
	});

	jList.setSelectedIndex(0);
    }

    private void initSelectionListners(BooleanType booleanType, JCheckBox jCheckBox) {
	if (booleanType.getEnableParameterNames().length > 0) {
	    enableParameters(booleanType.getEnableParameterNames(), jCheckBox.isSelected());

	    jCheckBox.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    enableParameters(booleanType.getEnableParameterNames(), jCheckBox.isSelected());
		}
	    });
	}
    }

    private void enableParameters(String[] names, boolean enable) {
	for (String parameterName : names) {
	    int index = getIndexBasedOnName(parameterName);
	    enableParameter(index, enable);
	}
    }

    private void enableParameter(int index, boolean enable) {
	if (index >= 0 && index < parameterNames.length) {
	    parameterDescriptions[index].setEnabled(enable);
	    parameterNames[index].setEnabled(enable);
	    parameterValueGiver[index].setEnabled(enable);
	}
    }

    /**
     * 
     * @param name
     * @return index of first found parameter with given name
     */
    private int getIndexBasedOnName(String name) {
	for (int i = 0; i < parameterNames.length; i++) {
	    if (name.equals(parameterNames[i].getText()))
		return i;
	}
	return -1;
    }

    private JSpinner createIntSpinner(IntegerType integerType) throws Exception {
	int min = integerType.getMinimumF().function(pointsAmount, inputDimensions);
	int max = integerType.getMaximumF().function(pointsAmount, inputDimensions);

	int initialValue;
	if (integerType.getDefaultValueF() != null)
	    initialValue = integerType.getDefaultValueF().function(pointsAmount, inputDimensions);
	else
	    initialValue = (max - min) / 2 + min;

	validateValues(min, initialValue, max);

	int stepSize = Math.max((max - min) / 50, 1);
	return createIntSpinner(initialValue, min, max, stepSize);
    }

    private JSpinner createIntSpinner(int value, int minimum, int maximum, int stepSize) {
	JSpinner spinner = new JSpinner();
	spinner.setModel(new SpinnerNumberModel(value, minimum, maximum, stepSize));
	setSpinnerParameters(spinner, 4);
	return spinner;
    }

    private JComponent createDoubleSpinner(DoubleType doubleType) throws Exception {
	double min = doubleType.getMinimumF().function(new Double(pointsAmount), new Double(inputDimensions));
	double max = doubleType.getMaximumF().function(new Double(pointsAmount), new Double(inputDimensions));
	double initialValue;
	if (doubleType.getDefaultValueF() != null) {
	    initialValue = doubleType.getDefaultValueF().function(new Double(pointsAmount), new Double(inputDimensions));
	}
	else
	    initialValue = (max - min) / 2 + min;

	validateValues(min, initialValue, max);

	double stepSize = (max - min) / 50;
	return createDoubleSpinner(initialValue, min, max, stepSize);
    }

    private void validateValues(double min, double value, double max) throws Exception {
	if (min > max)
	    throw new Exception("Minimum function for: (" + pointsAmount + ", " + inputDimensions + ") is bigger than maximum function.");
	if (value < min)
	    throw new Exception("default value function for: (" + pointsAmount + ", " + inputDimensions + ") is smaller than minimum function.");
	if (value > max)
	    throw new Exception("default value function for: (" + pointsAmount + ", " + inputDimensions + ") is bigger than maximum function.");
    }

    private JSpinner createDoubleSpinner(double value, double minimum, double maximum, double stepSize) {
	JSpinner spinner = new JSpinner();
	spinner.setModel(new SpinnerNumberModel(value, minimum, maximum, stepSize));
	setSpinnerParameters(spinner, 4);
	return spinner;
    }

    private JSpinner setSpinnerParameters(JSpinner jSpinner, int columns) {
	jSpinner.addMouseWheelListener(new SpinnerMouseWheelChanger());
	JComponent edditor = jSpinner.getEditor();
	JFormattedTextField jftf = ((JSpinner.DefaultEditor) edditor).getTextField();
	jftf.setColumns(columns);
	return jSpinner;
    }

    private boolean isEnoughMemoryAvaliable() {
	return (Runtime.getRuntime().freeMemory() - featureSelection.getMinimumMemmory(pointsAmount, inputDimensions) > 0);
    }

    private JPanel getMemmoryWaringMessage() {
	JPanel mainPanel = new JPanel();
	mainPanel.setLayout(new BorderLayout(0, 0));

	JLabel lblWarning = new JLabel("<html> Not enough memory to process this Dimensionality Reduction "
		+ "<br>            Assign more memory to Java Virtual Machine" + "<br>                 for example use \"-Xmx4G\" argument");

	lblWarning.setFont(new Font("Tahoma", Font.BOLD, 16));
	lblWarning.setHorizontalAlignment(SwingConstants.CENTER);

	mainPanel.add(lblWarning, BorderLayout.CENTER);

	Component horizontalStrut = Box.createHorizontalStrut(20);
	mainPanel.add(horizontalStrut, BorderLayout.WEST);

	Component horizontalStrut_1 = Box.createHorizontalStrut(20);
	mainPanel.add(horizontalStrut_1, BorderLayout.EAST);

	Component verticalStrut = Box.createVerticalStrut(20);
	mainPanel.add(verticalStrut, BorderLayout.SOUTH);

	return mainPanel;
    };

    public T showDialog(int inputDataDimensions, int pointsAmount, int x, int y) throws Exception {
	this.inputDimensions = inputDataDimensions;
	this.pointsAmount = pointsAmount;
	this.result = null;

	this.addWindowListener(new java.awt.event.WindowAdapter() {
	    @Override
	    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		result = null;
	    }
	});

	if (isEnoughMemoryAvaliable()) {
	    getContentPane().add(getMainPanel(), BorderLayout.CENTER);
	    setKeybindConfirm((JPanel) getContentPane());
	}
	else {
	    getContentPane().add(getMemmoryWaringMessage(), BorderLayout.CENTER);
	    btnOk.setEnabled(false);
	}

	setKeybindCancel((JPanel) getContentPane());

	setLocation(x, y);
	pack();
	setMinimumSize(getSize());
	setVisible(true);

	return this.result;
    }

    public T showDialog(int inputDataDimensions, int pointsAmount) throws Exception {
	return showDialog(inputDataDimensions, pointsAmount, 100, 100);
    }

    public T showDialog(Hierarchy hierarchy) throws Exception {
	return showDialog(HierarchyUtils.getFirstInstance(hierarchy).getData().length, hierarchy.getOverallNumberOfInstances(), 100, 100);
    }

    public T showDialog(Hierarchy hierarchy, int x, int y) throws Exception {
	return showDialog(HierarchyUtils.getFirstInstance(hierarchy).getData().length, hierarchy.getOverallNumberOfInstances(), x, y);
    }

    @SuppressWarnings("unchecked")
    private void setResult() {

	List<FunctionParameters> functionParameters = featureSelection.getParameters();
	int parametersNumber = functionParameters.size();

	Map<String, Object> map = new HashMap<>();
	for (int i = 0; i < parametersNumber; i++) {
	    String key = parameterNames[i].getText();
	    Object value = getValue(parameterValueGiver[i]);
	    map.put(key, value);
	}
	result = (T) featureSelection.createInstance(map);
    }

    private static Object getValue(JComponent jComponent) {
	if (jComponent.getClass() == JSpinner.class) {
	    return ((JSpinner) jComponent).getValue();
	}
	else if (jComponent.getClass() == JCheckBox.class) {
	    return ((JCheckBox) jComponent).isSelected();
	}
	else if (jComponent.getClass() == JScrollPane.class) {
	    JList<?> jList = (JList<?>) ((JScrollPane) jComponent).getViewport().getComponent(0);
	    return jList.getSelectedValue();
	}
	else
	    return null;
    }

    private void setKeybindCancel(JPanel contentPanel) {
	// escape button to cancel dialog
	contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC");
	contentPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC");
	contentPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC");

	contentPanel.getActionMap().put("ESC", new AbstractAction() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		closeDialog(e);
	    }
	});
    }

    private void setKeybindConfirm(JPanel contentPanel) {
	// press "enter" or "space" to confirm dialog
	contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");
	contentPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");
	contentPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");

	contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "ENTER");
	contentPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "ENTER");
	contentPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "ENTER");
	contentPanel.getActionMap().put("ENTER", new AbstractAction() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void actionPerformed(ActionEvent e) {
		confirmDialog(e);
	    }
	});
    }

    private void closeDialog(ActionEvent e) {
	result = null;
	dispose();
    }

    private void confirmDialog(ActionEvent e) {
	setResult();
	dispose();
    }
}