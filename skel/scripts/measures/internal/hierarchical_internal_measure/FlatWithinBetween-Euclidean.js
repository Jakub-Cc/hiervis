function() {
	// Load required classes
	var HierarchicalInternalMeasure = Java.type( 'hierarchy_measures.internal_measures.HierarchicalInternalMeasure' );
	var FlatWithinBetweenIndex = Java.type( 'hierarchy_measures.internal_measures.FlatWithinBetweenIndex' );
	var Euclidean = Java.type( 'hierarchy_measures.distance_measures.Euclidean' );

	// Initialize the measure object
	var qualityMeasure = new FlatWithinBetweenIndex( new Euclidean() );

	// Create and return the result holder object
	var measureData = {};
	measureData.measure = new HierarchicalInternalMeasure( qualityMeasure );
	measureData.id = 'Hierarchical Internal Measure (Flat Within-Between Index, Euclidean)';
	measureData.callback = function ( hierarchy ) {
		return this.measure.getMeasure( hierarchy );
	}

	return measureData;
}
