package pl.pwr.hiervis.dimensionReduction.ui.elements;

import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelectionResult;

public class FeatureSelectionResultTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -1215902733821083700L;
    private static final int COLUMN_RANK = 0;
    private static final int COLUMN_SCORE = 1;
    private static final int COLUMN_NAME = 2;

    private String[] columnNames = { "Rank", "Score", "Dimension Name" };
    private List<FeatureSelectionResult> featureSelectionResults;

    public FeatureSelectionResultTableModel(List<FeatureSelectionResult> featureSelectionResults) {
	this.featureSelectionResults = featureSelectionResults;
	Collections.sort(this.featureSelectionResults);
    }

    @Override
    public int getColumnCount() {
	return columnNames.length;
    }

    @Override
    public int getRowCount() {
	return featureSelectionResults.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
	return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
	if (featureSelectionResults.isEmpty()) {
	    return Object.class;
	}
	return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
	FeatureSelectionResult result = featureSelectionResults.get(rowIndex);
	Object returnValue = null;

	switch (columnIndex) {
	case COLUMN_RANK:
	    returnValue = result.getRank();
	    break;
	case COLUMN_SCORE:
	    returnValue = result.getScore();
	    break;
	case COLUMN_NAME:
	    returnValue = result.getDimensionName();
	    break;
	default:
	    throw new IllegalArgumentException("Invalid column index");
	}
	return returnValue;
    }

}
