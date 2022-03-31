package Desastres;

import IA.Desastres.Centros;
import IA.Desastres.Grupos;

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
    private ArrayList<LinkedList<Integer>> asignacion;

    /**
     * Constructora dado un número de grupos i un número de helicópteros
     *
     * @param ngroups      número de grupos
     * @param nhelicopters número de helicópteros
     */
    public estado(int ngroups, int nhelicopters, int gensolini) {
        Random myRandom = new Random();
        if(gensolini == 0) gen_estado_inicial_random(ngroups, nhelicopters, myRandom);
        else if(gensolini == 1) gen_estado_inicial_malo(ngroups, nhelicopters);
        else gen_estado_inicial_greedy(ngroups,nhelicopters);
    }

    public estado(int ngroups, int nhelicopters, int seed, int gensolini) {
        Random myRandom = new Random(seed);
        if(gensolini == 0) gen_estado_inicial_random(ngroups, nhelicopters, myRandom);
        else if(gensolini == 1) gen_estado_inicial_malo(ngroups, nhelicopters);
        else gen_estado_inicial_greedy(ngroups,nhelicopters);
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
     *  @param ngroups      número de grupos
     * @param nhelicopters número de helicópteros
     * @param myRandom random number generator
     */
    private void gen_estado_inicial_random(int ngroups, int nhelicopters, Random myRandom) {
        asignacion = new ArrayList<>();
        for (int i = 0; i < nhelicopters; ++i) {
            asignacion.add(new LinkedList<>());
        }
        int nremainingGroups = ngroups;
        LinkedList<Integer> remainingGroups = new LinkedList<>();
        for (int i = 0; i < ngroups; ++i) {
            remainingGroups.add(i);
        }
        while (!remainingGroups.isEmpty()) {
            int idhelicopter = abs(myRandom.nextInt() % nhelicopters);
            int idgroup = abs(myRandom.nextInt() % nremainingGroups);
//            System.out.println(idhelicopter + " : " + idgroup); // debug
            int randomGroup = remainingGroups.get(idgroup);
            asignacion.get(idhelicopter).add(randomGroup);
            remainingGroups.remove(idgroup);
            nremainingGroups--;
        }
        // debug
        /*
        for(int i = 0; i < asignacion.size(); ++i){
            System.out.println("Helicoptero: " + i);
            for(int j = 0; j < asignacion.get(i).size(); ++j){
                System.out.print(asignacion.get(i).get(j) + " ");
            }
            System.out.println();
        }
        */


    }

    /**
     * Out of the groups in remaining groups returns the closest group to the given group
     * @param id1 identifier of the group where we are
     * @param center_or_group does the identifier id1 belong to a center or to a group
     * @param remainingGroups list of the remaining groups not yet asigned
     * @param npersonas personas que el helicoptero contiene
     * @return identifier of the closest group, return -1 if there isn't a possible closest group
     * @author Pol Casacuberta Gil
     */
     int closest_distance_group(int id1, centerOrGroup center_or_group, LinkedList<Integer> remainingGroups, int npersonas){
        double min_dist = Double.MAX_VALUE;
        int group_min_dist = -1;
        int n = board.grupos.size();
        if (center_or_group == centerOrGroup.CENTER) {
            for (int i = 0; i < n; ++i) {
                if (remainingGroups.contains(i)) {
                    double dist = board.get_distancia(id1, i, board.select_distance.CENTER_TO_GROUP);
                    if (dist < min_dist && npersonas + board.grupos.get(i).getNPersonas() <= 15) {
                        min_dist = dist;
                        group_min_dist = i;
                    }
                }
            }
        }
        else if (center_or_group == centerOrGroup.GROUP) {
            for (int i = 0; i < n; ++i) {
                if (remainingGroups.contains(i)) {
                    double dist = board.get_distancia(id1, i, board.select_distance.GROUP_TO_GROUP);
                    if (dist < min_dist && npersonas + board.grupos.get(i).getNPersonas() <= 15) {
                        min_dist = dist;
                        group_min_dist = i;
                    }
                }
            }
        }
        return group_min_dist;
    }

    enum centerOrGroup {CENTER, GROUP}

    /**
     * Genera un estado incial greedy
     * @param ngroups número de grupos
     * @param nhelicopters número de helicópteros
     * @author Pol Casacuberta Gil
     */
    private void gen_estado_inicial_greedy(int ngroups, int nhelicopters) {
        asignacion = new ArrayList<>();
        for (int i = 0; i < nhelicopters; ++i) {
            asignacion.add(new LinkedList<>());
        }
        PriorityQueue<PairDH> priorityQueue = new PriorityQueue<>();
        Centros centros = board.centros;
        int countHelicopters = 0;
        for (int i = 0; i < centros.size(); ++i){
            int m = centros.get(i).getNHelicopteros();
            for (int j = 0; j < m; ++j) {
                Helicopter helicopter = new Helicopter(i,countHelicopters,0, centerOrGroup.CENTER,i,0);
                priorityQueue.add(new PairDH(0.0,helicopter));
                countHelicopters++;
            }
        }
        LinkedList<Integer> remainingGroups = new LinkedList<>();
        for (int i = 0; i < ngroups; ++i) {
            remainingGroups.add(i);
        }
        while (remainingGroups.size() > 0) {
            PairDH top = priorityQueue.poll();
            assert top != null;
            double totalTimeHelicopter= top.getKey();
            Helicopter helicopter = top.getValue();
            double distance = 0;

            int close_group = closest_distance_group(helicopter.getId_position(),helicopter.getCenter_or_group(), remainingGroups, helicopter.getNpersonas());
            if (close_group == -1 || helicopter.getN_groups() == 3 || helicopter.getNpersonas() >= 15) {
                helicopter.setN_groups(0);
                helicopter.setNpersonas(0);
                distance = board.get_distancia(helicopter.getCenter_id(), helicopter.getId_position(), board.select_distance.CENTER_TO_GROUP); // distancia del helicoptero a su respectivo centro
                helicopter.setId_position(helicopter.getCenter_id());
                helicopter.setCenter_or_group(centerOrGroup.CENTER);
                priorityQueue.add(new PairDH(totalTimeHelicopter+distance,helicopter));
            }
            else {
                double timeToPickUpGroup = 0;
                Grupos g = board.grupos;
                int timeperpeople = 1;
                if(g.get(close_group).getPrioridad() == 1) timeperpeople = 2;
                int group_n_personas = g.get(close_group).getNPersonas();
                timeToPickUpGroup += (group_n_personas * timeperpeople);

                if (helicopter.getCenter_or_group() == centerOrGroup.GROUP) {
                    distance = board.get_distancia(helicopter.getId_position(), close_group, board.select_distance.GROUP_TO_GROUP);
                }
                else if (helicopter.getCenter_or_group() == centerOrGroup.CENTER) {
                    distance = board.get_distancia(helicopter.getId_position(), close_group, board.select_distance.CENTER_TO_GROUP);
                }
                asignacion.get(helicopter.getHelicopter_id()).add(close_group);
                remainingGroups.remove(Integer.valueOf(close_group));
                helicopter.setNpersonas(helicopter.getNpersonas() + group_n_personas);
                helicopter.setN_groups(helicopter.getN_groups() + 1);
                helicopter.setCenter_or_group(centerOrGroup.GROUP);
                priorityQueue.add(new PairDH(totalTimeHelicopter+distance+timeToPickUpGroup,helicopter));
            }
        }
//        for(int i = 0; i < asignacion.size(); ++i){
//            System.out.println("Helicoptero: " + i);
//            for(int j = 0; j < asignacion.get(i).size(); ++j){
//                System.out.print(asignacion.get(i).get(j) + " ");
//            }
//            System.out.println();
//        }

    }
    
   private void gen_estado_inicial_malo(int ngroups, int nhelicopters) {
       asignacion = new ArrayList<>();
       for (int i = 0; i < nhelicopters; ++i) {
           asignacion.add(new LinkedList<>());
       }

       for (int i = 0; i < ngroups; ++i) {
           asignacion.get(0).add(i);
       }

//       for (int i = 0; i < asignacion.size(); ++i) {
//           System.out.println("Helicoptero: " + i);
//           for (int j = 0; j < asignacion.get(i).size(); ++j) {
//               System.out.print(asignacion.get(i).get(j) + " ");
//           }
//           System.out.println();
//       }
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
        Integer valor = asignacion.get(i).get(j);
        asignacion.get(i).remove((int) j);
        asignacion.get(x).add(y, valor);
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
