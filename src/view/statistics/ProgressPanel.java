
package view.statistics;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javares.control.Listener;
import javares.files.config.Config;

import javax.swing.JPanel;

import model.Log;
import model.util.Map;
import view.figures.StdvMeanFloatDistrTitled;
import controller.DataProvider;


/**
 * @since 17.04.2014
 * @author Julian Schelker
 */
public class ProgressPanel extends JPanel implements Listener {

    private static final long serialVersionUID = 5886940464545768865L;
    protected Config config;
    protected DataProvider data;

    public ProgressPanel(Config config, DataProvider dataprovider) {
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
        @SuppressWarnings("unchecked")
        HashMap<String, Double>[] maps = new HashMap[logs.length];
        int index = 0;
        for (Log l : logs)
            maps[index++] = l.getProgressTimes();
        HashMap<String, ArrayList<Double>> progressData = Map.listUp(maps);

        // Set<String> keys = progressData.keySet();
        // ArrayList<String> keysSorted = new ArrayList<String>(keys);
        // Collections.sort(keysSorted);

        ArrayList<String> keysSorted2 = Log.progressTimeGoals;

        for (String key : keysSorted2) {
            ArrayList<Double> list = progressData.get(key);
            if (list == null)
                list = new ArrayList<>();
            float[] values = new float[list.size()];
            for (int i = 0; i < list.size(); i++)
                values[i] = list.get(i).floatValue();
            StdvMeanFloatDistrTitled x = new StdvMeanFloatDistrTitled(key, values,
                new Color(128, 128, 128));
            x.addSubTitle("completed: " + list.size() + " / " + logs.length);
            this.add(x, c);
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
