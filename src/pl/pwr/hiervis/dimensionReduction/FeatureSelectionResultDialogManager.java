package pl.pwr.hiervis.dimensionReduction;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelection;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.dimensionReduction.ui.FeatureSelectionResultDialog;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class FeatureSelectionResultDialogManager {

    List<ResultDialog> list;

    public FeatureSelectionResultDialogManager() {
	list = new ArrayList<ResultDialog>();

	HVContext.getContext().hierarchyClosing.addListener(this::onHierarchyClosing);
    }

    private void onHierarchyClosing(LoadedHierarchy loadedHierarchy) {
	Predicate<ResultDialog> loadedHierPred = resultDialog -> resultDialog.loadedHierarchy.equals(loadedHierarchy);
	deleteIfPred(loadedHierPred);
    }

    private void deleteDialogIfExisting(LoadedHierarchy loadedHierarchy, FeatureSelection featureSelection) {
	Predicate<ResultDialog> loadedHierPred = resultDialog -> resultDialog.loadedHierarchy.equals(loadedHierarchy);
	Predicate<ResultDialog> fsPred = resultDialog -> resultDialog.featureSelection.getClass().equals(featureSelection.getClass());
	deleteIfPred(loadedHierPred.and(fsPred));
    }

    private void deleteIfPred(Predicate<ResultDialog> pred) {
	ListIterator<ResultDialog> iter = list.listIterator();
	while (iter.hasNext()) {
	    ResultDialog resultDialog = iter.next();
	    if (pred.test(resultDialog)) {
		resultDialog.dialog.dispose();
		iter.remove();
	    }
	}
    }

    private FeatureSelectionResultDialog addDialog(LoadedHierarchy loadedHierarchy, FeatureSelection featureSelection,
	    List<FeatureSelectionResult> results) {
	String featureSelectionName = featureSelection.getName();
	FeatureSelectionResultDialog dialog = new FeatureSelectionResultDialog(results, featureSelectionName);
	list.add(new ResultDialog(loadedHierarchy, featureSelection, dialog));
	return dialog;
    }

    private FeatureSelectionResultDialog getResultDialog(LoadedHierarchy loadedHierarchy, Class<?> featureSelectionClass) {
	for (ResultDialog resultDialog : list) {
	    if (resultDialog.loadedHierarchy == loadedHierarchy && resultDialog.featureSelection.getClass() == featureSelectionClass)
		return resultDialog.dialog;
	}
	return null;
    }

    public void addResultAndShow(CalculatedDimensionReduction reduction) {
	deleteDialogIfExisting(reduction.inputLoadedHierarchy, (FeatureSelection) reduction.dimensionReduction);
	FeatureSelectionResultDialog dialog = addDialog(reduction.inputLoadedHierarchy, (FeatureSelection) reduction.dimensionReduction,
		reduction.fsResult);

	dialog.showDialog();
    }

    public void showDialog(LoadedHierarchy loadedHierarchy, Class<?> featureSelectionClass) {
	FeatureSelectionResultDialog dialog = getResultDialog(loadedHierarchy, featureSelectionClass);
	if (dialog != null)
	    dialog.showDialog();
    }

    private class ResultDialog {
	LoadedHierarchy loadedHierarchy;
	FeatureSelection featureSelection;
	FeatureSelectionResultDialog dialog;

	public ResultDialog(LoadedHierarchy loadedHierarchy, FeatureSelection featureSelection, FeatureSelectionResultDialog dialog) {
	    this.loadedHierarchy = loadedHierarchy;
	    this.featureSelection = featureSelection;
	    this.dialog = dialog;
	}
    }
}
