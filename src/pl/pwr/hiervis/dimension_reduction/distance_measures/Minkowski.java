package pl.pwr.hiervis.dimension_reduction.distance_measures;

public class Minkowski implements DistanceMeasure {
	double p;

	public Minkowski() {
		this.p = 1.5;
	}

	public Minkowski(double p) {
		this.p = p;
	}

	@Override
	public double getDistance(double[] a, double[] b) {
		if (a.length != b.length) {
			throw new IllegalArgumentException("a.length != b.length");
		}

		double measure = 0.0;
		for (int i = 0; i < a.length; i++) {
			measure += Math.pow(Math.abs(a[i] - b[i]), p);
		}

		return Math.pow(measure, 1.0 / p);
	}

	public String toString() {
		return "Minkowski";
	}
}
