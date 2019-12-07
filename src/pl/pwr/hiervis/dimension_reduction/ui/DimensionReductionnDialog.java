package pl.pwr.hiervis.dimension_reduction.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
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
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimension_reduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.BooleanType;
import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.DataType;
import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.DoubleType;
import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.FunctionParameters;
import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.FunctionParametersConst;
import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.IntegerType;
import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.ListType;
import pl.pwr.hiervis.dimension_reduction.methods.core.UndefinedParamException;
import pl.pwr.hiervis.dimension_reduction.ui.elements.HelpIcon;
import pl.pwr.hiervis.dimension_reduction.ui.elements.KeyBinds;
import pl.pwr.hiervis.dimension_reduction.ui.elements.SpinnerMouseWheelChanger;
import pl.pwr.hiervis.util.ui.GridBagConstraintsBuilder;

public class DimensionReductionnDialog<T extends DimensionReductionI> extends JDialog {

	private static final String TAHOMA = "Tahoma";
	private static final long serialVersionUID = 4278239526977510130L;
	private JButton btnOk;
	private static GridBagConstraintsBuilder builder = new GridBagConstraintsBuilder();

	private int inputDimensions;
	private int pointsAmount;

	private transient T result;
	private transient T featureSelection;

	private JLabel[] parameterNames;
	private JLabel[] parameterDescriptions;
	private JComponent[] parameterValueGiver;

	public DimensionReductionnDialog(T featureSelection) {

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

		Component horizontalStrut1 = Box.createHorizontalStrut(20);
		getContentPane().add(horizontalStrut1, BorderLayout.EAST);
		btnCancel.addActionListener(this::closeDialog);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

		this.setTitle(featureSelection.getName());

	}

