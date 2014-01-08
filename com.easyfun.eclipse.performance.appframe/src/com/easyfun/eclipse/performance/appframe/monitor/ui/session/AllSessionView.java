package com.easyfun.eclipse.performance.appframe.monitor.ui.session;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MiscUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ParallelUtil;
import com.easyfun.eclipse.performance.appframe.monitor.ui.common.ServerComposite;
import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.helper.SessionModel;
import com.easyfun.eclipse.rcp.ColumnViewerSorter;


/**
 * 
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-2
 */
public class AllSessionView extends Composite{
	private static Log log = LogFactory.getLog(AllSessionView.class);
	
	private TableViewer tableViewer;

	private ServerComposite serverComposite;

	public AllSessionView(Composite parent){
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
		
		initData();
	}
	
	private void initData(){
		try {
			serverComposite.initData("CRM-WEB");
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
		Button button = new Button(parent, SWT.PUSH);
		button.setText("查  询");
		button.setLayoutData(new GridData());
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				int count = 0;
				HashMap ip = new HashMap();
				HashMap regionSize = new HashMap();
				HashMap regionIP = new HashMap();

				Map otherInfo = new HashMap(); // linzm
				try {
					long[] tmpIds = serverComposite.getSelectId();
					List rtn = ParallelUtil.getSessionUserInfo(20, 8, tmpIds);
					List<SessionModel> list = new ArrayList<SessionModel>();
					for (Iterator iter = rtn.iterator(); iter.hasNext();) {
						HashMap[] objUserInfo = (HashMap[]) iter.next();
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
								model.setSerialId((String) objUserInfo[i].get("SERIAL_ID"));
								model.setSerialId(((Long) objUserInfo[i].get("SERVER_ID")).longValue() + "");
								Boolean isLogout = (Boolean) objUserInfo[i].get("IS_LOGOUTED");
								if (isLogout == null) {
									model.setIsLogouted("正常");
								} else {
									model.setIsLogouted("已注销");
								}

								count++;
								ip.put((String) objUserInfo[i].get("IP"), null);

								Object attrs = objUserInfo[i].get("ATTRS");
								if ((attrs != null) && ((attrs instanceof Map))) {
									Map map = (Map) attrs;
									String regionId = (String) map.get(MiscUtil.getUserInfoRegionIdKey());

									if (regionSize.containsKey(regionId)) {
										Integer tmpSize = (Integer) regionSize.get(regionId);
										regionSize.put(regionId, new Integer(tmpSize.intValue() + 1));
									} else {
										Integer tmpSize = new Integer(1);
										regionSize.put(regionId, new Integer(tmpSize.intValue()));
									}

									if (regionIP.containsKey(regionId)) {
										HashMap tmpMap = (HashMap) regionIP.get(regionId);
										tmpMap.put((String) objUserInfo[i].get("IP"), null);
										regionIP.put(regionId, tmpMap);
									} else {
										HashMap tmpMap = new HashMap();
										tmpMap.put((String) objUserInfo[i].get("IP"), null);
										regionIP.put(regionId, tmpMap);
									}
								}
								list.add(model);
							}
							
						}

					}
					tableViewer.setInput(list);
				} catch (Throwable ex) {
					log.error("异常", ex);
				}

				otherInfo.put("size" ,count);
				otherInfo.put("ip_size" ,count);
				if (!regionSize.isEmpty()) {
					Set key = regionSize.keySet();
					for (Iterator iter = key.iterator(); iter.hasNext();) {
						String item = (String) iter.next();
						otherInfo.put("real_region_id", item);
						otherInfo.put("size", ((Integer) regionSize.get(item)).intValue());
						otherInfo.put("ip_size", ((HashMap) regionIP.get(item)).size());
					}
				}
				
			}
		});
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.CHECK);
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
		column.getColumn().setText("IS_LOGOUTED");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof SessionModel){
					SessionModel model = (SessionModel)element;
					return model.getIsLogouted();
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
					return c1.getIsLogouted().compareTo(c2.getIsLogouted());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("强制注销");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return "TODO";
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
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("快捷Trace");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return "TODO";
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
		
		new AllSessionView(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
