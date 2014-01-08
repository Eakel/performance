package com.easyfun.eclipse.performance.appframe.monitor.ui.remotelocal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MidServerControl;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ParallelUtil;
import com.easyfun.eclipse.performance.appframe.monitor.ui.common.ServerComposite;
import com.easyfun.eclipse.performance.appframe.monitor.ui.remotecache.CacheUtil;
import com.easyfun.eclipse.performance.appframe.monitor.ui.remotecache.HostDesc;
import com.easyfun.eclipse.rcp.ColumnViewerSorter;
import com.easyfun.eclipse.rcp.RCPUtil;


/**
 * CRM本地缓存: 产品本地/权限本地
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-2
 */
public class LocalCacheView extends Composite{
	
	private static final Log log = LogFactory.getLog(LocalCacheView.class);
	private TableViewer tableViewer;
	private ServerComposite serverComposite;
	private ComboViewer comboViewer;
	
	public LocalCacheView(Composite parent){
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
		
		initData();
	}
	
	private void initData() {
		serverComposite.initData("CRM-APP");
	}
	
	private void initControl(Composite parent){
		Composite top = new Composite(parent, SWT.NULL);
		top.setLayout(new GridLayout(1, true));
		top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createTopComposite(top);
		
		Composite content = new Composite(parent, SWT.NULL);
		content.setLayout(new GridLayout(1, false));
		content.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createContent(content);		
		
		Composite bottom = new Composite(parent, SWT.NULL);
		bottom.setLayout(new GridLayout(5, false));
		bottom.setLayoutData(new GridData(GridData.FILL_BOTH));
		createBottomComposite(bottom);
	}
	
	private void createTopComposite(Composite parent){		
		serverComposite = new ServerComposite(parent);
	}
	
