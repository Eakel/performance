package com.easyfun.eclipse.performance.trace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.easyfun.eclipse.component.ftp.FTPBean;
import com.easyfun.eclipse.performance.navigator.console.LogHelper;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SFTPv3Client;
import com.trilead.ssh2.SFTPv3DirectoryEntry;

public class SFtpClient {
	public static final int BIN = 0;
	public static final int ASC = 1;
	private SFTPv3Client client = null;

	
	private Connection conn;
	
	public SFtpClient(FTPBean bean) throws Exception {
		this(bean.getFtpType(), bean.getHost(), bean.getPort(), bean.getUserName(), bean.getPasswd(), bean.getFilePath());
	}

	public SFtpClient(int ftpType,String ip, int port, String userName, String password, String remotePath) throws Exception {
		if(ftpType == FTPBean.TYPE_SFTP){
			conn = new Connection(ip, port);
			long t1 = System.currentTimeMillis();
			LogHelper.info("����: " + " [" + ip + ":" + port + "]");
			conn.connect();		
			long t2 = System.currentTimeMillis();
			LogHelper.info(" [" + ip + ":" + port + "] SFTP���ӽ�����ʱ��Ϊ: " + (t2 - t1) + "ms");
			boolean isAuthenticated = conn.authenticateWithPassword(userName, password);		
			long t3 = System.currentTimeMillis();
			LogHelper.info(" [" + ip + ":" + port + "] SFTP��֤��ʱ��Ϊ: " + (t3 - t2) + "ms");
			if (!isAuthenticated) {
				LogHelper.error("��֤ʧ��:" + " ip=" + ip + ", username=" + userName);
				throw new IOException("��֤ʧ��:" + " ip=" + ip + ", username=" + userName);
			}
			this.client = new SFTPv3Client(conn);
		}else{
			LogHelper.error("��֧�ֵ�FTP����");
			throw new Exception("��֧�ֵ�FTP����");			
		}
	}
	
	public Connection getConn(){
		return conn;
	}

	public List<SFTPv3DirectoryEntry> list(String filePath) throws Exception{
		ArrayList<SFTPv3DirectoryEntry> list = new ArrayList<SFTPv3DirectoryEntry>();
		String ftpPath = client.canonicalPath(filePath);
		Vector<SFTPv3DirectoryEntry> vector = client.ls(ftpPath);
		for (SFTPv3DirectoryEntry entry : vector) {
			list.add(entry);
		}
		return list;
	}

}