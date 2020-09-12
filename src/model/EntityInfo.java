package model;

/**
 * @since 16.02.2014
 * @author Julian Schelker
 */
public class EntityInfo {

	private Vec3 pos;
	private Vec3 forward;
	private int id;

	private int target;

	private float energy;
	private boolean isPlayer;
	private String name;

	public EntityInfo(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}

	/**
	 * @return the energy
	 */
	public float getEnergy() {
		return energy;
	}

	/**
	 * @return the forward
	 */
	public Vec3 getForward() {
		return forward;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public Vec3 getPos() {
		return this.pos;
	}

	/**
	 * @return the target
	 */
	public int getTarget() {
		return target;
	}

	/**
	 * @return the isPlayer
	 */
	public boolean isPlayer() {
		return isPlayer;
	}

	public void setEnergy(float energy) {
		this.energy = energy;
	}

	public void setForward(Vec3 vec3) {
		this.forward = vec3;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param pos
	 *            the pos to set
	 */
	public void setPos(Vec3 pos) {
		this.pos = pos;
	}

	public void setTarget(int id) {
		this.target = id;
	}

	@Override
	public String toString() {
		if (this.isPlayer)
			return "<player>name=" + name + ";pos=" + this.pos + ";fwdDir="
				+ this.forward + ";energy=" + this.energy + ";id=" + this.id
				+ "</player>";
		else
			return "<enemy>name=" + name + ";pos=" + this.pos + ";fwdDir=" + this.forward
				+ ";id=" + this.id + ";attTarget=" + this.target + "</enemy>";
	}
}
