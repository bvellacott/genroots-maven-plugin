package org.smicon.rest.test;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import papu.annotations.Model;

//@Entity
@Model(persistenceUnitName = "test-pu", plural = "testcclasses")
@IdClass(value = CompositeKey.class)
public class TestClassComposite {
	
	@Id
	private int key1;
	@Id
	private int key2;
	
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
	
}
