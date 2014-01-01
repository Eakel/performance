package com.easyfun.eclipse.component.ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;

import com.easyfun.eclipse.performance.navigator.console.LogHelper;
import com.easyfun.eclipse.rcp.ConcurrentCapacity;

/**
 * 处理FTP/SFTP
 * 
 * @author linzhaoming
 *
 */
public class FTPHelper {
	private static transient Log log = LogFactory.getLog(FTPHelper.class);
	
	public static final int BIN = 0;
	public static final int ASC = 1;
	private FTPClient client = null;

	private static int TIMEOUT_SECONDS = 120;
	private static ConcurrentCapacity SEM = new ConcurrentCapacity(10, 3);
	
	private int ftpType;
	private String ip;
	private int port;
	private String userName;
	private String passwd;
	private String remotePath;
	private String localPath;
	
	public FTPHelper(FTPHostBean bean){
		this(bean.getFtpType(), bean.getHost(), bean.getPort(), bean.getUsername(), bean.getPassword(), bean.getRemotePath(), "");
	}

	public FTPHelper(int ftpType, String ip, int port, String userName, String password, String remotePath, String localPath){
		this.ftpType = ftpType;
		this.ip = ip;
		this.port = port;
		this.userName = userName;
		this.passwd = password;
		this.remotePath = remotePath;
		this.localPath = localPath;
	}
	
	/** 连接到FTP服务器*/
	public void connect() throws Exception {
		if (ftpType == FTPHostBean.TYPE_SFTP) {
			this.client = new FTPSClient(true);
			LogHelper.debug(log, "[SFTP]类型");
		} else if (ftpType == FTPHostBean.TYPE_FTP) {
			this.client = new FTPClient();
			LogHelper.debug(log, "[FTP]类型");
		} else {
			throw new Exception("不支持的FTP类型");
		}
		
		this.client.setDefaultTimeout(TIMEOUT_SECONDS * 1000);
		boolean remoteVerificationEnabled = true;
		boolean localPasv = false;

		if (ip.startsWith("{PASV}")) {
			remoteVerificationEnabled = false;
			ip = StringUtils.substringAfter(ip, "{PASV}");
		} else if (ip.startsWith("{PORT}")) {
			remoteVerificationEnabled = true;
			ip = StringUtils.substringAfter(ip, "{PORT}");
		} else if (ip.startsWith("{LOCAL_PASV}")) {
			localPasv = true;
			ip = StringUtils.substringAfter(ip, "{LOCAL_PASV}");
		}
		
		LogHelper.info(log, "Trying to connect [" + ip + ":" + port + "]");
		this.client.connect(ip, port);
		this.client.setRemoteVerificationEnabled(remoteVerificationEnabled);
		this.client.login(userName, this.passwd);
		LogHelper.info(log, "Logged in as user: " + userName);
		if (localPasv) {
			this.client.enterLocalPassiveMode();
		}
		
//		if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
//			throw new RuntimeException("FTP server refused connection.");
//		}
	    
		if(StringUtils.isNotEmpty(remotePath)){
			this.client.changeWorkingDirectory(wrapper(remotePath));
		}
		
		LogHelper.info(log, "Connected to " + ip);
	}
	

	public void disconnect() throws Exception {
		if (this.client.isConnected()) {
			this.client.disconnect();
			this.client.logout();
		}
	}
	
	public FTPClient getFTPClient() {
		return client;
	}

	private String wrapper(String str) throws Exception {
		return new String(str.getBytes(), "ISO-8859-1");
	}

	public void setEncoding(String encoding) {
		this.client.setControlEncoding(encoding);
	}

	public void bin() throws Exception {
		this.client.setFileType(FTP.BINARY_FILE_TYPE);
	}

	public void asc() throws Exception {
		this.client.setFileType(FTP.ASCII_FILE_TYPE);
	}

