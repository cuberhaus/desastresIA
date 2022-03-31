package Desastres;

import java.util.AbstractMap;

/**
 * Pair consisting of a double and a Helicopter
 * @author Pol Casacuberta
 */
public class PairDH extends AbstractMap.SimpleEntry<Double,Helicopter> implements Comparable<PairDH> {
    /**
     * Default constructor
     * @param key double
     * @param value helicopter
     */
    public PairDH(Double key, Helicopter value) {
        super(key, value);
    }

    /**
     * Pairs are compared by their key
     * @param o object to compare to
     * @return 1 if o is bigger, 0 if equal, -1 if smaller
     */
    @Override
    public int compareTo(PairDH o) {
        if (o.getKey() < this.getKey()) {
           return 1;
        }
        else if (o.getKey() > this.getKey()) {
           return -1;
        }
        return 0;
    }
}
