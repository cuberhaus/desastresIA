import Desastres.*;
import IA.Desastres.*;
import IA.Desastres.Centros;
import IA.Desastres.Grupos;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

import java.text.DecimalFormat;
import java.util.*;


public class main {
    public static void main(String args[]) {

        Centros c = new Centros(5, 1, 1004);
        Grupos g = new Grupos(100, 1004);
        board b = new board(g,c);
        estado estado_actual = new estado(g.size(),  board.getnhelicopters());

        ArrayList<LinkedList<Integer>> asignacion = estado_actual.getvec();
        int n = asignacion.size();
        for (int i = 0; i < n; ++i) {
            int m = asignacion.get(i).size();
            for (int j = 0; j < m; ++j) {
                //System.out.println(i + " : " + asignacion.get(i).get(j) + " "); // debug

                //System.out.println("Grupo: " + g.get(asignacion.get(i).get(j)).getCoordX() + " : " + g.get(asignacion.get(i).get(j)).getCoordY() + " Personas: " + g.get(asignacion.get(i).get(j)).getNPersonas()+ " Prioridad: " + g.get(asignacion.get(i).get(j)).getPrioridad()); // debug
            }
            //System.out.println("Centro: " + c.get(i).getCoordX() + " : " + c.get(i).getCoordY()); // debug
        }
        //final DecimalFormat df = new DecimalFormat("0.00");

        double distancia = b.calc_distancia(2,4,-2,4);
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
//            System.out.println("Heuristico inicial: " + hini);
            Search search =  new HillClimbingSearch();

            long startTime = System.nanoTime();
            SearchAgent agent = new SearchAgent(problem,search);
            long elapsedTime = System.nanoTime() - startTime;

            System.out.println("Texec: "
                    + elapsedTime/1000000);

//            System.out.println();
//            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
//            printFinalState(search);

            double hfinal = problem.getHeuristicFunction().getHeuristicValue((estado)search.getGoalState());
            System.out.println("Heuristico final: " + hfinal);

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

}
