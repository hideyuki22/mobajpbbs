package model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements Serializable {
	private int id;
	private String loginid;
	private String name;
	private String text;
	private int rank;
	private int roll;
	private String mail;
	private String image;
	private final String defaultImage = "default.jpg";
	private int teamid;
	private String teamName;

	public User(int userid, String name) {
		this.id = userid;
		this.name = name;
	}

	public User(int id, String loginid, String name, String text, int rank, int roll, String mail, String image,
			int teamid, String teamName) {
		this.id = id;
		this.loginid = loginid;
		this.name = name;
		this.text = text;
		this.rank = rank;
		this.roll = roll;
		this.mail = mail;
		this.image = image;
		this.teamid = teamid;
		this.teamName = teamName;
	}

	public User(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.loginid = rs.getString("login_id");
		this.name = rs.getString("name");
		this.text = rs.getString("text");
		this.roll = rs.getInt("roll_id");
		this.rank = rs.getInt("rank_id");
		this.mail = rs.getString("mail");
		this.image = rs.getString("image");
		this.teamid = rs.getInt("team_id");
		this.teamName = rs.getString("team_name");
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	public String getLoginid() {
		return this.loginid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return this.text;
	}

	public String[] getSplitText() {
		return this.text != null ? this.text.split("(\\r\\n|\\r|\\n)") : null;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getImage() {
		return !"".equals(this.image) && this.image != null ? this.image : "default.jpg";
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDefaultImage() {
		return this.defaultImage;
	}

	public int getRank() {
		return this.rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getRoll() {
		return this.roll;
	}

	public void setRoll(int roll) {
		this.roll = roll;
	}

	public int getTeamid() {
		return this.teamid;
	}

	public void setTeamid(int teamid) {
		this.teamid = teamid;
	}

	public String getTeamName() {
		return this.teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
}