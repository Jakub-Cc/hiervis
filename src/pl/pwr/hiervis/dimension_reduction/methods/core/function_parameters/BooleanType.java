package pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters;

import pl.pwr.hiervis.dimension_reduction.methods.core.Function2p;

public class BooleanType implements DataType {
	private boolean defaultValue;
	private String[] enableParameterNames;
	private Function2p<Integer> enabled;

	public BooleanType(boolean defaultValue, Function2p<Integer> enabled, String... enableParameterName) {
		this.defaultValue = defaultValue;
		this.enableParameterNames = enableParameterName;
		this.enabled = enabled;
	}

	public BooleanType(boolean defaultValue, String... enableParameterName) {
		this(defaultValue, (x, y) -> 1, enableParameterName);
	}

	public boolean getDefaultValue() {
		return defaultValue;
	}

	public String[] getEnableParameterNames() {
		return enableParameterNames.clone();
	}

	public Function2p<Integer> isEnabled() {
		return enabled;
	}

	@SuppressWarnings("unused")
	private BooleanType() {
	}
}