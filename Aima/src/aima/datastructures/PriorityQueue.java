/*
 * Created on Sep 14, 2004
 *
 */
package aima.datastructures;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import aima.util.AbstractQueue;

/**
 * @author Ravi Mohan
 *  
 */
public class PriorityQueue extends AbstractQueue {
	private Comparator comparator;

	public PriorityQueue(Comparator comparator) {
		this.comparator = comparator;
	}

	public void add(Object anItem) {
		super.addToFront(anItem);
		Collections.sort(linkedList, comparator);
	}

	public void add(List items) {

		for (int i = items.size() - 1; i > -1; i--) {
			add(items.get(i));
		}

	}

	public Object remove() {
		return super.removeFirst();
	}

	public Object get() {
		return super.getFirst();
	}

}