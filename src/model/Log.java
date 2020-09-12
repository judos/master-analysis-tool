
package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import javares.data.Serializer;
import javares.data.SerializerException;
import javares.data.TupleR;
import javares.files.FileUtils;
import javares.games.unitCoordination.PointF;
import javares.math.MathJS;
import model.events.EmissaireAttack;
import model.events.EnergyEvent;
import model.events.Event;
import model.events.EventFacts;
import model.events.FindEvent;
import model.events.NamedDamageEvent;
import model.events.TimeComparator;
import model.events.UnidentifiedEvent;
import model.events.UnidentifiedEvent.Type;
import model.game.EmissaireLog;
import model.game.EmissaireMechanics;
import model.game.EmissaireObjects;
import model.game.EmissaireProgress;
import model.questions.Evaluation;
import model.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @since 10.02.2014
 * @author Julian Schelker
 */
public class Log {

    private String name;
    private ArrayList<PositionGroup> positions;
    private ArrayList<UnidentifiedEvent> events;
    private boolean hasEvaluation;
    private Evaluation evaluation;
    private File evaluationFile;
    private ArrayList<EnergyEvent> lampsTaken;
    private ArrayList<EnergyEvent> chestsFound;
    private ArrayList<EnergyEvent> eyeReturnedEnergy;
    private ArrayList<NamedDamageEvent> damageEvents;
    private ArrayList<Event> eventsProgress;
    private HashMap<String, Double> progressTimes;
    private double initialTime;
    private CombatAnalyzer combatAnalyzer;
    private ArrayList<Double> progressPointTimes;
    private ArrayList<TimeEnergy> playerEnergy;
    public static ArrayList<String> progressTimeGoals;

    public Log(File log) {
        this.name = log.getName();
        parsePositions(new File(log, "logging1.xml"));
        parseEvents(new File(log, "logging2.xml"));
        this.hasEvaluation = false;
        this.evaluationFile = new File(log, "evaluation form.dat");
        parseEvaluation();
        analyzeEnergy();
        analyzeProgress();
    }

    private void analyzeProgress() {
        final float radiusProgressPoint = 4;
        this.progressPointTimes = new ArrayList<Double>();
        this.progressPointTimes.add(0.);
        PointF[] pro = EmissaireProgress.progress;
        int currentPro = 0;
        for (PositionGroup p : this.positions) {
            EntityInfo i = p.getEntityInfo(EmissaireLog.PLAYER_NAME);
            Vec3 pos = i.getPos();
            float d = pro[currentPro].distanceTo(new PointF(pos.getX(), pos.getY()));
            if (d < radiusProgressPoint) {
                this.progressPointTimes.add(p.getTime());
                currentPro++;
                if (currentPro == pro.length)
                    break;

            }
        }
        this.progressPointTimes.add(getEndTime());
    }

    public double getTimeForProgress(double progress) {
        double stepInList = 1. / (this.progressPointTimes.size() - 1);
        int lowerIndex = MathJS.floor(progress / stepInList);
        double remainPer = (progress % stepInList) / stepInList;

        if (lowerIndex > this.progressPointTimes.size() - 2)
            return getEndTime();

        double t1 = this.progressPointTimes.get(lowerIndex);
        double t2 = this.progressPointTimes.get(lowerIndex + 1);

        return t1 + (t2 - t1) * remainPer;
    }

    public CombatAnalyzer getCombat() {
        if (this.combatAnalyzer == null) {
            this.combatAnalyzer = new CombatAnalyzer();
            this.combatAnalyzer.initialize(this);
        }
        return this.combatAnalyzer;
    }

