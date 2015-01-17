package org.smicon.rest.metas;

import java.beans.PropertyDescriptor;

public class EntityPropertyMeta {
	
	private EntityMeta parentMeta;
	private EntityMeta childMeta;
	private PropertyDescriptor propertyDescriptor;
	
	public EntityPropertyMeta(EntityMeta aParentMeta, EntityMeta aChildMeta, PropertyDescriptor aPropertyDescriptor) {
		this.parentMeta = aParentMeta;
		this.childMeta = aChildMeta;
		this.propertyDescriptor = aPropertyDescriptor;
	}

	public EntityMeta getParentMeta() {
		return parentMeta;
	}

	public EntityMeta getChildMeta() {
		return childMeta;
	}

	public PropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}
	
}
