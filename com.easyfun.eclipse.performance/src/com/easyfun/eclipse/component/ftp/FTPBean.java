package com.easyfun.eclipse.component.ftp;

import com.easyfun.eclipse.component.db.DBUrlBean;

/**
 * POJO for FTP
 * @author linzhaoming
 *
 * 2011-4-11
 *
 */
public class FTPBean {
	
	/** 类型[FTP] */
	public static final int TYPE_FTP = 0;
	/** 类型[SFTP] */
	public static final int TYPE_SFTP = 1;

	private int ftpType = TYPE_FTP;
	
	private String name;

	private String host = "";
	private int port = 21;

	private String passwd = "";
	private String userName = "";

	/** FTP服务器远程路径 */
	private String remotePath = "";
	
	/** FTP服务器远程路径*/
	public String getRemotePath() {
		return remotePath;
	}
	
	/** FTP服务器远程路径*/
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}

	public int getFtpType() {
		return ftpType;
	}
	public void setFtpType(int ftpType) {
		this.ftpType = ftpType;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public FTPBean copy(){
		FTPBean bean = new FTPBean();
		bean.setFtpType(ftpType);
		bean.setHost(host);
		bean.setName("CopyOf" + name);
		bean.setPasswd(passwd);
		bean.setPort(port);
		bean.setRemotePath(remotePath);
		bean.setUserName(userName);
		return bean;
	}

	public String getFTPDesc(){
		StringBuffer desc = new StringBuffer();
		desc.append("host:").append(getHost()).append("\t");
		desc.append("port:").append(getPort()).append("\t");
		desc.append("ftpType:").append(getFtpType()).append("\t");
		desc.append("username:").append(getUserName()).append("\t");
		desc.append("password:").append(getPasswd()).append("\t");
		desc.append("file:").append(getRemotePath());
		return desc.toString();
	}
	
}
