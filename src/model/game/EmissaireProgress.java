
package model.game;

import javares.games.unitCoordination.PointF;


/**
 * @since 02.06.2014
 * @author Julian Schelker
 */
public class EmissaireProgress {

    public static PointF[] progress = new PointF[] { new PointF(203, 292), // leaving
                                                                           // dock
        new PointF(203, 317), // first door
        new PointF(232, 304), // second door
        p(225, 313), // up the stairs
        p(248, 304), // beginning of corridor
        p(248, 245), // end of corridor
        p(221, 240), // down at the water again
        p(219, 236), // turning left
        p(234, 216), // elevator
        p(221, 213), // near shadow 3
        p(213, 208), // going up the ramp
        p(230, 220), // being on top, in the curve
        p(252, 238), // before the hallway
        p(252, 311), // lever to open exit
        p(264, 266) // the exit
    };

    public static PointF p(float x, float y) {
        return new PointF(x, y);
    }
}
