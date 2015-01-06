package org.smicon.rest;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import org.smicon.rest.exceptions.IncorrectIdStructureException;

import com.google.common.base.Joiner;

public abstract class GetSetPair
<
G extends Member, 
S extends Member
>
implements
GetSetPairI<G, S> {
	
	protected static final Joiner j = Joiner.on("");

	protected G getter;
	protected S setter;
	protected String entityVarName;
	protected String compositeVarName;
	
	public GetSetPair(G aGetter, S aSetter, String aEntityVariableName, String aCompositeVariableName) throws IncorrectIdStructureException
	{
		if(!(aGetter instanceof Field) && !(aGetter instanceof Method) ) {
			throw new IllegalArgumentException("A varible reader method (getter) or accessible variable is missing!");
		}
		if(!(aSetter instanceof Field) && !(aSetter instanceof Method) ) {
			throw new IllegalArgumentException("A varible reader method (setter) or accessible variable is missing!");
		}
		
		this.getter = aGetter;
		this.setter = aSetter;
		this.entityVarName = aEntityVariableName;
		this.compositeVarName = aCompositeVariableName;

		if(this.getter instanceof Method) {
			Method getterMethod = (Method)this.getter;
			
			if(this.setter instanceof Method) {
				Method setterMethod = (Method)this.setter;
				if(!getterMethod.getReturnType().isAssignableFrom(setterMethod.getParameterTypes()[0])) {
					throw new IncorrectIdStructureException("Types missmatch for the getter and setter members!");
				}
			}
			else if(this.setter instanceof Field) {
				Field field = (Field)this.setter;
				if(!getterMethod.getReturnType().isAssignableFrom(field.getType())) {
					throw new IncorrectIdStructureException("Types missmatch for the getter and setter members!");
				}
			}
		}
		else if(this.getter instanceof Field) {
			Field getterField = (Field)this.getter;
			
			if(this.setter instanceof Method) {
				Method setterMethod = (Method)this.setter;
				if(!getterField.getType().isAssignableFrom(setterMethod.getParameterTypes()[0])) {
					throw new IncorrectIdStructureException("Types missmatch for the getter and setter members!");
				}
			}
			else if(this.setter instanceof Field) {
				Field setterField = (Field)this.setter;
				if(!getterField.getType().isAssignableFrom(setterField.getType())) {
					throw new IncorrectIdStructureException("Types missmatch for the getter and setter members!");
				}
			}
			
		}
		
	}
	
	public String getEntityVariableName() {
		return entityVarName;
	}

	public String getCompositeVariableName() {
		return compositeVarName;
	}

}
