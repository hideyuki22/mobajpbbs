package core;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.SQLException;

public class DAOBase {
	private static DbManager dbMnager = new DbManager();
	//ハッシュ化用
	protected String hashKey = "levtyeq";

	public static void setConnInfo(String[] connInfo) {
		setConnInfo(connInfo, "0");
	}

	public static void setConnInfo(String[] connInfo, String name) {
		dbMnager.setDb(connInfo, name);
	}

	public Connection getCon() throws SQLException {
		return this.getCon("0");
	}

	public Connection getCon(String name) throws SQLException {
		return dbMnager.getCon(name);
	}

	public String hash(String hash) {
		StringBuffer buffer = new StringBuffer();

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			String pass = hash + this.hashKey;
			byte[] result = digest.digest(pass.getBytes());

			for (int i = 0; i < result.length; ++i) {
				String tmp = Integer.toHexString(result[i] & 255);
				if (tmp.length() == 1) {
					buffer.append('0').append(tmp);
				} else {
					buffer.append(tmp);
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		return buffer.toString();
	}
}