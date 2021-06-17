package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import core.DAOBase;
import model.Category;

public class CategoryDAO extends DAOBase {

	public boolean isCategoryid(int categoryid) {
		//SQL
		String sql = "SELECT * FROM CATEGORY WHERE ID = ?";

		try {
			Connection con = this.getCon();
			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, categoryid);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					return true;
				}
			}finally {
				if (con != null) {
					con.close();
				}
			}
		}catch (SQLException e) {
		e.printStackTrace();

		}
		return false;
	}

	public Category getCategoryid(int categoryid) {
		//SQL
		String sql = "SELECT * FROM CATEGORY WHERE ID = ?";

		try {
			Connection con = this.getCon();
			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, categoryid);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					Category category = new Category(rs.getInt("id"), rs.getString("name"), rs.getBoolean("team_flag"));
					return category;

				}
			}finally {
				if (con != null) {
					con.close();
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<Category> getCategoryList() {
		ArrayList<Category> CategoryList = new ArrayList<>();
		String sql = "SELECT * FROM CATEGORY ORDER BY ID ASC";

		try {
			Connection con = this.getCon();
			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					Category category = new Category(rs.getInt("id"), rs.getString("name"),
							rs.getBoolean("team_flag"));
					CategoryList.add(category);
				}
			} finally {
				if (con != null) {
					con.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return CategoryList;
	}
}