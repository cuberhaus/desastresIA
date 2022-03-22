package Desastres;

import java.util.AbstractMap;

public class PairDH extends AbstractMap.SimpleEntry<Double,Helicopter> implements Comparable<PairDH> {
    public PairDH(Double key, Helicopter value) {
        super(key, value);
    }

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
