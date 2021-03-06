package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import core.DAOBase;
import model.Post;

public class PostDAO extends DAOBase {
	String SelectColumn = " ACCOUNT.*,POST.ID AS POST_ID,CATEGORY.NAME AS CATEGORY_NAME,"
					+ "POST.TITLE AS POST_TITLE,POST.TEXT AS POST_TEXT,"
					+ "POST.DATE AS POST_DATE,POST.TIME AS POST_TIME,"
					+ "TEAM.NAME AS TEAM_NAME ";
	public Post getPost(int postid) {
		//sql
		String sql = "SELECT " + SelectColumn + " FROM POST,CATEGORY,ACCOUNT "
				+ "LEFT JOIN TEAM ON ACCOUNT.TEAM_ID = TEAM.ID "
				+ "WHERE POST.USER_ID = ACCOUNT.ID AND "
				+ "POST.CATEGORY_ID = CATEGORY.ID AND POST.ID = ? ";

		try {
			Connection con = this.getCon();

			Post post;
			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, postid);
				ResultSet rs = pstmt.executeQuery();
				if (!rs.next()) {
					return null;
				}

				post = new Post(rs);
				return post;
			} finally {
				if (con != null) {
					con.close();
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();

		}
		return null;
	}

	public boolean isPost(int postid) {
		boolean result = false;
		//sql
		String sql = "SELECT * FROM POST WHERE ID = ?";

		try {
			Connection con = this.getCon();

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, postid);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					result = true;
				}
			} finally {
				if (con != null) {
					con.close();
				}

			}
		}catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public boolean CheckPostUser(int postid, int id) {
		boolean result = false;
		String sql = "SELECT * FROM POST WHERE ID = ? AND USER_ID = ?";

		try {
				Connection con = this.getCon();

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, postid);
				pstmt.setInt(2, id);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					result = true;
				}
			} finally {
				if (con != null) {
					con.close();
				}

			}
		}catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<Post> FetchAllPost() {
		return this.FetchAllPost(0, 12);
	}

	public ArrayList<Post> FetchAllPost(int offset) {
		return this.FetchAllPost(offset, 12);
	}

	public ArrayList<Post> FetchAllPost(int offset, int limit) {
		int key = offset * limit;
		//sql
		String sql = "SELECT " + SelectColumn + ",COALESCE(MAX(COMMENT.DATE + COMMENT.TIME),POST.DATE + POST.TIME) AS MAX_DATETIME, "
				+ "COUNT(COMMENT.ID) AS COUNT_COMMENT "
				+ "FROM POST LEFT JOIN COMMENT ON POST.ID = COMMENT.POST_ID, "
				+ "CATEGORY,ACCOUNT "
				+ "LEFT JOIN TEAM ON ACCOUNT.TEAM_ID = TEAM.ID "
				+ "WHERE POST.USER_ID = ACCOUNT.ID AND POST.CATEGORY_ID = CATEGORY.ID "
				+ "GROUP BY POST.ID,ACCOUNT.ID,CATEGORY.ID,TEAM.ID "
				+ "ORDER BY MAX_DATETIME "
				+ "DESC LIMIT ? OFFSET ? ";

		ArrayList<Post> postList = new ArrayList<>();

		try {
			Connection con = this.getCon();

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, limit);
				pstmt.setInt(2, key);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					Post post = new Post(rs);
					postList.add(post);
				}
			} finally {
				if (con != null) {
					con.close();
				}

			}
			}catch (SQLException e) {
				e.printStackTrace();
		}

		return postList;
	}

	public ArrayList<Post> FetchAllPost(int offset, int limit, int categoryid) {

		int key = offset * limit;
		//sql
		String sql = "SELECT " + SelectColumn + ",COALESCE(MAX(COMMENT.DATE + COMMENT.TIME),POST.DATE + POST.TIME) AS MAX_DATETIME, "
				+ "COUNT(COMMENT.ID) AS COUNT_COMMENT "
				+ "FROM POST LEFT JOIN COMMENT ON POST.ID = COMMENT.POST_ID, "
				+ "CATEGORY,ACCOUNT "
				+ "LEFT JOIN TEAM ON ACCOUNT.TEAM_ID = TEAM.ID "
				+ "WHERE POST.USER_ID = ACCOUNT.ID AND POST.CATEGORY_ID = CATEGORY.ID AND CATEGORY.ID = ? "
				+ "GROUP BY POST.ID,ACCOUNT.ID,CATEGORY.ID,TEAM.ID "
				+ "ORDER BY MAX_DATETIME "
				+ "DESC LIMIT ? OFFSET ? ";
		ArrayList<Post> postList = new ArrayList<>();

		try {
			Connection con = this.getCon();

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, categoryid);
				pstmt.setInt(2, limit);
				pstmt.setInt(3, key);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					Post post = new Post(rs);
					postList.add(post);
				}
			} finally {
				if (con != null) {
					con.close();
				}

			}
		}catch (SQLException e) {
			e.printStackTrace();
		}

		return postList;
	}

	public ArrayList<Post> FetchAllUserPost(int offset, int limit, int id) {
		int key = offset * limit;
		String sql = "SELECT " + SelectColumn + " FROM POST,CATEGORY,ACCOUNT "
				+ "LEFT JOIN TEAM ON ACCOUNT.TEAM_ID = TEAM.ID "
				+ "WHERE POST.USER_ID = ACCOUNT.ID AND POST.CATEGORY_ID = CATEGORY.ID AND "
				+ "(POST.USER_ID = ? OR POST.ID IN (SELECT POST_ID FROM COMMENT WHERE USER_ID = ?) ) "
				+ "ORDER BY POST.DATE,POST.TIME "
				+ "DESC LIMIT ? OFFSET ? ";
		ArrayList<Post> postList = new ArrayList<>();

		try {
			Connection con = this.getCon();

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, id);
				pstmt.setInt(2, id);
				pstmt.setInt(3, limit);
				pstmt.setInt(4, key);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					Post post = new Post(rs);
					postList.add(post);
				}
			} finally {
				if (con != null) {
					con.close();
				}

			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return postList;
	}

	public boolean InsertPost(Post post,int userid,int categoryid) {
		//???????????????????????????????????????
		if (!post.getTitle().isEmpty() && !post.getText().isEmpty()) {

			//sql
			String sql = "INSERT INTO POST(USER_ID,CATEGORY_ID,TITLE,TEXT) VALUES(?, ?, ?, ?)";
			boolean result = false;

			try {
				Connection con = this.getCon();

				try {
					con.setAutoCommit(false);
					PreparedStatement pstmt = con.prepareStatement(sql);
					pstmt.setInt(1, userid);
					pstmt.setInt(2, categoryid);
					pstmt.setString(3, post.getTitle());
					pstmt.setString(4, post.getText());
					int r = pstmt.executeUpdate();
					if (r != 0) {
						result = true;
					}

					con.commit();
				} catch (SQLException e) {
					e.printStackTrace();
					con.rollback();
				} finally {
					if (con != null) {
						con.close();
					}

				}
			}catch (SQLException e) {
				e.printStackTrace();
			}

			return result;
		} else {
			return false;
		}
	}

	public boolean UpdateString(String key, String word, int postid) {
		//sql
		String sql = "UPDATE POST SET " + key + " = ? WHERE ID = ?";
		boolean result = false;

		try {
			Connection con = this.getCon();

			try {
				con.setAutoCommit(false);
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, word);
				pstmt.setInt(2, postid);
				int r = pstmt.executeUpdate();
				if (r != 0) {
					result = true;
				}

				con.commit();
			} catch (SQLException var19) {
				var19.printStackTrace();
				con.rollback();
			} finally {
				if (con != null) {
					con.close();
				}

			}
		}catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public boolean DeletePost(int postid) {
		//sql
		String sql = "DELETE FROM COMMENT WHERE POST_ID = ? ;DELETE FROM POST "
				+ "WHERE ID = ? ";
		boolean result = false;

		try {
			Connection con = this.getCon();

			try {
				con.setAutoCommit(false);
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, postid);
				pstmt.setInt(2, postid);
				int r = pstmt.executeUpdate();
				if (r != 0) {
					result = true;
				}

				con.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				con.rollback();
			} finally {
				if (con != null) {
					con.close();
				}

			}
		}catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
}