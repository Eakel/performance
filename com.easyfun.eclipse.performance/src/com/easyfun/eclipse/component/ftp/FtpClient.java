package com.easyfun.eclipse.component.ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;

import com.easyfun.eclipse.rcp.ConcurrentCapacity;

public class FtpClient {
	private static transient Log log = LogFactory.getLog(FtpClient.class);
	public static final int BIN = 0;
	public static final int ASC = 1;
	private FTPClient client = null;
	private String localPath = null;
	private String remotePathHis = null;

	private static int TIMEOUT_SECONDS = 120;
	private static ConcurrentCapacity SEM = new ConcurrentCapacity(10, 3);
	
	public FtpClient(FTPBean bean) throws Exception {
		this(bean.getFtpType(), bean.getHost(), bean.getPort(), bean.getUserName(), bean.getPasswd(), bean.getFilePath(), "", "");
	}

	public FtpClient(int ftpType,String ip, int port, String userName, String password, String remotePath, String localPath, String remotePathHis) throws Exception {
		if(ftpType == FTPBean.TYPE_SFTP){
			this.client = new FTPSClient(true);  
		}else if(ftpType == FTPBean.TYPE_FTP){
			this.client = new FTPClient();
		}else{
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
		
		this.client.connect(ip, port);
		this.client.setRemoteVerificationEnabled(remoteVerificationEnabled);
		this.client.login(userName, password);
		if (localPasv) {
			this.client.enterLocalPassiveMode();
		}
	    
		this.client.changeWorkingDirectory(wrapper(remotePath));
		this.localPath = localPath;
		this.remotePathHis = remotePathHis;
	}

	private String wrapper(String str) throws Exception {
		return new String(str.getBytes(), "ISO-8859-1");
	}

	public void setEncoding(String encoding) {
		this.client.setControlEncoding(encoding);
	}

	public void bin() throws Exception {
		this.client.setFileType(2);
	}

	public void asc() throws Exception {
		this.client.setFileType(0);
	}

	public String[] list(String pathName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			List list = new ArrayList();

			FTPFile[] ftpFiles = this.client.listFiles(pathName);
			for (int i = 0; i < ftpFiles.length; i++) {
				if (ftpFiles[i].isFile()) {
					list.add(ftpFiles[i].getName());
				}
			}

			return (String[]) (String[]) list.toArray(new String[0]);
		} finally {
			if (isAcquire)
				SEM.release();
		}
	}

	/** 20120802新增 */
	public String[] listNames() throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			String[] fileNames = this.client.listNames();
			String[] arrayOfString1 = fileNames;
			return arrayOfString1;
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	/** 20120802新增 */
	public String[] listNames(String pathName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			String[] fileNames = this.client.listNames(pathName);
			String[] arrayOfString1 = fileNames;
			return arrayOfString1;
		} finally {
			if (isAcquire) {
				SEM.release();
			}
		}
	}

	public String[] list() throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();
			List list = new ArrayList();

			FTPFile[] ftpFiles = this.client.listFiles();
			for (int i = 0; i < ftpFiles.length; i++) {
				if (ftpFiles[i].isFile()) {
					list.add(ftpFiles[i].getName());
				}
			}

			return (String[]) list.toArray(new String[0]);
		} finally {
			if (isAcquire)
				SEM.release();
		}
	}

	public FTPFile getFtpFile(String fileName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();

			FTPFile rtn = null;

			FTPFile[] ftpFiles = this.client.listFiles(fileName);

			for (int i = 0; i < ftpFiles.length; i++) {
				if ((ftpFiles[i].isFile()) && (ftpFiles[i].getName().equals(fileName))) {
					rtn = ftpFiles[i];
					break;
				}
			}

			if (rtn == null) {
				throw new Exception("无法在目录:" + this.client.printWorkingDirectory() + "下找到文件:" + fileName);
			}

			return rtn;
		} finally {
			if (isAcquire)
				SEM.release();
		}
	}

	public String[] listDir() throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();

			List list = new ArrayList();

			FTPFile[] ftpFiles = this.client.listFiles();

			for (int i = 0; i < ftpFiles.length; i++) {
				if (ftpFiles[i].isDirectory()) {
					list.add(ftpFiles[i].getName());
				}
			}

			return (String[]) (String[]) list.toArray(new String[0]);
		} finally {
			if (isAcquire)
				SEM.release();
		}
	}

	public String getCurrentWorkingDirectory() throws Exception {
		return this.client.printWorkingDirectory();
	}

	public boolean mkdir(String dir) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();

			boolean bool1 = this.client.makeDirectory(dir);
			return bool1;
		} finally {
			if (isAcquire)
				SEM.release();
		}
	}

	public void changeWorkingDirectory(String dir) throws Exception {
		this.client.changeWorkingDirectory(wrapper(dir));
	}

	public void upload(String remoteFileName, InputStream input, int mode) throws Exception {
		if (mode == 0) {
			this.client.setFileType(2);
		} else if (mode == 1) {
			this.client.setFileType(0);
		} else {
//			ExceptionUtil.throwBusinessException("BAS0000203", "" + mode);
			//不支持的传输模式:
		}
		upload(remoteFileName, input);
	}

	public void upload(String remoteFileName, InputStream input) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();

			this.client.storeFile(wrapper(remoteFileName), input);
		} finally {
			if (isAcquire)
				SEM.release();
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
				if (is != null)
					is.close();
			}
		} finally {
			if (isAcquire)
				SEM.release();
		}
	}

	public void upload(String remoteFileName, String localFileName, int mode) throws Exception {
		if (mode == 0) {
			this.client.setFileType(2);
		} else if (mode == 1) {
			this.client.setFileType(0);
		} else {
//			ExceptionUtil.throwBusinessException("BAS0000203", "" + mode);
		}
		upload(remoteFileName, localFileName);
	}

	public void download(String remoteFileName, OutputStream output, int mode) throws Exception {
		if (mode == 0) {
			this.client.setFileType(2);
		} else if (mode == 1) {
			this.client.setFileType(0);
		} else {
//			ExceptionUtil.throwBusinessException("BAS0000203", "" + mode);
		}
		download(remoteFileName, output);
	}

	public void download(String remoteFileName, OutputStream output) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();

			boolean rtn = this.client.retrieveFile(wrapper(remoteFileName), output);
