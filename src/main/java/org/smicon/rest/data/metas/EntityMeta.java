package org.smicon.rest.data.metas;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.smicon.rest.GetCompositeSetEntityPair;
import org.smicon.rest.data.entitymeta.EntityValidationDataI;
import org.smicon.rest.data.modelmeta.Metas;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistry;
import org.smicon.rest.data.modelmeta.registry.ModelMetaRegistryI;
import org.smicon.rest.exceptions.IncorrectEntityStructureException;
import org.smicon.rest.exceptions.IncorrectIdStructureException;
import org.smicon.rest.functionality.entitymeta.EntityValidationFunctions;
import org.smicon.rest.functionality.modelmeta.ModelValidationFunctions;
import org.smicon.rest.types.id.IdType;
import org.smicon.rest.types.id.IdTypeEnum;

import com.google.common.collect.Sets;
import com.google.common.primitives.Primitives;

public class EntityMeta
{

	public static final String modelVarNameInSetter = "aModel";
	public static final String idVarNameInSetter = "aId";

	private ModelMetaRegistryI registry;
	private EntityValidationDataI validationData;

	private Class<?> entityClass;
	private BeanInfo modelBeanInfo;

	private Class<?> idClass;
	private BeanInfo idBeanInfo;

	private Class<? extends IdType> idType;
	private String singularIdName;

	private LinkedHashMap<String, EntityIdMeta> idPathParameterMetas = new LinkedHashMap<String, EntityIdMeta>();
	private LinkedHashMap<String, EntityIdMeta> embeddedIdPathParameterMetas = new LinkedHashMap<String, EntityIdMeta>();

	private LinkedHashMap<String, PropertyDescriptor> simpleProperties = new LinkedHashMap<String, PropertyDescriptor>();
	private LinkedHashMap<String, EntityPropertyMeta> childEntityPropertiesWithSimpleIds = new LinkedHashMap<String, EntityPropertyMeta>();
	private LinkedHashMap<String, EntityPropertyMeta> childEntityPropertiesWithEmbeddedIds = new LinkedHashMap<String, EntityPropertyMeta>();
	private LinkedHashMap<String, EntityPropertyMeta> childEntityPropertiesWithCompositeIds = new LinkedHashMap<String, EntityPropertyMeta>();
	private LinkedHashMap<String, EntityCollectionPropertyMeta> entityCollectionProperties = new LinkedHashMap<String, EntityCollectionPropertyMeta>();

	// For circular dependency detection
	private boolean deduceChildEntityStructureInitiated;

	protected EntityMeta()
	{
		this.setDefaultValues();
	}
	
	private void setDefaultValues()
	{
		entityClass = null;
		modelBeanInfo = null;

		idClass = null;
		idBeanInfo = null;

		idType = null;
		singularIdName = null;

		idPathParameterMetas = new LinkedHashMap<String, EntityIdMeta>();
		embeddedIdPathParameterMetas = new LinkedHashMap<String, EntityIdMeta>();

		simpleProperties = new LinkedHashMap<String, PropertyDescriptor>();
		childEntityPropertiesWithSimpleIds = new LinkedHashMap<String, EntityPropertyMeta>();
		childEntityPropertiesWithEmbeddedIds = new LinkedHashMap<String, EntityPropertyMeta>();
		childEntityPropertiesWithCompositeIds = new LinkedHashMap<String, EntityPropertyMeta>();
		entityCollectionProperties = new LinkedHashMap<String, EntityCollectionPropertyMeta>();
	}

	public void setEntityClass(Class<?> aEntityClass) throws Exception
	{
		if(this.registry == null) throw new IllegalAccessException("No registry has been set!!");
		if(this.validationData == null) throw new IllegalAccessException("No validation data has been set!!");
		
		this.setDefaultValues();
		this.entityClass = aEntityClass;
		this.modelBeanInfo = Introspector.getBeanInfo(this.entityClass);
		deduceChildEntityStructureInitiated = false;
		this.deduceClassStructure();
	}
	
	public Class<?> getEntityClass()
	{
		return entityClass;
	}

	public void setRegistry(ModelMetaRegistryI aRegistry)
	{
		this.registry = aRegistry;
	}
	
	public void setValidationData(EntityValidationDataI aData)
	{
		this.validationData = aData;
	}
	
	public EntityValidationDataI getValidationData() throws Exception
	{
		if(this.validationData == null) throw new IllegalAccessException("No validation data has been set!!");
		return this.validationData;
	}

