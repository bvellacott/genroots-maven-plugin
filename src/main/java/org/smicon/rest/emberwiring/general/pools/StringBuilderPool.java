package org.smicon.rest.emberwiring.general.pools;

import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.smicon.rest.emberwiring.general.factories.StringBuilderFactory;

public class StringBuilderPool
extends
GenericObjectPool<StringBuilder>
{

	public StringBuilderPool(GenericObjectPoolConfig config, AbandonedConfig abandonedConfig)
	{
		super(new StringBuilderFactory(), config, abandonedConfig);
	}

	public StringBuilderPool(GenericObjectPoolConfig config)
	{
		super(new StringBuilderFactory(), config);
	}

	public StringBuilderPool()
	{
		super(new StringBuilderFactory());
	}

}
