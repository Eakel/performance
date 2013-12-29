package com.easyfun.eclipse.component.ftp;

import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * FTP util
 * @author linzhaoming
 *
 * 2011-4-16
 *
 */
public class FTPUtil {
	
	/** ¶ÁÈ¡FTPÁ÷*/
	public static InputStream getInputStream(FTPBean ftpBean) throws Exception{
		FTPClient client = new FTPClient();
		client.setConnectTimeout(1000);
		client.connect(ftpBean.getHost(), ftpBean.getPort());
		client.login(ftpBean.getUserName(), ftpBean.getPasswd());
		client.setFileType(FTP.ASCII_FILE_TYPE);
		InputStream is = client.retrieveFileStream(ftpBean.getFilePath());
		return is;
	}
}
