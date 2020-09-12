package controller;

import javares.control.Listener;
import model.Log;
import model.Minimap;

/**
 * @since 16.02.2014
 * @author Julian Schelker
 */
public interface DataProvider {

	String PLAYER_POS = "playerPosInTime";
	String ENEMY_POS = "enemyPosInTime";
	String DEATH_POINTS = "deathPoints";
	String PLAYER_NAMES = "playerNames";
	String LAMPS_TAKEN = "lampsTaken";
	String CHESTS_FOUND = "chestsFound";
	String MISSION_PROGRESS = "missionProgress";
	String MISSION_NAME_LABELS = "missionNameLabels";
	String VIEW_DIRECTIONS = "viewDirection";

	public void addListener(Listener l);

	public Log[] getDisplayedLogs();

	public Minimap getMinimap();

	public boolean isOptionSet(String option);

}
