package org.smicon.rest.test;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import papu.annotations.Model;

//@Entity 
@Model(persistenceUnitName = "test-pu", plural = "embeddeds")
public class TestClassEmbedded {
	
	@EmbeddedId
	private EmbeddableCompositeKey embId;
	
	@OneToOne
	private TestClass tcl;

	public EmbeddableCompositeKey getEmbId() {
		return embId;
	}

	public void setEmbId(EmbeddableCompositeKey embId) {
		this.embId = embId;
	}

	public TestClass getTcl() {
		return tcl;
	}

	public void setTcl(TestClass tcl) {
		this.tcl = tcl;
	}

}
