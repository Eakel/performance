package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;

/**
 * [MON_NODE]
 * @author linzhaoming
 * 
 * Created at 2012-9-26
 */
public class MonNode implements Serializable {
	private static final long serialVersionUID = -900115409198500111L;
	private String state;
	private String nodeImg;
	private String remarks;
	private String nodeType;
	private String url;
	private String name;
	private long nodeId;
	private long treeParentId;
	private long pageId;

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return this.state;
	}

	public void setNodeImg(String nodeImg) {
		this.nodeImg = nodeImg;
	}

	public String getNodeImg() {
		return this.nodeImg;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getNodeType() {
		return this.nodeType;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public long getNodeId() {
		return this.nodeId;
	}

	public void setTreeParentId(long treeParentId) {
		this.treeParentId = treeParentId;
	}

	public void setPageId(long pageId) {
		this.pageId = pageId;
	}

	public long getTreeParentId() {
		return this.treeParentId;
	}

	public long getPageId() {
		return this.pageId;
	}
}