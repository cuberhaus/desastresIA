package aima.util;

import java.util.Hashtable;

public class UniversalDictionary extends Hashtable {
	//Very minimal implementation
	Object value;

	public UniversalDictionary(Object val) {
		value = val;
	}

	public Object get(Object key) {
		return value;
	}

	public Object put(Object key, Object value) {
		return value;
	}
}