package com.easyfun.eclipse.component.ftp;

/**
 * POJO for FTP
 * @author linzhaoming
 *
 * 2011-4-11
 *
 */
public class FTPBean {
	
	public static final int TYPE_FTP = 0;
	public static final int TYPE_SFTP = 1;
	
	private String filePath = "";
	private String passwd = "";
	private String userName = "";
	private int port = 21;
	private String host = "";
	
	private int ftpType = TYPE_FTP;
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
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
	public String getFTPDesc(){
		StringBuffer desc = new StringBuffer();
		desc.append("host:").append(getHost()).append("\t");
		desc.append("port:").append(getPort()).append("\t");
		desc.append("ftpType:").append(getFtpType()).append("\t");
		desc.append("username:").append(getUserName()).append("\t");
		desc.append("password:").append(getPasswd()).append("\t");
		desc.append("file:").append(getFilePath());
		return desc.toString();
	}
	
}
