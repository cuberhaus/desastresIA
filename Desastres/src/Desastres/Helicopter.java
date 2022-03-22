package Desastres;

import java.util.Objects;

/**
 * @author Pol Casacuberta
 */
public class Helicopter implements Comparable<Helicopter>{
    /**
     * Constructora dado un centro, un helicóptero, un número de grupos dentro del helicóptero, el id de la posición
     * donde se encuentra y de si se encuentra en un centro o un grupo
     * @param center_id el helicóptero pertenece a este centro
     * @param helicopter_id id del helicóptero
     * @param n_groups el helicóptero contiene n grupos
     * @param center_or_group el helicóptero està situado en un centro o en un grupo
     * @param id_position identificador del lugar donde se encuentra
     */
    public Helicopter(int center_id, int helicopter_id, int n_groups, estado.centerOrGroup center_or_group, int id_position, int npersonas) {
        this.center_id = center_id;
        this.helicopter_id = helicopter_id;
        this.n_groups = n_groups;
        this.center_or_group = center_or_group;
        this.id_position = id_position;
        this.npersonas = npersonas;
    }
    /**
     * Center id
     */
    int center_id;
    /**
     * Helicopter id
     */
    int helicopter_id;
    /**
     * Amount of groups on it
     */
    int n_groups;
    /**
     * Indica si el helicóptero se encuentra dentro de un centro o de un grupo
     */
    estado.centerOrGroup center_or_group;
    /**
     * Identificador de la posición del helicóptero, ya sea de un centro o de un grupo
     */
    int id_position;

    public int getNpersonas() {
        return npersonas;
    }

    public void setNpersonas(int npersonas) {
        this.npersonas = npersonas;
    }

    /**
     * Número de personas
     */
    int npersonas;

    public int getCenter_id() {
        return center_id;
    }

//    public void setCenter_id(int center_id) {
//        this.center_id = center_id;
//    }

    public int getHelicopter_id() {
        return helicopter_id;
    }

//    public void setHelicopter_id(int helicopter_id) {
//        this.helicopter_id = helicopter_id;
//    }

    public int getN_groups() {
        return n_groups;
    }

    public void setN_groups(int n_groups) {
        this.n_groups = n_groups;
    }

    public estado.centerOrGroup getCenter_or_group() {
        return center_or_group;
    }

    public void setCenter_or_group(estado.centerOrGroup center_or_group) {
        this.center_or_group = center_or_group;
    }

    public int getId_position() {
        return id_position;
    }

    public void setId_position(int id_position) {
        this.id_position = id_position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Helicopter)) return false;
        Helicopter that = (Helicopter) o;
        return center_id == that.center_id && helicopter_id == that.helicopter_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(center_id, helicopter_id);
    }

    @Override
    public int compareTo(Helicopter o) {
        if (o.helicopter_id < this.helicopter_id){
            return 1;
        }
        else if (o.helicopter_id > this.helicopter_id) {
            return -1;
        }
        return 0;
    }
}
