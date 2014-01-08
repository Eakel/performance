package com.easyfun.eclipse.performance.appframe.monitor.ui.query;
import java.util.HashMap;
import java.util.Map;

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
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.rcp.ColumnViewerSorter;
import com.easyfun.eclipse.rcp.RCPUtil;


/**
 * 
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-2
 */
public class MonitorQueryView extends Composite{
	
	private TableViewer tableViewer;
	private Combo isTriggerCombo;
	private Combo monTypeCombo;
	private Combo businessTypeCombo;
	private Text infoNameText;
	private Text doneDateText;
	private Text createDateText;
	private Text hostNameText;
	private Text ipText;


	public MonitorQueryView(Composite parent){
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
		label.setText("监控IP：");
		
		ipText = new Text(parent, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.widthHint = 100;
		ipText.setLayoutData(gridData);
		ipText.setText("");
		
		label = new Label(parent, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("监控主机名称：");
		
		hostNameText = new Text(parent, SWT.BORDER);
		gridData = new GridData();
		gridData.widthHint = 100;
		hostNameText.setLayoutData(gridData);
		hostNameText.setText("");
		
		label = new Label(parent, SWT.NULL);
		label.setLayoutData(new GridData());
		
		//line 2
		label = new Label(parent, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("业务归属区域：");
		
		businessTypeCombo = new Combo(parent, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.widthHint = 85;
		businessTypeCombo.setLayoutData(gridData);
		businessTypeCombo.add("全部");
		businessTypeCombo.add("CRM(APP)主机");
		businessTypeCombo.select(0);
		
		label = new Label(parent, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("监控类型：");
		
		monTypeCombo = new Combo(parent, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.widthHint = 85;
		monTypeCombo.setLayoutData(gridData);
		monTypeCombo.add("全部");
		monTypeCombo.add("TABLE");
		monTypeCombo.add("EXEC");
		monTypeCombo.select(0);
		
		label = new Label(parent, SWT.NULL);
		label.setLayoutData(new GridData());
		
		//line 3
		label = new Label(parent, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("开始监控时间：");
		
		createDateText = new Text(parent, SWT.BORDER);
		gridData = new GridData();
		gridData.widthHint = 100;
		createDateText.setLayoutData(gridData);
		createDateText.setText("");
		
		label = new Label(parent, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("完成监控时间：");
		
		doneDateText = new Text(parent, SWT.BORDER);
		gridData = new GridData();
		gridData.widthHint = 100;
		doneDateText.setLayoutData(gridData);
		doneDateText.setText("");
		
		label = new Label(parent, SWT.NULL);
		label.setLayoutData(new GridData());
		
		//line 4
		label = new Label(parent, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("是否触发告警：");
		
		isTriggerCombo = new Combo(parent, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.widthHint = 85;
		isTriggerCombo.setLayoutData(gridData);
		isTriggerCombo.add("全部");
		isTriggerCombo.add("Y");
		isTriggerCombo.add("N");
		isTriggerCombo.select(0);
		
		label = new Label(parent, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("监控名称：");
		
		infoNameText = new Text(parent, SWT.BORDER);
		gridData = new GridData();
		gridData.widthHint = 100;
		infoNameText.setLayoutData(gridData);
		infoNameText.setText("");
		
		Button button = new Button(parent, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("查  询");
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				Map condition = new HashMap();

				condition.put("IP",ipText.getText().trim());
				condition.put("HOSTNAME",hostNameText.getText().trim());

//				condition.put("BUSI_AREA",BUSI_AREA.value);
				condition.put("MON_TYPE",monTypeCombo.getItem(monTypeCombo.getSelectionIndex()));

				String createDate = createDateText.getText().trim();
				if (StringUtils.isEmpty(createDate)) {
					RCPUtil.showError(getShell(), "必须输入开始时间");
					return;
				}
				String doneDate = doneDateText.getText().trim();
				if (StringUtils.isEmpty(doneDate)) {
					RCPUtil.showError(getShell(), "必须输入结束时间");
					return;
				}

//				if(computeMonthDiff(createDate,doneDate)){
				  condition.put("CREATE_DATE",createDateText.getText().trim());
				  condition.put("DONE_DATE",doneDateText.getText().trim());
				  condition.put("IS_TRIGGER_WARN",isTriggerCombo.getItem(isTriggerCombo.getSelectionIndex()));
				  condition.put("INFO_NAME",infoNameText.getText().trim());
//				  DataGrid_RefreshPage("MonRecord",condition);
//				}
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
		column.getColumn().setText("告警级别");
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
		column.getColumn().setText("是否告警");
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
		column.getColumn().setText("监控名称");
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
		column.getColumn().setText("监控值");
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
		column.getColumn().setText("监控类型");
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
		column.getColumn().setText("主机");
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
		Button button = new Button(parent, SWT.NULL);
		button.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false));
		button.setText("查看告警信息");
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
		
		new MonitorQueryView(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
