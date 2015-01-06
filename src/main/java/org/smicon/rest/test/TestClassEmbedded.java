package org.smicon.rest.test;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import papu.annotations.Model;

@Entity
@Model(persistenceUnitName = "test-pu", plural = "embeddeds")
public class TestClassEmbedded {
	
	@EmbeddedId
	private EmbeddableCompositeKey embId;

	public EmbeddableCompositeKey getEmbId() {
		return embId;
	}

	public void setEmbId(EmbeddableCompositeKey embId) {
		this.embId = embId;
	}

}
