package org.smicon.rest.emberwiring.general;

import org.smicon.rest.emberwiring.general.pools.JoinerPool;
import org.smicon.rest.emberwiring.general.pools.StringBuilderPool;

public final class GeneralPools
{
	
	public static final StringBuilderPool string_builder_pool = new StringBuilderPool();
	
	public static final JoinerPool joiner_pool = new JoinerPool();
	
}
