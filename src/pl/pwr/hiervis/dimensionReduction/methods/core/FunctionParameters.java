package pl.pwr.hiervis.dimensionReduction.methods.core;

import java.util.List;
import java.util.Map;

public interface FunctionParameters {

    public DataType getValueClass();

    public String getValueName();

    public String getValueDescription();

    public abstract class DataType {
	public boolean isType(Class<?> type) {
	    return this.getClass() == type;
	}
    }

    public class BooleanType extends DataType {
	private boolean defaultValue;
	private String[] enableParameterNames;

	public BooleanType(boolean defaultValue, String... enableParameterName) {
	    this.defaultValue = defaultValue;
	    this.enableParameterNames = enableParameterName;
	}

	public boolean getDefaultValue() {
	    return defaultValue;
	}

	public String[] getEnableParameterNames() {
	    return enableParameterNames.clone();
	}

	@SuppressWarnings("unused")
	private BooleanType() {
	}
    }

    public class ListType<T> extends DataType {
	private List<T> listValues;
	private Map<T, String[]> enablesMap;

	public ListType(List<T> listValues, Map<T, String[]> enablesMap) {
	    this.listValues = listValues;
	    this.enablesMap = enablesMap;
	}

	public ListType(List<T> listValues) {
	    this(listValues, null);
	}

	public List<T> getValues() {
	    return listValues;
	}

	public Map<T, String[]> getEnableParameterNames() {
	    return enablesMap;
	}

	@SuppressWarnings("unused")
	private ListType() {
	}
    }

    public abstract class NumericalType<T> extends DataType {

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

    public class DoubleType extends NumericalType<Double> {
	public DoubleType(Function2p<Double> minimumF, Function2p<Double> maximumF) {
	    super(minimumF, maximumF);
	}

	public DoubleType(Function2p<Double> minimumF, Function2p<Double> maximumF, Function2p<Double> defaultValueF) {
	    super(minimumF, maximumF, defaultValueF);
	}
    }

    public class IntegerType extends NumericalType<Integer> {
	public IntegerType(Function2p<Integer> minimumF, Function2p<Integer> maximumF) {
	    super(minimumF, maximumF);
	}

	public IntegerType(Function2p<Integer> minimumF, Function2p<Integer> maximumF, Function2p<Integer> defaultValueF) {
	    super(minimumF, maximumF, defaultValueF);
	}
    }

    public interface Function2p<T> {
	public T function(T points, T dimensions);
    }

    public static Class<BooleanType> BOOLEAN = BooleanType.class;
    public static Class<IntegerType> INTEGER = IntegerType.class;
    public static Class<DoubleType> DOUBLE = DoubleType.class;
    public static Class<ListType> LIST = ListType.class;

    public static FunctionParameters CreateFunctionParameter(String name, String description, DataType dataType) {
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
