package com.easyfun.eclipse.performance.appframe.monitor.ui.refreshcache;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.ai.appframe2.complex.mbean.standard.cache.CacheMonitorMBean;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonServer;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.StringLengthDescComparator;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.action.appframe.CacheMonitorAction;
import com.easyfun.eclipse.performance.appframe.monitor.ui.common.ServerComposite;
import com.easyfun.eclipse.rcp.ColumnViewerSorter;
import com.easyfun.eclipse.rcp.RCPUtil;



/**
 * CRM缓存刷新
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-2
 */
public class RefreshCacheView extends Composite{
	
	private static Log log = LogFactory.getLog(RefreshCacheView.class);
	
	private TableViewer tableViewer;

	private ServerComposite serverComposite;

	private Text timeoutText;

	public RefreshCacheView(Composite parent){
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
		
		initData();
	}
	
	private void initData(){
		try {
			serverComposite.initData("CRM-APP");
			IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
			MonServer[] objMonServer = objIMonSV.getMonMbeanServerByServerType("CRM-APP");
			List<String> list = new ArrayList<String>();
			if (objMonServer != null && objMonServer.length >= 1) {
				String[] cacheIds = CacheMonitorAction.getAllCaches(objMonServer[0].getServerId());
				java.util.Arrays.sort(cacheIds, new StringLengthDescComparator());
				for (int i = 0; i < cacheIds.length; i++) {
					list.add(cacheIds[i]);
				}
			}
			tableViewer.setInput(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initControl(Composite parent){
		Composite top = new Composite(parent, SWT.NULL);
		top.setLayout(new GridLayout(5, true));
		
		createTopComposite(top);
		
		Composite content = new Composite(parent, SWT.NULL);
		content.setLayout(new GridLayout(1, false));
		content.setLayoutData(new GridData(GridData.FILL_BOTH));
		createContent(content);		
	}
	
	private void createTopComposite(Composite parent){
		serverComposite = new ServerComposite(parent);
	}
	
	private void createContent(Composite parent){
		Composite c1 = new Composite(parent, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c1.setLayout(new GridLayout(6, false));
		
		Label label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("cache列表");
		RCPUtil.setBold(label);
		
		label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("超时(秒)：");
		
		timeoutText = new Text(c1, SWT.BORDER);
		timeoutText.setLayoutData(new GridData());
		timeoutText.setText("");
		
		Button button = new Button(c1, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("全选");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = tableViewer.getTable().getItems();
				for (TableItem item : items) {
					item.setChecked(true);
				}
			}
		});
		
		button = new Button(c1, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("全不选");
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = tableViewer.getTable().getItems();
				for (TableItem item : items) {
					item.setChecked(false);
				}
			}
		});
		
		button = new Button(c1, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("刷新");
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					long[] serverIds = serverComposite.getSelectId();
					
					List<String> list = new ArrayList<String>();
					TableItem[] items = tableViewer.getTable().getItems();
					for (TableItem item : items) {
						if(item.getChecked()){
							list.add(item.getText());
						}
					}
					
					String[] cacheIds = list.toArray(new String[0]);
					
					
					HashMap map = new HashMap();
					IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
					MonServer[] objMonServer = objIMonSV.getMonServerByServerId(serverIds);
					for (int i = 0; i < serverIds.length; i++) {
						CacheMonitorMBean objCacheMonitorMBean = null;
						List l = new ArrayList();
						try {
							objCacheMonitorMBean = (CacheMonitorMBean) ClientProxy.getObject(serverIds[i], CacheMonitorMBean.class);
							for (int j = 0; j < cacheIds.length; j++) {
								try {
									objCacheMonitorMBean.forceRefresh(cacheIds[j]);
								} catch (Throwable ex) {
									l.add(cacheIds[j] + "失败");
									log.error("刷新异常", ex);
								}
							}
						} catch (Throwable ex) {
							map.put(new Long(serverIds[i]), objMonServer[i].getName() + ",连接失败,该server上的所有cache刷新失败");
							log.error("获得服务异常", ex);
						} finally {
							if (objCacheMonitorMBean != null) {
								ClientProxy.destroyObject(objCacheMonitorMBean);
							}
						}

						if (!l.isEmpty()) {
							map.put(new Long(serverIds[i]), objMonServer[i].getName() + ",刷新cache失败列表," + StringUtils.join(l.iterator(), ","));
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		});
		
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.CHECK);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("Cache");
		column.getColumn().setWidth(600);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				String model = (String)element;
				return model;
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof String && e2 instanceof String){
					String c1 = (String) e1;
					String c2 = (String) e2;
					return c1.compareTo(c2);
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
	}
	
	public static void main(String[] args) throws Exception{
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		
		new RefreshCacheView(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