    public HashMap<String, Integer> getTotalEnergyStat() {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        String[] keys = getTotalEnergyStatKeys();
        for (String key : keys)
            result.put(key, 0);

        ArrayList<NamedDamageEvent> x = getEventsDamage();
        for (NamedDamageEvent e : x) {
            String key = "Other";
            if (e.getType() == FindEvent.EMISSAIRE_ATTACK)
                key = "Emissaire Attacks";
            else if (e.getType() == FindEvent.SHADOW_ATTACK)
                key = "Shadow Attack";
            else if (e.getType() == FindEvent.FALL_DAMAGE)
                key = "Fall Damage";
            Map.add(result, key, e.getEnergyDifference());
        }
        String key = "Lamps taken";
        for (EnergyEvent e : lampsTaken) {
            Map.add(result, key, e.getEnergyDifference());
        }
        key = "Chest found";
        for (EnergyEvent e : chestsFound) {
            Map.add(result, key, e.getEnergyDifference());
        }
        key = "Mechanical Eye";
        for (EnergyEvent e : eyeReturnedEnergy) {
            Map.add(result, key, e.getEnergyDifference());
        }
        return result;
    }

    /**
     * @return the damageEvents
     */
    public ArrayList<NamedDamageEvent> getEventsDamage() {
        return damageEvents;
    }

    private void analyzeEnergy() {
        this.lampsTaken = summarizeObjectUsage("lamps");
        this.chestsFound = summarizeObjectUsage("chest");
        // this.fallDamage = summarizeFallDamages();
        this.damageEvents = new ArrayList<NamedDamageEvent>();
        this.eventsProgress = summarizeProgressObjects();
        summarizeProgressTimes();

        ArrayList<EventFacts> facts = summarizeFactsAndMechEyeEnergy();
        summarizeCheckEmissaireAttackDuration(facts);
        summarizeDamageCause(facts);
        summarizeDamageCheckShadowStartAttack(facts);
        summarizeDamageCheckShadowAttackDuration(facts);
        summarizeShadowAttacks(facts);

        for (EventFacts f : facts) {
            NamedDamageEvent dmg = findDamageCause(f);
            this.damageEvents.add(dmg);
        }
    }

    private void summarizeProgressTimes() {
        this.progressTimes = new HashMap<String, Double>();
        String[][] progressGroups = new String[][] {
            new String[] { "level started", "corridorSwitch on:1" },
            new String[] { "corridorSwitch on:1", "roofPanelSwitch" },
            new String[] { "roofPanelSwitch", "elevator up:1" },
            new String[] { "elevator up:1", "cityDoorSwitch on:1" },
            new String[] { "cityDoorSwitch on:1", "level solved" } };
        int nr = 1;
        progressTimeGoals = new ArrayList<String>();
        for (String[] group : progressGroups) {
            progressTimeGoals.add(nr + ". " + group[1]);
            try {
                double time1 = getTimeForProgressEvent(group[0]);
                double time2 = getTimeForProgressEvent(group[1]);
                double missionTime = time2 - time1;
                if (missionTime < 0)
                    throw new RuntimeException("invalid order of mission goals");
                this.progressTimes.put(nr + ". " + group[1], time2 - time1);

            } catch (Exception e) {}
            nr++;
        }
    }

    private double getTimeForProgressEvent(String eventName) {
        for (Event e : this.eventsProgress) {
            if (e.getName().equals(eventName))
                return e.getTime();
        }
        throw new InvalidParameterException("Couldn't find progress event named: "
            + eventName);
    }

    private ArrayList<Event> summarizeProgressObjects() {
        ArrayList<Event> result = new ArrayList<Event>();
        result.add(new Event("level started", 0));
        for (UnidentifiedEvent event : this.events) {
            if (event.isOfType(UnidentifiedEvent.Type.ITEM)) {
                String item = event.getValue("item");
                // everything that is not just a pickable item
                if (!item.startsWith("chest") && !item.startsWith("lamps")) {
                    double time = event.getTime();
                    result.add(new Event(item, time));
                }
            }
            if (event.isOfType(UnidentifiedEvent.Type.LEVEL_SOLVED))
                result.add(new Event("level solved", event.getTime()));
        }
        return result;
    }

