package Desastres;


import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//import static Desastres.board.estado_actual;

/**
 * GENERAL
 *
 * @author Sara
 */
public class DesastresSuccessorFunction2 implements SuccessorFunction {
    public List getSuccessors(Object estat) {
        ArrayList llistaSuccessors = new ArrayList();
        DesastresHeuristicFunction1 heuristicfunc = new DesastresHeuristicFunction1();

        estado estado_act = (estado) estat;
        ArrayList<LinkedList<Integer>> orden = estado_act.getvec();
        int H = orden.size();
        int i = 0;
        int j, k, l;

        for (i = 0; i < H; i++) { //OPERADOR REASIGNAR GENERAL
            for (j = 0; j < H; ++j) {
                for (k = 0; k < orden.get(i).size(); ++k) {
                    if (orden.get(j).size() == 0) {
                        l = 0;
                        if ((i != j) || (k != l)) {
                            estado newestat = new estado(estado_act);
                            newestat.reasignar_grupo_general(i, k, j, l);

                            double V = heuristicfunc.getHeuristicValue(newestat);
                            String S = "Reasignado (general) el grupo en [" + i + "][" + k + "]" + " a [" + j + "][" + l + "] " + "Coste(" + V + ") ---> " + newestat.toString();
                            llistaSuccessors.add(new Successor(S, newestat));
                        }

                    } else {
                        for (l = 0; l < orden.get(j).size(); ++l) {
                            if (i != j || k != l) {
                                estado newestat = new estado(estado_act);
                                newestat.reasignar_grupo_general(i, k, j, l);

                                double V = heuristicfunc.getHeuristicValue(newestat);
                                String S = "Reasignado (general) el grupo en [" + i + "][" + k + "]" + " a [" + j + "][" + l + "] " + "Coste(" + V + ") ---> " + newestat.toString();
                                llistaSuccessors.add(
                                        new Successor(S, newestat));
                            }
                        }
                    }
                }
            }
        }
        return llistaSuccessors;
    }
}

