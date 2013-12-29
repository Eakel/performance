package com.easyfun.eclipse.performance.oracle;

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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.internal.dialogs.WorkbenchPreferenceDialog;
import org.eclipse.ui.part.ViewPart;

import com.easyfun.eclipse.component.db.ConnectionModel;
import com.easyfun.eclipse.component.kv.KeyValue;
import com.easyfun.eclipse.component.kv.KeyValueTableViewer;
import com.easyfun.eclipse.performance.navigator.console.LogHelper;
import com.easyfun.eclipse.performance.oracle.preferences.OracleJDBCPreferencePage;
import com.easyfun.eclipse.performance.oracle.preferences.OraclePrefUtil;
import com.easyfun.eclipse.performance.oracle.preferences.OracleTableFilterPreferencePage;
import com.easyfun.eclipse.rcp.RCPUtil;
import com.easyfun.eclipse.util.StringUtil;
import com.easyfun.eclipse.util.TimeUtil;

/**
 * @author linzhaoming
 *
 * 2011-5-8
 *
 */
public class OracleTableView extends ViewPart {
	private static final String SCHEMES = "schemes";
	
	private ConnectionModel connectionModel;
	/** ��ǰ�����趨����*/
//	private String prop;
	
	private Properties tablePrefixProp;
	
	private TabFolder tabFolder;

