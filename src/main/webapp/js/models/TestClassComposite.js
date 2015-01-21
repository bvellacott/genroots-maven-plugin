App.TestClassComposite = DS.Model.extend({
	// Simple properties
	key1 : DS.attr('number'),
	key2 : DS.attr('number'),
	// Entity properties with simple id's
	simple : DS.belongsTo('testClass'),
	// Entity properties with embedded id's
	embedded : DS.belongsTo('testClassEmbedded'),
	// Entity properties with composite id's
	composite : DS.belongsTo('testClassComposite'),
});
