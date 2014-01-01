package com.easyfun.eclipse.component.ftp;

/**
 * POJO for FTP
 * @author linzhaoming
 *
 * 2011-4-11
 *
 */
public class FTPHostBean {
	
	/** 类型[FTP] */
	public static final int TYPE_FTP = 0;
	/** 类型[SFTP] */
	public static final int TYPE_SFTP = 1;

	private int ftpType = TYPE_FTP;
	
	private String name;

	private String host = "";
	private int port = 21;

	private String password = "";
	private String username = "";

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
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
	
	public boolean equals(Object obj) {
		if (obj instanceof FTPHostBean) {
			FTPHostBean bean = (FTPHostBean) obj;
			return bean.getName().equals(name) && bean.getFtpType() == ftpType
					&& bean.getHost().equals(host) && bean.getPort() == port
					&& bean.getUsername().equals(username)
					&& bean.getPassword().equals(password)
					&& bean.getRemotePath().equals(remotePath);
		} else {
			return false;
		}
	}
	
	public FTPHostBean copy(){
		FTPHostBean bean = new FTPHostBean();
		bean.setFtpType(ftpType);
		bean.setHost(host);
		bean.setName("CopyOf" + name);
		bean.setPassword(password);
		bean.setPort(port);
		bean.setRemotePath(remotePath);
		bean.setUsername(username);
		return bean;
	}
	
}
