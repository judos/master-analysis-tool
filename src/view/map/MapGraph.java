
package view.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.ArrayList;

import javares.control.Listener;
import javares.data.SerializerException;
import javares.data.TupleR;
import javares.files.config.Config;
import javares.files.config.Property;
import javares.games.unitCoordination.PointF;
import javares.graphics.ImageJS;

import javax.swing.JPanel;

import model.Log;
import model.Minimap;
import model.Time;
import model.TimeForLog;
import model.Vec3;
import model.events.EnergyEvent;
import model.events.Event;
import model.events.UnidentifiedEvent;
import model.game.EmissaireLog;
import model.util.GraphicsUtil;
import model.util.PolygonUtil;
import view.FontObjects;
import controller.DataProvider;
import controller.ScreenCoordsConverter;
import controller.TimeControlProvider;
import controller.TimeControlProvider.Show;


/**
 * @since 10.02.2014
 * @author Julian Schelker
 */
public class MapGraph extends JPanel implements MouseMotionListener, MouseListener,
    MouseWheelListener, Listener {

    private static final long serialVersionUID = -7299753647623715952L;
    private static final double SHOW_VIEW_DIRECTION_DISTANCE = 200;
    private PointF draggingOriginPoint;
    private PointF drawOffset;
    private double scale;
    private ScreenCoordsConverter posCV;
    private DataProvider dataprovider;
    private Config config;
    private Image imgDeathPoint;
    private Property vanish;
    private TimeControlProvider timeProvider;
    private Image imgLamps;
    private Image imgChest;
    private Image imgProgress;

    public MapGraph(Config config, DataProvider dataprovider,
        TimeControlProvider timeControlProvider) {
        this.config = config;
        this.dataprovider = dataprovider;
        this.dataprovider.addListener(this);
        this.timeProvider = timeControlProvider;
        this.setPreferredSize(new Dimension(800, 600));
        this.setMinimumSize(new Dimension(200, 100));
        this.setDoubleBuffered(true);
        configLoad(config);
        this.posCV = null;
        this.imgDeathPoint = ImageJS.loadBufferedImage(new File("img/kreuz.png"));
        this.imgLamps = ImageJS.loadBufferedImage(new File("img/lamps.png"));
        this.imgChest = ImageJS.loadBufferedImage(new File("img/kiste.png"));
        this.imgProgress = ImageJS.loadBufferedImage(new File("img/gutzeichen.png"));

        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.addMouseWheelListener(this);
        repaint();
    }

    @Override
    public void actionPerformed(Object source, String message, Object value) {
        final Minimap m = this.dataprovider.getMinimap();
        posCV = new ScreenCoordsConverter(m.getStartX(), m.getEndX(), m.getStartY(),
            m.getEndY(), m.getImageSynchronous());
        repaint();
    }

    private void configLoad(Config config) {
        Property scale = config.getPropertyByName("gf_scale");
        if (scale.isValidDouble())
            this.scale = scale.getDouble();
        else
            this.scale = 0.2;
        Property offset = config.getPropertyByName("gf_offset");
        try {
            this.drawOffset = (PointF) offset.getObject();
        } catch (SerializerException e) {
            e.printStackTrace();
            this.drawOffset = new PointF(0, 0);
        }

        this.vanish = this.config.getPropertyByName("timeUntilPositionVanishes");
        if (!vanish.isValidInt())
            vanish.setValue(60);
    }

    public void configSave(Config config) {
        Property scale = config.getPropertyByName("gf_scale");
        scale.setValue(this.scale);
        Property offset = config.getPropertyByName("gf_offset");
        try {
            offset.setValue(this.drawOffset);
        } catch (SerializerException e) {
            System.err.println("couldn't store gf_offset in config");
            e.printStackTrace();
        }
    }

    private void drawDeathPoints(Graphics2D g, Log[] drawData, TimeForLog showTime) {
        if (this.posCV == null)
            return;
        for (Log l : drawData) {
            float time = showTime.getTime(l);
            ArrayList<UnidentifiedEvent> death = l
                .filterEvents(UnidentifiedEvent.Type.DEATH_POINT);
            for (UnidentifiedEvent d : death) {
                double eventTime = d.getTime();
                if (eventTime <= time) {
                    PointF pos = l.getPlayerPosAtTime(eventTime);
                    double alpha = getAlphaFromTime(eventTime, time);
                    GraphicsUtil.setGraphicsAlpha(g, alpha);
                    drawImageAt(g, this.imgDeathPoint, pos, l.getName());
                    Point drawPoint = this.posCV.transW2S(pos);
                    g.drawImage(this.imgDeathPoint,
                        drawPoint.x - this.imgDeathPoint.getWidth(null) / 2, drawPoint.y
                            - this.imgDeathPoint.getHeight(null) / 2, null);
                    if (this.dataprovider.isOptionSet(DataProvider.PLAYER_NAMES)) {
                        g.setColor(Color.red);
                        g.drawString(l.getName(),
                            drawPoint.x + this.imgDeathPoint.getWidth(null) / 2,
                            drawPoint.y);
                    }
                }
            }
        }
    }

    private double getAlphaFromTime(double eventTime, float guiShowTime) {
        double alpha = 1;
        if (this.timeProvider.isShowSomeMomentInTime()) {
            double goneSince = guiShowTime - eventTime;
            alpha = 1 - (goneSince / this.vanish.getInt());
            if (alpha < 0)
                alpha = 0;
            if (alpha > 1)
                alpha = 1;
        }
        return alpha;
    }

    private void drawEntityPositions(Graphics2D g, Log cur, float guiShowTime) {
        if (this.dataprovider.isOptionSet(DataProvider.PLAYER_POS)) {
            ArrayList<TupleR<Time, Vec3>> movement = cur.getPlayerPositionInTime();
            drawOneEntity(g, cur, guiShowTime, movement, new Color(1f, 1f, 1f), true);
        }

        if (this.dataprovider.isOptionSet(DataProvider.ENEMY_POS)) {
            String[] allEnt = cur.getEntities();
            for (String entity : allEnt) {
                if (!entity.equals(EmissaireLog.PLAYER_NAME)) {
                    ArrayList<TupleR<Time, Vec3>> movement = cur
                        .getPositionsInTime(entity);
                    drawOneEntity(g, cur, guiShowTime, movement, new Color(1f, 0f, 0f),
                        false);
                }
            }
        }
    }

    private void drawOneEntity(Graphics2D g, Log cur, float guiShowTime,
        ArrayList<TupleR<Time, Vec3>> movement, Color color, boolean player) {
        Vec3 lastPos = null;
        g.setStroke(new BasicStroke(2));
        if (this.timeProvider.isShowSomeMomentInTime()) {
            double showTime = guiShowTime + movement.get(0).e0.getTime();
            int nextPoint;
            for (nextPoint = 0; nextPoint < movement.size(); nextPoint++) {
                if (movement.get(nextPoint).e0.getTime() > showTime)
                    break;
            }
            // interpolate first point
            if (nextPoint >= 1 && nextPoint < movement.size()) {
                TupleR<Time, Vec3> curX = movement.get(nextPoint);
                TupleR<Time, Vec3> last = movement.get(nextPoint - 1);
                double inter = (showTime - last.e0.getTime())
                    / (curX.e0.getTime() - last.e0.getTime());
                PointF p1 = this.posCV.transFW2S(curX.e1);
                PointF p2 = this.posCV.transFW2S(last.e1);
                Point start = p2.getPoint();
                Point end = p2.add(p1.subtract(p2).scale((float) inter)).getPoint();

                g.setColor(color);
                g.drawLine(start.x, start.y, end.x, end.y);
                g.setColor(color.darker());
                g.fillOval(end.x - 8, end.y - 8, 17, 17);

                if (player) {
                    g.setColor(Color.red);
                    g.fillRect(end.x - 15, end.y - 15, 30, 8);
                    g.setColor(Color.green);
                    int width = (int) (cur.getPlayerLifeAtTime(showTime) * 30);
                    g.fillRect(end.x - 15, end.y - 15, width, 8);
                }

                if (this.dataprovider.isOptionSet(DataProvider.PLAYER_NAMES)) {
                    FontMetrics m = g.getFontMetrics();
                    int wi = m.stringWidth(cur.getName());
                    g.drawString(cur.getName(), end.x - wi / 2, end.y - m.getHeight());
                }
            }

            if (nextPoint >= movement.size())
                nextPoint = movement.size() - 1;
            for (int point = nextPoint - 1; point >= 1; point--) {
                TupleR<Time, Vec3> curX = movement.get(point);
                TupleR<Time, Vec3> last = movement.get(point - 1);
                // blend the last minute
                double alpha = getAlphaFromTime(curX.e0.getTime(), guiShowTime);
                if (alpha <= 0)
                    break;
                Color c = null;
                try {
                    c = new Color(color.getRed(), color.getBlue(), color.getGreen(),
                        (int) (255 * alpha));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(color + " alpha: " + alpha + " alpha int: "
                        + ((int) (255 * alpha)));
                }
                Point p1 = this.posCV.transW2S(curX.e1);
                Point p2 = this.posCV.transW2S(last.e1);
                g.setColor(c);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

        }
        else {
            g.setColor(color);
            for (TupleR<Time, Vec3> spot : movement) {
                if (lastPos != null) {
                    if (this.posCV != null) {
                        Point start = this.posCV.transW2S(lastPos);
                        Point end = this.posCV.transW2S(spot.e1);
                        g.drawLine(start.x, start.y, end.x, end.y);
                    }
                }
                lastPos = spot.e1;
            }
        }

    }

    private float getTimeForAllLogs(Log[] drawData) {
        double maxTimeSpan = 0;
        for (Log cur : drawData) {
            ArrayList<TupleR<Time, Vec3>> movement = cur.getPlayerPositionInTime();
            double timeMin = movement.get(0).e0.getTime();
            double timeMax = movement.get(movement.size() - 1).e0.getTime();
            double timeSpan = timeMax - timeMin;
            if (timeSpan > maxTimeSpan)
                maxTimeSpan = timeSpan;
        }
        double timePer = this.timeProvider.getShowPercentage();
        return (float) (timePer * maxTimeSpan);
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        Point point = arg0.getPoint();
        System.out.println(this.posCV.transScreen2World(point));
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        if (posCV == null)
            return;
        if (this.draggingOriginPoint == null)
            this.draggingOriginPoint = new PointF(arg0.getPoint());
        else {
            PointF newOrigin = new PointF(arg0.getPoint());
            PointF movement = newOrigin.subtract(this.draggingOriginPoint);
            this.drawOffset.addI(movement);
            this.posCV.setMove(this.drawOffset.x, this.drawOffset.y, this.scale);
            this.draggingOriginPoint = newOrigin;
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {

    }

    @Override
    public void mouseExited(MouseEvent arg0) {

    }

    @Override
    public void mouseMoved(MouseEvent arg0) {

    }

    @Override
    public void mousePressed(MouseEvent arg0) {

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        this.draggingOriginPoint = null;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent arg0) {
        int rot = arg0.getWheelRotation();
        double scaleBefore = this.scale;
        if (rot < 0)
            this.scale *= 1.30;
        else if (rot > 0)
            this.scale /= 1.30;
        if (this.scale > 2)
            this.scale = 2;

        Minimap minimap = this.dataprovider.getMinimap();
        if (minimap != null) {
            Image img = minimap.getImage();
            if (img != null) {
                int w = img.getWidth(null);
                int h = img.getHeight(null);

                // how much right is the mouse on the map? 0=left 1=right
                double perX = (arg0.getPoint().x - this.drawOffset.getX())
                    / (w * scaleBefore);
                double addWidth = (this.scale - scaleBefore) * w;
                this.drawOffset.x -= addWidth * perX;

                double perY = (arg0.getPoint().y - this.drawOffset.getY())
                    / (h * scaleBefore);
                double addHeight = (this.scale - scaleBefore) * h;
                this.drawOffset.y -= addHeight * perY;
            }
            if (this.posCV != null)
                this.posCV.setMove(this.drawOffset.x, this.drawOffset.y, this.scale);
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics gn) {
        Graphics2D g = (Graphics2D) gn;
        Composite c = g.getComposite();
        if (this.posCV != null)
            this.posCV.setMove(this.drawOffset.x, this.drawOffset.y, this.scale);
        Rectangle r = g.getClipBounds();
        g.setColor(Color.black);
        g.fillRect(r.x, r.y, r.width, r.height);

        paintMapOrLoadingScreen(g, r);

        Log[] drawData = this.dataprovider.getDisplayedLogs();
        if (drawData != null) {

            TimeForLog showTime = null;

            // make sure everything is displayed
            if (this.timeProvider.getShow() == Show.ALL)
                showTime = new TimeForLog.Constant(Float.MAX_VALUE);
            else if (this.timeProvider.getShow() == Show.MOMENT)
                showTime = new TimeForLog.Constant(getTimeForAllLogs(drawData));
            else
                showTime = new TimeForLog.AskLog(this.timeProvider.getShowPercentage());

            for (Log cur : drawData) {
                drawEntityPositions((Graphics2D) g, cur, showTime.getTime(cur));
            }
            if (this.dataprovider.isOptionSet(DataProvider.LAMPS_TAKEN))
                drawLamps(g, drawData, showTime);
            if (this.dataprovider.isOptionSet(DataProvider.CHESTS_FOUND))
                drawChests(g, drawData, showTime);
            if (this.dataprovider.isOptionSet(DataProvider.DEATH_POINTS))
                drawDeathPoints(g, drawData, showTime);
            if (this.dataprovider.isOptionSet(DataProvider.VIEW_DIRECTIONS)
                && this.timeProvider.isShowSomeMomentInTime())
                drawViewDirection(g, drawData, showTime);
            if (this.dataprovider.isOptionSet(DataProvider.MISSION_PROGRESS))
                drawMissionProgress(g, drawData, showTime);

            if (this.timeProvider.isShowSomeMomentInTime()) {
                g.setComposite(c);
                g.setFont(FontObjects.MEDIUM_L_BOLD);
                g.setColor(Color.white);
                FontMetrics fm = g.getFontMetrics();
                String text = showTime.getDisplayText();
                g.drawString(text, r.width / 2 - fm.stringWidth(text) / 2,
                    fm.getAscent() + 5);
            }
        }
    }

    private void drawViewDirection(Graphics2D g, Log[] drawData, TimeForLog showTime) {
        GraphicsUtil.setGraphicsAlpha(g, 1);
        for (Log l : drawData) {
            float time = showTime.getTime(l);
            if (this.dataprovider.isOptionSet(DataProvider.PLAYER_POS))
                drawViewDirectionFor(EmissaireLog.PLAYER_NAME, g, l, time);
            if (this.dataprovider.isOptionSet(DataProvider.ENEMY_POS)) {
                for (String enemy : l.getEntitiesWithoutPlayer())
                    drawViewDirectionFor(enemy, g, l, time);
            }

        }
    }

    private void drawViewDirectionFor(String entity, Graphics2D g, Log log,
        float guiShowTime) {
        if (this.posCV == null)
            return;
        if (guiShowTime > log.getEndTime())
            return;
        PointF pos = log.getEntityPosAtTime(entity, guiShowTime);
        double angle = log.getEntityViewAngleAtTime(entity, guiShowTime);
        final double DEGREE = 2 * Math.PI / 360;
        angle = this.posCV.transAngleW2S(angle);
        Point screenPos = this.posCV.transW2S(pos);
        Polygon poly = PolygonUtil.createSector(screenPos, angle, 65 * DEGREE,
            SHOW_VIEW_DIRECTION_DISTANCE * this.scale);
        g.setColor(new Color(128, 255, 128, 64));
        if (!entity.equals(EmissaireLog.PLAYER_NAME))
            if (log.getAttentionAtTime(entity, guiShowTime) != 0)
                g.setColor(new Color(255, 128, 128, 64));
        g.fillPolygon(poly);
    }

    private void drawMissionProgress(Graphics2D g, Log[] drawData, TimeForLog showTime) {
        if (this.posCV == null)
            return;
        for (Log l : drawData) {
            float time = showTime.getTime(l);
            ArrayList<Event> events = l.getEventsProgress();
            for (Event d : events) {
                if (d.getTime() <= time) {
                    PointF pos = l.getPlayerPosAtTime(d.getTime());
                    double alpha = getAlphaFromTime(d.getTime(), time);
                    GraphicsUtil.setGraphicsAlpha(g, alpha);
                    String label = null;
                    if (this.dataprovider.isOptionSet(DataProvider.MISSION_NAME_LABELS))
                        label = d.getName();
                    drawImageAt(g, this.imgProgress, pos, l.getName(), label);
                }
            }
        }
    }

    private void drawChests(Graphics2D g, Log[] drawData, TimeForLog showTime) {
        if (this.posCV == null)
            return;
        for (Log l : drawData) {
            float time = showTime.getTime(l);
            ArrayList<EnergyEvent> chest = l.getEventsChestFound();
            for (EnergyEvent d : chest) {
                if (d.getTime() <= time) {
                    PointF pos = l.getPlayerPosAtTime(d.getTime());
                    double alpha = getAlphaFromTime(d.getTime(), time);
                    GraphicsUtil.setGraphicsAlpha(g, alpha);
                    drawImageAt(g, this.imgChest, pos, l.getName(), d.getName());
                }
            }
        }
    }

    private void drawLamps(Graphics2D g, Log[] drawData, TimeForLog showTime) {
        if (this.posCV == null)
            return;
        for (Log l : drawData) {
            float time = showTime.getTime(l);
            ArrayList<EnergyEvent> lamps = l.getEventsLampTaken();
            for (EnergyEvent d : lamps) {
                if (d.getTime() <= time) {
                    PointF pos = l.getPlayerPosAtTime(d.getTime());
                    double alpha = getAlphaFromTime(d.getTime(), time);
                    GraphicsUtil.setGraphicsAlpha(g, alpha);
                    drawImageAt(g, this.imgLamps, pos, l.getName(), d.getName());
                }
            }
        }
    }

    private void drawImageAt(Graphics2D g, Image image, PointF pos, String playerName) {
        drawImageAt(g, image, pos, playerName, null);
    }

    private void drawImageAt(Graphics2D g, Image image, PointF pos, String playerName,
        String descr2) {
        Point drawPoint = this.posCV.transW2S(pos);
        g.drawImage(image, drawPoint.x - image.getWidth(null) / 2,
            drawPoint.y - image.getHeight(null) / 2, null);
        if (this.dataprovider.isOptionSet(DataProvider.PLAYER_NAMES)) {
            g.setColor(Color.red);
            g.drawString(playerName, drawPoint.x + image.getWidth(null) / 2, drawPoint.y);
        }
        if (this.dataprovider.isOptionSet(DataProvider.MISSION_NAME_LABELS)
            && descr2 != null) {
            g.setColor(Color.green);
            int h = g.getFontMetrics().getHeight();
            g.drawString(descr2, drawPoint.x + image.getWidth(null) / 2, drawPoint.y + h);
        }
    }

    private void paintMapOrLoadingScreen(Graphics2D g, Rectangle r) {
        Minimap minimap = this.dataprovider.getMinimap();
        if (minimap != null) {
            Image img = minimap.getImage();
            if (img != null) {
                int w = img.getWidth(null);
                int h = img.getHeight(null);
                g.drawImage(img, this.drawOffset.getX(), this.drawOffset.getY(),
                    (int) (w * scale), (int) (h * scale), null);
            }
            else { // Loading screen
                g.setColor(Color.green);
                g.setFont(FontObjects.BIG);
                FontMetrics m = g.getFontMetrics();
                String display = "Loading minimap...";
                int w2 = m.stringWidth(display);
                g.drawString(display, (int) r.getWidth() / 2 - w2 / 2,
                    (int) r.getHeight() / 2);
            }
        }
    }

}
