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

import java.text.ParseException;

import com.fasterxml.jackson.annotation.JsonProperty;

import papu.mvc.Controller;

import org.smicon.rest.testentities.CompositeKey;
import org.smicon.rest.testentities.TestClass;
import org.smicon.rest.testentities.TestClassEmbedded;
import java.lang.String;
import org.smicon.rest.testentities.TestClassComposite;
import org.smicon.rest.testentities.EmbeddableCompositeKey;
import java.util.Date;


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

	@POST
	@Path("/{key1}::{key2}")
	public Object create(@PathParam("key1") int key1, @PathParam("key2") int key2, TestClassCompositeWrap aWrap) throws Exception {
		TestClassCompositeWrapper aModel = aWrap.testClassComposite;
		aModel.setKey1(key1);
		aModel.setKey2(key2);
		return wrap(createModel(aModel.wrapped));
	}

	@GET
	public Object findAll() throws Exception {
		return wrapAll(findAllModels());
	}

	@GET
	@Path("/{key1}::{key2}")
	public Object find(@PathParam("key1") int key1, @PathParam("key2") int key2) throws Exception {
		CompositeKey id = new CompositeKey();
		id.key1 = key1;
		id.key2 = key2;

		return wrap(findModel(id));
	}

	@PUT
	@Path("/{key1}::{key2}")
	public Object update(@PathParam("key1") int key1, @PathParam("key2") int key2, TestClassCompositeWrap aWrap) throws Exception {
		TestClassCompositeWrapper aModel = aWrap.testClassComposite;
		aModel.setKey1(key1);
		aModel.setKey2(key2);
		return wrap(updateModel(aModel.wrapped));
	}

	@DELETE
	@Path("/{key1}::{key2}")
	public Object delete(@PathParam("key1") int key1, @PathParam("key2") int key2) throws Exception {
		CompositeKey id = new CompositeKey();
		id.key1 = key1;
		id.key2 = key2;

		return wrap(deleteModel(id));
	}

	public static Object wrapAll(List<TestClassComposite> aList) {
		final ArrayList<TestClassCompositeWrapper> allWrapped = new ArrayList<TestClassCompositeWrapper>(aList.size());
		for(int i = 0 ; i < aList.size(); i++) allWrapped.add(new TestClassCompositeWrapper(aList.get(i))); 
		return new Object(){ @JsonProperty List<TestClassCompositeWrapper> testcclasses = allWrapped; };
	};
	
	public static Object wrapAll(Map<?, TestClassComposite> aMap) {
		final HashMap<Object, TestClassCompositeWrapper> allWrapped = new HashMap<Object, TestClassCompositeWrapper>(aMap.size());
		for(Object key : aMap.keySet()) allWrapped.put(key, new TestClassCompositeWrapper(aMap.get(key))); 
		return new Object(){ @JsonProperty Map<Object, TestClassCompositeWrapper> testcclasses = allWrapped; };
	};
	
	public static TestClassCompositeWrap wrap(final TestClassComposite aResult) {
		return new TestClassCompositeWrap(aResult);
	}
	
	static class TestClassCompositeWrap {
		@JsonProperty TestClassCompositeWrapper testClassComposite;
		
		public TestClassCompositeWrap() { this(new TestClassComposite()); };
		public TestClassCompositeWrap(TestClassComposite aWrappable) { testClassComposite = new TestClassCompositeWrapper(aWrappable); };
	}
	
	static class TestClassCompositeWrapper {
		TestClassComposite wrapped;
		
		public TestClassCompositeWrapper() { this(new TestClassComposite()); }
		public TestClassCompositeWrapper(TestClassComposite aWrapped) { wrapped = aWrapped; }

		// Simple properties
		public int getKey1() { return wrapped.getKey1(); }
		public void setKey1(int key1) { wrapped.setKey1(key1); }
		public int getKey2() { return wrapped.getKey2(); }
		public void setKey2(int key2) { wrapped.setKey2(key2); }
		// Entity properties with simple id's
		public int getSimple() { return wrapped.getSimple().getId1(); }
		public void setSimple(int id) {
			TestClass entity = new TestClass();
			entity.setId1(id);
			wrapped.setSimple(entity);
		}
		// Entity properties with embedded id's
		public String getEmbedded() { 
			EmbeddableCompositeKey id = wrapped.getEmbedded().getEmbId();
			return (new StringBuilder()).append("").append(id.emKey1).append("::").append(id.emKey2).append("::").append(id.emKey3).toString();
		}
		public void setEmbedded(String composedId) throws ParseException {
			String parts[] = composedId.split("::");
			EmbeddableCompositeKey id = new EmbeddableCompositeKey();
			id.emKey1 = parse(parts[0], int.class);
			id.emKey2 = parse(parts[1], String.class);
			id.emKey3 = parse(parts[2], java.util.Date.class);
			TestClassEmbedded entity = new TestClassEmbedded();
			entity.setEmbId(id);
			wrapped.setEmbedded(entity);
		}
		// Entity properties with composite id's
		public String getComposite() { 
			TestClassComposite entity = wrapped.getComposite();
			return (new StringBuilder()).append("").append(entity.getKey1()).append("::").append(entity.getKey2()).toString(); 
		}
		public void setComposite(String composedId) throws ParseException {
			String[] parts = composedId.split("::");
			TestClassComposite entity = new TestClassComposite();
			entity.setKey1(parse(parts[0], int.class));
			entity.setKey2(parse(parts[1], int.class));
			wrapped.setComposite(entity);
		}
	}

}