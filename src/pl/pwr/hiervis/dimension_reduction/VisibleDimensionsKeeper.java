package pl.pwr.hiervis.dimension_reduction;

public class VisibleDimensionsKeeper {

	private boolean[] visibleVerticalDimensions;
	private boolean[] visibleHorizontalDimensions;

	public VisibleDimensionsKeeper(int size) {
		visibleVerticalDimensions = new boolean[size];
		visibleHorizontalDimensions = new boolean[size];
	}

	public boolean[] getVisibleVerticalDimensions() {
		return visibleVerticalDimensions;
	}

	public boolean[] getVisibleHorizontalDimensions() {
		return visibleHorizontalDimensions;
	}

	public void toggleVisibility(int dim, boolean horizontal, boolean isVisible) {
		if (horizontal)
			visibleHorizontalDimensions[dim] = isVisible;
		else
			visibleVerticalDimensions[dim] = isVisible;
	}

	public String getString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\n");
		stringBuilder.append("H: ");
		for (boolean b : visibleHorizontalDimensions) {
			stringBuilder.append(b + ",");
		}
		stringBuilder.append("\n");
		stringBuilder.append("V: ");
		for (boolean b : visibleVerticalDimensions) {
			stringBuilder.append(b + ",");
		}
		return stringBuilder.toString();
	}
}
