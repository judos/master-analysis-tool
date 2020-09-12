
package view.statistics;

import javares.files.config.Config;
import javares.files.config.Property;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.DataProvider;


/**
 * @since 05.03.2014
 * @author Julian Schelker
 */
public class StatisticsPanel extends JTabbedPane implements ChangeListener {

	private static final long serialVersionUID = 2628422705129669226L;
	private DataProvider dataprovider;
	private ItemFirstUsePanel itemPanel;
	private EnergyPanel energyPanel;
	private Config config;
	private Property propTab;
	private ProgressPanel progressPanel;
	private CombatPanel combatPanel;

	public StatisticsPanel(Config config, DataProvider dataprovider) {
		super();
		this.config = config;
		this.propTab = config.getPropertyByName("statistics_selected_tab");

		this.dataprovider = dataprovider;

		initAll();
		this.addChangeListener(this);
	}

	private void initAll() {
		int selected = 0;
		if (propTab.isValidInt())
			selected = propTab.getInt();
		this.itemPanel = new ItemFirstUsePanel(this.config, this.dataprovider);
		this.addTab("Events - first activation", itemPanel);
		this.progressPanel = new ProgressPanel(this.config, this.dataprovider);
		this.addTab("Players progress - Completion time", this.progressPanel);
		this.energyPanel = new EnergyPanel(this.config, this.dataprovider);
		this.addTab("Energy - usage and loss", this.energyPanel);
		this.combatPanel = new CombatPanel(this.config, this.dataprovider);
		this.addTab("Combat situations", this.combatPanel.getPanel());
		this.setSelectedIndex(selected);
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		this.propTab.setValue(this.getSelectedIndex());
	}
}
