import Desastres.*;
import IA.Desastres.Centros;
import IA.Desastres.Grupo;
import IA.Desastres.Grupos;
import aima.search.framework.*;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.*;
import static Desastres.board.*;

public class main {
    public static void main(String[] args) {
        //long startTime = System.nanoTime();

        /**
         =========================================================================
         =                   Selectores de funcionalidades:                      =
         =                   algorithm = 0 -> HC                                 =
         =                               1 -> SA                                 =
         =                                                                       =
         =                   successorfunc = 1 -> SWAP                           =
         =                                   2 -> REASSIGNAR GENERAL             =
         =                                   3 -> REASSIGNAR REDUCIDO            =
         =                                   4 -> SWAP + GENERAL                 =
         =                                   5 -> SWAP + REDUCIDO                =
         =                                   6 -> SA                             =
         =                                                                       =
         =                   gensolini = 0 -> RANDOM                             =
         =                               1-> TODO A UNO                          =
         =                               2-> "GREEDY"                            =
         =                                                                       =
         =                   heuristicfunc = 1 -> SUMATODOS PONDERADA            =
         =                                   2 -> SUMATODOS                      =
         =                                   3 -> SUMATODOS PONDERADA +          =
         =                                        RESCATAR PRIORITARIOS          =
         =                                                                       =
         =========================================================================
         */
        int algorithm = 0;
        int successorfunc = 4;
        int gensolini = 2;
        int heuristicfunc = 3;
        //selector for successor

        aima.search.framework.SuccessorFunction SF = null;
        if(algorithm == 0) SF = new DesastresSuccessorFunction4();
        else SF = new DesastresSuccessorFunction6();

        /**
         =========================================================================
         =                                                                       =
         =               Valores para SA, creacion problema y seeds              =
         =                                                                       =
         =========================================================================

         */
        int steps = 2000;
        int stiter = 100;
        int k = 5;
        double lambda = 0.001;

        int seed = 1000;
        int ncentros = 5;
        int ngrupos = 100;
        int nhelicopters = 1;


        if (args.length == 1) {
            seed = Integer.parseInt(args[0]);
        }
        if (args.length == 2) {
            seed = Integer.parseInt(args[0]);
            ngrupos = Integer.parseInt(args[1]);
        }
        if (args.length == 3) {
            seed = Integer.parseInt(args[0]);
            ngrupos = Integer.parseInt(args[1]);
            ncentros = Integer.parseInt(args[2]);
        }
        if (args.length == 4) {
            seed = Integer.parseInt(args[0]);
            ngrupos = Integer.parseInt(args[1]);
            ncentros = Integer.parseInt(args[2]);
            nhelicopters = Integer.parseInt(args[3]);
        }
        if (args.length == 5) {
            seed = Integer.parseInt(args[0]);
            lambda = Double.parseDouble(args[1]);
            k = Integer.parseInt(args[2]);
            steps = Integer.parseInt(args[3]);
            stiter = Integer.parseInt(args[4]);
        }
        Centros c = new Centros(ncentros, nhelicopters, seed);
        Grupos g = new Grupos(ngrupos, seed);

        board b = new board(g,c);
        estado estado_actual = new estado(g.size(), board.getnhelicopters(), gensolini);
        //estado estado_actual = new estado(g.size(),  board.getnhelicopters(),123456, gensolini);


        aima.search.framework.HeuristicFunction HF = new DesastresHeuristicFunction1();
        if(heuristicfunc == 2) HF = new DesastresHeuristicFunction2();
        else if(heuristicfunc == 3) HF = new DesastresHeuristicFunction3();


        // 0 for HC, anything else for SA
        if(algorithm == 0){
            switch (successorfunc){
                case 1:
                    SF = new DesastresSuccessorFunction1();
                    break;
                case 2:
                    SF = new DesastresSuccessorFunction2();
                    break;
                case 3:
                    SF = new DesastresSuccessorFunction3();
                    break;
                case 4:
                    SF = new DesastresSuccessorFunction4();
                    break;
                case 5:
                    SF = new DesastresSuccessorFunction5();
                    break;
                default:
                    SF = new DesastresSuccessorFunction4();
                    break;
            }
            executeHC(estado_actual,SF, HF);
        } else {
            executeSA(estado_actual,successorfunc, HF, steps, stiter, k, lambda);
        }
        //long elapsedTime = System.nanoTime() - startTime;

        //System.out.println("Texec: "
        //        + elapsedTime/1000000);
    }


