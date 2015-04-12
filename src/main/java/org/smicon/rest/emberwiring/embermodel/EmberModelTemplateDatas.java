package org.smicon.rest.emberwiring.embermodel;

import java.util.ArrayList;
import java.util.List;

import org.smicon.rest.emberwiring.metas.ModelMetaI;

public final class EmberModelTemplateDatas
{
	public static Object createEmberModelTemplateData(final ModelMetaI aMeta, final EmberModelTypeConfigurationI aConfiguration) throws Exception
	{
		Object data = new Object() {
		    public Object className = aMeta.getModelClass().getSimpleName();
		    public Object simpleProperties = createSimpleProperties(aMeta, aConfiguration);
		    public Object entityPropertiesWithSimpleIds = createPropertiesWithSimpleIds(aMeta);
		    public Object entityPropertiesWithEmbeddedIds = createPropertiesWithEmbeddedIds(aMeta);
		    public Object entityPropertiesWithCompositeIds = createPropertiesWithCompositeIds(aMeta);
		    public Object entityCollectionProperties = createModelCollectionProperties(aMeta);
		};
		
		return data;
	}
	
	public static Object createSimpleProperties(ModelMetaI aMeta, final EmberModelTypeConfigurationI aConfiguration) {
		final List props = new ArrayList();
		for(final String propName : aMeta.getSimpleProperties().keySet()) {
			if(aMeta.getIdPathParameterMetas().containsKey(propName)) continue;
			if(aMeta.getEmbeddedIdPathParameterMetas().containsKey(propName)) continue;
			final Class propType = aMeta.getSimpleProperties().get(propName).getPropertyDescriptor().getPropertyType();
			props.add(new Object() {
	            public Object parameterName = propName;
	            public Object jsType = getEmberJsType(propType, aConfiguration);
		    });
		}
		for(final String propName : aMeta.getSimpleCollectionProperties().keySet()) {
			final Class propType = aMeta.getSimpleCollectionProperties().get(propName).getPropertyDescriptor().getPropertyType();
			props.add(new Object() {
	            public Object parameterName = propName;
	            public Object jsType = getEmberJsType(propType, aConfiguration);
		    });
		}
		return new Object() { public Object properties = props; };
	}
	
	public static final Object createPropertiesWithSimpleIds(ModelMetaI aMeta) {
		final List props = new ArrayList();
		for(final String propName : aMeta.getModelPropertiesWithSimpleIds().keySet()) {
			final Class propType = aMeta.getModelPropertiesWithSimpleIds().get(propName).getPropertyDescriptor().getPropertyType();
			props.add(new Object() {
	            public Object parameterName = propName;
	            public Object targetTypeStringForm = getJsTypeStringForm(propType.getSimpleName());
		    });
		}

		return new Object() { public Object properties = props; };
	}

	public static final Object createPropertiesWithEmbeddedIds(ModelMetaI aMeta) {
		final List props = new ArrayList();
		for(final String propName : aMeta.getModelPropertiesWithEmbeddedIds().keySet()) {
			final Class propType = aMeta.getModelPropertiesWithEmbeddedIds().get(propName).getPropertyDescriptor().getPropertyType();
			props.add(new Object() {
	            public Object parameterName = propName;
	            public Object targetTypeStringForm = getJsTypeStringForm(propType.getSimpleName());
		    });
		}

		return new Object() { public Object properties = props; };
	}

	public static final Object createPropertiesWithCompositeIds(ModelMetaI aMeta) {
		final List props = new ArrayList();
		for(final String propName : aMeta.getModelPropertiesWithCompositeIds().keySet()) {
			final Class propType = aMeta.getModelPropertiesWithCompositeIds().get(propName).getPropertyDescriptor().getPropertyType();
			props.add(new Object() {
	            public Object parameterName = propName;
	            public Object targetTypeStringForm = getJsTypeStringForm(propType.getSimpleName());
		    });
		}

		return new Object() { public Object properties = props; };
	}

	public static final Object createModelCollectionProperties(ModelMetaI aMeta) {
		final List props = new ArrayList();
		for(final String propName : aMeta.getModelCollectionProperties().keySet()) {
			final Class targerType = aMeta.getModelCollectionProperties().get(propName).getTargetMeta().getModelClass();
			props.add(new Object() {
	            public Object parameterName = propName;
	            public Object targetTypeStringForm = getJsTypeStringForm(targerType.getSimpleName());
		    });
		}

		return new Object() { public Object properties = props; };
	}

	public static String getEmberJsType(Class<?> aJavaType, EmberModelTypeConfigurationI aConfiguration)
	{
	    if(aConfiguration.getJavaTypesRepresentingEmberBooleans().contains(aJavaType))
	    {
	    	return "boolean";
	    }
	    if(aConfiguration.getJavaTypesRepresentingEmberNumbers().contains(aJavaType))
	    {
	    	return "number";
	    }
	    if(aConfiguration.getJavaTypesRepresentingEmberDates().contains(aJavaType))
	    {
	    	return "date";
	    }
	    if(aConfiguration.getJavaTypesRepresentingEmberStrings().contains(aJavaType))
	    {
	    	return "string";
	    }
	    for(Object o : aConfiguration.getJavaTypesRepresentingEmberArrays())
	    {
	    	Class<?> cls = (Class<?>)o;
	    	if(cls.isAssignableFrom(aJavaType)) return "array" ;
	    }
	    
	    return "object";
	}
	
	public static String getJsTypeStringForm(String aObjectForm)
	{
		return aObjectForm.substring(0,1).toLowerCase() + aObjectForm.substring(1);
	}
	
}
