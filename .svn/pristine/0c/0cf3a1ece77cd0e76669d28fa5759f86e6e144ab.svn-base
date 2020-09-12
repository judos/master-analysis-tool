
package view.statistics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javares.control.Listener;
import javares.files.config.Config;

import javax.swing.JPanel;

import model.Log;
import model.game.EmissaireObjects;
import controller.DataProvider;


/**
 * @since 05.03.2014
 * @author Julian Schelker
 */
public class ItemFirstUsePanel extends JPanel implements Listener {

	private DataProvider dataprovider;
	private Config config;

	public ItemFirstUsePanel(Config config, DataProvider dataprovider) {
		super(true);
		this.config = config;
		this.dataprovider = dataprovider;
		initAll();

		this.dataprovider.addListener(this);
	}

	// occurs when displayed data is changed
	@Override
	public void actionPerformed(Object arg0, String arg1, Object arg2) {
		this.removeAll();
		initAll();
		this.validate();
		this.repaint();
	}

	private void initAll() {
		Log[] logs = this.dataprovider.getDisplayedLogs();
		initItemsData(logs);
	}

	private void initItemsData(Log[] logs) {
		HashMap<String, Integer> itemsUsed = new HashMap<String, Integer>();
		int logAmount = logs.length;
		for (Log curLog : logs) {
			Iterable<String> items = curLog.getUsedItemsAndEvents();
			for (String item : items) {
				if (itemsUsed.containsKey(item))
					itemsUsed.put(item, itemsUsed.get(item) + 1);
				else
					itemsUsed.put(item, 1);
			}
		}
		// List<String> itemsSorted = new ArrayList<String>(itemsUsed.keySet());
		// Collections.sort(itemsSorted);
		String[] itemsSorted = EmissaireObjects.getEventOrder();

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		for (String item : itemsSorted) {
			if (!itemsUsed.containsKey(item))
				itemsUsed.put(item, 0);
			int used = itemsUsed.get(item);
			this.add(new ItemGraphPanel(item, used, logAmount), c);
			c.gridx++;
			if (c.gridx > 7) {
				c.gridx = 0;
				c.gridy++;
			}

		}
	}

	private static final long serialVersionUID = 4127624670821246999L;

}
