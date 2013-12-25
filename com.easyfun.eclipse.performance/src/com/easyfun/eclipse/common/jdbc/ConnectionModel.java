package com.easyfun.eclipse.common.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

/**数据库连接模型
 * 
 * @author linzhaoming
 * Create Date: 2010-11-27
 */
public class ConnectionModel {
//	private String url = "jdbc:mysql://127.0.0.1:3306/base";
//	private String driver = "com.mysql.jdbc.Driver";
//	private String name = "base";
//	private String password ="base" ;
	
	private String url = "";
	private String driver = "";
	private String name = "";
	private String password ="" ;
	
	private Connection connection;
	private boolean initial = false;
	
	public ConnectionModel(){
	}

	/**
	 * @param driver
	 * @param name
	 * @param password
	 * @param url
	 */
	public ConnectionModel(String driver, String name, String password, String url) {
		super();
		this.driver = driver;
		this.name = name;
		this.password = password;
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
		
	private void initConnection() throws Exception{
		Class.forName(driver);
		connection = DriverManager.getConnection(url, name, password);
	}
	
	/**
	 * 重新获取数据库连接
	 */
	public Connection getRefreshConnection() throws Exception{
		initConnection();
		return connection;
	}
	
	/** 生成一个数据库新连接*/
	public Connection getNewConnection() throws Exception{
		if(initial == false){
			initConnection();
			initial = true;
		}
		return DriverManager.getConnection(url, name, password);
	}
	
	/**
	 * 获取数据库连接
	 * @return
	 */
	public Connection getConnection() throws Exception{
		if(initial == false){
			initConnection();
			initial = true;
		}
		return connection;
	}
	
	public String toString() {
		return "URL:" + url + ", Name=" + name + ", Pasword=" + password; 
	}
}
