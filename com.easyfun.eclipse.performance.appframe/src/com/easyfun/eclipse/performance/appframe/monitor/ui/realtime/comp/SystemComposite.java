package com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.ai.appframe2.complex.mbean.standard.system.SystemMonitorMBean;
import com.easyfun.eclipse.component.kv.KeyValue;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.rcp.ColumnViewerSorter;
import com.easyfun.eclipse.rcp.RCPUtil;

/**
 * AppFrame System
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-4
 */
public class SystemComposite extends Composite implements IDataInit{
	private static Log log = LogFactory.getLog(SystemComposite.class);

	private TableViewer tableViewer;
	
	private InvokeModel model;

	private Text conditionText;
	
	public SystemComposite(Composite parent) {
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
	}
	
	public void initData(InvokeModel model) throws Exception{
		this.model = model;
	}
	
	private void initControl(Composite parent){
		Composite top = new Composite(parent, SWT.NULL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		top.setLayoutData(gridData);
		top.setLayout(new GridLayout(5, true));
		createTopComposite(top);
		
		Composite content = new Composite(parent, SWT.NULL);
		content.setLayout(new GridLayout(1, false));
		content.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createContent(content);		
		
		Composite bottom = new Composite(parent, SWT.NULL);
		bottom.setLayout(new GridLayout(1, false));
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 300;
		bottom.setLayoutData(gridData);
		createBottomComposite(bottom);
	}
	
	private void createTopComposite(Composite parent){
	}
	
	private void createContent(Composite parent){
		//line 1
		Label label = new Label(parent, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("从classpath获得文件");
		RCPUtil.setBold(label);
		
		//line 2
		Composite c1 = new Composite(parent, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c1.setLayout(new GridLayout(3, false));
		
		Button button = new Button(c1, SWT.PUSH);
		button.setText("查  询");
		button.setLayoutData(new GridData());
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
			}
		});
		
		Combo combo = new Combo(c1, SWT.READ_ONLY);
		combo.setLayoutData(new GridData());
		combo.add("查询文件内容");
		combo.add("查询文件资源路径");
		combo.select(0);
		
		Text text = new Text(c1, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.widthHint = 200;
		text.setLayoutData(gridData);
		
		//line 3
		text = new Text(parent, SWT.MULTI|SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 200;
		text.setLayoutData(gridData);
	}
	
	private void createBottomComposite(Composite parent){
		Composite c1 = new Composite(parent, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c1.setLayout(new GridLayout(1, false));
		
		Label label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("系统属性");
		RCPUtil.setBold(label);
		
		Composite c2 = new Composite(parent, SWT.NULL);
		c2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c2.setLayout(new GridLayout(3, false));
		
		Button button = new Button(c2, SWT.PUSH);
		button.setText("查询系统属性");
		button.setLayoutData(new GridData());
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				String condition = conditionText.getText().trim();
				SystemMonitorMBean objSystemMonitorMBean = null;
				try {
					objSystemMonitorMBean = (SystemMonitorMBean) model.getObject(SystemMonitorMBean.class);
					HashMap map = objSystemMonitorMBean.fetchSystemProperties(condition);
					Set keys = map.keySet();
					
					List<KeyValue> list = new ArrayList<KeyValue>();
					for (Iterator iter = keys.iterator(); iter.hasNext();) {
						String item = (String) iter.next();
						KeyValue kv = new KeyValue();
						kv.setKey(item);
						kv.setValue((String)map.get(item));
						list.add(kv);
					}
					tableViewer.setInput(list);
				} catch (Exception ex) {
					log.error("异常", ex);
				} finally {
					if (objSystemMonitorMBean != null) {
						ClientProxy.destroyObject(objSystemMonitorMBean);
					}
				}
			}
		});
		
		conditionText = new Text(c2, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.widthHint = 300;
		conditionText.setLayoutData(gridData);
		
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("key");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof KeyValue) {
					return ((KeyValue) element).getKey();
				} else {
					return element.toString();
				}
			}
		});

		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if (e1 instanceof KeyValue && e2 instanceof KeyValue) {
					KeyValue c1 = (KeyValue) e1;
					KeyValue c2 = (KeyValue) e2;
					return c1.getKey().compareTo(c2.getKey());
				} else {
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("value");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof KeyValue) {
					return ((KeyValue) element).getValue();
				} else {
					return element.toString();
				}
			}
		});

		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if (e1 instanceof KeyValue && e2 instanceof KeyValue) {
					KeyValue c1 = (KeyValue) e1;
					KeyValue c2 = (KeyValue) e2;
					return c1.getValue().compareTo(c2.getValue());
				} else {
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
	}

}
