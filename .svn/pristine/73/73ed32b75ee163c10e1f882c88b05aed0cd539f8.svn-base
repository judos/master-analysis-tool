package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javares.files.OpenFileDialog;

import javax.swing.JFileChooser;

import model.Log;

/**
 * @since 25.03.2014
 * @author Julian Schelker
 */
public class StichLogs {

	public StichLogs() {
		try {
			promptDialogs();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void promptDialogs() throws IOException {
		OpenFileDialog d = new OpenFileDialog(null, "Choose the first log",
			new File("."), new String[] { "xml" });
		if (d.isCancelled())
			return;
		OpenFileDialog d2 = new OpenFileDialog(null, "Choose the second log", null,
			new String[] { "xml" });
		if (d2.isCancelled())
			return;

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setSelectedFile(new File(d2.getSelectedFile(), "-merged"));
		int returnValue = fileChooser.showSaveDialog(null);
		if (returnValue != JFileChooser.APPROVE_OPTION)
			return;

		stitch(d.getSelectedFile(), d2.getSelectedFile(), fileChooser.getSelectedFile());
	}

	private void stitch(File file1, File file2, File result) throws IOException {
		Log l1 = new Log(file1);
		Log l2 = new Log(file2);

		System.out.println(l1.getEndTime());

		if (!result.exists())
			result.mkdirs();
		File pos = new File(result, "logging1.xml");
		File events = new File(result, "logging2.xml");
		BufferedWriter outPos = new BufferedWriter(new FileWriter(pos));
		BufferedWriter outEvents = new BufferedWriter(new FileWriter(events));
		l1.outputStart(outPos, outEvents);

		l1.outputPositions(0, outPos);
		l1.outputEvents(0, outEvents);

		l2.outputPositions(l1.getEndTime() + stitchDifference, outPos);
		l2.outputEvents(l1.getEndTime() + stitchDifference, outEvents);

		l1.outputEnd(outPos, outEvents);
		outPos.close();
		outEvents.close();
	}

	public static int stitchDifference = 2;

}
