package org.smicon.rest.metas;

import papu.annotations.Model;

public class ModelEntityMeta 
extends EntityMeta
{
	
	private Model modelAnnotation;

	public ModelEntityMeta() throws Exception { }
	
	protected void init(Class<?> aModelClass) throws Exception {
		super.init(aModelClass);
		modelAnnotation = aModelClass.getAnnotation(Model.class);
	}

	public Model getModelAnnotation() {
		return modelAnnotation;
	}

	public String getPersistenceUnitName() {
		return this.getModelAnnotation().persistenceUnitName();
	}

	public String getPlural() {
		return modelAnnotation.plural();
	}

	public String getSingular() {
		return this.getModelClass().getSimpleName().toLowerCase();
	}

}
