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
     */
    public void swap_grupos(Integer i, Integer j, Integer x, Integer y){
        Integer aux=asignacion[i][j];
        asignacion[i][j]=asignacion[x][y];
        asignacion[x][y]=aux;
    }

    public void reasignar_grupo(){

    }
    
}
