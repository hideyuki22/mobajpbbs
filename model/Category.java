package model;

import java.io.Serializable;

public class Category implements Serializable {
	private int id;
	private String name;
	private boolean teamFlag;

	public Category(int id, String name, boolean teamFlag) {
		this.id = id;
		this.name = name;
		this.teamFlag = teamFlag;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isTeamFlag() {
		return this.teamFlag;
	}

	public void setTeamFlag(boolean teamFlag) {
		this.teamFlag = teamFlag;
	}
}