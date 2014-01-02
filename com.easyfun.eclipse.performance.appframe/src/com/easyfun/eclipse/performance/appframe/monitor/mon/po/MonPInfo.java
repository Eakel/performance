package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;

/**
 * ´ú±í[MON_P_INFO]¼ÇÂ¼
 * @author linzm
 * 
 * Created at 2012-9-20
 */
public class MonPInfo implements Serializable {
	private static final long serialVersionUID = -9001191409198359103L;
	/** [MON_P_INFO.INFO_ID]*/
	private long infoId;
	private String name;
	/** [M_P_TIME.EXPR]*/
	private String expr;
	private String hostname;
	private long typeId;
	private long thresholdId;
	private long splitRuleId;
	private long timeId;
	/** TYPE×Ö¶Î*/
	private String type;
	private String busiArea;
	private long grpId;

	/** [M_P_TIME.EXPR]*/
	public String getExpr() {
		return this.expr;
	}

	public long getTypeId() {
		return this.typeId;
	}

	public String getHostname() {
		return this.hostname;
	}

	public String getName() {
		return this.name;
	}

	/** [MON_P_INFO.INFO_ID]*/
	public long getInfoId() {
		return this.infoId;
	}

	public long getThresholdId() {
		return this.thresholdId;
	}

	public long getSplitRuleId() {
		return this.splitRuleId;
	}

	public long getTimeId() {
		return this.timeId;
	}

	/** TYPE×Ö¶Î*/
	public String getType() {
		return this.type;
	}

	public String getBusiArea() {
		return this.busiArea;
	}

	public long getGrpId() {
		return this.grpId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	/** [M_P_TIME.EXPR]*/
	public void setExpr(String expr) {
		this.expr = expr;
	}

	/** [MON_P_INFO.INFO_ID]*/
	public void setInfoId(long infoId) {
		this.infoId = infoId;
	}

	public void setThresholdId(long thresholdId) {
		this.thresholdId = thresholdId;
	}

	public void setSplitRuleId(long splitRuleId) {
		this.splitRuleId = splitRuleId;
	}

	public void setTimeId(long timeId) {
		this.timeId = timeId;
	}

	/** TYPE×Ö¶Î*/
	public void setType(String type) {
		this.type = type;
	}

	public void setBusiArea(String busiArea) {
		this.busiArea = busiArea;
	}

	public void setGrpId(long grpId) {
		this.grpId = grpId;
	}
}