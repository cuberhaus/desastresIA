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
    public static ArrayList<Integer> helicopter; //mirar
    
    /**
    * Matriz donde precalcularemos las distancias de los centros a los grupos
    */
    public static ArrayList<ArrayList<Double>> distancia_centro_grupos;
    
    /**
    * Matriz donde precalcularemos las distancias de los grupos a los grupos
    */
    public static ArrayList<ArrayList<Double>> distancia_grupos_grupos;

    /**
     * Constructora por defecto
     */
    public board(){
    }
    
    public board(Grupos g, Centros c){
        grupos = g;
        centros = c;    
    }
    
    public void gen_estado_inicial(){
    
    
    }
}
