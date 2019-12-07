package pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters;

import pl.pwr.hiervis.dimension_reduction.methods.core.Function2p;

public class DoubleType extends NumericalType<Double> {
	public DoubleType(Function2p<Double> minimumF, Function2p<Double> maximumF) {
		super(minimumF, maximumF);
	}

	public DoubleType(Function2p<Double> minimumF, Function2p<Double> maximumF, Function2p<Double> defaultValueF) {
		super(minimumF, maximumF, defaultValueF);
	}
}