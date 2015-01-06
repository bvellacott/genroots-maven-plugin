package org.smicon.rest;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.smicon.rest.exceptions.IncorrectIdStructureException;
import org.smicon.rest.testLibs.Bean;
import org.smicon.rest.types.id.IdType;
import org.smicon.rest.types.id.IdTypeComposite;
import org.smicon.rest.types.id.IdTypeEnum;

import papu.annotations.Model;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.google.common.primitives.Primitives;

public class RouteTemplate {
	
	public static final String modelVarNameInSetter = "aModel";
	public static final String idVarNameInSetter = "aId";

	public static final Set<Class> nonPrimitiveIdTypes = 
			Sets.newHashSet(
					(Class)String.class, 
					java.util.Date.class,
					java.sql.Date.class,
					java.math.BigDecimal.class,
					java.math.BigInteger.class);
	
	private static final Joiner j = Joiner.on("");
	private static final Joiner jtt = Joiner.on("");
	
	private Class<?> modelClass;
	private BeanInfo modelBeanInfo;
	
	private Class<?> idClass;
	private BeanInfo idBeanInfo;
	
	private Class<? extends IdType> idType;
	private String singularIdName;

//	private ArrayList<Field> idFields = new ArrayList<Field>();
//	private ArrayList<Field> embeddedIdFields = new ArrayList<Field>();
//
//	private ArrayList<Method> idAccessors = new ArrayList<Method>();
//	private ArrayList<Method> embeddedIdAccessors = new ArrayList<Method>();

	private LinkedHashMap<String, EntityIdMeta> idPathParameters = new LinkedHashMap<String, EntityIdMeta>();
	private LinkedHashMap<String, EntityIdMeta> embeddedIdPathParameters = new LinkedHashMap<String, EntityIdMeta>();

	private String routeClassPostfix;
	private String plural;
	private String persistenceUnitName;
	 

	public void setModelClass(Class<?> aModelClass) throws Exception{
		this.modelClass = aModelClass;
		this.modelBeanInfo = Introspector.getBeanInfo(this.modelClass);
		Model modelAnnotation = this.modelClass.getAnnotation(Model.class);
		
		this.setPlural(modelAnnotation.plural());
		this.setPersistenceUnitName(modelAnnotation.persistenceUnitName());
		this.setRouteClassPostfix("Route");
		
		this.deduceIdStructure();

		if(this.idType == IdTypeEnum.embedded) {
			this.handleEmbeddedId(modelAnnotation);
		} else if(this.idType == IdTypeEnum.composite) {
			this.handleCompositeId(modelAnnotation);
		} else {
			this.handleSimpleId(modelAnnotation);
		}
	}

	private void deduceIdStructure() throws Exception {
		IdClass idCls = this.modelClass.getAnnotation(IdClass.class);

		this.populateIdArrays();
		this.validateEmbeddedIdUniformity();
//		this.validateSimpleIdTypes();
//		this.validateEmbeddedIdTypes();
		
		if(idCls != null) {
			this.validateCompositeIdStructure(idCls);
			this.generateSingularIdName();
		}
		else {
			if(this.containsSimpleIds()) {
				this.validateSimpleIdStructure();
			}
			else if(this.containsEmbeddedIds()) {
				this.validateEmbeddedIdStructure();
				//this.generateSingularIdName();
			}
		}
		
		this.validateIdStructureFound();
		
//		this.setIdPathParameters();
	}
	
	private void populateIdArrays() throws Exception {

		Field[] fieldArray = this.modelClass.getDeclaredFields();
		for (Field field : fieldArray) {
			this.checkAndPopulateIdField(field);
		}

		Method[] methodArray = this.modelClass.getDeclaredMethods();
		for (Method method : methodArray) {
			this.checkAndPopulateIdAccessor(method);
		}

	}

