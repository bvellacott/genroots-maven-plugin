package org.smicon.rest.testentities;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import papu.annotations.Model;

@Entity 
@Model(persistenceUnitName = "test-pu", plural = "testclasses") 
public class TestClassAllIdsGenerated
{
	
	int id1;
	
	TestClassEmbedded embed;
	
	Map<Long, TestClassComposite> composites;

	@Id
	@GeneratedValue
	public int getId1() {
		return id1;
	}

	public void setId1(int id1) {
		this.id1 = id1;
	}

	@OneToOne
	public TestClassEmbedded getEmbed() {
		return embed;
	}

	public void setEmbed(TestClassEmbedded embed) {
		this.embed = embed;
	}

	@OneToMany(targetEntity=TestClassComposite.class)
	public Map<Long, TestClassComposite> getComposites() {
		return composites;
	}

	public void setComposites(Map<Long, TestClassComposite> composites) {
		this.composites = composites;
	}
	
}
