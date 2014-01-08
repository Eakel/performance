package com.easyfun.eclipse.performance.appframe.monitor.ui.query;
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

import com.easyfun.eclipse.rcp.ColumnViewerSorter;
import com.easyfun.eclipse.rcp.RCPUtil;


/**
 * 
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-2
 */
public class MonImageView extends Composite{
	
	private TableViewer tableViewer;
	private static Combo timeTypeCombo;
	private static Combo timeRangeCombo;


	public MonImageView(Composite parent){
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
		top.setLayout(new GridLayout(5, true));
		createTopComposite(top);
		
		Composite content = new Composite(parent, SWT.NULL);
		content.setLayout(new GridLayout(1, false));
		content.setLayoutData(new GridData(GridData.FILL_BOTH));
		createContent(content);		
		
		Composite bottom = new Composite(parent, SWT.NULL);
		bottom.setLayout(new GridLayout(1, false));
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		bottom.setLayoutData(gridData);
		createBottomComposite(bottom);
	}
	
	private void createTopComposite(Composite parent){
		//line 1
		Label label = new Label(parent, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("监控组：");
		
		Combo combo = new Combo(parent, SWT.READ_ONLY);
		combo.setLayoutData(new GridData());
		combo.add("全部");
		combo.add("CRM测试环境主机");
		combo.add("CRM测试数据库");
		combo.select(0);		
		
		Button button = new Button(parent, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("查  询");
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
			}
		});
	}
	
	private void createContent(Composite parent){
		Composite c1 = new Composite(parent, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c1.setLayout(new GridLayout(1, false));
		
		Label label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("查询结果");
		RCPUtil.setBold(label);
		
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("告警ID");
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
		column.getColumn().setText("监控名称");
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
		column.getColumn().setText("监控类型");
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
		column.getColumn().setText("监控分组");
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
	
	private static void createBottomComposite(Composite parent){
		Composite c1 = new Composite(parent, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c1.setLayout(new GridLayout(4, false));
		
		Label label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("");
		
		timeTypeCombo = new Combo(c1, SWT.READ_ONLY);
		timeTypeCombo.setLayoutData(new GridData());
		timeTypeCombo.add("最近");
		timeTypeCombo.add("时间范围");
		timeTypeCombo.select(0);
		
		timeRangeCombo = new Combo(c1, SWT.READ_ONLY);
		timeRangeCombo.setLayoutData(new GridData());
		timeRangeCombo.add("1小时内");
		timeRangeCombo.add("2小时内");
		timeRangeCombo.select(0);
		
		label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("");
		
		Button button = new Button(parent, SWT.NULL);
		button.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false));
		button.setText("查看图像信息");
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
		
		new MonImageView(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
