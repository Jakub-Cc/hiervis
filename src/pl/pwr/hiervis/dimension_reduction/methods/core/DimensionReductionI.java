package pl.pwr.hiervis.dimension_reduction.methods.core;

import java.util.List;
import java.util.Map;

import pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters.FunctionParameters;

public interface DimensionReductionI {

    public String getName();

    public String getSimpleName();

    public String getDescription();

    public Long getMinimumMemmory(int pointsNumber, int dimensionSize);

    public List<FunctionParameters> getParameters();

    public DimensionReductionI createInstance(Map<String, Object> parameters);
}
