function() {
	// Load required classes
	var FlatDunn2 = Java.type( 'hierarchy_measures.internal_measures.FlatDunn2' );
	var Euclidean = Java.type( 'hierarchy_measures.distance_measures.Euclidean' );

	// Create and return the result holder object
	var measureData = {};
	measureData.measure = new FlatDunn2( new Euclidean() );
	measureData.id = 'Flat Dunn 2 (Euclidean)';
	measureData.callback = function ( hierarchy ) {
		return this.measure.getMeasure( hierarchy );
	}

	return measureData;
}
