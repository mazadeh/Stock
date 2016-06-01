package ir.stock.domain;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.*;
import java.sql.*;

public class JDBCUtil {

	private final static String CONN_STR = "jdbc:hsqldb:hsql://localhost/";
	private final static Logger LOGGER = LOGUtil.getLogger(JDBCUtil.class); 

	static {
		try {
				Class.forName("org.hsqldb.server.Server");
				LOGGER.config("HSQL server loaded");
			} catch (ClassNotFoundException ex) {
				LOGGER.severe("Unable to load HSQL sever");
		}
		try {
				Class.forName("org.hsqldb.jdbcDriver");
				LOGGER.config("HSQL JDBC driver loaded");
			} catch (ClassNotFoundException ex) {
				LOGGER.severe("Unable to load HSQL JDBC driver");
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(CONN_STR);
	}
}
