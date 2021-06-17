package model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Comment implements Serializable {
	private int id;
	private User user;
	private String text;
	private Date date;
	private Time time;
	private ArrayList<Integer> childList = new ArrayList<>();
	private boolean flag = true;
	private int level = 0;

	public Comment(int id, User user, String text, Date date, Time time) {
		this.id = id;
		this.user = user;
		this.text = text;
		this.date = date;
		this.time = time;
	}

	public Comment(ResultSet rs) throws SQLException {
		this.id = rs.getInt("comment_id");
		this.user = new User(rs);
		this.text = rs.getString("comment_text");
		this.date = rs.getDate("comment_date");
		this.time = rs.getTime("comment_time");
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getText() {
		return this.text;
	}

	public String[] getSplitText() {
		if(this.text != null) {
			return this.text.split("(\\r\\n|\\r|\\n)");
		}
		return null;
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

	public ArrayList<Integer> getChildList() {
		return this.childList;
	}

	public void setChildList(ArrayList<Integer> childList) {
		this.childList = childList;
	}

	public boolean isFlag() {
		return this.flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public static ArrayList<Comment> CreateTreeList(ArrayList<Comment> cmList) {
		Map<Integer, Comment> commentMap = new HashMap<>();
		Iterator<Comment> itr = cmList.iterator();

		while (itr.hasNext()) {
			Comment cm = itr.next();
			//>>������T��
			String regex = "(>>\\d+)";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(cm.getText());

			while (m.find()) {
				int postid = Integer.parseInt(m.group().substring(2));
				if (commentMap.containsKey(postid)) {
					((Comment) commentMap.get(postid)).getChildList().add(cm.getId());
				}
			}

			commentMap.put(cm.getId(), cm);
		}

		Iterator<Integer> keyItr = commentMap.keySet().iterator();
		return NextComment(commentMap, keyItr, 0);
	}

	private static ArrayList<Comment> NextComment(Map<Integer, Comment> commentMap,
			Iterator<Integer> keyItr,int level) {
		//�ő傶��Ȃ������烌�x����������
		if (level < 10) {
			++level;
		}

		ArrayList<Comment> commentList = new ArrayList<>();

		while (keyItr.hasNext()) {
			int key = (Integer) keyItr.next();
			if (((Comment) commentMap.get(key)).isFlag()) {
				Comment comment = (Comment) commentMap.get(key);
				comment.setLevel(level);
				commentList.add(comment);
				((Comment) commentMap.get(key)).setFlag(false);
				//�ԐM���X�g���Ȃ����m�F
				if (!((Comment) commentMap.get(key)).getChildList().isEmpty()) {
					ArrayList<Integer> childList = ((Comment) commentMap.get(key)).getChildList();
					Iterator<Integer> childKeyItr = childList.iterator();
					commentList.addAll(NextComment(commentMap, childKeyItr, level));
				}
			}
		}

		return commentList;
	}
}