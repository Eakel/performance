package com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp;

import java.util.Date;

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

import com.ai.appframe2.complex.mbean.standard.IControl;
import com.ai.appframe2.complex.mbean.standard.sv.SVMethodMonitorMBean;
import com.ai.appframe2.complex.mbean.standard.sv.SVMethodSummary;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.TimeUtil;
import com.easyfun.eclipse.rcp.ColumnViewerSorter;
import com.easyfun.eclipse.rcp.RCPUtil;

/**
 * AppFrame SV
 * @author linzhaoming
 *
 * @Created 2014-1-4
 */
public class ServiceComposite extends Composite implements IDataInit{
	private static Log log = LogFactory.getLog(ServiceComposite.class);
	
	private TableViewer tableViewer;
	
	private long serverId = -1;

	private Text conditionText;

	private Button statusButton;

	private Text timeoutText;
	
	public ServiceComposite(Composite parent) {
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
	}
	
	public void initData(long serverId) throws Exception{
		this.serverId = serverId;
	}
	
	private void initControl(Composite parent){
		Composite comp = new Composite(parent, SWT.NULL);
		comp.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(gridData);
		createContent(comp);
	}
	
	private void createContent(Composite parent){
		Composite c1 = new Composite(parent, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c1.setLayout(new GridLayout(1, false));
		
		Label label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("服务信息");
		RCPUtil.setBold(label);
		
		Composite c2 = new Composite(parent, SWT.NULL);
		c2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c2.setLayout(new GridLayout(9, false));
		
		Button button = new Button(c2, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("查询服务信息");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				String condition = conditionText.getText().trim();
				SVMethodMonitorMBean objSVMethodMonitorMBean = null;
				try {
					objSVMethodMonitorMBean = (SVMethodMonitorMBean) ClientProxy.getObject(serverId, SVMethodMonitorMBean.class);
					SVMethodSummary[] objSVMethodSummary = objSVMethodMonitorMBean.fetchSVMethodSummary(condition);
					tableViewer.setInput(objSVMethodSummary);
				} catch (Exception ex) {
					log.error("异常", ex);
				} finally {
					if (objSVMethodMonitorMBean != null) {
						ClientProxy.destroyObject(objSVMethodMonitorMBean);
					}
				}
			}
		});
		
		conditionText = new Text(c2, SWT.BORDER);
		conditionText.setLayoutData(new GridData());
		conditionText.setText("");
		
