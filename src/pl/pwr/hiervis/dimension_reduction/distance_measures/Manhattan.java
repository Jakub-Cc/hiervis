package pl.pwr.hiervis.dimension_reduction.distance_measures;

public class Manhattan implements DistanceMeasure {

	@Override
	public double getDistance(double[] a, double[] b) {
		if (a.length != b.length) {
			throw new IllegalArgumentException("a.length != b.length");
		}

		double measure = 0.0;
		for (int i = 0; i < a.length; i++) {
			measure += Math.abs(a[i] - b[i]);
		}

		return measure;
	}

	public String toString() {
		return "Manhattan";
	}

}
