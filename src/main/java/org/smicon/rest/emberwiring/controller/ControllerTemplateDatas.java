package org.smicon.rest.emberwiring.controller;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.GeneratedValue;

import org.smicon.rest.emberwiring.general.GeneralPools;
import org.smicon.rest.emberwiring.metas.ModelMetaI;
import org.smicon.rest.emberwiring.metas.ModelPropertyMetaI;
import org.smicon.rest.emberwiring.metas.SimplePropertyMetaI;
import org.smicon.rest.emberwiring.types.id.IdTypeEnum;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.primitives.Primitives;

public final class ControllerTemplateDatas
{

	public static String createIdPath(final ModelMetaI aMeta, String aUrlDelimiter, boolean aIncludeGenerated) throws Exception {
		StringBuilder sb = GeneralPools.string_builder_pool.borrowObject();
		Joiner j = GeneralPools.joiner_pool.borrowObject("");
		
		if(aMeta.getIdType() == IdTypeEnum.embedded) {
			boolean isFirst = true;
			BeanInfo idbi = Introspector.getBeanInfo(aMeta.getIdClass());
			Set<String> nameSet = new HashSet();
			for(PropertyDescriptor pd : idbi.getPropertyDescriptors()) {
				if(pd.getName().equals("class")) continue;
				nameSet.add(pd.getName());
				Field f = aMeta.getIdClass().getDeclaredField(pd.getName());
				boolean isGenerated = pd.getReadMethod().getAnnotation(GeneratedValue.class) != null ||
					(f != null &&f.getAnnotation(GeneratedValue.class) != null);
				if(!aIncludeGenerated && isGenerated) continue;
				if(isFirst) {
					sb.append(j.join("/{", pd.getName(), "}"));
					isFirst = false;
				}
				else
					sb.append(j.join(aUrlDelimiter, "{", pd.getName(), "}"));
			}
			for(Field f : aMeta.getIdClass().getDeclaredFields()) {
				if(nameSet.contains(f.getName())) continue;
				boolean isGenerated = f.getAnnotation(GeneratedValue.class) != null;
				if(!aIncludeGenerated && isGenerated) continue;
				if(isFirst) {
					sb.append(j.join("/{", f.getName(), "}"));
					isFirst = false;
				}
				else
					sb.append(j.join(aUrlDelimiter, "{", f.getName(), "}"));
			}
		}
		else {
			boolean isFirst = true;
			for(String idName : aMeta.getIdPathParameterMetas().keySet()) {
				SimplePropertyMetaI meta = aMeta.getIdPathParameterMetas().get(idName);
				if(!aIncludeGenerated && meta.isGenerated()) continue;
				if(isFirst) {
					sb.append(j.join("/{", idName, "}"));
					isFirst = false;
				}
				else
					sb.append(j.join(aUrlDelimiter, "{", idName, "}"));
			}
		}
		String result = sb.toString();
		GeneralPools.string_builder_pool.returnObject(sb);
		GeneralPools.joiner_pool.returnObject("", j);
		return result;
	}
	
