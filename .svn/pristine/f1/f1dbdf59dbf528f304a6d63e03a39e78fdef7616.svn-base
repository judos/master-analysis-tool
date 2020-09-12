package view.formDialog;

import java.util.ArrayList;

import javares.control.Listener;

import javax.swing.JPanel;

/**
 * @since 17.03.2014
 * @author Julian Schelker
 */
public abstract class AnswerPanel {
	protected ArrayList<Listener> listeners;

	public AnswerPanel() {
		this.listeners = new ArrayList<Listener>();
	}

	public void addListener(Listener l) {
		this.listeners.add(l);
	}

	public abstract JPanel getJPanel();

	public abstract String getText();

}