    private void summarizeCheckEmissaireAttackDuration(ArrayList<EventFacts> facts) {
        for (int i = 0; i < facts.size(); i++) {
            EventFacts f = facts.get(i);
            if (f.getFindEvent().getEvent() == FindEvent.EMISSAIRE_ATTACK) {
                float t = f.getTime();
                // check forward
                for (int j = i + 1; j < facts.size(); j++) {
                    EventFacts f2 = facts.get(j);
                    if (Math.abs(f2.getTime() - t) <= 0.5)
                        f2.getFindEvent().set(FindEvent.EMISSAIRE_ATTACK, 0,
                            "emissaire doesn't have time");
                    else
                        break;
                }
                // check backward
                for (int j = i - 1; j >= 0; j--) {
                    EventFacts f2 = facts.get(j);
                    if (Math.abs(f2.getTime() - t) <= 0.5)
                        f2.getFindEvent().set(FindEvent.EMISSAIRE_ATTACK, 0,
                            "emissaire doesn't have time");
                    else
                        break;
                }
            }
        }
    }

    private void summarizeShadowAttacks(ArrayList<EventFacts> facts) {
        for (EventFacts f : facts) {
            if (f.getFindEvent().isAmbiguous()) {
                if (f.getLifeDiff() == -6)
                    f.getFindEvent().setOnly(FindEvent.SHADOW_ATTACK,
                        "emissaire can't attack so strong");
            }
        }

    }

    private void summarizeDamageCheckShadowAttackDuration(ArrayList<EventFacts> facts) {
        String descr = "";
        float lastTime = 0;
        for (int i = 0; i < facts.size(); i++) {
            EventFacts fact = facts.get(i);
            if (fact.getFindEvent().getEvent() == FindEvent.SHADOW_ATTACK) {
                descr = fact.getDamageOrigin();
                lastTime = fact.getTime();
                for (int j = i + 1; j < facts.size(); j++) {
                    EventFacts f2 = facts.get(j);
                    if (f2.getFindEvent().getEvent() == FindEvent.SHADOW_ATTACK) {
                        if (Math.abs(f2.getTime() - lastTime) < 0.7
                            && f2.getDamageOrigin().equals(descr))
                            f2.getFindEvent().set(FindEvent.SHADOW_ATTACK, 0,
                                "shadow can't attack so fast");
                    }
                    if (lastTime - f2.getTime() >= 0.7)
                        break;
                }
            }
        }
    }

    private void summarizeDamageCheckShadowStartAttack(ArrayList<EventFacts> facts) {
        float assumeTime = 0;
        for (int i = facts.size() - 1; i >= 0; i--) {
            EventFacts fact = facts.get(i);
            if (fact.getFindEvent().getEvent() == FindEvent.SHADOW_ATTACK
                && fact.getLifeDiff() == -6)
                assumeTime = fact.getTime();
            float tDiff = Math.abs(assumeTime - fact.getTime());
            if (tDiff > 0.7 && tDiff < 0.9 && fact.getLifeDiff() == -2)
                fact.getFindEvent().setOnly(FindEvent.SHADOW_ATTACK,
                    "logical consequence");
        }
    }

    private void summarizeDamageCause(ArrayList<EventFacts> facts) {
        for (EventFacts fact : facts) {
            FindEvent f = fact.getFindEvent();
            if (f.isAmbiguous()) {
                double move = getMaxMoveAmountDuringStunForShadows(fact.getTime(),
                    fact.getLifeDiff());
                if (move >= 0.5)
                    f.set(FindEvent.EMISSAIRE_ATTACK, 0, "ShadowMove=" + move + ">=0.5");
                if (move < 0.045)
                    f.set(FindEvent.SHADOW_ATTACK, 0, "ShadowMove=" + move + "<0.045");
                if (f.isAmbiguous()) {
                    if (move >= 0.4)
                        f.setOnly(FindEvent.SHADOW_ATTACK, "ShadowMove=" + move + ">=0.4");
                    if (move < 0.1)
                        f.setOnly(FindEvent.EMISSAIRE_ATTACK, "ShadowMove=" + move
                            + "<0.1");
                }
            }
        }
    }

