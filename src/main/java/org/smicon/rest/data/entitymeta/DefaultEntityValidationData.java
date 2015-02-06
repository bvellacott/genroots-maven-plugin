package org.smicon.rest.data.entitymeta;

import java.util.Set;

public class DefaultEntityValidationData
implements
EntityValidationDataI
{
	private static final DefaultEntityValidationData instance = new DefaultEntityValidationData();
	
	private DefaultEntityValidationData()
	{}
	
	public DefaultEntityValidationData getInstance()
	{
		return instance;
	}

	@Override
	public Set<Class> getIdTypes()
	{
		return (Set<Class>)default_id_types;
	}

	@Override
	public Set<Class> getNonPrimitiveIdTypes()
	{
		return (Set<Class>)default_non_primitive_id_types;
	}

	@Override
	public Set<Class> getPrimitiveTypes()
	{
		return (Set<Class>)default_primitive_types;
	}
	
	
}