//			if (!rtn)
//				ExceptionUtil.throwBusinessException("BAS0000205", remoteFileName);
		} finally {
			if (isAcquire)
				SEM.release();
		}
	}

	public void download(String remoteFileName, String localFileName, int mode) throws Exception {
		if (mode == 0) {
			this.client.setFileType(2);
		} else if (mode == 1) {
			this.client.setFileType(0);
		} else {
//			ExceptionUtil.throwBusinessException("BAS0000203", "" + mode);
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
//				if (!rtn)
//					ExceptionUtil.throwBusinessException("BAS0000205", remoteFileName);
			} finally {
				if (os != null)
					os.close();
			}
		} finally {
			if (isAcquire)
				SEM.release();
		}
	}

	public InputStream readRemote(String remoteFileName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();

			InputStream localInputStream = this.client.retrieveFileStream(wrapper(remoteFileName));
			return localInputStream;
		} finally {
			if (isAcquire)
				SEM.release();
		}
	}

	public int getReplay() throws Exception {
		return this.client.getReply();
	}

	public InputStream readRemote(String remoteFileName, int mode) throws Exception {
		if (mode == 0) {
			this.client.setFileType(2);
		} else if (mode == 1) {
			this.client.setFileType(0);
		} else {
//			ExceptionUtil.throwBusinessException("BAS0000203", "" + mode);
		}
		return readRemote(remoteFileName);
	}

	public void rename(String oldRemoteFileName, String newRemoteFileName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();

			this.client.rename(wrapper(oldRemoteFileName), wrapper(newRemoteFileName));
		} finally {
			if (isAcquire)
				SEM.release();
		}
	}

	public void delete(String remoteFileName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();

			boolean rtn = this.client.deleteFile(wrapper(remoteFileName));
//			if (!rtn)
//				ExceptionUtil.throwBusinessException("BAS0000205", remoteFileName);
		} finally {
			if (isAcquire)
				SEM.release();
		}
	}

	public void completePendingCommand() throws Exception {
		this.client.completePendingCommand();
	}

	public void close() throws Exception {
		if (this.client.isConnected())
			this.client.disconnect();
	}

	public OutputStream getOutputStream(String fileName) throws Exception {
		boolean isAcquire = false;
		try {
			isAcquire = SEM.acquire();

			OutputStream localOutputStream = this.client.storeFileStream(wrapper(fileName));
			return localOutputStream;
		} finally {
			if (isAcquire)
				SEM.release();
		}
	}

	public void moveFileToRemoteHisDir(String fileName) throws Exception {
		if (this.client.listFiles(fileName).length == 0) {
//			ExceptionUtil.throwBusinessException("BAS0000201", fileName);
		}

		if (StringUtils.isBlank(this.remotePathHis)) {
//			ExceptionUtil.throwBusinessException("BAS0000202", "remotePathHis");
		}

		StringBuffer newFileName = new StringBuffer();
		newFileName.append(this.remotePathHis);
		newFileName.append("/");
		newFileName.append(fileName);
		rename(fileName, newFileName.toString());
	}

}