package controller;

import java.util.HashMap;

import model.CombatAnalyzer;
import model.Log;

/**
 * @since 20.05.2014
 * @author Julian Schelker
 */
public class CombatAttackTimeCount {

	private CombatHashData<Float> timePerStunP;
	private CombatHashData<Float> hitRatioP;

	public CombatAttackTimeCount(Log[] logs) {

		this.timePerStunP = new CombatHashData<Float>(logs.length);
		this.hitRatioP = new CombatHashData<Float>(logs.length);

		for (Log l : logs) {
			CombatAnalyzer combat = l.getCombat();

			HashMap<String, Float> stunTime = combat.getStunTime();
			HashMap<String, Integer> stunCount = combat.getStunCount();
			HashMap<String, Integer> attackCount = combat.getAttackCount();

			HashMap<String, Float> timePerStun = new HashMap<>();
			HashMap<String, Float> hitRatio = new HashMap<>();
			for (String key : stunTime.keySet()) {
				if (stunCount.get(key) > 0)
					timePerStun.put(key, stunTime.get(key) / stunCount.get(key));
				else
					timePerStun.put(key, 0f);
				if (attackCount.get(key) > 0)
					hitRatio.put(key, (float) stunCount.get(key) / attackCount.get(key));
				else
					hitRatio.put(key, 0f);
			}
			timePerStunP.addData(timePerStun);
			hitRatioP.addData(hitRatio);
		}
	}

	/**
	 * @return the timePerStunP
	 */
	public CombatHashData<Float> getTimePerStunP() {
		return timePerStunP;
	}

	/**
	 * @return the hitRatioP
	 */
	public CombatHashData<Float> getHitRatioP() {
		return hitRatioP;
	}

}
