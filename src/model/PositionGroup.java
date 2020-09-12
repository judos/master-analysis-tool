package model;

import java.util.HashMap;

/**
 * @since 16.02.2014
 * @author Julian Schelker
 */
public class PositionGroup {

	private double time;
	private HashMap<String, EntityInfo> entities;

	public PositionGroup(double time) {
		this.time = time;
		this.entities = new HashMap<String, EntityInfo>();
	}

	public void addInfo(String entityName, EntityInfo info) {
		this.entities.put(entityName, info);
	}

	public EntityInfo getEntityInfo(String entityName) {
		return this.entities.get(entityName);
	}

	public String[] getEntityNames() {
		return this.entities.keySet().toArray(new String[] {});
	}

	/**
	 * @return the time
	 */
	public double getTime() {
		return time;
	}
}