	public OracleTableView() {
	}

	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());

		final Composite topComposite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(4, false);
		topComposite.setLayout(gridLayout);

		final Button dbButton = new Button(topComposite, SWT.NONE);
		dbButton.setText("���ݿ��趨");
		dbButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				WorkbenchPreferenceDialog dialog = WorkbenchPreferenceDialog.createDialogOn(getShell(), OracleJDBCPreferencePage.PREF_ID);
			    dialog.showOnly(new String[] { OracleTableFilterPreferencePage.ID });
			    dialog.open();
			}
		});

		final Button filterSetttingButton = new Button(topComposite, SWT.NONE);
		filterSetttingButton.setText("�����趨");
		filterSetttingButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				WorkbenchPreferenceDialog dialog = WorkbenchPreferenceDialog.createDialogOn(getShell(), OracleTableFilterPreferencePage.ID);
			    dialog.showOnly(new String[] { OracleTableFilterPreferencePage.ID });
			    dialog.open();
			    
			    try {
					tablePrefixProp = new Properties();
					tablePrefixProp.load(new ByteArrayInputStream(OraclePrefUtil.getTableFilter().getBytes()));
					refreshTabByProp(tablePrefixProp);
				} catch (Exception e1) {
					e1.printStackTrace();
					RCPUtil.showError(getShell(), e1.getMessage());
				}
			}
			
		});

		final Button anaButton = new Button(topComposite, SWT.NONE);
		anaButton.setText("��ʼ����");
		anaButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
					anaButton.getDisplay().asyncExec(new Runnable() {
						public void run() {
							try {
								anaButton.setEnabled(false);
								connectionModel = OraclePrefUtil.getConnectionModel();
								Connection conn = connectionModel.getRefreshConnection();
								DatabaseMetaData metaData = conn.getMetaData();
								LogHelper.debug("ʹ�õ��û���Ϊ:" + metaData.getUserName());
								initByConnection(conn);
							} catch (Exception ex) {
								ex.printStackTrace();
								LogHelper.error("��ȡ�����Ϣʧ��", ex);
								RCPUtil.showError(getShell(), "��ȡ�����Ϣʧ��\n" + ex.getMessage());
							}finally{
								anaButton.setEnabled(true);
							}
						}

					});
			}
		});
		
		final Button exportButton = new Button(topComposite, SWT.NONE);
		exportButton.setText("����ΪExcel");
		exportButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					exportUIResult(tabFolder);
				} catch (Exception ex) {
					ex.printStackTrace();
					LogHelper.error(ex);
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
			e1.printStackTrace();
			LogHelper.error(e1);
			RCPUtil.showError(getShell(), e1.getMessage());
		}
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
		ByteArrayInputStream bis = new ByteArrayInputStream(OraclePrefUtil.getTableFilter().getBytes());
		p.load(bis);
		
		String[] ownerNames = p.getProperty(SCHEMES).split(";");	//�û����б���";"�ָ�
		
		HashMap<String, List<TableModel>> ownerTableMap = new HashMap<String, List<TableModel>>();
		HashMap<String, String> filterMap = new HashMap<String, String>();
		long begin = System.currentTimeMillis();
				
		//1����ѯ����
		for (String ownerName : ownerNames) {
			String sql = "SELECT OWNER, TABLE_NAME, TABLESPACE_NAME FROM ALL_TABLES WHERE OWNER IN (?)";
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
				model.setTableSpace(rs.getString("TABLESPACE_NAME"));
				tableList.add(model);
			}
			
			if(rs != null){
				rs.close();
			}
			
			if(pstm != null){
				pstm.close();
			}
		}
		
		//2�������û������������б�Ĵ�С
		Set<String> userNameSet = ownerTableMap.keySet();
		for (Iterator iter = userNameSet.iterator(); iter.hasNext();) {
			String ownerName = (String) iter.next();
			List<TableModel> tableList = ownerTableMap.get(ownerName);

			int mode = 2;
			String modeValue = tablePrefixProp.getProperty(ownerName + "." + "mode");
			if(StringUtil.isNumeric(modeValue)){
				mode = Integer.valueOf(modeValue);
			}else{
				LogHelper.error("���õ�[" + ownerName + "." + "mode]��Ϊ���ֻ�Ϊ�գ�ȡĬ��ֵ2");
				mode = 2;
			}
			CountDownLatch latch = new CountDownLatch(mode);
			for (int i = 0; i < mode; i++) {
				TableWorker worker = new TableWorker(connectionModel, ownerName, tableList, mode, i, latch);
				worker.start();
			}
			latch.await();
		}

		System.out.println("��ѯ���ݿ⻨��ʱ�䣺" + (System.currentTimeMillis() - begin) + " ms");
		LogHelper.info("��ѯ���ݿ⻨��ʱ�䣺" + (System.currentTimeMillis() - begin) + " ms");
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
		
		private Connection conn;
		
		private int mode;
		private int value;
		
		private CountDownLatch latch;
		
		public TableWorker(ConnectionModel connectionModel, String ownerName, List<TableModel> tableList, int mode, int value, CountDownLatch latch) throws Exception{			
			this.conn = connectionModel.getNewConnection();
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
						String countSql = "select count(*) from " + ownerName + "." + tableModel.getTableName();
						PreparedStatement stmt = null;
						ResultSet rs = null;
						try {
							stmt = conn.prepareStatement(countSql);
							rs = stmt.executeQuery(countSql);
							while (rs.next()) {
								tableModel.setCount(rs.getLong(1));
							}
						} catch (Exception e1) {
							// TODO:
							// 10G���������ձ�����BIN$VjFjj7lXREm5Vc9FWR7BLA==$0��
							// ���ֶ��ж�DROPPED=YES
							e1.printStackTrace();
						} finally {
							if (rs != null) {
								rs.close();
							}
							if (stmt != null) {
								stmt.close();
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				latch.countDown();
				try {
					if(conn != null){
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
					LogHelper.error(e);
				}
			}
			System.out.println("�߳�" + getName() + "�����ʱ�䣺" + (System.currentTimeMillis() - t1) + " ms");
			LogHelper.info("�߳�" + getName() + "�����ʱ�䣺" + (System.currentTimeMillis() - t1) + " ms");
		}
	}
	
	private void updateResult(HashMap<String, List<TableModel>> ownerTableMap, HashMap<String, String> filterMap) throws Exception{
		TabItem[] tabItems = tabFolder.getItems();
		for(int i=1; i<tabItems.length; i++){
			tabItems[i].dispose();
		}
		
		long begin = System.currentTimeMillis();
		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("�������");

		//�������
		KeyValueTableViewer tableViewer = new KeyValueTableViewer(tabFolder, new String[]{"�û���", "�������"});
		tabItem.setControl(tableViewer.getTable());
		List summaryList = new ArrayList();
		for (Iterator iter = ownerTableMap.keySet().iterator(); iter.hasNext();) {
			String ownerName = (String) iter.next();
			KeyValue kv = new KeyValue();
			kv.setKey(ownerName);
			List<TableModel> list = ownerTableMap.get(ownerName);
			kv.setValue(String.valueOf(list.size()));
			summaryList.add(kv);
		}
		tableViewer.setInput(summaryList);
		
		for (Iterator iter = ownerTableMap.keySet().iterator(); iter.hasNext();) {
			String ownerName = (String) iter.next();
			List<TableModel> list = ownerTableMap.get(ownerName);
			if(list != null && list.size() >0){
				final TabItem tabItem1 = new TabItem(tabFolder, SWT.NONE);
				tabItem1.setText(ownerName);
	
				KeyValueTableViewer tableViewer1 = new KeyValueTableViewer(tabFolder, new String[]{"����", "��¼��"});
				tabItem1.setControl(tableViewer1.getTable());
				
				List tt = new ArrayList();
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
		System.out.println("�������ݿⷵ�ؽ������ʱ��: " + (System.currentTimeMillis() -begin)  + " ms");
		LogHelper.info("�������ݿⷵ�ؽ������ʱ��: " + (System.currentTimeMillis() -begin)  + " ms");
	}
	
	/**
	 * ��TabFolder�����ݵ���
	 * @param tabFolder
	 * @throws Exception
	 */
	private void exportUIResult(TabFolder tabFolder) throws Exception{
		String fileName = "MySQL������" + TimeUtil.getYYYYMMDDHHMMSS(new Date()) + ".xls";
		File file = RCPUtil.openSaveDialog(getShell(), new String[]{"*.xls", "*.*"}, fileName);
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
			WritableSheet sheet = book.createSheet(tabIem.getText(), i);	// iΪҳ����
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
		// д�����ݲ��ر��ļ�
		book.write();
		book.close();
		
		System.out.println("д��Excel�ļ�ʱ��: " + (System.currentTimeMillis() -begin)  + " ms");
		LogHelper.info("д��Excel�ļ�ʱ��: " + (System.currentTimeMillis() -begin)  + " ms");
	}
	
	private void init() throws Exception{
		tablePrefixProp = new Properties();
		tablePrefixProp.load(new ByteArrayInputStream(OraclePrefUtil.getTableFilter().getBytes()));
		refreshTabByProp(tablePrefixProp);
	}
	
	private void refreshTabByProp(Properties prop2) throws Exception{
		Set<Object> keySet = prop2.keySet();
		List list = new ArrayList();
		for (Iterator iter = keySet.iterator(); iter.hasNext();) {
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
			KeyValueTableViewer tableViewer = createNewTab(tabFolder, "�����趨", new String[] { "������", "ֵ" }, new int[] { 150, 450 });
			tableViewer.setInput(list);
		} else {
			KeyValueTableViewer tableViewer = createNewTab(tabFolder, "�����趨", new String[] { "������", "ֵ" }, new int[] { 150, 450 });
			tableViewer.setInput(list);
		}
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	private Shell getShell(){
		return getSite().getShell();
	}

}