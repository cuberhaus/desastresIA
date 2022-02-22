/*
 * Created on Sep 14, 2004
 *
 */
package aima.search.informed;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aima.basic.Util;
import aima.search.framework.Node;
import aima.search.framework.NodeExpander;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchUtils;

/**
 * @author Ravi Mohan
 *  
 */
public class SimulatedAnnealingSearch extends NodeExpander implements Search {

    private Object goalState;
    private Node lastNode;
    private int steps;
    private boolean trace = false;
    private Scheduler scheduler;

    public SimulatedAnnealingSearch() {
        this.steps = 10000;
        this.scheduler = new Scheduler();
    }

    public SimulatedAnnealingSearch(int steps, int stiter, int k, double lamb) {
        this.steps = steps;
        this.scheduler = new Scheduler(k, lamb, stiter);
    }

    public void traceOn() {
        trace = true;
    }

    @SuppressWarnings("unchecked")
    public List search(Problem p) throws Exception {
        clearInstrumentation();
        Node current = new Node(p.getInitialState());
        Node next = null;
        Node best = current;
        List ret = new ArrayList();
   //     String status = "";
        Random rnd = new Random();
   //     boolean changed = false;

        List children = expandNode(current, p);

        for (int step = 0; step < this.steps; step += 1) {
            double temp = scheduler.getTemp(step);
            if (temp == 0.0) {
                break;
            }
            //expansions++;
            //System.out.println("step = "+step+" expansions = "+expansions);
            if (children.size() > 0) {
                //TODO take care of no possible expansion situation?
                next = (Node) Util.selectRandomlyFromList(children);
                double deltaE = getValue(next, p) - getValue(current, p);
                //System.out.print("deltaE = "+deltaE+"\n");
                double al = rnd.nextDouble();
                double prob = 1.0 / (1.0 + Math.exp(deltaE / temp));

                if (trace && (deltaE < 0.0) && (al > prob)) {
                    System.out.println("Pr Acep=" + prob + " Delta E=" + deltaE + " Temp= " + temp);
                }

                if ((deltaE > 0.0) || (al > prob)) {
                    current = next;
                    if (getValue(current, p) > getValue(best, p)) {
                        best = current;
                    }
                    // changed=true;
                }


            }
            //if (changed) children = expandNode(current, p);
            //changed=false; 
            children = expandNode(current, p);

        }
        goalState = best.getState();
        lastNode = best;

        //if (p.isGoalState(current.getState())) {
        //   status = "success";
        //} else {
        //   status = "not completed";
        //	}

      //  ret = SearchUtils.actionsFromNodes(best.getPathFromRoot());
        ret.add(goalState);
        //  int coste = -getValue(best, p);
        // ret.add(status + "\nFinal state = " + best.getState()+" Coste= "+coste);


        //System.out.println("Number of expansions = "+expansions);
        return ret;//Total Failure
    }

    public Object getGoalState() {
        return (goalState);
    }

    private double getHeuristic(Node aNode, Problem p) {
        return p.getHeuristicFunction().getHeuristicValue(aNode.getState());
    }

    private double getValue(Node n, Problem p) {
        return -1 * getHeuristic(n, p); //assumption greater heuristic value =>
        // HIGHER on hill; 0 == goal state;
        //SA deals with gardient DESCENT
    }

    public List getPathStates() {
        return SearchUtils.statesFromNodes(lastNode.getPathFromRoot());
    }
}