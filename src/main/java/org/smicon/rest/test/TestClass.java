package org.smicon.rest.test;

import javax.persistence.Entity;
import javax.persistence.Id;

import papu.annotations.Model;

//@Entity
@Model(persistenceUnitName = "test-pu", plural = "testclasses") 
public class TestClass{
	
	@Id
	int id1;

	public int getId1() {
		return id1;
	}

	public void setId1(int id1) {
		this.id1 = id1;
	}
	
}