    private ArrayList<EventFacts> summarizeFactsAndMechEyeEnergy() {
        this.eyeReturnedEnergy = new ArrayList<EnergyEvent>();
        this.playerEnergy = new ArrayList<TimeEnergy>();
        ArrayList<EventFacts> facts = new ArrayList<EventFacts>();
        Integer life = null;
        int lifeNew = 0;
        for (UnidentifiedEvent ch : this.events) {
            if (ch.isOfType(UnidentifiedEvent.Type.EYE_RETURNED)) {
                int energyReceived = 100 - life;
                this.eyeReturnedEnergy.add(new EnergyEvent("MechEye", ch.getTime(),
                    energyReceived));
                lifeNew = 100;
            }
            else if (ch.isOfType(UnidentifiedEvent.Type.HEALTH_CHANGE)) {
                lifeNew = Integer.valueOf(ch.getValue("life"));
                if (lifeNew < 0)
                    lifeNew = 0;
                if (life != null) {
                    int lifeDiff = lifeNew - life;
                    if (lifeDiff < 0) { // check where the damage came from
                        EventFacts out = findDamageFacts(ch.getTime(), lifeDiff);
                        facts.add(out);
                    }
                }
            }
            life = lifeNew;
            this.playerEnergy.add(new TimeEnergy(ch.getTime(), life));
        }
        return facts;
    }

    private EventFacts findDamageFacts(float time, int lifeDiff) {
        float[] shadowDistances = getShadowDistance(time);
        float minShadowDistance = MathJS.min(shadowDistances);
        float movementEmissaire = getPlayerMovementDistanceIn(time - 1, time);
        float deltaZ = getMovementDistanceZOfIn(EmissaireLog.PLAYER_NAME, time - 1, time);
        FindEvent f = new FindEvent();
        if (minShadowDistance > 2)
            f.set(FindEvent.SHADOW_ATTACK, 0, "ShadowDistance=" + minShadowDistance
                + ">2");
        if (deltaZ < 0.5)
            f.set(FindEvent.FALL_DAMAGE, 0, "deltaZ<0.5");
        if (deltaZ >= 2)
            f.setOnly(FindEvent.FALL_DAMAGE, "deltaZ>=2");
        if (lifeDiff != -2 && lifeDiff != -6)
            f.set(FindEvent.SHADOW_ATTACK, 0, "lifeDiff!=2,6");
        if (movementEmissaire > 0.8 && f.isAmbiguous())
            f.setOnly(FindEvent.SHADOW_ATTACK, "EmissaireMove>0.8");

        return new EventFacts(shadowDistances, movementEmissaire, deltaZ, f, time,
            lifeDiff, getEntitiesWithoutPlayer());
    }

    private NamedDamageEvent findDamageCause(EventFacts fact) {
        String description = "";
        FindEvent f = fact.getFindEvent();
        if (f.getEvent() == FindEvent.SHADOW_ATTACK)
            description += getEntitiesWithoutPlayer()[MathJS.minIndex(fact
                .getShadowDistances())];

        if (f.getEvent() == FindEvent.EMISSAIRE_ATTACK)
            return new EmissaireAttack(fact, getEntitiesWithoutPlayer());

        NamedDamageEvent x = new NamedDamageEvent(fact.getTime(), fact.getLifeDiff(),
            f.getEvent(), f.getReason(), description);
        return x;
    }

