package aima.basic;

import java.util.ArrayList;
import java.util.List;

public abstract class Environment {

	protected ArrayList objects;

	protected ArrayList agents;

	protected ArrayList views;

	public abstract void executeAction(Agent a, String act);

	public abstract Percept getPerceptSeenBy(Agent anAgent);

	protected Environment() {
		agents = new ArrayList();
		objects = new ArrayList();
		views = new ArrayList();
	}

	public void registerView(BasicEnvironmentView bev) {
		views.add(bev);
	}

	public void updateViews(String command) {

		for (int i = 0; i < views.size(); i++) {
			BasicEnvironmentView v = (BasicEnvironmentView) views.get(i);

			v.envChanged(command);
		}
	}

	public boolean isDone() {

		boolean retval = true;

		for (int i = 0; i < agents.size(); i++) {
			Agent a = (Agent) agents.get(i);
			if (a.isAlive()) {
				retval = false;
				break;
			}
		}

		return retval;
	}

	public void createExogenousChange() {

	}

	public void step() {
		if (!(this.isDone())) {

			for (int i = 0; i < agents.size(); i++) {
				Agent a = (Agent) agents.get(i);

				String anAction = a.execute(this.getPerceptSeenBy(a));

				updateViews(anAction);
				this.executeAction(a, anAction);
			}
			this.createExogenousChange();
		}
	}

	public void step(int n) {

		for (int i = 0; i < n; i++) {

			step();

		}
	}

	public void stepUntilNoOp() {
		while (!(this.isDone())) {
			step();
		}
	}

	public ArrayList getAgents() {
		return agents;
	}

	public ArrayList getObjects() {
		return objects;
	}

	public boolean alreadyContains(EnvironmentObject o) {

		boolean retval = false;

		for (int oi = 0; oi < objects.size(); oi++) {
			EnvironmentObject eo = (EnvironmentObject) objects.get(oi);
			if (eo.equals(o)) {
				retval = true;
			}
		}

		return retval;
	}

	public boolean alreadyContains(Agent anAgent) {
		boolean retval = false;
		for (int i = 0; i < agents.size(); i++) {
			Agent agent = (Agent) agents.get(i);
			if (agent.equals(anAgent)) {
				retval = true;
			}
		}
		return retval;
	}

	public void addAgent(Agent a, String attributeName, Object attributeValue) {
		if (!(alreadyContains(a))) {
			a.setAttribute(attributeName, attributeValue);
			agents.add(a);
		}
	}

	public void addObject(EnvironmentObject o, String attributeName,
			Object attributeValue) {
		if (!(alreadyContains(o))) {
			o.setAttribute(attributeName, attributeValue);
			objects.add(o);
		}
	}

	public void addObject(EnvironmentObject o) {
		if (!(alreadyContains(o))) {
			objects.add(o);
		}
	}

	public void addAgent(Agent a) {
		if (!(alreadyContains(a))) {
			agents.add(a);
		}
	}

	public List getAllObjects() {
		List l = new ArrayList();
		l.addAll(objects);
		l.addAll(agents);
		return l;
	}

}