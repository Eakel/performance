package com.easyfun.eclipse.performance.appframe.monitor.ui.remotecache;

public class MemCacheModel {
	/** �����Ͷ˿� */
	private String serverHost;
	/** ��ǰ������ */
	private String currConns;
	/** �ܲ�ѯ���� */
	private String cmdGet;
	/** �ڴ�����ֽ��� */
	private String bytes;
	/** ��ǰ�������� */
	private String currItems;
	/** ���ж������� */
	private String totalItems;
	/** ���ö������ */
	private String cmdSet;
	/** ����������� */
	private String evictions;
	/** uptime */
	private String uptime;
	/** hint��ѯ���� */
	private String getHint;
	/** �����ڴ��ֽ��� */
	private String limitMaxbytes;
	/** ����ֽ��� */
	private String bytesWritten;
	/** �����ֽ��� */
	private String bytesRead;

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public String getCurrConns() {
		return currConns;
	}

	public void setCurrConns(String currConns) {
		this.currConns = currConns;
	}

	public String getCmdGet() {
		return cmdGet;
	}

	public void setCmdGet(String cmdGet) {
		this.cmdGet = cmdGet;
	}

	public String getBytes() {
		return bytes;
	}

	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

	public String getCurrItems() {
		return currItems;
	}

	public void setCurrItems(String currItems) {
		this.currItems = currItems;
	}

	public String getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(String totalItems) {
		this.totalItems = totalItems;
	}

	public String getCmdSet() {
		return cmdSet;
	}

	public void setCmdSet(String cmdSet) {
		this.cmdSet = cmdSet;
	}

	public String getEvictions() {
		return evictions;
	}

	public void setEvictions(String evictions) {
		this.evictions = evictions;
	}

	public String getUptime() {
		return uptime;
	}

	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

	public String getGetHint() {
		return getHint;
	}

	public void setGetHint(String getHint) {
		this.getHint = getHint;
	}

	public String getLimitMaxbytes() {
		return limitMaxbytes;
	}

	public void setLimitMaxbytes(String limitMaxbytes) {
		this.limitMaxbytes = limitMaxbytes;
	}

	public String getBytesWritten() {
		return bytesWritten;
	}

	public void setBytesWritten(String bytesWritten) {
		this.bytesWritten = bytesWritten;
	}

	public String getBytesRead() {
		return bytesRead;
	}

	public void setBytesRead(String bytesRead) {
		this.bytesRead = bytesRead;
	}

}
