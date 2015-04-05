package org.smicon.rest.emberwiring.general.pools;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.reflections.Reflections;
import org.smicon.rest.emberwiring.general.factories.ReflectionsFactory;

public class ReflectionsPool
extends
GenericKeyedObjectPool<String, Reflections>
{
	
	public ReflectionsPool(GenericKeyedObjectPoolConfig config)
	{
		super(new ReflectionsFactory(), config);
	}

	public ReflectionsPool()
	{
		super(new ReflectionsFactory());
	}
	
}
