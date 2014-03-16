package com.easyfun.eclipse.component.sql;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBHelper {
	public static Connection getConnection() throws Exception{
		Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/base", "root", "my");
		return conn;
	}
}
