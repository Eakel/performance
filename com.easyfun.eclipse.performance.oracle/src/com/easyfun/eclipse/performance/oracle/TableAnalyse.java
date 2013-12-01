package com.easyfun.eclipse.performance.oracle;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


/**
 * ͳ�Ʊ����
 * @author linzhaoming
 * 2011-3-11
 *
 */
public class TableAnalyse {

	private static final String SCHEMES = "schemes";
	private static final String CONF_FILE_NAME = "configure.properties";
	private static final String FILE_NAME = "fileName";
	private static final String JDBC_PASSWD = "jdbc.passwd";
	private static final String JDBC_USER = "jdbc.user";
	private static final String JDBC_URL = "jdbc.url";
	private static final String JDBC_DRIVER = "jdbc.driver";

	public static void main(String[] args) throws Exception{
		Properties p = new Properties();
		p.load(new FileInputStream(CONF_FILE_NAME));
		String strs = p.getProperty(SCHEMES);			//�û����б���";"�ָ�
		String[] ownerNames = strs.split(";");
		Class.forName(p.getProperty(JDBC_DRIVER));
		Connection conn = DriverManager.getConnection(p.getProperty(JDBC_URL), p.getProperty(JDBC_USER), p.getProperty(JDBC_PASSWD));
		String fileName = p.getProperty(FILE_NAME);	//��������
		boolean isLog = "true".equals(p.getProperty("log"));
		
		HashMap<String, List<TableModel>> map = new HashMap<String, List<TableModel>>();
		HashMap<String, String> filterMap = new HashMap<String, String>();
		long begin = System.currentTimeMillis();
		for (String ownerName : ownerNames) {
			String sql = "SELECT OWNER, TABLE_NAME FROM ALL_TABLES WHERE OWNER IN (?)";
			String filterName = p.getProperty(ownerName.toLowerCase() + ".filter");
			if(filterName!=null && filterName.length()>0){
				sql += " AND TABLE_NAME LIKE '" + filterName + "'";
			}
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setString(1, ownerName.toUpperCase());
			ResultSet rs = pstm.executeQuery();
			List<TableModel> list = new ArrayList<TableModel>();
			map.put(ownerName, list);
			filterMap.put(ownerName, filterName);
			while(rs.next()){
				TableModel model = new TableModel();
				model.setTableName(rs.getString("TABLE_NAME"));
				list.add(model);
			}
			
			List<TableModel> tableNameList = map.get(ownerName);
			PreparedStatement stmt;
			for (TableModel model : tableNameList) {
				String countSql = "select count(*) from " + ownerName + "." + model.getTableName();
				stmt = conn.prepareStatement(countSql);
				stmt.executeQuery(countSql);
				rs = stmt.executeQuery();
				while(rs.next()){							
					model.setCount(rs.getLong(1));
					if(isLog){
						int limit = 0;
						if(args.length >0){
							try {
								limit = Integer.parseInt(args[0]);
							} catch (Exception e) {
								//
							}
						}
						if(model.getCount() >= limit){
							System.out.println(countSql + " [" + model.getCount() + "]");
						}
					}
				}
				stmt.close();
			}		
		}
		
		System.out.println("��ȡ���ݿ�ʱ�䣺" + (System.currentTimeMillis() - begin) + " ms");
		if(fileName != null && fileName.length() >0){
			write2Excel(map, filterMap, fileName);	
		}
		
	}
	
	private static void write2Excel(HashMap<String, List<TableModel>> map, HashMap<String, String> filterMap, String fileName) throws Exception{
		long begin = System.currentTimeMillis();
		WritableWorkbook book = Workbook.createWorkbook(new File(fileName));		

		WritableSheet summarySheet = book.createSheet("�������", 0);	// iΪҳ����
		Label label1 = new Label(0, 0, "�û���");
		summarySheet.addCell(label1);
		Label label2 = new Label(1, 0, "�������");
		summarySheet.addCell(label2);
		Label label3 = new Label(2, 0, "��������");
		summarySheet.addCell(label3);
		
		int beginIndex = 1;
		for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
			String ownerName = (String) iter.next();
			List<TableModel> list = map.get(ownerName);
			Label label = new Label(0, beginIndex, ownerName);
			summarySheet.addCell(label);
			
			Number number = new Number(1, beginIndex, list.size());
			summarySheet.addCell(number);
			
			label = new Label(2, beginIndex, filterMap.get(ownerName));
			summarySheet.addCell(label);
			beginIndex++;
		}
		
		int i=1;
		for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
			String ownerName = (String) iter.next();
			List<TableModel> list = map.get(ownerName);
			WritableSheet sheet = book.createSheet(ownerName, i);	// iΪҳ����
			for(int j=0; j<list.size(); j++){
				TableModel tableModel = list.get(j);
				Label label = new Label(0, j, tableModel.getTableName());
				// ������õĵ�Ԫ����ӵ���������
				sheet.addCell(label);
				jxl.write.Number number = new jxl.write.Number(1, j, tableModel.getCount());
				sheet.addCell(number);
			}	
			i++;
		}
		// д�����ݲ��ر��ļ�
		book.write();
		book.close();
		
		System.out.println("д��Excel�ļ�ʱ��: " + (System.currentTimeMillis() -begin)  + " ms");
	}

}
