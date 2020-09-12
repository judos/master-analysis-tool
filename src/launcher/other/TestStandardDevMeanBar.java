
package launcher.other;

import java.awt.Dimension;
import java.util.Random;

import javax.swing.JFrame;

import view.figures.StdvMeanFloatDistribution;


/**
 * @since 18.03.2014
 * @author Julian Schelker
 */
public class TestStandardDevMeanBar extends JFrame {

	public TestStandardDevMeanBar() {
		Dimension d = new Dimension(150, 200);

		float[] answer = new float[] { 0, };

		StdvMeanFloatDistribution t = new StdvMeanFloatDistribution(answer);

		this.add(t.getPanel());
		this.setPreferredSize(d);
		this.setMinimumSize(d);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	private static final long serialVersionUID = 1573592774303180115L;

	@SuppressWarnings("unused")
	private float[] getRandomDistr() {
		float[] answer = new float[1000];
		Random r = new Random();
		for (int i = 0; i < answer.length; i++)
			answer[i] = (float) r.nextGaussian();
		return answer;
	}

	public static void main(String[] args) {
		new TestStandardDevMeanBar();
	}
}
