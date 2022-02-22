/*
 * Created on Aug 24, 2003 by Ravi Mohan
 *  
 */
package aima.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Util {
	public static Set listToSet(List l) {
		Set s = new HashSet();
		for (int i = 0; i < l.size(); i++) {
			s.add(l.get(i));
		}
		return s;
	}

	public static List SetToList(Set s) {
		List l = new ArrayList();
		Iterator i = s.iterator();
		while (i.hasNext()) {
			l.add(i.next());
		}
		return l;
	}

	public static List rest(ArrayList l) {
		List ls = (List) l.clone();
		ls.remove(0);
		return ls;
	}

}