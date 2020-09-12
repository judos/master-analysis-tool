package launcher.other;

import java.awt.Dimension;

import javax.swing.JFrame;

import model.Interval;
import view.figures.DiffMeanBarComparedTitled;

/**
 * @since 18.03.2014
 * @author Julian Schelker
 */
public class TestStdDevMeanBar extends JFrame {
	public TestStdDevMeanBar() {
		Dimension d = new Dimension(150, 400);

		float[] answer = new float[] { 10, 20 };
		// Random r = new Random();
		// for (int i = 0; i < answer.length; i++)
		// answer[i] = (float) r.nextGaussian();

		DiffMeanBarComparedTitled t = new DiffMeanBarComparedTitled("Test", new String[] {
			"time counted", "numbers counted" }, new double[] { 1, 2 }, new double[] {
			0.2, 0.1 });
		Interval i = t.getInterval();
		t.setDisplayInterval(new Interval(i.getMin(), i.getMax() + 1));

		this.add(t.getPanel());
		this.setPreferredSize(d);
		// this.setMinimumSize(d);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	private static final long serialVersionUID = 1573592774303180115L;

	public static void main(String[] args) {
		new TestStdDevMeanBar();
	}
}
