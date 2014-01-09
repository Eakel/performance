package com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp;

import java.util.ArrayList;
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

import com.ai.appframe2.complex.mbean.standard.datasource.DataSourceMonitorMBean;
import com.ai.appframe2.complex.mbean.standard.datasource.DataSourceRuntime;
import com.ai.appframe2.complex.mbean.standard.datasource.DataSourceSummary;
import com.ai.appframe2.complex.mbean.standard.tm.TmSummary;
import com.ai.appframe2.complex.mbean.standard.tm.TransactionMonitorMBean;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.rcp.ColumnViewerSorter;
import com.easyfun.eclipse.rcp.RCPUtil;

/**
 * AppFrame Basic
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-4
 */
public class BasicComposite extends Composite implements IDataInit{
	
	private static Log log = LogFactory.getLog(JVMComposite.class);
	
	private InvokeModel model = null;

	private TableViewer tmTableViewer;

	private TableViewer dbConfTableViewer;

	private TableViewer dbRuntimeTableViewer;
	
	public BasicComposite(Composite parent) {
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
	}
	
	public void initData(InvokeModel model) throws Exception{
		this.model = model;
	}
	
	public void initData(String jmxUrl) throws Exception {
		
	}
	
	private void initControl(Composite parent){
		Composite top = new Composite(parent, SWT.NULL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 120;
		top.setLayoutData(gridData);
		top.setLayout(new GridLayout(1, false));
		createTopComposite(top);
		
		Composite content = new Composite(parent, SWT.NULL);
		content.setLayout(new GridLayout(1, false));
		content.setLayoutData(new GridData(GridData.FILL_BOTH));
		createContent(content);		
		
		Composite bottom = new Composite(parent, SWT.NULL);
		bottom.setLayout(new GridLayout(1, false));
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 300;
		bottom.setLayoutData(gridData);
		createBottomComposite(bottom);
	}
	
	private void createTopComposite(Composite parent){
		Composite c1 = new Composite(parent, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c1.setLayout(new GridLayout(1, false));
		
		Label label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("事务信息");
		RCPUtil.setBold(label);
		
		Button button = new Button(c1, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("查询事务信息");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				TransactionMonitorMBean objTransactionMonitorMBean = null;
				try {
					objTransactionMonitorMBean = (TransactionMonitorMBean) model.getObject(TransactionMonitorMBean.class);

					TmSummary objTmSummary = objTransactionMonitorMBean.fetchTmSummary();

					List<TmSummary> list = new ArrayList<TmSummary>();
					list.add(objTmSummary);
					tmTableViewer.setInput(list);
				} catch (Exception ex) {
					log.error("异常", ex);
				} finally {
					if (objTransactionMonitorMBean != null) {
						ClientProxy.destroyObject(objTransactionMonitorMBean);
					}
				}
			}
		});
		
		tmTableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		tmTableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tmTableViewer.getTable().setHeaderVisible(true);
		tmTableViewer.getTable().setLinesVisible(true);
		tmTableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(tmTableViewer, SWT.NULL);
		column.getColumn().setText("开始事务");
		column.getColumn().setWidth(200);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof TmSummary){
					TmSummary tmSum = (TmSummary)element;
					return tmSum.getStartCount() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tmTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof TmSummary && e2 instanceof TmSummary){
					TmSummary c1 = (TmSummary) e1;
					TmSummary c2 = (TmSummary) e2;
					return (int)(c1.getStartCount() - c2.getStartCount());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tmTableViewer, SWT.NULL);
		column.getColumn().setText("提交事务");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof TmSummary){
					TmSummary tmSum = (TmSummary)element;
					return tmSum.getCommitCount() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tmTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof TmSummary && e2 instanceof TmSummary){
					TmSummary c1 = (TmSummary) e1;
					TmSummary c2 = (TmSummary) e2;
					return (int)(c1.getCommitCount() - c2.getCommitCount());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tmTableViewer, SWT.NULL);
		column.getColumn().setText("回滚事务");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof TmSummary){
					TmSummary tmSum = (TmSummary)element;
					return tmSum.getRollbackCount() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tmTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof TmSummary && e2 instanceof TmSummary){
					TmSummary c1 = (TmSummary) e1;
					TmSummary c2 = (TmSummary) e2;
					return (int)(c1.getRollbackCount() - c2.getRollbackCount());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tmTableViewer, SWT.NULL);
		column.getColumn().setText("挂起事务");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof TmSummary){
					TmSummary tmSum = (TmSummary)element;
					return tmSum.getSuspendCount() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tmTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof TmSummary && e2 instanceof TmSummary){
					TmSummary c1 = (TmSummary) e1;
					TmSummary c2 = (TmSummary) e2;
					return (int)(c1.getSuspendCount() - c2.getSuspendCount());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tmTableViewer, SWT.NULL);
		column.getColumn().setText("恢复事务");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof TmSummary){
					TmSummary tmSum = (TmSummary)element;
					return tmSum.getResumeCount() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tmTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof TmSummary && e2 instanceof TmSummary){
					TmSummary c1 = (TmSummary) e1;
					TmSummary c2 = (TmSummary) e2;
					return (int)(c1.getResumeCount() - c2.getResumeCount());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
	}
	
	private void createContent(Composite parent){
		Composite c1 = new Composite(parent, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c1.setLayout(new GridLayout(1, false));
		
		Label label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("数据源配置信息");
		RCPUtil.setBold(label);
		
		Composite c2 = new Composite(parent, SWT.NULL);
		c2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c2.setLayout(new GridLayout(2, false));
		
		Button button = new Button(c2, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("查询数据源配置信息");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				DataSourceMonitorMBean objDataSourceMonitorMBean = null;
				try {
					objDataSourceMonitorMBean = (DataSourceMonitorMBean) model.getObject(DataSourceMonitorMBean.class);
					DataSourceSummary[] objDataSourceSummary = objDataSourceMonitorMBean.fetchAllDataSourceConfig();
					dbConfTableViewer.setInput(objDataSourceSummary);
				} catch (Exception ex) {
					log.error("异常", ex);
				} finally {
					if (objDataSourceMonitorMBean != null) {
						ClientProxy.destroyObject(objDataSourceMonitorMBean);
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
				DataSourceSummary[] sums = (DataSourceSummary[])dbConfTableViewer.getInput();
				sb.append("数据源").append("\t").append("驱动").append("\t").append("URL").append("\t")
				.append("用户名").append("\t").append("initialSize").append("\t").append("maxActive").append("\t")
				.append("maxIdle").append("\t").append("minIdle").append("\t").append("maxWait").append("\n");
				for (DataSourceSummary sum : sums) {
					sb.append(sum.getDataSource()).append("\t").append(sum.getDriverClassName()).append("\t").append(sum.getUrl()).append("\t")
					.append(sum.getUsername()).append("\t").append(sum.getInitialSize()).append("\t").append(sum.getMaxActive()).append("\t")
					.append(sum.getMaxIdle()).append("\t").append(sum.getMinIdle()).append("\t").append(sum.getMaxWait()).append("\t").append("\n");
				}
				
				String content = sb.toString();
				
				RCPUtil.copyToClipboard(getShell(), content);
			}
		});
		
		dbConfTableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		dbConfTableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dbConfTableViewer.getTable().setHeaderVisible(true);
		dbConfTableViewer.getTable().setLinesVisible(true);
		dbConfTableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(dbConfTableViewer, SWT.NULL);
		column.getColumn().setText("数据源");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof DataSourceSummary){
					DataSourceSummary dsSum = (DataSourceSummary)element;
					return dsSum.getDataSource();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(dbConfTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof DataSourceSummary && e2 instanceof DataSourceSummary){
					DataSourceSummary c1 = (DataSourceSummary) e1;
					DataSourceSummary c2 = (DataSourceSummary) e2;
					return  c1.getDataSource().compareTo(c2.getDataSource());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(dbConfTableViewer, SWT.NULL);
		column.getColumn().setText("驱动");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof DataSourceSummary){
					DataSourceSummary dsSum = (DataSourceSummary)element;
					return dsSum.getDriverClassName();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(dbConfTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof DataSourceSummary && e2 instanceof DataSourceSummary){
					DataSourceSummary c1 = (DataSourceSummary) e1;
					DataSourceSummary c2 = (DataSourceSummary) e2;
					return  c1.getDriverClassName().compareTo(c2.getDriverClassName());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(dbConfTableViewer, SWT.NULL);
		column.getColumn().setText("URL");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof DataSourceSummary){
					DataSourceSummary dsSum = (DataSourceSummary)element;
					return dsSum.getUrl();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(dbConfTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof DataSourceSummary && e2 instanceof DataSourceSummary){
					DataSourceSummary c1 = (DataSourceSummary) e1;
					DataSourceSummary c2 = (DataSourceSummary) e2;
					return  c1.getUrl().compareTo(c2.getUrl());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(dbConfTableViewer, SWT.NULL);
		column.getColumn().setText("用户名");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof DataSourceSummary){
					DataSourceSummary dsSum = (DataSourceSummary)element;
					return dsSum.getUsername();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(dbConfTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof DataSourceSummary && e2 instanceof DataSourceSummary){
					DataSourceSummary c1 = (DataSourceSummary) e1;
					DataSourceSummary c2 = (DataSourceSummary) e2;
					return  c1.getUsername().compareTo(c2.getUsername());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(dbConfTableViewer, SWT.NULL);
		column.getColumn().setText("initialSize");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof DataSourceSummary){
					DataSourceSummary dsSum = (DataSourceSummary)element;
					return dsSum.getInitialSize() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(dbConfTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof DataSourceSummary && e2 instanceof DataSourceSummary){
					DataSourceSummary c1 = (DataSourceSummary) e1;
					DataSourceSummary c2 = (DataSourceSummary) e2;
					return  (int)(c1.getInitialSize()- c2.getInitialSize());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(dbConfTableViewer, SWT.NULL);
		column.getColumn().setText("maxActive");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof DataSourceSummary){
					DataSourceSummary dsSum = (DataSourceSummary)element;
					return dsSum.getMaxActive() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(dbConfTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof DataSourceSummary && e2 instanceof DataSourceSummary){
					DataSourceSummary c1 = (DataSourceSummary) e1;
					DataSourceSummary c2 = (DataSourceSummary) e2;
					return  (int)(c1.getMaxActive()- c2.getMaxActive());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(dbConfTableViewer, SWT.NULL);
		column.getColumn().setText("maxIdle");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof DataSourceSummary){
					DataSourceSummary dsSum = (DataSourceSummary)element;
					return dsSum.getMaxIdle() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(dbConfTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof DataSourceSummary && e2 instanceof DataSourceSummary){
					DataSourceSummary c1 = (DataSourceSummary) e1;
					DataSourceSummary c2 = (DataSourceSummary) e2;
					return  (int)(c1.getMaxIdle()- c2.getMaxIdle());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(dbConfTableViewer, SWT.NULL);
		column.getColumn().setText("minIdle");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof DataSourceSummary){
					DataSourceSummary dsSum = (DataSourceSummary)element;
					return dsSum.getMinIdle() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(dbConfTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof DataSourceSummary && e2 instanceof DataSourceSummary){
					DataSourceSummary c1 = (DataSourceSummary) e1;
					DataSourceSummary c2 = (DataSourceSummary) e2;
					return  (int)(c1.getMinIdle()- c2.getMinIdle());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(dbConfTableViewer, SWT.NULL);
		column.getColumn().setText("maxWait");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof DataSourceSummary){
					DataSourceSummary dsSum = (DataSourceSummary)element;
					return dsSum.getMaxWait() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(dbConfTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof DataSourceSummary && e2 instanceof DataSourceSummary){
					DataSourceSummary c1 = (DataSourceSummary) e1;
					DataSourceSummary c2 = (DataSourceSummary) e2;
					return  (int)(c1.getMaxWait()- c2.getMaxWait());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
	}
	
	private void createBottomComposite(Composite parent){
		Composite c1 = new Composite(parent, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c1.setLayout(new GridLayout(1, false));
		
		Label label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("数据源运行信息");
		RCPUtil.setBold(label);
		
		Composite c2 = new Composite(parent, SWT.NULL);
		c2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c2.setLayout(new GridLayout(2, false));
		
		Button button = new Button(c2, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("查询数据源运行信息");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				DataSourceMonitorMBean objDataSourceMonitorMBean = null;
				try {
					objDataSourceMonitorMBean = (DataSourceMonitorMBean) model.getObject(DataSourceMonitorMBean.class);
					DataSourceRuntime[] objDataSourceRuntime = objDataSourceMonitorMBean.fetchAllDataSourceRuntime();
					dbRuntimeTableViewer.setInput(objDataSourceRuntime);
				} catch (Exception ex) {
					log.error("异常", ex);
				} finally {
					if (objDataSourceMonitorMBean != null) {
						ClientProxy.destroyObject(objDataSourceMonitorMBean);
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
				DataSourceRuntime[] input = (DataSourceRuntime[])dbRuntimeTableViewer.getInput();
				sb.append("数据源").append("\t").append("物理数据库连接数量").append("\t").append("空闲数量").append("\t")
				.append("再用数量").append("\n");
				for (DataSourceRuntime model : input) {
					sb.append(model.getDataSource()).append("\t").append(model.getNumPhysical()).append("\t").append(model.getNumIdle()).append("\t")
					.append(model.getNumActive()).append("\n");
				}
				
				String content = sb.toString();
				
				RCPUtil.copyToClipboard(getShell(), content);
			}
		});
		
		dbRuntimeTableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		dbRuntimeTableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dbRuntimeTableViewer.getTable().setHeaderVisible(true);
		dbRuntimeTableViewer.getTable().setLinesVisible(true);
		dbRuntimeTableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(dbRuntimeTableViewer, SWT.NULL);
		column.getColumn().setText("数据源");
		column.getColumn().setWidth(200);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof DataSourceRuntime){
					DataSourceRuntime dsRun = (DataSourceRuntime)element;
					return dsRun.getDataSource();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(dbRuntimeTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof DataSourceRuntime && e2 instanceof DataSourceRuntime){
					DataSourceRuntime c1 = (DataSourceRuntime) e1;
					DataSourceRuntime c2 = (DataSourceRuntime) e2;
					return  c1.getDataSource().compareTo(c2.getDataSource());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(dbRuntimeTableViewer, SWT.NULL);
		column.getColumn().setText("物理数据库连接数量");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof DataSourceRuntime){
					DataSourceRuntime dsRun = (DataSourceRuntime)element;
					return dsRun.getNumPhysical() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(dbRuntimeTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof DataSourceRuntime && e2 instanceof DataSourceRuntime){
					DataSourceRuntime c1 = (DataSourceRuntime) e1;
					DataSourceRuntime c2 = (DataSourceRuntime) e2;
					return  (int)(c1.getNumPhysical() - c2.getNumPhysical());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(dbRuntimeTableViewer, SWT.NULL);
		column.getColumn().setText("空闲数量");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof DataSourceRuntime){
					DataSourceRuntime dsRun = (DataSourceRuntime)element;
					return dsRun.getNumIdle() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(dbRuntimeTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof DataSourceRuntime && e2 instanceof DataSourceRuntime){
					DataSourceRuntime c1 = (DataSourceRuntime) e1;
					DataSourceRuntime c2 = (DataSourceRuntime) e2;
					return  (int)(c1.getNumIdle() - c2.getNumIdle());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(dbRuntimeTableViewer, SWT.NULL);
		column.getColumn().setText("在用数量");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof DataSourceRuntime){
					DataSourceRuntime dsRun = (DataSourceRuntime)element;
					return dsRun.getNumActive() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(dbRuntimeTableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof DataSourceRuntime && e2 instanceof DataSourceRuntime){
					DataSourceRuntime c1 = (DataSourceRuntime) e1;
					DataSourceRuntime c2 = (DataSourceRuntime) e2;
					return  (int)(c1.getNumActive() - c2.getNumActive());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
	}

}
