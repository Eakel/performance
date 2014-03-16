package com.easyfun.eclipse.component.sql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableColumn;

import com.easyfun.eclipse.component.sql.ui.DBContentProvider;
import com.easyfun.eclipse.component.sql.ui.DBViewLabelProvider;
import com.easyfun.eclipse.rcp.ColumnViewerSorter;
import com.easyfun.eclipse.rcp.RCPUtil;


public class DynamicTable {
	static Button button = null;
	static StyledText sqlText = null;
	static TableViewer tableViewer = null;
	
	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		
		SashForm composite = new SashForm(shell, SWT.HORIZONTAL);
		composite.setLayout(new GridLayout(2, false));
		
		Composite c1 = new Composite(composite, SWT.NULL);
		GridData gridData1 = new GridData(GridData.FILL_VERTICAL);
		gridData1.widthHint = 200;
		c1.setLayoutData(gridData1);
		c1.setLayout(new GridLayout());
		
		TreeViewer	viewer = new TreeViewer(c1, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.setContentProvider(new DBContentProvider());
		viewer.setLabelProvider(new DBViewLabelProvider());
		viewer.setInput(TreeHelper.getNavigator());
		
		
		Composite c2 = new Composite(composite, SWT.NULL);
		c2.setLayoutData(new GridData(GridData.FILL_BOTH));
		c2.setLayout(new GridLayout(2, false));
		
		sqlText = new StyledText(c2, SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL|SWT.WRAP);
		sqlText.addLineStyleListener(new SQLSegmentLineStyleListener());
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 40;
		sqlText.setLayoutData(gridData);
		sqlText.setText("select * from information_schema.global_status");
		
		Button button = new Button(c2, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("Query");
		
		
		TabFolder tabFolder = new TabFolder(c2, SWT.NULL);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		tabFolder.setLayoutData(gridData);
		tabFolder.setLayout(new GridLayout(1, false));
		
		TabItem tabItem1 = new TabItem(tabFolder, SWT.NULL);
		tabItem1.setText("Result");
		tabItem1.setControl(createResultComposite(tabFolder));
		
		TabItem tabItem2 = new TabItem(tabFolder, SWT.NULL);
		tabItem2.setText("Test");
		
		final Label errLabel = new Label(c2, SWT.NULL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		errLabel.setLayoutData(gridData);
		RCPUtil.setBold(errLabel);
		errLabel.setText("");
		
		composite.setWeights(new int[] { 1, 4 });
		
		shell.setSize(600, 300);
		shell.open();
		
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				try {
					TableColumn[] columns = tableViewer.getTable().getColumns();
					for (TableColumn col : columns) {
						col.dispose();
					}
					
					createByRS(tableViewer, sqlText.getText().trim());
				} catch (Exception e1) {
					e1.printStackTrace();
					errLabel.setText(e1.getMessage());
				}
			}
		});
		

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		

		display.dispose();
	}
	
	private static Composite createResultComposite(Composite comp){
		Composite parent = new Composite(comp, SWT.NULL);
		parent.setLayout(new GridLayout(1, false));
		
		
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.BORDER);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		tableViewer.getTable().setLayoutData(gridData);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		
		return parent;
	}
	
	private static void createByRS(TableViewer v, String sql) throws Exception{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		List<Map<String, String>> resultList;
		try {
			conn = DBHelper.getConnection();
			stmt = conn.prepareStatement(sql);	
			rs = stmt.executeQuery();
			
			
			ResultSetMetaData metaData = rs.getMetaData();
			int count = metaData.getColumnCount();
			
			List<String> colNames = new ArrayList<String>();
			for(int i=1; i<=count; i++){
				final String colName = metaData.getColumnName(i);
				TableViewerColumn vCol = new TableViewerColumn(v, SWT.NULL);
				vCol.getColumn().setText(colName);
				int prec = metaData.getPrecision(i);
				if(prec <50){
					prec = 50;
				}
				if(prec >200){
					prec = 200;
				}
				vCol.getColumn().setWidth( prec);
				colNames.add(colName);
				
				vCol.setLabelProvider(new CellLabelProvider(){
					public void update(ViewerCell cell) {
						Map<String, String> map = (Map<String, String>)cell.getElement();
						if(map.get(colName) == null){
							cell.setText("");
						}else{
							cell.setText(map.get(colName).toString());
						}
					}
				});
				
				new ColumnViewerSorter(v, vCol){
					protected int doCompare(Viewer viewer, Object e1, Object e2) {
						Map<String, String> map1 = (Map<String, String>)e1;
						Map<String, String> map2 = (Map<String, String>)e2;
						String value1 = map1.get(colName);
						if(value1 == null){
							value1 = "";
						}
						String value2 = map2.get(colName);
						if(value2 == null){
							value2 = "";
						}
						return value1.compareTo(value2);
					}
				};
			}
			
			resultList = new ArrayList<Map<String, String>>();
			while(rs.next()){
				Map<String, String> map = new HashMap<String, String>();
				for (String key : colNames) {
					map.put(key, rs.getString(key));
				}
				
				resultList.add(map);
			}
			v.setInput(resultList);
		} catch (Exception e) {
			throw e;
		}finally{
			if(rs != null){
				rs.close();
			}
			if(stmt != null){
				stmt.close();
			}
			if(conn != null){
				conn.close();
			}
		}
	}
	


}
