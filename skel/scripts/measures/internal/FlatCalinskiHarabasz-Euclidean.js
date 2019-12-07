function() {
	// Load required classes
	var FlatCalinskiHarabasz = Java.type( 'hierarchy_measures.internal_measures.FlatCalinskiHarabasz' );
	var Euclidean = Java.type( 'hierarchy_measures.distance_measures.Euclidean' );

	// Create and return the result holder object
	var measureData = {};
	measureData.measure = new FlatCalinskiHarabasz( new Euclidean() );
	measureData.id = 'Flat Calinski-Harabasz (Euclidean)';
	measureData.callback = function ( hierarchy ) {
		return this.measure.getMeasure( hierarchy );
	}

	return measureData;
}
