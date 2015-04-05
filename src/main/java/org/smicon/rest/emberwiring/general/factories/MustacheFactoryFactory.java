package org.smicon.rest.emberwiring.general.factories;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;

public class MustacheFactoryFactory
extends
BaseKeyedPooledObjectFactory<String, MustacheFactory>
{
	@Override
	public MustacheFactory create(String aResourceRoot)
	{
		return new DefaultMustacheFactory(aResourceRoot);
	}

	@Override
	public PooledObject<MustacheFactory> wrap(MustacheFactory aExecutor)
	{
		return new DefaultPooledObject(aExecutor);
	}

}
