package com.easyfun.eclipse.performance.appframe.monitor.ui.serverinfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MidServerControl;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonCExec;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPHost;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.HttpUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ParallelUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.SSHUtil;
import com.easyfun.eclipse.rcp.ColumnViewerSorter;
import com.easyfun.eclipse.rcp.RCPUtil;


/**
 * CRM������
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-2
 */
public class ServerInfoView extends Composite{
	
	private Combo groupCombo;
	private Text timeoutText;
	private Text threadCountText;
	private Text serverNameText;
	private Text serverThreadCountText;
	private Text serverTimeoutText;
	private TableViewer tableViewer;


	public ServerInfoView(Composite parent){
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
		
		initData();
	}
	
	private void initData(){
		try {
			IMonSV objIMonSV = (IMonSV)ServiceFactory.getService(IMonSV.class);
			String[] grpName = objIMonSV.getAllMidServerControlGrp();
			for (int i = 0; i < grpName.length; i++) {
				groupCombo.add(grpName[i]);
			}
			
			if(groupCombo.getItemCount() >0){
				groupCombo.select(0);
			}
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
		
		Composite bottom = new Composite(parent, SWT.NULL);
		bottom.setLayout(new GridLayout(5, false));
		bottom.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createBottomComposite(bottom);
	}
	
	private void createTopComposite(Composite top){
		Label label = new Label(top, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("���飺");
		
		groupCombo = new Combo(top, SWT.READ_ONLY);
		groupCombo.setLayoutData(new GridData(100, SWT.DEFAULT));
//		groupCombo.add("CRM-APP");
//		groupCombo.add("CRM-WEB");
//		groupCombo.add("INTER-CRM");
		groupCombo.select(0);
		
		label = new Label(top, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("��ʱ(��)��");
		
		timeoutText = new Text(top, SWT.BORDER);
		timeoutText.setLayoutData(new GridData(100, SWT.DEFAULT));
		timeoutText.setText("8");
		
		label = new Label(top, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("");
		
		label = new Label(top, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("����ִ���߳�����");
		
		threadCountText = new Text(top, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 120;
		threadCountText.setLayoutData(gridData);
		threadCountText.setText("10");
		
		label = new Label(top, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("���������ƣ�");
		
		serverNameText = new Text(top, SWT.BORDER);
		serverNameText.setLayoutData(new GridData(100, SWT.DEFAULT));
		
		Button button = new Button(top, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("��  ѯ");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				try {
					List list = new ArrayList();
					String groupName = groupCombo.getItem(groupCombo.getSelectionIndex());
					if(StringUtils.isNotEmpty(groupName)){
						list.add(" GRP_NAME = '" + groupName + "'");	
					}
					
					int threadCount = Integer.parseInt(threadCountText.getText().trim());
					int timeout = Integer.parseInt(timeoutText.getText().trim());
					
					String serverName = serverNameText.getText().trim();
					if(StringUtils.isNotEmpty(serverName)){
						list.add(" SERVER_NAME like '%" + serverName + "%'");
					}
					list.add(" STATE='U' ");
					String sqlCondition = StringUtils.join(list.iterator(), " and ");
					
					IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
					MidServerControl[] controls = objIMonSV.getMidServerServerControlByCondition(sqlCondition, threadCount, timeout, 0, 1000);
					tableViewer.setInput(controls);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	private void createContent(Composite parent){
		tableViewer = new TableViewer(parent, SWT.CHECK | SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("��������");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MidServerControl control = (MidServerControl)element;
				return control.getHostname();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MidServerControl && e2 instanceof MidServerControl){
					MidServerControl c1 = (MidServerControl) e1;
					MidServerControl c2 = (MidServerControl) e2;
					return c1.getHostname().compareToIgnoreCase(c2.getHostname());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		TableViewerColumn value = new TableViewerColumn(tableViewer, SWT.NULL);
		value.getColumn().setText("URL");
		value.getColumn().setWidth(250);
		value.getColumn().setMoveable(true);
		value.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MidServerControl control = (MidServerControl)element;
				return control.getUrl();
			}
		});
		
		new ColumnViewerSorter(tableViewer, value) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MidServerControl && e2 instanceof MidServerControl){
					MidServerControl c1 = (MidServerControl) e1;
					MidServerControl c2 = (MidServerControl) e2;
					return c1.getUrl().compareToIgnoreCase(c2.getUrl());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		TableViewerColumn state = new TableViewerColumn(tableViewer, SWT.NULL);
		state.getColumn().setText("״̬");
		state.getColumn().setWidth(50);
		state.getColumn().setMoveable(true);
		state.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MidServerControl control = (MidServerControl)element;
				if(control.getStatus().equals("OK")){
					return "����";
				}else if(control.getStatus().equals("TIMEOUT")){
					return "��ʱ";
				}else if(control.getStatus().equals("EXCEPTION")){
					return "��������";
				}
				return control.getStatus();
			}
		});
		
		new ColumnViewerSorter(tableViewer, state) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MidServerControl && e2 instanceof MidServerControl){
					MidServerControl c1 = (MidServerControl) e1;
					MidServerControl c2 = (MidServerControl) e2;
					return c1.getStatus().compareToIgnoreCase(c2.getStatus());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		TableViewerColumn serverName = new TableViewerColumn(tableViewer, SWT.NULL);
		serverName.getColumn().setText("����������");
		serverName.getColumn().setWidth(200);
		serverName.getColumn().setMoveable(true);
		serverName.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MidServerControl control = (MidServerControl)element;
				return control.getServerName();
			}
		});
		
		new ColumnViewerSorter(tableViewer, serverName) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MidServerControl && e2 instanceof MidServerControl){
					MidServerControl c1 = (MidServerControl) e1;
					MidServerControl c2 = (MidServerControl) e2;
					return c1.getServerName().compareToIgnoreCase(c2.getServerName());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		TableViewerColumn group = new TableViewerColumn(tableViewer, SWT.NULL);
		group.getColumn().setText("����");
		group.getColumn().setWidth(100);
		group.getColumn().setMoveable(true);
		group.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MidServerControl control = (MidServerControl)element;
				return control.getGrpName();
			}
		});
		
