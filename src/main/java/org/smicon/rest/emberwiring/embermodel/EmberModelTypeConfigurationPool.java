package org.smicon.rest.emberwiring.embermodel;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.smicon.rest.emberwiring.general.factories.DefaultingFactoryConfigurationI;
import org.smicon.rest.emberwiring.general.factories.DefaultingFactoryStaticFactory;

public class EmberModelTypeConfigurationPool
extends
GenericObjectPool<DefaultEmberModelTypeConfiguration>
{

	public EmberModelTypeConfigurationPool() throws Exception
	{
		super(DefaultingFactoryStaticFactory
			.newFactory(new DefaultingFactoryConfigurationI<EmberModelTypeConfigurationI, DefaultEmberModelTypeConfiguration>() {

				EmberModelTypeConfigurationI default_instance = new DefaultEmberModelTypeConfiguration();

				@Override
				public EmberModelTypeConfigurationI getDefault()
				{
					return default_instance;
				}

				@Override
				public Class<DefaultEmberModelTypeConfiguration> getMutableType()
				{
					return DefaultEmberModelTypeConfiguration.class;
				}
			}));
	}

}
