package aima.search.nodestore;

import java.util.Comparator;
import java.util.List;

import aima.datastructures.PriorityQueue;
import aima.search.framework.Node;
import aima.search.framework.NodeStore;

/**
 * @author Ravi Mohan
 *  
 */
public class PriorityNodeStore implements NodeStore {

	PriorityQueue queue;

	public PriorityNodeStore(Comparator comparator) {

		queue = new PriorityQueue(comparator);
	}

	public void add(Node anItem) {

		queue.add(anItem);
	}

	public Node remove() {

		return (Node) queue.remove();
	}

	public void add(List nodes) {

		queue.add(nodes);
	}

	public boolean isEmpty() {

		return queue.isEmpty();
	}

	public int size() {
		return queue.size();
	}

}