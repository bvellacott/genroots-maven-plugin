package org.smicon.rest.emberwiring.general.pools;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.smicon.rest.emberwiring.general.factories.MustacheFactoryFactory;

import com.github.mustachejava.MustacheFactory;

public class MustacheFactoryPool
extends
GenericKeyedObjectPool<String, MustacheFactory>
{
	
	public MustacheFactoryPool(GenericKeyedObjectPoolConfig config)
	{
		super(new MustacheFactoryFactory(), config);
	}

	public MustacheFactoryPool()
	{
		super(new MustacheFactoryFactory());
	}
	
}
