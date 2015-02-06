package org.smicon.rest;

import java.lang.reflect.Member;

/**
 * Get rid of this hierarchy! Write a better mustache template and use that.
 * 
 * 
 * @author benjamin
 *
 */
public interface GetSetPairI 
<
G extends Member,
S extends Member
>
{
	public String getEntityVariableName();

	public String getCompositeVariableName();

}
