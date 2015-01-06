package org.smicon.rest;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import org.smicon.rest.exceptions.IncorrectIdStructureException;

public class GetEntitySetCompositePair 
extends
GetSetPair<Method, Member>{

	public GetEntitySetCompositePair(Method aGetter, Member aSetter, String aEntityVariableName, String aCompositeVariableName) throws IncorrectIdStructureException {
		super(aGetter, aSetter, aEntityVariableName, aCompositeVariableName);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if(this.setter instanceof Method) {
			Method setterMethod = (Method)this.setter;
			
			sb.append(j.join(new Object[]{ this.getCompositeVariableName(), ".", setterMethod.getName(), "(" , this.getEntityVariableName(), "."}));
		}
		else if(this.setter instanceof Field) {
			Field field = (Field)this.setter;
			
			sb.append(j.join(new Object[]{ this.getCompositeVariableName(), ".", field.getName(), " = " , this.getEntityVariableName(), "."}));
		}
		
		sb.append(j.join(new Object[]{ this.getter.getName(), "());\n" }));
		
		return sb.toString();
	}
	
}
