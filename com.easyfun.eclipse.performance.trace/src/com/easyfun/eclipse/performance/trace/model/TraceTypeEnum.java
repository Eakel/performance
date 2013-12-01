package com.easyfun.eclipse.performance.trace.model;

public enum TraceTypeEnum {
	/** APP���� */
	TYPE_APP("App"),

	/** WEB���� */
	TYPE_WEB("Web"),

	/** MEM���� */
	TYPE_MEM("Mem"),

	/** Http���� */
	TYPE_HTTP("Http"),

	/** Ws���� */
	TYPE_WS("Ws"),

	/** JDBC���� */
	TYPE_JDBC("Jdbc"),

	/** DAO���� */
	TYPE_DAO("Dao"),

	/** SRV���� */
	TYPE_SVR("Svr"),

	/** CAU���� */
	TYPE_CAU("Cau"),

	/** MDB���� */
	TYPE_MDB("Mdb"),

	/** SECMEM���� */
	TYPE_SECMEM("SecMem"),

	/** BCC���� */
	TYPE_BCC("Bcc");
	
	private String str;
	
	private TraceTypeEnum(String str){
		this.str = str;
	}
	
	public String toString() {
		return str;
	}
	
}
