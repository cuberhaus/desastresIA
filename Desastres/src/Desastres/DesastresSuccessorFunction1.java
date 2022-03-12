package Desastres;


import java.util.Vector;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import aima.search.framework.HeuristicFunction;

import static Desastres.board.estado_actual;

/**
     *
     * @author  Sara
     */
public class DesastresSuccessorFunction1 implements SuccessorFunction {
        public List getSuccessors(Object estat)
        {
            ArrayList llistaSuccessors = new ArrayList();

            estado estado_act=(estado) estado_actual;
            ArrayList<LinkedList <Integer> > orden = estado_act.getvec();
            int H = orden.size();
            int i=0;
            int j, k, l;

            if (i==i)  {
                for (i=0; i<H; i++) { //OPERADOR SWAP
                    for (j=i; j<H; ++j){
                        for(k=0; k<orden.get(i).size(); ++k){
                            for (l=0; l<orden.get(j).size(); ++l){
                                if (i!=j || k!=l){
                                    estado newestat=new estado(estado_act);
                                    newestat.swap_grupos(i, k, j, l);
                                    llistaSuccessors.add(
                                            new Successor("Intercambiados los grupos en [" + i + "][" +k+"]"+" y [" + i + "][" +k+"]", newestat));
                                }
                            }
                        }
                    }
                }
            }
            if (i==i)  {
                for (i=0; i<H; i++) { //OPERADOR REASIGNAR GENERAL
                    for (j=0; j<H; ++j){
                        for(k=0; k<orden.get(i).size(); ++k){
                            for (l=0; l<orden.get(j).size(); ++l){
                                if (i!=j || k!=l){
                                    estado newestat=new estado(estado_act);
                                    newestat.reasignar_grupo_general(i, k, j, l);
                                    llistaSuccessors.add(
                                            new Successor("Reasignado el grupo en [" + i + "][" +k+"]"+" a [" + i + "][" +k+"]", newestat));
                                }
                            }
                        }
                    }
                }
            }
            if (i==i)  {
                for (i=0; i<H; i++) { //OPERADOR REASIGNAR GENERAL
                    for (j=0; j<H; ++j){
                        if (i!=j){
                            estado newestat=new estado(estado_act);
                            newestat.reasignar_grupo_reducido(i, j);
                            llistaSuccessors.add(
                                    new Successor("Reasignado el grupo en el final de " + i + "a" +j, newestat));
                        }
                    }
                }
            }
                return llistaSuccessors;
}
}

