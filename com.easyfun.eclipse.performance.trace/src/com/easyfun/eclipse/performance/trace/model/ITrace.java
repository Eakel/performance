package com.easyfun.eclipse.performance.trace.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
/**
 * 5��ʵ����
 * <li>AppTrace
 * <li>DaoTrace
 * <li>JdbcTrace
 * <li>SvrTrace
 * <li>WebTrace
 * <li>BccMemTrace
 * <li>CauTrace
 * <li>HttpTrace
 * <li>SecMemTrace
 * <li>SvrTrace
 * <li>WsTrace
 * @author linzhaoming
 * 
 * Created at 2011-2-16
 */
public interface ITrace extends Serializable {
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	public static final String FILTERGROUP = "Trace";
	
//	/** APP����*/
//	public static final String TYPE_APP = "App";
//	
//	/** WEB����*/
//	public static final String TYPE_WEB = "Web";
//	
//	/** MEM����*/
//	public static final String TYPE_MEM = "Mem";
//	
//	/** Http����*/
//	public static final String TYPE_HTTP = "Http";
//	
//	/** Ws����*/
//	public static final String TYPE_WS = "Ws";
//	
//	/** JDBC����*/
//	public static final String TYPE_JDBC = "Jdbc";
//
//	/** DAO����*/
//	public static final String TYPE_DAO = "Dao";
//	
//	/** SRV����*/
//	public static final String TYPE_SVR = "Svr";
//	
//	/** CAU���� */
//	public static final String TYPE_CAU = "Cau";
//	
//	/** MDB���� */
//	public static final String TYPE_MDB = "Mdb";
//	
//	/** SECMEM���� */
//	public static final String TYPE_SECMEM = "SecMem";
//	
//	/** BCC���� */
//	public static final String TYPE_BCC = "bcc";
	
	public void setId(String id);

	public String getName();

	public void setName(String name);

	public void setCreateTime(String createTime);

	public TraceTypeEnum getType();

	public void setType(TraceTypeEnum type);

	public void addChild(ITrace objITrace);

	public boolean isNode();

	public String getDisplay();
	
	public String getCreateTime();
	
	public boolean isVisible();

	public void setVisible(boolean visible);
}