	public static Object createIdMethodParameters(final ModelMetaI aMeta, boolean aIncludeGenerated) throws Exception {
		StringBuilder sb = GeneralPools.string_builder_pool.borrowObject();
		Joiner j = GeneralPools.joiner_pool.borrowObject("");
		
		if(aMeta.getIdType() == IdTypeEnum.embedded) {
			boolean isFirst = true;
			BeanInfo idbi = Introspector.getBeanInfo(aMeta.getIdClass());
			Set<String> nameSet = new HashSet();
			for(PropertyDescriptor pd : idbi.getPropertyDescriptors()) {
				if(pd.getName().equals("class")) continue;
				nameSet.add(pd.getName());
				Field f = aMeta.getIdClass().getDeclaredField(pd.getName());
				boolean isGenerated = pd.getReadMethod().getAnnotation(GeneratedValue.class) != null ||
					(f != null &&f.getAnnotation(GeneratedValue.class) != null);
				if(!aIncludeGenerated && isGenerated) continue;
				if(isFirst) {
					sb.append(j.join("@PathParam(\"", pd.getName(), "\") ", pd.getPropertyType().getSimpleName(), " ", pd.getName()));
					isFirst = false;
				}
				else
					sb.append(j.join(", @PathParam(\"", pd.getName(), "\") ", pd.getPropertyType().getSimpleName(), " ", pd.getName()));
			}
			for(Field f : aMeta.getIdClass().getDeclaredFields()) {
				if(nameSet.contains(f.getName())) continue;
				boolean isGenerated = f.getAnnotation(GeneratedValue.class) != null;
				if(!aIncludeGenerated && isGenerated) continue;
				if(isFirst) {
					sb.append(j.join("@PathParam(\"", f.getName(), "\") ", f.getType().getSimpleName(), " ", f.getName()));
					isFirst = false;
				}
				else
					sb.append(j.join(", @PathParam(\"", f.getName(), "\") ", f.getType().getSimpleName(), " ", f.getName()));
			}
		}
		else {
			boolean isFirst = true;
			for(String idName : aMeta.getIdPathParameterMetas().keySet()) {
				SimplePropertyMetaI meta = aMeta.getIdPathParameterMetas().get(idName);
				String idType = meta.getPropertyDescriptor().getPropertyType().getSimpleName();
				if(!aIncludeGenerated && meta.isGenerated()) continue;
				if(isFirst) {
					sb.append(j.join("@PathParam(\"", idName, "\") ", idType, " ", idName));
					isFirst = false;
				}
				else
					sb.append(j.join(", @PathParam(\"", idName, "\") ", idType, " ", idName));
			}
		}
		String result = sb.toString();
		GeneralPools.string_builder_pool.returnObject(sb);
		GeneralPools.joiner_pool.returnObject("", j);
		return result;
	}
	
	public static Object createHasNonGenIds(ModelMetaI aMeta) throws Exception{
		boolean has = false;
		
		if(aMeta.getIdType() == IdTypeEnum.embedded) {
			boolean isFirst = true;
			BeanInfo idbi = Introspector.getBeanInfo(aMeta.getIdClass());
			Set<String> nameSet = new HashSet();
			for(PropertyDescriptor pd : idbi.getPropertyDescriptors()) {
				if(pd.getName().equals("class")) continue;
				nameSet.add(pd.getName());
				Field f = aMeta.getIdClass().getDeclaredField(pd.getName());
				boolean isGenerated = pd.getReadMethod().getAnnotation(GeneratedValue.class) != null ||
					(f != null &&f.getAnnotation(GeneratedValue.class) != null);
				if(!isGenerated) {
					has = true;
					break;
				}
			}
			for(Field f : aMeta.getIdClass().getDeclaredFields()) {
				if(nameSet.contains(f.getName())) continue;
				boolean isGenerated = f.getAnnotation(GeneratedValue.class) != null;
				if(!isGenerated) {
					has = true;
					break;
				}
			}
		}
		else {
			boolean isFirst = true;
			for(String idName : aMeta.getIdPathParameterMetas().keySet()) {
				SimplePropertyMetaI meta = aMeta.getIdPathParameterMetas().get(idName);
				String idType = meta.getPropertyDescriptor().getPropertyType().getSimpleName();
				if(!meta.isGenerated()) {
					has = true;
					break;
				}
			}
		}
		if(has) return new Object();
		return null;
	}
	
	public static Object createIdMethodAttributes(final ModelMetaI aMeta, boolean aIncludeGenerated) throws Exception {
		StringBuilder sb = GeneralPools.string_builder_pool.borrowObject();
		
		boolean isFirst = true;
		for(String idName : aMeta.getIdPathParameterMetas().keySet()) {
			SimplePropertyMetaI meta = aMeta.getIdPathParameterMetas().get(idName);
			if(!aIncludeGenerated && meta.isGenerated()) continue;
			if(isFirst) {
				sb.append(idName);
				isFirst = false;
			}
			else
				sb.append(", ").append(idName);
		}
		String result = sb.toString();
		GeneralPools.string_builder_pool.returnObject(sb);
		return result;
	}
	
	public static Object createSimpleProperties(Map<String, SimplePropertyMetaI> aSimplePropertyMap, Map<String, SimplePropertyMetaI> aSimpleCollectionsPropertyMap) {
		List props = new ArrayList();
		for(String propName : aSimplePropertyMap.keySet()) 
			props.add(createSimpleProperty(aSimplePropertyMap.get(propName)));
		for(String propName : aSimpleCollectionsPropertyMap.keySet())
			props.add(createSimpleProperty(aSimpleCollectionsPropertyMap.get(propName)));
		return props;
	}
	
