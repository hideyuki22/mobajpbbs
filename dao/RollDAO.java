package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import core.DAOBase;

public class RollDAO extends DAOBase {
	public Map<Integer, String> getRollList() {
		//sql
		String sql = "SELECT * FROM Roll ORDER BY ID ASC";
		Map<Integer, String> rolls = new HashMap<>();

		try {
			Connection con = this.getCon();

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					rolls.put(rs.getInt("id"), rs.getString("name"));
				}
			} finally {
				if (con != null) {
					con.close();
				}

			}
		}catch (SQLException e) {
			e.printStackTrace();
		}

		return rolls;
	}

	public boolean isRoll(int id) {
		//sql
		String sql = "SELECT * FROM ROLL WHERE ID = ?";

		try {
			Connection con = this.getCon();

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, id);
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

		}
		return false;
	}
}