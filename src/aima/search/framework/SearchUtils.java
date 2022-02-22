package aima.search.framework;

import java.util.ArrayList;
import java.util.List;

public class SearchUtils {

	public static List actionsFromNodes(List nodeList) {
		List stateList = new ArrayList();
		for (int i = 1; i < nodeList.size(); i++) { //ignore root node this has
													// no action hence 1 not
													// zero
			Node node = (Node) nodeList.get(i);
			stateList.add(node.getAction());
		}
		return stateList;
	}
        
	public static List statesFromNodes(List nodeList) {
		List stateList = new ArrayList();
		for (int i = 1; i < nodeList.size(); i++) { //ignore root node this has
													// no action hence 1 not
													// zero
			Node node = (Node) nodeList.get(i);
			stateList.add(node.getState());
		}
		return stateList;
	}

	public static List stringToList(String str) {

		List list = new ArrayList();
		list.add(str);
		return list;

	}

	public static boolean listMatches(List list, String string) {
		return ((list.size() == 1) && (((String) list.get(0)).equals(string)));
	}

}