package org.smicon.rest.testentities;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.fasterxml.jackson.annotation.JsonProperty;

import papu.mvc.Controller;

import org.smicon.rest.testentities.TestClassComposite;
import org.smicon.rest.testentities.TestClassEmbedded;
import java.util.ArrayList;
import org.smicon.rest.testentities.CompositeKey;
import java.util.List;
import static org.smicon.rest.testentities.TestClassEmbeddedController.*;

@Singleton
@Path("testclasses")
public class TestClassController
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

	@GET
	public Object findAll() throws Exception {
		return wrapAll(findAllModels());
	}

	@POST
	@Path("/{id1}")
	public TestClassOW create(@PathParam("id1") int id1, TestClassOW aModelOW) throws Exception {
		TestClass aModel = aModelOW.unwrap();
		aModel.setId1(id1);
		
		return wrap(createModel(aModel));
	}

	@GET
	@Path("/{id1}")
	public TestClassOW find(@PathParam("id1") int id1) throws Exception {
		return wrap(findModel(id1));
	}

	@PUT
	@Path("/{id1}")
	public TestClassOW update(@PathParam("id1") int id1, TestClassOW aModelOW) throws Exception {
		TestClass aModel = aModelOW.unwrap();
		aModel.setId1(id1);

		return wrap(updateModel(aModel));
	}

	@DELETE
	@Path("/{id1}")
	public TestClassOW delete(@PathParam("id1") int id1) throws Exception {
		return wrap(deleteModel(id1));
	}

	public static Object wrapAll(List<TestClass> aList) {
		final ArrayList<TestClassIW> allWrapped = new ArrayList();
		for(int i = 0 ; i < aList.size(); i++) allWrapped.add(new TestClassIW(aList.get(i))); 
		return new Object(){ @JsonProperty List<TestClassIW> testclasses = allWrapped; };
	};
	
	public static Object wrapAll(Map<?, TestClass> aMap) {
		final HashMap<Object, TestClassIW> allWrapped = new HashMap();
		for(Object key : aMap.keySet()) allWrapped.put(key, new TestClassIW(aMap.get(key))); 
		return new Object(){ @JsonProperty Map<Object, TestClassIW> testclasses = allWrapped; };
	};
	
	public static TestClassOW wrap(final TestClass aResult) {
		return new TestClassOW(aResult);
	}
	
	static class TestClassOW {
		@JsonProperty TestClassIW testclass;
		
		public TestClassOW() { this(new TestClass()); };
		public TestClassOW(TestClass aWrappable) { testclass = new TestClassIW(aWrappable); };
		public TestClass unwrap() { return testclass.unwrap(); }
	}
	
	// Link routes
	@GET
	@Path("/{id1}/embeddeds")
	public Object getEmbeddeds(@PathParam("id1") int id1) throws Exception {
		return TestClassEmbeddedController.wrapAll(findModel(id1).getEmbeddeds());
	}
	
	public static class TestClassIW {
		TestClass testclass;
		
		public TestClassIW() { this(new TestClass()); }
		public TestClassIW(TestClass aWrappable) { testclass = aWrappable; }

		public TestClass unwrap() { return testclass; }

		// Simple properties
		public int getId1() { return testclass.getId1(); }
		public void setId1(int id1) { testclass.setId1(id1); }
		// Model properties with simple id's
		// Model properties with embedded id's
		// Model properties with composite id's
		public String getCompost() { 
			TestClassComposite compost = testclass.getCompost();
			return (new StringBuilder()).append(compost.getKey1()).append("::").append(compost.getKey2()).toString(); 
		}
		public void setCompost(String id) throws Exception {
			String[] parts = id.split("::");
			TestClassComposite compost = new TestClassComposite();
			compost.setKey1(parse(parts[0], int.class));
			compost.setKey2(parse(parts[1], int.class));
			testclass.setCompost(compost);
		}
		// Model collection properties
		public void setEmbeddeds(List<TestClassEmbeddedIW> TestClassEmbeddeds) { 
			List<TestClassEmbedded> embeddeds = new java.util.ArrayList();
			for(TestClassEmbeddedIW TestClassEmbeddedvar : TestClassEmbeddeds) 
				embeddeds.add(TestClassEmbeddedvar.unwrap());
			testclass.setEmbeddeds(embeddeds); 
		}
		
		// Links for model collection properties - required by the ember data format
		@JsonProperty Object links = new Object() { 
			@JsonProperty String embeddeds = "embeddeds";
		};
	}
}