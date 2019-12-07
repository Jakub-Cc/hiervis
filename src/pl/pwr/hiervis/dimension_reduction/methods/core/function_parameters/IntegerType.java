package pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters;

import pl.pwr.hiervis.dimension_reduction.methods.core.Function2p;

public class IntegerType extends NumericalType<Integer> {
	public IntegerType(Function2p<Integer> minimumF, Function2p<Integer> maximumF) {
		super(minimumF, maximumF);
	}

	public IntegerType(Function2p<Integer> minimumF, Function2p<Integer> maximumF,
			Function2p<Integer> defaultValueF) {
		super(minimumF, maximumF, defaultValueF);
	}
}