    private double getMaxMoveAmountDuringStunForShadows(float time, int lifeDiff) {
        double[] move = new double[getEntities().length - 1];
        Vec3 playerPos = getEntityVecAtTime(EmissaireLog.PLAYER_NAME, time);
        int i = 0;
        for (String enemy : getEntities()) {
            if (!enemy.equals(EmissaireLog.PLAYER_NAME)) {
                Vec3 pos = getEntityVecAtTime(enemy, time);
                float d = pos.distanceTo(playerPos);
                if (d < 6) {
                    double charge = EmissaireMechanics
                        .getChargeScaled(Math.abs(lifeDiff));
                    float stun = (float) EmissaireMechanics.getFreezeTime(charge, d);
                    if (stun > 0) {
                        move[i] = getMovementDistanceOfIn(enemy, time + 0.5f, time + 0.5f
                            + stun);
                        move[i] /= stun;
                    }
                    else
                        move[i] = 0;
                }
                i++;
            }
        }
        return MathJS.max(move);
    }

    private float getPlayerMovementDistanceIn(float time1, float time2) {
        PointF pos1 = getPlayerPosAtTime(time1);
        PointF pos2 = getPlayerPosAtTime(time2);
        return pos1.distanceTo(pos2);
    }

    private float getMovementDistanceOfIn(String entity, float time1, float time2) {
        Vec3 pos1 = getEntityVecAtTime(entity, time1);
        Vec3 pos2 = getEntityVecAtTime(entity, time2);
        return pos1.distanceTo(pos2);
    }

    private float getMovementDistanceZOfIn(String entity, float time1, float time2) {
        Vec3 pos1 = getEntityVecAtTime(entity, time1);
        Vec3 pos2 = getEntityVecAtTime(entity, time2);
        return pos1.getZ() - pos2.getZ();
    }

    public float[] getShadowDistance(float time) {
        float[] result = new float[getEntities().length - 1];
        int index = 0;
        PointF playerPos = getEntityPosAtTime(EmissaireLog.PLAYER_NAME, time);
        for (String enemy : getEntitiesWithoutPlayer()) {
            PointF pos = getEntityPosAtTime(enemy, time);
            result[index] = pos.distanceTo(playerPos);
            index++;
        }
        return result;
    }

    public String[] getEntitiesWithoutPlayer() {
        String[] ent = getEntities();
        String[] result = new String[ent.length - 1];
        int index = 0;
        for (String e : ent) {
            if (!e.equals(EmissaireLog.PLAYER_NAME)) {
                result[index] = e;
                index++;
            }
        }
        return result;
    }

    private ArrayList<EnergyEvent> summarizeObjectUsage(String itemPrefix) {
        ArrayList<EnergyEvent> result = new ArrayList<EnergyEvent>();
        for (UnidentifiedEvent event : this.events) {
            if (event.isOfType(UnidentifiedEvent.Type.ITEM)) {
                String item = event.getValue("item");
                if (item.startsWith(itemPrefix)) {
                    double time = event.getTime();
                    int energy = EmissaireObjects.getEnergyByObject(item);
                    result.add(new EnergyEvent(item, time, energy));
                }
            }
        }
        return result;
    }

    public ArrayList<UnidentifiedEvent> filterEvents(Type t) {
        ArrayList<UnidentifiedEvent> result = new ArrayList<UnidentifiedEvent>();
        for (UnidentifiedEvent e : this.events) {
            if (e.isOfType(t))
                result.add(e);
        }
        return result;
    }

    public double getEndTime() {
        return this.positions.get(this.positions.size() - 1).getTime();
    }

    public String[] getEntities() {
        return this.positions.get(0).getEntityNames();
    }

    public Evaluation getEvaluation() {
        return this.evaluation;
    }

