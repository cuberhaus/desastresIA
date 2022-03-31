package Desastres;

import IA.Desastres.Grupo;
import aima.search.framework.HeuristicFunction;

import java.util.ArrayList;
import java.util.LinkedList;

import static Desastres.board.numhelicopters;

public class DesastresHeuristicFunction2 implements HeuristicFunction {

    //minimizar máximo de los tiempos de todos los helicópteros
    //1 helicóptero puede como mucho llevar 15 personas, 10 mins cd entre viajes
    public double getHeuristicValue(Object estat) {
        double heuristic = 0;
        ArrayList<LinkedList<Integer>> estadoact = ((estado)estat).getvec();

        //suma todos
        double ttotal = 0;
        for(int i = 0; i < estadoact.size(); ++i){
            //Capacitat actual per l'helicópter actual en el viatje que "esta realitzant"
            int capacitatact = 0;
            double tiempoact = 0;
            int centroact = board.getcentro(board.helicopter.get(i));
            int lastgroup = -1;
            int ngrups = 0;
            for(int j = 0; j < estadoact.get(i).size(); ++j){
                Grupo g = board.getgrupo(estadoact.get(i).get(j));
                if(j != estadoact.get(i).size()-1) {
                    if (capacitatact + g.getNPersonas() <= 15 && ngrups < 3) {
                        //Aún cabe gente en el helicóptero para este viaje
                        capacitatact += g.getNPersonas();
                        ++ngrups;
                        //sales del centro
                        if (lastgroup == -1) {
                            tiempoact += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                            //System.out.println(board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP));
                            int timeperpeople = 1;
                            if (g.getPrioridad() == 1) timeperpeople = 2;

                            tiempoact += (g.getNPersonas() * timeperpeople);
                            lastgroup = estadoact.get(i).get(j);

                        } else {
                            //sales de un grupo
                            tiempoact += (board.get_distancia(lastgroup, estadoact.get(i).get(j), board.select_distance.GROUP_TO_GROUP)) / 1.66667;

                            int timeperpeople = 1;
                            if (g.getPrioridad() == 1) timeperpeople = 2;

                            tiempoact += (g.getNPersonas() * timeperpeople);
                            lastgroup = estadoact.get(i).get(j);
                        }

                    } else {
                        //viaje "lleno", ya sea por limite de personas o por numero de grupos
                        capacitatact = 0;
                        tiempoact += (board.get_distancia(centroact, lastgroup, board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                        //10 min cooldown
                        tiempoact += 10;


                        tiempoact += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                        int timeperpeople = 1;
                        if (g.getPrioridad() == 1) timeperpeople = 2;

                        tiempoact += (g.getNPersonas() * timeperpeople);
                        lastgroup = estadoact.get(i).get(j);
                        ngrups = 1;
                    }
                } else if(capacitatact + g.getNPersonas() <= 15 && ngrups < 3){
                    //recoges ultimo grupo y vuelves
                    capacitatact += g.getNPersonas();
                    ++ngrups;
                    //sales del centro
                    if (lastgroup == -1) {
                        tiempoact += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                        //System.out.println(board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP));
                        int timeperpeople = 1;
                        if (g.getPrioridad() == 1) timeperpeople = 2;

                        tiempoact += (g.getNPersonas() * timeperpeople);
                        lastgroup = estadoact.get(i).get(j);

                    } else {
                        //sales de un grupo
                        tiempoact += (board.get_distancia(lastgroup, estadoact.get(i).get(j), board.select_distance.GROUP_TO_GROUP)) / 1.66667;

                        int timeperpeople = 1;
                        if (g.getPrioridad() == 1) timeperpeople = 2;

                        tiempoact += (g.getNPersonas() * timeperpeople);
                        lastgroup = estadoact.get(i).get(j);
                    }
                    capacitatact = 0;
                    tiempoact += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                    lastgroup = estadoact.get(i).get(j);
                    ngrups = 1;

                } else{
                    //dejar grupo, wait, coger grupo, volver
                    capacitatact = 0;
                    tiempoact += (board.get_distancia(centroact, lastgroup, board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                    //10 min cooldown
                    tiempoact += 10;


                    tiempoact += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                    int timeperpeople = 1;
                    if (g.getPrioridad() == 1) timeperpeople = 2;

                    tiempoact += (g.getNPersonas() * timeperpeople);
                    lastgroup = estadoact.get(i).get(j);
                    ngrups = 1;

                    capacitatact = 0;
                    tiempoact += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                }
            }
            ttotal += tiempoact;
        }

        heuristic = ttotal;
        //System.out.println(heuristic);
        return heuristic;
    }

}
