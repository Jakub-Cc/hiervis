package pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters;

public interface DataType {
	public default boolean isType(Class<?> type) {
		return this.getClass() == type;
	}
}