package controller;

import java.util.ArrayList;

import javares.data.ArraysJS;
import javares.files.config.Config;
import javares.files.config.Property;

import javax.swing.JList;
import javax.swing.ListModel;

import model.Log;

/**
 * @since 17.02.2014
 * @author Julian Schelker
 */
public class ShowLogProperty {

	private Config config;
	private Property shownData;

	public ShowLogProperty(Config config) {
		this.config = config;
		this.shownData = this.config.getPropertyByName("shownData");
	}

	public int[] getLastSelected(JList<Log> list) {
		ListModel<Log> m = list.getModel();
		ArrayList<Integer> result = new ArrayList<>();
		String[] last = this.shownData.getString().split(";");
		for (int i = 0; i < m.getSize(); i++) {
			Log x = m.getElementAt(i);
			if (ArraysJS.contains(last, x.getName()))
				result.add(i);
		}

		int[] selected = new int[result.size()];
		int arrPos = 0;
		for (Integer i : result)
			selected[arrPos++] = i;
		return selected;
	}

	public void setProperty(Log[] selectedData) {
		StringBuffer p = new StringBuffer();
		for (Log l : selectedData) {
			p.append(l.getName() + ";");
		}
		if (p.length() == 0)
			this.shownData.setValue("");
		else
			this.shownData.setValue(p.substring(0, p.length() - 1));
	}

}
