package pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters;

public class FunctionParametersConst {
	public static final Class<BooleanType> BOOLEAN = BooleanType.class;
	public static final Class<IntegerType> INTEGER = IntegerType.class;
	public static final Class<DoubleType> DOUBLE = DoubleType.class;
	public static final Class<ListType> LIST = ListType.class;

	private FunctionParametersConst() {
		throw new AssertionError("Static class");
	}
}
