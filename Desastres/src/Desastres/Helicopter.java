package Desastres;

import java.util.Objects;

/**
 * @author Pol Casacuberta
 */
public class Helicopter implements Comparable<Helicopter> {
    /**
     * Constructora dado un centro, un helicóptero, un número de grupos dentro del helicóptero, el id de la posición
     * donde se encuentra y de si se encuentra en un centro o un grupo
     *
     * @param center_id       el helicóptero pertenece a este centro
     * @param helicopter_id   id del helicóptero
     * @param n_groups        el helicóptero contiene n grupos
     * @param center_or_group el helicóptero està situado en un centro o en un grupo
     * @param id_position     identificador del lugar donde se encuentra
     * @param npersonas       Cantidad de personas en el helicoptero
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
    private final int center_id;
    /**
     * Helicopter id
     */
    private final int helicopter_id;
    /**
     * Amount of groups on it
     */
    private int n_groups;
    /**
     * Indica si el helicóptero se encuentra dentro de un centro o de un grupo
     */
    private estado.centerOrGroup center_or_group;
    /**
     * Identificador de la posición del helicóptero, ya sea de un centro o de un grupo
     */
    private int id_position;

    /**
     * Número de personas
     */
    private int npersonas;

    /**
     * Obtiene las personas en el helicóptero
     *
     * @return npersonas
     */
    public int getNpersonas() {
        return npersonas;
    }

    /**
     * Actualiza las personas en el helicóptero
     *
     * @param npersonas personas
     */
    public void setNpersonas(int npersonas) {
        this.npersonas = npersonas;
    }


    /**
     * Obtiene el id del centro
     *
     * @return id centro
     */
    public int getCenter_id() {
        return center_id;
    }

    /**
     * Obtiene id del helicóptero
     *
     * @return helicopter id
     */
    public int getHelicopter_id() {
        return helicopter_id;
    }

    /**
     * Obtiene número de grupos
     *
     * @return n groups
     */
    public int getN_groups() {
        return n_groups;
    }

    /**
     * Actualiza el número de grupos
     * @param n_groups número de grupos
     */
    public void setN_groups(int n_groups) {
        this.n_groups = n_groups;
    }

    /**
     * Obtiene si lugar donde se encuentra es un grupo o un centro
     * @return centro o grupo
     */
    public estado.centerOrGroup getCenter_or_group() {
        return center_or_group;
    }

    /**
     * Actualiza el lugar donde se encuentra, grupo o centro
     * @param center_or_group centro o grupo
     */
    public void setCenter_or_group(estado.centerOrGroup center_or_group) {
        this.center_or_group = center_or_group;
    }

    /**
     * Obtiene el identificador de la posición
     * @return identificador de la posición
     */
    public int getId_position() {
        return id_position;
    }

    /**
     * Actualiza el identificador de la posición
     * @param id_position identificador de la posición
     */
    public void setId_position(int id_position) {
        this.id_position = id_position;
    }

    /**
     * Permite decir si dos helicopteros son iguales o no
     * @param o objeto helicóptero
     * @return True si són iguales, false si no lo son
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Helicopter)) return false;
        Helicopter that = (Helicopter) o;
        return center_id == that.center_id && helicopter_id == that.helicopter_id;
    }

    /**
     * Devuelve un Hash code
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(center_id, helicopter_id);
    }

    /**
     * Permite comparar dos helicópteros
     * @param o objeto helicóptero
     * @return devuelve 1 -1 o 0
     */
    @Override
    public int compareTo(Helicopter o) {
        if (o.helicopter_id < this.helicopter_id) {
            return 1;
        } else if (o.helicopter_id > this.helicopter_id) {
            return -1;
        }
        return 0;
    }
}