    public ArrayList<EnergyEvent> getEventsLampTaken() {
        return this.lampsTaken;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public PointF getPlayerPosAtTime(double showTime) {
        return getEntityPosAtTime(EmissaireLog.PLAYER_NAME, showTime);
    }

    /**
     * @param showTime
     * @return the last index such that positions(index).getTime() <= showTime
     */
    private int getLastIndexBeforeTime(double showTime) {
        int start = 0;
        int ende = this.positions.size() - 1;
        do {
            int testIndex = (start + ende) / 2;
            double t = this.positions.get(testIndex).getTime();
            if (t > showTime)
                ende = testIndex - 1;
            else if (t < showTime)
                start = testIndex + 1;
            else
                return testIndex;
            if (start > ende)
                return ende;
        } while (true);
    }

    public PointF getEntityPosAtTime(String entity, double showTime) {
        Vec3 r = getEntityVecAtTime(entity, showTime);
        return new PointF(r.getX(), r.getY());
    }

    /**
     * @param showTime
     * @return the percentage of health the player has at that time
     */
    public float getPlayerLifeAtTime(double showTime) {
        int index = 0;
        for (index = 0; index < this.playerEnergy.size(); index++) {
            if (this.playerEnergy.get(index).getTime() > showTime)
                break;
        }
        if (index >= this.playerEnergy.size())
            index = this.playerEnergy.size() - 1;
        return (float) this.playerEnergy.get(index).getLife() / 100;
    }

    public double getEntityViewAngleAtTime(String entity, double showTime) {
        int lastPoint = getLastIndexBeforeTime(showTime);
        // check boundary points
        if (lastPoint < 0) {
            lastPoint = 0;
            showTime = this.positions.get(0).getTime();
        }
        if (lastPoint > this.positions.size() - 2) {
            lastPoint = this.positions.size() - 2;
            showTime = this.positions.get(this.positions.size() - 1).getTime();
        }
        int nextPoint = lastPoint + 1;
        PositionGroup lastGroup = this.positions.get(lastPoint);
        PositionGroup nextGroup = this.positions.get(nextPoint);

        double interpolationPercentage = (showTime - lastGroup.getTime())
            / (nextGroup.getTime() - lastGroup.getTime());

        Vec3 lastFwd = lastGroup.getEntityInfo(entity).getForward();
        Vec3 nextFwd = nextGroup.getEntityInfo(entity).getForward();
        double lastAngle = Math.atan2(lastFwd.getY(), lastFwd.getX());
        double nextAngle = Math.atan2(nextFwd.getY(), lastFwd.getX());
        double interpolDiff = nextAngle - lastAngle;
        if (interpolDiff > Math.PI)
            nextAngle -= 2 * Math.PI;
        if (interpolDiff < -Math.PI)
            nextAngle += 2 * Math.PI;

        double angle = lastAngle * (1 - interpolationPercentage) + nextAngle
            * interpolationPercentage;
        return (angle + 2 * Math.PI) % (2 * Math.PI);
    }

    public Vec3 getEntityVecAtTime(String entity, double showTime) {
        ArrayList<TupleR<Time, Vec3>> movement = getPositionsInTime(entity);
        // first find nearest point in future
        int nextPoint;
        // for (nextPoint = 0; nextPoint < movement.size(); nextPoint++) {
        // if (movement.get(nextPoint).e0.getTime() > showTime)
        // break;
        // }
        nextPoint = getLastIndexBeforeTime(showTime) + 1;

        // interpolate point
        if (nextPoint >= 1 && nextPoint < movement.size()) {
            TupleR<Time, Vec3> curX = movement.get(nextPoint);
            TupleR<Time, Vec3> last = movement.get(nextPoint - 1);
            // calculate percentage between points
            double inter = (showTime - last.e0.getTime())
                / (curX.e0.getTime() - last.e0.getTime());
            Vec3 p1 = curX.e1;
            Vec3 p2 = last.e1;
            return p2.add(p1.subtract(p2).scale((float) inter));
        }
        else if (nextPoint == 0) {
            return movement.get(0).e1;
        }
        else {
            return movement.get(movement.size() - 1).e1;
        }
    }

    public ArrayList<TupleR<Time, Vec3>> getPlayerPositionInTime() {
        return getPositionsInTime(EmissaireLog.PLAYER_NAME);
    }

    public ArrayList<TupleR<Time, Vec3>> getPositionsInTime(String object) {
        ArrayList<TupleR<Time, Vec3>> result = new ArrayList<TupleR<Time, Vec3>>();
        for (PositionGroup pos : this.positions) {
            EntityInfo info = pos.getEntityInfo(object);
            result.add(new TupleR<Time, Vec3>(new Time(pos.getTime()), info.getPos()));
        }
        return result;
    }

    public HashSet<String> getUsedItemsAndEvents() {
        HashSet<String> result = new HashSet<String>();
        ArrayList<UnidentifiedEvent> events = filterEvents(UnidentifiedEvent.Type.ITEM);
        for (UnidentifiedEvent e : events) {
            String itemName = e.getValue("item");
            result.add(itemName);
        }
        events = filterEvents(UnidentifiedEvent.Type.LEVEL_SOLVED);
        for (UnidentifiedEvent e : events) {
            String eventName = e.getValue("event");
            result.add(eventName);
        }
        return result;
    }

    public boolean hasEvaluation() {
        return hasEvaluation;
    }

    public void output(double addTime, String output) throws IOException {
        File o = new File(output);
        File pos = new File(o, "logging1.xml");
        File events = new File(o, "logging2.xml");
        BufferedWriter outPos = new BufferedWriter(new FileWriter(pos));
        BufferedWriter outEvents = new BufferedWriter(new FileWriter(events));
        outputStart(outPos, outEvents);
        outputPositions(addTime, outPos);
        outputEvents(addTime, outEvents);
        outputEnd(outPos, outEvents);
        outPos.close();
        outEvents.close();
    }

    public void outputEnd(BufferedWriter outPos, BufferedWriter outEvents)
        throws IOException {
        outPos.write("</Root>");
        outEvents.write("</Root>");
    }

    public void outputEvents(double addTime, BufferedWriter outEvents) throws IOException {
        for (UnidentifiedEvent e : this.events) {
            outEvents.write(" " + e.toStringTimeDiff(addTime));
            outEvents.newLine();
        }
    }

    public void outputPositions(double addTime, BufferedWriter out) throws IOException {
        for (PositionGroup g : this.positions) {

            out.write(" <time>" + (g.getTime() + addTime) + "</time>");
            out.newLine();
            String[] names = g.getEntityNames();
            for (String entity : names) {
                EntityInfo info = g.getEntityInfo(entity);
                out.write(" " + info.toString());
                out.newLine();
            }
        }
    }

    public void outputStart(BufferedWriter outPos, BufferedWriter outEvents)
        throws IOException {
        outPos.write("<Root>");
        outPos.newLine();
        outEvents.write("<Root>");
        outEvents.newLine();
    }

    private void parseEvaluation() {
        if (this.evaluationFile.exists()) {
            try {
                this.evaluation = (Evaluation) Serializer
                    .stream2object(new FileInputStream(this.evaluationFile));
                this.hasEvaluation = true;
            } catch (FileNotFoundException | SerializerException e) {
                e.printStackTrace();
                System.err.println("Error with Evaluation for " + this.name);
            }
        }
    }

    private void parseEvents(File file) {
        this.events = new ArrayList<UnidentifiedEvent>();
        try {
            String content = FileUtils.readAllFile(file);
            Document doc = Serializer.text2Document(content);
            Element root = doc.getDocumentElement();
            NodeList nodes = root.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String nodeType = node.getNodeName();
                if (nodeType.equals("#text"))
                    continue;
                if (!nodeType.equals("event"))
                    throw new Exception("Unexpected node in logging2.xml: " + nodeType);
                String value = node.getChildNodes().item(0).getNodeValue();
                this.events.add(new UnidentifiedEvent(value, this.initialTime));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(this.events, new TimeComparator());
    }

    private void parsePositions(File file) {
        this.positions = new ArrayList<PositionGroup>();
        try {
            boolean isInitialTimeSet = false;
            String content = FileUtils.readAllFile(file);
            Document doc = Serializer.text2Document(content);
            Element root = doc.getDocumentElement();
            NodeList nodes = root.getChildNodes();
            PositionGroup lastGroup = null;
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String nodeType = node.getNodeName();
                if (nodeType.equals("time")) {
                    double time = Double.valueOf(node.getChildNodes().item(0)
                        .getNodeValue());
                    if (!isInitialTimeSet) {
                        this.initialTime = time;
                        isInitialTimeSet = true;
                    }
                    lastGroup = new PositionGroup(time - this.initialTime);
                    this.positions.add(lastGroup);
                }
                if (nodeType.equals("player") || nodeType.equals("enemy")) {
                    String valueGroup = node.getChildNodes().item(0).getNodeValue();
                    EntityInfo entity = new EntityInfo(nodeType.equals("player"));
                    String name = null;
                    for (String assignment : valueGroup.split(";")) {
                        String[] values = assignment.split("=");
                        switch (values[0]) {
                            case "name":
                                name = values[1];
                                entity.setName(name);
                                break;
                            case "pos":
                                entity.setPos(new Vec3(values[1]));
                                break;
                            case "fwdDir":
                                entity.setForward(new Vec3(values[1]));
                                break;
                            case "id":
                                entity.setId(Integer.valueOf(values[1]));
                                break;
                            case "attTarget":
                                entity.setTarget(Integer.valueOf(values[1]));
                                break;
                            case "energy":
                                entity.setEnergy(Float.valueOf(values[1]));
                                break;
                            default:
                                System.err.println("unknown property: " + values[0]);
                        }
                    }
                    lastGroup.addInfo(name, entity);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setEvaluation(Evaluation e) {
        try {
            FileOutputStream f = new FileOutputStream(this.evaluationFile);
            Serializer.object2Stream(e, f);
            f.close();
            this.hasEvaluation = true;
        } catch (Exception err) {
            System.err.println("Exception with writing evaluation for Log " + this.name);
        }

        this.evaluation = e;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public ArrayList<EnergyEvent> getEventsChestFound() {
        return this.chestsFound;
    }

    public void outputAllLifeLosses() {
        Integer life = null;
        int lifeNew = 0;
        for (UnidentifiedEvent ch : this.events) {
            if (ch.isOfType(UnidentifiedEvent.Type.EYE_RETURNED)) {
                lifeNew = 100;
            }
            else if (ch.isOfType(UnidentifiedEvent.Type.HEALTH_CHANGE)) {
                lifeNew = Integer.valueOf(ch.getValue("life"));
                if (life != null) {
                    int lifeDiff = lifeNew - life;
                    if (lifeDiff < 0) { // check where the damage came from
                        Time t = new Time(ch.getTime());
                        System.out.println(t.toMMSS() + " \t" + ch.getTime()
                            + ", \tlife: " + lifeDiff);
                    }
                }
            }
            life = lifeNew;
        }
    }

    public ArrayList<Event> getEventsProgress() {
        return this.eventsProgress;
    }

    public HashMap<String, Double> getProgressTimes() {
        return this.progressTimes;
    }

    public static String[] getTotalEnergyStatKeys() {
        return new String[] { "Emissaire Attacks", "Shadow Attack", "Fall Damage",
            "Lamps taken", "Chest found", "Mechanical Eye", "Other" };
    }

    public static HashMap<String, ArrayList<Integer>> getTotalEnergyStatForMultibleLogs(
        Log[] logs) {
        @SuppressWarnings("unchecked")
        HashMap<String, Integer>[] maps = new HashMap[logs.length];
        int index = 0;
        for (Log l : logs)
            maps[index++] = l.getTotalEnergyStat();
        return Map.listUp(maps);
    }

    public int getAttentionAtTime(String entity, float guiShowTime) {
        int index = getLastIndexBeforeTime(guiShowTime);
        PositionGroup group = this.positions.get(index);
        EntityInfo entityInfo = group.getEntityInfo(entity);
        return entityInfo.getTarget();
    }

}