package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import core.DAOBase;

public class RankDAO extends DAOBase {
	public Map<Integer, String> getRankList() {
		//sql
		String sql = "SELECT * FROM Rank ORDER BY ID ASC";
		Map<Integer, String> ranks = new HashMap<>();

		try {
			Connection con = this.getCon();

			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					ranks.put(rs.getInt("id"), rs.getString("name"));
				}
			} finally {
				if (con != null) {
					con.close();
				}

			}
		}catch (SQLException var16) {
			var16.printStackTrace();
		}

		return ranks;
	}

	public boolean isRank(int id) {
		//sql
		String sql = "SELECT * FROM RANK WHERE ID = ?";

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