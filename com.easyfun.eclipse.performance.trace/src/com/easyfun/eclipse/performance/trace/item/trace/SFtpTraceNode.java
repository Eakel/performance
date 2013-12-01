package com.easyfun.eclipse.performance.trace.item.trace;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;

import com.easyfun.eclipse.common.ui.ftp.FTPBean;
import com.easyfun.eclipse.common.util.TimeUtil;
import com.easyfun.eclipse.performance.trace.SFtpClient;
import com.easyfun.eclipse.performance.trace.TraceUtil;
import com.easyfun.eclipse.performance.trace.builder.TraceBuilder;
import com.easyfun.eclipse.performance.trace.model.AppTrace;
import com.easyfun.eclipse.utils.resource.FileUtil;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.SFTPv3DirectoryEntry;

/**
 * SFTP Trace树节点
 * @author linzhaoming
 *
 * 2011-12-16
 *
 * @param <T>
 */
public class SFtpTraceNode<T> extends TraceNode<T>{
	private SFTPv3DirectoryEntry ftpEntry;
	private SFtpClient ftpClient;
	private FTPBean ftpBean = null;
	
	public SFtpTraceNode(T type, SFtpClient sftpClient, SFTPv3DirectoryEntry ftpEntry, FTPBean ftpBean){
		super(type, ftpEntry.filename);
		this.ftpClient = sftpClient;
		this.ftpEntry = ftpEntry;
		this.ftpBean = ftpBean;
	}
	
	public SFTPv3DirectoryEntry getFTPEnry(){
		return ftpEntry;
	}
	
	public SFtpClient getFTPClient(){
		return ftpClient;
	}
	
	public String getDisplayName() {
		String time = TimeUtil.getLongDisplayTime(ftpEntry.attributes.atime);
		String cost = String.valueOf(TraceUtil.getCostByFileName(ftpEntry.filename));
		String display = "(" + cost + "ms) [" + time + "] (" + FileUtil.getDisplayFileSize(ftpEntry.attributes.size) + ") " + ftpEntry.filename;
		return display;
	}

	public Collection getChildren() {
		try {
			if (ftpEntry.attributes.isDirectory()) {
				return ftpClient.list(ftpEntry.filename);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return super.getChildren();
	}

	public boolean isDirectory() {
		return ftpEntry.attributes.isDirectory();
	}

	public FTPBean getFTPBean() {
		return ftpBean;
	}
	
	/** 从FTP服务器中初始化AppTrace对象*/
	public void initAppTrace() throws Exception{
		SFTPv3DirectoryEntry entry = ftpEntry;
		Connection conn = ftpClient.getConn();
		SCPClient scp = conn.createSCPClient();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		String filePath = "";
		if (ftpBean.getFilePath().endsWith("/")) {
			filePath = ftpBean.getFilePath() + entry.filename;
		} else {
			filePath = ftpBean.getFilePath() + "/" + entry.filename;
		}
		scp.get(filePath, bos);

		ByteArrayInputStream ins = new ByteArrayInputStream(bos.toByteArray());
		AppTrace appTrace = TraceBuilder.parseTraceStream(ins, entry, "SFTP");
		
		setAppTrace(appTrace);
	}
}