	private JPanel getMainPanel() throws UndefinedParamException {
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
		title.setFont(new Font(TAHOMA, Font.BOLD, 15));
		GridBagConstraints titleConst = builder.insets(0, 5, 5, 5).anchorCenter().span(2, 1).position(0, 0).build();
		mainPanel.add(title, titleConst);

		parameterNames = new JLabel[parametersNumber];
		parameterDescriptions = new JLabel[parametersNumber];
		parameterValueGiver = new JComponent[parametersNumber];

		for (int i = 0; i < parametersNumber; i++) {
			parameterNames[i] = createParameterNameLabel(functionParameters.get(i));
			parameterValueGiver[i] = createParameterValueGiver(functionParameters.get(i));
			parameterDescriptions[i] = createParameterDescriptionLabel(functionParameters.get(i));

			mainPanel.add(parameterNames[i], getLabelConst(1, 1 + rowPerParameter * i));
			mainPanel.add(parameterValueGiver[i],
					getValueConst(0, 1 + rowPerParameter * i, functionParameters.get(i).getValueClass()));
			mainPanel.add(parameterDescriptions[i], getDesConst(0, 2 + rowPerParameter * i));
		}

		for (int i = 0; i < parametersNumber; i++) {
			if (parameterValueGiver[i] instanceof JCheckBox
					&& functionParameters.get(i).getValueClass() instanceof BooleanType) {
				initSelectionListners((BooleanType) functionParameters.get(i).getValueClass(),
						(JCheckBox) parameterValueGiver[i]);
			} else if (functionParameters.get(i).getValueClass() instanceof ListType<?>) {
				initListSelectionListners(functionParameters.get(i), parameterValueGiver[i]);
			}
		}

		getContentPane().addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// not used
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// not used
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

	private GridBagConstraints getDesConst(int x, int y) {
		return builder.insets(0, 5, 5, 5).anchorWest().fill().span(2, 1).position(x, y).build();
	}

	private GridBagConstraints getLabelConst(int x, int y) {
		return builder.insets(5, 5, 0, 5).anchorWest().fill().position(x, y).build();
	}

	private GridBagConstraints getValueConst(int x, int y, DataType dataType) {
		if (dataType instanceof BooleanType) {
			return builder.insets(5, 5, 0, 5).anchorEast().position(x, y).build();
		} else if (dataType instanceof ListType<?>) {
			return builder.insets(5, 5, 0, 5).anchorEast().fill().span(2, 1).position(x, y).build();
		}
		return builder.insets(5, 5, 0, 5).anchorEast().fill().position(x, y).build();
	}

	private JLabel createParameterNameLabel(FunctionParameters functionParameters) {
		String name = functionParameters.getValueName();
		JLabel jLabel = new JLabel(name);

		if (functionParameters.getValueClass().isType(FunctionParametersConst.LIST)) {
			jLabel.setVisible(false);
		}

		return jLabel;
	}

	private JLabel createParameterDescriptionLabel(FunctionParameters functionParameters) {
		String description = functionParameters.getValueDescription();
		JLabel jLabel = new JLabel(transformDescriptionToMultiline(description));
		jLabel.setFont(new Font(TAHOMA, Font.PLAIN, 10));
		return jLabel;
	}

	private String transformDescriptionToMultiline(String description) {
		return "<html>" + description.replaceAll("\n", "<br>") + "</html>";
	}

	private JComponent createParameterValueGiver(FunctionParameters functionParameters) throws UndefinedParamException {
		DataType dataType = functionParameters.getValueClass();
		if (dataType.isType(FunctionParametersConst.DOUBLE)) {
			return createDoubleSpinner((DoubleType) dataType);
		} else if (dataType.isType(FunctionParametersConst.INTEGER)) {
			return createIntSpinner((IntegerType) dataType);
		} else if (dataType.isType(FunctionParametersConst.BOOLEAN)) {
			return createCheckBox((BooleanType) dataType);
		} else if (dataType.isType(FunctionParametersConst.LIST)) {
			return createList(functionParameters);
		}
		throw new UndefinedParamException("Undefined parameter value handling for " + dataType);
	}

	private JComponent createList(FunctionParameters functionParameters) {

		ListType<?> listType = (ListType<?>) functionParameters.getValueClass();
		JScrollPane scrollPane = new JScrollPane();

		JLabel header = new JLabel(functionParameters.getValueName());
		header.setFont(new Font(TAHOMA, Font.BOLD, 15));
		scrollPane.setColumnHeaderView(header);
		add(scrollPane, BorderLayout.CENTER);

		JList<Object> measuresList = new JList<>();
		measuresList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(measuresList);
		measuresList.setListData(listType.getValues().toArray());

		return scrollPane;
	}

	private JCheckBox createCheckBox(BooleanType booleanType) {
		JCheckBox jCheckBox = new JCheckBox();
		jCheckBox.setSelected(booleanType.getDefaultValue());

		if (booleanType.isEnabled().function(pointsAmount, inputDimensions) == 0) {
			jCheckBox.setSelected(false);
			jCheckBox.setEnabled(false);
		}

		return jCheckBox;
	}

	private void initListSelectionListners(FunctionParameters functionParameters, Component component) {

		ListType<?> l = (ListType<?>) functionParameters.getValueClass();
		JList<?> jList = (JList<?>) ((JScrollPane) component).getViewport().getComponent(0);

		jList.addListSelectionListener(e -> {
			for (Entry<?, String[]> entrySet : l.getEnableParameterNames().entrySet()) {
				enableParameters(entrySet.getValue(),
						jList.getSelectedValue().getClass() == entrySet.getKey().getClass());
			}
		});
		jList.setSelectedIndex(0);
	}

	private void initSelectionListners(BooleanType booleanType, JCheckBox jCheckBox) {
		if (booleanType.getEnableParameterNames().length > 0) {
			enableParameters(booleanType.getEnableParameterNames(), jCheckBox.isSelected());

			jCheckBox.addActionListener(
					e -> enableParameters(booleanType.getEnableParameterNames(), jCheckBox.isSelected()));
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

	private JSpinner createIntSpinner(IntegerType integerType) {
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

	private JComponent createDoubleSpinner(DoubleType doubleType) {
		double min = doubleType.getMinimumF().function((double) pointsAmount, (double) inputDimensions);
		double max = doubleType.getMaximumF().function((double) pointsAmount, (double) inputDimensions);
		double initialValue;
		if (doubleType.getDefaultValueF() != null) {
			initialValue = doubleType.getDefaultValueF().function((double) pointsAmount, (double) inputDimensions);
		} else {
			initialValue = (max - min) / 2 + min;
		}

		validateValues(min, initialValue, max);

		double stepSize = (max - min) / 50;
		return createDoubleSpinner(initialValue, min, max, stepSize);
	}

	private void validateValues(double min, double value, double max) {
		if (min > max)
			throw new IllegalArgumentException("Minimum function for: (" + pointsAmount + ", " + inputDimensions
					+ ") is bigger than maximum function.");
		if (value < min)
			throw new IllegalArgumentException("default value function for: (" + pointsAmount + ", " + inputDimensions
					+ ") is smaller than minimum function.");
		if (value > max)
			throw new IllegalArgumentException("default value function for: (" + pointsAmount + ", " + inputDimensions
					+ ") is bigger than maximum function.");
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
		return (Runtime.getRuntime().freeMemory()
				- featureSelection.getMinimumMemmory(pointsAmount, inputDimensions) > 0);
	}

	private JPanel getMemmoryWaringMessage() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(0, 0));

		JLabel lblWarning = new JLabel("<html> Not enough memory to process this Dimensionality Reduction "
				+ "<br>            Assign more memory to Java Virtual Machine"
				+ "<br>                 for example use \"-Xmx4G\" argument");

		lblWarning.setFont(new Font(TAHOMA, Font.BOLD, 16));
		lblWarning.setHorizontalAlignment(SwingConstants.CENTER);

		mainPanel.add(lblWarning, BorderLayout.CENTER);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		mainPanel.add(horizontalStrut, BorderLayout.WEST);

		Component horizontalStrut1 = Box.createHorizontalStrut(20);
		mainPanel.add(horizontalStrut1, BorderLayout.EAST);

		Component verticalStrut = Box.createVerticalStrut(20);
		mainPanel.add(verticalStrut, BorderLayout.SOUTH);

		return mainPanel;
	}

	public T showDialog(int inputDataDimensions, int pointsAmount, int x, int y) throws UndefinedParamException {
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

			AbstractAction confirm = new AbstractAction() {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					confirmDialog(e);
				}
			};
			KeyBinds.setKeybindENTER((JPanel) getContentPane(), confirm);
			KeyBinds.setKeybindSPACE((JPanel) getContentPane(), confirm);
		} else {
			getContentPane().add(getMemmoryWaringMessage(), BorderLayout.CENTER);
			btnOk.setEnabled(false);
		}

		KeyBinds.setKeybindESC((JPanel) getContentPane(), new AbstractAction() {
			private static final long serialVersionUID = 5532476186933885266L;

			@Override
			public void actionPerformed(ActionEvent e) {
				closeDialog(e);
			}
		});

		setLocation(x, y);
		pack();
		setMinimumSize(getSize());
		setVisible(true);

		return this.result;
	}

	public T showDialog(int inputDataDimensions, int pointsAmount) throws UndefinedParamException {
		return showDialog(inputDataDimensions, pointsAmount, 100, 100);
	}

	public T showDialog(Hierarchy hierarchy, int x, int y) throws UndefinedParamException {
		return showDialog(HierarchyUtils.getFirstInstance(hierarchy).getData().length,
				hierarchy.getOverallNumberOfInstances(), x, y);
	}

	public T showDialog(Hierarchy hierarchy) throws UndefinedParamException {
		return showDialog(hierarchy, 100, 100);
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
		} else if (jComponent.getClass() == JCheckBox.class) {
			return ((JCheckBox) jComponent).isSelected();
		} else if (jComponent.getClass() == JScrollPane.class) {
			JList<?> jList = (JList<?>) ((JScrollPane) jComponent).getViewport().getComponent(0);
			return jList.getSelectedValue();
		}
		return null;
	}

	private void closeDialog(@SuppressWarnings("unused") ActionEvent e) {
		result = null;
		dispose();
	}

	private void confirmDialog(@SuppressWarnings("unused") ActionEvent e) {
		setResult();
		dispose();
	}

	public T getFeatureSelection() {
		return featureSelection;
	}
}