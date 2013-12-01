package com.easyfun.eclipse.performance.trace.model;

import java.io.Serializable;

public class PtmtParam implements Serializable {
	public String type = null;
	public String value = null;

	public PtmtParam(String type, String value) {
		this.type = type;
		this.value = value;
	}
}