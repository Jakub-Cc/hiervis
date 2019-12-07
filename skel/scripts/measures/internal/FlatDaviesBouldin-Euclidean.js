function() {
	// Load required classes
	var FlatDaviesBouldin = Java.type( 'hierarchy_measures.internal_measures.FlatDaviesBouldin' );
	var Euclidean = Java.type( 'hierarchy_measures.distance_measures.Euclidean' );

	// Create and return the result holder object
	var measureData = {};
	measureData.measure = new FlatDaviesBouldin( new Euclidean() );
	measureData.id = 'Flat Davies-Bouldin (Euclidean)';
	measureData.callback = function ( hierarchy ) {
		return this.measure.getMeasure( hierarchy );
	}

	return measureData;
}