	private void createContent(Composite parent){
		//line 3
		Composite c3 = new Composite(parent, SWT.NULL);
		c3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c3.setLayout(new GridLayout(5, false));
		
		comboViewer = new ComboViewer(c3, SWT.READ_ONLY);
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
		
		Button button = new Button(c3, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("查  询");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				long[] ids = serverComposite.getSelectId();
				try {
					if(ids.length ==0){
						RCPUtil.showError(getShell(), "至少选择一个");
						return;
					}

					List rtn = ParallelUtil.getMemcachedLocalCacheInfo(20, 8, ids);
					
					List<MemLocalModel> list = new ArrayList<MemLocalModel>();
					for (Iterator iter = rtn.iterator(); iter.hasNext();) {
						Map<String, ?> map = (Map) iter.next();
						MemLocalModel model = new MemLocalModel();
						try {
							model.setServerId(map.get("SERVER_ID").toString());
							model.setName(map.get("NAME").toString());
							model.setHitRate(map.get("HIT_RATE").toString());
							model.setSize(map.get("SIZE").toString());
							model.setCurrentByteSize(map.get("CURRENT_BYTE_SIZE").toString());
							model.setLimitBytes(map.get("LIMIT_BYTES").toString());
							model.setHit(map.get("HIT").toString());
							model.setMiss(map.get("MISS").toString());
							model.setEvit(map.get("EVICT").toString());
							model.setOverload(map.get("OVERLOAD").toString());
							model.setUptime(map.get("UPTIME").toString());
							model.setBucket(map.get("BUCKET").toString());
							list.add(model);
						} catch (Throwable ex) {
							log.error("异常", ex);
						}
					}
					
					tableViewer.setInput(list);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		button = new Button(c3, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("清除本地Cache");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				long[] ids = serverComposite.getSelectId();
				try {
					if(ids.length ==0){
						RCPUtil.showError(getShell(), "至少选择一个");
						return;
					}

					Map map = ParallelUtil.clearLocalCache(20, 8, ids);

					if (map.isEmpty()) {
						RCPUtil.showMessage(getShell(), "全部刷新成功");
					} else {
						StringBuffer sb = new StringBuffer();
						Collection c = map.values();
						for (Iterator iter = c.iterator(); iter.hasNext();) {
							String item = (String) iter.next();
							sb.append(item + "\n");
						}
						RCPUtil.showMessage(getShell(), "部分刷新成功\n" + sb.toString());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
	
	private void createBottomComposite(Composite parent){
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("服务器");
		column.getColumn().setWidth(150);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemLocalModel model = (MemLocalModel)element;
				return model.getName();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MidServerControl && e2 instanceof MidServerControl){
					MemLocalModel c1 = (MemLocalModel) e1;
					MemLocalModel c2 = (MemLocalModel) e2;
					return c1.getName().compareToIgnoreCase(c2.getName());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("命中率");
		column.getColumn().setWidth(60);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemLocalModel model = (MemLocalModel)element;
				return model.getHitRate();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemLocalModel && e2 instanceof MemLocalModel){
					MemLocalModel c1 = (MemLocalModel) e1;
					MemLocalModel c2 = (MemLocalModel) e2;
					return c1.getHitRate().compareToIgnoreCase(c2.getHitRate());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("对象数量");
		column.getColumn().setWidth(70);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemLocalModel model = (MemLocalModel)element;
				return model.getSize();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemLocalModel && e2 instanceof MemLocalModel){
					MemLocalModel c1 = (MemLocalModel) e1;
					MemLocalModel c2 = (MemLocalModel) e2;
					return c1.getSize().compareToIgnoreCase(c2.getSize());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("当前内存");
		column.getColumn().setWidth(70);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemLocalModel model = (MemLocalModel)element;
				return model.getCurrentByteSize();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemLocalModel && e2 instanceof MemLocalModel){
					MemLocalModel c1 = (MemLocalModel) e1;
					MemLocalModel c2 = (MemLocalModel) e2;
					return c1.getCurrentByteSize().compareToIgnoreCase(c2.getCurrentByteSize());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("最大内存");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemLocalModel model = (MemLocalModel)element;
				return model.getLimitBytes();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemLocalModel && e2 instanceof MemLocalModel){
					MemLocalModel c1 = (MemLocalModel) e1;
					MemLocalModel c2 = (MemLocalModel) e2;
					return c1.getLimitBytes().compareToIgnoreCase(c2.getLimitBytes());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("命中数");
		column.getColumn().setWidth(60);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemLocalModel model = (MemLocalModel)element;
				return model.getHit();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemLocalModel && e2 instanceof MemLocalModel){
					MemLocalModel c1 = (MemLocalModel) e1;
					MemLocalModel c2 = (MemLocalModel) e2;
					return c1.getHit().compareToIgnoreCase(c2.getHit());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("未命中数");
		column.getColumn().setWidth(60);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemLocalModel model = (MemLocalModel)element;
				return model.getMiss();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemLocalModel && e2 instanceof MemLocalModel){
					MemLocalModel c1 = (MemLocalModel) e1;
					MemLocalModel c2 = (MemLocalModel) e2;
					return c1.getOverload().compareToIgnoreCase(c2.getOverload());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("弹出数");
		column.getColumn().setWidth(60);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemLocalModel model = (MemLocalModel)element;
				return model.getEvit();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemLocalModel && e2 instanceof MemLocalModel){
					MemLocalModel c1 = (MemLocalModel) e1;
					MemLocalModel c2 = (MemLocalModel) e2;
					return c1.getEvit().compareToIgnoreCase(c2.getEvit());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("过载数");
		column.getColumn().setWidth(60);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemLocalModel model = (MemLocalModel)element;
				return model.getOverload();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemLocalModel && e2 instanceof MemLocalModel){
					MemLocalModel c1 = (MemLocalModel) e1;
					MemLocalModel c2 = (MemLocalModel) e2;
					return c1.getOverload().compareToIgnoreCase(c2.getOverload());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("UPTIME");
		column.getColumn().setWidth(160);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemLocalModel model = (MemLocalModel)element;
				return model.getUptime();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemLocalModel && e2 instanceof MemLocalModel){
					MemLocalModel c1 = (MemLocalModel) e1;
					MemLocalModel c2 = (MemLocalModel) e2;
					return c1.getUptime().compareToIgnoreCase(c2.getUptime());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("BUCKET");
		column.getColumn().setWidth(60);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				MemLocalModel model = (MemLocalModel)element;
				return model.getBucket();
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof MemLocalModel && e2 instanceof MemLocalModel){
					MemLocalModel c1 = (MemLocalModel) e1;
					MemLocalModel c2 = (MemLocalModel) e2;
					return c1.getBucket().compareToIgnoreCase(c2.getBucket());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("下载KEY文件");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return "下载";
			}
		});
	}
	
	public static void main(String[] args) throws Exception{
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		
		new LocalCacheView(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
