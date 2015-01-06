package org.smicon.rest.test;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import papu.mvc.Controller;

import java.lang.String;
import java.util.Date;
import org.smicon.rest.test.EmbeddableCompositeKey;
import java.lang.Integer;


@Singleton
@Path("embeddeds")
public class TestClassEmbeddedRoute 
extends
Controller<TestClassEmbedded, EmbeddableCompositeKey>
{

	@PersistenceUnit(unitName = "test-pu")
	private EntityManagerFactory emf;
	
	@Override
	public Class<TestClassEmbedded> getModelClass() {
		return TestClassEmbedded.class;
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}

	@Override
	public void setModelId(TestClassEmbedded aModel, EmbeddableCompositeKey aId) {
		aModel.setEmbId(aId);
	}
	
	@POST
	public TestClassEmbedded createModel(TestClassEmbedded aMsg ) throws Exception {
		return create(aMsg);
	}

	@GET
	public List<TestClassEmbedded> findAllMessages() throws Exception {
		return findAll(this.getModelClass());
	}

	@GET
	@Path("/{emKey1}/{emKey2}/{emKey3}")
	public TestClassEmbedded findMessage(@PathParam("emKey1") int emKey1, @PathParam("emKey2") String emKey2, @PathParam("emKey3") Date emKey3) throws Exception {
		EmbeddableCompositeKey embId = new EmbeddableCompositeKey();
		embId.emKey1 = emKey1;
		embId.emKey2 = emKey2;
		embId.emKey3 = emKey3;

		return find(embId);
	}

	@PUT
	@Path("/{emKey1}/{emKey2}/{emKey3}")
	public TestClassEmbedded updateModel(@PathParam("emKey1") int emKey1, @PathParam("emKey2") String emKey2, @PathParam("emKey3") Date emKey3, TestClassEmbedded aMsg ) throws Exception {
		EmbeddableCompositeKey embId = new EmbeddableCompositeKey();
		embId.emKey1 = emKey1;
		embId.emKey2 = emKey2;
		embId.emKey3 = emKey3;

		return update(embId, aMsg);
	}

	@DELETE
	@Path("/{emKey1}/{emKey2}/{emKey3}")
	public TestClassEmbedded deleteModel(@PathParam("emKey1") int emKey1, @PathParam("emKey2") String emKey2, @PathParam("emKey3") Date emKey3) throws Exception {
		EmbeddableCompositeKey embId = new EmbeddableCompositeKey();
		embId.emKey1 = emKey1;
		embId.emKey2 = emKey2;
		embId.emKey3 = emKey3;

		return delete(embId, this.getModelClass());
	}

}
