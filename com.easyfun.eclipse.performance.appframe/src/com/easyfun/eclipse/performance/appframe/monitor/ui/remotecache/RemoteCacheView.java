package com.easyfun.eclipse.performance.appframe.monitor.ui.remotecache;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
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

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MidServerControl;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonNode;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPage;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonServer;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MemcachedStatUtil;
import com.easyfun.eclipse.rcp.ColumnViewerSorter;


/**
 * CRM远程缓存
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-2
 */
public class RemoteCacheView extends Composite{
	
	private static final Log log = LogFactory.getLog(RemoteCacheView.class);
	private TableViewer tableViewer;
	private ComboViewer comboViewer;


	public RemoteCacheView(Composite parent){
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
	}
	
	private void initData(long serverId){
		try {
			IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
			MonServer objMonMbeanServer = objIMonSV.getMonServerByServerId(serverId);
			String str = objMonMbeanServer.getLocator();

			String[] tmp = StringUtils.split(str, ",");
			List<MemCacheModel> list = new ArrayList<MemCacheModel>();
			for (int i = 0; i < tmp.length; i++) {
				try {
					String[] tmp2 = StringUtils.split(tmp[i], ":");
					HashMap<String, String> map = MemcachedStatUtil.getStat(tmp2[0].trim(), Integer.parseInt(tmp2[1]));
					MemCacheModel model = new MemCacheModel();
					model.setServerHost(tmp[i]);
					model.setCurrConns(map.get("curr_connections"));
					model.setCmdGet(map.get("cmd_get"));
					model.setBytes(map.get("bytes"));
					model.setCurrItems(map.get("curr_items"));
					model.setTotalItems(map.get("total_items"));
					model.setCmdSet(map.get("cmd_set"));
					model.setEvictions(map.get("evictions"));
					model.setUptime(map.get("uptime"));
					model.setGetHint(map.get("get_hits"));
					model.setLimitMaxbytes(map.get("limit_maxbytes"));
					model.setBytesWritten(map.get("bytes_written"));
					model.setBytesRead(map.get("bytes_read"));
					list.add(model);
				} catch (Exception ex) {
					log.error("获取信息出错", ex);
				}
			}
			
			tableViewer.setInput(list);
		} catch (Exception ex) {
			log.error("异常", ex);
		}
	}
	
	private long getServerId(){
		try {
			HostDesc bean = (HostDesc)((IStructuredSelection)comboViewer.getSelection()).getFirstElement();
			long nodeId = bean.getServerId();
			IMonSV objIMonSV = (IMonSV)ServiceFactory.getService(IMonSV.class);
			MonNode objMonNode = objIMonSV.getMonNodeByNodeId(nodeId);
			long pageId = objMonNode.getPageId();
			MonPage objMonPage = objIMonSV.getMonPageByPageId(pageId);
			String serverId = String.valueOf(objMonPage.getServerId());
			return Long.parseLong(serverId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
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
		comboViewer = new ComboViewer(parent, SWT.READ_ONLY);
		comboViewer.getCombo().setLayoutData(new GridData());
		comboViewer.setContentProvider(new ArrayContentProvider());
		List<HostDesc> list = CacheUtil.test();
		comboViewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				if (element instanceof HostDesc) {
					HostDesc desc = (HostDesc) element;
					return desc.getDesc();
				} else {
					return element.toString();
				}
			}
		});
		comboViewer.setInput(list);
		comboViewer.getCombo().select(0);
		