	public static Object createSimpleProperty(SimplePropertyMetaI aMeta) {
		final PropertyDescriptor pd = aMeta.getPropertyDescriptor();
		return new Object() {
            public Object type = pd.getPropertyType().getSimpleName();
            public Object getterName = pd.getReadMethod().getName();
            public Object setterName = pd.getWriteMethod().getName();
            public Object name = pd.getName();
        };
	}
	
	public static Object createModelPropertiesWithSimpleIds(Map<String, ModelPropertyMetaI> aPropertyMap) {
		List props = new ArrayList();
		for(String propName : aPropertyMap.keySet()) 
			props.add(createModelPropertyWithSimpleId(aPropertyMap.get(propName)));
		return props;
	}
	
	public static Object createModelPropertyWithSimpleId(ModelPropertyMetaI aMeta) {
		final PropertyDescriptor pd = aMeta.getPropertyDescriptor();
		final ModelMetaI mm = aMeta.getTargetMeta();
		final PropertyDescriptor idpd = mm.getIdPathParameterMetas().get(mm.getSingularIdName()).getPropertyDescriptor();
		return new Object() {
            public Object name = pd.getName();
            public Object type = pd.getPropertyType().getSimpleName();
            public Object setterName = pd.getWriteMethod().getName();
            public Object getterName = pd.getReadMethod().getName();
            public Object idName = mm.getSingularIdName();
            public Object idType = mm.getIdClass().getSimpleName();
            public Object idSetterName = idpd.getWriteMethod().getName();
            public Object idGetterName = idpd.getReadMethod().getName();
        };
	}
	
	public static Object createModelPropertiesWithEmbeddedIds(Map<String, ModelPropertyMetaI> aPropertyMap, String aUrlDelimiter) throws Exception {
		List props = new ArrayList();
		for(String propName : aPropertyMap.keySet()) 
			props.add(createModelPropertyWithEmbeddedId(aPropertyMap.get(propName), aUrlDelimiter));
		return props;
	}
	
	public static Object createModelPropertyWithEmbeddedId(ModelPropertyMetaI aMeta, final String aUrlDelimiter) throws Exception {
		final PropertyDescriptor pd = aMeta.getPropertyDescriptor();
		final ModelMetaI mm = aMeta.getTargetMeta();
		final PropertyDescriptor idpd = mm.getEmbeddedIdPathParameterMetas().get(mm.getSingularIdName()).getPropertyDescriptor();
		return new Object() {
            public String name = pd.getName();
            public Object type = pd.getPropertyType().getSimpleName();
            public Object idType = idpd.getPropertyType().getSimpleName();
            public Object idName = idpd.getName();
            public Object getterName = pd.getReadMethod().getName();
            public Object setterName = pd.getWriteMethod().getName();
            public Object idGetterName = idpd.getReadMethod().getName();
            public Object idSetterName = idpd.getWriteMethod().getName();
            public String serialisedId = idpd.getName() + "Serial";
            public Object urlDelimiter = aUrlDelimiter;
            public Object idProperties = createIdProperties(idpd.getPropertyType());
            public Object idSerialiseLine = createEmbeddedIdSerialiseLine(idpd, aUrlDelimiter);
       };
	}
	
	public static Object createIdProperties(Class aIdType) throws Exception {
		List properties = new ArrayList();
		Set<String> nameSet = new HashSet();
		BeanInfo bi = Introspector.getBeanInfo(aIdType, Introspector.USE_ALL_BEANINFO);
		int index = 0;
		for(final PropertyDescriptor pd : bi.getPropertyDescriptors()) {
			if(pd.getName().equals("class")) continue;
			nameSet.add(pd.getName());
			final int finalIndex = index;
			properties.add(new Object(){
                public Object propertyName = pd.getName();
                public Object propertyType = pd.getPropertyType().getSimpleName();
                public int index = finalIndex;
			});
			index++;
		}
		for(final Field f : aIdType.getDeclaredFields()) {
			if(nameSet.contains(f.getName())) continue;
			nameSet.add(f.getName());
			final int finalIndex = index;
			properties.add(new Object(){
                public Object propertyName = f.getName();
                public Object propertyType = f.getType().getSimpleName();
                public int index = finalIndex;
			});
			index++;
		}
		return properties;
	}
	