	private void checkAndPopulateIdField(Field aField) throws Exception {
		EmbeddedId embeddedId = aField.getAnnotation(EmbeddedId.class);

		if (embeddedId == null) {
			this.checkAndPopulateSimpleIdField(aField);
		} else {
//			this.embeddedIdFields.add(aField);
			this.singularIdName = aField.getName();
			PropertyDescriptor propertyDescriptor = this.getModelPropertyDescriptor(this.singularIdName, aField.getType());
			
			EntityIdMeta embeddedIdMeta = new EntityIdMeta();
			embeddedIdMeta.setFieldType(propertyDescriptor.getPropertyType());
			embeddedIdMeta.setPropertyDescriptor(propertyDescriptor);
			
			GetCompositeSetEntityPair pair = new GetCompositeSetEntityPair(
					aField, propertyDescriptor.getWriteMethod(),
					modelVarNameInSetter, idVarNameInSetter);
			embeddedIdMeta.setReadWritePair(pair);
			this.embeddedIdPathParameters.put(this.singularIdName, embeddedIdMeta);
		}
	}

	private void checkAndPopulateSimpleIdField(Field aField) throws Exception {
		Id id = aField.getAnnotation(Id.class);
		
		if (id != null) {
//			this.idFields.add(aField);
			this.singularIdName = aField.getName();
			if(!this.isValidSimpleIdType(aField.getType())) {
				throw new IncorrectIdStructureException("The class: " + this.modelClass.getName() + 
					" contains an incorrect id field: " + aField.getType() + " by the name: " + aField.getName());
			}
			PropertyDescriptor propertyDescriptor = this.getModelPropertyDescriptor(this.singularIdName, aField.getType());
			
			EntityIdMeta idMeta = new EntityIdMeta();
			idMeta.setFieldType(propertyDescriptor.getPropertyType());
			idMeta.setPropertyDescriptor(propertyDescriptor);
			this.idPathParameters.put(this.singularIdName, idMeta);
		}
	}

	private void checkAndPopulateIdAccessor(Method aAccessor) throws Exception {
		EmbeddedId embeddedId = aAccessor.getAnnotation(EmbeddedId.class);
		
		if (embeddedId == null) {
			this.checkAndPopulateSimpleIdAccessor(aAccessor);
		} else {
			//this.embeddedIdAccessors.add(aAccessor);
			this.singularIdName = aAccessor.getName().substring(3).toLowerCase();
			PropertyDescriptor propertyDescriptor = this.getModelPropertyDescriptor(this.singularIdName, this.getTypeOfIdAccessorMethod(aAccessor));
			
			EntityIdMeta embeddedIdMeta = new EntityIdMeta();
			embeddedIdMeta.setFieldType(propertyDescriptor.getPropertyType());
			embeddedIdMeta.setPropertyDescriptor(propertyDescriptor);
			this.embeddedIdPathParameters.put(this.singularIdName, embeddedIdMeta);
		}
	}
	
	private void checkAndPopulateSimpleIdAccessor(Method aAccessor) throws Exception {
		Id id = aAccessor.getAnnotation(Id.class);
		
		if (id != null) {
//			this.idAccessors.add(aAccessor);
			this.singularIdName = aAccessor.getName().substring(3).toLowerCase();
			
			Class<?> type = this.getTypeOfIdAccessorMethod(aAccessor);
			if(!this.isValidSimpleIdType(aAccessor.getReturnType())) {
				throw new IncorrectIdStructureException("The class: " + this.modelClass.getName() + 
						" contains an incorrect id accessor type: " + aAccessor.getReturnType() + " by the name: " + aAccessor.getName());
			}
			PropertyDescriptor propertyDescriptor = this.getModelPropertyDescriptor(this.singularIdName, type);
			
			EntityIdMeta idMeta = new EntityIdMeta();
			idMeta.setFieldType(propertyDescriptor.getPropertyType());
			idMeta.setPropertyDescriptor(propertyDescriptor);
			this.idPathParameters.put(this.singularIdName, idMeta);
		}
	}

	private Class<?> getTypeOfIdAccessorMethod(Method aAccessor) throws Exception{
		Class<?> type = aAccessor.getReturnType();
		if(type != null) {
			return type;
		}

		Parameter[] accessorParams = aAccessor.getParameters();
		if(accessorParams.length == 1) {
			return accessorParams[0].getType(); 
		}
		
		throw new IncorrectIdStructureException("The type of the id parameter couldn't be deduced from the accessor method annotated as an identifier. "
				+ "It seems the method isn't a valid accessor method.");
	}
	
	private PropertyDescriptor getModelPropertyDescriptor(String aPropertyName, Class<?> aPropertyType) throws Exception { 
		return this.validateAndGetBeanPropertyDescriptor(this.modelBeanInfo, aPropertyName, aPropertyType);
	}
	
