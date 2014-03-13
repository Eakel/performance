package com.easyfun.eclipse.component.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The Model for the DB Connection
 * 
 * @author linzhaoming 
 * 
 * Create Date: 2010-11-27
 */
public class ConnectionModel {

	private String url = "";
	private String driverClass = "";
	private String username = "";
	private String password ="" ;
	
	private Connection connection;
	private boolean initial = false;
	
	public ConnectionModel(){
	}

	/**
	 * @param driverClass
	 * @param username
	 * @param password
	 * @param url
	 */
	public ConnectionModel(String driverClass, String username, String password, String url) {
		super();
		this.driverClass = driverClass;
		this.username = username;
		this.password = password;
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
		
	private void initConnection() throws Exception{
		Class.forName(driverClass);
	}
	
	/** 获取一个数据库连接*/
	public Connection getConnection() throws Exception{
		if(initial == false){
			initConnection();
			initial = true;
		}
		
		if(connection != null && !connection.isClosed()){
			connection.close();
		}
		
		connection = DriverManager.getConnection(url, username, password);
		return connection;
	}
	
	/** 关闭连接*/
	public void close(){
		try {
			if(connection != null && !connection.isClosed()){
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return "URL:" + url + ", Name=" + username + ", Pasword=" + password; 
	}
}
