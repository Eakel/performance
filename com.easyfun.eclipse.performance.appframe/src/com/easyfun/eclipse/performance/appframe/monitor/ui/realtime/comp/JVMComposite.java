package com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp;

import java.util.List;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.ai.appframe2.complex.mbean.standard.jvm5.JVM5MonitorMBean;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.helper.MBeanHelper;
import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.helper.ThreadModel;
import com.easyfun.eclipse.rcp.ColumnViewerSorter;
import com.easyfun.eclipse.rcp.RCPUtil;

/**
 * JVM基本
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-4
 */
public class JVMComposite extends Composite implements IDataInit{
	protected static Log log = LogFactory.getLog(BasicComposite.class);
	
	private TableViewer tableViewer;
	
	private InvokeModel model;
	
	public JVMComposite(Composite parent) {
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
	}
	
	private void initControl(Composite parent){
		Composite content = new Composite(parent, SWT.NULL);
		content.setLayout(new GridLayout(1, false));
		content.setLayoutData(new GridData(GridData.FILL_BOTH));
		createContent(content);		
		
		Composite bottom = new Composite(parent, SWT.NULL);
		bottom.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 300;
		bottom.setLayoutData(gridData);
		createBottomComposite(bottom);
	}
	
	public void initData(InvokeModel model) throws Exception{
		this.model = model;
	}
	
	private void createContent(Composite parent){
		Label label = new Label(parent, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("内存信息");
		RCPUtil.setBold(label);
		
		label = new Label(parent, SWT.NULL);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 200;
		label.setLayoutData(gridData);
		label.setText("");
		RCPUtil.setBold(label);
		
		Composite c1 = new Composite(parent, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c1.setLayout(new GridLayout(6, false));
		
		Button button = new Button(c1, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("启动");
		
		button = new Button(c1, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("停止");
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
			}
		});
		
		label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("终止时间");
		
		Text text = new Text(c1, SWT.BORDER);
		gridData = new GridData();
		gridData.widthHint = 60;
		text.setLayoutData(gridData);
		text.setText("60");
		
		label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("间隔时间");
		
		text = new Text(c1, SWT.BORDER);
		gridData = new GridData();
		gridData.widthHint = 60;
		text.setLayoutData(gridData);
		text.setText("2");
	}
	
	private void createBottomComposite(Composite parent){
		Composite c1 = new Composite(parent, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c1.setLayout(new GridLayout(1, false));
		
		Label label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("线程信息");
		RCPUtil.setBold(label);
		
		Composite c2 = new Composite(parent, SWT.NULL);
		c2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c2.setLayout(new GridLayout(2, false));
		
		Button button = new Button(c2, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("查询线程信息");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				JVM5MonitorMBean objJVM5MontiorMBean = null;
				try {
					objJVM5MontiorMBean = (JVM5MonitorMBean) model.getObject(JVM5MonitorMBean.class);
					List<ThreadModel> theadList = MBeanHelper.getAllThreadInfo(objJVM5MontiorMBean);
					tableViewer.setInput(theadList);
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if (objJVM5MontiorMBean != null) {
						ClientProxy.destroyObject(objJVM5MontiorMBean);
					}
				}
			}
		});
		
		button = new Button(c2, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("拷贝到剪切板");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sb = new StringBuffer();
				List<ThreadModel> input = (List<ThreadModel>)tableViewer.getInput();
				sb.append("序号").append("\t").append("线程ID").append("\t").append("线程状态").append("\t")
				.append("线程名称").append("\n");
				for (ThreadModel model : input) {
					sb.append(model.getRowNum()).append("\t").append(model.getThreadId()).append("\t").append(model.getState()).append("\t")
					.append(model.getThreadName()).append("\n");
				}
				
				String content = sb.toString();
				
				RCPUtil.copyToClipboard(getShell(), content);
			}
		});
		
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("序号");
		column.getColumn().setWidth(50);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof ThreadModel){
					ThreadModel model = (ThreadModel)element;
					return "" + model.getRowNum();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof ThreadModel && e2 instanceof ThreadModel){
					ThreadModel c1 = (ThreadModel) e1;
					ThreadModel c2 = (ThreadModel) e2;
					return (int)(c1.getRowNum()- c2.getRowNum());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("线程ID");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof ThreadModel){
					ThreadModel model = (ThreadModel)element;
					return "" + model.getThreadId();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof ThreadModel && e2 instanceof ThreadModel){
					ThreadModel c1 = (ThreadModel) e1;
					ThreadModel c2 = (ThreadModel) e2;
					return (int)(c1.getThreadId()- c2.getThreadId());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("线程状态");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof ThreadModel){
					ThreadModel model = (ThreadModel)element;
					return "" + model.getState();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof ThreadModel && e2 instanceof ThreadModel){
					ThreadModel c1 = (ThreadModel) e1;
					ThreadModel c2 = (ThreadModel) e2;
					return c1.getState().compareTo(c2.getState());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("线程名称");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof ThreadModel){
					ThreadModel model = (ThreadModel)element;
					return "" + model.getThreadName();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof ThreadModel && e2 instanceof ThreadModel){
					ThreadModel c1 = (ThreadModel) e1;
					ThreadModel c2 = (ThreadModel) e2;
					return c1.getThreadName().compareTo(c2.getThreadName());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
	}

}
