
package controller;

/**
 * @since 17.03.2014
 * @author Julian Schelker
 */
public interface TimeControlProvider {

    public enum Show {
        ALL, MOMENT, PROGRESS;
    }

    public Show getShow();

    /**
     * @return a number [0,1]
     */
    public double getShowPercentage();

    /**
     * 
     * @return false if should show all movement
     */
    public boolean isShowSomeMomentInTime();
}
