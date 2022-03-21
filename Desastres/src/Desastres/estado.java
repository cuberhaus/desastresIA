package Desastres;

import IA.Desastres.Grupo;

import java.util.ArrayList;
import java.util.LinkedList;
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
        //gen_estado_inicial(ngroups, nhelicopters);
        gen_estado_inicial_malo(ngroups, nhelicopters);
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
    private void gen_estado_inicial(int ngroups, int nhelicopters) {
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
         int n = asignacion.size();

    }

    private void gen_estado_inicial_greedy(int ngroups, int nhelicopters) {
    }

    private void gen_estado_inicial_malo(int ngroups, int nhelicopters){
        asignacion = new ArrayList<>();
        for (int i = 0; i < nhelicopters; ++i) {
            asignacion.add(new LinkedList<>());
        }

        for (int i = 0; i < ngroups; ++i) {
            asignacion.get(0).add(i);
        }

        for(int i = 0; i < asignacion.size(); ++i){
            System.out.println("Helicoptero: " + i);
            for(int j = 0; j < asignacion.get(i).size(); ++j){
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