    private static void executeSA(estado estado_actual, int successorfunction, HeuristicFunction HF, int steps, int stiter, int k, double lamb) {
        try {
            Problem problem =  new Problem(estado_actual,new DesastresSuccessorFunction6(), new DesastresGoalTest(),HF);
            Search search =  new SimulatedAnnealingSearch(steps,stiter,k,lamb);

            //long startTime = System.nanoTime();
            SearchAgent agent = new SearchAgent(problem,search);
            //long elapsedTime = System.nanoTime() - startTime;

           // System.out.println("Texec: "
           //          + elapsedTime/1000000);

//            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
//            printFinalState(search);

            //System.out.println("nodesExpanded: " + agent.getActions().size());
            //System.out.println("Heuristico final: " + hfinal);
            //ESTO REALMENTE ES SUMA DE LOS TIEMPOS!!!!!
            System.out.println("Heuristico final: " + gettime((estado)search.getGoalState()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executeHC(estado estado_actual, SuccessorFunction SF, HeuristicFunction HF) {
        try {
            Problem problem =  new Problem(estado_actual,SF, new DesastresGoalTest(),HF);
            double hini = problem.getHeuristicFunction().getHeuristicValue(estado_actual);

            Search search =  new HillClimbingSearch();


            long startTime = System.nanoTime();
            SearchAgent agent = new SearchAgent(problem,search);
            long elapsedTime = System.nanoTime() - startTime;

            System.out.println("Texec: "
                    + elapsedTime/1000000);

//            printActions(agent.getActions());
//            printInstrumentation(agent.getInstrumentation());
//            printFinalState(search);


            //System.out.println("Heuristico inicial: " + hini);
//            System.out.println("nodesExpanded: " + agent.getActions().size());
            //System.out.println("Heuristico final: " + hfinal);
            //ESTO REALMENTE ES SUMA DE LOS TIEMPOS!!!!!
            System.out.println("Heuristico final: " + gettime((estado)search.getGoalState()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                            //System.out.println(board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP));
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

                        if(priorittravel) {
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
                        //System.out.println(board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP));
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
                        if (g.getPrioridad() == 1){
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

                    if(priorittravel) {
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

                    if(priorittravel) {
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

                    if(priorittravel) {
                        prioritytime = ttotal;
                        priorittravel = false;
                    }
                }
            }
            //tiempo del helicoptero que mas tarda
            if (tmax == -1) tmax = tiempoact;
            else if (tmax < tiempoact) tmax = tiempoact;

            //suma de cuadrado de tiempos
            //tmax += (Math.pow(tiempoact,2));

            //tmax += tiempoact;

            //opcion Sara
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

        for (int i = 0; i < tiemposheli.size(); ++i) {
            if (tiemposheli.get(i) != 0) aux += tiemposheli.get(i) * (ngrupos.get(i) / (float) board.grupos.size());
            else nhelisingrupo++;
            //if(tiemposheli.get(i) != 0) aux += (tiemposheli.get(i)*(Math.log10((tiemposheli.get(i)/tmax))));
        }

        double ponderacion = (1 - ((ttotal / numhelicopters) / tmax));


        //heuristic = (ttotal + aux * ponderacion) + prioritytime;
        //System.out.println("tmax: " + tmax);
        //System.out.println("aux: "+ aux);

        System.out.println("Priority time: " + prioritytime);
        heuristic = ttotal;
        //System.out.println(heuristic);
        return heuristic;
    }

}


// CODIGO DE DEBUGGING POR SI LO ECHAMOS EN FALTA

        /*
        try {
            //Problem problem =  new Problem(estado_actual,new DesastresSuccessorFunction1(), new DesastresGoalTest(),new DesastresHeuristicFunction1());
            Problem problem =  new Problem(estado_actual,new DesastresSuccessorFunction2(), new DesastresGoalTest(),new DesastresHeuristicFunction1());
            double hini = problem.getHeuristicFunction().getHeuristicValue(estado_actual);
            //double timefinal = gettime(estado_actual);
            //System.out.println("Heuristico ini: " + gettime(estado_actual));
            //System.out.println("Suma tiempos: " + timefinal);
            //Search search =  new HillClimbingSearch();
            Search search =  new SimulatedAnnealingSearch(2000,100,5,0.001);

            //long startTime = System.nanoTime();
            SearchAgent agent = new SearchAgent(problem,search);
            //long elapsedTime = System.nanoTime() - startTime;

            //System.out.println("Texec: "
            //        + elapsedTime/1000000);

//            System.out.println();
//            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
            printFinalState(search);

            //double hfinal = problem.getHeuristicFunction().getHeuristicValue((estado)search.getGoalState());
            //double timefinal = gettime((estado)search.getGoalState());
            //System.out.println("Suma tiempos: " + timefinal);
            long elapsedTime = System.nanoTime() - startTime;

            System.out.println("Texec: "
                    + elapsedTime/1000000);
            //System.out.println("Heuristico inicial: " + hini);
            //System.out.println("nodesExpanded: " + agent.getActions().size());
            //System.out.println("Heuristico final: " + hfinal);
            //ESTO REALMENTE ES SUMA DE LOS TIEMPOS!!!!!

            System.out.println("Heuristico final: " + gettime((estado)search.getGoalState()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

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