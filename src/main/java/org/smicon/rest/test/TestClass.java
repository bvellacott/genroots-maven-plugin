package org.smicon.rest.test;

import javax.persistence.Entity;
import javax.persistence.Id;

import papu.annotations.Model;

//@Entity
@Model(persistenceUnitName = "test-pu", plural = "testclasses") 
public class TestClass{
	
	@Id
	int id1;
	
}
