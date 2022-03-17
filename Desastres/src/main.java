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
        Centros c = new Centros(5, 1, 123456);
        Grupos g = new Grupos(100, 123456);
        board b = new board(g,c);
        estado estado_actual = new estado(g.size(),  board.getnhelicopters());
        //final DecimalFormat df = new DecimalFormat("0.00");

        double distancia = b.calc_distancia(2,4,-2,4);
//        System.out.println(distancia);


        b.precalc_dist_c_g();
        b.precalc_dist_g_g();

        /*
        ArrayList<LinkedList<Integer>> estadoact = b.getestado();
        for(int i = 0; i < estadoact.size();++i){
            for(int j = 0; j < estadoact.get(i).size(); ++j){
                System.out.println("posh: " + i + " posg: " + j + " " + estadoact.get(i).get(j));
            }
        }


        System.out.println();
        //b.getestado2().swap_grupos(0,0, 0 ,2);
        //b.getestado2().reasignar_grupo_general(0,0,2,0);
        //b.getestado2().reasignar_grupo_reducido(0,1);
        ArrayList<LinkedList<Integer>> estadoact2 = b.getestado();
        for(int i = 0; i < estadoact.size();++i){
            for(int j = 0; j < estadoact.get(i).size(); ++j){
                System.out.println("posh: " + i + " posg: " + j + " " + estadoact.get(i).get(j));
            }
        }
        */
        try {
            Problem problem =  new Problem(estado_actual,new DesastresSuccessorFunction1(), new DesastresGoalTest(),new DesastresHeuristicFunction1());
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
            System.out.println("\n"+ search.getGoalState().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 0; i < c.size(); ++i){
            for(int j = 0; j < g.size(); ++j){
//                System.out.println(b.get_distancia(i,j,0));
            }
        }

        for(int i = 0; i < g.size(); ++i){
            for(int j = 0; j < g.size(); ++j) {
//                System.out.println(b.get_distancia(i, j, 1));
            }
        }
    }

    private static void printActions(List actions) {
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
