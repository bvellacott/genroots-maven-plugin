package org.smicon.rest.test;

import java.util.Date;

import javax.persistence.Embeddable;

@Embeddable
public class EmbeddableCompositeKey {

	int emKey1;
	String emKey2;
	Date emKey3;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + emKey1;
		result = prime * result + ((emKey2 == null) ? 0 : emKey2.hashCode());
		result = prime * result + ((emKey3 == null) ? 0 : emKey3.hashCode());
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
		EmbeddableCompositeKey other = (EmbeddableCompositeKey) obj;
		if (emKey1 != other.emKey1)
			return false;
		if (emKey2 == null) {
			if (other.emKey2 != null)
				return false;
		} else if (!emKey2.equals(other.emKey2))
			return false;
		if (emKey3 == null) {
			if (other.emKey3 != null)
				return false;
		} else if (!emKey3.equals(other.emKey3))
			return false;
		return true;
	}
	
}
