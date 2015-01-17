package org.smicon.rest.test;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import papu.annotations.Model;

//@Entity 
@Model(persistenceUnitName = "test-pu", plural = "testclasses") 
public class TestClass{
	
	@Id
//	@GeneratedValue
	int id1;
	
	@OneToOne
	TestClassComposite compost;
	
	@OneToMany(targetEntity=TestClassEmbedded.class)
	List embeddeds;

	public int getId1() {
		return id1;
	}

	public void setId1(int id1) {
		this.id1 = id1;
	}

	public TestClassComposite getCompost() {
		return compost;
	}

	public void setCompost(TestClassComposite compost) {
		this.compost = compost;
	}

	public List<TestClassEmbedded> getEmbeddeds() {
		return embeddeds;
	}

	public void setEmbeddeds(List<TestClassEmbedded> embeddeds) {
		this.embeddeds = embeddeds;
	}
	
}
