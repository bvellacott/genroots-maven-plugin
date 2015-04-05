App.TestClass = DS.Model.extend({
	// Simple properties
	// Entity properties with simple id's
	// Entity properties with embedded id's
	// Entity properties with composite id's
	compost : DS.belongsTo('testClassComposite', {async : true}),
	// Entity collection properties
	embeddeds : DS.hasMany('testClassEmbedded', {async : true}),
});
