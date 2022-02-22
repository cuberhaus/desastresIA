package aima.basic;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Util {

	static Random r = new Random();

	public static int min(int i, int j) {
		return (i > j ? j : i);
	}

	public static int max(int i, int j) {
		return (i < j ? j : i);
	}

	public static ArrayList range(int start, int end) {
		ArrayList retVal = new ArrayList();
		for (int i = start; i <= end; i++) {
			retVal.add(new Integer(i));
		}
		return retVal;
	}

	public static ArrayList getFilledArrayList(int size, int value) {
		ArrayList retVal = new ArrayList();
		for (int i = 0; i < size; i++) {
			retVal.add(new Integer(value));
		}
		return retVal;
	}

	public static ArrayList split(String sourceString, String delimiter) {

		Tokenizer tokenizer = new Tokenizer(sourceString, delimiter);
		int tokenCount = tokenizer.countTokens();
		ArrayList result = new ArrayList();
		for (int i = 0; i < tokenCount; i++) {
			result.add(tokenizer.nextToken());
		}
		if (result.get(result.size() - 1).equals("")) {
			result.remove(result.size() - 1);
		} //weird extra space token at the end ... fix later
		return result;

	}

	public static ArrayList getKeysAsArrayList(Hashtable h) {
		ArrayList retVal = new ArrayList();
		Set s = h.keySet();
		Iterator i = s.iterator();
		while (i.hasNext()) {
			retVal.add(i.next());
		}
		return retVal;
	}

	public static void printHashtable(Hashtable h) {
		Enumeration e = h.keys();
		while (e.hasMoreElements()) {
			Object key = e.nextElement();
			String keyString = key.toString();
			String valueString = h.get(key).toString();
			System.out.println(keyString + " : " + valueString);

		}

	}

	public Object first(List l) {
		List temp = new ArrayList();
		//copy l in totemp
		for (int i = 0; i < l.size(); i++) {
			temp.add(l.get(i));
		}
		Object retVal = temp.remove(0);
		return retVal;

	}

	public List rest(List l) {
		List temp = new ArrayList();
		//copy l in totemp
		for (int i = 0; i < l.size(); i++) {
			temp.add(l.get(i));
		}
		Object retVal = temp.remove(0);
		return temp;

	}

	public static Object selectRandomlyFromList(List l) {
		//System.out.println("size = " +l.size());
		int index = r.nextInt(l.size());
		//System.out.println(index);
		return l.get(index);
	}

	public static void printHashtableKeys(Hashtable h) {
		Enumeration e = h.keys();
		while (e.hasMoreElements()) {
			Object key = e.nextElement();
			String keyString = key.toString();

			System.out.println(keyString);

		}
	}
}