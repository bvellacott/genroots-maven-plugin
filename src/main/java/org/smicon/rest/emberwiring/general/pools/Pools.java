package org.smicon.rest.emberwiring.general.pools;


public final class Pools
{
	public static final MustacheFactoryPool mustacheFactoryPool = new MustacheFactoryPool();

	public static final ReflectionsPool reflectionsPool = new ReflectionsPool();

//	public static ObjectPool<EmberModelTypeConfigurationI> emberModelTypeConfigurations = new GenericObjectPool<EmberModelTypeConfigurationI>(
//		DefaultingFactoryStaticFactory.newFactory(new DefaultingFactoryConfigurationI<EmberModelTypeConfigurationI, EmberModelTypeConfigurationI>() {
//
//			@Override
//			public EmberModelTypeConfigurationI getDefault() throws Exception
//			{
//				return null;
//			}
//
//			@Override
//			public Class<EmberModelTypeConfigurationI> getMutableType() throws Exception
//			{
//				return null;
//			}
//
//		}));

}
