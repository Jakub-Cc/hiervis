package pl.pwr.hiervis.dimension_reduction.distance_measures;

public interface DistanceMeasure
{
	public double getDistance(double[] a, double[] b);

	public String toString();
}
