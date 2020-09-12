
package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javares.control.Listener;
import javares.files.config.Config;
import javares.files.config.Property;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Data;
import model.Log;
import model.Minimap;
import view.form.FormPanel;
import view.map.MapPanel;
import view.statistics.StatisticsPanel;
import controller.DataProvider;
import controller.Main1Interface;
import controller.ShowLogProperty;
import controller.StichLogs;


/**
 * @since 18.11.2013
 * @author Julian Schelker
 */
public class MenuFrame extends JFrame implements DataProvider {

    private Main1Interface controller;

    private Data data;

    private Config config;

    private JComboBox<Minimap> minimaps;
    private int selectedMinimapIndex;
    private DataProvider dataprovider;
    protected Minimap selectedMinimap;
    private ArrayList<Listener> listener;
    protected Log[] selectedData;
    protected HashMap<String, Boolean> options;

    private JPanel optionsPanel;

    private StatisticsPanel statistics;

    private MapPanel mapPanel;

    private FormPanel formPanel;

    private JButton selectAll;

    private JList<Log> listedData;

    public MenuFrame(Data d, Config config) {
        super("Menu");
        this.data = d;
        this.selectedData = new Log[0];
        this.config = config;
        this.listener = new ArrayList<Listener>();
        this.options = new HashMap<String, Boolean>();
    }

    @Override
    public void addListener(Listener l) {
        this.listener.add(l);
    }

