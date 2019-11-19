package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.reader.GeneratedCSVReader;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.dimensionReduction.ui.elements.HelpIcon;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.LoadedHierarchyUtils;

public class DropDimensionDialog extends JDialog {

	private static final long serialVersionUID = 763612916884634445L;
	protected int maxOutputDimensions = 2;
	protected int pointsAmount = 0;
	private JButton btnOk;

	private LoadedHierarchy loadedHierarchy;
	private JList<Dimension> dimensionsToKeepList;
	private JList<Dimension> dimensionsToRemoveList;
	private List<FeatureSelectionResult> featureSelectionResults;

	private LoadedHierarchy resultHierarchy = null;

	public static void main(String[] args) throws IOException {

		Hierarchy hierarchy;
		GeneratedCSVReader CSVReader = new GeneratedCSVReader();

		hierarchy = CSVReader.load("wide.csv", false, true, false, false, true);

		LoadedHierarchy loadedHierarchy = new LoadedHierarchy(hierarchy,
				new LoadedHierarchy.Options(false, false, false, false, false));

		DropDimensionDialog dropDimension = new DropDimensionDialog(loadedHierarchy);
		dropDimension.showDialog();

	}

	public DropDimensionDialog() {
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

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 258, 49, 258 };
		gbl_panel.rowHeights = new int[] { 369 };
		gbl_panel.columnWeights = new double[] { 1.0, 0.0, 1.0 };
		gbl_panel.rowWeights = new double[] { 1.0 };
		panel.setLayout(gbl_panel);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel.add(scrollPane, gbc_scrollPane);

		dimensionsToRemoveList = new JList<Dimension>();
		dimensionsToRemoveList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		dimensionsToRemoveList.setFont(new Font("Tahoma", Font.BOLD, 20));
		scrollPane.setViewportView(dimensionsToRemoveList);

		JLabel lblNewLabel = new JLabel("Dimension to remove");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setColumnHeaderView(lblNewLabel);

		JPanel btnsTranserPane = new JPanel();
		GridBagConstraints gbc_btnsTranserPane = new GridBagConstraints();
		gbc_btnsTranserPane.fill = GridBagConstraints.BOTH;
		gbc_btnsTranserPane.insets = new Insets(0, 0, 0, 5);
		gbc_btnsTranserPane.gridx = 1;
		gbc_btnsTranserPane.gridy = 0;
		panel.add(btnsTranserPane, gbc_btnsTranserPane);
		GridBagLayout gbl_btnsTranserPane = new GridBagLayout();
		gbl_btnsTranserPane.columnWidths = new int[] { 0 };
		gbl_btnsTranserPane.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_btnsTranserPane.columnWeights = new double[] { 0.0 };
		gbl_btnsTranserPane.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
		btnsTranserPane.setLayout(gbl_btnsTranserPane);

		JButton btnNewButton = new JButton(">");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				transferSelected(dimensionsToRemoveList, dimensionsToKeepList);
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		btnsTranserPane.add(btnNewButton, gbc_btnNewButton);

		JButton btnNewButton_1 = new JButton(">>");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				transferAll(dimensionsToRemoveList, dimensionsToKeepList);
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 1;
		btnsTranserPane.add(btnNewButton_1, gbc_btnNewButton_1);

