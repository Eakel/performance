package com.easyfun.eclipse.component.db;

/**
 * DBUrlDialogµÄ¸¨ÖúÀà
 * 
 * @author linzhaoming
 *
 * 2013-12-26
 *
 */
public class DBUrlBean {
	private String name;
	private String url;
	private String driverClass;
	private String username;
	private String password;

	private int dbType;
	
	public static final int ORACLE_TYPE = 0; 
	
	public static final int MYSQL_TYPE = 1; 

	public DBUrlBean() {
//		this.dbType = dbType;
	}

	public DBUrlBean(int dbType, String url, String driverClass, String username, String password) {
		this.dbType = dbType;
		this.url = url;
		this.driverClass = driverClass;
		this.username = username;
		this.password = password;
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

	public int getDbType() {
		return dbType;
	}
	
	public void setDbType(int dbType) {
		this.dbType = dbType;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public boolean equals(Object obj) {
		if (obj instanceof DBUrlBean) {
			DBUrlBean bean = (DBUrlBean) obj;
			return bean.getName().equals(name) && bean.getDbType() == dbType
					&& bean.getDriverClass().equals(driverClass)
					&& bean.getPassword().equals(password)
					&& bean.getUsername().equals(username);
		} else {
			return false;
		}
	}

	public DBUrlBean copy(){
		DBUrlBean bean = new DBUrlBean();
		bean.setDbType(dbType);
		bean.setName("CopyOf" + name);
		bean.setDriverClass(driverClass);
		bean.setUrl(url);
		bean.setUsername(username);
		bean.setPassword(password);
		return bean;
	}
	
	public static void main(String[] args) {
		
	}

}
