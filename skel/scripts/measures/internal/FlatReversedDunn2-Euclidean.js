function() {
	// Load required classes
	var FlatReversedDunn2 = Java.type( 'hierarchy_measures.internal_measures.FlatReversedDunn2' );
	var Euclidean = Java.type( 'hierarchy_measures.distance_measures.Euclidean' );

	// Create and return the result holder object
	var measureData = {};
	measureData.measure = new FlatReversedDunn2( new Euclidean() );
	measureData.id = 'Flat Reversed Dunn 2 (Euclidean)';
	measureData.callback = function ( hierarchy ) {
		return this.measure.getMeasure( hierarchy );
	}

	return measureData;
}
