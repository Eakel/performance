package com.easyfun.eclipse.performance.mysql.analyze.ui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import com.easyfun.eclipse.component.db.ConnectionModel;
import com.easyfun.eclipse.component.db.DBUtil;
import com.easyfun.eclipse.component.kv.KeyValue;
import com.easyfun.eclipse.component.kv.KeyValueTableViewer;
import com.easyfun.eclipse.performance.mysql.analyze.model.TableModel;
import com.easyfun.eclipse.performance.navigator.console.LogHelper;
import com.easyfun.eclipse.performance.preferences.DBUrlPreferencePage;
import com.easyfun.eclipse.performance.preferences.EasyFunPrefUtil;
import com.easyfun.eclipse.performance.preferences.TableFilterPreferencePage;
import com.easyfun.eclipse.rcp.RCPUtil;
import com.easyfun.eclipse.util.StringUtil;
import com.easyfun.eclipse.util.TimeUtil;

public class MySQLView extends ViewPart {
	private static final String SCHEMES = "schemes";

//	private ConnectionModel connectionModel;
	/** 当前过滤设定属性 */
	// private String prop;
	private Properties tablePrefixProp;

	private TabFolder tabFolder;
	
	private static final Log log = LogFactory.getLog(MySQLView.class);

	public MySQLView() {
	}

	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());

		final Composite topComposite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(4, false);
		topComposite.setLayout(gridLayout);

		final Button dbButton = new Button(topComposite, SWT.NONE);
		dbButton.setText("数据库设定");
		dbButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
