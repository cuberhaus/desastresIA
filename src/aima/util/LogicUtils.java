/*
 * Created on May 3, 2003 by Ravi Mohan
 *  
 */
package aima.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class LogicUtils {
	private static Random r = new Random();

	public static List setToList(Set s) {

		List retVal = new ArrayList(s);
		return retVal;

	}

	public static Set listToSet(List l) {

		Set retVal = new HashSet(l);
		return retVal;

	}

	public static Set union(Set one, Set two) {
		//		HashSet three = new HashSet();
		//		Iterator iteratorOne = one.iterator();
		//		while (iteratorOne.hasNext()) {
		//			Object sym = iteratorOne.next();
		//			three.add(sym);
		//		}
		//
		//		Iterator iteratorTwo = two.iterator();
		//		while (iteratorTwo.hasNext()) {
		//			Object sym = iteratorTwo.next();
		//			if (!(in(three, sym))) {
		//				three.add(sym);
		//			}
		//		}
		//      return three;
		Set union = new HashSet(one);
		union.addAll(two);
		return union;
	}

	public static Set intersection(Set one, Set two) {
		//		Set three = new HashSet();
		//		Iterator iteratorOne = one.iterator();
		//		while (iteratorOne.hasNext()) {
		//			Object sym = iteratorOne.next();
		//			if (in(two, sym)) {
		//				three.add(sym);
		//			}
		//		}
		//		return three;
		Set intersection = new HashSet(one);
		intersection.retainAll(two);
		return intersection;
	}

	public static boolean in(Set s, Object o) {
		boolean retVal = false;
		Iterator i = s.iterator();
		while (i.hasNext()) {
			Object obj = i.next();
			if (obj.equals(o)) {
				retVal = true;
				break;
			}
		}
		return retVal;
	}

	public static boolean in(List l, Object o) {
		boolean retVal = false;
		Iterator i = l.iterator();
		while (i.hasNext()) {
			Object obj = i.next();
			if (obj.equals(o)) {
				retVal = true;
				break;
			}
		}
		return retVal;
	}

	public static Set difference(Set one, Set two) {
		Set three = new HashSet();
		Iterator iteratorOne = one.iterator();
		while (iteratorOne.hasNext()) {
			Object sym = iteratorOne.next();
			if (!(in(two, sym))) {
				three.add(sym);
			}
		}
		return three;
	}

	public static Object first(List l) {
		List newList = (List) ((ArrayList) l).clone();
		return newList.get(0);
	}

	public static List rest(List l) {
		List newList = (List) ((ArrayList) l).clone();
		newList.remove(0);
		return newList;
	}

	public static List merge(List one, List two) {
		Set setOne = listToSet(one);
		Set setTwo = listToSet(two);
		Set setThree = union(setOne, setTwo);
		return setToList(setThree);
	}

	public static boolean randomBoolean() {
		int trueOrFalse = r.nextInt(2);
		return (!(trueOrFalse == 0));
	}

}