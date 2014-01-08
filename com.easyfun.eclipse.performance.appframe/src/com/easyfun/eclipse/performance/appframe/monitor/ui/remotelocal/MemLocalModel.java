package com.easyfun.eclipse.performance.appframe.monitor.ui.remotelocal;

public class MemLocalModel {
	private String serverId;
	private String name;
	private String hitRate;
	private String size;
	private String currentByteSize;
	private String limitBytes;
	private String hit;
	private String miss;
	private String evit;
	private String overload;
	private String uptime;
	private String bucket;

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHitRate() {
		return hitRate;
	}

	public void setHitRate(String hitRate) {
		this.hitRate = hitRate;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCurrentByteSize() {
		return currentByteSize;
	}

	public void setCurrentByteSize(String currentByteSize) {
		this.currentByteSize = currentByteSize;
	}

	public String getLimitBytes() {
		return limitBytes;
	}

	public void setLimitBytes(String limitBytes) {
		this.limitBytes = limitBytes;
	}

	public String getHit() {
		return hit;
	}

	public void setHit(String hit) {
		this.hit = hit;
	}

	public String getMiss() {
		return miss;
	}

	public void setMiss(String miss) {
		this.miss = miss;
	}

	public String getEvit() {
		return evit;
	}

	public void setEvit(String evit) {
		this.evit = evit;
	}

	public String getOverload() {
		return overload;
	}

	public void setOverload(String overload) {
		this.overload = overload;
	}

	public String getUptime() {
		return uptime;
	}

	public void setUptime(String uptime) {
		this.uptime = uptime;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

}
