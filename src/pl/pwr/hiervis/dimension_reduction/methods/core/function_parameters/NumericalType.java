package pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters;

import pl.pwr.hiervis.dimension_reduction.methods.core.Function2p;

public abstract class NumericalType<T> implements DataType {

	private Function2p<T> minimumF;
	private Function2p<T> maximumF;
	private Function2p<T> defaultValueF;

	@SuppressWarnings("unused")
	private NumericalType() {
	}

	public NumericalType(Function2p<T> minimumF, Function2p<T> maximumF, Function2p<T> defaultValueF) {
		this.minimumF = minimumF;
		this.maximumF = maximumF;
		this.defaultValueF = defaultValueF;
	}

	public NumericalType(Function2p<T> minimumF, Function2p<T> maximumF) {
		this(minimumF, maximumF, null);
	}

	public Function2p<T> getMinimumF() {
		return minimumF;
	}

	public Function2p<T> getMaximumF() {
		return maximumF;
	}

	public Function2p<T> getDefaultValueF() {
		return defaultValueF;
	}
}