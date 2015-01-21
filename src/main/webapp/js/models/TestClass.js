App.TestClass = DS.Model.extend({
	// Simple properties
	id1 : DS.attr('number'),
	// Entity properties with composite id's
	compost : DS.belongsTo('testClassComposite'),
	// Entity collection properties
	embeddeds : DS.hasMany('testClassEmbedded'),
});
