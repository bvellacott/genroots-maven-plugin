package org.smicon.rest.test;

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

import java.text.ParseException;

import com.fasterxml.jackson.annotation.JsonProperty;

import papu.mvc.Controller;

import org.smicon.rest.test.EmbeddableCompositeKey;
import java.util.Date;
import java.lang.String;
import org.smicon.rest.test.TestClass;


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

	@POST
	@Path("/{emKey1}::{emKey2}::{emKey3}")
	public Object create(@PathParam("emKey1") int emKey1, @PathParam("emKey2") String emKey2, @PathParam("emKey3") Date emKey3, TestClassEmbeddedWrapper aModel) throws Exception {
		EmbeddableCompositeKey embId = new EmbeddableCompositeKey();
		embId.emKey1 = emKey1;
		embId.emKey2 = emKey2;
		embId.emKey3 = emKey3;

		aModel.setEmbId(embId);
		return wrap(createModel(aModel.wrapped));
	}

	@GET
	public Object findAll() throws Exception {
		return wrapAll(findAllModels());
	}

	@GET
	@Path("/{emKey1}::{emKey2}::{emKey3}")
	public Object find(@PathParam("emKey1") int emKey1, @PathParam("emKey2") String emKey2, @PathParam("emKey3") Date emKey3) throws Exception {
		EmbeddableCompositeKey embId = new EmbeddableCompositeKey();
		embId.emKey1 = emKey1;
		embId.emKey2 = emKey2;
		embId.emKey3 = emKey3;

		return wrap(findModel(embId));
	}

	@PUT
	@Path("/{emKey1}::{emKey2}::{emKey3}")
	public Object update(@PathParam("emKey1") int emKey1, @PathParam("emKey2") String emKey2, @PathParam("emKey3") Date emKey3, TestClassEmbeddedWrapper aModel) throws Exception {
		EmbeddableCompositeKey embId = new EmbeddableCompositeKey();
		embId.emKey1 = emKey1;
		embId.emKey2 = emKey2;
		embId.emKey3 = emKey3;

		aModel.setEmbId(embId);
		return wrap(updateModel(aModel.wrapped));
	}

	@DELETE
	@Path("/{emKey1}::{emKey2}::{emKey3}")
	public Object delete(@PathParam("emKey1") int emKey1, @PathParam("emKey2") String emKey2, @PathParam("emKey3") Date emKey3) throws Exception {
		EmbeddableCompositeKey embId = new EmbeddableCompositeKey();
		embId.emKey1 = emKey1;
		embId.emKey2 = emKey2;
		embId.emKey3 = emKey3;

		return wrap(deleteModel(embId));
	}

	public static Object wrapAll(List<TestClassEmbedded> aList) {
		final ArrayList<TestClassEmbeddedWrapper> allWrapped = new ArrayList<TestClassEmbeddedWrapper>(aList.size());
		for(int i = 0 ; i < aList.size(); i++) allWrapped.add(new TestClassEmbeddedWrapper(aList.get(i))); 
		return new Object(){ @JsonProperty List<TestClassEmbeddedWrapper> embeddeds = allWrapped; };
	};
	
	public static Object wrapAll(Map<?, TestClassEmbedded> aMap) {
		final HashMap<Object, TestClassEmbeddedWrapper> allWrapped = new HashMap<Object, TestClassEmbeddedWrapper>(aMap.size());
		for(Object key : aMap.keySet()) allWrapped.put(key, new TestClassEmbeddedWrapper(aMap.get(key))); 
		return new Object(){ @JsonProperty Map<Object, TestClassEmbeddedWrapper> Messages = allWrapped; };
	};
	
	public static Object wrap(final TestClassEmbedded aResult) {
		return new Object(){ @JsonProperty TestClassEmbeddedWrapper testclassembedded = new TestClassEmbeddedWrapper(aResult);};
	}
	
	static class TestClassEmbeddedWrapper {
		TestClassEmbedded wrapped;
		
		public TestClassEmbeddedWrapper() { this(new TestClassEmbedded()); }
		public TestClassEmbeddedWrapper(TestClassEmbedded aWrapped) { wrapped = aWrapped; }

		// Simple properties
		public EmbeddableCompositeKey getEmbId() { return wrapped.getEmbId(); }
		public void setEmbId(EmbeddableCompositeKey embId) { wrapped.setEmbId(embId); }
		// Entity properties with simple id's
		public int getTcl() { return wrapped.getTcl().getId1(); }
		public void setTcl(int id) {
			TestClass entity = new TestClass();
			entity.setId1(id);
			wrapped.setTcl(entity);
		}
	}
}