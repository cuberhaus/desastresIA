package Desastres;

import IA.Desastres.Grupo;
import aima.search.framework.HeuristicFunction;

import java.util.ArrayList;
import java.util.LinkedList;

import static Desastres.board.numhelicopters;

/**
 * Función heurística que suma al heurístico1 el tiempo que tardan los grupos prioritarios en ser rescatados
 * @author Alejandro Espinosa
 */

public class DesastresHeuristicFunction3 implements HeuristicFunction {


    //minimizar máximo de los tiempos de todos los helicópteros
    //1 helicóptero puede como mucho llevar 15 personas, 10 mins cd entre viajes
    public double getHeuristicValue(Object estat) {
        double heuristic = 0;
        ArrayList<LinkedList<Integer>> estadoact = ((estado) estat).getvec();

        //el que mas tarda
        double tmax = -1;
        //suma todos
        double ttotal = 0;

        double prioritytime = 0;

        ArrayList<Double> tiemposheli = new ArrayList<>();
        ArrayList<Integer> ngrupos = new ArrayList<>();
        for (int i = 0; i < estadoact.size(); ++i) {
            //Capacitat actual per l'helicópter actual en el viatje que "esta realitzant"
            int capacitatact = 0;
            double tiempoact = 0;
            int centroact = board.getcentro(i);
            int lastgroup = -1;
            int ngrups = 0;
            boolean priorittravel = false;
            for (int j = 0; j < estadoact.get(i).size(); ++j) {
                Grupo g = board.getgrupo(estadoact.get(i).get(j));
                if (j != estadoact.get(i).size() - 1) {
                    if (capacitatact + g.getNPersonas() <= 15 && ngrups < 3) {
                        //Aún cabe gente en el helicóptero para este viaje
                        capacitatact += g.getNPersonas();
                        ++ngrups;
                        //sales del centro
                        if (lastgroup == -1) {
                            tiempoact += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                            ttotal += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                            int timeperpeople = 1;
                            if (g.getPrioridad() == 1) {
                                timeperpeople = 2;
                                priorittravel = true;
                            }

                            tiempoact += (g.getNPersonas() * timeperpeople);
                            ttotal += (g.getNPersonas() * timeperpeople);
                            lastgroup = estadoact.get(i).get(j);

                        } else {
                            //sales de un grupo
                            tiempoact += (board.get_distancia(lastgroup, estadoact.get(i).get(j), board.select_distance.GROUP_TO_GROUP)) / 1.66667;
                            ttotal += (board.get_distancia(lastgroup, estadoact.get(i).get(j), board.select_distance.GROUP_TO_GROUP)) / 1.66667;

                            int timeperpeople = 1;
                            if (g.getPrioridad() == 1) {
                                timeperpeople = 2;
                                priorittravel = true;
                            }

                            tiempoact += (g.getNPersonas() * timeperpeople);
                            ttotal += (g.getNPersonas() * timeperpeople);
                            lastgroup = estadoact.get(i).get(j);
                        }

                    } else {
                        //viaje "lleno", ya sea por limite de personas o por numero de grupos
                        capacitatact = 0;
                        tiempoact += (board.get_distancia(centroact, lastgroup, board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                        ttotal += (board.get_distancia(centroact, lastgroup, board.select_distance.CENTER_TO_GROUP)) / 1.66667;

                        if (priorittravel) {
                            prioritytime = ttotal;
                            priorittravel = false;
                        }
                        //10 min cooldown
                        tiempoact += 10;
                        ttotal += 10;


                        tiempoact += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                        ttotal += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                        int timeperpeople = 1;
                        if (g.getPrioridad() == 1) {
                            timeperpeople = 2;
                            priorittravel = true;
                        }

                        tiempoact += (g.getNPersonas() * timeperpeople);
                        ttotal += (g.getNPersonas() * timeperpeople);
                        lastgroup = estadoact.get(i).get(j);
                        ngrups = 1;
                    }
                } else if (capacitatact + g.getNPersonas() <= 15 && ngrups < 3) {
                    //recoges ultimo grupo y vuelves
                    capacitatact += g.getNPersonas();
                    ++ngrups;
                    //sales del centro
                    if (lastgroup == -1) {
                        tiempoact += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                        ttotal += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                        int timeperpeople = 1;
                        if (g.getPrioridad() == 1) {
                            timeperpeople = 2;
                            priorittravel = true;
                        }

                        tiempoact += (g.getNPersonas() * timeperpeople);
                        ttotal += (g.getNPersonas() * timeperpeople);
                        lastgroup = estadoact.get(i).get(j);

                    } else {
                        //sales de un grupo
                        tiempoact += (board.get_distancia(lastgroup, estadoact.get(i).get(j), board.select_distance.GROUP_TO_GROUP)) / 1.66667;
                        ttotal += (board.get_distancia(lastgroup, estadoact.get(i).get(j), board.select_distance.GROUP_TO_GROUP)) / 1.66667;

                        int timeperpeople = 1;
                        if (g.getPrioridad() == 1) {
                            timeperpeople = 2;
                            priorittravel = true;
                        }

                        tiempoact += (g.getNPersonas() * timeperpeople);
                        ttotal += (g.getNPersonas() * timeperpeople);
                        lastgroup = estadoact.get(i).get(j);
                    }
                    capacitatact = 0;
                    tiempoact += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                    ttotal += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;

                    if (priorittravel) {
                        prioritytime = ttotal;
                        priorittravel = false;
                    }

                    lastgroup = estadoact.get(i).get(j);
                    ngrups = 1;

                } else {
                    //dejar grupo, wait, coger grupo, volver
                    capacitatact = 0;
                    tiempoact += (board.get_distancia(centroact, lastgroup, board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                    ttotal += (board.get_distancia(centroact, lastgroup, board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                    //10 min cooldown

                    if (priorittravel) {
                        prioritytime = ttotal;
                        priorittravel = false;
                    }

                    tiempoact += 10;

                    tiempoact += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                    ttotal += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                    int timeperpeople = 1;
                    if (g.getPrioridad() == 1) {
                        timeperpeople = 2;
                        priorittravel = true;
                    }

                    tiempoact += (g.getNPersonas() * timeperpeople);
                    ttotal += (g.getNPersonas() * timeperpeople);
                    lastgroup = estadoact.get(i).get(j);
                    ngrups = 1;

                    capacitatact = 0;
                    tiempoact += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;
                    ttotal += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP)) / 1.66667;

                    if (priorittravel) {
                        prioritytime = ttotal;
                        priorittravel = false;
                    }
                }
            }
            //tiempo del helicoptero que mas tarda
            if (tmax == -1) tmax = tiempoact;
            else if (tmax < tiempoact) tmax = tiempoact;

            tiemposheli.add(tiempoact);
            ngrupos.add(estadoact.get(i).size());
        }


        double aux = 0.0;

        for (int i = 0; i < tiemposheli.size(); ++i) {
            if (tiemposheli.get(i) != 0) aux += tiemposheli.get(i) * (ngrupos.get(i) / (float) board.grupos.size());
        }

        double ponderacion = (1 - ((ttotal / numhelicopters) / tmax));



        heuristic = (ttotal + aux * ponderacion) + prioritytime*128;

        return heuristic;
    }
}
