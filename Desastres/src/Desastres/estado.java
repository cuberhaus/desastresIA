package Desastres;

import IA.Desastres.Centros;
import IA.Desastres.Grupo;
import aima.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * @author Sara y Pol
 */


public class estado {
    /**
     * Representa una asignación de grupos a helicópteros donde las posiciones del ArrayList son los helicópteros
     * y cada elemento de la LinkedList és un grupo asignado a ese helicóptero
     */
    public ArrayList<LinkedList<Integer>> asignacion;

    /**
     * Constructora dado un número de grupos i un número de helicópteros
     *
     * @param ngroups      número de grupos
     * @param nhelicopters número de helicópteros
     */
    public estado(int ngroups, int nhelicopters) {
        gen_estado_inicial_random(ngroups, nhelicopters);
        //gen_estado_inicial_malo(ngroups, nhelicopters);
    }

    /**
     * Constructora dado un estado
     *
     * @param estat estado
     */
    public estado(estado estat) {
        ArrayList<LinkedList<Integer>> nuevaassig = new ArrayList<>();
        for(int i = 0; i < estat.getvec().size(); ++i){
            LinkedList<Integer> nuevalinked = new LinkedList<>();
            nuevalinked = (LinkedList<Integer>) estat.getvec().get(i).clone();
            nuevaassig.add(nuevalinked);
        }
        asignacion = nuevaassig;
    }

    // 2. Gen estado inicial

    /**
     * Genera una solución inicial asignando grupos aleatorios a helicópteros aleatorios
     *
     * @param ngroups      número de grupos
     * @param nhelicopters número de helicópteros
     */
    private void gen_estado_inicial_random(int ngroups, int nhelicopters) {
        asignacion = new ArrayList<>();
        for (int i = 0; i < nhelicopters; ++i) {
            asignacion.add(new LinkedList<>());
        }
        Random random = new Random(); // creating Random object
        int nremainingGroups = ngroups;
        LinkedList<Integer> remainingGroups = new LinkedList<>();
        for (int i = 0; i < ngroups; ++i) {
            remainingGroups.add(i);
        }
        while (!remainingGroups.isEmpty()) {
            int idhelicopter = abs(random.nextInt() % nhelicopters);
            int idgroup = abs(random.nextInt() % nremainingGroups);
//            System.out.println(idhelicopter + " : " + idgroup); // debug
            int randomGroup = remainingGroups.get(idgroup);
            asignacion.get(idhelicopter).add(randomGroup);
            remainingGroups.remove(idgroup);
            nremainingGroups--;
        }
        // debug
        for(int i = 0; i < asignacion.size(); ++i){
            System.out.println("Helicoptero: " + i);
            for(int j = 0; j < asignacion.get(i).size(); ++j){
                System.out.print(asignacion.get(i).get(j) + " ");
            }
            System.out.println();
        }

    }

    int closest_distance_group(int id1, board.select_distance select_distance){
        return id1;
    }

    enum center_or_group {CENTER, GROUP}

    static class Tuple3 {
        Object first;
        Object second;
        Object third;

        public Object getFirst() {
            return this.first;
        }

        public Object getSecond() {
            return this.second;
        }

        public Object getThird() {
            return this.third;
        }

        public Tuple3(Object a, Object b, Object c) {
            this.first = a;
            this.second = b;
            this.third = b;
        }

        public int hashCode() {
            return 0;
        }

        public String toString() {
            return "( " + this.first.toString() + " , " + this.second.toString() + " , " + this.third.toString() + " ) ";
        }
    }

