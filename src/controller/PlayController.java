
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JTextField;


/**
 * @since 04.06.2014
 * @author Julian Schelker
 */
public class PlayController implements ActionListener {

    private JButton play;
    private JScrollBar bar;
    private boolean running;
    private Thread runThread;
    private JTextField speed;
    public static final String[] text = { "play", "stop" };

    public PlayController(JButton play, JScrollBar bar, JTextField speed) {
        this.play = play;
        this.bar = bar;
        this.speed = speed;
        this.running = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.running = !this.running;
        updateStatus();
    }

    private void updateStatus() {
        if (this.running)
            run();
        else
            stop();
        this.play.setText(text[this.running ? 1 : 0]);
    }

    private void stop() {
        this.runThread = null;
    }

    private void run() {
        this.runThread = new Thread() {

            @Override
            public void run() {
                PlayController that = PlayController.this;
                while (that.running) {
                    System.out.println(that.bar.getVisibleAmount() + " / "
                        + that.bar.getMaximum());
                    if (that.bar.getValue() >= that.bar.getMaximum()
                        - that.bar.getVisibleAmount())
                        break;
                    that.bar.setValue(that.bar.getValue() + 1);
                    try {
                        Thread.sleep(that.getSpeed());
                    } catch (InterruptedException e) {}
                }
                that.running = false;
                that.updateStatus();
            }
        };
        this.runThread.start();
    }

    protected long getSpeed() {
        int sp = 0;
        try {
            sp = Integer.valueOf(this.speed.getText());
        } catch (Exception e) {
            sp = 50;
        }
        if (sp < 5)
            sp = 5;
        return sp;
    }

}
