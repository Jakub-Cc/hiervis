function() {
	// Load required classes
	var HierarchicalInternalMeasure = Java.type( 'hierarchy_measures.internal_measures.HierarchicalInternalMeasure' );
	var FlatReversedDunn2 = Java.type( 'hierarchy_measures.internal_measures.FlatReversedDunn2' );
	var Euclidean = Java.type( 'hierarchy_measures.distance_measures.Euclidean' );

	// Initialize the measure object
	var qualityMeasure = new FlatReversedDunn2( new Euclidean() );

	// Create and return the result holder object
	var measureData = {};
	measureData.measure = new HierarchicalInternalMeasure( qualityMeasure );
	measureData.id = 'Hierarchical Internal Measure (Flat Reversed Dunn 2, Euclidean)';
	measureData.callback = function ( hierarchy ) {
		return this.measure.getMeasure( hierarchy );
	}

	return measureData;
}
