package Desastres;

import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.random;
import java.util.Random;
/**
 * @author Sara y Pol
 */


public class estado {
    public static ArrayList<LinkedList<Integer>> asignacion;

    public estado(int ngroups, int nhelicopters) {
        gen_estado_inicial(ngroups, nhelicopters);
    }

    // 2. Gen estado inicial
    private void gen_estado_inicial(int ngroups, int nhelicopters) {
        asignacion = new ArrayList<LinkedList<Integer>>();
        for (int i = 0; i < nhelicopters; ++i) {
            asignacion.add(new LinkedList<>());
        }
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < ngroups; ++i) {
            list.add(i);
        }
        Random random = new Random(); // creating Random object
        int nremainingGroups = ngroups;
        LinkedList <Integer> remainingGroups = new LinkedList<>();
        for (int i = 0; i < ngroups; ++i) {
            remainingGroups.add(i);
        }
        while (! remainingGroups.isEmpty()){
            int idhelicopter = abs(random.nextInt() % nhelicopters);
            int idgroup = abs(random.nextInt() % nremainingGroups);
//            System.out.println(idhelicopter + " : " + idgroup); // debug
            int randomGroup = remainingGroups.get(idgroup);
            asignacion.get(idhelicopter).add(randomGroup);
            remainingGroups.remove(idgroup);
            nremainingGroups--;
        }
        // debug
//        int n = asignacion.size();
//        for (int i = 0; i < n; ++i) {
//            int m = asignacion.get(i).size();
//            for (int j = 0; j < m; ++j) {
//                System.out.println(i + " : " + asignacion.get(i).get(j) + " "); // debug
//            }
//        }
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
     * @param id1
     * @param id2
     */
    public void reasignar_grupo_reducido(Integer id1, Integer id2) {
        if (asignacion.get(id1).size()>0) asignacion.get(id2).add(asignacion.get(id1).pollLast());
    }

    public ArrayList<LinkedList<Integer>> getvec(){
        return this.asignacion;
    }
    
}
