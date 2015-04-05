package org.smicon.rest.emberwiring.general.factories;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.reflections.Reflections;

public class ReflectionsFactory
extends
BaseKeyedPooledObjectFactory<String, Reflections>
{
	@Override
	public Reflections create(String aPackageName)
	{
		return new Reflections(aPackageName);
	}

	@Override
	public PooledObject<Reflections> wrap(Reflections aReflections)
	{
		return new DefaultPooledObject(aReflections);
	}

}