    private void addOptionTo(JPanel panel, String text, final String codeText,
        GridBagConstraints c) {
        final JCheckBox box1 = new JCheckBox(text);
        final Property property = this.config.getPropertyByName(codeText);
        box1.setSelected(property.getBoolean());
        box1.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent arg0) {
                property.setValue(box1.isSelected());
                options.put(codeText, box1.isSelected());
                notifyListener();
            }
        });

        options.put(codeText, property.getBoolean());
        panel.add(box1, c);
        c.gridy++;
    }

    private void configLoad() {
        final Property posX = this.config.getPropertyByName("winPosX");
        final Property posY = this.config.getPropertyByName("winPosY");
        if (posX.isValidInt() && posY.isValidInt())
            this.setLocation(posX.getInt(), posY.getInt());
        Property sizeX = this.config.getPropertyByName("winSizeWidth");
        Property sizeY = this.config.getPropertyByName("winSizeHeight");
        if (sizeX.isValidInt() && sizeY.isValidInt())
            this.setSize(sizeX.getInt(), sizeY.getInt());

        Property max = this.config.getPropertyByName("winMaximized");
        if (max.isValidInt())
            this.setExtendedState(max.getInt());
    }

    protected void configSave() {
        this.mapPanel.configSave(config);

        Property posX = this.config.getPropertyByName("winPosX");
        Property posY = this.config.getPropertyByName("winPosY");
        Point loc = this.getLocation();
        posX.setValue(loc.x);
        posY.setValue(loc.y);

        Property sizeX = this.config.getPropertyByName("winSizeWidth");
        Property sizeY = this.config.getPropertyByName("winSizeHeight");
        Dimension size = this.getSize();
        sizeX.setValue(size.width);
        sizeY.setValue(size.height);

        Property max = this.config.getPropertyByName("winMaximized");
        max.setValue(this.getExtendedState());
    }

    private ListSelectionListener createDisplayLogListener(final JList<Log> list,
        final ShowLogProperty shownData) {
        return new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                int[] r = list.getSelectedIndices();
                selectedData = new Log[r.length];
                for (int i = 0; i < r.length; i++)
                    selectedData[i] = list.getModel().getElementAt(r[i]);
                shownData.setProperty(selectedData);
                notifyListener();
            }
        };
    }

    private Component createMapOptionsPanel() {
        JPanel mapOptions = new JPanel();
        mapOptions.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        mapOptions.add(new JLabel("Display options:"), c);
        c.gridy++;
        addOptionTo(mapOptions, "Player position in time", DataProvider.PLAYER_POS, c);
        addOptionTo(mapOptions, "Enemy positions in time", DataProvider.ENEMY_POS, c);
        addOptionTo(mapOptions, "View directions", DataProvider.VIEW_DIRECTIONS, c);
        addOptionTo(mapOptions, "Named labels", DataProvider.PLAYER_NAMES, c);
        addOptionTo(mapOptions, "Death points", DataProvider.DEATH_POINTS, c);
        addOptionTo(mapOptions, "Lamps picked", DataProvider.LAMPS_TAKEN, c);
        addOptionTo(mapOptions, "Hidden chests found", DataProvider.CHESTS_FOUND, c);
        addOptionTo(mapOptions, "Mission progress", DataProvider.MISSION_PROGRESS, c);
        addOptionTo(mapOptions, "Named mission labels", DataProvider.MISSION_NAME_LABELS,
            c);

        return mapOptions;
    }

    private ActionListener createMiniMapListener(final Property property) {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                selectedMinimap = (Minimap) minimaps.getSelectedItem();
                property.setValue(selectedMinimap.getName());
                selectedMinimap.waitUntilImageLoaded();
                notifyListener();
            }
        };
    }

    private Component createOptionsPanel() {
        this.optionsPanel = new JPanel();
        optionsPanel.setLayout(new CardLayout());
        optionsPanel.add(createMapOptionsPanel(), CARD_MAP_OPTIONS);
        optionsPanel.add(createStatisticOptionsPanel(), CARD_STAT_OPTIONS);
        return optionsPanel;
    }

    private Component createStatisticOptionsPanel() {
        JPanel statOptions = new JPanel();
        // statOptions.setLayout(new GridBagLayout());
        // GridBagConstraints c = new GridBagConstraints();
        // c.gridx = 0;
        // c.gridy = 0;
        // c.weightx = 1;
        // c.weighty = 1;
        // c.anchor = GridBagConstraints.WEST;
        // c.fill = GridBagConstraints.HORIZONTAL;

        return statOptions;
    }

    @Override
    public Log[] getDisplayedLogs() {
        return this.selectedData;
    }

    @Override
    public Minimap getMinimap() {
        return this.selectedMinimap;
    }

    private void init() {

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.ipadx = 1;
        c.ipady = 1;
        c.anchor = GridBagConstraints.NORTHWEST;

        c.fill = GridBagConstraints.BOTH;

        JLabel label = new JLabel("Minimap:");
        label.setFont(FontObjects.MEDIUM_BOLD);
        this.add(label, c);
        c.gridy++;

        final Property property = this.config.getPropertyByName("minimap");
        Minimap[] maps = this.data.getMinimaps();
        this.minimaps = new JComboBox<Minimap>(maps);
        minimaps.addActionListener(createMiniMapListener(property));
        for (int i = 0; i < maps.length; i++)
            if (maps[i].getName().equals(property.getString()))
                this.selectedMinimapIndex = i;

        this.add(minimaps, c);
        c.gridy++;

        label = new JLabel("Available data:");
        label.setFont(FontObjects.MEDIUM_BOLD);
        this.add(label, c);
        c.gridy++;

        this.selectAll = new JButton("Select all");
        this.selectAll.addActionListener(createSelectAllListener());
        this.add(this.selectAll, c);
        c.gridy++;

        c.weighty = 1;

        ShowLogProperty shownData = new ShowLogProperty(this.config);
        this.listedData = new JList<Log>(this.data.getLogs());
        listedData.setBorder(new LineBorder(Color.lightGray, 1));
        listedData.addListSelectionListener(createDisplayLogListener(listedData,
            shownData));
        listedData.setSelectedIndices(shownData.getLastSelected(listedData));
        JScrollPane scroll = new JScrollPane(listedData);
        scroll.setPreferredSize(new Dimension(100, 200));
        scroll.setMinimumSize(new Dimension(100, 200));
        this.add(scroll, c);
        c.gridy++;

        c.weighty = 0;
        this.add(createOptionsPanel(), c);

        c.gridx = 1;
        c.gridheight = c.gridy + 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;

        final JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        final Property activTab = this.config.getPropertyByName("active_tab");
        activTab.disableChanges();
        tabs.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent arg0) {
                int i = tabs.getSelectedIndex();
                activTab.setValue(i);
                CardLayout l = (CardLayout) optionsPanel.getLayout();
                if (i == 0)
                    l.show(optionsPanel, CARD_MAP_OPTIONS);
                else
                    l.show(optionsPanel, CARD_STAT_OPTIONS);
            }
        });
        tabs.setFont(new Font("Arial", 0, 24));

        this.mapPanel = new MapPanel(this.config, this.dataprovider);
        tabs.add("Map", this.mapPanel);

        this.statistics = new StatisticsPanel(this.config, this.dataprovider);
        tabs.add("Statistics", this.statistics);

        this.formPanel = new FormPanel(this.config, this.dataprovider);
        tabs.add("Form data", this.formPanel);
        if (activTab.isValidInt()) {
            int i = activTab.getInt();
            tabs.setSelectedIndex(i);
        }
        activTab.enableChanges();

        this.add(tabs, c);
    }

    private ActionListener createSelectAllListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MenuFrame that = MenuFrame.this;
                that.listedData.setSelectionInterval(0, that.listedData.getModel()
                    .getSize() - 1);
            }

        };
    }

    public void initAll(DataProvider d, Main1Interface controller) {
        this.controller = controller;
        this.dataprovider = d;

        initMenu();
        init();
        initWindowListener();
        this.pack();
        configLoad();
        this.setTitle("Analysis");
        this.setVisible(true);

        minimaps.setSelectedIndex(this.selectedMinimapIndex);
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Import
        JMenu menu = new JMenu("Import");
        menu.setMnemonic(KeyEvent.VK_I);
        menuBar.add(menu);

        // Minimap
        JMenuItem menuItem = new JMenuItem("Minimap", KeyEvent.VK_M);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
            ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
            "Import a minimap from CryEngine");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                controller.importMinimap();
            }
        });
        menu.add(menuItem);

        // Evaluation
        menuItem = new JMenuItem("Questionaire", KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
            ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
            "Import evaluation for a log");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                controller.importQuestions();
            }
        });
        menu.add(menuItem);

        // Edit
        JMenu edit = new JMenu("Edit");
        edit.setMnemonic(KeyEvent.VK_E);
        menuBar.add(edit);

        // Stitch Logs
        menuItem = new JMenuItem("Stitch Logs", KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
            ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
            "Merge two consequent logs");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                new StichLogs();
            }
        });
        edit.add(menuItem);

        this.setJMenuBar(menuBar);
    }

    private void initWindowListener() {

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                configSave();
                controller.closeMenu();
            }
        });
    }

    @Override
    public boolean isOptionSet(String option) {
        return this.options.get(option);
    }

    protected void notifyListener() {
        for (Listener l : this.listener) {
            l.actionPerformed((DataProvider) this, "data update", null);
        }
    }

    public static final String OPTIONS_ITEM_USED = "items used";

    private static final String CARD_MAP_OPTIONS = "mapOptions";

    private static final String CARD_STAT_OPTIONS = "statisticsOptions";

    private static final long serialVersionUID = 1147445567287830363L;

}
