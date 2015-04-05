package org.smicon.rest.emberwiring.metas;

import java.beans.PropertyDescriptor;

public interface SimplePropertyMetaI
{

	PropertyDescriptor getPropertyDescriptor();
	
	boolean isGenerated();
	
}
