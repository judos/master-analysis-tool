
package view.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javares.files.config.Config;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import controller.DataProvider;
import controller.PlayController;
import controller.TimeControlProvider;


/**
 * @since 17.03.2014
 * @author Julian Schelker
 */
public class MapPanel extends JPanel implements TimeControlProvider {

    private GridBagConstraints c2;
    private Config config;
    private DataProvider dataprovider;
    private MapGraph drawingPanel;

    protected Show show;
    protected double showMomentTime;
    private JRadioButton showAll;
    private JRadioButton showMoment;
    private JRadioButton showProgress;
    private JScrollBar timeBar;
    private JButton playButton;

    public static final int timeBarResolution = 5000;

    public MapPanel(Config config, DataProvider dataprovider) {
        this.config = config;
        this.dataprovider = dataprovider;
        this.show = Show.ALL;
        this.showMomentTime = 0;

        initLayout();
        initMapGraph();
        initTimeLapse();
    }

    public void configSave(Config config) {
        this.drawingPanel.configSave(config);
    }

    /**
     * @return the showMomentTime
     */
    @Override
    public double getShowPercentage() {
        if (show == Show.ALL)
            return 1;
        return showMomentTime;
    }

    private void initLayout() {
        this.setBorder(new LineBorder(Color.lightGray, 1));
        this.setLayout(new GridBagLayout());
        this.c2 = new GridBagConstraints();
    }

    private void initMapGraph() {
        c2.gridx = 0;
        c2.gridy = 0;
        c2.weightx = 1;
        c2.weighty = 1;
        c2.fill = GridBagConstraints.BOTH;
        c2.gridwidth = 4;
        this.drawingPanel = new MapGraph(this.config, this.dataprovider,
            (TimeControlProvider) this);
        this.add(this.drawingPanel, c2);
    }

    private void initTimeLapse() {
        c2.gridy = 1;
        c2.gridwidth = 1;
        c2.weightx = 0;
        c2.weighty = 0;
        c2.fill = GridBagConstraints.NONE;

        this.add(new JLabel("Timelapse:"), c2);

        c2.gridx++;
        createShowAllButton();

        c2.gridx++;;
        createShowMomentButton();

        c2.gridx++;
        createTimeBar();

        c2.gridx = 1;
        c2.gridy++;
        createProgressButton();

        c2.gridx++;
        createPlayButton();

        ButtonGroup timeSelected = new ButtonGroup();
        timeSelected.add(showAll);
        timeSelected.add(showMoment);
        timeSelected.add(showProgress);
    }

    private void createPlayButton() {
        this.playButton = new JButton("play");
        JTextField speed = new JTextField("50");
        speed.setPreferredSize(new Dimension(150, 20));
        speed.setMinimumSize(new Dimension(150, 20));
        this.playButton.addActionListener(new PlayController(this.playButton,
            this.timeBar, speed));
        this.add(this.playButton, c2);

        c2.gridx++;
        c2.anchor = GridBagConstraints.WEST;
        this.add(speed, c2);
    }

    private void createProgressButton() {
        this.showProgress = new JRadioButton("show progress");
        showProgress.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                show = Show.PROGRESS;
                notifyListener();
            }
        });

        this.add(showProgress, c2);
    }

    private void createTimeBar() {
        c2.fill = GridBagConstraints.HORIZONTAL;
        this.timeBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, timeBarResolution / 20,
            0, timeBarResolution + timeBarResolution / 20);
        timeBar.addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent arg0) {
                showMomentTime = (double) (arg0.getValue()) / timeBarResolution;
                if (show == Show.ALL) {
                    show = Show.MOMENT;
                    showMoment.setSelected(true);
                }
                notifyListener();
            }
        });
        this.add(timeBar, c2);
        c2.fill = GridBagConstraints.NONE;
    }

    private void createShowMomentButton() {
        this.showMoment = new JRadioButton("show moment");
        showMoment.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                show = Show.MOMENT;
                notifyListener();
            }
        });

        this.add(showMoment, c2);

    }

    private void createShowAllButton() {
        this.showAll = new JRadioButton("show all");
        showAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                show = Show.ALL;
                notifyListener();
            }
        });
        showAll.setSelected(true);
        this.add(showAll, c2);
    }

    /**
     * @return the showMoment
     */
    @Override
    public boolean isShowSomeMomentInTime() {
        return show == Show.MOMENT || show == Show.PROGRESS;
    }

    protected void notifyListener() {
        this.drawingPanel.actionPerformed(this, "time lapse changed", null);
    }

    private static final long serialVersionUID = 2041698696885708569L;

    @Override
    public Show getShow() {
        return this.show;
    }

}
