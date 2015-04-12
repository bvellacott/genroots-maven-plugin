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

import java.util.Date;
import org.smicon.rest.testentities.CompositeKey;
import java.lang.Class;
import java.lang.Class;
import org.smicon.rest.testentities.TestClassComposite;
import org.smicon.rest.testentities.CompositeKey;
import org.smicon.rest.testentities.EmbeddableCompositeKey;
import org.smicon.rest.testentities.TestClass;
import java.lang.String;
import org.smicon.rest.testentities.TestClassEmbedded;
import org.smicon.rest.testentities.EmbeddableCompositeKey;

@Singleton
@Path("testcclasses")
public class TestClassCompositeController
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
	public EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}

	@GET
	public Object findAll() throws Exception {
		return wrapAll(findAllModels());
	}

	@POST
	@Path("/{key1}::{key2}")
	public TestClassCompositeOW create(@PathParam("key1") int key1, @PathParam("key2") int key2, TestClassCompositeOW aModelOW) throws Exception {
		TestClassComposite aModel = aModelOW.unwrap();
		aModel.setKey1(key1);
		aModel.setKey2(key2);
		
		return wrap(createModel(aModel));
	}

	@GET
	@Path("/{key1}::{key2}::{key3}")
	public TestClassCompositeOW find(@PathParam("key1") int key1, @PathParam("key2") int key2, @PathParam("key3") int key3) throws Exception {
		CompositeKey id = new CompositeKey();
		id.key1 = key1;
		id.key2 = key2;
		id.key3 = key3;
		return wrap(findModel(id));
	}

	@PUT
	@Path("/{key1}::{key2}::{key3}")
	public TestClassCompositeOW update(@PathParam("key1") int key1, @PathParam("key2") int key2, @PathParam("key3") int key3, TestClassCompositeOW aModelOW) throws Exception {
		TestClassComposite aModel = aModelOW.unwrap();
		aModel.setKey1(key1);
		aModel.setKey2(key2);
		aModel.setKey3(key3);

		return wrap(updateModel(aModel));
	}

	@DELETE
	@Path("/{key1}::{key2}::{key3}")
	public TestClassCompositeOW delete(@PathParam("key1") int key1, @PathParam("key2") int key2, @PathParam("key3") int key3) throws Exception {
		CompositeKey id = new CompositeKey();
		id.key1 = key1;
		id.key2 = key2;
		id.key3 = key3;
		return wrap(deleteModel(id));
	}

	public static Object wrapAll(List<TestClassComposite> aList) {
		final ArrayList<TestClassCompositeIW> allWrapped = new ArrayList();
		for(int i = 0 ; i < aList.size(); i++) allWrapped.add(new TestClassCompositeIW(aList.get(i))); 
		return new Object(){ @JsonProperty List<TestClassCompositeIW> testcclasses = allWrapped; };
	};
	
	public static Object wrapAll(Map<?, TestClassComposite> aMap) {
		final HashMap<Object, TestClassCompositeIW> allWrapped = new HashMap();
		for(Object key : aMap.keySet()) allWrapped.put(key, new TestClassCompositeIW(aMap.get(key))); 
		return new Object(){ @JsonProperty Map<Object, TestClassCompositeIW> testcclasses = allWrapped; };
	};
	
	public static TestClassCompositeOW wrap(final TestClassComposite aResult) {
		return new TestClassCompositeOW(aResult);
	}
	
	static class TestClassCompositeOW {
		@JsonProperty TestClassCompositeIW testclasscomposite;
		
		public TestClassCompositeOW() { this(new TestClassComposite()); };
		public TestClassCompositeOW(TestClassComposite aWrappable) { testclasscomposite = new TestClassCompositeIW(aWrappable); };
		public TestClassComposite unwrap() { return testclasscomposite.unwrap(); }
	}
	
	// Link routes
	public static class TestClassCompositeIW {
		TestClassComposite testclasscomposite;
		
		public TestClassCompositeIW() { this(new TestClassComposite()); }
		public TestClassCompositeIW(TestClassComposite aWrappable) { testclasscomposite = aWrappable; }

		public TestClassComposite unwrap() { return testclasscomposite; }

		// Simple properties
		public int getKey1() { return testclasscomposite.getKey1(); }
		public void setKey1(int key1) { testclasscomposite.setKey1(key1); }
		public int getKey2() { return testclasscomposite.getKey2(); }
		public void setKey2(int key2) { testclasscomposite.setKey2(key2); }
		public int getKey3() { return testclasscomposite.getKey3(); }
		public void setKey3(int key3) { testclasscomposite.setKey3(key3); }
		// Model properties with simple id's
		public int getSimple() { return testclasscomposite.getSimple().getId1(); }
		public void setSimple(int id1) {
			TestClass simple = new TestClass();
			simple.setId1(id1);
			testclasscomposite.setSimple(simple);
		}
		// Model properties with embedded id's
		public String getEmbedded() {
			EmbeddableCompositeKey embId = testclasscomposite.getEmbedded().getEmbId();
			return (new StringBuilder()).append(embId.emKey1).append("::").append(embId.emKey2).append("::").append(embId.emKey3).toString();
		}
		public void setEmbedded(String embIdSerial) throws Exception {
			String[] parts = embIdSerial.split("::");
			EmbeddableCompositeKey embId = new EmbeddableCompositeKey();
			embId.emKey1 = parse(parts[0], int.class);
			embId.emKey2 = parse(parts[1], String.class);
			embId.emKey3 = parse(parts[2], Date.class);
			TestClassEmbedded embedded = new TestClassEmbedded();
			embedded.setEmbId(embId);
			testclasscomposite.setEmbedded(embedded);
		}
		// Model properties with composite id's
		public String getComposite() { 
			TestClassComposite composite = testclasscomposite.getComposite();
			return (new StringBuilder()).append(composite.getKey1()).append("::").append(composite.getKey2()).append("::").append(composite.getKey3()).toString(); 
		}
		public void setComposite(String id) throws Exception {
			String[] parts = id.split("::");
			TestClassComposite composite = new TestClassComposite();
			composite.setKey1(parse(parts[0], int.class));
			composite.setKey2(parse(parts[1], int.class));
			composite.setKey3(parse(parts[2], int.class));
			testclasscomposite.setComposite(composite);
		}
		// Model collection properties
		
		// Links for model collection properties - required by the ember data format
		@JsonProperty Object links = new Object() { 
		};
	}
}