	public static Object createEmbeddedIdSerialiseLine(PropertyDescriptor aIdDescriptor, String aUrlDelimiter) throws Exception {
		Joiner j = GeneralPools.joiner_pool.borrowObject("");
		StringBuilder sb = GeneralPools.string_builder_pool.borrowObject();
		String idName = aIdDescriptor.getName();
		Class idType = aIdDescriptor.getPropertyType();
		
		sb.append("(new StringBuilder())");
		boolean hasPrevious = false;
		Set<String> nameSet = new HashSet();
		BeanInfo bi = Introspector.getBeanInfo(idType, Introspector.USE_ALL_BEANINFO);
		for(PropertyDescriptor pd : bi.getPropertyDescriptors()) {
			if(pd.getName().equals("class")) continue;
			nameSet.add(pd.getName());
			if(hasPrevious) sb.append(j.join(".append(\"", aUrlDelimiter, "\")"));
			hasPrevious = true;
			sb.append(j.join(".append(", idName, ".", pd.getReadMethod().getName(), "())"));
		}
		for(Field f : idType.getDeclaredFields()) {
			if(nameSet.contains(f.getName())) continue;
			nameSet.add(f.getName());
			if(hasPrevious) sb.append(j.join(".append(\"", aUrlDelimiter, "\")"));
			hasPrevious = true;
			sb.append(j.join(".append(", idName, ".", f.getName(), ")"));
		}
		sb.append(".toString()");
		String line = sb.toString();
		GeneralPools.joiner_pool.returnObject("", j);
		GeneralPools.string_builder_pool.returnObject(sb);
		return line;
	}
	
	public static Object createModelPropertiesWithCompositeIds(Map<String, ModelPropertyMetaI> aPropertyMap, String aUrlDelimiter) throws Exception {
		List props = new ArrayList();
		for(String propName : aPropertyMap.keySet()) 
			props.add(createModelPropertyWithCompositeId(aPropertyMap.get(propName), aUrlDelimiter));
		return props;
	}
	
	public static Object createModelPropertyWithCompositeId(ModelPropertyMetaI aMeta, final String aUrlDelimiter) throws Exception {
		final PropertyDescriptor pd = aMeta.getPropertyDescriptor();
		final ModelMetaI mm = aMeta.getTargetMeta();
		return new Object() {
            public String name = pd.getName();
            public Object type = pd.getPropertyType().getSimpleName();
            public Object getterName = pd.getReadMethod().getName();
            public Object setterName = pd.getWriteMethod().getName();
            public String serialisedId = mm.getSingularIdName();
            public Object urlDelimiter = aUrlDelimiter;
            public Object idProperties = createCompositeIdProperties(mm.getIdPathParameterMetas());
            public Object idSerialiseLine = createCompositeIdSerialiseLine(mm.getIdPathParameterMetas(), pd.getName(), aUrlDelimiter);
       };
	}
	
	public static Object createCompositeIdProperties(Map<String, SimplePropertyMetaI> aIdPropertyMap) throws Exception {
		List properties = new ArrayList();
		int index = 0;
		for(String idName : aIdPropertyMap.keySet()) {
			final int finalIndex = index;
			final PropertyDescriptor pd = aIdPropertyMap.get(idName).getPropertyDescriptor();
			properties.add(new Object(){
                public Object propertySetterName = pd.getWriteMethod().getName();
                public Object propertyType = pd.getPropertyType().getSimpleName();
                public int index = finalIndex;
			});
			index++;
		}
		return properties;
	}
	
	public static Object createCompositeIdSerialiseLine(Map<String, SimplePropertyMetaI> aIdPropertyMap, String aModelName, String aUrlDelimiter) throws Exception {
		Joiner j = GeneralPools.joiner_pool.borrowObject("");
		StringBuilder sb = GeneralPools.string_builder_pool.borrowObject();
		
		sb.append("(new StringBuilder())");
		boolean hasPrevious = false;
		for(String idName : aIdPropertyMap.keySet()) {
			if(hasPrevious) sb.append(j.join(".append(\"", aUrlDelimiter, "\")"));
			hasPrevious = true;
			sb.append(j.join(".append(", aModelName, ".", aIdPropertyMap.get(idName).getPropertyDescriptor().getReadMethod().getName(), "())"));
		}
		sb.append(".toString()");
		String line = sb.toString();
		GeneralPools.joiner_pool.returnObject("", j);
		GeneralPools.string_builder_pool.returnObject(sb);
		return line;
	}
	