		JButton btnNewButton_2 = new JButton("<");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				transferSelected(dimensionsToKeepList, dimensionsToRemoveList);
			}
		});
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_2.gridx = 0;
		gbc_btnNewButton_2.gridy = 2;
		btnsTranserPane.add(btnNewButton_2, gbc_btnNewButton_2);

		JButton btnNewButton_3 = new JButton("<<");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				transferAll(dimensionsToKeepList, dimensionsToRemoveList);
			}
		});
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_3.gridx = 0;
		gbc_btnNewButton_3.gridy = 3;
		btnsTranserPane.add(btnNewButton_3, gbc_btnNewButton_3);

		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 2;
		gbc_scrollPane_1.gridy = 0;
		panel.add(scrollPane_1, gbc_scrollPane_1);

		dimensionsToKeepList = new JList<Dimension>();
		dimensionsToKeepList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		dimensionsToKeepList.setFont(new Font("Tahoma", Font.BOLD, 20));
		scrollPane_1.setViewportView(dimensionsToKeepList);

		JLabel lblNewLabel_1 = new JLabel("Dimension to keep");
		scrollPane_1.setColumnHeaderView(lblNewLabel_1);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		btnCancel.addActionListener(this::closeDialog);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

		this.setTitle("Remove Dimensions");
	}

	public void transferSelected(JList<Dimension> sourceList, JList<Dimension> targetList) {

		List<Dimension> sourceListData = getListData(sourceList);
		List<Dimension> targetListData = getListData(targetList);

		int[] targetIndexes = targetList.getSelectedIndices();
		int[] sourceIndexes = sourceList.getSelectedIndices();
		Arrays.sort(sourceIndexes);
		for (int i = sourceIndexes.length - 1; i >= 0; i--) {

			targetListData.add(sourceListData.remove(sourceIndexes[i]));
		}

		sourceList.setListData(sourceListData.toArray(new Dimension[0]));

		Collections.sort(targetListData);
		targetList.setListData(targetListData.toArray(new Dimension[0]));

		sourceList.setSelectedIndices(sourceIndexes);
		targetList.setSelectedIndices(targetIndexes);
	}

	public List<Dimension> getListData(JList<Dimension> dimensionsToRemoveList2) {
		List<Dimension> listData = new ArrayList<>();

		for (int i = 0; i < dimensionsToRemoveList2.getModel().getSize(); i++) {
			listData.add(dimensionsToRemoveList2.getModel().getElementAt(i));
		}

		return listData;
	}

	public void transferAll(JList<Dimension> sourceList, JList<Dimension> targetList) {
		Dimension[] dimensions;

		if (featureSelectionResults == null) {
			Hierarchy hierarchy = loadedHierarchy.getMainHierarchy();
			dimensions = new Dimension[hierarchy.getRoot().getSubtreeInstances().getFirst().getData().length];

			if (hierarchy.getDataNames() == null) {
				for (int i = 0; i < dimensions.length; i++) {
					dimensions[i] = new Dimension(i, "dimension " + (i + 1));
				}
			} else {
				for (int i = 0; i < hierarchy.getDataNames().length; i++)
					dimensions[i] = new Dimension(i, hierarchy.getDataNames()[i]);
			}
		} else {
			dimensions = new Dimension[featureSelectionResults.size()];
			for (int i = 0; i < featureSelectionResults.size(); i++) {
				dimensions[i] = new Dimension(featureSelectionResults.get(i).getRank() - 1,
						featureSelectionResults.get(i).getDimensionName());
			}
		}

		Arrays.sort(dimensions);
		targetList.setListData(dimensions);

		Dimension[] emptyArray = new Dimension[0];
		sourceList.setListData(emptyArray);

		sourceList.setSelectedIndex(0);
		targetList.setSelectedIndex(0);
	}

	public DropDimensionDialog(LoadedHierarchy loadedHierarchy, List<FeatureSelectionResult> selectionResult) {
		this();
		setHierarchy(loadedHierarchy);
		this.featureSelectionResults = selectionResult;
		transferAll(dimensionsToRemoveList, dimensionsToKeepList);
	}

	public DropDimensionDialog(LoadedHierarchy loadedHierarchy) {
		this(loadedHierarchy, null);
	}

	public void setHierarchy(LoadedHierarchy loadedHierarchy) {
		this.loadedHierarchy = loadedHierarchy;
	}

	private void closeDialog(ActionEvent e) {
		resultHierarchy = null;
		dispose();
	}

	private void confirmDialog(ActionEvent e) {
		if (validateSelection()) {
			setResult();
			dispose();
		}
	}

	private boolean validateSelection() {
		if (getListData(dimensionsToKeepList).size() >= 1) {
			return true;
		} else
			return false;
	}

	private void setResult() {
		List<Dimension> dimensionList = getListData(dimensionsToKeepList);

		List<Integer> list = new ArrayList<Integer>();

		if (featureSelectionResults != null) {
			Collections.sort(featureSelectionResults);
			for (Dimension d : dimensionList) {
				list.add(featureSelectionResults.get(d.getIndex()).getOrginalIndex());
			}
		} else {
			for (Dimension d : dimensionList) {
				list.add(d.getIndex());
			}
		}
		resultHierarchy = LoadedHierarchyUtils.removeDimensions(loadedHierarchy, list);
	}

	@Override
	public void setVisible(boolean b) {
		// super.setVisible(b);
	}

	public LoadedHierarchy showDialog() {
		resultHierarchy = null;
		pack();
		setMinimumSize(getSize());

		super.setVisible(true);
		return resultHierarchy;
	}

	private class Dimension implements Comparable<Dimension> {
		private int index;
		private String description;

		public Dimension(int index, String description) {
			this.index = index;
			this.description = description;
		}

		public int getIndex() {
			return index;
		}

		@Override
		public String toString() {
			return description;
		}

		@Override
		public int compareTo(Dimension o) {
			return index - o.getIndex();
		}
	}
}
