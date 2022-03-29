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
public class DesastresSuccessorFunction2 implements SuccessorFunction {
        public List getSuccessors(Object estat)
        {
            ArrayList llistaSuccessors = new ArrayList();

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
                    i = myRandom.nextInt(H);
                    j = myRandom.nextInt(H);
                    int K = orden.get(i).size();
                    int L = orden.get(j).size();
                    k = myRandom.nextInt(K);
                    l = myRandom.nextInt(L);

                } while (i == j && k == l);
                if ((i != j) || (k != l)) {
                    estado newestat = new estado(estado_act);
                    newestat.swap_grupos(i, k, j, l);
                    //System.out.println("i: " + i + " j: " + j + " k: " + k + " l: " + l);

                    llistaSuccessors.add(
                            new Successor("Intercambiados los grupos en [" + i + "][" + k + "]" + " y [" + j + "][" + l + "]", newestat));
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

                    llistaSuccessors.add(
                            new Successor("Reasignado (reducido) el grupo en el final de " + i + "a" +j, newestat));
                }
            }
            else if (choose_op == 2) {
                do {
                    i = myRandom.nextInt(H);
                    j = myRandom.nextInt(H);
                    int K = orden.get(i).size();
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
                    llistaSuccessors.add(
                            new Successor("Reasignado (general) el grupo en [" + i + "][" + k + "]" + " a [" + j + "][" + l + "]", newestat));
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

