/*
 * Created on Sep 21, 2004
 *
 */
package aima.search.csp;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ravi Mohan
 *  
 */
public class Domains {
	private Hashtable hash;

	//a hash Of Lists { variable: ListOfDomainValues
	public Domains(List variables) {
		this.hash = new Hashtable();
		Iterator varIter = variables.iterator();
		while (varIter.hasNext()) {
			hash.put(varIter.next(), new ArrayList());
		}
	}

	public List getDomainOf(String variable) {
		return (List) hash.get(variable);
	}

	public void addToDomain(String variable, Object value) {
		List varDomains = (List) hash.get(variable);

		if (!(varDomains.contains(value))) {
			varDomains.add(value);
		}
	}

	public void addToDomain(String variable, List values) {
		List varDomains = (List) hash.get(variable);
		for (int i = 0; i < values.size(); i++) {
			addToDomain(variable, values.get(i));
		}

	}

	public void removeFromDomain(String variable, Object value) {
		List varDomains = (List) hash.get(variable);
		varDomains.remove(value);
	}

	public String toString() {
		return hash.toString();
	}

}