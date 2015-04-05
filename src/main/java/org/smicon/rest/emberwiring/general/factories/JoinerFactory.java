package org.smicon.rest.emberwiring.general.factories;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.google.common.base.Joiner;

public class JoinerFactory
extends
BaseKeyedPooledObjectFactory<String, Joiner>
{
	@Override
	public Joiner create(String aSeparator)
	{
		return Joiner.on(aSeparator);
	}

	@Override
	public PooledObject<Joiner> wrap(Joiner aJoiner)
	{
		return new DefaultPooledObject<Joiner>(aJoiner);
	}

}
