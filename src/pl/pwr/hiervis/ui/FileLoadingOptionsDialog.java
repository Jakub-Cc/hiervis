package pl.pwr.hiervis.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.SwingUIUtils;

@SuppressWarnings("serial")
public class FileLoadingOptionsDialog extends JDialog {
	private static final int PREF_WIDTH = 350;

	private transient LoadedHierarchy.Options options = null;

	private JCheckBox cboxTrueClass;
	private JCheckBox cboxInstanceName;
	private JCheckBox cboxDataNames;
	private JCheckBox cboxFillGaps;

	public FileLoadingOptionsDialog(Window frame, LoadedHierarchy.Options initialOptions) {
		super(frame, "File Loading Options");
		setResizable(false);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { PREF_WIDTH };
		gridBagLayout.columnWeights = new double[] { 1.0 };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		createOptionsPanel(initialOptions);
		createButtonPanel();

		SwingUIUtils.installEscapeCloseOperation(this);

		pack();
	}

	private void createOptionsPanel(LoadedHierarchy.Options initialOptions) {
		JPanel cOptions = new JPanel();

		cOptions.setLayout(new BoxLayout(cOptions, BoxLayout.Y_AXIS));

		GridBagConstraints gbcCOptions = new GridBagConstraints();
		gbcCOptions.fill = GridBagConstraints.BOTH;
		gbcCOptions.insets = new Insets(5, 5, 5, 5);
		gbcCOptions.gridx = 0;
		gbcCOptions.gridy = 0;
		getContentPane().add(cOptions, gbcCOptions);

		cboxTrueClass = new JCheckBox("True Class");
		cOptions.add(cboxTrueClass);

		JLabel lblTrueClass = new JLabel(
				"<html>Whether the input file contains a column specifying ground-truth assignment of each instance.</html>");
		fixedWidthLabel(lblTrueClass, PREF_WIDTH);
		cOptions.add(lblTrueClass);
		cOptions.add(Box.createVerticalStrut(10));

		cboxInstanceName = new JCheckBox("Instance Name");
		cOptions.add(cboxInstanceName);

		JLabel lblInstanceName = new JLabel(
				"<html>Whether the input file contains a column specifying name of each instance.</html>");
		fixedWidthLabel(lblInstanceName, PREF_WIDTH);
		cOptions.add(lblInstanceName);
		cOptions.add(Box.createVerticalStrut(10));

		cboxDataNames = new JCheckBox("Column Header");
		cOptions.add(cboxDataNames);

		JLabel lblDataNames = new JLabel("<html>Whether the first row of the input file is a column header.</html>");
		fixedWidthLabel(lblDataNames, PREF_WIDTH);
		cOptions.add(lblDataNames);
		cOptions.add(Box.createVerticalStrut(10));

		cboxFillGaps = new JCheckBox("Fill Breadth Gaps");
		cOptions.add(cboxFillGaps);

		JLabel lblFillGaps = new JLabel("<html>Whether the hierarchy constructed from the input file should be "
				+ "fixed to account for missing group siblings.</html>");
		fixedWidthLabel(lblFillGaps, PREF_WIDTH);
		cOptions.add(lblFillGaps);
		cOptions.add(Box.createVerticalStrut(10));

		// Apply initial options
		cboxTrueClass.setSelected(initialOptions.hasTrueClassAttribute);
		cboxInstanceName.setSelected(initialOptions.hasInstanceNameAttribute);
		cboxDataNames.setSelected(initialOptions.hasColumnHeader);
		cboxFillGaps.setSelected(initialOptions.isFillBreadthGaps);
	}

	private void createButtonPanel() {
		JPanel cButtons = new JPanel();
		GridBagConstraints gbcCButtons = new GridBagConstraints();
		gbcCButtons.fill = GridBagConstraints.BOTH;
		gbcCButtons.gridx = 0;
		gbcCButtons.gridy = 1;
		getContentPane().add(cButtons, gbcCButtons);

		GridBagLayout gblCButtons = new GridBagLayout();
		gblCButtons.columnWidths = new int[] { 0, 0 };
		gblCButtons.rowHeights = new int[] { 0, 0 };
		gblCButtons.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gblCButtons.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		cButtons.setLayout(gblCButtons);

		JButton btnConfirm = new JButton("Confirm");
		GridBagConstraints gbcBtnConfirm = new GridBagConstraints();
		gbcBtnConfirm.insets = new Insets(5, 0, 5, 0);
		gbcBtnConfirm.gridx = 0;
		gbcBtnConfirm.gridy = 0;
		cButtons.add(btnConfirm, gbcBtnConfirm);

		getRootPane().setDefaultButton(btnConfirm);
		btnConfirm.requestFocus();

		btnConfirm.addActionListener(e -> {
			updateOptions();
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		});
	}

	private static void fixedWidthLabel(JLabel lbl, int width) {
		Dimension d = lbl.getPreferredSize();
		lbl.setPreferredSize(new Dimension(width, (1 + d.width / width) * d.height));
	}

	private void updateOptions() {
		options = new LoadedHierarchy.Options(cboxInstanceName.isSelected(), cboxTrueClass.isSelected(),
				cboxDataNames.isSelected(), cboxFillGaps.isSelected(), false);
	}

	/**
	 * @return the new options instance, if the user exited the dialog by pressing
	 *         the 'Confirm' button. Null otherwise.
	 */
	public LoadedHierarchy.Options getOptions() {
		return options;
	}
}
