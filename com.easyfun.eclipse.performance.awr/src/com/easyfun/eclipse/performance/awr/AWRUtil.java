package com.easyfun.eclipse.performance.awr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.easyfun.eclipse.performance.awr.model.SnapShot;

public class AWRUtil {
	
	/** 获取数据库的AWR列表*/
	public static List<SnapShot> getSnaps(Connection conn) throws Exception{
		List<SnapShot> snapshot = new ArrayList<SnapShot>();
		
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement("select * from (select a.snap_id, to_char(a.BEGIN_INTERVAL_TIME,'yyyymmddhh24mi') as begin_date,to_char(a.end_interval_time,'yyyymmddhh24mi') as end_date, a.dbid, a.instance_number,c.INSTANCE_NAME,c.VERSION,UTL_INADDR.get_host_name  from dba_hist_snapshot a,v$database b,v$instance c where a.dbid = b.DBID  and a.instance_number = c.INSTANCE_NUMBER order by a.begin_interval_time desc )");
			rs = ptmt.executeQuery();
			while (rs.next()) {
				SnapShot objSnapShot = new SnapShot();
				objSnapShot.setSnapId(rs.getLong("SNAP_ID"));
				objSnapShot.setBeginDate(rs.getString("BEGIN_DATE"));
				objSnapShot.setEndDate(rs.getString("END_DATE"));
				objSnapShot.setDbId(rs.getLong("DBID"));
				objSnapShot.setInstanceNumber(rs.getLong("INSTANCE_NUMBER"));
				objSnapShot.setInstanceName(rs.getString("INSTANCE_NAME"));
				objSnapShot.setVersion(rs.getString("VERSION"));
				objSnapShot.setHostname(rs.getString("GET_HOST_NAME"));
				snapshot.add(objSnapShot);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		
//		Collections.sort(snapshot, new Comparator<SnapShot>() {
//			public int compare(SnapShot o1, SnapShot o2) {
////				return o1.getBeginDate().compareTo(o2.getBeginDate());
//				return o2.getBeginDate().compareTo(o1.getBeginDate());
//			}
//		});
		return snapshot;
	}
	
	/** 产生AWR的HTML报告
	 * <li>0: HTML
	 * <li>1: Text
	 * */
	public static String genAWR(int type, Connection conn, List snapshot, long min, long max, long dbId, long instanceNumber) throws Exception{
		if(type == 0){
			return genAWRHtml(conn, snapshot, min, max, dbId, instanceNumber);
		}else if(type ==1){
			return genAWRText(conn, snapshot, min, max, dbId, instanceNumber);
		}else{
			throw new Exception("未能识别的类型");
		}
	}
	
	
	/** 产生AWR的HTML报告*/
	public static String genAWRHtml(Connection conn, List snapshot, long min, long max, long dbId, long instanceNumber) throws Exception{
		StringBuffer sb = new StringBuffer();

		SnapShot snap = (SnapShot) snapshot.get(0);
		PreparedStatement ptmt = null;
		ResultSet rs = null;

		try {
			// 参数列表：[dbId] [instanceNumber] [startSanpId] [endSanpId]
			ptmt = conn.prepareStatement("select * from table(DBMS_WORKLOAD_REPOSITORY.AWR_REPORT_HTML(?,?,?,?))");
			ptmt.setFetchSize(1000);
			ptmt.setLong(1, snap.getDbId());
			ptmt.setLong(2, snap.getInstanceNumber());
			ptmt.setLong(3, min);
			ptmt.setLong(4, max);
			rs = ptmt.executeQuery();

			while (rs.next()) {
				sb.append(rs.getString(1));
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}

			if (conn != null) {
				conn.close();
			}
		}

		return sb.toString();
	}
	
	public static String genAWRText(Connection conn, List snapshot, long min, long max, long dbId, long instanceNumber) throws Exception{
		StringBuffer sb = new StringBuffer();

		PreparedStatement ptmt = null;
		ResultSet rs = null;

		try {
			// 参数列表：[dbId] [instanceNumber] [startSanpId] [endSanpId]
			ptmt = conn
					.prepareStatement("select * from table(DBMS_WORKLOAD_REPOSITORY.AWR_REPORT_TEXT(?,?,?,?))");
			ptmt.setFetchSize(1000);
			ptmt.setLong(1, dbId);
			ptmt.setLong(2, instanceNumber);
			ptmt.setLong(3, min);
			ptmt.setLong(4, max);
			rs = ptmt.executeQuery();

			while (rs.next()) {
				sb.append(rs.getString(1)).append("\n");
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}

			if (conn != null) {
				conn.close();
			}
		}

		return sb.toString();
	}
}
