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
	public EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}

	@Override
	public void setModelId(TestClass aModel, Integer aId) {
		aModel.setId1(aId);
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
	@Path("/{id1}")
	public TestClass findMessage(@PathParam("id1") int id1) throws Exception {
		return find(id1);
	}

	@PUT
	@Path("/{id1}")
	public TestClass updateModel(@PathParam("id1") int id1, TestClass aMsg ) throws Exception {
		return update(id1, aMsg);
	}

	@DELETE
	@Path("/{id1}")
	public TestClass deleteModel(@PathParam("id1") int id1) throws Exception {
		return delete(id1, this.getModelClass());
	}

}
