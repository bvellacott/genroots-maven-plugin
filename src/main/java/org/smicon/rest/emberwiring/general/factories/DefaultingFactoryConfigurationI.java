package org.smicon.rest.emberwiring.general.factories;


public interface DefaultingFactoryConfigurationI
<
I,
M extends I
>
{
	
	I getDefault();
	
	Class<M> getMutableType();
	
}
