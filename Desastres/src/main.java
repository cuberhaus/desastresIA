import Desastres.board;
import IA.Desastres.*;
import IA.Desastres.Centros;
import IA.Desastres.Grupos;

import java.text.DecimalFormat;
import java.util.*;

public class main {
    public static void main(String args[]) {
        Centros c = new Centros(5, 1, 123456);
        Grupos g = new Grupos(100, 123456);
        board b = new board(g,c);

        //final DecimalFormat df = new DecimalFormat("0.00");

        double distancia = b.calc_distancia(2,4,-2,4);
//        System.out.println(distancia);


        b.precalc_dist_c_g();
        b.precalc_dist_g_g();

        ArrayList<LinkedList<Integer>> estadoact = b.getestado();
        for(int i = 0; i < estadoact.size();++i){
            for(int j = 0; j < estadoact.get(i).size(); ++j){
                System.out.println("posh: " + i + " posg: " + j + " " + estadoact.get(i).get(j));
            }
        }
        System.out.println();
        //b.getestado2().swap_grupos(0,0, 0 ,2);
        b.getestado2().reasignar_grupo_general(0,0,2,0);
        //b.getestado2().reasignar_grupo_reducido(0,1);
        ArrayList<LinkedList<Integer>> estadoact2 = b.getestado();
        for(int i = 0; i < estadoact.size();++i){
            for(int j = 0; j < estadoact.get(i).size(); ++j){
                System.out.println("posh: " + i + " posg: " + j + " " + estadoact.get(i).get(j));
            }
        }

        for(int i = 0; i < c.size(); ++i){
            for(int j = 0; j < g.size(); ++j){
//                System.out.println(b.get_distancia(i,j,0));
            }
        }

        for(int i = 0; i < g.size(); ++i){
            for(int j = 0; j < g.size(); ++j) {
//                System.out.println(b.get_distancia(i, j, 1));
            }
        }
    }
}