	public static Object createModelCollecionProperties(Map<String, ModelPropertyMetaI> aPropertyMap, String aUrlDelimiter) throws Exception {
		List props = new ArrayList();
		for(String propName : aPropertyMap.keySet()) 
			props.add(createModelCollecionProperty(aPropertyMap.get(propName), aUrlDelimiter));
		return props;
	}
	
	public static Object createModelCollecionProperty(final ModelPropertyMetaI aMeta, final String aControllerPostfix) throws Exception {
		final PropertyDescriptor pd = aMeta.getPropertyDescriptor();
		final ModelMetaI mm = aMeta.getTargetMeta();
		
		boolean isMap = Map.class.isAssignableFrom(pd.getPropertyType());
		final Object isMapVal = isMap ? new Object() : null;
		final Object keyTypeVal = isMap ? ((ParameterizedType)pd.getReadMethod().getGenericReturnType()).getActualTypeArguments()[0].getTypeName() : null;

		return new Object() {
            public String name = pd.getName();
            public Object type = pd.getPropertyType().getSimpleName();
            public Object getterName = pd.getReadMethod().getName();
            public Object setterName = pd.getWriteMethod().getName();
            public Object modelOuterWrapperType = mm.getModelClass().getSimpleName() + "OW";
//            public Object idMethodParameters = createIdMethodParameters(mm, true);
            public Object controllerType = mm.getModelClass().getSimpleName() + aControllerPostfix;
//            public Object idMethodAttributes = createIdMethodAttributes(mm, true);
            public Object modelInnerWrapperType = mm.getModelClass().getSimpleName() + "IW";
            public Object wrappedName = mm.getModelClass().getSimpleName() + "s";
            public Object modelType = mm.getModelClass().getSimpleName();
            public Object instanceType = getCollectionInstanceType(pd.getPropertyType()).getName();
            public Object loopVariable = mm.getModelClass().getSimpleName() + "var";
//            public Object singular = mm.getModelClass().getSimpleName().toLowerCase();
            public Object modelPlural = mm.getModelAnnotation().plural();
            public Object isMap = isMapVal;
            public Object keyType = keyTypeVal;
       };
 	}
	
	public static Object createModelSimpleIdSetters(final ModelMetaI aMeta) {
		if(aMeta.getIdType() != IdTypeEnum.simple) return null;
		final SimplePropertyMetaI meta = aMeta.getIdPathParameterMetas().get(aMeta.getSingularIdName());
		final PropertyDescriptor desc = meta.getPropertyDescriptor();
		final Object property = new Object() {
			public Object setterName = desc.getWriteMethod().getName();
            public Object idPropertyName = aMeta.getSingularIdName();
        };
		final Object nonGenerated = meta.isGenerated() ? null : property;
			
		return new Object() {
	        public Object nonGeneratedIdProperty = nonGenerated;
	        public Object idProperty = property;
	    };
	}
	
	public static Object createModelEmbeddedIdSetters(ModelMetaI aMeta) throws Exception {
		if(aMeta.getIdType() != IdTypeEnum.embedded) return null;
		final SimplePropertyMetaI meta = aMeta.getEmbeddedIdPathParameterMetas().get(aMeta.getSingularIdName());
		final PropertyDescriptor desc = meta.getPropertyDescriptor();
		
		Set nameSet = new HashSet();
		final List idProps = new ArrayList();
		final List nonGenIdProps = new ArrayList();
		for(final PropertyDescriptor pd : Introspector.getBeanInfo(desc.getPropertyType()).getPropertyDescriptors()) {
			if(pd.getName() == "class" || pd.getWriteMethod() == null) continue;
			nameSet.add(pd.getName());
			Object prop = new Object() {
	            public Object setter = new Object() {
	                public Object setterName = pd.getWriteMethod().getName();
	                public Object idPropertyName = pd.getName();
	            };
	            public Object field = null;
	        };
			idProps.add(prop);
			Field f = desc.getPropertyType().getDeclaredField(pd.getName());
			if(pd.getReadMethod().getAnnotation(GeneratedValue.class) == null && 
				(f == null || f.getAnnotation(GeneratedValue.class) == null))
				nonGenIdProps.add(prop);
		}
		for(final Field f : desc.getPropertyType().getDeclaredFields()) {
			if(nameSet.contains(f.getName())) continue;
			Object prop = new Object() {
	            public Object setter = null;
	            public Object field = new Object() {
	                public Object idPropertyName = f.getName();
	            };
	        };
			idProps.add(prop);
			if(f.getAnnotation(GeneratedValue.class) == null)
				nonGenIdProps.add(prop);
		}
		
		return new Object() {
	        public Object idType = desc.getPropertyType().getSimpleName();
	        public Object idName = desc.getName();
	        public Object nonGeneratedIdProperties = nonGenIdProps;
	        public Object setterName = desc.getWriteMethod().getName();
	        public Object idProperties = idProps;    
	    };
	}
	