		new ColumnViewerSorter(tableViewer, group) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MidServerControl && e2 instanceof MidServerControl){
					MidServerControl c1 = (MidServerControl) e1;
					MidServerControl c2 = (MidServerControl) e2;
					return c1.getGrpName().compareToIgnoreCase(c2.getGrpName());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		TableViewerColumn info = new TableViewerColumn(tableViewer, SWT.NULL);
		info.getColumn().setText("��Ϣ");
		info.getColumn().setWidth(300);
		info.getColumn().setMoveable(true);
		info.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MidServerControl control = (MidServerControl)element;
				return control.getInfo();
			}
		});
		
		new ColumnViewerSorter(tableViewer, info) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MidServerControl && e2 instanceof MidServerControl){
					MidServerControl c1 = (MidServerControl) e1;
					MidServerControl c2 = (MidServerControl) e2;
					return c1.getInfo().compareToIgnoreCase(c2.getInfo());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
	}
	
	private void createBottomComposite(Composite bottom){
		Label label = new Label(bottom, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("ֹͣ������server����ִ���߳�����(��)��");
		
		serverThreadCountText = new Text(bottom, SWT.BORDER);
		serverThreadCountText.setLayoutData(new GridData(200, SWT.DEFAULT));
		serverThreadCountText.setToolTipText("(ֹͣ������server���ò��еķ�ʽ������server����һ��һ���ķ�ʽ) ");
		serverThreadCountText.setText("36");
		
		label = new Label(bottom, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("ֹͣ������server����ִ�г�ʱʱ��(��): ");
		
		serverTimeoutText = new Text(bottom, SWT.BORDER);
		serverTimeoutText.setLayoutData(new GridData(200, SWT.DEFAULT));
		serverTimeoutText.setToolTipText("(����ֹͣserver��ʱ��,����60��;����server��ʱ��,����180��) ");
		serverTimeoutText.setText("120");
		
		label = new Label(bottom, SWT.NULL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		label.setLayoutData(gridData);
		
		Composite buttonArea1 = new Composite(bottom, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalSpan = 5;
		buttonArea1.setLayoutData(gridData);
		buttonArea1.setLayout(new GridLayout(5, false));
		
		Button selectErrServerButton = new Button(buttonArea1, SWT.PUSH);
		selectErrServerButton.setText("ѡ���쳣��server");
		selectErrServerButton.setLayoutData(new GridData());
		selectErrServerButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = tableViewer.getTable().getItems();
				int count = 0;
				for (int i=0; i<items.length; i++ ) {
					TableItem item = items[i];
					MidServerControl control = (MidServerControl)item.getData();
					if(control.getStatus().equals("EXCEPTION")){
						count++;
						item.setChecked(true);
					}else{
						item.setChecked(false);
					}
				}
				
				if (count == 0) {
					RCPUtil.showMessage(getShell(), "û���쳣��server");
				} else {
					RCPUtil.showMessage(getShell(), "��" + count + "���쳣��server");
				}
			}
		});
		
		Button selectOKServerButton = new Button(buttonArea1, SWT.PUSH);
		selectOKServerButton.setText("ѡ��������server");
		selectOKServerButton.setLayoutData(new GridData());
		selectOKServerButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = tableViewer.getTable().getItems();
				int count = 0;
				for (int i=0; i<items.length; i++ ) {
					TableItem item = items[i];
					MidServerControl control = (MidServerControl)item.getData();
					if(control.getStatus().equals("OK")){
						item.setChecked(true);
						count++;
					}else{
						item.setChecked(false);
					}
				}
				
				if (count == 0) {
					RCPUtil.showMessage(getShell(), "û��������server");
				} else {
					RCPUtil.showMessage(getShell(), "��" + count + "��������server");
				}
			}
		});
		
		
		label = new Label(buttonArea1, SWT.NULL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		label.setLayoutData(gridData);
		
		Composite buttonArea2 = new Composite(bottom, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalSpan = 5;
		buttonArea2.setLayoutData(gridData);
		buttonArea2.setLayout(new GridLayout(5, false));
		
		Button checkVersionButton = new Button(buttonArea2, SWT.PUSH);
		checkVersionButton.setText("���汾��Ϣ");
		checkVersionButton.setLayoutData(new GridData());
		checkVersionButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				Set<String> set = new TreeSet<String>();
				MidServerControl[] controls = getSeletedControls();
				
				if(controls.length ==0){
					RCPUtil.showError(getShell(), "����ѡ��һ��");
					return;
				}
				
				for (MidServerControl control : controls) {
					set.add(control.getInfo());
				}
				StringBuffer sb = new StringBuffer();
				if(set.size() > 1){
					sb = new StringBuffer("��ע��,�����˶���汾��Ϣ!\n");
					for (String str : set) {
						sb.append(str).append("\n");
					}
				}else{
					sb.append(set.iterator().next());
				}
				RCPUtil.showMessage(getShell(), sb.toString());
			}
		});
		
		Button stopServerButton = new Button(buttonArea2, SWT.PUSH);
		stopServerButton.setText("ֹͣserver");
		stopServerButton.setLayoutData(new GridData());
		stopServerButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				try {
					MidServerControl[] controls = getSeletedControls();
					if(controls.length == 0){
						RCPUtil.showError(getShell(), "����ѡ��һ��");
						return;
					}
					
					if(StringUtils.isEmpty(serverThreadCountText.getText().trim())){
						RCPUtil.showError(getShell(), "��������server�Ĳ���ִ���߳�����");
						serverThreadCountText.setFocus();
						return;
					}
					
					if(StringUtils.isEmpty(serverTimeoutText.getText().trim())){
						RCPUtil.showError(getShell(), "��������server�Ĳ���ִ�г�ʱʱ��");
						serverTimeoutText.setFocus();
						return;
					}
					
					String serverThreadCount = serverThreadCountText.getText().trim();
					String serverTimeout = serverTimeoutText.getText().trim();
					
					IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
					List list = new ArrayList();
					for (MidServerControl objMidServerServerControl : controls) {
						MonCExec objMonCExec = objIMonSV.getMonCExecByExecId(objMidServerServerControl.getStopExecId());
						MonPHost objMonPHost = objIMonSV.getMonPHostByHostname(objMidServerServerControl.getHostname());

						ParallelUtil.MidServerTask objMidServerTask = new ParallelUtil.MidServerTask();
						objMidServerTask.ip = objMonPHost.getIp();
						objMidServerTask.port = (int) objMonPHost.getSshport();
						objMidServerTask.username = objMonPHost.getUsername();
						objMidServerTask.password = objMonPHost.getPassword();
						objMidServerTask.serverName = objMidServerServerControl.getServerName();
						objMidServerTask.path = (objMidServerServerControl.getPfPath() + " " + objMidServerServerControl.getServerName());
						objMidServerTask.shell = objMonCExec.getExpr();
						list.add(objMidServerTask);
					}
					
					String[] rtn = ParallelUtil.computeMidServer(Integer.parseInt(serverThreadCount), Integer.parseInt(serverTimeout),
							(ParallelUtil.MidServerTask[]) list.toArray(new ParallelUtil.MidServerTask[0]));
					RCPUtil.showMessage(getShell(), StringUtils.join(rtn, "\n"));
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});
		
		Button startServerButton = new Button(buttonArea2, SWT.PUSH);
		startServerButton.setText("����server");
		startServerButton.setLayoutData(new GridData());
		startServerButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				try {
					MidServerControl[] controls = getSeletedControls();
					if(controls.length == 0){
						RCPUtil.showError(getShell(), "����ѡ��һ��");
						return;
					}
					
					if(StringUtils.isEmpty(serverThreadCountText.getText().trim())){
						RCPUtil.showError(getShell(), "��������server�Ĳ���ִ���߳�����");
						serverThreadCountText.setFocus();
						return;
					}
					
					if(StringUtils.isEmpty(serverTimeoutText.getText().trim())){
						RCPUtil.showError(getShell(), "��������server�Ĳ���ִ�г�ʱʱ��");
						serverTimeoutText.setFocus();
						return;
					}
					
					String serverThreadCount = serverThreadCountText.getText().trim();
					String serverTimeout = serverTimeoutText.getText().trim();
					
					IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
					List list = new ArrayList();
					for (MidServerControl objMidServerServerControl : controls) {
						MonCExec objMonCExec = objIMonSV.getMonCExecByExecId(objMidServerServerControl.getStartExecId());
						MonPHost objMonPHost = objIMonSV.getMonPHostByHostname(objMidServerServerControl.getHostname());

						ParallelUtil.MidServerTask objMidServerTask = new ParallelUtil.MidServerTask();
						objMidServerTask.ip = objMonPHost.getIp();
						objMidServerTask.port = (int) objMonPHost.getSshport();
						objMidServerTask.username = objMonPHost.getUsername();
						objMidServerTask.password = objMonPHost.getPassword();
						objMidServerTask.serverName = objMidServerServerControl.getServerName();
						objMidServerTask.path = (objMidServerServerControl.getPfPath() + " " + objMidServerServerControl.getServerName());
						objMidServerTask.shell = objMonCExec.getExpr();
						list.add(objMidServerTask);
					}
					
					String[] rtn = ParallelUtil.computeMidServer(Integer.parseInt(serverThreadCount), Integer.parseInt(serverTimeout),
							(ParallelUtil.MidServerTask[]) list.toArray(new ParallelUtil.MidServerTask[0]));
					
					RCPUtil.showMessage(getShell(), StringUtils.join(rtn, "\n"));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		Button restartServerButton = new Button(buttonArea2, SWT.PUSH);
		restartServerButton.setText("����server");
		restartServerButton.setLayoutData(new GridData());
		restartServerButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				MidServerControl[] controls = getSeletedControls();
				if(controls.length ==0){
					RCPUtil.showError(getShell(), "����ѡ��һ��");
					return;
				}
				
				if(RCPUtil.showConfirm(getShell(), "�����������server,��Ҫ����server��(����server���õ��Ǵ��з�ʽ,�ȽϺ�ʱ)?")){
					try {
						IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
						List list = new ArrayList();
						
						for (MidServerControl objMidServerServerControl : controls) {
							MonPHost objMonPHost = objIMonSV.getMonPHostByHostname(objMidServerServerControl.getHostname());
							if (objMidServerServerControl != null) {
								MonCExec objMonCExec = objIMonSV.getMonCExecByExecId(objMidServerServerControl.getStopExecId());
								SSHUtil.ssh4Shell(objMonPHost.getIp(), (int) objMonPHost.getSshport(), objMonPHost.getUsername(), objMonPHost.getPassword(),
										objMidServerServerControl.getServerName(), objMidServerServerControl.getPfPath() + " " + objMidServerServerControl.getServerName(),
										objMonCExec.getExpr());
							}

							if (objMidServerServerControl != null) {
								MonCExec objMonCExec = objIMonSV.getMonCExecByExecId(objMidServerServerControl.getStartExecId());
								SSHUtil.ssh4Shell(objMonPHost.getIp(), (int) objMonPHost.getSshport(), objMonPHost.getUsername(), objMonPHost.getPassword(),
										objMidServerServerControl.getServerName(), objMidServerServerControl.getPfPath() + " " + objMidServerServerControl.getServerName(),
										objMonCExec.getExpr());
							}

							list.add(objMidServerServerControl.getServerName() + "ִ�гɹ�");
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		
		Button checkServerStatusButton = new Button(buttonArea2, SWT.PUSH);
		checkServerStatusButton.setText("���server״̬");
		checkServerStatusButton.setLayoutData(new GridData());
		checkServerStatusButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				try {
					MidServerControl[] controls = getSeletedControls();
					if(controls.length ==0){
						RCPUtil.showError(getShell(), "����ѡ��һ��");
						return;
					}
					
					List list = new ArrayList();
					for (MidServerControl control : controls) {
						String info = HttpUtil.curl(control.getUrl(), 10);
						list.add(control.getServerName() + " " + info);
					}
					
					RCPUtil.showMessage(getShell(), StringUtils.join(list.iterator(), "\n"));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		Composite buttonArea3 = new Composite(bottom, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalSpan = 5;
		buttonArea3.setLayoutData(gridData);
		buttonArea3.setLayout(new GridLayout(5, false));
		
		Button setWebTraceButton = new Button(buttonArea3, SWT.PUSH);
		setWebTraceButton.setText("����web��trace");
		setWebTraceButton.setLayoutData(new GridData());
		setWebTraceButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				traceSetting("web");
			}
		});
		
		Button setAppTraceButton = new Button(buttonArea3, SWT.PUSH);
		setAppTraceButton.setText("����app��trace");
		setAppTraceButton.setLayoutData(new GridData());
		setAppTraceButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				traceSetting("app");
			}
		});
		
		label = new Label(buttonArea3, SWT.NULL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		label.setLayoutData(gridData);
	}
	
	/** ��ȡ��ѡ��MidServerControl*/
	private MidServerControl[] getSeletedControls(){
		TableItem[] items = tableViewer.getTable().getItems();
		
		List<MidServerControl> selectItems = new ArrayList<MidServerControl>();
		
		for (TableItem item : items) {
			if(item.getChecked()){
				selectItems.add((MidServerControl)item.getData());
			}
		}
		
		return selectItems.toArray(new MidServerControl[0]);
	}
	
	/** ����Trace�߼�*/
	private void traceSetting(String type){
		MidServerControl[] controls = getSeletedControls();
		if(controls.length ==0){
			RCPUtil.showError(getShell(), "����ѡ��һ��");
			return;
		}
		
		if(!RCPUtil.showConfirm(getShell(), "�����������" + type + "��trace,��Ҫ����" + type + "��trace��?")){
			return;
		}
		
		StringBuffer sb = new StringBuffer("����" + type + "��trace��server��:\n");
		for (MidServerControl control : controls) {
			sb.append(control.getServerName()).append("\n");
			if(control.getGrpName().toUpperCase().indexOf(type.toUpperCase()) == -1){
				RCPUtil.showMessage(getShell(), control.getServerName()+",����" + type+ ",��������web��trace");
				return;
			}
		}
		
		if(!RCPUtil.showConfirm(getShell(), sb.toString())){
			return;
		}
		
		int intType = TraceSettingDialog.APP_TYPE;
		if(type.equalsIgnoreCase("APP")){
			intType = TraceSettingDialog.APP_TYPE;
		}else if(type.equalsIgnoreCase("WEB")){
			intType = TraceSettingDialog.WEB_TYPE;
		}
		
		TraceSettingDialog diagDialog = new TraceSettingDialog(getShell(), controls, intType);
		diagDialog.open();
	}
	
	public static void main(String[] args) throws Exception{
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		
		new ServerInfoView(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
