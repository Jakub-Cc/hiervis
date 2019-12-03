package pl.pwr.hiervis.dimension_reduction.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
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
import javax.swing.WindowConstants;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.reader.GeneratedCSVReader;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.dimension_reduction.ui.elements.HelpIcon;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.LoadedHierarchyUtils;

public class DropDimensionDialog extends JDialog {

	private static final long serialVersionUID = 763612916884634445L;
	protected int maxOutputDimensions = 2;
	protected int pointsAmount = 0;
	private transient List<FeatureSelectionResult> featureSelectionResults;
	private transient LoadedHierarchy loadedHierarchy;
	private transient LoadedHierarchy resultHierarchy = null;
	private JButton btnOk;
	private JList<Dimension> dimensionsToKeepList;
	private JList<Dimension> dimensionsToRemoveList;

	public static void main(String[] args) throws IOException {

		Hierarchy hierarchy;
		GeneratedCSVReader csvReader = new GeneratedCSVReader();

		hierarchy = csvReader.load("wide.csv", false, true, false, false, true);

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

		Component horizontalStrut1 = Box.createHorizontalStrut(20);
		getContentPane().add(horizontalStrut1, BorderLayout.EAST);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gblPanel = new GridBagLayout();
		gblPanel.columnWidths = new int[] { 258, 49, 258 };
		gblPanel.rowHeights = new int[] { 369 };
		gblPanel.columnWeights = new double[] { 1.0, 0.0, 1.0 };
		gblPanel.rowWeights = new double[] { 1.0 };
		panel.setLayout(gblPanel);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.insets = new Insets(0, 0, 0, 5);
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 0;
		panel.add(scrollPane, gbcScrollPane);

		dimensionsToRemoveList = new JList<>();
		dimensionsToRemoveList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		dimensionsToRemoveList.setFont(new Font("Tahoma", Font.BOLD, 20));
		scrollPane.setViewportView(dimensionsToRemoveList);

		JLabel lblNewLabel = new JLabel("Dimension to remove");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setColumnHeaderView(lblNewLabel);

		JPanel btnsTranserPane = new JPanel();
		GridBagConstraints gbcBtnsTranserPane = new GridBagConstraints();
		gbcBtnsTranserPane.fill = GridBagConstraints.BOTH;
		gbcBtnsTranserPane.insets = new Insets(0, 0, 0, 5);
		gbcBtnsTranserPane.gridx = 1;
		gbcBtnsTranserPane.gridy = 0;
		panel.add(btnsTranserPane, gbcBtnsTranserPane);
		GridBagLayout gblBtnsTranserPane = new GridBagLayout();
		gblBtnsTranserPane.columnWidths = new int[] { 0 };
		gblBtnsTranserPane.rowHeights = new int[] { 0, 0, 0, 0 };
		gblBtnsTranserPane.columnWeights = new double[] { 0.0 };
		gblBtnsTranserPane.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
		btnsTranserPane.setLayout(gblBtnsTranserPane);

		JButton btnNewButton = new JButton(">");
		btnNewButton.addActionListener(e -> transferSelected(dimensionsToRemoveList, dimensionsToKeepList));
		GridBagConstraints gbcBtnNewButton = new GridBagConstraints();
		gbcBtnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnNewButton.insets = new Insets(0, 0, 5, 0);
		gbcBtnNewButton.gridx = 0;
		gbcBtnNewButton.gridy = 0;
		btnsTranserPane.add(btnNewButton, gbcBtnNewButton);

		JButton btnNewButton1 = new JButton(">>");
		btnNewButton1.addActionListener(e -> transferAll(dimensionsToRemoveList, dimensionsToKeepList));
		GridBagConstraints gbcBtnNewButton1 = new GridBagConstraints();
		gbcBtnNewButton1.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnNewButton1.insets = new Insets(0, 0, 5, 0);
		gbcBtnNewButton1.gridx = 0;
		gbcBtnNewButton1.gridy = 1;
		btnsTranserPane.add(btnNewButton1, gbcBtnNewButton1);

		JButton btnNewButton2 = new JButton("<");
		btnNewButton2.addActionListener(e -> transferSelected(dimensionsToKeepList, dimensionsToRemoveList));
		GridBagConstraints gbcbtnNewButton2 = new GridBagConstraints();
		gbcbtnNewButton2.fill = GridBagConstraints.HORIZONTAL;
		gbcbtnNewButton2.insets = new Insets(0, 0, 5, 0);
		gbcbtnNewButton2.gridx = 0;
		gbcbtnNewButton2.gridy = 2;
		btnsTranserPane.add(btnNewButton2, gbcbtnNewButton2);

		JButton btnNewButton3 = new JButton("<<");
		btnNewButton3.addActionListener(e -> transferAll(dimensionsToKeepList, dimensionsToRemoveList));
		GridBagConstraints gbcBtnNewButton3 = new GridBagConstraints();
		gbcBtnNewButton3.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnNewButton3.gridx = 0;
		gbcBtnNewButton3.gridy = 3;
		btnsTranserPane.add(btnNewButton3, gbcBtnNewButton3);

		JScrollPane scrollPane1 = new JScrollPane();
		GridBagConstraints gbcScrollPane1 = new GridBagConstraints();
		gbcScrollPane1.fill = GridBagConstraints.BOTH;
		gbcScrollPane1.gridx = 2;
		gbcScrollPane1.gridy = 0;
		panel.add(scrollPane1, gbcScrollPane1);

		dimensionsToKeepList = new JList<>();
		dimensionsToKeepList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		dimensionsToKeepList.setFont(new Font("Tahoma", Font.BOLD, 20));
		scrollPane1.setViewportView(dimensionsToKeepList);

		JLabel lblNewLabel1 = new JLabel("Dimension to keep");
		scrollPane1.setColumnHeaderView(lblNewLabel1);
		lblNewLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		btnCancel.addActionListener(this::closeDialog);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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

	private void closeDialog(@SuppressWarnings("unused") ActionEvent e) {
		resultHierarchy = null;
		dispose();
	}

	private void confirmDialog(@SuppressWarnings("unused") ActionEvent e) {
		if (validateSelection()) {
			setResult();
			dispose();
		}
	}

	private boolean validateSelection() {
		return !getListData(dimensionsToKeepList).isEmpty();
	}

	private void setResult() {
		List<Dimension> dimensionList = getListData(dimensionsToKeepList);

		List<Integer> list = new ArrayList<>();

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
		// override to disable
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
