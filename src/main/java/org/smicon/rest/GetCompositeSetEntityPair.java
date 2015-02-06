package org.smicon.rest;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import org.smicon.rest.exceptions.IncorrectIdStructureException;

public class GetCompositeSetEntityPair 
extends 
GetSetPair<Member, Method> {
	
	
	public GetCompositeSetEntityPair(Member aGetter, Method aSetter, String aEntityVariableName, String aCompositeVariableName) throws IncorrectIdStructureException {
		super(aGetter, aSetter, aEntityVariableName, aCompositeVariableName);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(j.join(new Object[]{ this.getEntityVariableName(), ".", this.setter.getName(), "(" , this.getCompositeVariableName(), "."}));
		
		if(this.getter instanceof Method) {
			Method getterMethod = (Method)this.getter;
			
			sb.append(j.join(new Object[]{ getterMethod, "());\n" }));
		}
		else if(this.getter instanceof Field) {
			Field field = (Field)this.getter;
			
			sb.append(j.join(new Object[]{ field.getName(), ");\n" }));
		}
		
		return sb.toString();
	}
	
}
