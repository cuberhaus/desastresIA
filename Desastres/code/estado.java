import java.util.*;
/**
 *
 * @author  Sara
 */


 
public class estado {
    public static ArrayList<ArrayList<Integer>> asignacion;

    public estado(){}

    //Operadores

    /**
     * Intercambia las posiciones de dos grupos en el estado. Intercambia asignacion[i][j] y asignacion[x][y]
     * @param i Helicóptero al que está asignado el primer grupo
     * @param j Posición del primer grupo en el orden de rescate
     * @param x Helicóptero al que está asignado el segundo grupo
     * @param y Posición del segundo grupo en el orden de rescate
     */
    public void swap_grupos(Integer i, Integer j, Integer x, Integer y){
        Integer aux=asignacion[i][j];
        asignacion[i][j]=asignacion[x][y];
        asignacion[x][y]=aux;
    }

    /**
     *
     */
    public void reasignar_grupo(){

    }
    
}
