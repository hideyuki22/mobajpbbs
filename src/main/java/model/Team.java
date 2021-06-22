package model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Team implements Serializable {
	private static String defaultImageUrl;
	private int id;
	private User reader;
	private String name;
	private String image;
	private final String defaultImage =  defaultImageUrl +"/upload/defaultteam.jpg";
	private String text;

	public int getId() {
		return this.id;
	}

	public Team(int id, User reader, String name, String image, String text) {
		this.id = id;
		this.reader = reader;
		this.name = name;
		this.image = image;
		this.text = text;
	}

	public Team(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.reader = new User(rs.getInt("reader_id"), rs.getString("reader_name"));
		this.name = rs.getString("name");
		this.image = rs.getString("image");
		this.text = rs.getString("text");
	}

	public static String getDefaultImageUrl() {
		return defaultImageUrl;
	}

	public static void setDefaultImageUrl(String defaultImageUrl) {
		Team.defaultImageUrl = defaultImageUrl;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getReader() {
		return this.reader;
	}

	public void setReader(User reader) {
		this.reader = reader;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return !"".equals(this.image) && this.image != null ? this.image : this.defaultImage;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDefaultImage() {
		return this.defaultImage;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String[] getSplitText() {
		return this.text != null ? this.text.split("(\\r\\n|\\r|\\n)") : null;
	}
}