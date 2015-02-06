package org.smicon.rest.data.mustachetemplate.controller.modelwrapper;

import org.smicon.rest.data.populators.abstracts.TemplatePopulator;

public class IdPropertyPopulator 
extends 
TemplatePopulator 
{
	protected int index;
	protected String urlIdDelimiter;
	protected Class<?> type;
	protected String name;
	
	public IdPropertyPopulator(Class<?> aType, String aName, int aIndex, String aUrlIdDelimiter) {
		this.type = aType;
		this.name = aName;
		this.index = aIndex;
		this.urlIdDelimiter = aUrlIdDelimiter;
	}

	public String getProperty() {
		return this.name;
	}

	public String getType() {
		if(this.type == java.util.Date.class || this.type == java.sql.Date.class) {
			return this.type.getName();
		}
		return this.type.getSimpleName();
	}

	public int getIndex() {
		return index;
	}
	
	public String getUrlIdDelimiter() {
		if(this.index == 0) {
			return "";
		}
		return this.urlIdDelimiter;
	}
}
