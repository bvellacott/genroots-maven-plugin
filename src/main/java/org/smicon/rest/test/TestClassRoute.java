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

import java.lang.Integer;
import org.smicon.rest.test.TestClass;


@Singleton
@Path("testclasses")
public class TestClassRoute 
extends
Controller<TestClass, Integer>
{

	@PersistenceUnit(unitName = "test-pu")
	private EntityManagerFactory emf;
	
	@Override
	public Class<TestClass> getModelClass() {
		return TestClass.class;
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory()
	{
		return emf;
	}
	
	@Override
	public void setModelId(TestClass aModel, Integer aId) {
		aModel.id1 = aId;
	}
	
	@POST
	public TestClass createModel(TestClass aMsg ) throws Exception {
		return create(aMsg);
	}

	@GET
	public List<TestClass> findAllMessages() throws Exception {
		return findAll(this.getModelClass());
	}

	@GET
	@Path("/{id}")
	public TestClass findMessage(@PathParam("id") Integer aId ) throws Exception {
		return find(aId);
	}

	@PUT
	@Path("/{id}")
	public TestClass updateModel(@PathParam("id") Integer aId, TestClass aMsg ) throws Exception {
		return update(aId, aMsg);
	}

	@DELETE
	@Path("/{id}")
	public TestClass deleteModel(@PathParam("id") Integer aId ) throws Exception {
		return delete(aId, this.getModelClass());
	}

}
