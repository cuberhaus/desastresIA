package Desastres;


import java.util.Vector;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import aima.search.framework.HeuristicFunction;

//import static Desastres.board.estado_actual;

/**
     * SWAP
     * @author  Sara
     */
public class DesastresSuccessorFunction1 implements SuccessorFunction {
        public List getSuccessors(Object estat)
        {
            ArrayList llistaSuccessors = new ArrayList();
            DesastresHeuristicFunction1 heuristicfunc = new DesastresHeuristicFunction1();

            estado estado_act=(estado) estat;
            ArrayList<LinkedList <Integer> > orden = estado_act.getvec();
            int H = orden.size();
            int i=0;
            int j, k, l;


            for (i=0; i<H; ++i) { //OPERADOR SWAP
                for (j=i; j<H; ++j){
                    for(k=0; k<orden.get(i).size(); ++k){
                        for (l = 0; l < orden.get(j).size(); ++l) {
                            if ((i != j) || (k != l)) {
                                estado newestat = new estado(estado_act);
                                newestat.swap_grupos(i, k, j, l);

                                double V = heuristicfunc.getHeuristicValue(newestat);
                                String S = "Intercambiados los grupos en [" + i + "][" + k + "]" + " y [" + j + "][" + l + "] " + "Coste(" + V +") ---> " + newestat.toString();
                                llistaSuccessors.add(new Successor(S, newestat));
                            }
                        }
                    }
                }
            }
            return llistaSuccessors;
        }
}

