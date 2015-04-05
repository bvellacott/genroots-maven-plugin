App.TestClass = DS.Model.extend({
	// Entity properties with composite id's
	compost : DS.belongsTo('testClassComposite', {async : true}),
	// Entity collection properties
	embeddeds : DS.hasMany('testClassEmbedded', {async : true}),
});
