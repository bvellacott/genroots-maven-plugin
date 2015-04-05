App.TestClassComposite = DS.Model.extend({
	// Entity properties with simple id's
	simple : DS.belongsTo('testClass', {async : true}),
	// Entity properties with embedded id's
	embedded : DS.belongsTo('testClassEmbedded', {async : true}),
	// Entity properties with composite id's
	composite : DS.belongsTo('testClassComposite', {async : true}),
});
