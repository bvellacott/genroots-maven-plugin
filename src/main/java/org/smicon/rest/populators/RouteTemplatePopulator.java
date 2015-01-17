package org.smicon.rest.populators;

import org.smicon.rest.metas.EntityIdMeta;
import org.smicon.rest.metas.EntityMeta;
import org.smicon.rest.populators.abstracts.ModelExtensionPopulator;
import org.smicon.rest.populators.modelwrapper.ModelWrapperPopulator;
import org.smicon.rest.types.id.IdTypeEnum;

public class RouteTemplatePopulator 
extends ModelExtensionPopulator {
	
	public static final String urlIdDelimiter = "::";
	
	private String routeClassPostfix;
	
	// Child populators
	private ModelWrapperPopulator wrapperPopulator;
	 
	public RouteTemplatePopulator(Class<?> aModelClass) throws Exception {
		super(aModelClass);
		this.setRouteClassPostfix("Route");
		
		this.createChildPopulators();
	}
	
	public void setRouteClassPostfix(String aPostFix) {
		this.routeClassPostfix = aPostFix;
	}

	public String getRouteClassName() {
		return this.getClassName() + this.routeClassPostfix;
	}

	public String getIdPathExtension() {
		return this.getIdPathExtension(false);
	}
	
	public String getIdPathExtensionOmitAutoGenerated() {
		return this.getIdPathExtension(true);
	}
	
	public String getCreateRouteIdPathExtension() throws Exception {
		for(EntityIdMeta meta : this.getModelEntityMeta().getIdPathParameterMetas().values()) {
			if(!meta.isGenerated()) {
				return j.join("\n\t@Path(\"", this.getIdPathExtensionOmitAutoGenerated(), "\")");
			}
		}
		return "\t\t";
	}
	
	public String getIdPathExtension(boolean aOmitAutoGeneratedIds) {
		StringBuilder sb = new StringBuilder();
		
		String delimiter = "/";
		for(String pathParam : this.getModelEntityMeta().getIdPathParameterMetas().keySet()) {
			if(!(aOmitAutoGeneratedIds && this.getModelEntityMeta().getIdPathParameterMetas().get(pathParam).isGenerated())) {
				sb.append(j.join(delimiter, "{", pathParam, "}"));
				delimiter = RouteTemplatePopulator.urlIdDelimiter;
			}
		}
		return sb.toString();
	}

	public String getIdParameters() {
		return this.getIdParameters(false);
	}
	
	public String getIdParametersOmitAutoGenerated() {
		return this.getIdParameters(true);
	}
	
	public String getCreateRouteIdParameters() {
		String params = this.getIdParameters(true);
		if(params.length() > 0) {
			return j.join(params, ", ");
		}
		return "";
	}
	
	public String getIdParameters(boolean aOmitAutoGeneratedIds) {
		StringBuilder sb = new StringBuilder();

		boolean isFirst = true;
		for(String pathParam : this.getModelEntityMeta().getIdPathParameterMetas().keySet()) {
			if(!(aOmitAutoGeneratedIds && this.getModelEntityMeta().getIdPathParameterMetas().get(pathParam).isGenerated())) {
				Class type = this.getModelEntityMeta().getIdPathParameterMetas().get(pathParam).getFieldType();
				if(!isFirst) sb.append(", ");
				sb.append(j.join("@PathParam(\"", pathParam, "\") ", type.getSimpleName(), " ", pathParam));
				isFirst = false;
			}
		}
		return sb.toString();
	}

	public String getIdSetterCode(boolean aOmitAutogeneratedIds) {
		StringBuilder sb = new StringBuilder();

		sb.append(j.join("\tpublic void setModelId(", this.getClassName(), " aModel, ", this.getIdClassName(), " aId) {\n"));
		if(this.getModelEntityMeta().getIdType() == IdTypeEnum.simple || this.getModelEntityMeta().getIdType() == IdTypeEnum.embedded) {
			sb.append(j.join("\t\taModel.set", this.getModelEntityMeta().getSingularIdName().substring(0, 1).toUpperCase(), this.getModelEntityMeta().getSingularIdName().substring(1), "(aId);\n"));
		} else {
			for(EntityIdMeta meta : this.getModelEntityMeta().getIdPathParameterMetas().values()) {
				sb.append(j.join("\t\t", meta.getReadWritePair()));
			}
		}
		sb.append("\t}");
		
		return sb.toString();
	}
	
	public String getSingularIdName() {
		return this.getModelEntityMeta().getSingularIdName();
	}
	
	public String getIdGenerateCode() throws Exception {
		return this.getIdGenerateCode(false);
	}
	
	public String getIdGenerateCodeOmitAutoGenerated() throws Exception {
		return this.getIdGenerateCode(true);
	}
	
	public String getIdGenerateCode(boolean aOmitAutoGeneratedIds) throws Exception {
		if(this.getModelEntityMeta().getIdType() == IdTypeEnum.simple) {
			return "\t\t";
		}
		else {
			StringBuilder sb = new StringBuilder();
			sb.append(j.join("\t\t", this.getIdClassName(), " ", this.getSingularIdName(), " = new ", this.getIdClassName(), "();\n"));
			for(String paramName : this.getModelEntityMeta().getIdPathParameterMetas().keySet()) {
				if(!(aOmitAutoGeneratedIds && this.getModelEntityMeta().getIdPathParameterMetas().get(paramName).isGenerated())) {
					sb.append(j.join("\t\t", this.generateSetterLine(this.getModelEntityMeta().getSingularIdName(), this.getModelEntityMeta().getIdBeanInfo(), paramName, this.getModelEntityMeta().getIdClass())));
				}
			}
			sb.append(j.join("\n\t\t", ""));
			return sb.toString();
		}
	}
	
	public String getModelVariableName() {
		return EntityMeta.modelVarNameInSetter;
	}
	
	public String getSetModelIdsOmitGeneratedCode() throws Exception {
		return this.getSetModelIdsCode(true);
	}
	
	public String getSetModelIdsCode() throws Exception {
		return this.getSetModelIdsCode(false);
	}
	
	public String getSetModelIdsCode(boolean aOmitAutoGeneratedIds) throws Exception {
		StringBuilder sb = new StringBuilder("\t\t");
		if(this.getModelEntityMeta().getIdType() == IdTypeEnum.simple) {
			if(!(aOmitAutoGeneratedIds && this.getModelEntityMeta().getIdPathParameterMetas().get(this.getModelEntityMeta().getSingularIdName()).isGenerated())) {
				return j.join("\t\t", this.getModelVariableName(), ".", this.getModelEntityMeta().getIdPathParameterMetas().get(this.getSingularIdName()).getPropertyDescriptor().getWriteMethod().getName(), 
						"(", this.getSingularIdName(), ");\n\t\t");
			}
			
			return sb.toString();
		}
		else if(this.getModelEntityMeta().getIdType() == IdTypeEnum.embedded) {
			return j.join(this.getIdGenerateCode(), this.getModelVariableName(), ".", 
					this.getModelEntityMeta().getEmbeddedIdPathParameterMetas().get(this.getSingularIdName()).getPropertyDescriptor().getWriteMethod().getName(),
					"(", this.getSingularIdName(), ");\n\t\t");
		}
		for(String paramName : this.getModelEntityMeta().getIdPathParameterMetas().keySet()) {
			if(!(aOmitAutoGeneratedIds && this.getModelEntityMeta().getIdPathParameterMetas().get(paramName).isGenerated())) {
				sb.append(j.join(this.generateSetterLine(this.getModelVariableName(), this.getModelEntityMeta().getModelBeanInfo(), paramName, this.getModelEntityMeta().getIdClass()), "\t\t"));
			}
		}
//		sb.append("\n\t\t");
//		
		return sb.toString();
	}
	
	public String getUrlIdDelimiter() {
		return urlIdDelimiter;
	}
	/*
	 * *****************************************************************************
	 * Child populators
	 * *****************************************************************************
	 */
	
	private void createChildPopulators() throws Exception {
		this.wrapperPopulator = new ModelWrapperPopulator(this.getModelEntityMeta().getModelClass());
	}
	
	public ModelWrapperPopulator getWrapperPopulator() {
		return this.wrapperPopulator;
	}
}