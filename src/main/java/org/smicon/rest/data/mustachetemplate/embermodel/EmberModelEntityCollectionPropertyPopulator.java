package org.smicon.rest.data.mustachetemplate.embermodel;

import java.util.Collection;
import java.util.Map;

import org.smicon.rest.data.metas.EntityCollectionPropertyMeta;
import org.smicon.rest.data.metas.ModelMeta;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;
import org.smicon.rest.exceptions.IncorrectEntityStructureException;

public class EmberModelEntityCollectionPropertyPopulator
extends
EmberModelEntityPropertyPopulator
{
	private Class<Collection<?>> collectionType;
	private EntityCollectionPropertyMeta meta;
	private ModelMeta targetMeta;

	public EmberModelEntityCollectionPropertyPopulator(EntityCollectionPropertyMeta aMeta, ModelMetaRegistryI aRegistry) throws Exception
	{
		super(aMeta, aRegistry);

		if (!(Collection.class.isAssignableFrom(this.propertyDescriptor.getPropertyType()) || Map.class.isAssignableFrom(this.propertyDescriptor
			.getPropertyType())))
		{
			throw new IncorrectEntityStructureException("The property: " + this.getParameterName() + " doesn't represent a Collection or a Map!");
		}
		collectionType = (Class<Collection<?>>) this.propertyDescriptor.getPropertyType();
		this.meta = aMeta;

		try
		{
			this.targetMeta = (ModelMeta) this.getRegistry().getModelMeta(this.meta.getTargetType());
		}
		catch (ClassCastException e)
		{
			throw new IncorrectEntityStructureException(
				"The target type of a public entity relationship doesn't point to a valid Model entity. i.e The target entity is not annotated @Model.", e);
		}
	}

	@Override
	public String getTargetTypeObjectForm()
	{
		return this.targetMeta.getEntityClass().getSimpleName();
	}

	@Override
	public String getTargetTypeStringForm()
	{
		String objForm = this.getTargetTypeObjectForm();
		return objForm.substring(0,1).toLowerCase() + objForm.substring(1);
	}

}