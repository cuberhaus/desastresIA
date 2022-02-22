/*
 * Created on Sep 14, 2004
 *
 */
package aima.search.informed;

/**
 * @author Ravi Mohan
 *
 */
public class Scheduler {
    
    private int k, limit;
    
    private double lam;
    
    public Scheduler(int k, double lam, int desc) {
        this.k = k;
        this.lam = lam;
        this.limit = desc;
    }
    
    public Scheduler() {
        this.k = 20;
        this.lam = 0.005;
        this.limit = 100;
    }
    
    public double getTemp(int t) {
        double tempD;
        int temp;
        tempD=(t/limit)*limit;
        temp=(int) tempD;
        double res = k * Math.exp((-1) * lam * temp);
        return res;
    }
}

