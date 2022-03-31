package Desastres;


import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//import static Desastres.board.estado_actual;

/**
 * REDUCIDO
 *
 * @author Sara
 */
public class DesastresSuccessorFunction3 implements SuccessorFunction {
    public List getSuccessors(Object estat) {
        ArrayList llistaSuccessors = new ArrayList();
        DesastresHeuristicFunction1 heuristicfunc = new DesastresHeuristicFunction1();

        estado estado_act = (estado) estat;
        ArrayList<LinkedList<Integer>> orden = estado_act.getvec();
        int H = orden.size();
        int i = 0;
        int j, k, l;

        for (i = 0; i < H; i++) { //OPERADOR REASIGNAR REDUCIDO
            for (j = 0; j < H; ++j) {
                if (i != j) {
                    estado newestat = new estado(estado_act);
                    newestat.reasignar_grupo_reducido(i, j);

                    double V = heuristicfunc.getHeuristicValue(newestat);
                    String S = "Reasignado (reducido) el grupo en el final de " + i + "a " + j + "Coste(" + V + ") ---> " + newestat.toString();
                    llistaSuccessors.add(new Successor(S, newestat));
                }
            }
        }

        return llistaSuccessors;
    }
}