	public ModelMetaRegistryI getRegistry() throws Exception
	{
		if(this.registry == null) throw new IllegalAccessException("No registry has been set!!");
		return this.registry;
	}

	public BeanInfo getModelBeanInfo()
	{
		return modelBeanInfo;
	}

	public Class<?> getIdClass()
	{
		return idClass;
	}

	public BeanInfo getIdBeanInfo()
	{
		return idBeanInfo;
	}

	public Class<? extends IdType> getIdType()
	{
		return idType;
	}

	public String getSingularIdName()
	{
		return singularIdName;
	}

	public LinkedHashMap<String, EntityIdMeta> getIdPathParameterMetas()
	{
		return idPathParameterMetas;
	}

	public LinkedHashMap<String, EntityIdMeta> getEmbeddedIdPathParameterMetas()
	{
		return embeddedIdPathParameterMetas;
	}

	public static Class<?> getTypeOfIdAccessorMethod(Method aAccessor) throws Exception
	{
		Class<?> type = aAccessor.getReturnType();
		if (type != null)
		{
			return type;
		}

		Parameter[] accessorParams = aAccessor.getParameters();
		if (accessorParams.length == 1)
		{
			return accessorParams[0].getType();
		}

		throw new IncorrectIdStructureException("The type of the id parameter couldn't be deduced from the accessor method annotated as an identifier. "
		+ "It seems the method isn't a valid accessor method.");
	}

	private boolean containsSimpleIds()
	{
		return this.idPathParameterMetas.size() > 0;
	}

	private boolean containsEmbeddedIds()
	{
		return this.embeddedIdPathParameterMetas.size() > 0;
	}

	public LinkedHashMap<String, PropertyDescriptor> getSimpleProperties()
	{
		return simpleProperties;
	}

	public LinkedHashMap<String, EntityPropertyMeta> getChildEntityPropertiesWithSimpleIds()
	{
		return childEntityPropertiesWithSimpleIds;
	}

	public LinkedHashMap<String, EntityPropertyMeta> getChildEntityPropertiesWithEmbeddedIds()
	{
		return childEntityPropertiesWithEmbeddedIds;
	}

	public LinkedHashMap<String, EntityPropertyMeta> getChildEntityPropertiesWithCompositeIds()
	{
		return childEntityPropertiesWithCompositeIds;
	}

	public LinkedHashMap<String, EntityCollectionPropertyMeta> getEntityCollectionProperties()
	{
		return entityCollectionProperties;
	}

	private void deduceClassStructure() throws Exception
	{
		this.deduceIdStructure();
		this.deduceChildEntityStructure();
	}

	private void deduceChildEntityStructure() throws Exception
	{
		if (this.deduceChildEntityStructureInitiated) return;
		this.deduceChildEntityStructureInitiated = true;

		for (PropertyDescriptor desc : this.modelBeanInfo.getPropertyDescriptors())
		{
			if (desc.getName().equals("class")) continue;

			ModelMeta meta = this.getRegistry().getModelMeta(desc.getPropertyType());

			if (meta == null)
			{
				Class<?> target = this.getEntityCollectionTargetEntity(this.entityClass, desc);
				if (target != null)
				{
					this.entityCollectionProperties.put(desc.getName(), new EntityCollectionPropertyMeta(this, meta, desc, target));
				}
				else
				{
					this.simpleProperties.put(desc.getName(), desc);
				}
			}
			else
			{
				EntityPropertyMeta propertyMeta = new EntityPropertyMeta(this, meta, desc);
				if (meta.getIdType() == IdTypeEnum.simple)
				{
					this.childEntityPropertiesWithSimpleIds.put(desc.getName(), propertyMeta);
				}
				else if (meta.getIdType() == IdTypeEnum.embedded)
				{
					this.childEntityPropertiesWithEmbeddedIds.put(desc.getName(), propertyMeta);
				}
				else if (meta.getIdType() == IdTypeEnum.composite)
				{
					this.childEntityPropertiesWithCompositeIds.put(desc.getName(), propertyMeta);
				}
				else
				{
					throw new RuntimeException("The entity meta type has no id type!!");
				}
			}
		}
	}

