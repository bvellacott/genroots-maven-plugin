package org.smicon.rest.emberwiring.general.factories;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public final class DefaultingFactoryStaticFactory
{

	public static <I, M extends I> PooledObjectFactory<M> newFactory(final DefaultingFactoryConfigurationI<I, M> aConfiguration) throws Exception
	{
		DefaultingFactoryFunctions.validateConfiguration(aConfiguration);
		
		PooledObjectFactory<M> factory = new BasePooledObjectFactory<M>(){

			@Override
			public M create() throws Exception
			{
				return aConfiguration.getMutableType().newInstance();
			}

			@Override
			public PooledObject<M> wrap(M aMutable)
			{
				return new DefaultPooledObject<M>(aMutable);
			}

			@Override
			public void activateObject(PooledObject<M> p) throws Exception
			{
				super.activateObject(p);
				DefaultingFactoryFunctions.formatInstance(aConfiguration.getDefault(), p.getObject());
			}

			@Override
			public void passivateObject(PooledObject<M> p) throws Exception
			{
				super.passivateObject(p);
				DefaultingFactoryFunctions.enullInstance(p.getObject());
			}

		};
		
		return factory;
	}
	
}
