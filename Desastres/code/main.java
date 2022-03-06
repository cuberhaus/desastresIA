import IA.Desastres.*;
import IA.Desastres.Centros;
import IA.Desastres.Grupos;

import java.util.*;

public class main {
    public static void main(String args[]) {
        Centros c = new Centros(5, 1, 123456);
        Grupos g = new Grupos(100, 123456);
        board b = new board(g,c);

        double distancia = b.calc_distancia(2,4,-2,4);
        System.out.println(distancia);
    }
}
