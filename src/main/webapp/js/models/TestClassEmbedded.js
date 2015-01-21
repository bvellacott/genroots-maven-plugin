App.TestClassEmbedded = DS.Model.extend({
	// Simple properties
	embId : DS.attr('object'),
	// Entity properties with simple id's
	tcl : DS.belongsTo('testClass'),
});
