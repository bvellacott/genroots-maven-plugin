package org.smicon.rest.emberwiring.general.factories;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class StringBuilderFactory
extends
BasePooledObjectFactory<StringBuilder>
{

	@Override
	public StringBuilder create()
	{
		return new StringBuilder();
	}

	@Override
	public PooledObject<StringBuilder> wrap(StringBuilder aStringBuilder)
	{
		return new DefaultPooledObject<StringBuilder>(aStringBuilder);
	}

	@Override
	public void passivateObject(PooledObject<StringBuilder> p) throws Exception
	{
		super.passivateObject(p);
		p.getObject().setLength(0);
	}

}
