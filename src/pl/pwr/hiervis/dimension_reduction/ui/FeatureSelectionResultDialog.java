package pl.pwr.hiervis.dimension_reduction.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.dimension_reduction.ui.elements.BarGraph;
import pl.pwr.hiervis.dimension_reduction.ui.elements.DecimalFormatRenderer;
import pl.pwr.hiervis.dimension_reduction.ui.elements.FeatureSelectionResultTableModel;
import pl.pwr.hiervis.dimension_reduction.ui.elements.KeyBinds;
import pl.pwr.hiervis.dimension_reduction.ui.elements.TableColumnAdjuster;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.ui.JFileChooserEx;

public class FeatureSelectionResultDialog extends JDialog {

	private static final long serialVersionUID = -2918179612825385352L;
	private static final Logger log = LogManager.getLogger(FeatureSelectionResultDialog.class);
	private transient LoadedHierarchy loadedHierarchy;
	private transient List<FeatureSelectionResult> resultsList;
	private JTable dataTable;
	private String dropDimTitle;

	public void showDialog() {
		this.pack();
		this.setMinimumSize(this.getSize());
		this.setVisible(true);

	}

	public FeatureSelectionResultDialog(List<FeatureSelectionResult> selectionResult, String featureSelectionName) {
		HVContext context = HVContext.getContext();
		loadedHierarchy = context.getHierarchy();
		dropDimTitle = "[D] " + context.getHierarchyFrame().getSelectedTabTitle();

		String title = featureSelectionName + " Result for: " + context.getHierarchyFrame().getSelectedTabTitle();
		this.setTitle(title);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 107, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.insets = new Insets(0, 0, 0, 5);
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 0;
		getContentPane().add(scrollPane, gbcScrollPane);

		this.resultsList = selectionResult;

		FeatureSelectionResultTableModel tabModel = new FeatureSelectionResultTableModel(selectionResult);

		dataTable = new JTable(tabModel);
		dataTable.setEnabled(false);

		dataTable.getColumnModel().getColumn(1).setCellRenderer(new DecimalFormatRenderer(selectionResult));

		TableRowSorter<TableModel> sorter = new TableRowSorter<>(dataTable.getModel());
		dataTable.setRowSorter(sorter);

		scrollPane.setViewportView(dataTable);
		TableColumnAdjuster columnAdjuster = new TableColumnAdjuster(dataTable, 12);
		columnAdjuster.adjustColumns();

		setCellAlighment(dataTable.getColumnModel().getColumn(0), SwingConstants.RIGHT);

		JPanel panel = new JPanel();
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.fill = GridBagConstraints.BOTH;
		gbcPanel.gridx = 1;
		gbcPanel.gridy = 0;
		getContentPane().add(panel, gbcPanel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel barChart = createBarChart(10);
		panel.add(barChart, BorderLayout.CENTER);

		JPanel btnPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) btnPanel.getLayout();
		flowLayout.setHgap(15);
		panel.add(btnPanel, BorderLayout.SOUTH);

		JButton btnRemoveDimensions = new JButton("Remove Dimensions");
		btnRemoveDimensions.addActionListener(this::openDropDimensionDialog);
		btnPanel.add(btnRemoveDimensions);

		JButton btnExportToCsv = new JButton("Export to CSV");
		btnExportToCsv.addActionListener(this::exportToCSV);
		btnPanel.add(btnExportToCsv);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		KeyBinds.setKeybindESC((JPanel) getContentPane(), KeyBinds.createDisposeAction(this));
	}

	/**
	 * 
	 * @param tableColumn
	 * @param aligment    JLabel.CENTER | JLabel.RIGHT | JLabel.LEFT
	 */
	private void setCellAlighment(TableColumn tableColumn, int aligment) {
		DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(aligment);
		tableColumn.setCellRenderer(cellRenderer);
	}

	private JPanel createBarChart(int maxAmount) {
		maxAmount = Math.min(maxAmount, resultsList.size());

		double[] data = new double[maxAmount];
		String[] labels = new String[maxAmount];
		for (int i = 0; i < maxAmount; i++) {
			data[i] = resultsList.get(i).getScore();
			labels[i] = resultsList.get(i).getDimensionName();
		}

		Color backgroundColor = HVContext.getContext().getConfig().getBackgroundColor();
		Color barColor = HVContext.getContext().getConfig().getHistogramColor();

		return BarGraph.createBarGraph(data, labels, backgroundColor, barColor);
	}

	private void openDropDimensionDialog(@SuppressWarnings("unused") ActionEvent actionEvent) {
		DropDimensionDialog dropDimensionDialogn = new DropDimensionDialog(loadedHierarchy, resultsList);
		LoadedHierarchy hierarchy = dropDimensionDialogn.showDialog();
		if (hierarchy != null) {
			HVContext.getContext().loadHierarchy(dropDimTitle, hierarchy);
			dispose();
		}
	}

	private void exportToCSV(@SuppressWarnings("unused") ActionEvent actionEvent) {
		JFileChooserEx fileDialog = new JFileChooserEx();
		fileDialog.setCurrentDirectory(new File("."));
		fileDialog.setDialogTitle("Select destination file");
		fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileDialog.setAcceptAllFileFilterUsed(true);
		fileDialog.addChoosableFileFilter(new FileNameExtensionFilter("*.csv", "csv"));

		if (fileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			log.trace(fileDialog.getSelectedFile().getAbsolutePath());
			FeatureSelectionResult.saveToFile(fileDialog.getSelectedFile(), resultsList);
		} else {
			log.trace("Saving aborted.");
		}
	}

}