	public String[] list(String pathName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			List<String> list = new ArrayList<String>();

			FTPFile[] ftpFiles = this.client.listFiles(pathName);
			for (int i = 0; i < ftpFiles.length; i++) {
				if (ftpFiles[i].isFile()) {
					list.add(ftpFiles[i].getName());
				}
			}

			return (String[]) list.toArray(new String[0]);
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	public String[] listNames() throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			String[] fileNames = this.client.listNames();
			return fileNames;
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	public String[] listNames(String pathName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			String[] fileNames = this.client.listNames(pathName);
			return fileNames;
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	/** 列出文件名称列表*/
	public String[] list() throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			List<String> list = new ArrayList<String>();
			FTPFile[] ftpFiles = this.client.listFiles();
			for (int i = 0; i < ftpFiles.length; i++) {
				if (ftpFiles[i].isFile()) {
					list.add(ftpFiles[i].getName());
				}
			}

			return list.toArray(new String[0]);
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}
	
	public FTPFile[] listFiles() throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			
			FTPFile[] ftpFiles = this.client.listFiles();
			return ftpFiles;
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	public FTPFile getFtpFile(String fileName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			FTPFile rtn = null;
			FTPFile[] ftpFiles = this.client.listFiles(fileName);
			for (int i = 0; i < ftpFiles.length; i++) {
				FTPFile ftpFile = ftpFiles[i];
				if ((ftpFile.isFile()) && (ftpFile.getName().equals(fileName))) {
					rtn = ftpFile;
					break;
				}
			}

			if (rtn == null) {
				throw new Exception("无法在目录:" + this.client.printWorkingDirectory() + "下找到文件:" + fileName);
			}

			return rtn;
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	public String[] listDir() throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			List<String> list = new ArrayList<String>();
			FTPFile[] ftpFiles = this.client.listFiles();
			for (int i = 0; i < ftpFiles.length; i++) {
				FTPFile ftpFie = ftpFiles[i];
				if (ftpFie.isDirectory()) {
					list.add(ftpFie.getName());
				}
			}

			return list.toArray(new String[0]);
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	public boolean mkdir(String dir) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			return this.client.makeDirectory(dir);
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	public boolean changeWorkingDirectory(String dir) throws Exception {
		return this.client.changeWorkingDirectory(wrapper(dir));
	}

	public void upload(String remoteFileName, InputStream input, int mode) throws Exception {
		if (mode == 0) {
			this.client.setFileType(2);
		} else if (mode == 1) {
			this.client.setFileType(0);
		} else {
			throw new Exception("不支持的传输模式:");
		}
		upload(remoteFileName, input);
	}

	public void upload(String remoteFileName, InputStream input) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			this.client.storeFile(wrapper(remoteFileName), input);
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	public void upload(String remoteFileName, String localFileName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			InputStream is = null;
			try {
				is = new BufferedInputStream(new FileInputStream(this.localPath + "/" + localFileName));
				this.client.storeFile(wrapper(remoteFileName), is);
			} finally {
				if (is != null) {
					is.close();
				}
			}
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	public void upload(String remoteFileName, String localFileName, int mode) throws Exception {
		if (mode == 0) {
			this.client.setFileType(2);
		} else if (mode == 1) {
			this.client.setFileType(0);
		} else {
			throw new Exception("不支持的传输模式:" + mode);
		}
		upload(remoteFileName, localFileName);
	}

	public void download(String remoteFileName, OutputStream output, int mode) throws Exception {
		if (mode == 0) {
			this.client.setFileType(2);
		} else if (mode == 1) {
			this.client.setFileType(0);
		} else {
			throw new Exception("不支持的传输模式:" + mode);
		}
		download(remoteFileName, output);
	}

	public void download(String remoteFileName, OutputStream output) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			boolean rtn = this.client.retrieveFile(wrapper(remoteFileName), output);
			if (!rtn) {
				throw new Exception("下载远程文件:" + remoteFileName + ",不成功");
			}
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	public void download(String remoteFileName, String localFileName, int mode) throws Exception {
		if (mode == BIN) {
			this.client.setFileType(FTP.BINARY_FILE_TYPE);
		} else if (mode == ASC) {
			this.client.setFileType(FTP.ASCII_FILE_TYPE);
		} else {
			throw new Exception("不支持的传输模式:" + mode);
		}
		download(remoteFileName, localFileName);
	}

	public void download(String remoteFileName, String localFileName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			OutputStream os = null;
			try {
				os = new BufferedOutputStream(new FileOutputStream(this.localPath + "/" + localFileName));
				boolean rtn = this.client.retrieveFile(wrapper(remoteFileName), os);
				if (!rtn) {
					throw new Exception("下载远程文件:" + remoteFileName + ",不成功");
				}
			} finally {
				if (os != null) {
					os.close();
				}
			}
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	public InputStream readRemote(String remoteFileName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			return this.client.retrieveFileStream(wrapper(remoteFileName));
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	public int getReplay() throws Exception {
		return this.client.getReply();
	}

	public InputStream readRemote(String remoteFileName, int mode) throws Exception {
		if (mode == BIN) {
			this.client.setFileType(FTP.BINARY_FILE_TYPE);
		} else if (mode == ASC) {
			this.client.setFileType(FTP.ASCII_FILE_TYPE);
		} else {
			throw new Exception("不支持的传输模式:" + mode);
		}
		return readRemote(remoteFileName);
	}

	public void rename(String oldRemoteFileName, String newRemoteFileName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			this.client.rename(wrapper(oldRemoteFileName), wrapper(newRemoteFileName));
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	public void delete(String remoteFileName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			boolean rtn = this.client.deleteFile(wrapper(remoteFileName));
			if (!rtn) {
				throw new Exception("下载远程文件:" + remoteFileName + ",不成功");
			}
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	public void completePendingCommand() throws Exception {
		this.client.completePendingCommand();
	}

	public OutputStream getOutputStream(String fileName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			OutputStream localOutputStream = this.client.storeFileStream(wrapper(fileName));
			return localOutputStream;
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}
	
	public String getWorkingDirectory() throws IOException {
		return client.printWorkingDirectory();
	}
	
	public boolean changeToParentDirectory() throws IOException{
		return client.changeToParentDirectory();
	}
	
	/** *
	 * @deprecated 不应该区分对待
	 */
	public static InputStream getInputStream(FTPHostBean ftpBean) throws Exception{
		FTPClient client = new FTPClient();
		client.setConnectTimeout(1000);
		client.connect(ftpBean.getHost(), ftpBean.getPort());
		client.login(ftpBean.getUsername(), ftpBean.getPassword());
		client.setFileType(FTP.ASCII_FILE_TYPE);
		InputStream is = client.retrieveFileStream(ftpBean.getRemotePath());
		return is;
	}

}