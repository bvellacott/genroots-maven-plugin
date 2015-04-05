App.TestClassEmbedded = DS.Model.extend({
	// Entity properties with simple id's
	tcl : DS.belongsTo('testClass', {async : true}),
});