    private void gen_estado_inicial_greedy(int ngroups, int nhelicopters) {
        // first element is a pair with current groups will be total time, second is a pair where first element is center of the helicopter and second
        // element is number of helicopter within that center, third element is another pair first element
        // is id of center or group and whether the helicopter sits in a center or a group
        PriorityQueue<Pair> priorityQueue = new PriorityQueue<>();
        Centros centros = board.centros;
        for (int i = 0; i < centros.size(); ++i){
            int m = centros.get(i).getNHelicopteros();
            for (int j = 0; j < m; ++j) {
                Helicopter helicopter = new Helicopter(i,j,0,center_or_group.CENTER,i);
                priorityQueue.add(0.0,helicopter);
            }
        }

        int nremainingGroups = ngroups;
        LinkedList<Integer> remainingGroups = new LinkedList<>();
        for (int i = 0; i < ngroups; ++i) {
            remainingGroups.add(i);
        }
        while (!remainingGroups.isEmpty()) {
//            Tuple3 helicopter = priorityQueue.poll();
//            Pair p1 = (Pair) helicopter.getSecond();
//            Pair p2 = (Pair) helicopter.getThird();
//            closest_distance_group(p2.getSecond(),p2.getSecond());

//            asignacion.get(idhelicopter).add(randomGroup);
//            remainingGroups.remove(idgroup);
//            nremainingGroups--;
        }

        double tmax = -1;
        for(int i = 0; i < asignacion.size(); ++i){
            //Capacitat actual per l'helicópter actual en el viatje que "esta realitzant"
            int capacitatact = 0;
            double tiempoact = 0;
            int centroact = board.getcentro(i);
            int lastgroup = -1;
            int ngrups = 0;
            for(int j = 0; j < asignacion.get(i).size(); ++j){
                Grupo g = board.getgrupo(asignacion.get(i).get(j));
                if(capacitatact + g.getNPersonas() <= 15 && ngrups < 3) {
                    //Aún cabe gente en el helicóptero para este viaje
                    capacitatact += g.getNPersonas();
                    ++ngrups;
                    //sales del centro
                    if(lastgroup == -1){
                        tiempoact += (board.get_distancia(centroact, asignacion.get(i).get(j), board.select_distance.CENTER_TO_GROUP))/1.66667;
                        //System.out.println(board.get_distancia(centroact, estadoact.get(i).get(j), board.select_distance.CENTER_TO_GROUP));
                        int timeperpeople = 1;
                        if(g.getPrioridad() == 1) timeperpeople = 2;

                        tiempoact += (g.getNPersonas() *timeperpeople);
                        lastgroup = asignacion.get(i).get(j);

                    } else{
                        //sales de un grupo
                        tiempoact += (board.get_distancia(lastgroup, asignacion.get(i).get(j), board.select_distance.GROUP_TO_GROUP))/1.66667;

                        int timeperpeople = 1;
                        if(g.getPrioridad() == 1) timeperpeople = 2;

                        tiempoact += (g.getNPersonas() *timeperpeople);
                        lastgroup = asignacion.get(i).get(j);
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
            if(tmax == -1) tmax = tiempoact;
            else if(tmax < tiempoact) tmax = tiempoact;
        }
//        heuristic = -tmax;
        //System.out.println(heuristic);
//        return heuristic;
    }
    
   private void gen_estado_inicial_malo(int ngroups, int nhelicopters) {
       asignacion = new ArrayList<>();
       for (int i = 0; i < nhelicopters; ++i) {
           asignacion.add(new LinkedList<>());
       }

       for (int i = 0; i < ngroups; ++i) {
           asignacion.get(0).add(i);
       }

       for (int i = 0; i < asignacion.size(); ++i) {
           System.out.println("Helicoptero: " + i);
           for (int j = 0; j < asignacion.get(i).size(); ++j) {
               System.out.print(asignacion.get(i).get(j) + " ");
           }
           System.out.println();
       }
   }
    //Operadores

    /**
     * Intercambia las posiciones de dos grupos en el estado. Intercambia asignacion[i][j] y asignacion[x][y]
     *
     * @param i Helicóptero al que está asignado el primer grupo
     * @param j Posición del primer grupo en el orden de rescate
     * @param x Helicóptero al que está asignado el segundo grupo
     * @param y Posición del segundo grupo en el orden de rescate
     */
    public void swap_grupos(Integer i, Integer j, Integer x, Integer y) {
        Integer aux = asignacion.get(i).get(j);
        asignacion.get(i).set(j, asignacion.get(x).get(y));
        asignacion.get(x).set(y, aux);
    }

    /**
     * Reasigna el elemento en asignacion[i][j] a asignacion[x][y]
     * Factor de ramificación: G*(G-1)
     *
     * @param i Helicóptero al que está asignado el primer grupo
     * @param j Posición del primer grupo en el orden de rescate
     * @param x Helicóptero al que está asignado el segundo grupo
     * @param y Posición del segundo grupo en el orden de rescate
     */
    public void reasignar_grupo_general(Integer i, Integer j, Integer x, Integer y) {
        asignacion.get(x).add(y, asignacion.get(i).get(j));
        int aux = j;
        asignacion.get(i).remove(aux);
    }

    /**
     * Mueve el último elemento del helicóptero id1 a la última posición del helicóptero id2.
     * Factor de ramificación: H*(H-1)
     *
     * @param id1 identificador de grupo 1
     * @param id2 identificador de grupo 2
     */
    public void reasignar_grupo_reducido(Integer id1, Integer id2) {
        if (asignacion.get(id1).size() > 0) asignacion.get(id2).add(asignacion.get(id1).pollLast());
    }

    /**
     * Devuelve la asignación actual
     *
     * @return la asignación actual
     */
    public ArrayList<LinkedList<Integer>> getvec() {
        return asignacion;
    }

}
