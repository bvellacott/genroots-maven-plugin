package org.smicon.rest.emberwiring.general.factories;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public final class DefaultingKeyedFactoryStaticFactory
{

	public static <K, I, M extends I> KeyedPooledObjectFactory<K, M> newFactory(final DefaultingFactoryConfigurationI<I, M> aConfiguration) throws Exception
	{
		DefaultingFactoryFunctions.validateConfiguration(aConfiguration);
		
		KeyedPooledObjectFactory<K, M> factory = new BaseKeyedPooledObjectFactory<K, M>(){

			@Override
			public M create(K aKey) throws Exception
			{
				return aConfiguration.getMutableType().newInstance();
			}

			@Override
			public PooledObject<M> wrap(M aMutable)
			{
				return new DefaultPooledObject<M>(aMutable);
			}

			@Override
			public void activateObject(K aKey, PooledObject<M> p) throws Exception
			{
				super.activateObject(aKey, p);
				DefaultingFactoryFunctions.formatInstance(aConfiguration.getDefault(), p.getObject());
			}

			@Override
			public void passivateObject(K aKey, PooledObject<M> p) throws Exception
			{
				super.passivateObject(aKey, p);
				DefaultingFactoryFunctions.enullInstance(p.getObject());
			}

		};
		
		return factory;
	}
	
}
