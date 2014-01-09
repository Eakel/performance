package com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.ai.appframe2.complex.mbean.standard.action.ActionMonitorMBean;
import com.ai.appframe2.complex.mbean.standard.action.ActionSummary;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.TimeUtil;
import com.easyfun.eclipse.rcp.RCPUtil;

/**
 * AppFrame Action
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-4
 */
public class ActionComposite extends Composite implements IDataInit{
	protected static Log log = LogFactory.getLog(ActionComposite.class);
	
	private TableViewer tableViewer;
	private Text conditionText;
	
	public ActionComposite(Composite parent) {
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
	}
	
	public void initData(InvokeModel model) throws Exception{
		ActionMonitorMBean objActionMonitorMBean = null;
		try {
			objActionMonitorMBean = (ActionMonitorMBean)model.getObject(ActionMonitorMBean.class);
			ActionSummary[] objActionSummary = objActionMonitorMBean.fetchActionSummary(conditionText.getText().trim());
			tableViewer.setInput(objActionSummary);
		}catch (Exception ex){
			ex.printStackTrace();
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
		label.setText("Action��Ϣ");
		RCPUtil.setBold(label);
		
		Composite c2 = new Composite(parent, SWT.NULL);
		c2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c2.setLayout(new GridLayout(9, false));
		
		Button button = new Button(c2, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("��ѯAction��Ϣ");
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
			}
		});
		
		conditionText = new Text(c2, SWT.BORDER);
		conditionText.setLayoutData(new GridData());
		conditionText.setText("");
		
		button = new Button(c2, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("���״̬");
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
			}
		});
		
		button = new Button(c2, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("�򿪼��");
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
			}
		});
		
		label = new Label(c2, SWT.NULL);
		GridData gridData = new GridData();
		label.setLayoutData(gridData);
		label.setText("ʱ��(��)");
		
		Text text = new Text(c2, SWT.BORDER);
		gridData = new GridData();
		gridData.widthHint = 60;
		text.setLayoutData(gridData);
		text.setText("10");
		
		button = new Button(c2, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("�رռ��");
		
		button = new Button(c2, SWT.NULL);
		button.setLayoutData(new GridData());
		button.setText("���������а�");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sb = new StringBuffer();
				ActionSummary[] input = (ActionSummary[])tableViewer.getInput();
				sb.append("��").append("\t").append("������").append("\t").append("��С��ʱ(ms)").append("\t")
				.append("����ʱ(ms)").append("\t").append("ƽ����ʱ(ms)").append("\t").append("�ܴ���").append("\t")
				.append("�ɹ�����").append("\t").append("ʧ�ܴ���").append("\t").append("���һ�κ�ʱ(ms)").append("\t")
				.append("�ܺ�ʱ(ms)").append("���һ�ε���ʱ��").append("\n");
				for (ActionSummary model : input) {
					sb.append(model.getClassName()).append("\t").append(model.getMethodName()).append("\t").append(model.getMin()).append("\t")
					.append(model.getMax()).append("\t").append(model.getAvg()).append("\t").append(model.getTotalCount()).append("\t")
					.append(model.getSuccessCount()).append("\t").append(model.getFailCount()).append("\t").append(model.getLastUseTime()).append("\t")
					.append(model.getTotalUseTime()).append("\t").append(TimeUtil.format(new Date(model.getLast()))).append("\t")
					.append("\n");
				}
				
				String content = sb.toString();
				
				RCPUtil.copyToClipboard(getShell(), content);
				
			}
		});
		
		label = new Label(c2, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("");
		
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("��");
		column.getColumn().setWidth(200);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof ActionSummary){
					ActionSummary model = (ActionSummary)element;
					return model.getClassName();
				}else{
					return element.toString();
				}
			}
		});
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("������");
		column.getColumn().setWidth(60);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof ActionSummary){
					ActionSummary model = (ActionSummary)element;
					return model.getMethodName();
				}else{
					return element.toString();
				}
			}
		});
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("��С��ʱ(ms)");
		column.getColumn().setWidth(90);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof ActionSummary){
					ActionSummary model = (ActionSummary)element;
					return "" + model.getMin();
				}else{
					return element.toString();
				}
			}
		});
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("����ʱ(ms)");
		column.getColumn().setWidth(90);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof ActionSummary){
					ActionSummary model = (ActionSummary)element;
					return "" +  model.getMax();
				}else{
					return element.toString();
				}
			}
		});
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("ƽ����ʱ(ms)");
		column.getColumn().setWidth(90);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof ActionSummary){
					ActionSummary model = (ActionSummary)element;
					return "" + model.getAvg();
				}else{
					return element.toString();
				}
			}
		});
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("�ܴ���");
		column.getColumn().setWidth(60);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof ActionSummary){
					ActionSummary model = (ActionSummary)element;
					return model.getTotalCount() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("�ɹ�����");
		column.getColumn().setWidth(60);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof ActionSummary){
					ActionSummary model = (ActionSummary)element;
					return model.getSuccessCount() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("ʧ�ܴ���");
		column.getColumn().setWidth(60);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof ActionSummary){
					ActionSummary model = (ActionSummary)element;
					return model.getFailCount() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("���һ�κ�ʱ(ms)");
		column.getColumn().setWidth(110);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof ActionSummary){
					ActionSummary model = (ActionSummary)element;
					return model.getLastUseTime() + "";
				}else{
					return element.toString();
				}
			}
		});
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("�ܺ�ʱ(ms)");
		column.getColumn().setWidth(80);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof ActionSummary){
					ActionSummary model = (ActionSummary)element;
					return model.getTotalUseTime()  + "";
				}else{
					return element.toString();
				}
			}
		});
		
		column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("���һ�ε���ʱ��");
		column.getColumn().setWidth(150);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if(element instanceof ActionSummary){
					ActionSummary model = (ActionSummary)element;
					;
					return TimeUtil.format(new Date(model.getLast()));
				}else{
					return element.toString();
				}
			}
		});
	}

}
