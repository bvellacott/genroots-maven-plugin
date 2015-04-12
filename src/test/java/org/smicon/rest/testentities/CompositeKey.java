package org.smicon.rest.testentities;

import javax.persistence.GeneratedValue;

public class CompositeKey {
	
	@GeneratedValue
	int key1;
	int key2;
	int key3;
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + this.key1;
		result = prime * result + this.key2;
		result = prime * result + this.key3;
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		CompositeKey other = (CompositeKey) obj;
		if (this.key1 != other.key1) return false;
		if (this.key2 != other.key2) return false;
		if (this.key3 != other.key3) return false;
		return true;
	}
	
	

}
