package com.easyfun.eclipse.performance.appframe.monitor.ui.unclosed;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ai.appframe2.complex.mbean.standard.datasource.UnclosedNewConnRuntime;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ParallelUtil;
import com.easyfun.eclipse.performance.appframe.monitor.ui.common.ServerComposite;
import com.easyfun.eclipse.rcp.ColumnViewerSorter;



/**
 * 
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-2
 */
public class UncloseConnectionView extends Composite{
	
	private static Log log = LogFactory.getLog(UncloseConnectionView.class);
	
	private TableViewer tableViewer;

	private ServerComposite serverComposite;

	public UncloseConnectionView(Composite parent){
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
		Composite c1 = new Composite(parent, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c1.setLayout(new GridLayout(2, false));
		
		Button button = new Button(c1, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("拷贝到剪切板");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
			}
		});
		
		button = new Button(c1, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("查  询");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				try {
					long[] tmpIds = serverComposite.getSelectId();
					List rtn = ParallelUtil.getUnClosedNewConnection(20, 8, tmpIds);
					
					List<UnclosedModel> list = new ArrayList<UnclosedModel>();
					for (Iterator iter = rtn.iterator(); iter.hasNext();) {
						UnclosedModel model = new UnclosedModel();
						HashMap map = (HashMap) iter.next();
						model.setServerId((String) map.get("SERVER_NAME"));
						model.setServerId((String) map.get("SERVER_ID"));
						UnclosedNewConnRuntime[] runtimes = (UnclosedNewConnRuntime[]) map.get("UNCLOSED_LIST");
						model.setRuntimes(runtimes);
						list.add(model);
					}
					
					tableViewer.setInput(list);
				} catch (Exception ex) {
					log.error("异常", ex);
				}
			}
		});
		
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("服务器");
		column.getColumn().setWidth(200);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof UnclosedModel){
					UnclosedModel model = (UnclosedModel)element;
					return model.getServerName();
				}else{
					return element.toString();
				}
			}
		});
		
		//TODO: UnclosedModel需要用表格树来展示
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("开始时间");
		column.getColumn().setWidth(200);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
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
		column.getColumn().setText("持续时间(秒)");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
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
		column.getColumn().setText("数据源");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
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
		column.getColumn().setText("UUID");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
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
		column.getColumn().setText("强制关闭");
		column.getColumn().setWidth(100);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
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
		
		new UncloseConnectionView(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
