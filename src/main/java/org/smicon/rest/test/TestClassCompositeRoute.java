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

import papu.mvc.Controller;


@Singleton
@Path("testcclasses")
public class TestClassCompositeRoute 
extends
Controller<TestClassComposite, CompositeKey>
{

	@PersistenceUnit(unitName = "test-pu")
	private EntityManagerFactory emf;
	
	@Override
	public Class<TestClassComposite> getModelClass() {
		return TestClassComposite.class;
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory()
	{
		return emf;
	}
	
	@Override
	public void setModelId(TestClassComposite aModel, CompositeKey aId) {
		Integer key1 = aId.key1;
		aModel.key1 = aId.key1;
		Integer key2 = aId.key2;
		aModel.key2 = aId.key2;
	}
	
	@POST
	public TestClassComposite createModel(TestClassComposite aMsg ) throws Exception {
		return create(aMsg);
	}

	@GET
	public List<TestClassComposite> findAllMessages() throws Exception {
		return findAll(this.getModelClass());
	}

	@GET
	@Path("/id")
	public TestClassComposite findMessage( CompositeKey aId ) throws Exception {
		return find(aId);
	}

	@PUT
	@Path("/id")
	public TestClassComposite updateModel( CompositeKey aId, TestClassComposite aMsg ) throws Exception {
		return update(aId, aMsg);
	}

	@DELETE
	@Path("/id")
	public TestClassComposite deleteModel( CompositeKey aId ) throws Exception {
		return delete(aId, this.getModelClass());
	}

}
