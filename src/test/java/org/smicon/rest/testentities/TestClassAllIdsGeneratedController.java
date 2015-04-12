package org.smicon.rest.testentities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import org.smicon.rest.testentities.TestClassCompositeController.TestClassCompositeIW;

import papu.mvc.Controller;

import com.fasterxml.jackson.annotation.JsonProperty;

@Singleton
@Path("testclasses")
public class TestClassAllIdsGeneratedController
extends
Controller<TestClassAllIdsGenerated, Integer>
{

	@PersistenceUnit(unitName = "test-pu")
	private EntityManagerFactory emf;
	
	@Override
	public Class<TestClassAllIdsGenerated> getModelClass() {
		return TestClassAllIdsGenerated.class;
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
	@Path("")
	public TestClassAllIdsGeneratedOW create(TestClassAllIdsGeneratedOW aModelOW) throws Exception {
		TestClassAllIdsGenerated aModel = aModelOW.unwrap();
		
		return wrap(createModel(aModel));
	}

	@GET
	@Path("/{id1}")
	public TestClassAllIdsGeneratedOW find(@PathParam("id1") int id1) throws Exception {
		return wrap(findModel(id1));
	}

	@PUT
	@Path("/{id1}")
	public TestClassAllIdsGeneratedOW update(@PathParam("id1") int id1, TestClassAllIdsGeneratedOW aModelOW) throws Exception {
		TestClassAllIdsGenerated aModel = aModelOW.unwrap();
		aModel.setId1(id1);

		return wrap(updateModel(aModel));
	}

	@DELETE
	@Path("/{id1}")
	public TestClassAllIdsGeneratedOW delete(@PathParam("id1") int id1) throws Exception {
		return wrap(deleteModel(id1));
	}

	public static Object wrapAll(List<TestClassAllIdsGenerated> aList) {
		final ArrayList<TestClassAllIdsGeneratedIW> allWrapped = new ArrayList();
		for(int i = 0 ; i < aList.size(); i++) allWrapped.add(new TestClassAllIdsGeneratedIW(aList.get(i))); 
		return new Object(){ @JsonProperty List<TestClassAllIdsGeneratedIW> testclasses = allWrapped; };
	};
	
	public static Object wrapAll(Map<?, TestClassAllIdsGenerated> aMap) {
		final HashMap<Object, TestClassAllIdsGeneratedIW> allWrapped = new HashMap();
		for(Object key : aMap.keySet()) allWrapped.put(key, new TestClassAllIdsGeneratedIW(aMap.get(key))); 
		return new Object(){ @JsonProperty Map<Object, TestClassAllIdsGeneratedIW> testclasses = allWrapped; };
	};
	
	public static TestClassAllIdsGeneratedOW wrap(final TestClassAllIdsGenerated aResult) {
		return new TestClassAllIdsGeneratedOW(aResult);
	}
	
	static class TestClassAllIdsGeneratedOW {
		@JsonProperty TestClassAllIdsGeneratedIW testclassallidsgenerated;
		
		public TestClassAllIdsGeneratedOW() { this(new TestClassAllIdsGenerated()); };
		public TestClassAllIdsGeneratedOW(TestClassAllIdsGenerated aWrappable) { testclassallidsgenerated = new TestClassAllIdsGeneratedIW(aWrappable); };
		public TestClassAllIdsGenerated unwrap() { return testclassallidsgenerated.unwrap(); }
	}
	
	// Link routes
	@GET
	@Path("/{id1}/composites")
	public Object getComposites(@PathParam("id1") int id1) throws Exception {
		return TestClassCompositeController.wrapAll(findModel(id1).getComposites());
	}
	
	public static class TestClassAllIdsGeneratedIW {
		TestClassAllIdsGenerated testclassallidsgenerated;
		
		public TestClassAllIdsGeneratedIW() { this(new TestClassAllIdsGenerated()); }
		public TestClassAllIdsGeneratedIW(TestClassAllIdsGenerated aWrappable) { testclassallidsgenerated = aWrappable; }

		public TestClassAllIdsGenerated unwrap() { return testclassallidsgenerated; }

		// Simple properties
		public int getId1() { return testclassallidsgenerated.getId1(); }
		public void setId1(int id1) { testclassallidsgenerated.setId1(id1); }
		// Model properties with simple id's
		// Model properties with embedded id's
		public String getEmbed() {
			EmbeddableCompositeKey embId = testclassallidsgenerated.getEmbed().getEmbId();
			return (new StringBuilder()).append(embId.emKey1).append("::").append(embId.emKey2).append("::").append(embId.emKey3).toString();
		}
		public void setEmbed(String embIdSerial) throws Exception {
			String[] parts = embIdSerial.split("::");
			EmbeddableCompositeKey embId = new EmbeddableCompositeKey();
			embId.emKey1 = parse(parts[0], int.class);
			embId.emKey2 = parse(parts[1], String.class);
			embId.emKey3 = parse(parts[2], Date.class);
			TestClassEmbedded embed = new TestClassEmbedded();
			embed.setEmbId(embId);
			testclassallidsgenerated.setEmbed(embed);
		}
		// Model properties with composite id's
		// Model collection properties
		public void setComposites(Map<java.lang.Long, TestClassCompositeIW> TestClassComposites) { 
			Map<java.lang.Long, TestClassComposite> composites = new java.util.HashMap();
			for(java.lang.Long _key_ : TestClassComposites.keySet()) 
				composites.put(_key_, TestClassComposites.get(_key_).unwrap());
			testclassallidsgenerated.setComposites(composites); 
		}
		
		// Links for model collection properties - required by the ember data format
		@JsonProperty Object links = new Object() { 
			@JsonProperty String composites = "testcclasses";
		};
	}
}