package view.statistics;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javares.control.Listener;
import javares.files.config.Config;

import javax.swing.JPanel;

import model.Log;
import view.figures.StdvMeanFloatDistrTitled;
import controller.DataProvider;

/**
 * @since 02.04.2014
 * @author Julian Schelker
 */
public class EnergyPanel extends JPanel implements Listener {

	protected DataProvider data;
	protected Config config;
	private static final long serialVersionUID = -3224995213891142669L;

	public EnergyPanel(Config config, DataProvider dataprovider) {
		super(true);
		this.config = config;
		this.data = dataprovider;

		initAll();
		this.data.addListener(this);
	}

	private void initAll() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		Log[] logs = this.data.getDisplayedLogs();
		HashMap<String, ArrayList<Integer>> energy = Log
			.getTotalEnergyStatForMultibleLogs(logs);
		for (Entry<String, ArrayList<Integer>> entry : energy.entrySet()) {
			ArrayList<Integer> list = entry.getValue();
			float[] values = new float[list.size()];
			for (int i = 0; i < list.size(); i++)
				values[i] = list.get(i);
			this.add(new StdvMeanFloatDistrTitled(entry.getKey(), values,
				new Color(128, 128, 128)), c);
			c.gridx++;
		}
	}

	@Override
	public void actionPerformed(Object arg0, String arg1, Object arg2) {
		// regenerate content of panel
		this.removeAll();
		initAll();
		this.validate();
		this.repaint();
	}

}