		statusButton = new Button(c2, SWT.NULL);
		GridData gridData = new GridData();
		gridData.widthHint = 150;
		statusButton.setLayoutData(gridData);
		statusButton.setText("监控状态");
		statusButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				SVMethodMonitorMBean objSVMethodMonitorMBean = null;
				try {
					objSVMethodMonitorMBean = (SVMethodMonitorMBean) ClientProxy.getObject(serverId, SVMethodMonitorMBean.class);

					IControl objIControl = objSVMethodMonitorMBean;
					statusButton.setText("监控状态 " + objIControl.fetchStatus());
				} catch (Exception ex) {
					log.error("异常", ex);
				} finally {
					if (objSVMethodMonitorMBean != null) {
						ClientProxy.destroyObject(objSVMethodMonitorMBean);
					}
				}
			}
		});
		
		button = new Button(c2, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("打开监控");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				long timeout = Long.parseLong(timeoutText.getText().trim());
				SVMethodMonitorMBean objSVMethodMonitorMBean = null;
				try {
					objSVMethodMonitorMBean = (SVMethodMonitorMBean) ClientProxy.getObject(serverId, SVMethodMonitorMBean.class);
					IControl objIControl = objSVMethodMonitorMBean;
					objIControl.enable(timeout);
					statusButton.setText("监控状态：" + objIControl.fetchStatus());
				} catch (Exception ex) {
					log.error("异常", ex);
				} finally {
					if (objSVMethodMonitorMBean != null) {
						ClientProxy.destroyObject(objSVMethodMonitorMBean);
					}
				}
			}
		});
		
		label = new Label(c2, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("时长(秒)");
		
		timeoutText = new Text(c2, SWT.BORDER);
		gridData = new GridData();
		gridData.widthHint = 60;
		timeoutText.setLayoutData(gridData);
		timeoutText.setText("10");
		
		button = new Button(c2, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("关闭监控");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				SVMethodMonitorMBean objSVMethodMonitorMBean = null;
				try {
					objSVMethodMonitorMBean = (SVMethodMonitorMBean) ClientProxy.getObject(serverId, SVMethodMonitorMBean.class);
					IControl objIControl = objSVMethodMonitorMBean;
					objIControl.disable();

					statusButton.setText("监控状态：" + objIControl.fetchStatus());
				} catch (Exception ex) {
					log.error("异常", ex);
				} finally {
					if (objSVMethodMonitorMBean != null) {
						ClientProxy.destroyObject(objSVMethodMonitorMBean);
					}
				}
			}
		});
		
		button = new Button(c2, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("拷贝到剪切板");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sb = new StringBuffer();
				SVMethodSummary[] input = (SVMethodSummary[])tableViewer.getInput();
				sb.append("实现类").append("\t").append("方法名").append("\t").append("最小耗时(ms)").append("\t")
				.append("最大耗时(ms)").append("\t").append("平均耗时(ms)").append("\t").append("总次数").append("\t")
				.append("成功次数").append("\t").append("失败次数").append("\t").append("最后一次耗时(ms)").append("\t")
				.append("总耗时(ms)").append("\t").append("最后一次调用时间").append("\n");
				for (SVMethodSummary model : input) {
					sb.append(model.getClassName()).append("\t").append(model.getMethodName()).append("\t").append(model.getMin()).append("\t")
					.append(model.getMax()).append("\t").append(model.getAvg()).append("\t").append(model.getTotalCount()).append("\t")
					.append(model.getSuccessCount()).append("\t").append(model.getFailCount()).append("\t").append(model.getLastUseTime()).append("\t")
					.append(model.getTotalUseTime()).append("\t").append(TimeUtil.format(new Date(model.getLast()))).append("\t")
					.append("\n");
				}
				
				String content = sb.toString();
				
				RCPUtil.copyToClipboard(getShell(), content);
			}
		});
		
		label = new Label(c2, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("");
		
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("实现类");
		column.getColumn().setWidth(200);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof SVMethodSummary) {
					SVMethodSummary model = (SVMethodSummary) element;
					return model.getClassName();
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SVMethodSummary && e2 instanceof SVMethodSummary){
					SVMethodSummary c1 = (SVMethodSummary) e1;
					SVMethodSummary c2 = (SVMethodSummary) e2;
					return c1.getClassName().compareTo(c2.getClassName());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("方法名");
		column.getColumn().setWidth(60);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof SVMethodSummary) {
					SVMethodSummary model = (SVMethodSummary) element;
					return model.getMethodName();
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SVMethodSummary && e2 instanceof SVMethodSummary){
					SVMethodSummary c1 = (SVMethodSummary) e1;
					SVMethodSummary c2 = (SVMethodSummary) e2;
					return c1.getMethodName().compareTo(c2.getMethodName());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("最小耗时(ms)");
		column.getColumn().setWidth(90);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof SVMethodSummary) {
					SVMethodSummary model = (SVMethodSummary) element;
					return model.getMin() + "";
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SVMethodSummary && e2 instanceof SVMethodSummary){
					SVMethodSummary c1 = (SVMethodSummary) e1;
					SVMethodSummary c2 = (SVMethodSummary) e2;
					return (int)(c1.getMin()- c2.getMin());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("最大耗时(ms)");
		column.getColumn().setWidth(90);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof SVMethodSummary) {
					SVMethodSummary model = (SVMethodSummary) element;
					return model.getMax() + "";
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SVMethodSummary && e2 instanceof SVMethodSummary){
					SVMethodSummary c1 = (SVMethodSummary) e1;
					SVMethodSummary c2 = (SVMethodSummary) e2;
					return (int)(c1.getMax()- c2.getMax());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("平均耗时(ms)");
		column.getColumn().setWidth(90);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof SVMethodSummary) {
					SVMethodSummary model = (SVMethodSummary) element;
					return model.getAvg() + "";
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SVMethodSummary && e2 instanceof SVMethodSummary){
					SVMethodSummary c1 = (SVMethodSummary) e1;
					SVMethodSummary c2 = (SVMethodSummary) e2;
					return (int)(c1.getAvg()- c2.getAvg());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("总次数");
		column.getColumn().setWidth(60);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof SVMethodSummary) {
					SVMethodSummary model = (SVMethodSummary) element;
					return model.getTotalCount() + "";
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SVMethodSummary && e2 instanceof SVMethodSummary){
					SVMethodSummary c1 = (SVMethodSummary) e1;
					SVMethodSummary c2 = (SVMethodSummary) e2;
					return (int)(c1.getTotalCount()- c2.getTotalCount());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("成功次数");
		column.getColumn().setWidth(60);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof SVMethodSummary) {
					SVMethodSummary model = (SVMethodSummary) element;
					return model.getSuccessCount() + "";
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SVMethodSummary && e2 instanceof SVMethodSummary){
					SVMethodSummary c1 = (SVMethodSummary) e1;
					SVMethodSummary c2 = (SVMethodSummary) e2;
					return (int)(c1.getSuccessCount()- c2.getSuccessCount());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("失败次数");
		column.getColumn().setWidth(60);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof SVMethodSummary) {
					SVMethodSummary model = (SVMethodSummary) element;
					return model.getFailCount() + "";
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SVMethodSummary && e2 instanceof SVMethodSummary){
					SVMethodSummary c1 = (SVMethodSummary) e1;
					SVMethodSummary c2 = (SVMethodSummary) e2;
					return (int)(c1.getFailCount()- c2.getFailCount());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("最后一次耗时(ms)");
		column.getColumn().setWidth(110);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof SVMethodSummary) {
					SVMethodSummary model = (SVMethodSummary) element;
					return model.getLastUseTime() + "";
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SVMethodSummary && e2 instanceof SVMethodSummary){
					SVMethodSummary c1 = (SVMethodSummary) e1;
					SVMethodSummary c2 = (SVMethodSummary) e2;
					return (int)(c1.getLastUseTime()- c2.getLastUseTime());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("总耗时(ms)");
		column.getColumn().setWidth(80);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof SVMethodSummary) {
					SVMethodSummary model = (SVMethodSummary) element;
					return model.getTotalUseTime() + "";
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SVMethodSummary && e2 instanceof SVMethodSummary){
					SVMethodSummary c1 = (SVMethodSummary) e1;
					SVMethodSummary c2 = (SVMethodSummary) e2;
					return (int)(c1.getTotalUseTime()- c2.getTotalUseTime());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("最后一次调用时间");
		column.getColumn().setWidth(120);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof SVMethodSummary) {
					SVMethodSummary model = (SVMethodSummary) element;
					return TimeUtil.format(new Date(model.getLast()));
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SVMethodSummary && e2 instanceof SVMethodSummary){
					SVMethodSummary c1 = (SVMethodSummary) e1;
					SVMethodSummary c2 = (SVMethodSummary) e2;
					return (int)(c1.getLast()- c2.getLast());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
	}

}
