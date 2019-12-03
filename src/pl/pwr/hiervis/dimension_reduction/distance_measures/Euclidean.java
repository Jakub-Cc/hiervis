package pl.pwr.hiervis.dimension_reduction.distance_measures;

public class Euclidean implements DistanceMeasure {

	@Override
	public double getDistance(double[] a, double[] b) {
		if (a.length != b.length) {
			throw new IllegalArgumentException("a.length != b.length");
		}

		double measure = 0.0;
		for (int i = 0; i < a.length; i++) {
			measure += Math.pow(a[i] - b[i], 2);
		}

		return Math.sqrt(measure);
	}

	public String toString() {
		return "Euclidean";
	}
}
