package Desastres;


import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//import static Desastres.board.estado_actual;

/**
 * @author Sara
 */
public class DesastresSuccessorFunctionORIGINAL implements SuccessorFunction {
    public List getSuccessors(Object estat) {
        ArrayList llistaSuccessors = new ArrayList();
        DesastresHeuristicFunction1 heuristicfunc = new DesastresHeuristicFunction1();
        //board area = (board)estat;
        //ArrayList<LinkedList<Integer>> orden = area.getestado();

        //estado estado_act = area.getestado2();
        estado estado_act = (estado) estat;
        ArrayList<LinkedList<Integer>> orden = estado_act.getvec();
        int H = orden.size();
        int i = 0;
        int j, k, l;

        if (i == i) {
            for (i = 0; i < H; ++i) { //OPERADOR SWAP
                for (j = i; j < H; ++j) {
                    for (k = 0; k < orden.get(i).size(); ++k) {

                        for (l = 0; l < orden.get(j).size(); ++l) {
                            if ((i != j) || (k != l)) {
                                estado newestat = new estado(estado_act);
                                newestat.swap_grupos(i, k, j, l);
                                //System.out.println("i: " + i + " j: " + j + " k: " + k + " l: " + l);

                                double V = heuristicfunc.getHeuristicValue(newestat);
                                String S = "Intercambiados los grupos en [" + i + "][" + k + "]" + " y [" + j + "][" + l + "] " + "Coste(" + V + ") ---> " + newestat.toString();
                                llistaSuccessors.add(
                                        new Successor(S, newestat));
                            }
                        }

                    }
                }
            }
        }
        if (i == i) {
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
                                llistaSuccessors.add(
                                        new Successor(S, newestat));
                            }

                        } else {
                            for (l = 0; l < orden.get(j).size(); ++l) {
                                if (i != j || k != l) {
                                    estado newestat = new estado(estado_act);
                                    newestat.reasignar_grupo_general(i, k, j, l);

                                    /*
                                    System.out.println("Reasignar general");
                                    for(int i2 = 0; i2 < newestat.getvec().size(); ++i2) {
                                        for(int j2 = 0; j2 < newestat.getvec().get(i2).size(); ++j2){
                                            System.out.print(newestat.getvec().get(i2).get(j2) + "  ");
                                        }
                                        System.out.println();
                                    }
                                    */
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
        }
        if (i != i) {
            for (i = 0; i < H; i++) { //OPERADOR REASIGNAR REDUCIDO
                for (j = 0; j < H; ++j) {
                    if (i != j) {
                        estado newestat = new estado(estado_act);
                        newestat.reasignar_grupo_reducido(i, j);

                            /*
                            System.out.println("Reasignar reducido");
                            for(int i2 = 0; i2 < newestat.getvec().size(); ++i2) {
                                for(int j2 = 0; j2 < newestat.getvec().get(i2).size(); ++j2){
                                    System.out.print(newestat.getvec().get(i2).get(j2) + "  ");
                                }
                                System.out.println();
                            }
                            System.out.println();
                            */
                        double V = heuristicfunc.getHeuristicValue(newestat);
                        String S = "Reasignado (reducido) el grupo en el final de " + i + "a " + j + "Coste(" + V + ") ---> " + newestat.toString();
                        llistaSuccessors.add(
                                new Successor(S, newestat));
                    }
                }
            }
        }

            /*
            for(int p = 0; p < llistaSuccessors.size(); ++p){
                System.out.println(((Successor)llistaSuccessors.get(p)).getAction());
                for(int p2 = 0; p2 < ((estado)((Successor)llistaSuccessors.get(p)).getState()).getvec().size(); ++p2){
                    for(int p3 = 0; p3 < ((estado)((Successor)llistaSuccessors.get(p)).getState()).getvec().get(p2).size(); ++p3){
                        System.out.println(((estado)((Successor)llistaSuccessors.get(p)).getState()).getvec().get(p2).get(p3) + "  ");
                    }
                    System.out.println();
                }
            }
            */
        return llistaSuccessors;
    }
}

