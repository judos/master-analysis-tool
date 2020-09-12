
package controller;

import java.awt.Image;
import java.awt.Point;

import javares.games.unitCoordination.PointF;
import model.Vec3;


/**
 * @since 16.02.2014
 * @author Julian Schelker
 */
public class ScreenCoordsConverter {

    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private int width;
    private int height;
    private float offsetX;
    private float offsetY;
    private double scale;

    public ScreenCoordsConverter(double startX, double endX, double startY, double endY,
        Image img) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.width = img.getWidth(null);
        this.height = img.getHeight(null);
    }

    public void setMove(float offsetX, float offsetY, double scale) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.scale = scale;
    }

    /**
     * @param pos
     *            world coordinates
     * @return screen coordinates
     */
    public Point transW2S(PointF pos) {
        return transFW2S(pos.x, pos.y).getPoint();
    }

    public Point transW2S(Vec3 p) {
        return transFW2S(p).getPoint();
    }

    public PointF transFW2S(float px, float py) {
        double perX = (px - startX) / (endX - startX);
        double perY = (py - startY) / (endY - startY);
        float x = (float) (offsetX + perY * width * scale);
        float y = (float) (offsetY + perX * height * scale);
        return new PointF(x, y);
    }

    public PointF transFW2S(Vec3 p) {
        return transFW2S(p.getX(), p.getY());
    }

    public double transAngleW2S(double angle) {
        return -angle + Math.PI / 2;
    }

    public PointF transScreen2World(Point p) {
        double perX = (p.getY() - offsetY) / height / scale;
        double perY = (p.getX() - offsetX) / width / scale;
        double wX = perX * (endX - startX) + startX;
        double wY = perY * (endY - startY) + startY;
        return new PointF((float) wX, (float) wY);
    }

}
