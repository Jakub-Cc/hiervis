package pl.pwr.hiervis.dimension_reduction.methods.core.function_parameters;

import java.util.List;
import java.util.Map;

public class ListType<T> implements DataType {
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