package org.smicon.rest.test;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import papu.annotations.Model;

@Entity
@Model(persistenceUnitName = "test-pu", plural = "testcclasses")
@IdClass(value = CompositeKey.class)
public class TestClassComposite {
	
	@Id
	int key1;
	@Id
	int key2;
	
}