	private Class<?> getEntityCollectionTargetEntity(Class<?> aParentType, PropertyDescriptor aPd) throws Exception
	{
		OneToMany oneToMany;
		ManyToMany manyToMany;

		if (aPd.getReadMethod() != null)
		{
			oneToMany = aPd.getReadMethod().getAnnotation(OneToMany.class);
			if (oneToMany != null)
			{
				if (oneToMany.targetEntity() != void.class &&
					oneToMany.targetEntity() != Void.class)
				{
					return oneToMany.targetEntity();
				}
				return this.getEntityCollectionTargetEntity(aParentType, aPd.getReadMethod().getReturnType());
			}

			manyToMany = aPd.getReadMethod().getAnnotation(ManyToMany.class);
			if (manyToMany != null)
			{
				if (manyToMany.targetEntity() != void.class &&
					manyToMany.targetEntity() != Void.class)
				{
					return manyToMany.targetEntity();
				}
				return this.getEntityCollectionTargetEntity(aParentType, aPd.getReadMethod().getReturnType());
			}
		}

		Field field = null;
		try
		{
			field = this.entityClass.getDeclaredField(aPd.getName());
		}
		catch (Exception e)
		{};

		if (field != null)
		{
			oneToMany = field.getAnnotation(OneToMany.class);
			if (oneToMany != null)
			{
				if (oneToMany.targetEntity() != void.class &&
				oneToMany.targetEntity() != Void.class)
				{
					return oneToMany.targetEntity();
				}
				return this.getEntityCollectionTargetEntity(aParentType, field.getType());
			}

			manyToMany = field.getAnnotation(ManyToMany.class);
			if (manyToMany != null)
			{
				if (manyToMany.targetEntity() != void.class &&
				manyToMany.targetEntity() != Void.class)
				{
					return manyToMany.targetEntity();
				}
				return this.getEntityCollectionTargetEntity(aParentType, field.getType());
			}
		}

		return null;
	}

	/**
	 * This will try and deduce the target class of a Collection or Map property
	 * from the 'limited' generics information available at runtime.
	 * 
	 * 
	 * @param aParentType
	 * @param aOneToMany
	 * @param aCollectionType
	 * @return
	 * @throws Exception
	 */
	private Class<?> getEntityCollectionTargetEntity(Class<?> aParentType, Class<?> aCollectionType) throws Exception
	{
		String targetClassName = null;

		if (Collection.class.isAssignableFrom(aCollectionType))
		{
			if (aCollectionType.getTypeParameters().length == 0)
			{
				throw new IncorrectEntityStructureException("An entity collection in the class: " + aParentType.getSimpleName()
				+ " doesn't define a target entity either in the relation type annotation (@OneToMany(targetEntity=..) or via reflection.)");
			}
			targetClassName = aCollectionType.getTypeParameters()[0].getName();
		}
		else if (Map.class.isAssignableFrom(aCollectionType))
		{
			if (aCollectionType.getTypeParameters().length < 2)
			{
				throw new IncorrectEntityStructureException("An entity collection in the class: " + aParentType.getSimpleName()
				+ " doesn't define a target entity either in the relation type annotation (@OneToMany(targetEntity=..) or via reflection.)");
			}
			targetClassName = aCollectionType.getTypeParameters()[1].getName();
		}

		return this.getRegistry().getModel(targetClassName);
	}

	private void deduceIdStructure() throws Exception
	{
		IdClass idCls = this.entityClass.getAnnotation(IdClass.class);

		this.populateIdArrays();
		this.validateEmbeddedIdUniformity();

		if (idCls != null)
		{
			this.validateCompositeIdStructure(idCls);
			this.generateSingularIdName();
		}
		else
		{
			if (this.containsSimpleIds())
			{
				this.validateSimpleIdStructure();
			}
			else if (this.containsEmbeddedIds())
			{
				this.validateEmbeddedIdStructure();
			}
		}

		this.validateIdStructureFound();
	}

	private void populateIdArrays() throws Exception
	{

		Field[] fieldArray = this.entityClass.getDeclaredFields();
		for (Field field : fieldArray)
		{
			this.checkAndPopulateIdField(field);
		}

		Method[] methodArray = this.entityClass.getDeclaredMethods();
		for (Method method : methodArray)
		{
			this.checkAndPopulateIdAccessor(method);
		}

	}

