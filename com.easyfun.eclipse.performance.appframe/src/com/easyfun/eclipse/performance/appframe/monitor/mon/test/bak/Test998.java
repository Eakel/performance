package com.easyfun.eclipse.performance.appframe.monitor.mon.test.bak;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Test998 {
	public static void main(String[] args) throws Exception {
		String sDBDriver = "oracle.jdbc.driver.OracleDriver";
		String sConnStr = "jdbc:oracle:thin:@10.96.18.42:1521:HNZW2";

		Class.forName(sDBDriver);

		Connection conn = null;
		PreparedStatement ptmt = null;
		PreparedStatement ptmt2 = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(sConnStr, "ams", "ams-m1-123");

			conn.setAutoCommit(false);

			ptmt = conn
					.prepareStatement("SELECT /*+ PARALLEL(t, 4) full(t) */   ACCT_ID,   PRODUCT_INSTANCE_ID,   ACCT_ITEM_TYPE_ID,   BILLING_CYCLE_ID,   ORIGINAL_AMOUNT,   CDR_DISCOUNT,   ACCT_DISCOUNT,   ADJUST_AMOUNT,   RECE_AMOUNT,   REAL_AMOUNT,   OPT_DATE,   CREATED_DATE,   REGION_ID,   PRODUCT_PRICE_ID    FROM ams.ACCT_ITEM_HIS_TMP_F t   WHERE BILLING_CYCLE_ID = 200812");
			ptmt.setFetchSize(20000);
			ptmt2 = conn
					.prepareStatement("insert into yh2  (ACCT_ID,   PRODUCT_INSTANCE_ID,   ACCT_ITEM_TYPE_ID,   BILLING_CYCLE_ID,   ORIGINAL_AMOUNT,   CDR_DISCOUNT,   ACCT_DISCOUNT,   ADJUST_AMOUNT,   RECE_AMOUNT,   REAL_AMOUNT,   OPT_DATE,   CREATED_DATE,   REGION_ID,   PRODUCT_PRICE_ID)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

			rs = ptmt.executeQuery();
			long j = 0L;
			long start0 = System.currentTimeMillis();
			long start = System.currentTimeMillis();
			while (rs.next()) {
				ptmt2.setLong(1, rs.getLong(1));
				ptmt2.setLong(2, rs.getLong(2));
				ptmt2.setLong(3, rs.getLong(3));
				ptmt2.setLong(4, rs.getLong(4));
				ptmt2.setLong(5, rs.getLong(5));
				ptmt2.setLong(6, rs.getLong(6));
				ptmt2.setLong(7, rs.getLong(7));
				ptmt2.setLong(8, rs.getLong(8));
				ptmt2.setLong(9, rs.getLong(9));
				ptmt2.setLong(10, rs.getLong(10));
				ptmt2.setTimestamp(11, rs.getTimestamp(11));
				ptmt2.setTimestamp(12, rs.getTimestamp(12));
				ptmt2.setString(13, rs.getString(13));
				ptmt2.setLong(14, rs.getLong(14));

				ptmt2.addBatch();
				if (j % 20000L == 0L) {
					ptmt2.executeBatch();
					ptmt2.clearParameters();
					ptmt2.clearBatch();
					System.out.println("ºÄÊ±:" + (System.currentTimeMillis() - start) + ":ms");
					start = System.currentTimeMillis();
				}
				j += 1L;
			}

			ptmt2.executeBatch();

			conn.commit();
			System.out.println("×ÜºÄÊ±:" + (System.currentTimeMillis() - start0) + ":ms");
		} catch (Exception ex) {
			conn.rollback();
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt2 != null) {
				ptmt2.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
}