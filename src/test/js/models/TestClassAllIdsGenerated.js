App.TestClassAllIdsGenerated = DS.Model.extend({
	// Simple properties
	// Entity properties with simple id's
	// Entity properties with embedded id's
	embed : DS.belongsTo('testClassEmbedded', {async : true}),
	// Entity properties with composite id's
	// Entity collection properties
	composites : DS.hasMany('testClassComposite', {async : true}),
});
