
package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javares.files.FileUtils;


/**
 * @since 10.02.2014
 * @author Julian Schelker
 */
public class Data {

    private ArrayList<Log> logs;
    private ArrayList<Minimap> minimaps;

    public Data() {
        initLogs();
        initMinimaps();
    }

    private void initMinimaps() {
        FileUtils.checkOrCreateDir("data/minimaps");
        File logs = new File("data/minimaps");
        this.minimaps = new ArrayList<Minimap>();
        for (File map : logs.listFiles()) {
            if (map.isDirectory()) {
                try {
                    this.minimaps.add(new Minimap(map));
                } catch (IOException e) {
                    // do nothing, minimap can't be loaded
                }
            }
        }
    }

    private void initLogs() {
        FileUtils.checkOrCreateDir("data/logs");
        File logs = new File("data/logs");
        this.logs = new ArrayList<Log>();
        for (File log : logs.listFiles()) {
            this.logs.add(new Log(log));
        }
    }

    public Log[] getLogs() {
        return this.logs.toArray(new Log[] {});
    }

    public Minimap[] getMinimaps() {
        return this.minimaps.toArray(new Minimap[] {});
    }
}
