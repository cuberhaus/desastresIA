import IA.Desastres.*;
import IA.Desastres.Centros;
import IA.Desastres.Grupos;

import java.util.*;

/**
 *
 * @author  patata
 */
 
public class board {

    public static Grupos grupos;
    public static Centros centros;
    public static ArrayList<Integer> helicopter; //id del centro al que pertenecen

    public static ArrayList<ArrayList<Integer>> estado;

    /**
    * Matriz donde precalcularemos las distancias de los centros a los grupos
    */
    public static ArrayList<ArrayList<Double>> distancia_centro_grupos;
    
    /**
    * Matriz donde precalcularemos las distancias de los grupos a los grupos
    */
    public static ArrayList<ArrayList<Double>> distancia_grupos_grupos;


    // 1. Creadora
    /**
     * Constructora por defecto
     */
    public board(){
    }
    
    public board(Grupos g, Centros c){
        grupos = g;
        centros = c;    
    }

    // 2. Gen estado inicial
    public void gen_estado_inicial(){

    }

    // 3. Operadores
    public void swap_grupos(){

    }

    public void reasignar_grupo(){

    }

    // 3. Calc distancia de p (x1,y1) a q (x2,y2)
    public double calc_distancia(int x1, int y1, int x2, int y2){
       return Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2));
    }

    // 4. getters distancias
    public double get_distancia(int id1, int id2, int select){
        if(select == 0) return distancia_centro_grupos.get(id1).get(id2); //centro a grupos
        else return distancia_grupos_grupos.get(id1).get(id2); //grupos a grupos
    }

    // 5. Precalcular distancias c_g
    public ArrayList<ArrayList<Double>> precalc_dist_c_g(){
        ArrayList<ArrayList<Double>> aux = new ArrayList<ArrayList<Double>>();

        return aux;
    }

    // 6. Precalcular distancias g_g
    public ArrayList<ArrayList<Double>> precalc_dist_g_g(){
        ArrayList<ArrayList<Double>> aux = new ArrayList<ArrayList<Double>>();
        return aux;
    }

    // 7. initialize, quiza no se usa
    public void initialize(){

    }

    //hacer un main
}
