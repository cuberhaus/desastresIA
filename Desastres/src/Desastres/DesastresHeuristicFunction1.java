package Desastres;

import IA.Desastres.*;
import aima.search.framework.HeuristicFunction;

import java.util.ArrayList;
import java.util.LinkedList;


public class DesastresHeuristicFunction1 implements HeuristicFunction{

    //minimizar máximo de las distancias de todos los helicópteros
    //1 helicóptero puede como mucho llevar 15 personas, 10 mins cd entre viajes
    public double getHeuristicValue(Object estado) {
        double heuristic = 0;
        board area = (board)estado;
        ArrayList<LinkedList<Integer>> estadoact = area.getestado();
        double tmax = -1;
        for(int i = 0; i < estadoact.size(); ++i){
            //Capacitat actual per l'helicópter actual en el viatje que "esta realitzant"
            int capacitatact = 0;
            double tiempoact = 0;
            int centroact = area.getcentro(i);
            int lastgroup = -1;
            for(int j = 0; j < estadoact.get(i).size(); ++j){
                Grupo g = area.getgrupo(estadoact.get(i).get(j));
                if(capacitatact + g.getNPersonas() <= 15) {
                    //Aún cabe gente en el helicóptero para este viaje
                    capacitatact += g.getNPersonas();
                    
                    //sales del centro
                    if(lastgroup == -1){
                        tiempoact += (area.get_distancia(centroact, estadoact.get(i).get(j), 0))/1.66667;
                        
                        int timeperpeople = 1;
                        if(g.getPrioridad() == 1) timeperpeople = 2;
                        
                        tiempoact += (g.getNPersonas() *timeperpeople);
                        lastgroup = estadoact.get(i).get(j);
                    
                    } else{
                        //sales de un grupo
                        tiempoact += (area.get_distancia(lastgroup, estadoact.get(i).get(j), 1))/1.66667;
                        
                        int timeperpeople = 1;
                        if(g.getPrioridad() == 1) timeperpeople = 2;
                        
                        tiempoact += (g.getNPersonas() *timeperpeople);
                        lastgroup = estadoact.get(i).get(j);
                    }
                    
                } else{
                    //viaje "lleno"
                    capacitatact = 0;
                    tiempoact += (area.get_distancia(centroact, lastgroup, 0))/1.66667;
                    //10 min cooldown
                    tiempoact += 10;
                    
                    lastgroup = -1;
                    
                }
            }
            if(tmax == -1) tmax = tiempoact;
            else if(tmax < tiempoact) tmax = tiempoact;
        }
        
        return heuristic;
    }

}