	private PropertyDescriptor validateAndGetBeanPropertyDescriptor(BeanInfo aBeanInfo, String aPropertyName, Class<?> aPropertyType) throws Exception {
		for(PropertyDescriptor pd : aBeanInfo.getPropertyDescriptors()) {
			if(this.validatePropertyDescriptor(pd, aPropertyName, aPropertyType)) {
				return pd;
			}
		}
		throw new IncorrectIdStructureException("No property descriptor was found for a property named: " + aPropertyName + " in the entity bean: " + this.modelClass);
	}
	
	private boolean validatePropertyDescriptor(PropertyDescriptor aPropertyDescriptor, String aPropertyName, Class<?> aPropertyType) throws Exception{
		if(!aPropertyDescriptor.getName().equals(aPropertyName)) {
			return false;
		}
		
		if(Primitives.unwrap(aPropertyDescriptor.getPropertyType()) != Primitives.unwrap(aPropertyType)) {
			throw new IncorrectIdStructureException("The type of the property: " + aPropertyName + " in the entity class: " + 
					this.modelClass + " doesn't match with a similarily named accessor method in the same class" );
		}
		
		if(aPropertyDescriptor.getReadMethod() == null) {
			throw new IncorrectIdStructureException("The property: " + aPropertyName + " is missing a reader (getter) method");
		}
		
		if(aPropertyDescriptor.getWriteMethod() == null) {
			throw new IncorrectIdStructureException("The property: " + aPropertyName + " is missing a writer (setter) method");
		}
		
		return true;
	}
	
	private void validateEntityIdAccessorMethods(PropertyDescriptor aPropertyDescriptor) throws Exception {
		if(aPropertyDescriptor.getReadMethod() == null) {
			throw new IncorrectIdStructureException("The property: " + aPropertyDescriptor.getName() + " has no reader method in the class: " + this.modelClass.getName());
		}
	}

	private void generateSingularIdName() {
		this.singularIdName = "id";
		while(this.idPathParameters.keySet().contains(this.singularIdName))
		{
			this.singularIdName = "_" + this.singularIdName;
		}
	}
	
//	private void setIdPathParameters() throws Exception {
//		if(this.idType.isAssignableFrom(IdTypeEnum.composite)) {
//			for(Field field : this.idType.getDeclaredFields()) {
//				this.idPathParameters.put(field.getName(), field.getType());
//			}
//	        BeanInfo info = Introspector.getBeanInfo( this.idType );
//	 		for(PropertyDescriptor pd : info.getPropertyDescriptors()) {
//				this.idPathParameters.put(pd.getName(), pd.getPropertyType());
//			}
//		}
//		else {
//			this.idPathParameters.put(this.singularIdName, this.idClass);
//		}
//	}
//	
	private void validateIdStructureFound() throws IncorrectIdStructureException {
		if(this.idClass == null) {
			throw new IncorrectIdStructureException("No valid id structure was found for the class: " + this.modelClass);
		}
	}
	
	private void setSimpleIdClass() throws Exception {
		for(EntityIdMeta meta : this.idPathParameters.values()) {
			this.idClass = meta.getFieldType();
		}

		this.idBeanInfo = Introspector.getBeanInfo(this.idClass);
	}
	
	private void setEmbeddedIdClass() throws Exception {
		for(EntityIdMeta meta : this.embeddedIdPathParameters.values()) {
			this.idClass = meta.getFieldType();
		}
		
		this.validateCompositeIdFields(this.idClass);

		this.idBeanInfo = Introspector.getBeanInfo(this.idClass);
		
	}
	
	private void validateCompositeIdFields(Class<?> aCompositeIdType) throws Exception {
		for(Field field : aCompositeIdType.getDeclaredFields()) {
			if(!this.isValidSimpleIdType(field.getType())) {
				throw new IncorrectIdStructureException("The type: " + aCompositeIdType + 
						" isn't a valid composite id type because it has fields that aren't simple id types");
			}
		}
	}
	
	private boolean containsSimpleIds() {
		return this.idPathParameters.size() > 0;
	}
	
	private boolean containsEmbeddedIds() {
		return this.embeddedIdPathParameters.size() > 0;
	}
	
	private void validateContainsOnlyOneSimpleId() throws IncorrectIdStructureException
	{
		if(this.idPathParameters.size() != 1)
		{
			throw new IncorrectIdStructureException("The class: " + this.modelClass.getName() + 
					" either contains too many simple id parameters or is missing the @IdClass type annotation!");
		}
	}
	
