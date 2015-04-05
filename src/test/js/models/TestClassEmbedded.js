App.TestClassEmbedded = DS.Model.extend({
	// Simple properties
	// Entity properties with simple id's
	tcl : DS.belongsTo('testClass', {async : true}),
	// Entity properties with embedded id's
	// Entity properties with composite id's
	// Entity collection properties
});
