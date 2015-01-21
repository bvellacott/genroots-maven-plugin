package org.smicon.rest.populators.embermodel;

import java.util.Collection;
import java.util.Map;

import org.smicon.rest.exceptions.IncorrectEntityStructureException;
import org.smicon.rest.metas.EntityCollectionPropertyMeta;
import org.smicon.rest.metas.Metas;
import org.smicon.rest.metas.ModelEntityMeta;

public class EmberModelEntityCollectionPropertyPopulator
extends
EmberModelEntityPropertyPopulator
{
	private Class<Collection<?>> collectionType;
	private EntityCollectionPropertyMeta meta;
	private ModelEntityMeta targetMeta;

	public EmberModelEntityCollectionPropertyPopulator(EntityCollectionPropertyMeta aMeta) throws Exception
	{
		super(aMeta);

		if (!(Collection.class.isAssignableFrom(this.propertyDescriptor.getPropertyType()) || Map.class.isAssignableFrom(this.propertyDescriptor
			.getPropertyType())))
		{
			throw new IncorrectEntityStructureException("The property: " + this.getParameterName() + " doesn't represent a Collection or a Map!");
		}
		collectionType = (Class<Collection<?>>) this.propertyDescriptor.getPropertyType();
		this.meta = aMeta;

		try
		{
			this.targetMeta = (ModelEntityMeta) Metas.getEntityMeta(this.meta.getTargetType());
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
		return this.targetMeta.getModelClass().getSimpleName();
	}

	@Override
	public String getTargetTypeStringForm()
	{
		String objForm = this.getTargetTypeObjectForm();
		return objForm.substring(0,1).toLowerCase() + objForm.substring(1);
	}

}