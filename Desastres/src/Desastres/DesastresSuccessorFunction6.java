package Desastres;


import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

//import static Desastres.board.estado_actual;

/**
     *
     * @author  Sara y Pol
     */
public class DesastresSuccessorFunction6 implements SuccessorFunction {
        public List getSuccessors(Object estat)
        {
            ArrayList llistaSuccessors = new ArrayList();
            DesastresHeuristicFunction1 heuristicfunc = new DesastresHeuristicFunction1();
            //board area = (board)estat;
            //ArrayList<LinkedList<Integer>> orden = area.getestado();

            //estado estado_act = area.getestado2();
            estado estado_act=(estado) estat;
            ArrayList<LinkedList <Integer> > orden = estado_act.getvec();
            int H = orden.size();
            int i=0;
            int j, k, l;
            Random myRandom = new Random();
            int choose_op = myRandom.nextInt(3);

            if (choose_op == 0) {
                do {
                    int L;
                    int K = L = 0;
                    j = myRandom.nextInt(H);
                    while (K == 0 || L == 0) {
                        i = myRandom.nextInt(H);
                        j = myRandom.nextInt(H);
                        K = orden.get(i).size();
                        L = orden.get(j).size();
                    }

                    k = myRandom.nextInt(K);
                    l = myRandom.nextInt(L);

                } while (i == j && k == l);
                if ((i != j) || (k != l)) {
                    estado newestat = new estado(estado_act);
                    newestat.swap_grupos(i, k, j, l);
                    //System.out.println("i: " + i + " j: " + j + " k: " + k + " l: " + l);

                    double V = heuristicfunc.getHeuristicValue(newestat);
                    String S = "Intercambiados los grupos en [" + i + "][" + k + "]" + " y [" + j + "][" + l + "]" + "Coste(" + V +") ---> " + newestat.toString();

                    llistaSuccessors.add(
                            new Successor(S, newestat));
                }

            }
            else if (choose_op == 1) {
                do {
                    i = myRandom.nextInt(H);
                    j = myRandom.nextInt(H);

                } while (i == j);
                if (i!=j){
                    estado newestat=new estado(estado_act);
                    newestat.reasignar_grupo_reducido(i, j);

                    double V = heuristicfunc.getHeuristicValue(newestat);
                    String S = "Reasignado (reducido) el grupo en el final de " + i + "a" +j+ "Coste(" + V +") ---> " + newestat.toString();

                    llistaSuccessors.add(
                            new Successor(S, newestat));
                }
            }
            else if (choose_op == 2) {
                do {
                    int K = 0;
                    j = myRandom.nextInt(H);
                    while (K == 0) {
                        i = myRandom.nextInt(H);
                        j = myRandom.nextInt(H);
                        K = orden.get(i).size();
                    }

                    k = myRandom.nextInt(K);
                    int L = orden.get(j).size();
                    if (L == 0) {
                       l = 0 ;
                    } else {
                        l = myRandom.nextInt(L);
                    }

                } while (i == j && k == l);
                if ((i!=j) || (k!=l)){
                    estado newestat = new estado(estado_act);
                    newestat.reasignar_grupo_general(i, k, j, l);
                    double V = heuristicfunc.getHeuristicValue(newestat);
                    String S = "Reasignado (general) el grupo en [" + i + "][" + k + "]" + " a [" + j + "][" + l + "]" + "Coste(" + V +") ---> " + newestat.toString();
                    llistaSuccessors.add(
                            new Successor(S, newestat));
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

