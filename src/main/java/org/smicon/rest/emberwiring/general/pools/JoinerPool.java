package org.smicon.rest.emberwiring.general.pools;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.smicon.rest.emberwiring.general.factories.JoinerFactory;

import com.google.common.base.Joiner;

public class JoinerPool
extends
GenericKeyedObjectPool<String, Joiner>
{
	
	public JoinerPool(GenericKeyedObjectPoolConfig config)
	{
		super(new JoinerFactory(), config);
	}

	public JoinerPool()
	{
		super(new JoinerFactory());
	}
	
}
