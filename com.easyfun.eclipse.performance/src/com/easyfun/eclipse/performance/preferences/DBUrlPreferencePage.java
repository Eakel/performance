package com.easyfun.eclipse.performance.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.easyfun.eclipse.common.ui.db.DBTypeEnum;
import com.easyfun.eclipse.common.ui.db.DBUrlDialog;
import com.easyfun.eclipse.common.ui.db.DBUrlBean;

/**
 * 
 * @author linzhaoming
 *
 * 2013-12-27
 *
 */
public class DBUrlPreferencePage extends PreferencePage implements IWorkbenchPreferencePage{
	//TODO: 
	//1. 序列化保存
	//2. 导入/导出到文件
	//3. 数据库开始替换已有的Oralce与MySQL

	private Table jdbcTable;
	private TableViewer tableViewer;

	public void init(IWorkbench workbench) {
		
	}

	@Override
	protected Control createContents(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		jdbcTable = new Table(parent, SWT.BORDER|SWT.FULL_SELECTION);
		jdbcTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		jdbcTable.setLinesVisible(true);
		jdbcTable.setHeaderVisible(true);
		
		tableViewer = new TableViewer(jdbcTable);
		tableViewer.setLabelProvider(new DBUrlTableProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		tableViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event) {
				editSelect();
			}
		});
		
		TableColumn col = new TableColumn(jdbcTable, SWT.NULL);
		col.setText("Name");
		col.setWidth(100);
		
		col = new TableColumn(jdbcTable, SWT.NULL);
		col.setText("URL");
		col.setWidth(100);
		
		TableColumn co2 = new TableColumn(jdbcTable, SWT.NULL);
		co2.setText("Type");
		co2.setWidth(60);
		
		TableColumn col3 = new TableColumn(jdbcTable, SWT.NULL);
		col3.setText("User");
		col3.setWidth(70);
		
		TableColumn col4 = new TableColumn(jdbcTable, SWT.NULL);
		col4.setText("Password");
		col4.setWidth(70);
		
		Composite c1 = new Composite(parent, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		c1.setLayout(new GridLayout());
		
		Button b1 = new Button(c1, SWT.PUSH);
		b1.setText("&Add...");
		b1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		b1.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				DBUrlDialog dialog = new DBUrlDialog(getShell(), DBUrlDialog.TYPE_ADD, null);
				int result = dialog.open();
				if(result == DBUrlDialog.OK){
					DBUrlBean bean = dialog.getCreateBean();
					List<DBUrlBean> list = (List<DBUrlBean>)tableViewer.getInput();
					list.add(bean);
					tableViewer.setInput(list);
					tableViewer.setSelection(new StructuredSelection(bean));
				}
			}
		});
		
		b1 = new Button(c1, SWT.PUSH);
		b1.setText("&Edit...");
		b1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		b1.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				editSelect();
			}
		});
		
		b1 = new Button(c1, SWT.PUSH);
		b1.setText("&Remove");
		b1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		b1.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				Object select = ((IStructuredSelection)tableViewer.getSelection()).getFirstElement();
				if(select == null || !(select instanceof DBUrlBean)){
					return;
				}
				
				List<DBUrlBean> list = (List<DBUrlBean>)tableViewer.getInput();
				list.remove(select);
				tableViewer.setInput(list);
				tableViewer.setSelection(null);
			}
		});
		
		b1 = new Button(c1, SWT.PUSH);
		b1.setText("&Copy");
		b1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		b1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		b1.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				Object select = ((IStructuredSelection)tableViewer.getSelection()).getFirstElement();
				if(select == null || !(select instanceof DBUrlBean)){
					return;
				}
				
				DBUrlBean selectBean = (DBUrlBean)select;
				DBUrlBean bean = selectBean.copy();
				
				List<DBUrlBean> list = (List<DBUrlBean>)tableViewer.getInput();
				list.add(bean);
				tableViewer.add(bean);
				tableViewer.setSelection(new StructuredSelection(bean));
			}
		});
		
		fillData();
		
		return null;
	}
	
	private void editSelect(){
		Object select = ((IStructuredSelection)tableViewer.getSelection()).getFirstElement();
		if(select == null || !(select instanceof DBUrlBean)){
			return;
		}
		DBUrlBean bean = (DBUrlBean)select;
		DBUrlDialog dialog = new DBUrlDialog(getShell(), DBUrlDialog.TYPE_EDIT, bean);
		int result = dialog.open();
		if(result == DBUrlDialog.OK){
			//bean在Diaglog中自动被修改了
			tableViewer.refresh(bean);
		}
	}
	
	private void fillData(){
		List<DBUrlBean> list = new ArrayList<DBUrlBean>();
		//TODO: 硬编码
		DBUrlBean bean = new DBUrlBean(DBTypeEnum.Oracle, "jdbc:oracle:thin:@127.0.0.1:1521:orcl", "oracle.jdbc.driver.OracleDriver", "scotttt", "tiger");
		bean.setName("linzm");
		list.add(bean);
		tableViewer.setInput(list);
	}
	
	public static class DBUrlTableProvider extends LabelProvider implements ITableLabelProvider{
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof DBUrlBean) {
				DBUrlBean model = (DBUrlBean)element;
				switch (columnIndex) {
				case 0:
					return model.getName();
				case 1:
					return model.getUrl();
				case 2:
					return model.getDBType().toString();
				case 3:
					return model.getUsername().toString();
				case 4:
					return model.getPassword().toString();
				default:
					return model.toString();
				}
			} else {
				return element.toString();
			}
		}
	}
	
}
