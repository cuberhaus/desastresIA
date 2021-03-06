package Desastres;

import IA.Desastres.*;
import aima.search.framework.HeuristicFunction;

import java.util.ArrayList;
import java.util.LinkedList;
import static Desastres.board.*;

public class DesastresHeuristicFunction1 implements HeuristicFunction{

    //minimizar máximo de los tiempos de todos los helicópteros
    //1 helicóptero puede como mucho llevar 15 personas, 10 mins cd entre viajes
    public double getHeuristicValue(Object estat) {
        double heuristic = 0;
        ArrayList<LinkedList<Integer>> estadoact = ((estado)estat).getvec();

        //System.out.println("ESTAMOS EN EL HEURÍSTICO");
//        for(int i = 0; i < estadoact.size(); ++i){
//           for(int j = 0; j < estadoact.get(i).size(); ++j) {
//                System.out.print(estadoact.get(i).get(j) + "  ");
//            }
//           System.out.println();
//        }
       //        for(int i = 0; i < estadoact.size(); ++i){
//           for(int j = 0; j < estadoact.get(i).size(); ++j) {
//                System.out.print(estadoact.get(i).get(j) + "  ");
//            }
//           System.out.println();
//        }
// System.out.println("\n\n");

        //el que mas tarda
        double tmax = -1;

        //suma todos
        double ttotal = 0;

        //Opcions sara
        ArrayList<Double> tiemposheli = new ArrayList<>();
        ArrayList<Integer> ngrupos = new ArrayList<>();
        for(int i = 0; i < estadoact.size(); ++i){
            //Capacitat actual per l'helicópter actual en el viatje que "esta realitzant"
            int capacitatact = 0;
            double tiempoact = 0;
            int centroact = board.getcentro(i);
            int lastgroup = -1;
            int ngrups = 0;
            for(int j = 0; j < estadoact.get(i).size(); ++j){
                Grupo g = board.getgrupo(estadoact.get(i).get(j));
                if(capacitatact + g.getNPersonas() <= 15 && ngrups < 3) {
                    //Aún cabe gente en el helicóptero para este viaje
                    capacitatact += g.getNPersonas();
                    ++ngrups;
                    //sales del centro
                    if(lastgroup == -1){
                        tiempoact += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP))/1.66667;
                        //System.out.println(board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP));
                        int timeperpeople = 1;
                        if(g.getPrioridad() == 1) timeperpeople = 2;
                        
                        tiempoact += (g.getNPersonas() *timeperpeople);
                        lastgroup = estadoact.get(i).get(j);
                    
                    } else{
                        //sales de un grupo
                        tiempoact += (board.get_distancia(lastgroup, estadoact.get(i).get(j), board.select_distance.GROUP_TO_GROUP))/1.66667;
                        
                        int timeperpeople = 1;
                        if(g.getPrioridad() == 1) timeperpeople = 2;
                        
                        tiempoact += (g.getNPersonas() *timeperpeople);
                        lastgroup = estadoact.get(i).get(j);
                    }
                    
                } else{
                    //viaje "lleno", ya sea por limite de personas o por numero de grupos
                    capacitatact = 0;
                    tiempoact += (board.get_distancia(centroact, lastgroup, board.select_distance.CENTER_TO_GROUP))/1.66667;
                    //10 min cooldown
                    tiempoact += 10;
                    
                    lastgroup = -1;
                    ngrups = 0;
                    
                }
            }
            //tiempo del helicoptero que mas tarda
            if(tmax == -1) tmax = tiempoact;
            else if(tmax < tiempoact) tmax = tiempoact;

            //suma de cuadrado de tiempos
            //tmax += (Math.pow(tiempoact,2));

            //tmax += tiempoact;

            //opcion Sara
            ttotal += tiempoact;
            tiemposheli.add(tiempoact);
            ngrupos.add(estadoact.get(i).size());
        }


        double aux = 0.0;
        double nhelisingrupo = 0.0;
        /*
        for(int i = 0; i < tiemposheli.size(); ++i){
            if(tiemposheli.get(i) != 0) aux += (((tiemposheli.get(i)/ttotal))*(Math.log10((tiemposheli.get(i)/ttotal))));
            else nhelisingrupo++;
            //if(tiemposheli.get(i) != 0) aux += (tiemposheli.get(i)*(Math.log10((tiemposheli.get(i)/tmax))));
        }
        */

        for(int i = 0; i < tiemposheli.size(); ++i){
            if(tiemposheli.get(i) != 0) aux += tiemposheli.get(i) * (ngrupos.get(i)/(float)board.grupos.size());
            else nhelisingrupo++;
            //if(tiemposheli.get(i) != 0) aux += (tiemposheli.get(i)*(Math.log10((tiemposheli.get(i)/tmax))));
        }

        double ponderacion = (1-((ttotal/numhelicopters)/tmax));


        heuristic = (ttotal + aux*ponderacion);
        //System.out.println("tmax: " + tmax);
        //System.out.println("aux: "+ aux);

        //heuristic = ttotal;
        //System.out.println(heuristic);
        return heuristic;
    }

}
