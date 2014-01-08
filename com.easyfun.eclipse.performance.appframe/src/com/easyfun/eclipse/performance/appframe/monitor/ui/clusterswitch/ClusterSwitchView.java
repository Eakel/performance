package com.easyfun.eclipse.performance.appframe.monitor.ui.clusterswitch;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonServer;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ParallelUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ParallelUtil.ConnectApp;


/**
 * 集群切换
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-2
 */
public class ClusterSwitchView extends Composite{

	private TableViewer tableViewer;

	public ClusterSwitchView(Composite parent){
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
		
		initData();
	}
	
	private void initData(){
	}
	
	private void initControl(Composite parent){
		Composite top = new Composite(parent, SWT.NULL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 200;
		top.setLayoutData(gridData);
		top.setLayout(new GridLayout(1, true));
		createTopComposite(top);
		
		Composite content = new Composite(parent, SWT.NULL);
		content.setLayout(new GridLayout(1, false));
		content.setLayoutData(new GridData(GridData.FILL_BOTH));
		createContent(content);		
		
		Composite bottom = new Composite(parent, SWT.NULL);
		bottom.setLayout(new GridLayout(2, false));
		bottom.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createBottomComposite(bottom);
	}
	
	private void createTopComposite(Composite parent){
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("APP集群");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
			}
		});
		
		TableViewerColumn value = new TableViewerColumn(tableViewer, SWT.NULL);
		value.getColumn().setText("配置连接到此APP集群上的服务器列表");
		value.getColumn().setWidth(300);
		value.getColumn().setMoveable(true);
		value.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
			}
		});
		
		TableViewerColumn value1 = new TableViewerColumn(tableViewer, SWT.NULL);
		value1.getColumn().setText("可选列表");
		value1.getColumn().setWidth(200);
		value1.getColumn().setMoveable(true);
		value1.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
			}
		});
		
		TableViewerColumn value2 = new TableViewerColumn(tableViewer, SWT.NULL);
		value2.getColumn().setText("切换");
		value2.getColumn().setWidth(200);
		value2.getColumn().setMoveable(true);
		value2.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
			}
		});
	}
	
	private void createContent(Composite parent){
		TableViewer viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		viewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		viewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NULL);
		column.getColumn().setText("web或客户端服务器名称");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
			}
		});
		
		TableViewerColumn value = new TableViewerColumn(viewer, SWT.NULL);
		value.getColumn().setText("配置连接app的集群");
		value.getColumn().setWidth(200);
		value.getColumn().setMoveable(true);
		value.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
			}
		});
		
		TableViewerColumn state = new TableViewerColumn(viewer, SWT.NULL);
		state.getColumn().setText("当前连接app的集群");
		state.getColumn().setWidth(200);
		state.getColumn().setMoveable(true);
		state.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
			}
		});
		
		TableViewerColumn serverName = new TableViewerColumn(viewer, SWT.NULL);
		serverName.getColumn().setText("是否修改");
		serverName.getColumn().setWidth(100);
		serverName.getColumn().setMoveable(true);
		serverName.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
			}
		});
		
		TableViewerColumn group = new TableViewerColumn(viewer, SWT.NULL);
		group.getColumn().setText("修改app的集群");
		group.getColumn().setWidth(200);
		group.getColumn().setMoveable(true);
		group.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
			}
		});
	}
	
	private void createBottomComposite(Composite bottom){
		Button button = new Button(bottom, SWT.NULL);
		button.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false));
		button.setText("查询");
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String serverType = ""; // TODO:

				MonServer[] objMonServer = null;
				ConnectApp[] objConnectApp = null;
				TreeMap map = null;
				TreeMap canSelectedMap = null;
				HashMap mapping = null;
				try {
					IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
					objMonServer = objIMonSV.getMonMbeanServerByServerTypeOrderByServerName(serverType.trim());
					objConnectApp = ParallelUtil.getCurrentConnectAppCluster(objMonServer);

					map = new TreeMap();
					mapping = new HashMap();
					canSelectedMap = new TreeMap();
					for (int i = 0; i < objConnectApp.length; i++) {
						ArrayList list = (ArrayList) map.get(objConnectApp[i].oldAppCluster);
						if (list == null) {
							list = new ArrayList();
							map.put(objConnectApp[i].oldAppCluster, list);
						}
						list.add(objConnectApp[i].serverName);
						mapping.put(objConnectApp[i].serverName, String.valueOf(objConnectApp[i].serverId));

						// 可以选择的列表
						HashMap tmp = (HashMap) canSelectedMap.get(objConnectApp[i].oldAppCluster);
						if (tmp == null) {
							tmp = new HashMap();
							canSelectedMap.put(objConnectApp[i].oldAppCluster, tmp);
						}
						for (int j = 0; j < objConnectApp[i].canSelectAppCluster.length; j++) {
							tmp.put(objConnectApp[i].canSelectAppCluster[j], null);
						}
					}
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
				
				//可选列表
				//TODO: 处理表格树
			}
		});
		
		button = new Button(bottom, SWT.NULL);
		button.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false));
		button.setText("切换集群");
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
			}
		});
	}
	
	public static void main(String[] args) throws Exception{
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		
		new ClusterSwitchView(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