	private void validateContainsOnlyOneEmbeddeId() throws IncorrectIdStructureException
	{
		if(this.embeddedIdPathParameters.size() != 1)
		{
			throw new IncorrectIdStructureException("The class: " + this.modelClass.getName() + 
					" must define one and only one @EmbeddedId id!");
		}
	}
	
//	private void validateSimpleIdTypes() throws IncorrectIdStructureException
//	{
//		for(Field field : this.idFields) {
//			if(!this.isValidSimpleIdType(field.getType())) {
//				throw new IncorrectIdStructureException("The class: " + this.modelClass.getName() + 
//					" contains an incorrect id field: " + field.getType() + " by the name: " + field.getName());
//			}
//		}
//		for(Method accessor : this.idAccessors) {
//			if(!this.isValidSimpleIdType(accessor.getReturnType())) {
//				throw new IncorrectIdStructureException("The class: " + this.modelClass.getName() + 
//						" contains an incorrect id accessor type: " + accessor.getReturnType() + " by the name: " + accessor.getName());
//			}
//		}
//	}
	
//	private void validateEmbeddedIdTypes() throws IncorrectIdStructureException
//	{
//		HashSet<Class> embeddedIdTypes = new HashSet<Class>();
//		
//		for(Field field : this.embeddedIdFields) {
//			embeddedIdTypes.add(field.getType());
//		}
//		for(Method accessor : this.embeddedIdAccessors) {
//			embeddedIdTypes.add(accessor.getReturnType());
//			if(!this.isValidSimpleIdType(accessor.getReturnType())) {
//				throw new IncorrectIdStructureException("The class: " + this.modelClass.getName() + 
//						" contains an incorrect id accessor type: " + accessor.getReturnType() + " by the name: " + accessor.getName());
//			}
//		}
//		
//		for(Class type : embeddedIdTypes)
//		{
//			if(!this.isValidSimpleIdType(type)) {
//				throw new IncorrectIdStructureException("The class: " + this.modelClass.getName() + 
//					" contains an incorrect EmbeddedId type with an incorrect simple id type" + type);
//			}
//		}
//	}
//	
	private void validateCompositeIdStructure(IdClass aIdClsAnnotation) throws Exception{
		this.idType = IdTypeEnum.composite;
		this.idClass = aIdClsAnnotation.value();
		this.validateEmbeddedIdUniformity();
		
		this.validateCompositeIdFields(this.idClass);
		this.idBeanInfo = Introspector.getBeanInfo(this.idClass);
		
		this.validateThatEntityClassContainsSimpleIds();
		this.validateThatEntityClassContainsOnlySimpleIds();
		this.validateAndCreateEntityCompositeMapping(this.idClass, modelVarNameInSetter, idVarNameInSetter);

	}
	
	private void validateEmbeddedIdStructure() throws Exception{
		this.idType = IdTypeEnum.embedded;
		this.validateContainsOnlyOneEmbeddeId();
		this.setEmbeddedIdClass();
		this.validateThatIdClassPointsToEmbeddableClass();
		this.createEmbeddedKeyIdPathParameters();
	}
	
	private void validateSimpleIdStructure() throws Exception{
		this.idType = IdTypeEnum.simple;
		this.validateContainsOnlyOneSimpleId();
		this.setSimpleIdClass();
	}
	
	private void validateEmbeddedIdUniformity() throws IncorrectIdStructureException{
		if(this.containsSimpleIds() && this.containsEmbeddedIds()) {
			throw new IncorrectIdStructureException("The class: " + this.modelClass.getName() + " contains id's of type @EmbeddedId and @Id. "
					+ "Only on or the other type is permitted!");
		}
	}
	
	private void validateThatEntityClassContainsSimpleIds() throws IncorrectIdStructureException {
		if(!this.containsSimpleIds()) {
			throw new IncorrectIdStructureException("The class: " + this.modelClass.getName() + " contains no id's of type @Id. "
					+ "Classes annotated @IdClass must contain at least one simple id definition!");
		}
	}
	
