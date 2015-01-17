package org.smicon.rest.test;

import java.text.ParseException;
import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonProperty;


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

	@POST
	@Path("/{id1}")
	public Object create(@PathParam("id1") int id1, TestClassWrapper aModel) throws Exception {
		aModel.setId1(id1);
		return wrap(createModel(aModel.wrapped));
	}

	@GET
	public Object findAll() throws Exception {
		return wrapAll(findAllModels());
	}

	@GET
	@Path("/{id1}")
	public Object find(@PathParam("id1") int id1) throws Exception {
		return wrap(findModel(id1));
	}

	@PUT
	@Path("/{id1}")
	public Object update(@PathParam("id1") int id1, TestClassWrapper aModel) throws Exception {
		aModel.setId1(id1);
		return wrap(updateModel(aModel.wrapped));
	}

	@DELETE
	@Path("/{id1}")
	public Object delete(@PathParam("id1") int id1) throws Exception {
		return wrap(deleteModel(id1));
	}

	public static Object wrapAll(List<TestClass> aList) {
		final ArrayList<TestClassWrapper> allWrapped = new ArrayList<TestClassWrapper>(aList.size());
		for(int i = 0 ; i < aList.size(); i++) allWrapped.add(new TestClassWrapper((TestClass)aList.get(i))); 
		return new Object(){ @JsonProperty List<TestClassWrapper> testclasses = allWrapped; };
	};
	
	public static Object wrap(final TestClass aResult) {
		return new Object(){ @JsonProperty TestClassWrapper testclass = new TestClassWrapper(aResult);};
	}
	
	// Link routes
	@GET
	@Path("/{id1}/embeddeds")
	public Object getEmbeddeds(@PathParam("id1") int id1) throws Exception {
		return TestClassEmbeddedRoute.wrapAll(findModel(id1).getEmbeddeds());
	}

	static class TestClassWrapper {
		TestClass wrapped;
		
		// Routes for entity collection properties
		@JsonProperty Object links = new Object() { 
			@JsonProperty String embeddeds = "embeddeds";
		};
		
		public TestClassWrapper() { this(new TestClass()); }
		public TestClassWrapper(TestClass aWrapped) { wrapped = aWrapped; }

		// Simple properties
		public int getId1() { return wrapped.getId1(); }
		public void setId1(int id1) { wrapped.setId1(id1); }
		// Entity properties with composite id's
		public String getCompost() { 
			TestClassComposite entity = wrapped.getCompost();
			return (new StringBuilder()).append("").append(entity.getKey1()).append("::").append(entity.getKey2()).toString(); 
		}
		public void setCompost(String composedId) throws ParseException {
			String[] parts = composedId.split("::");
			TestClassComposite entity = new TestClassComposite();
			entity.setKey1(parse(parts[0], int.class));
			entity.setKey2(parse(parts[1], int.class));
			wrapped.setCompost(entity);
		}
		// Entity collection properties
		public void setEmbeddeds(List embeddeds) { wrapped.setEmbeddeds(embeddeds); }
	}
}