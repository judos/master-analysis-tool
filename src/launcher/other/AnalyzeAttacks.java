package launcher.other;

import java.io.File;
import java.util.ArrayList;

import javares.files.FileUtils;
import model.Log;
import model.events.FindEvent;
import model.events.NamedDamageEvent;

/**
 * @since 25.03.2014
 * @author Julian Schelker
 */
public class AnalyzeAttacks {

	private Log log;
	private ArrayList<Log> logs;

	public AnalyzeAttacks() {
		analyzeAllLogFiles();

	}

	private void analyzeAllLogFiles() {
		FileUtils.checkOrCreateDir("data/logs");
		File logFiles = new File("data/logs");
		int mistakes = 0;
		int total = 0;
		for (File logFile : logFiles.listFiles()) {
			Log log = new Log(logFile);
			ArrayList<NamedDamageEvent> dmg = log.getEventsDamage();
			total += dmg.size();
			for (int i = 0; i < dmg.size(); i++) {
				NamedDamageEvent x = dmg.get(i);
				if (x.getType() > 2 || x.getType() < 0) {
					System.out.println(log);
					outputMistake(i, dmg, null);
					mistakes++;
				}
			}
		}
		System.out.println("Mistakes: " + mistakes + " / " + total);
	}

	public void analyzeOneLog() {
		this.log = new Log(new File("data/logs/2014-03-31 julian3"));
		// this.log.outputAllLifeLosses();
		int in = 0;
		int mistakes = 0;
		int[] test = getJulian3TestEvents();
		ArrayList<NamedDamageEvent> x = this.log.getEventsDamage();
		for (int i = 0; i < x.size(); i++) {
			if (x.get(i).getType() != test[i] && test[i] != 10 && test[i] != 11) {
				outputMistake(i, x, test);
				mistakes++;
			}
		}
		System.out.println("Mistakes: " + mistakes + " / " + test.length);
	}

	private void outputMistake(int i, ArrayList<NamedDamageEvent> x, int[] test) {
		int min = Math.max(0, i - 2);
		int max = Math.min(x.size() - 1, i + 2);
		for (int index = min; index <= max; index++)
			System.out.println(index + " " + x.get(index));
		if (test != null)
			System.out.println("-> " + i + " should be: "
				+ FindEvent.getEventNameByType(test[i]));
		System.out.println();
	}

	protected int[] getJulian3TestEvents() {
		// 10 means unclear
		// 11 means it should be split
		return new int[] { 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 11, 0, 10, 10, 10,
			10, 10, 1, 0, 1, 1, 1, 0, 1, 1, 11, 0, 1, 1, 0 };
	}

	private int[] getJulian2TestEvents() {
		// 10 means unclear
		// 11 means it should be split
		return new int[] { 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 2, 2, 2, 1, 1, 1,
			1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1 };
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AnalyzeAttacks();
	}

}
