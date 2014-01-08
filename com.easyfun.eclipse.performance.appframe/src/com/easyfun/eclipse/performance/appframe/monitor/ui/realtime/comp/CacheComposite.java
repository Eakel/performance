package com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
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

import com.ai.appframe2.complex.mbean.standard.cache.CacheMonitorMBean;
import com.ai.appframe2.complex.mbean.standard.cache.CacheSummary;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.TimeUtil;
import com.easyfun.eclipse.rcp.ColumnViewerSorter;
import com.easyfun.eclipse.rcp.RCPUtil;

/**
 * AppFrame Cache
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-4
 */
public class CacheComposite extends Composite implements IDataInit{
	private static Log log = LogFactory.getLog(CacheComposite.class);

	
	private TableViewer tableViewer;
	
	private long serverId = -1;


	private ComboViewer cacheComboViewer;
	
	public CacheComposite(Composite parent) {
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
	}
	
	public void initData(long serverId) throws Exception{
		this.serverId = serverId;
		//this.serverId
		String[] rtn = null;
		CacheMonitorMBean objCacheMonitorMBean = null;
		try {
			objCacheMonitorMBean = (CacheMonitorMBean) ClientProxy.getObject(serverId, CacheMonitorMBean.class);
			rtn = objCacheMonitorMBean.listAllCache();
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objCacheMonitorMBean != null) {
				ClientProxy.destroyObject(objCacheMonitorMBean);
			}
		}
		cacheComboViewer.setInput(rtn);
		cacheComboViewer.getCombo().add("ALL", 0);
		
		cacheComboViewer.getCombo().select(0);
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
		label.setText("Cache信息");
		RCPUtil.setBold(label);
		
		Composite c2 = new Composite(parent, SWT.NULL);
		c2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c2.setLayout(new GridLayout(4, false));
		
		Button button = new Button(c2, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("查询Cache信息");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				String select = cacheComboViewer.getCombo().getItem(cacheComboViewer.getCombo().getSelectionIndex());
				String condition = "";
				if(!select.equals("ALL")){
					condition = select;
				}
				
				CacheSummary[] rtn = null;
				CacheMonitorMBean objCacheMonitorMBean = null;
				try {
					objCacheMonitorMBean = (CacheMonitorMBean) ClientProxy.getObject(serverId, CacheMonitorMBean.class);
					rtn = objCacheMonitorMBean.fetchCache(condition);
				} catch (Exception ex) {
					log.error("异常", ex);
				} finally {
					if (objCacheMonitorMBean != null) {
						ClientProxy.destroyObject(objCacheMonitorMBean);
					}
				}
				List<CacheSummary> list = new ArrayList<CacheSummary>();
				for (CacheSummary cacheSummary : rtn) {
					list.add(cacheSummary);
				}
				
				tableViewer.setInput(list);
			}
		});
		
		cacheComboViewer = new ComboViewer(c2, SWT.READ_ONLY);
		GridData gridData = new GridData();
		gridData.widthHint = 400;
		cacheComboViewer.getCombo().setLayoutData(gridData);
		cacheComboViewer.setContentProvider(new ArrayContentProvider());
		cacheComboViewer.setLabelProvider(new LabelProvider(){
			public String getText(Object element) {
				return super.getText(element);
			}
		});
		
		button = new Button(c2, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("强制刷新");
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
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
		column.getColumn().setText("类名");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof CacheSummary) {
					CacheSummary model = (CacheSummary) element;
					return model.getClassName();
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof CacheSummary && e2 instanceof CacheSummary){
					CacheSummary c1 = (CacheSummary) e1;
					CacheSummary c2 = (CacheSummary) e2;
					return c1.getClassName().compareTo(c2.getClassName());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("刷新以前的数量");
		column.getColumn().setWidth(120);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof CacheSummary) {
					CacheSummary model = (CacheSummary) element;
					return model.getOldCount() + "";
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof CacheSummary && e2 instanceof CacheSummary){
					CacheSummary c1 = (CacheSummary) e1;
					CacheSummary c2 = (CacheSummary) e2;
					return (int)(c1.getOldCount()- c2.getOldCount());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("刷新以后的数量");
		column.getColumn().setWidth(120);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof CacheSummary) {
					CacheSummary model = (CacheSummary) element;
					return model.getNewCount() + "";
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof CacheSummary && e2 instanceof CacheSummary){
					CacheSummary c1 = (CacheSummary) e1;
					CacheSummary c2 = (CacheSummary) e2;
					return (int)(c1.getNewCount()- c2.getNewCount());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("最后一次刷新开始时间");
		column.getColumn().setWidth(180);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof CacheSummary) {
					CacheSummary model = (CacheSummary) element;
					return TimeUtil.format(new Date(model.getLastRefreshStartTime()));
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof CacheSummary && e2 instanceof CacheSummary){
					CacheSummary c1 = (CacheSummary) e1;
					CacheSummary c2 = (CacheSummary) e2;
					return (int)(c1.getLastRefreshStartTime()- c2.getLastRefreshStartTime());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("最后一次刷新结束时间");
		column.getColumn().setWidth(180);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof CacheSummary) {
					CacheSummary model = (CacheSummary) element;
					return TimeUtil.format(new Date(model.getLastRefreshEndTime()));
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof CacheSummary && e2 instanceof CacheSummary){
					CacheSummary c1 = (CacheSummary) e1;
					CacheSummary c2 = (CacheSummary) e2;
					return (int)(c1.getLastRefreshEndTime()- c2.getLastRefreshEndTime());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
	}

}
