package pl.pwr.hiervis.dimensionReduction;

import java.util.ArrayList;
import java.util.List;

import pl.pwr.hiervis.core.HVContext;
import pl.pwr.hiervis.dimensionReduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class DimensionReductionRunnerManager {
    private HVContext context;
    private List<DimensionReductionRunner> taskList;

    public DimensionReductionRunnerManager(HVContext context) {
	this.context = context;
	taskList = new ArrayList<DimensionReductionRunner>();
	context.dimensionReductionCalculated.addListener(this::onDimensionReductionCalculated);
    }

    public void addTask(LoadedHierarchy loadedHierarchy, DimensionReductionI dimensionReduction) {
	DimensionReductionRunner dimensionReductionRunner = new DimensionReductionRunner(loadedHierarchy, dimensionReduction,
		context.dimensionReductionCalculated);
	dimensionReductionRunner.start();
	taskList.add(dimensionReductionRunner);
    }

    public boolean removeTask(LoadedHierarchy loadedHierarchy, Class<? extends DimensionReductionI> dimensionReductionClass) {
	for (DimensionReductionRunner reductionRunner : taskList) {
	    if (reductionRunner.isTheSame(loadedHierarchy, dimensionReductionClass)) {
		taskList.remove(reductionRunner);
		return true;
	    }
	}
	return false;
    }

    public void onDimensionReductionCalculated(CalculatedDimensionReduction calculatedDimensionReduction) {
	removeTask(calculatedDimensionReduction.inputLoadedHierarchy, calculatedDimensionReduction.dimensionReduction.getClass());
    }

    public boolean interuptTask(LoadedHierarchy loadedHierarchy, Class<? extends DimensionReductionI> dimensionReductionClass) {
	for (DimensionReductionRunner reductionRunner : taskList) {
	    if (reductionRunner.isTheSame(loadedHierarchy, dimensionReductionClass)) {
		reductionRunner.myInterrupt();
		taskList.remove(reductionRunner);
		return true;
	    }
	}
	return false;
    }

}
