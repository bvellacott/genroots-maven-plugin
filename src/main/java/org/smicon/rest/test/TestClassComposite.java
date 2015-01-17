package org.smicon.rest.test;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import papu.annotations.Model;

//@Entity 
@Model(persistenceUnitName = "test-pu", plural = "testcclasses")
@IdClass(value = CompositeKey.class)
public class TestClassComposite {
	
	@Id
	private int key1;
	@Id
	private int key2;
	
	@OneToOne(mappedBy="compost")
	TestClass simple;
	
	@OneToOne(mappedBy="compost")
	TestClassEmbedded embedded;
	
	@OneToOne(mappedBy="compost")
	TestClassComposite composite;
	
	public int getKey1() {
		return key1;
	}
	public void setKey1(int key1) {
		this.key1 = key1;
	}
	public int getKey2() {
		return key2;
	}
	public void setKey2(int key2) {
		this.key2 = key2;
	}
	public TestClass getSimple() {
		return simple;
	}
	public void setSimple(TestClass simple) {
		this.simple = simple;
	}
	public TestClassEmbedded getEmbedded() {
		return embedded;
	}
	public void setEmbedded(TestClassEmbedded embedded) {
		this.embedded = embedded;
	}
	public TestClassComposite getComposite() {
		return composite;
	}
	public void setComposite(TestClassComposite composite) {
		this.composite = composite;
	}
	
}
