package aima.basic;

import java.util.Hashtable;

public class ObjectWithDynamicAttributes {
	private Hashtable attributes = new Hashtable();

	public void setAttribute(Object key, Object value) {
		attributes.put(key, value);
	}

	public Object getAttribute(Object key) {
		return attributes.get(key);
	}
}