package model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

public class Post implements Serializable {
	private int id;
	private User user;
	private String category;
	private String title;
	private String text;
	private Date date;
	private Time time;

	public Post(int id, User user, String category, String title, String text, Date date, Time time) {
		this.id = id;
		this.user = user;
		this.category = category;
		this.title = title;
		this.text = text;
		this.date = date;
		this.time = time;
	}

	public Post(ResultSet rs) throws SQLException {
		this.id = rs.getInt("post.id");
		this.user = new User(rs);
		this.category = rs.getString("category.name");
		this.title = rs.getString("post.title");
		this.text = rs.getString("post.text");
		this.date = rs.getDate("post.date");
		this.time = rs.getTime("post.time");
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getTime() {
		return this.time;
	}

	public void setTime(Time time) {
		this.time = time;
	}
}