package pl.pwr.hiervis.dimensionReduction.methods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import basic_hierarchy.interfaces.Hierarchy;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelection;
import pl.pwr.hiervis.dimensionReduction.methods.core.FeatureSelectionResult;
import pl.pwr.hiervis.dimensionReduction.methods.core.FunctionParameters;
import pl.pwr.hiervis.hierarchy.LoadedHierarchy;

public class InfiniteFS extends FeatureSelection {

    private Double alpha;

    public InfiniteFS(double alpha) {
	this.alpha = alpha;
    }

    public InfiniteFS() {
	alpha = null;
	// TODO decide in empty function needed, or default contsructor is enough
    }

    @Override
    public String getName() {
	// TODO decide if sufficient name
	return "Infinite FS";
    }

    @Override
    public String getSimpleName() {
	// TODO decide if sufficient name
	return "InfFS";
    }

    @Override
    public String getDescription() {
	// TODO declare description
	return "Infinite FS description";
    }

    @Override
    public Long getMinimumMemmory(int pointsNumber, int dimensionSize) {
	// TODO calculate real memory requirements
	return 0l;
    }

    @Override
    public List<FeatureSelectionResult> selectFeatures(LoadedHierarchy source) {

	// TODO add logic

	Hierarchy hierarchy = source.getMainHierarchy();
	String[] dimensionNames;
	if (hierarchy.getDataNames() == null) {
	    dimensionNames = new String[hierarchy.getRoot().getSubtreeInstances().getFirst().getData().length];
	    for (int i = 0; i < dimensionNames.length; i++) {
		dimensionNames[i] = "dimension " + (i + 1);
	    }
	}
	else {
	    dimensionNames = hierarchy.getDataNames();
	}

	List<FeatureSelectionResult> set = new ArrayList<FeatureSelectionResult>();
	for (int i = 0; i < dimensionNames.length; i++) {
	    set.add(new FeatureSelectionResult(i, dimensionNames[i], i, dimensionNames.length - i));
	}
	return set;
    }

    @Override
    public List<FunctionParameters> getParameters() {

	List<FunctionParameters> functionParameters = new ArrayList<FunctionParameters>();

	functionParameters.add(new FunctionParameters() {
	    @Override
	    public String getValueName() {
		return "Alpha";
	    }

	    @Override
	    public String getValueDescription() {
		return "Alpha description Alpha descriptionAlpha description<br> Alpha descriptionAlpha descriptionAlpha \ndescriptionAlpha descriptionAlpha description Alpha descriptionAlpha descriptionAlpha description";
	    }

	    @Override
	    public DataType getValueClass() {
		return new DoubleType((points, dimensions) -> 0.0, (points, dimensions) -> 1.0);
	    }
	});

	return functionParameters;
    }

    @Override
    public FeatureSelection createInstance(Map<String, Object> parameters) {
	// TODO rethinks this initialization
	Double alpha = (Double) parameters.get(getParameters().get(0).getValueName());

	return new InfiniteFS(alpha);
    }

}
