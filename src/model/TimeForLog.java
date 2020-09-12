
package model;

import javares.math.MathJS;


/**
 * @since 02.06.2014
 * @author Julian Schelker
 */
public abstract class TimeForLog {

    public float getTime(Log l) {
        return (float) getTimeD(l);
    }

    public abstract double getTimeD(Log l);

    public abstract boolean isConstant();

    public abstract String getDisplayText();

    public static class Constant extends TimeForLog {

        private double v;

        public Constant(double v) {
            this.v = v;
        }

        @Override
        public double getTimeD(Log l) {
            return this.v;
        }

        @Override
        public boolean isConstant() {
            return true;
        }

        @Override
        public String getDisplayText() {
            return "time: " + new Time(this.v).toMMSS();
        }
    }

    public static class AskLog extends TimeForLog {

        private double progress;

        public AskLog(double progress) {
            this.progress = progress;
        }

        @Override
        public double getTimeD(Log l) {
            return l.getTimeForProgress(this.progress);
        }

        @Override
        public boolean isConstant() {
            return false;
        }

        @Override
        public String getDisplayText() {
            return "progress: " + MathJS.round(this.progress * 100, 1)
                + "% (Caution: nonlinear time lapse for players)";
        }

    }

}
