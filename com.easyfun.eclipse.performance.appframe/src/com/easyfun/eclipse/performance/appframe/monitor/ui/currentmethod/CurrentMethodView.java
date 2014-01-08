package com.easyfun.eclipse.performance.appframe.monitor.ui.currentmethod;
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

import com.easyfun.eclipse.rcp.ColumnViewerSorter;


/**
 * 当前方法调用
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-2
 */
public class CurrentMethodView extends Composite{
	
	private TableViewer serverTableViewer;
	private static TableViewer methodTableViewer;

	public CurrentMethodView(Composite parent){
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
		top.setLayoutData(gridData);
		top.setLayout(new GridLayout(1, true));
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
		Button button = new Button(parent, SWT.PUSH);
		button.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false));
		button.setText("刷  新");
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
			}
		});
	}
	
	private void createContent(Composite parent){
		serverTableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		serverTableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		serverTableViewer.getTable().setHeaderVisible(true);
		serverTableViewer.getTable().setLinesVisible(true);
		serverTableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(serverTableViewer, SWT.NULL);
		column.getColumn().setText("0");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
			}
		});
		
		new ColumnViewerSorter(serverTableViewer, column) {
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
		
		column = new TableViewerColumn(serverTableViewer, SWT.NULL);
		column.getColumn().setText("总计");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
			}
		});
		
		new ColumnViewerSorter(serverTableViewer, column) {
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
		
		column = new TableViewerColumn(serverTableViewer, SWT.NULL);
		column.getColumn().setText("运行服务器");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
			}
		});
		
		new ColumnViewerSorter(serverTableViewer, column) {
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
	
	private static void createBottomComposite(Composite parent){
		methodTableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		methodTableViewer.getTable().setLayoutData(gridData);
		methodTableViewer.getTable().setHeaderVisible(true);
		methodTableViewer.getTable().setLinesVisible(true);
		methodTableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(methodTableViewer, SWT.NULL);
		column.getColumn().setText("ServerName");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
			}
		});
		
		new ColumnViewerSorter(methodTableViewer, column) {
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
		
		column = new TableViewerColumn(methodTableViewer, SWT.NULL);
		column.getColumn().setText("运行方法");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return element.toString();
			}
		});
		
		new ColumnViewerSorter(methodTableViewer, column) {
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
		
		new CurrentMethodView(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