	private void validateThatEntityClassContainsOnlySimpleIds() throws IncorrectIdStructureException {
		if(this.embeddedIdPathParameters.size() > 0) {
			throw new IncorrectIdStructureException("The class: " + this.modelClass.getName() + " contains one or more id's of type @EmbeddedId. "
					+ "Only id's of type @Id are permitted for classes annotated @IdClass!");
		}
	}
	
	private void validateThatIdClassPointsToEmbeddableClass() throws IncorrectIdStructureException {
		if(this.idClass.getAnnotation(Embeddable.class) == null)
		{
			throw new IncorrectIdStructureException("The class: " + this.modelClass.getName() + " points to an incorrect composite id class. "
					+ "Only classes annotated @Embeddable are permitted for fields annotated @EmbeddedId!");
		}
	}
	
//	private boolean validateIsEntityIdDescriptorAndHasAccessors(PropertyDescriptor aPropertyDescriptor)
//	{
//		if(aPropertyDescriptor.getName() == "class") {
//			return false;
//		}
//		
//		if(this.modelClass.get)
//	}
	
	private void createEmbeddedKeyIdPathParameters() {
		HashSet<String> fieldNames = new HashSet<String>();
		
		for(PropertyDescriptor pd : this.idBeanInfo.getPropertyDescriptors()) {
			if(!pd.getName().equals("class") && 
					!fieldNames.contains(pd.getName()) &&
					pd.getWriteMethod() != null) {
				fieldNames.add(pd.getName());

				EntityIdMeta idMeta = new EntityIdMeta();
				idMeta.setFieldType(pd.getPropertyType());
				this.idPathParameters.put(pd.getName(), idMeta);
			}
		}
		for(Field field : idClass.getDeclaredFields()) {
			if(!fieldNames.contains(field.getName())) {
				fieldNames.add(field.getName());

				EntityIdMeta idMeta = new EntityIdMeta();
				idMeta.setFieldType(field.getType());
				this.idPathParameters.put(field.getName(), idMeta);
			}
		}
	}
	
	private void validateAndCreateEntityCompositeMapping(Class<?> aCompositeClass, String aEntityVariableName, String aCompositeVariableName) throws Exception {
		try {
			HashSet<String> fieldNames = new HashSet<String>();
			fieldNames.addAll(this.idPathParameters.keySet());
			
			for (Field field : aCompositeClass.getDeclaredFields()) {
				if (fieldNames.contains(field.getName())) {
					EntityIdMeta meta = idPathParameters.get(field.getName());
					PropertyDescriptor propertyDescriptor = meta.getPd();

					if (propertyDescriptor != null) {
						GetCompositeSetEntityPair pair = new GetCompositeSetEntityPair(
								field, propertyDescriptor.getWriteMethod(),
								aEntityVariableName, aCompositeVariableName);
						meta.setReadWritePair(pair);
						fieldNames.remove(field.getName());
					}
				}
			}

			BeanInfo compositeInfo = Introspector.getBeanInfo(this.modelClass);
			for (PropertyDescriptor pd : compositeInfo.getPropertyDescriptors()) {
				if (fieldNames.contains(pd.getName()) && !pd.getName().equals("class")) {
					EntityIdMeta meta = idPathParameters.get(pd.getName());
					PropertyDescriptor propertyDescriptor = meta.getPd();

					if (propertyDescriptor != null) {
						GetCompositeSetEntityPair pair = new GetCompositeSetEntityPair(
								pd.getReadMethod(), propertyDescriptor.getWriteMethod(),
								aEntityVariableName, aCompositeVariableName);
						meta.setReadWritePair(pair);
						fieldNames.remove(pd.getName());
					}
				}
			}
		} catch (Exception e) {
			throw new IncorrectIdStructureException(
					"The model entity id fields in the class: "
							+ this.modelClass.getName()
							+ " don't map correctly to the composite key class: "
							+ aCompositeClass.getSimpleName(), e);
		}
	}
	
	private void handleIds() throws Exception {
	}

	private void handleEmbeddedId(Model aModelAnnotation) {
		
	}

	private void handleCompositeId(Model aModelAnnotation) {

	}

	private void handleSimpleId(Model aModelAnnotation) {

	}

	public String getPackageName() {
		return this.modelClass.getPackage().getName();
	}

	public String getClassName() {
		return this.modelClass.getSimpleName();
	}

	public String getPlural() {
		return plural;
	}

	private void setPlural(String plural) {
		this.plural = plural;
	}

	public String getPersistenceUnitName() {
		return persistenceUnitName;
	}

