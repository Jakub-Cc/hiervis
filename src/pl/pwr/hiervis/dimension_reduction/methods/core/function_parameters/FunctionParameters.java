package pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters;

public interface FunctionParameters {

	public DataType getValueClass();

	public String getValueName();

	public String getValueDescription();

	public static FunctionParameters createFunctionParameter(String name, String description, DataType dataType) {
		return new FunctionParameters() {

			@Override
			public String getValueName() {
				return name;
			}

			@Override
			public String getValueDescription() {
				return description;
			}

			@Override
			public DataType getValueClass() {
				return dataType;
			}
		};

	}
}