	public static Object createModelCompositeIdSetters(final ModelMetaI aMeta) throws Exception {
		if(aMeta.getIdType() != IdTypeEnum.composite) return null;
		BeanInfo idbi = Introspector.getBeanInfo(aMeta.getIdClass(), Introspector.USE_ALL_BEANINFO);
		
		Set nameSet = new HashSet();
		final List idProps = new ArrayList();
		final List nonGenIdProps = new ArrayList();
		for(final PropertyDescriptor pd : idbi.getPropertyDescriptors()) {
			if(pd.getName() == "class" || pd.getWriteMethod() == null) continue;
			final SimplePropertyMetaI meta = aMeta.getIdPathParameterMetas().get(pd.getName());
			
			nameSet.add(pd.getName());
			Object prop = new Object() {
                public Object setterName = pd.getWriteMethod().getName();;
                public Object idPropertyName = pd.getName();
                public Object idSetter = new Object();
                public Object idField = null;
	        };
			idProps.add(prop);
			if(!meta.isGenerated())
				nonGenIdProps.add(prop);
		}
		for(final Field f : aMeta.getIdClass().getDeclaredFields()) {
			if(nameSet.contains(f.getName())) continue;
			final SimplePropertyMetaI meta = aMeta.getIdPathParameterMetas().get(f.getName());
			
			Object prop = new Object() {
                public Object setterName = meta.getPropertyDescriptor().getWriteMethod().getName();;
                public Object idPropertyName = f.getName();
                public Object idSetter = null;
                public Object idField = new Object();
	        };
			idProps.add(prop);
			if(!meta.isGenerated())
				nonGenIdProps.add(prop);
		}
		
		return  new Object() {
	        public Object idType = aMeta.getIdClass().getSimpleName();
	        public Object idName = aMeta.getSingularIdName();
	        public Object nonGeneratedIdProperties = nonGenIdProps;
	        public Object idProperties = idProps;
	    };

	}
	
