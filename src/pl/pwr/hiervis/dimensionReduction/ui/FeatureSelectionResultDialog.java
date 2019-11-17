package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.dimensionReduction.ui.elements.BarGraph;
import pl.pwr.hiervis.dimensionReduction.ui.elements.DecimalFormatRenderer;
import pl.pwr.hiervis.dimensionReduction.ui.elements.FeatureSelectionResultTableModel;
import pl.pwr.hiervis.dimensionReduction.ui.elements.KeyBinds;
import pl.pwr.hiervis.dimensionReduction.ui.elements.TableColumnAdjuster;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.SwingUIUtils;
import pl.pwr.hiervis.util.ui.JFileChooserEx;

public class FeatureSelectionResultDialog extends JDialog {

	private static final long serialVersionUID = -2918179612825385352L;
	private static final Logger log = LogManager.getLogger(FeatureSelectionResultDialog.class);
	private HVContext context;
	private LoadedHierarchy loadedHierarchy;
	private JTable dataTable;
	private List<FeatureSelectionResult> resultsList;
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
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		getContentPane().add(scrollPane, gbc_scrollPane);

		this.context = context;
		this.resultsList = selectionResult;

		FeatureSelectionResultTableModel tabModel = new FeatureSelectionResultTableModel(selectionResult);

		dataTable = new JTable(tabModel);
		dataTable.setEnabled(false);

		dataTable.getColumnModel().getColumn(1).setCellRenderer(new DecimalFormatRenderer(selectionResult));

		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dataTable.getModel());
		dataTable.setRowSorter(sorter);

		scrollPane.setViewportView(dataTable);
		TableColumnAdjuster columnAdjuster = new TableColumnAdjuster(dataTable, 12);
		columnAdjuster.adjustColumns();

		setCellAlighment(dataTable.getColumnModel().getColumn(0), JLabel.RIGHT);
		// setCellAlighment(dataTable.getColumnModel().getColumn(1), JLabel.RIGHT);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		getContentPane().add(panel, gbc_panel);
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

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

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

		Color backgroundColor = Color.WHITE;
		Color barColor = Color.BLUE;
		if (context != null) {
			backgroundColor = context.getConfig().getBackgroundColor();
			barColor = context.getConfig().getHistogramColor();
		}

		return BarGraph.CreateBarGraph(data, labels, backgroundColor, barColor);
	}

	private void openDropDimensionDialog(ActionEvent actionEvent) {
		DropDimensionDialog dropDimensionDialogn = new DropDimensionDialog(loadedHierarchy, resultsList);
		LoadedHierarchy hierarchy = dropDimensionDialogn.showDialog();
		if (hierarchy != null) {
			context.loadHierarchy(dropDimTitle, hierarchy);
			dispose();
		}
	}

	private void exportToCSV(ActionEvent actionEvent) {
		JFileChooserEx fileDialog = new JFileChooserEx();
		fileDialog.setCurrentDirectory(new File("."));
		fileDialog.setDialogTitle("Select destination file");
		fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileDialog.setAcceptAllFileFilterUsed(true);
		fileDialog.addChoosableFileFilter(new FileNameExtensionFilter("*.csv", "csv"));

		if (fileDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				log.trace(fileDialog.getSelectedFile().getAbsolutePath());
				FeatureSelectionResult.saveToFile(fileDialog.getSelectedFile(), resultsList);
			} catch (IOException e) {
				log.error("Error while saving feature selection results: ", e);
				SwingUIUtils.showErrorDialog("Error while saving feature selection results:\n\n" + e.getMessage());
			}
		} else {
			log.trace("Saving aborted.");
		}
	}

}