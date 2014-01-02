package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

public class MonPThreshold implements Serializable {
	private static final long serialVersionUID = -300115409198375105L;
	private long thresholdId;
	private String expr1;
	private String expr2;
	private String expr3;
	private String expr4;
	private String expr5;
	private String expr6;
	private String expr7;
	private String expr8;

	public void setThresholdId(long thresholdId) {
		this.thresholdId = thresholdId;
	}

	public void setExpr1(String expr1) {
		this.expr1 = expr1;
	}

	public void setExpr2(String expr2) {
		this.expr2 = expr2;
	}

	public void setExpr3(String expr3) {
		this.expr3 = expr3;
	}

	public void setExpr4(String expr4) {
		this.expr4 = expr4;
	}

	public void setExpr5(String expr5) {
		this.expr5 = expr5;
	}

	public void setExpr6(String expr6) {
		this.expr6 = expr6;
	}

	public void setExpr7(String expr7) {
		this.expr7 = expr7;
	}

	public void setExpr8(String expr8) {
		this.expr8 = expr8;
	}

	public long getThresholdId() {
		return this.thresholdId;
	}

	public String getExpr1() {
		return this.expr1;
	}

	public String getExpr2() {
		return this.expr2;
	}

	public String getExpr3() {
		return this.expr3;
	}

	public String getExpr4() {
		return this.expr4;
	}

	public String getExpr5() {
		return this.expr5;
	}

	public String getExpr6() {
		return this.expr6;
	}

	public String getExpr7() {
		return this.expr7;
	}

	public String getExpr8() {
		return this.expr8;
	}

	public String getAllExpr() {
		StringBuffer sb = new StringBuffer();
		if (!StringUtils.isBlank(this.expr1)) {
			sb.append(this.expr1);
		}
		if (!StringUtils.isBlank(this.expr2)) {
			sb.append(this.expr2);
		}
		if (!StringUtils.isBlank(this.expr3)) {
			sb.append(this.expr3);
		}
		if (!StringUtils.isBlank(this.expr4)) {
			sb.append(this.expr4);
		}
		if (!StringUtils.isBlank(this.expr5)) {
			sb.append(this.expr5);
		}
		if (!StringUtils.isBlank(this.expr6)) {
			sb.append(this.expr6);
		}
		if (!StringUtils.isBlank(this.expr7)) {
			sb.append(this.expr7);
		}
		if (!StringUtils.isBlank(this.expr8)) {
			sb.append(this.expr8);
		}
		return sb.toString();
	}
}