package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import core.DAOBase;
import model.Comment;

public class CommentDAO extends DAOBase {

	String SelectColumn = " ACCOUNT.*,COMMENT.ID AS COMMENT_ID,COMMENT.TEXT AS COMMENT_TEXT,"
			+ "COMMENT.DATA AS COMMENT_DATA,COMMENT.TIME AS COMMENT_TIME,"
			+ "TEAM.NAME AS TEAM_NAME ";
	public ArrayList<Comment> FetchAllComment(int postid) {
		//sql
		String sql = "SELECT " + SelectColumn + " FROM COMMENT,ACCOUNT "
				+ "LEFT JOIN TEAM ON ACCOUNT.TEAM_ID = TEAM.ID "
				+ "WHERE COMMENT.USER_ID = ACCOUNT.ID AND COMMENT.POST_ID = ? "
				+ "ORDER BY COMMENT.ID ASC ";

		ArrayList<Comment> cmList = new ArrayList<>();

		try {
			Connection con = this.getCon();
			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, postid);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					Comment cm = new Comment(rs);
					cmList.add(cm);
				}
			} finally {
				if (con != null) {
					con.close();
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cmList;
	}

	public int getMaxid(int postid) {
		//sql
		String sql = "SELECT COALESCE(MAX(ID),0) AS MAXID FROM COMMENT WHERE POST_ID = ? ";

		try {
			Connection con = this.getCon();
			int maxid;
			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, postid);
				ResultSet rs = pstmt.executeQuery();
				if (!rs.next()) {
					return 0;
				}
				maxid = rs.getInt("MAXID");
			} finally {
				if (con != null) {
					con.close();
				}
			}
			return maxid;
		} catch (SQLException e) {
		e.printStackTrace();
		return 0;
		}

	}

	public boolean InsertComment(int postid, int userid, String text) {
		//sql
		String sql = "INSERT INTO COMMENT(POST_ID,ID,USER_ID,TEXT) VALUES(?, ?, ?,?)";
		boolean result = false;

		try {
			Connection con = this.getCon();
			try {
				con.setAutoCommit(false);
				PreparedStatement pstmt = con.prepareStatement(sql);
				int maxid = this.getMaxid(postid) + 1;
				pstmt.setInt(1, postid);
				pstmt.setInt(2, maxid);
				pstmt.setInt(3, userid);
				pstmt.setString(4, text);
				int r = pstmt.executeUpdate();
				if (r != 0) {
					result = true;
				}

				con.commit();
			} catch (SQLException var20) {
				var20.printStackTrace();
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

	public boolean DeleteComment(int postid, int commentid) {
		//sql
		String sql = "DELETE FROM COMMENT WHERE POST_ID = ? AND ID = ?";
		boolean result = false;

		try {
			Connection con = this.getCon();

			try {
				con.setAutoCommit(false);
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, postid);
				pstmt.setInt(2, commentid);
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