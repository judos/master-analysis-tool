
package view.statistics;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javares.control.Listener;
import javares.data.StringJS;
import javares.files.config.Config;

import javax.swing.JPanel;

import model.BoundingBoxIntervalMerger;
import model.CombatAnalyzer;
import model.Interval;
import model.Log;
import model.MeanAndMeanDiff;
import model.util.Map;
import view.figures.DiffMeanBarComparedTitled;
import controller.CombatAttackTimeCount;
import controller.CombatHashData;
import controller.DataProvider;


/**
 * @since 19.05.2014
 * @author Julian Schelker
 */
public class CombatPanel implements Listener {

	private Config config;
	private DataProvider data;
	private DiffMeanBarComparedTitled[] content;
	private JPanel panel;

	public CombatPanel(Config config, DataProvider dataprovider) {
		this.panel = new JPanel(true);
		this.config = config;
		this.data = dataprovider;

		initAll();
		this.data.addListener(this);
	}

	/**
	 * @return the panel
	 */
	public JPanel getPanel() {
		return panel;
	}

	@Override
	public void actionPerformed(Object arg0, String arg1, Object arg2) {
		// regenerate content of panel
		this.panel.removeAll();
		initAll();
		this.panel.validate();
		this.panel.repaint();
	}

	private void initAll() {
		this.panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		Log[] logs = this.data.getDisplayedLogs();

		addDamagePanels(c, logs);
		c.gridx = 0;
		c.gridy++;
		addAttackPanels(c, logs);

	}

	private void addAttackPanels(GridBagConstraints c, Log[] logs) {

		CombatAttackTimeCount combatAttack = new CombatAttackTimeCount(logs);
		CombatHashData<Float> hitRatio = combatAttack.getHitRatioP();
		CombatHashData<Float> stunTime = combatAttack.getTimePerStunP();

		String[] subs = new String[] { "stun time / hit count", "hit ratio" };

		BoundingBoxIntervalMerger stunMerge = new BoundingBoxIntervalMerger();
		BoundingBoxIntervalMerger hitMerger = new BoundingBoxIntervalMerger();

		ArrayList<String> keys = stunTime.getSortedKeys();

		this.content = new DiffMeanBarComparedTitled[keys.size()];
		for (int i = 0; i < keys.size(); i++) {
			String enemy = keys.get(i);
			String enemyName = "Shadow " + StringJS.substr(keys.get(i), -1);
			MeanAndMeanDiff std0 = stunTime.getMMD(enemy);
			MeanAndMeanDiff std1 = hitRatio.getMMD(enemy);
			double[] mean = new double[] { std0.getMean(), std1.getMean() };
			double[] avgDiff = new double[] { std0.getAvgDiff(), std1.getAvgDiff() };
			this.content[i] = new DiffMeanBarComparedTitled(enemyName, subs, mean,
				avgDiff);

			this.content[i].colors = new Color[] { new Color(148, 148, 100),
				new Color(100, 100, 128) };
			this.content[i].initAll();

			stunMerge.addData(this.content[i].getLeftInterval());
			hitMerger.addData(this.content[i].getRightInterval());
		}
		hitMerger.addData(new Interval(0, 4));

		for (int i = 0; i < keys.size(); i++) {
			this.content[i].setDisplayIntervalLeft(stunMerge.getInterval());
			this.content[i].setDisplayIntervalRight(hitMerger.getInterval());
			this.panel.add(this.content[i].getPanel(), c);
			c.gridx++;
		}

	}

	@SuppressWarnings("unchecked")
	private void addDamagePanels(GridBagConstraints c, Log[] logs) {
		HashMap<String, Integer>[] dmgFrom = new HashMap[logs.length];
		HashMap<String, Integer>[] dmgTo = new HashMap[logs.length];
		for (int i = 0; i < logs.length; i++) {
			Log l = logs[i];
			CombatAnalyzer combat = l.getCombat();
			dmgFrom[i] = combat.getDamageTakenFrom();
			dmgTo[i] = combat.getDamageDoneTo();
		}
		HashMap<String, ArrayList<Integer>> dmgFromListed = Map.listUp(dmgFrom);
		HashMap<String, ArrayList<Integer>> dmgToListed = Map.listUp(dmgTo);

		// math
		HashMap<String, MeanAndMeanDiff> dmgFromMath = Map
			.calculateMeanMeanDiff(dmgFromListed);
		HashMap<String, MeanAndMeanDiff> dmgToMath = Map
			.calculateMeanMeanDiff(dmgToListed);

		Set<String> entities = dmgFromMath.keySet();
		ArrayList<String> entitiesSorted = new ArrayList<String>(entities);
		Collections.sort(entitiesSorted);
		String[] subsDmg = new String[] { "damage taken from", "damage done to" };

		BoundingBoxIntervalMerger merge = new BoundingBoxIntervalMerger();
		this.content = new DiffMeanBarComparedTitled[entities.size()];
		for (int i = 0; i < entitiesSorted.size(); i++) {
			String enemy = entitiesSorted.get(i);
			String enemyName = "Shadow " + StringJS.substr(entitiesSorted.get(i), -1);
			MeanAndMeanDiff std0 = dmgFromMath.get(enemy);
			MeanAndMeanDiff std1 = dmgToMath.get(enemy);
			double[] mean = new double[] { std0.getMean(), std1.getMean() };
			double[] avgDiff = new double[] { std0.getAvgDiff(), std1.getAvgDiff() };
			this.content[i] = new DiffMeanBarComparedTitled(enemyName, subsDmg, mean,
				avgDiff);
			merge.addData(this.content[i]);
		}
		for (int i = 0; i < entitiesSorted.size(); i++) {
			this.content[i].setDisplayInterval(merge.getInterval());
			this.panel.add(this.content[i].getPanel(), c);
			c.gridx++;
		}
	}

}
