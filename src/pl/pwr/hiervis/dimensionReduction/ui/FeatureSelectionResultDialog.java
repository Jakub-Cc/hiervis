package pl.pwr.hiervis.dimensionReduction.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelectionResultTableModel;
import pl.pwr.hiervis.dimensionReduction.ui.elements.BarGraph;
import pl.pwr.hiervis.dimensionReduction.ui.elements.TableColumnAdjuster;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.SwingUIUtils;
import pl.pwr.hiervis.util.ui.JFileChooserEx;
import prefuse.Display;

public class FeatureSelectionResultDialog extends JDialog {

    private static final long serialVersionUID = -2918179612825385352L;
    private HVContext context;
    private JTable dataTable;
    private List<FeatureSelectionResult> resultsList;
    private static final Logger log = LogManager.getLogger(FeatureSelectionResultDialog.class);

    public static void main(String[] args) {
	List<FeatureSelectionResult> list = new ArrayList<FeatureSelectionResult>();

	for (int i = 0; i < 80; i++) {
	    list.add(new FeatureSelectionResult(i, "Dimension_" + i, i, i));
	}

	FeatureSelectionResultDialog resultDialog = new FeatureSelectionResultDialog(list, null);
	resultDialog.pack();
	resultDialog.setMinimumSize(resultDialog.getSize());
	resultDialog.setVisible(true);
    }

    public void showDialog() {
	this.pack();
	this.setMinimumSize(this.getSize());
	this.setVisible(true);

    }

    public FeatureSelectionResultDialog(List<FeatureSelectionResult> selectionResult, HVContext context) {
	this.setTitle("Feature Selection Result Dialog");

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

	// DefaultTableModel defaultTableModel = creataDataTableModel(selectionResult);
	FeatureSelectionResultTableModel tabModel = new FeatureSelectionResultTableModel(selectionResult);

	dataTable = new JTable(tabModel);
	dataTable.setEnabled(false);

	TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dataTable.getModel());
	dataTable.setRowSorter(sorter);

	TableColumnAdjuster columnAdjuster = new TableColumnAdjuster(dataTable);
	columnAdjuster.adjustColumns();

	setCellAlighment(dataTable.getColumnModel().getColumn(0), JLabel.RIGHT);
	setCellAlighment(dataTable.getColumnModel().getColumn(1), JLabel.CENTER);

	scrollPane.setViewportView(dataTable);

	JPanel panel = new JPanel();
	GridBagConstraints gbc_panel = new GridBagConstraints();
	gbc_panel.fill = GridBagConstraints.BOTH;
	gbc_panel.gridx = 1;
	gbc_panel.gridy = 0;
	getContentPane().add(panel, gbc_panel);
	panel.setLayout(new BorderLayout(0, 0));

	Display barChart = createBarChart(10);
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
	setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
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

    private Display createBarChart(int maxAmount) {
	maxAmount = Math.min(maxAmount, resultsList.size());

	double[] data = new double[maxAmount];
	String[] lables = new String[maxAmount];
	for (int i = 0; i < maxAmount; i++) {
	    data[i] = resultsList.get(i).getScore();
	    lables[i] = resultsList.get(i).getDimensionName();
	}

	return new BarGraph(data, lables, context);
    }

    private void openDropDimensionDialog(ActionEvent actionEvent) {
	DropDimensionDialog dropDimensionDialogn = new DropDimensionDialog(context.getHierarchy(), resultsList);
	LoadedHierarchy hierarchy = dropDimensionDialogn.showDialog();
	if (hierarchy != null) {
	    String tabTitle = "[D] " + context.getHierarchyFrame().getSelectedTabTitle();
	    context.loadHierarchy(tabTitle, hierarchy);
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
	    }
	    catch (IOException e) {
		log.error("Error while saving feature selection results: ", e);
		SwingUIUtils.showErrorDialog("Error while saving feature selection results:\n\n" + e.getMessage());
	    }
	}
	else {
	    log.trace("Saving aborted.");
	}
    }

}