	private void setPersistenceUnitName(String persistenceUnitName) {
		this.persistenceUnitName = persistenceUnitName;
	}

	public void setRouteClassPostfix(String aPostFix) {
		this.routeClassPostfix = aPostFix;
	}

	public String getRouteClassName() {
		return this.getClassName() + this.routeClassPostfix;
	}

	public String getIdClassName() {
		return Primitives.wrap(this.idClass).getSimpleName();
	}

	private void setIdClass(Class idClass) {
		this.idClass = idClass;
	}

	public String getIdPathExtension() {
		StringBuilder sb = new StringBuilder();
		
		for(String pathParam : this.idPathParameters.keySet()) {
			sb.append(j.join("/{", pathParam, "}"));
		}
		return sb.toString();
	}

	public String getIdParameters() {
		StringBuilder sb = new StringBuilder();

		boolean isFirst = true;
		for(String pathParam : this.idPathParameters.keySet()) {
			Class type = this.idPathParameters.get(pathParam).getFieldType();
			if(!isFirst) sb.append(", ");
			sb.append(j.join("@PathParam(\"", pathParam, "\") ", type.getSimpleName(), " ", pathParam));
			isFirst = false;
		}
		return sb.toString();
	}

	public String getImports() {
		HashSet<Class> imports = new HashSet<Class>();
		for(EntityIdMeta meta : this.idPathParameters.values()) {
			imports.add(Primitives.wrap(meta.getFieldType()));
		}
		imports.add(Primitives.wrap(this.idClass));
		
		StringBuilder sb = new StringBuilder();
		for (Class cls : imports) {
			sb.append("import ").append(cls.getName()).append(";\n");
		}
		return sb.toString();
	}

	public String getIdSetterCode() {
		StringBuilder sb = new StringBuilder();

		sb.append(j.join("\tpublic void setModelId(", this.getClassName(), " aModel, ", this.getIdClassName(), " aId) {\n"));
		if(this.idType == IdTypeEnum.simple || this.idType == IdTypeEnum.embedded) {
			sb.append(j.join("\t\taModel.set", this.singularIdName.substring(0, 1).toUpperCase(), this.singularIdName.substring(1), "(aId);\n"));
		} else {
			for(EntityIdMeta meta : this.idPathParameters.values()) {
				sb.append(j.join("\t\t", meta.getReadWritePair()));
			}
		}
		sb.append("\t}");
		
		return sb.toString();
	}
	
	public String getSingularIdName() {
		return this.singularIdName;
	}
	
	public String getIdGenerateCode() throws Exception {
		if(this.idType == IdTypeEnum.simple) {
			return "\t\t";
		}
		else {
			StringBuilder sb = new StringBuilder();
			sb.append(j.join("\t\t", this.getIdClassName(), " ", this.getSingularIdName(), " = new ", this.getIdClassName(), "();\n"));
			for(String paramName : this.idPathParameters.keySet()) {
				sb.append(j.join("\t\t", this.generateSetterLine(paramName, this.idClass)));
			}
			sb.append(j.join("\n\t\t", ""));
			return sb.toString();
		}
	}
	
	private String generateSetterLine(String aParamName, Class aIdType) throws Exception {
		if(this.isValidSimpleIdType(aIdType)) {
			return j.join(aParamName, " = ", this.getSingularIdName(), ";\n");
		}
		else {		 
	 		for (PropertyDescriptor pd : this.idBeanInfo.getPropertyDescriptors()) {
				if(pd.getName().equals(aParamName) && pd.getWriteMethod() != null)	{
					return j.join(this.getSingularIdName(), ".", pd.getWriteMethod().getName(), "(", aParamName, ");\n");
				}
			}
			for(Field field : this.idClass.getDeclaredFields()) {
				if(field.getName().equals(aParamName)) {
					return j.join(this.getSingularIdName(), ".", aParamName, " = ", aParamName, ";\n");
				}
			}
			throw new IncorrectIdStructureException("The composite type: " + this.idClass + 
					" doesn't contain a field or accessor for a field by the name: " + aParamName);
		}
	}

	private boolean isValidSimpleIdType(Class aType)
	{
		return aType.isPrimitive() ||
				Primitives.allWrapperTypes().contains(aType) ||
				nonPrimitiveIdTypes.contains(aType);
	}
}
