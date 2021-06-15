package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DbManager {
	private Map<String, String[]> dbList = new HashMap<>();

	public void setDb(String[] connInfo, String name) {
		this.dbList.put(name, connInfo);
	}

	public void setDb(String[] connInfo) {
		this.setDb(connInfo, "0");
	}

	public Connection getCon(String name) throws SQLException {
		if (!this.dbList.containsKey(name)) {
			if (!this.dbList.containsKey("0")) {
				return null;
			}

			name = "0";
		}

		String[] connInfo = (String[]) this.dbList.get(name);
		String jdbcName = connInfo[0];
		String url = connInfo[1];
		String id = connInfo[2];
		String pass = connInfo[3];

		try {
			Class.forName(jdbcName);
		} catch (ClassNotFoundException var8) {
			var8.printStackTrace();
			return null;
		}

		return DriverManager.getConnection(url, id, pass);
	}

	public Connection getCon() throws SQLException {
		return this.getCon("0");
	}
}