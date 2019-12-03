package pl.pwr.hiervis.dimension_reduction.methods.core;

import pl.pwr.hiervis.dimension_reduction.methods.core.FunctionParameters.BooleanType;
import pl.pwr.hiervis.dimension_reduction.methods.core.FunctionParameters.DoubleType;
import pl.pwr.hiervis.dimension_reduction.methods.core.FunctionParameters.IntegerType;
import pl.pwr.hiervis.dimension_reduction.methods.core.FunctionParameters.ListType;

public class FunctionParametersConst {
	public static final Class<BooleanType> BOOLEAN = BooleanType.class;
	public static final Class<IntegerType> INTEGER = IntegerType.class;
	public static final Class<DoubleType> DOUBLE = DoubleType.class;
	public static final Class<ListType> LIST = ListType.class;

	private FunctionParametersConst() {
		throw new AssertionError("Static class");
	}
}
