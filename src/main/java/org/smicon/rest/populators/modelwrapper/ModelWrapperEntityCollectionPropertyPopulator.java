package org.smicon.rest.populators.modelwrapper;

import java.util.Collection;
import java.util.Map;

import org.smicon.rest.exceptions.IncorrectEntityStructureException;
import org.smicon.rest.metas.EntityCollectionPropertyMeta;
import org.smicon.rest.metas.Metas;
import org.smicon.rest.metas.ModelEntityMeta;

public class ModelWrapperEntityCollectionPropertyPopulator
extends
ModelWrapperSimplePropertyPopulator
{

	private Class<Collection<?>> collectionType;
	private EntityCollectionPropertyMeta meta;
	private ModelEntityMeta targetMeta;

	public ModelWrapperEntityCollectionPropertyPopulator(EntityCollectionPropertyMeta aMeta) throws Exception
	{
		super(aMeta.getPropertyDescriptor());

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

	public String getPlural() throws Exception
	{
		return this.targetMeta.getPlural();
	}

	public String getTargetType()
	{
		return this.meta.getTargetType().getSimpleName();
	}

}
