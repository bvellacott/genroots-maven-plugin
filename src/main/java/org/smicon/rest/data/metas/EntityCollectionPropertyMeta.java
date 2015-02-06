package org.smicon.rest.data.metas;

import java.beans.PropertyDescriptor;

/**
 * @author benjamin
 *
 */
public class EntityCollectionPropertyMeta
extends
EntityPropertyMeta
{
	
	private Class<?> targetClass;

	public EntityCollectionPropertyMeta(EntityMeta aParentMeta, EntityMeta aChildMeta, PropertyDescriptor aPropertyDescriptor, Class<?> aTargetClass)
	{
		super(aParentMeta, aChildMeta, aPropertyDescriptor);
		
		this.targetClass = aTargetClass;
	}

	public Class<?> getTargetType()
	{
		return this.targetClass;
	}

}
