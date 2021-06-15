package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import core.DAOBase;
import model.Team;

public class TeamDAO extends DAOBase {
	public boolean isName(String name) {
		//空白ならtrue
		if ("".equals(name)) {
			return true;
		}
		//sql
		String sql = "SELECT * FROM TEAM WHERE NAME = ?";
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean isTeam(int teamid) {
		//sql
		String sql = "SELECT * FROM TEAM WHERE id = ?";

		try {
			Connection con = this.getCon();

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, teamid);
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
		}
		return true;
	}

	public Team getTeam(int readerid) {
		//sql
		String sql = "SELECT * FROM TEAM,ACCOUNT AS USER WHERE "
				+ "TEAM.READER_ID = USER.ID AND TEAM.ID = ? ";

		try {
			Connection con = this.getCon();

			Team var10000;
			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, readerid);
				ResultSet rs = pstmt.executeQuery();
				if (!rs.next()) {
					return null;
				}

				Team team = new Team(rs);
				var10000 = team;
			} finally {
				if (con != null) {
					con.close();
				}

			}

			return var10000;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean InsertTeam(Map<String, String> teamMap) {
		//マップに値があるか確認
		if (teamMap.containsKey("userid") && teamMap.containsKey("name") && teamMap.containsKey("text")
				&& teamMap.containsKey("image")) {
			String userid = (String) teamMap.get("userid");
			String name = (String) teamMap.get("name");
			String text = (String) teamMap.get("text");
			String image = (String) teamMap.get("image");
			//名前が空白なら失敗
			if (this.isName(name)) {
				return false;
			}
			//sql
			String sql = "INSERT INTO TEAM(READER_ID,NAME,TEXT,IMAGE) VALUES(?, ?, ?, ? ); UPDATE ACCOUNT SET TEAM_ID = (SELECT ID FROM TEAM WHERE NAME = ? ) WHERE ID = ? ";
			boolean result = false;

			try {
				Connection con = this.getCon();

				try {
					con.setAutoCommit(false);
					PreparedStatement pstmt = con.prepareStatement(sql);
					pstmt.setString(1, userid);
					pstmt.setString(2, name);
					pstmt.setString(3, text);
					pstmt.setString(4, image);
					pstmt.setString(5, name);
					pstmt.setString(6, userid);
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
		return false;
	}

	public boolean DeleteTeam(int teamid) {
		//sql
		String sql = "DELETE FROM TEAM WHERE ID = ? ";
		boolean result = false;

		try {
			Connection con = this.getCon();

			try {
				con.setAutoCommit(false);
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, teamid);
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

	public boolean UpdateString(String key, String word, int teamid) {
		//sql
		String sql = "UPDATE TEAM SET " + key + " = ? WHERE ID = ?";
		boolean result = false;

		try {
			Connection con = this.getCon();

			try {
				con.setAutoCommit(false);
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, word);
				pstmt.setInt(2, teamid);
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

	public boolean UpdateInt(String key, int word, int teamid) {
		//sql
		String sql = "UPDATE TEAM SET " + key + " = ? WHERE ID = ?";
		boolean result = false;

		try {
			Connection con = this.getCon();

			try {
				con.setAutoCommit(false);
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, word);
				pstmt.setInt(2, teamid);
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