		Button button = new Button(parent, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("查  询");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				long serverId = getServerId();
				initData(serverId);
			}
		});
	}
	
	private void createContent(Composite parent){
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("主机和端口");
		column.getColumn().setWidth(120);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemCacheModel model = (MemCacheModel)element;
				return model.getServerHost();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MidServerControl && e2 instanceof MidServerControl){
					MemCacheModel c1 = (MemCacheModel) e1;
					MemCacheModel c2 = (MemCacheModel) e2;
					return c1.getServerHost().compareToIgnoreCase(c2.getServerHost());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("当前连接数");
		column.getColumn().setWidth(80);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemCacheModel model = (MemCacheModel)element;
				return model.getCurrConns();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemCacheModel && e2 instanceof MemCacheModel){
					MemCacheModel c1 = (MemCacheModel) e1;
					MemCacheModel c2 = (MemCacheModel) e2;
					return c1.getCurrConns().compareToIgnoreCase(c2.getCurrConns());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("总查询次数");
		column.getColumn().setWidth(80);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemCacheModel model = (MemCacheModel)element;
				return model.getCmdGet();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemCacheModel && e2 instanceof MemCacheModel){
					MemCacheModel c1 = (MemCacheModel) e1;
					MemCacheModel c2 = (MemCacheModel) e2;
					return c1.getCmdGet().compareToIgnoreCase(c2.getCmdGet());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("内存对象字节数");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemCacheModel model = (MemCacheModel)element;
				return model.getBytes();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemCacheModel && e2 instanceof MemCacheModel){
					MemCacheModel c1 = (MemCacheModel) e1;
					MemCacheModel c2 = (MemCacheModel) e2;
					return c1.getBytes().compareToIgnoreCase(c2.getBytes());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("当前对象数量");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemCacheModel model = (MemCacheModel)element;
				return model.getCurrItems();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemCacheModel && e2 instanceof MemCacheModel){
					MemCacheModel c1 = (MemCacheModel) e1;
					MemCacheModel c2 = (MemCacheModel) e2;
					return c1.getCurrItems().compareToIgnoreCase(c2.getCurrItems());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("所有对象数量");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemCacheModel model = (MemCacheModel)element;
				return model.getTotalItems();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemCacheModel && e2 instanceof MemCacheModel){
					MemCacheModel c1 = (MemCacheModel) e1;
					MemCacheModel c2 = (MemCacheModel) e2;
					return c1.getTotalItems().compareToIgnoreCase(c2.getTotalItems());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("设置对象次数");
		column.getColumn().setWidth(90);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemCacheModel model = (MemCacheModel)element;
				return model.getCmdSet();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemCacheModel && e2 instanceof MemCacheModel){
					MemCacheModel c1 = (MemCacheModel) e1;
					MemCacheModel c2 = (MemCacheModel) e2;
					return c1.getCmdSet().compareToIgnoreCase(c2.getCmdSet());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("弹出对象次数");
		column.getColumn().setWidth(90);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemCacheModel model = (MemCacheModel)element;
				return model.getEvictions();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemCacheModel && e2 instanceof MemCacheModel){
					MemCacheModel c1 = (MemCacheModel) e1;
					MemCacheModel c2 = (MemCacheModel) e2;
					return c1.getEvictions().compareToIgnoreCase(c2.getEvictions());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("uptime");
		column.getColumn().setWidth(80);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemCacheModel model = (MemCacheModel)element;
				return model.getUptime();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemCacheModel && e2 instanceof MemCacheModel){
					MemCacheModel c1 = (MemCacheModel) e1;
					MemCacheModel c2 = (MemCacheModel) e2;
					return c1.getUptime().compareToIgnoreCase(c2.getUptime());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("hint查询次数");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemCacheModel model = (MemCacheModel)element;
				return model.getGetHint();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemCacheModel && e2 instanceof MemCacheModel){
					MemCacheModel c1 = (MemCacheModel) e1;
					MemCacheModel c2 = (MemCacheModel) e2;
					return c1.getGetHint().compareToIgnoreCase(c2.getGetHint());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("限制内存字节数");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemCacheModel model = (MemCacheModel)element;
				return model.getLimitMaxbytes();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemCacheModel && e2 instanceof MemCacheModel){
					MemCacheModel c1 = (MemCacheModel) e1;
					MemCacheModel c2 = (MemCacheModel) e2;
					return c1.getLimitMaxbytes().compareToIgnoreCase(c2.getLimitMaxbytes());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("输出字节数");
		column.getColumn().setWidth(80);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemCacheModel model = (MemCacheModel)element;
				return model.getBytesWritten();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemCacheModel && e2 instanceof MemCacheModel){
					MemCacheModel c1 = (MemCacheModel) e1;
					MemCacheModel c2 = (MemCacheModel) e2;
					return c1.getBytesWritten().compareToIgnoreCase(c2.getBytesWritten());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("输入字节数");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemCacheModel model = (MemCacheModel)element;
				return model.getBytesRead();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemCacheModel && e2 instanceof MemCacheModel){
					MemCacheModel c1 = (MemCacheModel) e1;
					MemCacheModel c2 = (MemCacheModel) e2;
					return c1.getBytesRead().compareToIgnoreCase(c2.getBytesRead());
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
		
		new RemoteCacheView(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
