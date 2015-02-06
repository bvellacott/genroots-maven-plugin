package org.smicon.rest.data.metas;

import javax.persistence.Entity;

import org.smicon.rest.functionality.modelmeta.ModelValidationFunctions;

import papu.annotations.Model;

public class ModelMeta
extends
EntityMeta
{

	private Model modelAnnotation;

	public ModelMeta()
	{}

	private void setDefaultValues()
	{
		modelAnnotation = null;
	}

	public void setEntityClass(Class<?> aModelClass) throws Exception
	{
		if(!ModelValidationFunctions.isModel(aModelClass))
		{
			throw new IllegalArgumentException("The given type doesn't represent a valid Model. A valid model must be annotated both @" + Entity.class.getName() + " and  @" + Model.class.getName());
		}
		
		this.setDefaultValues();
		super.setEntityClass(aModelClass);
		modelAnnotation = aModelClass.getAnnotation(Model.class);
	}

	public Model getModelAnnotation()
	{
		return modelAnnotation;
	}

	public String getPersistenceUnitName()
	{
		return this.getModelAnnotation().persistenceUnitName();
	}

	public String getPlural()
	{
		return modelAnnotation.plural();
	}

	public String getSingular()
	{
		String name = this.getEntityClass().getSimpleName();
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

}
