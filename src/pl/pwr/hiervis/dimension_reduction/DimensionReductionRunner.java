package pl.pwr.hiervis.dimension_reduction;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimension_reduction.methods.core.DimensionReductionI;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureExtraction;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelection;
import pl.pwr.hiervis.dimension_reduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;
import pl.pwr.hiervis.util.Event;

public class DimensionReductionRunner extends Thread {
	private static final Logger log = LogManager.getLogger(DimensionReductionRunner.class);
	private DimensionReductionI dimensionReduction;
	private LoadedHierarchy inputLoadedHierarchy;
	private Event<CalculatedDimensionReduction> brodcastEvent;

	public DimensionReductionRunner(LoadedHierarchy loadedHierarchy, DimensionReductionI dimensionReduction,
			Event<CalculatedDimensionReduction> brodcastEvent) {
		inputLoadedHierarchy = loadedHierarchy;
		this.dimensionReduction = dimensionReduction;
		this.brodcastEvent = brodcastEvent;
		setName("DimensionReductionComputeThread: " + dimensionReduction.getSimpleName());
		setDaemon(true);
	}

	public boolean isTheSame(LoadedHierarchy loadedHierarchy,
			Class<? extends DimensionReductionI> dimensionReductionClass) {
		return (inputLoadedHierarchy == loadedHierarchy
				&& this.dimensionReduction.getClass() == dimensionReductionClass);
	}

	/*
	 * No other way to stop calculation.
	 */
	@SuppressWarnings("deprecation")
	public void myInterrupt() {
		this.stop();
	}

	@Override
	public void run() {
		Hierarchy outputHierarchy = null;
		List<FeatureSelectionResult> selectionResult = null;
		try {
			long start = System.currentTimeMillis();
			Hierarchy inputHierarchy = inputLoadedHierarchy.getHierarchyWraper().getOriginalHierarchy();
			if (FeatureExtraction.class.isAssignableFrom(dimensionReduction.getClass())) {
				outputHierarchy = ((FeatureExtraction) dimensionReduction).reduceHierarchy(inputHierarchy);

			} else if (FeatureSelection.class.isAssignableFrom(dimensionReduction.getClass())) {
				selectionResult = ((FeatureSelection) dimensionReduction).selectFeatures(inputHierarchy);
			}
			long elapsedTime = System.currentTimeMillis() - start;
			log.trace("{} calculated in: {}  sec", dimensionReduction.getSimpleName(), elapsedTime / (1000F));
		} catch (java.lang.ThreadDeath e) {
			log.trace("Stoped calculation of: {}", this.getName());
		} catch (Exception e) {
			log.trace(e);
		} finally {
			brodcastEvent.broadcast(new CalculatedDimensionReduction(inputLoadedHierarchy, dimensionReduction,
					outputHierarchy, selectionResult));
		}
	}

}