	private void checkAndPopulateIdField(Field aField) throws Exception
	{
		EmbeddedId embeddedId = aField.getAnnotation(EmbeddedId.class);

		if (embeddedId == null)
		{
			this.checkAndPopulateSimpleIdField(aField);
		}
		else
		{
			this.singularIdName = aField.getName();
			PropertyDescriptor propertyDescriptor = this.getModelPropertyDescriptor(this.singularIdName, aField.getType());

			EntityIdMeta embeddedIdMeta = new EntityIdMeta(aField.getName());
			embeddedIdMeta.setFieldType(propertyDescriptor.getPropertyType());
			embeddedIdMeta.setPropertyDescriptor(propertyDescriptor);

			GetCompositeSetEntityPair pair = new GetCompositeSetEntityPair(aField, propertyDescriptor.getWriteMethod(), modelVarNameInSetter, idVarNameInSetter);
			embeddedIdMeta.setReadWritePair(pair);
			this.embeddedIdPathParameterMetas.put(this.singularIdName, embeddedIdMeta);
		}
	}

	private void checkAndPopulateSimpleIdField(Field aField) throws Exception
	{
		Id id = aField.getAnnotation(Id.class);
		GeneratedValue generated = aField.getAnnotation(GeneratedValue.class);

		if (id != null)
		{
			this.singularIdName = aField.getName();
			if (!EntityValidationFunctions.isValidSimpleIdType(aField.getType(), this.getValidationData()))
			{
				throw new IncorrectIdStructureException("The class: " + this.entityClass.getName() + " contains an incorrect id field: " + aField.getType()
				+ " by the name: " + aField.getName());
			}
			PropertyDescriptor propertyDescriptor = this.getModelPropertyDescriptor(this.singularIdName, aField.getType());

			EntityIdMeta idMeta = new EntityIdMeta(aField.getName());
			idMeta.setFieldType(propertyDescriptor.getPropertyType());
			idMeta.setPropertyDescriptor(propertyDescriptor);
			idMeta.setGenerated(generated != null);
			this.idPathParameterMetas.put(this.singularIdName, idMeta);
		}
	}

	private void checkAndPopulateIdAccessor(Method aAccessor) throws Exception
	{
		EmbeddedId embeddedId = aAccessor.getAnnotation(EmbeddedId.class);

		if (embeddedId == null)
		{
			this.checkAndPopulateSimpleIdAccessor(aAccessor);
		}
		else
		{
			this.singularIdName = aAccessor.getName().substring(3).toLowerCase();
			PropertyDescriptor propertyDescriptor = this.getModelPropertyDescriptor(this.singularIdName, getTypeOfIdAccessorMethod(aAccessor));

			EntityIdMeta embeddedIdMeta = new EntityIdMeta(this.singularIdName);
			embeddedIdMeta.setFieldType(propertyDescriptor.getPropertyType());
			embeddedIdMeta.setPropertyDescriptor(propertyDescriptor);
			this.embeddedIdPathParameterMetas.put(this.singularIdName, embeddedIdMeta);
		}
	}

	private void checkAndPopulateSimpleIdAccessor(Method aAccessor) throws Exception
	{
		Id id = aAccessor.getAnnotation(Id.class);
		GeneratedValue generated = aAccessor.getAnnotation(GeneratedValue.class);

		if (id != null)
		{
			this.singularIdName = aAccessor.getName().substring(3).toLowerCase();

			Class<?> type = getTypeOfIdAccessorMethod(aAccessor);
			if (!EntityValidationFunctions.isValidSimpleIdType(aAccessor.getReturnType(), this.getValidationData()))
			{
				throw new IncorrectIdStructureException("The class: " + this.entityClass.getName() + " contains an incorrect id accessor type: "
				+ aAccessor.getReturnType() + " by the name: " + aAccessor.getName());
			}
			PropertyDescriptor propertyDescriptor = this.getModelPropertyDescriptor(this.singularIdName, type);

			EntityIdMeta idMeta = new EntityIdMeta(this.singularIdName);
			idMeta.setFieldType(propertyDescriptor.getPropertyType());
			idMeta.setPropertyDescriptor(propertyDescriptor);
			idMeta.setGenerated(generated != null);
			this.idPathParameterMetas.put(this.singularIdName, idMeta);
		}
	}

	private PropertyDescriptor getModelPropertyDescriptor(String aPropertyName, Class<?> aPropertyType) throws Exception
	{
		return this.validateAndGetBeanPropertyDescriptor(this.modelBeanInfo, aPropertyName, aPropertyType);
	}

