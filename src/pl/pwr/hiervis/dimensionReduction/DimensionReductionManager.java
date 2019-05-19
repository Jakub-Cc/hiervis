package pl.pwr.hiervis.dimensionReduction;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimensionReduction.ui.DimensionReductionDialog;
import pl.pwr.hiervis.dimensionReduction.ui.MdsDialog;
import pl.pwr.hiervis.dimensionReduction.ui.PcaDialog;
import pl.pwr.hiervis.dimensionReduction.ui.StarCoordsDialog;
import pl.pwr.hiervis.dimensionReduction.ui.TsneDialog;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

/**
 * Provides all dimension methods, number, and so on
 */
public class DimensionReductionManager {
    /*
     * Holds all dialogs for dimension reduction methods Elements need to be manually added
     * after implementation
     */
    private ArrayList<DimensionReductionDialog> dimensionReductionDialogs;
    private ArrayList<Pair<LoadedHierarchy, Class<? extends FeatureExtraction>>> calculationQue;

    public DimensionReductionManager() {
	calculationQue = new ArrayList<Pair<LoadedHierarchy, Class<? extends FeatureExtraction>>>();
	dimensionReductionDialogs = new ArrayList<DimensionReductionDialog>();
	dimensionReductionDialogs.add(new MdsDialog());
	dimensionReductionDialogs.add(new PcaDialog());
	dimensionReductionDialogs.add(new TsneDialog());
	dimensionReductionDialogs.add(new StarCoordsDialog());
    }

    public int getIndex(FeatureExtraction dimensionReduction) {
	if (dimensionReduction == null)
	    return -1;
	int index = -1;
	for (int i = 0; i < getSize(); i++) {
	    if (dimensionReductionDialogs.get(i).getResultClass().isAssignableFrom(dimensionReduction.getClass()))
		index = i;
	}
	return index;
    }

    public ArrayList<DimensionReductionDialog> getList() {
	return dimensionReductionDialogs;
    }

    public int getSize() {
	return dimensionReductionDialogs.size();
    }

    public String[] getNames() {
	return dimensionReductionDialogs.stream().map(e -> e.getName()).toArray(String[]::new);
    }

    public String[] getSimpleNames() {
	return dimensionReductionDialogs.stream().map(e -> e.getSimpleName()).toArray(String[]::new);
    }

    public DimensionReductionDialog[] getDialogs() {
	return dimensionReductionDialogs.toArray(new DimensionReductionDialog[0]);
    }

    public Class<? extends FeatureExtraction> getResaultClass(int index) {
	return (index < 0 || index >= getSize()) ? null : dimensionReductionDialogs.get(index).getResultClass();
    }

    public FeatureExtraction showDialog(int index, int maxOutputDimensions, int pointsAmount, int x, int y) {
	FeatureExtraction dimensionReduction = dimensionReductionDialogs.get(index).showDialog(maxOutputDimensions, pointsAmount, x, y);
	return dimensionReduction;
    }

    public FeatureExtraction showDialog(int index, int maxOutputDimensions, int pointsAmount) {
	FeatureExtraction dimensionReduction = dimensionReductionDialogs.get(index).showDialog(maxOutputDimensions, pointsAmount);
	return dimensionReduction;
    }

    public FeatureExtraction showDialog(int index, Hierarchy hierarchy, int x, int y) {
	FeatureExtraction dimensionReduction = dimensionReductionDialogs.get(index).showDialog(hierarchy, x, y);
	return dimensionReduction;
    }

    public FeatureExtraction showDialog(int index, Hierarchy hierarchy) {
	return showDialog(index, hierarchy, 100, 100);
    }

    public boolean addToQueue(LoadedHierarchy hierarchy, Class<? extends FeatureExtraction> reductionClass) {
	return calculationQue.add(Pair.of(hierarchy, reductionClass));
    }

    public boolean isInQueue(LoadedHierarchy hierarchy, Class<? extends FeatureExtraction> reductionClass) {

	for (Pair<LoadedHierarchy, Class<? extends FeatureExtraction>> pair : calculationQue) {
	    if (pair.getLeft() == hierarchy && pair.getRight() == reductionClass) {
		return true;
	    }
	}
	return false;
    }

    public boolean isInQueue(LoadedHierarchy hierarchy, int index) {
	return isInQueue(hierarchy, getResaultClass(index));
    }

    public boolean removeFromQueue(LoadedHierarchy hierarchy, Class<? extends FeatureExtraction> reductionClass) {
	for (Pair<LoadedHierarchy, Class<? extends FeatureExtraction>> pair : calculationQue) {
	    if (pair.getLeft() == hierarchy && pair.getRight() == reductionClass) {
		calculationQue.remove(pair);
		return true;
	    }
	}
	return false;
    }

}
