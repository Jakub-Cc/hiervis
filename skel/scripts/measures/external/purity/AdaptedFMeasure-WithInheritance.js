function() {
	// Load required classes
	var AdaptedFmeasure = Java.type( 'external_measures.AdaptedFmeasure' );

	// Initialize the measure object
	var withInstanceInheritance = true;
	var measure = new AdaptedFmeasure( withInstanceInheritance );

	// Create and return the result holder object
	var measureData = {};
	measureData.id = 'Adapted F-Measure (With Inheritance)';
	measureData.isApplicable = function ( hierarchy ) {
		// Applicable only to hierarchies with ground truth attribute
		return hierarchy.getNumberOfClasses() > 0;
	}
	measureData.callback = function ( hierarchy ) {
		return measure.getMeasure( hierarchy );
	}

	return measureData;
}