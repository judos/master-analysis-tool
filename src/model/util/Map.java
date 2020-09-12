package model.util;

import static javares.math.MathJS.listDo;
import static javares.math.MathJS.sum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javares.math.AddOperator;
import javares.math.MathJS;
import javares.math.UnaryOperator;
import model.MeanAndMeanDiff;
import model.StdvMean;

/**
 * @since 09.04.2014
 * @author Julian Schelker
 */
public class Map {
	public static <T> void add(HashMap<T, Integer> map, T key, int amount) {
		if (map.containsKey(key))
			map.put(key, map.get(key) + amount);
		else
			map.put(key, amount);
	}

	public static <T> void add(HashMap<T, Float> map, T key, float amount) {
		if (map.containsKey(key))
			map.put(key, map.get(key) + amount);
		else
			map.put(key, amount);
	}

	public static <T> HashMap<String, ArrayList<T>> listUp(HashMap<String, T>... maps) {
		HashMap<String, ArrayList<T>> result = new HashMap<String, ArrayList<T>>();
		for (HashMap<String, T> map : maps) {
			for (Entry<String, T> e : map.entrySet()) {
				ArrayList<T> list = result.get(e.getKey());
				if (list == null) {
					list = new ArrayList<T>();
					result.put(e.getKey(), list);
				}
				list.add(e.getValue());
			}
		}
		return result;
	}

	public static HashMap<String, StdvMean> calculateStdvMean(
		HashMap<String, ArrayList<Integer>> map) {
		HashMap<String, StdvMean> result = new HashMap<String, StdvMean>();

		for (Entry<String, ArrayList<Integer>> e : map.entrySet()) {
			ArrayList<Integer> values = e.getValue();
			double mean = MathJS.sum(values) / values.size();
			double stdv = MathJS.stdv(values);
			result.put(e.getKey(), new StdvMean(mean, stdv));
		}
		return result;
	}

	public static <T extends Number> HashMap<String, MeanAndMeanDiff>
		calculateMeanMeanDiff(HashMap<String, ArrayList<T>> map) {
		HashMap<String, MeanAndMeanDiff> result = new HashMap<String, MeanAndMeanDiff>();

		for (Entry<String, ArrayList<T>> e : map.entrySet()) {
			ArrayList<T> values = e.getValue();
			double mean = MathJS.sum(values) / values.size();

			AddOperator add = new AddOperator();
			UnaryOperator diffOp = add.bindArgumentB(-mean);
			UnaryOperator absOp = new UnaryOperator() {
				@Override
				public double calculate(double arg0) {
					return Math.abs(arg0);
				}
			};

			double diffAvg = sum(listDo(listDo(values, diffOp), absOp)) / values.size();

			result.put(e.getKey(), new MeanAndMeanDiff(mean, diffAvg));
		}
		return result;
	}
}
