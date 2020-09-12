
package model;

/**
 * @since 04.06.2014
 * @author Julian Schelker
 */
public class TimeEnergy {

    private float time;

    private int life;

    public TimeEnergy(float time, int life) {
        this.time = time;
        this.life = life;
    }

    /**
     * @return the time
     */
    public float getTime() {
        return time;
    }

    /**
     * @return the life
     */
    public int getLife() {
        return life;
    }

}