	public static Object createControllerData(final ModelMetaI aMeta, final ControllerWiringConfigurationI aConfiguration) throws Exception {
		Object controllerData = new Object() {
		    public Object packageName = aMeta.getModelClass().getPackage().getName();//aConfiguration.getPackageName();
		    public Object imports = getImports(aMeta, aConfiguration.getClassPostfix());
		    public Object plural = aMeta.getModelAnnotation().plural();
		    public Object controllerType = aMeta.getModelClass().getSimpleName() + aConfiguration.getClassPostfix();
		    public Object modelType = aMeta.getModelClass().getSimpleName();
		    public Object wrappedIdType = Primitives.wrap(aMeta.getIdClass()).getSimpleName();
		    public Object idType = aMeta.getIdClass().getSimpleName();
		    public Object persistenceUnitName = aMeta.getModelAnnotation().persistenceUnitName();
		    public Object nonGeneratedIdPath = createIdPath(aMeta, aConfiguration.getUrlIdDelimiter(), false);
		    public Object modelOuterWrapperType = aMeta.getModelClass().getSimpleName() + "OW";
		    public Object nonGeneratedIdMethodParameters = createIdMethodParameters(aMeta, false);
		    public Object hasNonGenIds = createHasNonGenIds(aMeta);
		    public Object outerWrapperVariableName = aConfiguration.getModelVariableName() + "OW";
		    public String modelInnerWrapperType = aMeta.getModelClass().getSimpleName() + "IW";
		    public Object modelVariableName = aConfiguration.getModelVariableName();
		    public String innerWrapperVariableName = aConfiguration.getModelVariableName() + "IW";
//		    public Object setSingularNonGeneratedIdLines = createSingularIdSetterLines(aMeta, false);
//		    public Object setModelNonGeneratedIdLines = createModelIdSetterLines(aMeta, innerWrapperVariableName, false);
		    public Object singular = aMeta.getModelClass().getSimpleName().toLowerCase();
		    public Object idPath = createIdPath(aMeta, aConfiguration.getUrlIdDelimiter(), true);
		    public Object idMethodParameters = createIdMethodParameters(aMeta, true);
		    public Object idMethodAttributes = createIdMethodAttributes(aMeta, true);
//		    public Object setSingularIdLines = createSingularIdSetterLines(aMeta, true);
//		    public Object singularIdName = aMeta.getSingularIdName();
//		    public Object setModelIdLines = createModelIdSetterLines(aMeta, innerWrapperVariableName, true);
		    public Object simple = createModelSimpleIdSetters(aMeta);
		    public Object embedded = createModelEmbeddedIdSetters(aMeta);
		    public Object composite = createModelCompositeIdSetters(aMeta);
		    public Object modelWrapper = new Object() {
		        public Object simpleProperties = new Object() {
		            public Object properties = createSimpleProperties(aMeta.getSimpleProperties(), aMeta.getSimpleCollectionProperties());
		        };
		        public Object modelPropertiesWithSimpleIds = new Object() {
		            public Object properties = createModelPropertiesWithSimpleIds(aMeta.getModelPropertiesWithSimpleIds());
		        };
		        public Object modelPropertiesWithEmbeddedIds = new Object() {
		            public Object properties = createModelPropertiesWithEmbeddedIds(aMeta.getModelPropertiesWithEmbeddedIds(), aConfiguration.getUrlIdDelimiter());
		        };
		        public Object modelPropertiesWithCompositeIds = new Object() {
		            public Object properties = createModelPropertiesWithCompositeIds(aMeta.getModelPropertiesWithCompositeIds(), aConfiguration.getUrlIdDelimiter());
		        };
		        public Object modelCollectionProperties = new Object() {
		            public Object properties = createModelCollecionProperties(aMeta.getModelCollectionProperties(), aConfiguration.getClassPostfix());		    
		        };
		    };
		};

		return controllerData;
	}

	public static String creatParseString(Class<?> aType, String aVariableName) throws Exception 
	{
		if(aType == String.class) return aVariableName;
	    if(aType == boolean.class || aType == Boolean.class) return "new Boolean(" + aVariableName + ")";
	    if(aType == byte.class || aType == Byte.class) return "new Byte(" + aVariableName + ")";
	    if(aType == char.class || aType == Character.class) return "(Character)" + aVariableName + ".charAt(0)";
	    if(aType == double.class || aType == Double.class) return "new Double(" + aVariableName + ")";
	    if(aType == float.class || aType == Float.class) return "new Float(" + aVariableName + ")";
	    if(aType == int.class || aType == Integer.class) return "new Integer(" + aVariableName + ")";
	    if(aType == long.class || aType == Long.class) return "new Long(" + aVariableName + ")";
	    if(aType == short.class || aType == Short.class) return "new Short(" + aVariableName + ")";
	    if(aType == java.util.Date.class) return "new java.util.Date(" + aVariableName + ")";
	    if(aType == java.sql.Date.class) return "java.sql.Date.valueOf(" + aVariableName + ")";
	    if(aType == BigDecimal.class) {
	    	DecimalFormat df = new DecimalFormat();
	    	df.setParseBigDecimal(true);
	    	return "new BigDecimal(" + aVariableName + ")";
	    }
	    if(aType == BigInteger.class) {
	    	DecimalFormat df = new DecimalFormat();
	    	df.setParseIntegerOnly(true);
	    	return "new BigInteger(" + aVariableName + ")";
	    }
	    
		throw new IllegalArgumentException("The type argument passed is not a valid simple type accepted by the EJB specification!");
	}
	
