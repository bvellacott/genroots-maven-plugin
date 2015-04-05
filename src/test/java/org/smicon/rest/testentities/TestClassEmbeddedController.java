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

import org.smicon.rest.testentities.EmbeddableCompositeKey;
import java.lang.String;
import org.smicon.rest.testentities.TestClass;
import java.util.Date;
import java.lang.Class;

@Singleton
@Path("embeddeds")
public class TestClassEmbeddedController
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

	@GET
	public Object findAll() throws Exception {
		return wrapAll(findAllModels());
	}

	@POST
	@Path("/{emKey1}::{emKey2}::{emKey3}")
	public TestClassEmbeddedOW create(@PathParam("emKey1") int emKey1, @PathParam("emKey2") String emKey2, @PathParam("emKey3") Date emKey3, TestClassEmbeddedOW aModelOW) throws Exception {
		TestClassEmbedded aModel = aModelOW.unwrap();
		EmbeddableCompositeKey embId = new EmbeddableCompositeKey();
		embId.emKey1 = emKey1;
		embId.emKey2 = emKey2;
		embId.emKey3 = emKey3;
		aModel.setEmbId(embId);
		
		return wrap(createModel(aModel));
	}

	@GET
	@Path("/{emKey1}::{emKey2}::{emKey3}")
	public TestClassEmbeddedOW find(@PathParam("emKey1") int emKey1, @PathParam("emKey2") String emKey2, @PathParam("emKey3") Date emKey3) throws Exception {
		EmbeddableCompositeKey embId = new EmbeddableCompositeKey();
		embId.emKey1 = emKey1;
		embId.emKey2 = emKey2;
		embId.emKey3 = emKey3;
		return wrap(findModel(embId));
	}

	@PUT
	@Path("/{emKey1}::{emKey2}::{emKey3}")
	public TestClassEmbeddedOW update(@PathParam("emKey1") int emKey1, @PathParam("emKey2") String emKey2, @PathParam("emKey3") Date emKey3, TestClassEmbeddedOW aModelOW) throws Exception {
		TestClassEmbedded aModel = aModelOW.unwrap();
		EmbeddableCompositeKey embId = new EmbeddableCompositeKey();
		embId.emKey1 = emKey1;
		embId.emKey2 = emKey2;
		embId.emKey3 = emKey3;
		aModel.setEmbId(embId);

		return wrap(updateModel(aModel));
	}

	@DELETE
	@Path("/{emKey1}::{emKey2}::{emKey3}")
	public TestClassEmbeddedOW delete(@PathParam("emKey1") int emKey1, @PathParam("emKey2") String emKey2, @PathParam("emKey3") Date emKey3) throws Exception {
		EmbeddableCompositeKey embId = new EmbeddableCompositeKey();
		embId.emKey1 = emKey1;
		embId.emKey2 = emKey2;
		embId.emKey3 = emKey3;
		return wrap(deleteModel(embId));
	}

	public static Object wrapAll(List<TestClassEmbedded> aList) {
		final ArrayList<TestClassEmbeddedIW> allWrapped = new ArrayList();
		for(int i = 0 ; i < aList.size(); i++) allWrapped.add(new TestClassEmbeddedIW(aList.get(i))); 
		return new Object(){ @JsonProperty List<TestClassEmbeddedIW> embeddeds = allWrapped; };
	};
	
	public static Object wrapAll(Map<?, TestClassEmbedded> aMap) {
		final HashMap<Object, TestClassEmbeddedIW> allWrapped = new HashMap();
		for(Object key : aMap.keySet()) allWrapped.put(key, new TestClassEmbeddedIW(aMap.get(key))); 
		return new Object(){ @JsonProperty Map<Object, TestClassEmbeddedIW> embeddeds = allWrapped; };
	};
	
	public static TestClassEmbeddedOW wrap(final TestClassEmbedded aResult) {
		return new TestClassEmbeddedOW(aResult);
	}
	
	static class TestClassEmbeddedOW {
		@JsonProperty TestClassEmbeddedIW testclassembedded;
		
		public TestClassEmbeddedOW() { this(new TestClassEmbedded()); };
		public TestClassEmbeddedOW(TestClassEmbedded aWrappable) { testclassembedded = new TestClassEmbeddedIW(aWrappable); };
		public TestClassEmbedded unwrap() { return testclassembedded.unwrap(); }
	}
	
	// Link routes
	public static class TestClassEmbeddedIW {
		TestClassEmbedded testclassembedded;
		
		public TestClassEmbeddedIW() { this(new TestClassEmbedded()); }
		public TestClassEmbeddedIW(TestClassEmbedded aWrappable) { testclassembedded = aWrappable; }

		public TestClassEmbedded unwrap() { return testclassembedded; }

		// Simple properties
		// Model properties with simple id's
		public int getTcl() { return testclassembedded.getTcl().getId1(); }
		public void setTcl(int id1) {
			TestClass tcl = new TestClass();
			tcl.setId1(id1);
			testclassembedded.setTcl(tcl);
		}
		// Model properties with embedded id's
		// Model properties with composite id's
		// Model collection properties
		
		// Links for model collection properties - required by the ember data format
		@JsonProperty Object links = new Object() { 
		};
	}
}