	private PropertyDescriptor validateAndGetBeanPropertyDescriptor(BeanInfo aBeanInfo, String aPropertyName, Class<?> aPropertyType) throws Exception
	{
		for (PropertyDescriptor pd : aBeanInfo.getPropertyDescriptors())
		{
			if (EntityValidationFunctions.validatePropertyDescriptor(pd, aPropertyName, aPropertyType))
			{
				return pd;
			}
		}
		throw new IncorrectIdStructureException("No property descriptor was found for a property named: " + aPropertyName + " in the entity bean: "
		+ this.entityClass);
	}

	private void generateSingularIdName()
	{
		this.singularIdName = "id";
		while (this.idPathParameterMetas.keySet().contains(this.singularIdName))
		{
			this.singularIdName = "_" + this.singularIdName;
		}
	}

	private void validateIdStructureFound() throws IncorrectIdStructureException
	{
		if (this.idClass == null)
		{
			throw new IncorrectIdStructureException("No valid id structure was found for the class: " + this.entityClass);
		}
	}

	private void setSimpleIdClass() throws Exception
	{
		for (EntityIdMeta meta : this.idPathParameterMetas.values())
		{
			this.idClass = meta.getFieldType();
		}

		this.idBeanInfo = Introspector.getBeanInfo(this.idClass);
	}

	private void setEmbeddedIdClass() throws Exception
	{
		for (EntityIdMeta meta : this.embeddedIdPathParameterMetas.values())
		{
			this.idClass = meta.getFieldType();
		}

		this.validateCompositeIdFields(this.idClass);

		this.idBeanInfo = Introspector.getBeanInfo(this.idClass);

	}

	private void validateCompositeIdFields(Class<?> aCompositeIdType) throws Exception
	{
		for (Field field : aCompositeIdType.getDeclaredFields())
		{
			if (!EntityValidationFunctions.isValidSimpleIdType(field.getType(), this.getValidationData()))
			{
				throw new IncorrectIdStructureException("The type: " + aCompositeIdType
				+ " isn't a valid composite id type because it has fields that aren't simple id types");
			}
		}
	}

	private void validateContainsOnlyOneSimpleId() throws IncorrectIdStructureException
	{
		if (this.idPathParameterMetas.size() != 1)
		{
			throw new IncorrectIdStructureException("The class: " + this.entityClass.getName()
			+ " either contains too many simple id parameters or is missing the @IdClass type annotation!");
		}
	}

	private void validateContainsOnlyOneEmbeddeId() throws IncorrectIdStructureException
	{
		if (this.embeddedIdPathParameterMetas.size() != 1)
		{
			throw new IncorrectIdStructureException("The class: " + this.entityClass.getName() + " must define one and only one @EmbeddedId id!");
		}
	}

	private void validateCompositeIdStructure(IdClass aIdClsAnnotation) throws Exception
	{
		this.idType = IdTypeEnum.composite;
		this.idClass = aIdClsAnnotation.value();
		this.validateEmbeddedIdUniformity();

		this.validateCompositeIdFields(this.idClass);
		this.idBeanInfo = Introspector.getBeanInfo(this.idClass);

		this.validateThatEntityClassContainsSimpleIds();
		this.validateThatEntityClassContainsOnlySimpleIds();
		this.validateAndCreateEntityCompositeMapping(this.idClass, modelVarNameInSetter, idVarNameInSetter);

	}

	private void validateEmbeddedIdStructure() throws Exception
	{
		this.idType = IdTypeEnum.embedded;
		this.validateContainsOnlyOneEmbeddeId();
		this.setEmbeddedIdClass();
		this.validateThatIdClassPointsToEmbeddableClass();
		this.createEmbeddedKeyIdPathParameters();
	}

	private void validateSimpleIdStructure() throws Exception
	{
		this.idType = IdTypeEnum.simple;
		this.validateContainsOnlyOneSimpleId();
		this.setSimpleIdClass();
	}

	private void validateEmbeddedIdUniformity() throws IncorrectIdStructureException
	{
		if (this.containsSimpleIds() && this.containsEmbeddedIds())
		{
			throw new IncorrectIdStructureException("The class: " + this.entityClass.getName() + " contains id's of type @EmbeddedId and @Id. "
			+ "Only on or the other type is permitted!");
		}
	}

	private void validateThatEntityClassContainsSimpleIds() throws IncorrectIdStructureException
	{
		if (!this.containsSimpleIds())
		{
			throw new IncorrectIdStructureException("The class: " + this.entityClass.getName() + " contains no id's of type @Id. "
			+ "Classes annotated @IdClass must contain at least one simple id definition!");
		}
	}