	public static Object getImports(ModelMetaI aMeta, String aControllerExtension) throws IntrospectionException
	{
		HashSet imports = new HashSet();

		addImportToCollection(imports, Primitives.wrap(aMeta.getIdClass()));
		for(PropertyDescriptor pd : Introspector.getBeanInfo(aMeta.getIdClass(), Introspector.USE_ALL_BEANINFO).getPropertyDescriptors()) {
			if(pd.getName() == "class") continue;
			addImportToCollection(imports, Primitives.wrap(pd.getPropertyType()));
		}
		for(Field f : aMeta.getIdClass().getDeclaredFields()) {
			addImportToCollection(imports, Primitives.wrap(f.getType()));
		}
		for (SimplePropertyMetaI meta : aMeta.getIdPathParameterMetas().values())
		{
			addImportToCollection(imports, Primitives.wrap(meta.getPropertyDescriptor().getPropertyType()));
		}
		for (SimplePropertyMetaI meta : aMeta.getSimpleProperties().values())
		{
			addImportToCollection(imports, Primitives.wrap(meta.getPropertyDescriptor().getPropertyType()));
		}
		for (ModelPropertyMetaI propMeta : aMeta.getModelPropertiesWithSimpleIds().values())
		{
			addImportToCollection(imports, propMeta.getTargetMeta().getModelClass());
			for (SimplePropertyMetaI propIdMeta : propMeta.getTargetMeta().getIdPathParameterMetas().values())
			{
				addImportToCollection(imports, Primitives.wrap(propIdMeta.getPropertyDescriptor().getPropertyType()));
			}
		}
		for (ModelPropertyMetaI propMeta : aMeta.getModelPropertiesWithEmbeddedIds().values())
		{
			addImportToCollection(imports, propMeta.getTargetMeta().getModelClass());
			addImportToCollection(imports, propMeta.getTargetMeta().getIdClass());
			for (SimplePropertyMetaI propIdMeta : propMeta.getTargetMeta().getEmbeddedIdPathParameterMetas().values())
			{
				addImportToCollection(imports, Primitives.wrap(propIdMeta.getPropertyDescriptor().getPropertyType()));
				for(PropertyDescriptor pd : Introspector.getBeanInfo(propIdMeta.getPropertyDescriptor().getPropertyType()).getPropertyDescriptors()) {
					addImportToCollection(imports, Primitives.wrap(pd.getPropertyType()));
				}
				for(Field f : propIdMeta.getPropertyDescriptor().getPropertyType().getDeclaredFields()) {
					addImportToCollection(imports, Primitives.wrap(f.getType()));
				}
			}
		}
		for (ModelPropertyMetaI propMeta : aMeta.getModelPropertiesWithCompositeIds().values())
		{
			addImportToCollection(imports, propMeta.getTargetMeta().getModelClass());
			addImportToCollection(imports, propMeta.getTargetMeta().getIdClass());
			for (SimplePropertyMetaI propIdMeta : propMeta.getTargetMeta().getIdPathParameterMetas().values())
			{
				addImportToCollection(imports, Primitives.wrap(propIdMeta.getPropertyDescriptor().getPropertyType()));
			}
		}
		for (ModelPropertyMetaI propMeta : aMeta.getModelCollectionProperties().values())
		{
			addImportToCollection(imports, Primitives.wrap(propMeta.getPropertyDescriptor().getPropertyType()));
			addImportToCollection(imports, Primitives.wrap(getCollectionInstanceType(propMeta.getPropertyDescriptor().getPropertyType())));
			addImportToCollection(imports, Primitives.wrap(propMeta.getTargetMeta().getModelClass()));
			addControllerStaticImportToCollection(imports, propMeta.getTargetMeta().getModelClass(), aControllerExtension);
			for (SimplePropertyMetaI propIdMeta : propMeta.getTargetMeta().getIdPathParameterMetas().values())
			{
				addImportToCollection(imports, Primitives.wrap(propIdMeta.getPropertyDescriptor().getPropertyType()));
			}
		}

		return Lists.newArrayList(imports);
	}

	public static Class getCollectionInstanceType(Class aCollectionClass) {
		if(aCollectionClass == Collection.class) return ArrayList.class;
		if(aCollectionClass == Set.class) return HashSet.class;
		if(aCollectionClass == List.class) return ArrayList.class;
		if(aCollectionClass == Map.class) return HashMap.class;
		return ArrayList.class;
	}
	
	public static void addImportToCollection(Collection aCollection, final Class<?> aType)
	{
		if (!Primitives.unwrap(aType).isPrimitive())
		{
			aCollection.add(new Object(){ public String name = aType.getName(); });
		}
	}

	public static void addControllerStaticImportToCollection(Collection aCollection, final Class<?> aType, final String aControllerExtension)
	{
		if (!Primitives.unwrap(aType).isPrimitive())
		{
			aCollection.add(new Object(){ public String name = "static " + aType.getName() + aControllerExtension + ".*"; });
		}
	}
}
