package org.smicon.rest;

import java.lang.reflect.Member;

public interface GetSetPairI 
<
G extends Member,
S extends Member
>
{
	public String getEntityVariableName();

	public String getCompositeVariableName();

}
