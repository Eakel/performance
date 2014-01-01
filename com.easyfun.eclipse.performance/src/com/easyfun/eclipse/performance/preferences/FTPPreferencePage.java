package com.easyfun.eclipse.performance.preferences;

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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.easyfun.eclipse.component.ftp.FTPHostBean;
import com.easyfun.eclipse.component.ftp.FTPHostDialog;
import com.easyfun.eclipse.component.ftp.FTPUtil;

/**
 * 
 * @author linzhaoming
 *
 * 2013-12-27
 *
 */
public class FTPPreferencePage extends PreferencePage implements IWorkbenchPreferencePage{
	//TODO: 
	//2. 导入/导出到文件

	private Table jdbcTable;
	private TableViewer tableViewer;
	
	public static final String PREF_ID = "com.easyfun.eclipse.performance.preferencePages.FTP";

	public void init(IWorkbench workbench) {
		
	}

	@Override
	protected Control createContents(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
		jdbcTable = new Table(parent, SWT.BORDER|SWT.FULL_SELECTION|SWT.CHECK);
		jdbcTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		jdbcTable.setLinesVisible(true);
		jdbcTable.setHeaderVisible(true);
		
		tableViewer = new TableViewer(jdbcTable);
		tableViewer.setLabelProvider(new FtpTableProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		tableViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event) {
				editSelect();
			}
		});
		
		tableViewer.getTable().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (event.detail == SWT.CHECK) {
					//Uncheck others
					TableItem[] items = tableViewer.getTable().getItems();
					for (TableItem item : items) {
						if(!item.equals(event.item)){
							item.setChecked(false);
						}
					}
				}
			}

		});
		
		TableColumn col = new TableColumn(jdbcTable, SWT.NULL);
		col.setText("Name");
		col.setWidth(100);
		
		col = new TableColumn(jdbcTable, SWT.NULL);
		col.setText("Host");
		col.setWidth(100);
		
		TableColumn co2 = new TableColumn(jdbcTable, SWT.NULL);
		co2.setText("Type");
		co2.setWidth(60);
		
		TableColumn col3 = new TableColumn(jdbcTable, SWT.NULL);
		col3.setText("User");
		col3.setWidth(70);
		
		TableColumn col4 = new TableColumn(jdbcTable, SWT.NULL);
		col4.setText("Port");
		col4.setWidth(70);
		
		Composite c1 = new Composite(parent, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		c1.setLayout(new GridLayout());
		
		Button b1 = new Button(c1, SWT.PUSH);
		b1.setText("&Add...");
		b1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		b1.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				FTPHostDialog dialog = new FTPHostDialog(getShell(), FTPHostDialog.TYPE_ADD, null);
				int result = dialog.open();
				if(result == FTPHostDialog.OK){
					FTPHostBean bean = dialog.getFTPBean();
					List<FTPHostBean> list = (List<FTPHostBean>)tableViewer.getInput();
					list.add(bean);
					tableViewer.setInput(list);
					tableViewer.setSelection(new StructuredSelection(bean));
					
					TableItem[] items = tableViewer.getTable().getItems();
					for (TableItem item : items) {
						if(item.getData().equals(bean)){
							item.setChecked(true);
							break;
						}
					}
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
				if(select == null || !(select instanceof FTPHostBean)){
					return;
				}
				
				List<FTPHostBean> list = (List<FTPHostBean>)tableViewer.getInput();
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
				if(select == null || !(select instanceof FTPHostBean)){
					return;
				}
				
				FTPHostBean selectBean = (FTPHostBean)select;
				FTPHostBean bean = selectBean.copy();
				
				List<FTPHostBean> list = (List<FTPHostBean>)tableViewer.getInput();
				list.add(bean);
				tableViewer.add(bean);
				tableViewer.setSelection(new StructuredSelection(bean));
			}
		});
		
		initData();
		
		return null;
	}
	
	private void editSelect(){
		Object select = ((IStructuredSelection)tableViewer.getSelection()).getFirstElement();
		if(select == null || !(select instanceof FTPHostBean)){
			return;
		}
		FTPHostBean bean = (FTPHostBean)select;
		FTPHostDialog dialog = new FTPHostDialog(getShell(), FTPHostDialog.TYPE_EDIT, bean);
		int result = dialog.open();
		if(result == FTPHostDialog.OK){
			//bean在Diaglog中自动被修改了
			tableViewer.refresh(bean);
		}
	}
	
	private void initData(){
		List<FTPHostBean> list = FTPUtil.getFTPBeans();
		tableViewer.setInput(list);
		
		FTPHostBean selectBean = FTPUtil.getSelectBean();
		
		if(selectBean != null){
			tableViewer.setSelection(new StructuredSelection(selectBean));
			TableItem[] items = tableViewer.getTable().getItems();
			for (TableItem item : items) {
				if(item.getData().equals(selectBean)){
					item.setChecked(true);
					break;
				}
			}
		}
	}
	
	public static class FtpTableProvider extends LabelProvider implements ITableLabelProvider{
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof FTPHostBean) {
				FTPHostBean model = (FTPHostBean)element;
				switch (columnIndex) {
				case 0:
					return model.getName();
				case 1:
					return model.getHost(); 
				case 2:
					switch (model.getFtpType()) {
						case FTPHostBean.TYPE_FTP:
							return "FTP";
						case FTPHostBean.TYPE_SFTP:
							return "SFTP";
						default:
							return "Unknown";
						}
				case 3:
					return model.getUsername();
				case 4:
					return "" + model.getPort();
				default:
					return model.toString();
				}
			} else {
				return element.toString();
			}
		}
	}
	
	public boolean performOk() {
		List<FTPHostBean> list = (List<FTPHostBean>)tableViewer.getInput();
		
		try {
			FTPUtil.saveFTPBeans(list);
			
			TableItem selectItem = null;			
			TableItem[] items = tableViewer.getTable().getItems();
			for (TableItem item : items) {
				if(item.getChecked()){
					selectItem = item;
					break;
				}
			}
			
			if(selectItem != null){
				FTPHostBean bean = (FTPHostBean)selectItem.getData();	
				FTPUtil.saveSelectUrlBean(bean.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.performOk();
	}
	
}
