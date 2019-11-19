package pl.pwr.hiervis.dimensionReduction;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;
import org.jfree.util.Log;

import basic_hierarchy.common.HierarchyUtils;
import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimensionReduction.methods.core.UndefinedParamException;
import pl.pwr.hiervis.dimensionReduction.methods.featureExtraction.MultidimensionalScaling;
import pl.pwr.hiervis.dimensionReduction.methods.featureExtraction.PrincipalComponentAnalysis;
import pl.pwr.hiervis.dimensionReduction.methods.featureExtraction.StarCoordinates;
import pl.pwr.hiervis.dimensionReduction.methods.featureExtraction.Tsne;
import pl.pwr.hiervis.dimensionReduction.methods.featureSelection.CFS;
import pl.pwr.hiervis.dimensionReduction.methods.featureSelection.InfiniteFS;
import pl.pwr.hiervis.dimensionReduction.methods.featureSelection.LaplacianScore;
import pl.pwr.hiervis.dimensionReduction.ui.DimensionReductionnDialog;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

/**
 * Provides all dimension methods, number, and so on
 */
public class DimensionReductionManager {
	/*
	 * Holds all dialogs for dimension reduction methods Elements need to be
	 * manually added after implementation
	 */

	private ArrayList<Pair<LoadedHierarchy, Class<? extends DimensionReductionI>>> calculationQue;

	private ArrayList<DimensionReductionI> dimensionReductions;

	public DimensionReductionManager() {
		calculationQue = new ArrayList<Pair<LoadedHierarchy, Class<? extends DimensionReductionI>>>();

		dimensionReductions = new ArrayList<DimensionReductionI>();
		dimensionReductions.add(new MultidimensionalScaling());
		dimensionReductions.add(new StarCoordinates());
		dimensionReductions.add(new PrincipalComponentAnalysis());
		dimensionReductions.add(new Tsne());
		dimensionReductions.add(new InfiniteFS());
		dimensionReductions.add(new CFS());
		dimensionReductions.add(new LaplacianScore());
	}

	public int getIndex(DimensionReductionI dimensionReduction) {
		if (dimensionReduction == null)
			return -1;
		int index = -1;
		for (int i = 0; i < getSize(); i++) {
			if (dimensionReductions.get(i).getClass().isAssignableFrom(dimensionReduction.getClass()))
				index = i;
		}
		return index;
	}

	public int getSize() {
		return dimensionReductions.size();
	}

	public String[] getNames() {
		return dimensionReductions.stream().map(e -> e.getName()).toArray(String[]::new);
	}

	public String[] getSimpleNames() {
		return dimensionReductions.stream().map(e -> e.getSimpleName()).toArray(String[]::new);
	}

	public Class<? extends DimensionReductionI> getResaultClass(int index) {
		return (index < 0 || index >= getSize()) ? null : dimensionReductions.get(index).getClass();
	}

	public DimensionReductionI showDialog(int index, int maxOutputDimensions, int pointsAmount, int x, int y) {

		DimensionReductionI dimensionReduction = dimensionReductions.get(index);
		DimensionReductionnDialog<DimensionReductionI> selectionDialog = new DimensionReductionnDialog<DimensionReductionI>(
				dimensionReduction);

		try {
			return selectionDialog.showDialog(maxOutputDimensions, pointsAmount, x, y);
		} catch (UndefinedParamException e) {
			Log.error(e);
			return null;
		}
	}

	public DimensionReductionI showDialog(int index, int maxOutputDimensions, int pointsAmount) {
		return showDialog(index, maxOutputDimensions, pointsAmount, 100, 100);
	}

	public DimensionReductionI showDialog(int index, Hierarchy hierarchy, int x, int y) {
		return showDialog(index, HierarchyUtils.getFirstInstance(hierarchy).getData().length,
				hierarchy.getOverallNumberOfInstances(), x, y);
	}

	public DimensionReductionI showDialog(int index, Hierarchy hierarchy) {
		return showDialog(index, hierarchy, 100, 100);
	}

	public boolean addToQueue(LoadedHierarchy hierarchy, Class<? extends DimensionReductionI> reductionClass) {
		return calculationQue.add(Pair.of(hierarchy, reductionClass));
	}

	public boolean isInQueue(LoadedHierarchy hierarchy, Class<? extends DimensionReductionI> reductionClass) {
		for (Pair<LoadedHierarchy, Class<? extends DimensionReductionI>> pair : calculationQue) {
			if (pair.getLeft() == hierarchy && pair.getRight() == reductionClass) {
				return true;
			}
		}
		return false;
	}

	public boolean isInQueue(LoadedHierarchy hierarchy, int index) {
		return isInQueue(hierarchy, getResaultClass(index));
	}

	public boolean removeFromQueue(LoadedHierarchy hierarchy, Class<? extends DimensionReductionI> reductionClass) {
		for (Pair<LoadedHierarchy, Class<? extends DimensionReductionI>> pair : calculationQue) {
			if (pair.getLeft() == hierarchy && pair.getRight() == reductionClass) {
				calculationQue.remove(pair);
				return true;
			}
		}
		return false;
	}

}
