
package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import model.MeanAndMeanDiff;
import model.util.Map;


/**
 * @since 20.05.2014
 * @author Julian Schelker
 */
public class CombatHashData<T extends Number> {

    private HashMap<String, T>[] hash;
    private int index;
    private HashMap<String, MeanAndMeanDiff> math;

    @SuppressWarnings("unchecked")
    public CombatHashData(int size) {
        this.hash = new HashMap[size];
        this.index = 0;
        if (size == 0)
            doStuff();
    }

    public void addData(HashMap<String, T> hash) {
        this.hash[index] = hash;
        index++;
        if (index == this.hash.length)
            doStuff();
    }

    public void doStuff() {
        HashMap<String, ArrayList<T>> listed = Map.listUp(hash);
        this.math = Map.calculateMeanMeanDiff(listed);
    }

    public ArrayList<String> getSortedKeys() {
        ArrayList<String> list = new ArrayList<String>(this.math.keySet());
        Collections.sort(list);
        return list;
    }

    public MeanAndMeanDiff getMMD(String key) {
        return this.math.get(key);
    }
}
