package com.easyfun.eclipse.common.ui.db;

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

	private DBTypeEnum dbType;

	public DBUrlBean(DBTypeEnum dbType) {
		this.dbType = dbType;
	}

	public DBUrlBean(DBTypeEnum dbType, String url, String driverClass, String username, String password) {
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

	public DBTypeEnum getDBType() {
		return dbType;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DBUrlBean copy(){
		DBUrlBean bean = new DBUrlBean(dbType);
		bean.setName("CopyOf" + name);
		bean.setDriverClass(driverClass);
		bean.setUrl(url);
		bean.setUsername(username);
		bean.setPassword(password);
		return bean;
	}

}