//				RCPUtil.showPreferencPage(getViewSite().getShell(), MySQLJDBCPreferencePage.PREF_ID);
				RCPUtil.showPreferencPage(getViewSite().getShell(), DBUrlPreferencePage.PREF_ID);
			}
		});

		final Button filterSetttingButton = new Button(topComposite, SWT.NONE);
		filterSetttingButton.setText("过滤设定");
		filterSetttingButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			    RCPUtil.showPreferencPage(getViewSite().getShell(), TableFilterPreferencePage.PREF_ID);
			    
			    try {
					tablePrefixProp = new Properties();
					tablePrefixProp.load(new ByteArrayInputStream(EasyFunPrefUtil.getTableFilter().getBytes()));
					refreshTabByProp(tablePrefixProp);
				} catch (Exception e1) {
					e1.printStackTrace();
					RCPUtil.showError(getViewSite().getShell(), e1.getMessage());
				}
			}
			
		});

		final Button anaButton = new Button(topComposite, SWT.NONE);
		anaButton.setText("开始分析");
		anaButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				anaButton.getDisplay().asyncExec(new Runnable() {
					public void run() {
						ConnectionModel connectionModel = null;
						try {
							anaButton.setEnabled(false);
							connectionModel = DBUtil.getConnectionModel();
							Connection conn = connectionModel.getConnection();
							DatabaseMetaData metaData = conn.getMetaData();
							LogHelper.debug(log, "使用的用户名为:" + metaData.getUserName());
							initByConnection(conn);
						} catch (Exception ex) {
							ex.printStackTrace();
							LogHelper.error(log, "获取表格信息失败", ex);
							RCPUtil.showError(getViewSite().getShell(), "获取表格信息失败\n" + ex.getMessage());
						} finally {
							anaButton.setEnabled(true);
							if(connectionModel != null){
								connectionModel.close();
							}
						}
					}

				});
			}
		});
		
		final Button exportButton = new Button(topComposite, SWT.NONE);
		exportButton.setText("导出为Excel");
		exportButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					exportUIResult(tabFolder);
				} catch (Exception ex) {
					ex.printStackTrace();
					LogHelper.error(log, ex);
				}
			}
		});

		final Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		mainComposite.setLayout(new GridLayout());

		tabFolder = new TabFolder(mainComposite, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		try {
			init();
		} catch (Exception e1) {
			LogHelper.error(log, e1);
			RCPUtil.showError(getViewSite().getShell(), e1.getMessage());
		}
	}

	public void setFocus() {
	}
	
	
	private KeyValueTableViewer createNewTab(TabFolder tabFolder, String tabName, String[] titles, int[] widths){
		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(tabName);

		KeyValueTableViewer tableViewer = new KeyValueTableViewer(tabFolder, titles, widths);
		tabItem.setControl(tableViewer.getTable());
		return tableViewer;
	}
	
	private void initByConnection(Connection conn) throws Exception{
		Properties p = new Properties();
		ByteArrayInputStream bis = new ByteArrayInputStream(EasyFunPrefUtil.getTableFilter().getBytes());
		p.load(bis);
		
		String[] ownerNames = p.getProperty(SCHEMES).split(";");	//用户名列表，用";"分割
		
		HashMap<String, List<TableModel>> ownerTableMap = new HashMap<String, List<TableModel>>();
		HashMap<String, String> filterMap = new HashMap<String, String>();
		long begin = System.currentTimeMillis();
				
		//1、查询表名
		for (String ownerName : ownerNames) {
			String sql = "SELECT TABLE_SCHEMA AS OWNER, TABLE_NAME, ENGINE FROM TABLES WHERE TABLE_SCHEMA IN (?)";
			String filterName = p.getProperty(ownerName.toLowerCase() + ".filter");
			if (filterName != null && filterName.length() > 0) {
				sql += " AND TABLE_NAME LIKE '" + filterName + "'";
			}
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setString(1, ownerName.toUpperCase());
			ResultSet rs = pstm.executeQuery();
			
			List<TableModel> tableList = new ArrayList<TableModel>();
			ownerTableMap.put(ownerName, tableList);
			filterMap.put(ownerName, filterName);
			
			while(rs.next()){
				TableModel model = new TableModel();
				model.setTableName(rs.getString("TABLE_NAME"));
				model.setTableSpace(rs.getString("ENGINE"));
				tableList.add(model);
			}
			
			if(rs != null){
				rs.close();
			}
			
			if(pstm != null){
				pstm.close();
			}
		}
		
		//2、根据用户名，查找所有表的大小
		Set<String> userNameSet = ownerTableMap.keySet();
		for (Iterator<String> iter = userNameSet.iterator(); iter.hasNext();) {
			String ownerName = (String) iter.next();
			List<TableModel> tableList = ownerTableMap.get(ownerName);

			int mode = 2;
			String modeValue = tablePrefixProp.getProperty(ownerName + "." + "mode");
			if(StringUtil.isNumeric(modeValue)){
				mode = Integer.valueOf(modeValue);
			}else{
				LogHelper.error(log, "配置的[" + ownerName + "." + "mode]不为数字或为空，取默认值2");
				mode = 2;
			}
			CountDownLatch latch = new CountDownLatch(mode);
			for (int i = 0; i < mode; i++) {
				TableWorker worker = new TableWorker(ownerName, tableList, mode, i, latch);
				worker.start();
			}
			latch.await();
		}

		LogHelper.info(log, "查询数据库花费时间：" + (System.currentTimeMillis() - begin) + " ms");
		updateResult(ownerTableMap, filterMap);
		
		try {
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	private static class TableWorker extends Thread{
		
		private List<TableModel> tableList;
		
		private String ownerName;
		
//		private Connection conn;
		
		private int mode;
		private int value;
		
		private CountDownLatch latch;
		
		public TableWorker(String ownerName, List<TableModel> tableList, int mode, int value, CountDownLatch latch) throws Exception{			
			this.ownerName = ownerName;
			this.tableList = tableList;
			this.mode= mode;
			this.value = value;
			this.latch = latch;
			this.setName("Table-Thread-[" + ownerName + "-" + mode + "-" + value + "]");
		}
		
		public void run() {
			long t1 = System.currentTimeMillis();
			try {
				for (int i = 0; i < tableList.size(); i++) {
					if (i % mode != value) {
						continue;
					}
					TableModel tableModel = tableList.get(i);
					if (tableModel.getTableName().indexOf("BIN$") != 0) {
						ConnectionModel connectionModel = null;;
						
						String countSql = "select count(*) from " + ownerName + "." + tableModel.getTableName();
						PreparedStatement stmt = null;
						ResultSet rs = null;
						try {
							connectionModel = DBUtil.getConnectionModel();
							Connection conn = connectionModel.getConnection();
							stmt = conn.prepareStatement(countSql);
							rs = stmt.executeQuery(countSql);
							while (rs.next()) {
								tableModel.setCount(rs.getLong(1));
							}
						} catch (Exception e1) {
							// TODO:
							// 10G的垃圾回收表，比如BIN$VjFjj7lXREm5Vc9FWR7BLA==$0，
							// 有字段判断DROPPED=YES
							e1.printStackTrace();
						} finally {
							if (rs != null) {
								rs.close();
							}
							if (stmt != null) {
								stmt.close();
							}

							if (connectionModel != null) {
								connectionModel.close();
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				latch.countDown();
			}
			LogHelper.info(log, "线程" + getName() + "处理的时间：" + (System.currentTimeMillis() - t1) + " ms");
		}
	}
	
	private void updateResult(HashMap<String, List<TableModel>> ownerTableMap, HashMap<String, String> filterMap) throws Exception{
		TabItem[] tabItems = tabFolder.getItems();
		for(int i=1; i<tabItems.length; i++){
			tabItems[i].dispose();
		}
		
		long begin = System.currentTimeMillis();
		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("总体情况");

		//总体情况
		KeyValueTableViewer tableViewer = new KeyValueTableViewer(tabFolder, new String[]{"用户名", "表格数量"});
		tabItem.setControl(tableViewer.getTable());
		List<KeyValue> summaryList = new ArrayList<KeyValue>();
		for (Iterator<String> iter = ownerTableMap.keySet().iterator(); iter.hasNext();) {
			String ownerName = (String) iter.next();
			KeyValue kv = new KeyValue();
			kv.setKey(ownerName);
			List<TableModel> list = ownerTableMap.get(ownerName);
			kv.setValue(String.valueOf(list.size()));
			summaryList.add(kv);
		}
		tableViewer.setInput(summaryList);
		
		for (Iterator<String> iter = ownerTableMap.keySet().iterator(); iter.hasNext();) {
			String ownerName = (String) iter.next();
			List<TableModel> list = ownerTableMap.get(ownerName);
			if(list != null && list.size() >0){
				final TabItem tabItem1 = new TabItem(tabFolder, SWT.NONE);
				tabItem1.setText(ownerName);
	
				KeyValueTableViewer tableViewer1 = new KeyValueTableViewer(tabFolder, new String[]{"表名", "记录数"});
				tabItem1.setControl(tableViewer1.getTable());
				
				List<KeyValue> tt = new ArrayList<KeyValue>();
				for(int j=0; j<list.size(); j++){
					TableModel tableModel = list.get(j);
					KeyValue kv1= new KeyValue();
					kv1.setKey(tableModel.getTableName());
					kv1.setValue(String.valueOf(tableModel.getCount()));
					tt.add(kv1);
				}	
				tableViewer1.setInput(tt);
			}
		}
		tabFolder.setSelection(1);
		LogHelper.info(log, "处理数据库返回结果花费时间: " + (System.currentTimeMillis() -begin)  + " ms");
	}
	
	/**
	 * 将TabFolder的内容导出
	 * @param tabFolder
	 * @throws Exception
	 */
	private void exportUIResult(TabFolder tabFolder) throws Exception{
		String fileName = "MySQL表格分析" + TimeUtil.getYYYYMMDDHHMMSS(new Date()) + ".xls";
		File file = RCPUtil.openSaveDialog(getViewSite().getShell(), new String[]{"*.xls", "*.*"}, fileName);
		if(file == null){
			return;
		}
		long begin = System.currentTimeMillis();
		WritableWorkbook book = Workbook.createWorkbook(file);		
		
		TabItem[] tabItems = tabFolder.getItems();
		for(int i=0; i<tabItems.length; i++){
			TabItem tabIem = tabItems[i];
			Table table = (Table)tabIem.getControl();
			
			TableColumn[] columns = table.getColumns();
			WritableSheet sheet = book.createSheet(tabIem.getText(), i);	// i为页数量
			TableItem[] tableItmes = table.getItems();
			for(int j=0; j<tableItmes.length; j++){
				TableItem tableItem = tableItmes[j];
				for(int k=0; k<columns.length; k++){
					String key = tableItem.getText(k);
					if(StringUtil.isNumber(key)){						
						jxl.write.Number number = new jxl.write.Number(k, j, Integer.valueOf(key));
						sheet.addCell(number);
					}else{
						Label label = new Label(k, j, key);
						sheet.addCell(label);
					}
				}
			}
		}
		// 写入数据并关闭文件
		book.write();
		book.close();
		
		LogHelper.info(log, "写入Excel文件时间: " + (System.currentTimeMillis() -begin)  + " ms");
	}
	
	private void init() throws Exception{
		tablePrefixProp = new Properties();
		tablePrefixProp.load(new ByteArrayInputStream(EasyFunPrefUtil.getTableFilter().getBytes()));
		refreshTabByProp(tablePrefixProp);
	}
	
	private void refreshTabByProp(Properties prop2) throws Exception{
		Set<Object> keySet = prop2.keySet();
		List<KeyValue> list = new ArrayList<KeyValue>();
		for (Iterator<Object> iter = keySet.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			String value = prop2.getProperty(key);
			KeyValue kv = new KeyValue(key, value);
			list.add(kv);
		}
		TabItem[] tabItems = tabFolder.getItems();
		for (int i = 0; i < tabItems.length; i++) {
			tabItems[i].dispose();
		}
		if (tabItems.length == 1) {
			KeyValueTableViewer tableViewer = createNewTab(tabFolder, "过滤设定", new String[] { "配置项", "值" }, new int[] { 150, 450 });
			tableViewer.setInput(list);
		} else {
			KeyValueTableViewer tableViewer = createNewTab(tabFolder, "过滤设定", new String[] { "配置项", "值" }, new int[] { 150, 450 });
			tableViewer.setInput(list);
		}
	}
}