package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import core.DAOBase;
import model.User;

public class UserDAO extends DAOBase {
	String SelectColumn =" ACCOUNT.*,TEAM.NAME AS TEAM_NAME ";
	public boolean isUser(int userid) {
		//sql
		String sql = "SELECT * FROM ACCOUNT WHERE ID = ?";

		try {
			Connection con = this.getCon();

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, userid);
				ResultSet rs = pstmt.executeQuery();
				if (!rs.next()) {
					return false;
				}
			} finally {
				if (con != null) {
					con.close();
				}

			}

			return true;
		}catch (SQLException e) {
		e.printStackTrace();
			return false;
		}
	}

	public boolean isLoginid(String loginid) {
		//sql
		String sql = "SELECT * FROM ACCOUNT WHERE LOGIN_ID = ?";

		try {
			Connection con = this.getCon();

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, loginid);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					return true;
				}
			} finally {
				if (con != null) {
					con.close();
				}

			}

			return false;
		}catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
	}

	public boolean isName(String name) {
		//空白ならtrue
		if ("".equals(name)) {
			return true;
		}
		//sql
		String sql = "SELECT * FROM ACCOUNT WHERE NAME = ?";

		try {
			Connection con = this.getCon();

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, name);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					return true;
				}
			} finally {
				if (con != null) {
					con.close();
				}

			}

			return false;
		}catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
	}

	public User getUser(int userid) {
		//sql
		String sql = "SELECT " + SelectColumn + " FROM ACCOUNT "
				+ "LEFT JOIN TEAM ON ACCOUNT.TEAM_ID = TEAM.ID "
				+ "WHERE ACCOUNT.ID = ? ";

		try {
			Connection con = this.getCon();

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, userid);
				ResultSet rs = pstmt.executeQuery();
				if (!rs.next()) {
					return null;
				}

				User user = new User(rs);
				return user;
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
	public boolean isTeamUser(int userid, int teamid) {
		//sql
		String sql = "SELECT * FROM ACCOUNT "
				+ " LEFT JOIN TEAM ON ACCOUNT.TEAM_ID = TEAM.ID "
				+ "WHERE ACCOUNT.ID = ? AND ACCOUNT.TEAM_ID = ? ";

		try {
			Connection con = this.getCon();
			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, userid);
				pstmt.setInt(2, teamid);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					return true;
				}
			} finally {
				if (con != null) {
					con.close();
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();

		}
		return false;
	}

	public User CheckLogin(String loginid, String pass) {
		//sql
		String sql = "SELECT " + SelectColumn + " FROM ACCOUNT "
				+ "LEFT JOIN TEAM ON ACCOUNT.TEAM_ID = TEAM.ID "
				+ "WHERE ACCOUNT.LOGIN_ID = ? AND ACCOUNT.PASS = ? ";

		try {
			Connection con = this.getCon();
			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, loginid);
				//パスワードハッシュ化
				pstmt.setString(2, this.hash(pass));
				ResultSet rs = pstmt.executeQuery();
				if (!rs.next()) {
					return null;
				}

				User user = new User(rs);
				return user;
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

	public ArrayList<User> getUserList() {
		ArrayList<User> userList = new ArrayList<>();
		//sql
		String sql = "SELECT " + SelectColumn + " FROM ACCOUNT "
				+ "LEFT JOIN TEAM ON ACCOUNT.TEAM_ID = TEAM.ID ";

		try {
			Connection con = this.getCon();

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					User user = new User(rs);
					userList.add(user);
				}
			} finally {
				if (con != null) {
					con.close();
				}

			}
		}catch (SQLException e) {
			e.printStackTrace();
		}

		return userList;
	}

	public ArrayList<User> getTeamUserList(int teamid) {
		ArrayList<User> userList = new ArrayList<>();
		//sql
		String sql = "SELECT " + SelectColumn + " FROM ACCOUNT "
				+ "LEFT JOIN TEAM ON ACCOUNT.TEAM_ID = TEAM.ID WHERE ACCOUNT.TEAM_ID =  ?";

		try {
			Connection con = this.getCon();

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, teamid);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					User user = new User(rs);
					userList.add(user);
				}
			} finally {
				if (con != null) {
					con.close();
				}

			}
		}catch (SQLException e) {
			e.printStackTrace();
		}

		return userList;
	}

	public boolean InsertUser(User user,String pass) {
		//値があるか確認
		if (!user.getLoginid().isEmpty() &&  !user.getMail().isEmpty()
				&& !user.getName().isEmpty()) {

			//パスワードのハッシュ化
			 pass = this.hash(pass);
			//表示名とログインIDが被ってないか確認
			if (!this.isLoginid(user.getLoginid()) && !this.isName(user.getName())) {
				String sql = "INSERT INTO ACCOUNT(LOGIN_ID,PASS,MAIL,NAME) VALUES(?, ?, ?, ? )";
				boolean result = false;

				try {
					Connection con = this.getCon();

					try {
						con.setAutoCommit(false);
						PreparedStatement pstmt = con.prepareStatement(sql);
						pstmt.setString(1, user.getLoginid());
						pstmt.setString(2, pass);
						pstmt.setString(3, user.getMail());
						pstmt.setString(4, user.getName());
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
		return false;
	}

	public boolean UpdateString(String key, String word, int userid) {
		//パスワードの場合はハッシュ化させる
		if ("pass".equals(key)) {
			word = this.hash(word);
		}
		//sql
		String sql = "UPDATE ACCOUNT SET " + key + " = ? WHERE ID = ?";
		boolean result = false;

		try {
			Connection con = this.getCon();

			try {
				con.setAutoCommit(false);
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, word);
				pstmt.setInt(2, userid);
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

	public boolean UpdateInt(String key, int word, int userid) {
		//sql
		String sql = "UPDATE ACCOUNT SET " + key + " = ? WHERE ID = ?";
		boolean result = false;

		try {
			Connection con = this.getCon();

			try {
				con.setAutoCommit(false);
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, word);
				pstmt.setInt(2, userid);
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

	public boolean UpdateNull(String key, int userid) {
		//sql
		String sql = "UPDATE ACCOUNT SET " + key + " = NULL WHERE ID = ?";
		boolean result = false;

		try {
			Connection con = this.getCon();

			try {
				con.setAutoCommit(false);
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, userid);
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