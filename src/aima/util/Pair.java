package aima.util;

public class Pair {
	Object first;

	Object second;

	public Object getFirst() {
		return first;
	}

	public Object getSecond() {
		return second;
	}

	public Pair(Object a, Object b) {
		first = a;
		second = b;
	}

	public int hashCode() {
		return 0;
	}

	public String toString() {
		return "( " + first.toString() + " , " + second.toString() + " ) ";
	}
}