package org.smicon.rest.emberwiring.general.factories;


public interface FactoryI<I, C>
{
	
	C getConfiguration();
	
	I create();
	
}
