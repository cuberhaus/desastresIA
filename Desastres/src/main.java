import Desastres.*;
import IA.Desastres.Centros;
import IA.Desastres.Grupo;
import IA.Desastres.Grupos;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.*;
import static Desastres.board.*;

public class main {
    public static void main(String[] args) {
        long startTime = System.nanoTime();

        // default values
        double lambda = 0;
        int k = 0;
        int seed = 1234;
        if (args.length == 1) {
            seed = Integer.parseInt(args[0]);
        }
        if (args.length == 3) {
            lambda = Double.parseDouble(args[1]);
            k = Integer.parseInt(args[2]);
        }
        Centros c = new Centros(5, 1, seed);
        Grupos g = new Grupos(100, seed);

        board b = new board(g,c);
        estado estado_actual = new estado(g.size(),  board.getnhelicopters(),seed);

        //<LinkedList<Integer>> asignacion = estado_actual.getvec();
        //int n = asignacion.size();
        //for (int i = 0; i < n; ++i) {
           // int m = asignacion.get(i).size();
           // for (int j = 0; j < m; ++j) {
                //System.out.println(i + " : " + asignacion.get(i).get(j) + " "); // debug

                //System.out.println("Grupo: " + g.get(asignacion.get(i).get(j)).getCoordX() + " : " + g.get(asignacion.get(i).get(j)).getCoordY() + " Personas: " + g.get(asignacion.get(i).get(j)).getNPersonas()+ " Prioridad: " + g.get(asignacion.get(i).get(j)).getPrioridad()); // debug
            //}
            //System.out.println("Centro: " + c.get(i).getCoordX() + " : " + c.get(i).getCoordY()); // debug
      //  }
        //final DecimalFormat df = new DecimalFormat("0.00");

     //   double distancia = b.calc_distancia(2,4,-2,4);
//        System.out.println(distancia);


//        b.precalc_dist_c_g();
//        b.precalc_dist_g_g();

        /*
        ArrayList<LinkedList<Integer>> estadoact = estado_actual.getvec();
        for(int i = 0; i < estadoact.size();++i){
            for(int j = 0; j < estadoact.get(i).size(); ++j){
                System.out.println("posh: " + i + " posg: " + j + " " + estadoact.get(i).get(j));
            }
        }


        System.out.println();
        //b.getestado2().swap_grupos(0,0, 0 ,2);
        estado_actual.reasignar_grupo_general(0,0,0,1);
        //b.getestado2().reasignar_grupo_reducido(0,1);
        ArrayList<LinkedList<Integer>> estadoact2 = estado_actual.getvec();
        for(int i = 0; i < estadoact.size();++i){
            for(int j = 0; j < estadoact.get(i).size(); ++j){
                System.out.println("posh: " + i + " posg: " + j + " " + estadoact.get(i).get(j));
            }
        }
        */
        try {
            Problem problem =  new Problem(estado_actual,new DesastresSuccessorFunction1(), new DesastresGoalTest(),new DesastresHeuristicFunction1());
            double hini = problem.getHeuristicFunction().getHeuristicValue(estado_actual);
            //double timefinal = gettime(estado_actual);
            //System.out.println("Suma tiempos: " + timefinal);
            Search search =  new HillClimbingSearch();
//            Search search =  new SimulatedAnnealingSearch();

            //long startTime = System.nanoTime();
            SearchAgent agent = new SearchAgent(problem,search);
            //long elapsedTime = System.nanoTime() - startTime;

            //System.out.println("Texec: "
            //        + elapsedTime/1000000);

//            System.out.println();
//            printActions(agent.getActions());
//            printInstrumentation(agent.getInstrumentation());
//            printFinalState(search);

            double hfinal = problem.getHeuristicFunction().getHeuristicValue((estado)search.getGoalState());
            //double timefinal = gettime((estado)search.getGoalState());
            //System.out.println("Suma tiempos: " + timefinal);
            long elapsedTime = System.nanoTime() - startTime;

            System.out.println("Texec: "
                    + elapsedTime/1000000);
            //System.out.println("Heuristico inicial: " + hini);
            System.out.println("nodesExpanded: " + agent.getActions().size());
            //System.out.println("Heuristico final: " + hfinal);
            //ESTO REALMENTE ES SUMA DE LOS TIEMPOS!!!!!
            System.out.println("Heuristico final: " + gettime((estado)search.getGoalState()));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for(int i = 0; i < c.size(); ++i){
//            for(int j = 0; j < g.size(); ++j){
//                System.out.println(b.get_distancia(i,j,0));
//            }
//        }

//        for(int i = 0; i < g.size(); ++i){
//            for(int j = 0; j < g.size(); ++j) {
//                System.out.println(b.get_distancia(i, j, 1));
//            }
//        }
    }

    private static void printFinalState(Search search) {
        estado est_final = (estado)search.getGoalState();
        for(int i = 0; i < est_final.getvec().size(); ++i){
            System.out.println("Helicoptero: " + i);
            for(int j = 0; j < est_final.getvec().get(i).size() ;++j){
                System.out.print(est_final.getvec().get(i).get(j) + " ");
            }
            System.out.println();
        }



    }

    private static void printActions(List actions) {
        System.out.println("Hemos tomado " + actions.size() + " decisiones");
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }

    private static double gettime(Object estat) {
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

        //double tmax = -1;
        double tmax = 0;
        for(int i = 0; i < estadoact.size(); ++i){
            //Capacitat actual per l'helicópter actual en el viatje que "esta realitzant"
            int capacitatact = 0;
            double tiempoact = 0;
            int centroact = board.getcentro(i);
            int lastgroup = -1;
            int ngrups = 0;
            //System.out.println("Helicóptero: " + i);
            //System.out.print("Viaje: ");
            for(int j = 0; j < estadoact.get(i).size(); ++j){
                Grupo g = board.getgrupo(estadoact.get(i).get(j));
                if(capacitatact + g.getNPersonas() <= 15 && ngrups < 3) {
                    //Aún cabe gente en el helicóptero para este viaje
                    //System.out.print(estadoact.get(i).get(j) + " capacidad: " + board.grupos.get(estadoact.get(i).get(j)).getNPersonas() + " , ");
                    capacitatact += g.getNPersonas();
                    //System.out.println(capacitatact);
                    ++ngrups;
                    //System.out.println(ngrups + "\n");
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

                    tiempoact += (board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP))/1.66667;
                    int timeperpeople = 1;
                    if(g.getPrioridad() == 1) timeperpeople = 2;

                    tiempoact += (g.getNPersonas() *timeperpeople);
                    lastgroup = estadoact.get(i).get(j);
                    ngrups = 1;
                    //System.out.println();
                    //System.out.println("Viaje: ");
                    //System.out.print(estadoact.get(i).get(j) + " capacidad: " + board.grupos.get(estadoact.get(i).get(j)).getNPersonas() + " , ");

                }
            }
            //if(tmax == -1) tmax = tiempoact;
            //else if(tmax < tiempoact) tmax = tiempoact;
            tmax += tiempoact;
//            System.out.println(tiempoact);
        }
        heuristic = tmax;
        //System.out.println(heuristic);
        return heuristic;
    }

}
