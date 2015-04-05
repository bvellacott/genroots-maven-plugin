package org.smicon.rest.emberwiring.metas;

import java.beans.PropertyDescriptor;

public interface ModelPropertyMetaI
{

	ModelMetaI getTargetMeta();

	PropertyDescriptor getPropertyDescriptor();
	
}
