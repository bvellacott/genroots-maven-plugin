package org.smicon.rest.emberwiring.general.factories;

public interface KeyedFactoryI<K, V, C>
{
	
	C getConfiguration();
	
	boolean isCacheing();
	
	boolean isInitialisingAfterCacheing();
	
	V create(K aKey) throws Exception;
	
}
