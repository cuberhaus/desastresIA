package Desastres;

import IA.Desastres.Centros;
import IA.Desastres.Grupo;
import IA.Desastres.Grupos;

import java.util.ArrayList;

/**
 * @author Todos
 */

public class board {
    /**
     * Estructura de datos donde almacenamos los grupos a rescatar
     */
    public static Grupos grupos;
    /**
     * Matriz donde precalcularemos las distancias de los centros a los grupos
     */
    public static ArrayList<ArrayList<Double>> distancia_centro_grupos;
    /**
     * Matriz donde precalcularemos las distancias de los grupos a los grupos
     */
    public static ArrayList<ArrayList<Double>> distancia_grupos_grupos;
    /**
     * Representa el estado actual del problema
     */
    public static int numhelicopters;
    /**
     * Estructura de datos donde guardamos los centros
     */
    private static Centros centros;
    /**
     * Identificamos la posición en la lista como el identificador del helicóptero
     * y el valor como el identificador del centro al que pertenece
     */
    private static ArrayList<Integer> helicopters;

    /**
     * Constructora dados unos grupos i unos centros
     *
     * @param grupos  grupos a los que habrá que rescatar
     * @param centros centros des de los que saldrán los helicopteros
     */
    public board(Grupos grupos, Centros centros) {
        board.grupos = grupos;
        board.centros = centros;
        int ncentros = board.centros.size();
        int nhelicopters = 0;
        helicopters = new ArrayList<>();
        for (int i = 0; i < ncentros; ++i) {
            int m = board.centros.get(i).getNHelicopteros();
            nhelicopters += m;
            for (int j = 0; j < m; ++j) {
                helicopters.add(i);
            }
        }
        numhelicopters = nhelicopters;
        precalc_dist_c_g();
        precalc_dist_g_g();
    }

    /**
     * Devuelve los centros del board
     *
     * @return centros
     */
    public static Centros getCentros() {
        return centros;
    }

    /**
     * Devuelve los helicopteros del board
     *
     * @return helicopteros
     */
    public static ArrayList<Integer> getHelicopters() {
        return helicopters;
    }


    // 3. Calc distancia de p (x1,y1) a q (x2,y2)

    /**
     * @param id1    Identificador de la primera "estructura", puede ser identificador a un centro o un grupo
     * @param id2    Identificador de la segunda estructura, siempre será un identificador a grupo
     * @param select Parámetro de seleccion de modo, si es 0 retorna la distáncia entre centro y grupo dados, si es != 0 retorna la distáncia entre dos grupos dados
     * @return Retorna la distancia en formato double
     * @author Alejandro
     * <p>
     * Funció que dependiendo del parámetro de seleccion retorna la distancia ya precalculada entre dos centro-grupo (select==0) o grupo-grupo(select!=0)
     */
    public static double get_distancia(int id1, int id2, select_distance select) {
        if (select == select_distance.CENTER_TO_GROUP)
            return distancia_centro_grupos.get(id1).get(id2); //centro a grupos
        else if (select == select_distance.GROUP_TO_GROUP)
            return distancia_grupos_grupos.get(id1).get(id2); //grupos a grupos
        return 0;
    }

    /**
     * Devuelve el grupo en la posición i
     *
     * @param i posición del grupo
     * @return grupo i
     */
    public static Grupo getgrupo(int i) {
        return grupos.get(i);
    }

    /**
     * Devuelve el centro en la posición i
     *
     * @param i posición del centro
     * @return centro i
     */
    public static int getcentro(int i) {
        return helicopters.get(i);
    }

    // 5. Precalcular distancias c_g

    public static int getnhelicopters() {
        return numhelicopters;
    }

    // 6. Precalcular distancias g_g

    /**
     * @param x1 Posición x en el plano del primer punto
     * @param y1 Posición y en el plano del primer punto
     * @param x2 Posición x en el plano del segundo punto
     * @param y2 Posición y en el plano del segundo punto
     * @return Retorna la distancia entre los dos puntos en formato double
     * @author Alejandro
     * <p>
     * Función que calcula la distáncia euclidiana en 2D entre dos puntos p(x1,y1) y q(x2,y2)
     */
    public double calc_distancia(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }

    /**
     * @return Retorna la matriz calculada
     * @author Alejandro
     * <p>
     * Función que precalcula las distáncias entre los centros y los grupos del parámetro implícito y los guarda en distancia_centros_grupos
     */
    public ArrayList<ArrayList<Double>> precalc_dist_c_g() {
        ArrayList<ArrayList<Double>> aux = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < centros.size(); ++i) {
            ArrayList<Double> actual = new ArrayList<>();
            for (int j = 0; j < grupos.size(); ++j) {
                int x1 = centros.get(i).getCoordX();
                int y1 = centros.get(i).getCoordY();
                int x2 = grupos.get(j).getCoordX();
                int y2 = grupos.get(j).getCoordY();
                actual.add(calc_distancia(x1, y1, x2, y2));
            }
            aux.add(actual);
        }
        distancia_centro_grupos = aux;
        return aux;
    }

    /**
     * @return Retorna la matriz calculada
     * @author Alejandro
     * <p>
     * Función muy parecida a precalc_dist_c_g, que precalcula las distáncias entre todos los grupos del parámetro implícito y los guarda en distancia_grupos_grupos
     */
    public ArrayList<ArrayList<Double>> precalc_dist_g_g() {
        ArrayList<ArrayList<Double>> aux = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < grupos.size(); ++i) {
            ArrayList<Double> actual = new ArrayList<>();
            for (int j = 0; j < grupos.size(); ++j) {
                int x1 = grupos.get(i).getCoordX();
                int y1 = grupos.get(i).getCoordY();
                int x2 = grupos.get(j).getCoordX();
                int y2 = grupos.get(j).getCoordY();
                actual.add(calc_distancia(x1, y1, x2, y2));
            }
            aux.add(actual);
        }
        distancia_grupos_grupos = aux;
        return aux;
    }

    // 7. initialize, quiza no se usa
    public void initialize() {

    }

    // 4. getters distancias
    public enum select_distance {CENTER_TO_GROUP, GROUP_TO_GROUP}
}