	private void validateThatEntityClassContainsOnlySimpleIds() throws IncorrectIdStructureException
	{
		if (this.embeddedIdPathParameterMetas.size() > 0)
		{
			throw new IncorrectIdStructureException("The class: " + this.entityClass.getName() + " contains one or more id's of type @EmbeddedId. "
			+ "Only id's of type @Id are permitted for classes annotated @IdClass!");
		}
	}

	private void validateThatIdClassPointsToEmbeddableClass() throws IncorrectIdStructureException
	{
		if (this.idClass.getAnnotation(Embeddable.class) == null)
		{
			throw new IncorrectIdStructureException("The class: " + this.entityClass.getName() + " points to an incorrect composite id class. "
			+ "Only classes annotated @Embeddable are permitted for fields annotated @EmbeddedId!");
		}
	}

	private void createEmbeddedKeyIdPathParameters()
	{
		HashSet<String> fieldNames = new HashSet<String>();

		for (PropertyDescriptor idpd : this.idBeanInfo.getPropertyDescriptors())
		{
			if (!idpd.getName().equals("class") && !fieldNames.contains(idpd.getName()) && idpd.getWriteMethod() != null)
			{
				fieldNames.add(idpd.getName());

				EntityIdMeta idMeta = new EntityIdMeta(idpd.getName());
				idMeta.setFieldType(idpd.getPropertyType());
				for (PropertyDescriptor mpd : this.getModelBeanInfo().getPropertyDescriptors())
				{
					if (idpd.getName().equals(idpd.getName()))
					{
						idMeta.setPropertyDescriptor(mpd);
						break;
					}
				}
				// idMeta.setReadWritePair(new
				// GetCompositeSetEntityPair(aGetter, aSetter,
				// aEntityVariableName, aCompositeVariableName));
				this.idPathParameterMetas.put(idpd.getName(), idMeta);
			}
		}
		for (Field field : idClass.getDeclaredFields())
		{
			if (!fieldNames.contains(field.getName()) && EntityValidationFunctions.isAccessibleField(field))
			{
				fieldNames.add(field.getName());

				EntityIdMeta idMeta = new EntityIdMeta(field.getName());
				idMeta.setFieldType(field.getType());
				for (PropertyDescriptor pd : this.getModelBeanInfo().getPropertyDescriptors())
				{
					if (pd.getName().equals(field.getName()))
					{
						idMeta.setPropertyDescriptor(pd);
						break;
					}
				}
				this.idPathParameterMetas.put(field.getName(), idMeta);
			}
		}
	}

	private void validateAndCreateEntityCompositeMapping(Class<?> aCompositeClass, String aEntityVariableName, String aCompositeVariableName) throws Exception
	{
		try
		{
			HashSet<String> fieldNames = new HashSet<String>();
			fieldNames.addAll(this.idPathParameterMetas.keySet());

			for (Field field : aCompositeClass.getDeclaredFields())
			{
				if (fieldNames.contains(field.getName()))
				{
					EntityIdMeta meta = idPathParameterMetas.get(field.getName());
					PropertyDescriptor propertyDescriptor = meta.getPropertyDescriptor();

					if (propertyDescriptor != null)
					{
						GetCompositeSetEntityPair pair = new GetCompositeSetEntityPair(field, propertyDescriptor.getWriteMethod(), aEntityVariableName,
						aCompositeVariableName);
						meta.setReadWritePair(pair);
						fieldNames.remove(field.getName());
					}
				}
			}

			BeanInfo compositeInfo = Introspector.getBeanInfo(this.entityClass);
			for (PropertyDescriptor pd : compositeInfo.getPropertyDescriptors())
			{
				if (fieldNames.contains(pd.getName()) && !pd.getName().equals("class"))
				{
					EntityIdMeta meta = idPathParameterMetas.get(pd.getName());
					PropertyDescriptor propertyDescriptor = meta.getPropertyDescriptor();

					if (propertyDescriptor != null)
					{
						GetCompositeSetEntityPair pair = new GetCompositeSetEntityPair(pd.getReadMethod(), propertyDescriptor.getWriteMethod(),
						aEntityVariableName, aCompositeVariableName);
						meta.setReadWritePair(pair);
						fieldNames.remove(pd.getName());
					}
				}
			}
		}
		catch (Exception e)
		{
			throw new IncorrectIdStructureException("The model entity id fields in the class: " + this.entityClass.getName()
			+ " don't map correctly to the composite key class: " + aCompositeClass.getSimpleName(), e);
		}
	}
}
