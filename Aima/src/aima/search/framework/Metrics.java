/*
 * Created on Sep 8, 2004
 *  
 */
package aima.search.framework;

import java.util.Hashtable;
import java.util.Set;

public class Metrics {
	private Hashtable hash;

	public Metrics() {
		this.hash = new Hashtable();
	}

	public void set(String name, int i) {
		hash.put(name, Integer.toString(i));
	}

	public int getInt(String name) {
		return new Integer((String) hash.get(name)).intValue();
	}

	public void set(String name, Object value) {
		hash.put(name, value);
	}

	public Object get(String name) {
		return hash.get(name);
	}

	public Set keySet() {
		return hash.keySet();
	}

	public String getString(String key) {
		return (String) hash.get(key);
	}

}