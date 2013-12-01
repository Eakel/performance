package com.easyfun.eclipse.performance.trace.model;

public enum TraceTypeEnum {
	/** APP类型 */
	TYPE_APP("App"),

	/** WEB类型 */
	TYPE_WEB("Web"),

	/** MEM类型 */
	TYPE_MEM("Mem"),

	/** Http类型 */
	TYPE_HTTP("Http"),

	/** Ws类型 */
	TYPE_WS("Ws"),

	/** JDBC类型 */
	TYPE_JDBC("Jdbc"),

	/** DAO类型 */
	TYPE_DAO("Dao"),

	/** SRV类型 */
	TYPE_SVR("Svr"),

	/** CAU类型 */
	TYPE_CAU("Cau"),

	/** MDB类型 */
	TYPE_MDB("Mdb"),

	/** SECMEM类型 */
	TYPE_SECMEM("SecMem"),

	/** BCC类型 */
	TYPE_BCC("Bcc");
	
	private String str;
	
	private TraceTypeEnum(String str){
		this.str = str;
	}
	
	public String toString() {
		return str;
	}
	
}
