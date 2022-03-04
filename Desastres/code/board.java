import package IA.Desastres;
import java.util.*;

/**
 *
 * @author  patata
 */
 
public class board {

    public static Grupos grupos;
    public static Centros centros;
    public static ArrayList<Integer> helicopter; //mirar
    
    /**
    * Matriz donde precalcularemos las distancias de los centros a los grupos
    */
    public static ArrayList<ArrayList<Double>> distancia_centro_grupos;
    
     /**
    * Matriz donde precalcularemos las distancias de los grupos a los grupos
    */
    public static ArrayList<ArrayList<Double>> distancia_grupos_grupos;

    
    public board(){
    }
    
    public board(Grupos g, Centros c){
        grupos = g;
        centros = c;    
    }
    
    public gen_estado_inicial(){
    
    
    }
}
