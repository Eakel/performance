package com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.ai.appframe2.complex.mbean.standard.session.AppframeSessionMonitorMBean;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.helper.SessionModel;
import com.easyfun.eclipse.rcp.ColumnViewerSorter;
import com.easyfun.eclipse.rcp.RCPUtil;

/**
 * AppFrame Session
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-4
 */
public class SessionComposite extends Composite implements IDataInit{
	private static Log log = LogFactory.getLog(SessionComposite.class);
	private TableViewer tableViewer;
	private Label countPeopleLabel;
	
	public SessionComposite(Composite parent) {
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
	}
	
	public void initData(InvokeModel invokeModel) throws Exception{
		AppframeSessionMonitorMBean objAppframeSessionMonitorMBean = null;
		try {
			objAppframeSessionMonitorMBean = (AppframeSessionMonitorMBean) invokeModel.getObject(AppframeSessionMonitorMBean.class);

			HashMap[] objUserInfo = objAppframeSessionMonitorMBean.fetchLogedUsers();
			List<SessionModel> list = new ArrayList<SessionModel>();
			for (int i = 0; i < objUserInfo.length; i++) {
				if (objUserInfo[i] != null) {
					SessionModel model = new SessionModel();
					model.setServerName((String) objUserInfo[i].get("SERVER_NAME"));
					model.setIp((String) objUserInfo[i].get("IP"));
					model.setCode((String) objUserInfo[i].get("CODE"));
					model.setName((String) objUserInfo[i].get("NAME"));
					model.setOrgName((String) objUserInfo[i].get("ORG_NAME"));
					model.setLoginTime(((Timestamp) objUserInfo[i].get("LOGIN_TIME")).toString());
					model.setSessionId((String) objUserInfo[i].get("SESSION_ID"));
					model.setAttrs(objUserInfo[i].get("ATTRS").toString());
					list.add(model);
				}
			}
			tableViewer.setInput(list);
			
			countPeopleLabel.setText(list.size() + "");
		} catch (Exception ex) {
			log.error("异常", ex);
		} finally {
			if (objAppframeSessionMonitorMBean != null)
				ClientProxy.destroyObject(objAppframeSessionMonitorMBean);
		}
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
		label.setText("Session信息");
		RCPUtil.setBold(label);
		
		Composite c2 = new Composite(parent, SWT.NULL);
		c2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c2.setLayout(new GridLayout(4, false));
		
		Button button = new Button(c2, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("查询Session信息");
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
			}
		});
		
		button = new Button(c2, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("拷贝到剪切板");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sb = new StringBuffer();
				List<SessionModel> input = (List<SessionModel>)tableViewer.getInput();
				sb.append("工号").append("\t").append("用户名称").append("\t").append("组织名称").append("\t")
				.append("登录时间").append("\t").append("客户端IP").append("\t").append("serverName").append("\t")
				.append("SESSIONID").append("\n");
				for (SessionModel model : input) {
					sb.append(model.getCode()).append("\t").append(model.getName()).append("\t").append(model.getOrgName()).append("\t")
					.append(model.getLoginTime()).append("\t").append(model.getIp()).append("\t").append(model.getServerName()).append("\t")
					.append(model.getSessionId()).append("\n");
				}
				
				String content = sb.toString();
				
				RCPUtil.copyToClipboard(getShell(), content);
			}
		});
		
		label = new Label(c2, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("登录人数：");
		
		countPeopleLabel = new Label(c2, SWT.NULL);
		GridData gridData = new GridData();
		gridData.widthHint = 60;
		countPeopleLabel.setLayoutData(gridData);
		countPeopleLabel.setText(" ");
		
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("工号");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof SessionModel){
					SessionModel model = (SessionModel)element;
					return model.getCode();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SessionModel && e2 instanceof SessionModel){
					SessionModel c1 = (SessionModel) e1;
					SessionModel c2 = (SessionModel) e2;
					return c1.getCode().compareTo(c2.getCode());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("用户名称");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof SessionModel){
					SessionModel model = (SessionModel)element;
					return model.getName();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SessionModel && e2 instanceof SessionModel){
					SessionModel c1 = (SessionModel) e1;
					SessionModel c2 = (SessionModel) e2;
					return c1.getName().compareTo(c2.getName());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("组织名称");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof SessionModel){
					SessionModel model = (SessionModel)element;
					return model.getOrgName();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SessionModel && e2 instanceof SessionModel){
					SessionModel c1 = (SessionModel) e1;
					SessionModel c2 = (SessionModel) e2;
					return c1.getOrgName().compareTo(c2.getOrgName());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("登录时间");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof SessionModel){
					SessionModel model = (SessionModel)element;
					return model.getLoginTime();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SessionModel && e2 instanceof SessionModel){
					SessionModel c1 = (SessionModel) e1;
					SessionModel c2 = (SessionModel) e2;
					return c1.getLoginTime().compareTo(c2.getLoginTime());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("客户端IP");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof SessionModel){
					SessionModel model = (SessionModel)element;
					return model.getIp();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SessionModel && e2 instanceof SessionModel){
					SessionModel c1 = (SessionModel) e1;
					SessionModel c2 = (SessionModel) e2;
					return c1.getIp().compareTo(c2.getIp());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("serverName");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof SessionModel){
					SessionModel model = (SessionModel)element;
					return model.getServerName();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SessionModel && e2 instanceof SessionModel){
					SessionModel c1 = (SessionModel) e1;
					SessionModel c2 = (SessionModel) e2;
					return c1.getServerName().compareTo(c2.getServerName());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("SESSIONID");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof SessionModel){
					SessionModel model = (SessionModel)element;
					return model.getSessionId();
				}else{
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof SessionModel && e2 instanceof SessionModel){
					SessionModel c1 = (SessionModel) e1;
					SessionModel c2 = (SessionModel) e2;
					return c1.getSessionId().compareTo(c2.getSessionId());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
	}

}
