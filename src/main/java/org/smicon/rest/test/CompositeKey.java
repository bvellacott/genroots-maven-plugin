package org.smicon.rest.test;

public class CompositeKey {
	
	int key1;
	int key2;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + key1;
		result = prime * result + key2;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompositeKey other = (CompositeKey) obj;
		if (key1 != other.key1)
			return false;
		if (key2 != other.key2)
			return false;
		return true;
	}
	
	

}
