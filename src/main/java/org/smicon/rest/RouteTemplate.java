package org.smicon.rest;

import java.util.HashSet;

public class RouteTemplate
{
	private String packageName;
	private String className;
	private String idClassName;
	private String routeClassName;
	private String plural;
	private String persistenceUnitName;
	private String idPathExtension;
	private String pathParamDeclaration;
	private HashSet<Class> imports = new HashSet<Class>();
	private String idSetterCode;
	
	public String getPackageName() {
		return packageName;
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getPlural() {
		return plural;
	}
	
	public void setPlural(String plural) {
		this.plural = plural;
	}

	public String getPersistenceUnitName() {
		return persistenceUnitName;
	}

	public void setPersistenceUnitName(String persistenceUnitName) {
		this.persistenceUnitName = persistenceUnitName;
	}

	public String getRouteClassName() {
		return routeClassName;
	}

	public void setRouteClassName(String routeClassName) {
		this.routeClassName = routeClassName;
	}

	public String getIdPathExtension() {
		return idPathExtension;
	}

	public void setIdPathExtension(String idPathExtension) {
		this.idPathExtension = idPathExtension;
	}

	public String getPathParamDeclaration() {
		return pathParamDeclaration;
	}

	public void setPathParamDeclaration(String pathParamDeclaration) {
		this.pathParamDeclaration = pathParamDeclaration;
	}

	public String getImports() {
		StringBuilder sb = new StringBuilder();
		for(Class cls : this.imports)
		{
			sb.append("import ").append(cls.getName()).append(";\n");
		}
		return sb.toString();
	}

	public void addImport(Class aImport) {
		this.imports.add(aImport);
	}

	public String getIdSetterCode() {
		return idSetterCode;
	}

	public void setIdSetterCode(String idSetterCode) {
		this.idSetterCode = idSetterCode;
	}

	public String getIdClassName() {
		return idClassName;
	}

	public void setIdClassName(String idClassName) {
		this.idClassName = idClassName;
	}

}
