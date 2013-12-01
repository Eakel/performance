package com.easyfun.eclipse.performance.jmx.dialog;

import javax.management.MBeanOperationInfo;
import javax.management.ObjectName;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

import com.easyfun.eclipse.performance.jmx.model.MBeanModel;

/**
 * @author linzhaoming
 * Create Date: 2010-8-14
 */
public class JMXBeanViewDialog extends TitleAreaDialog {
	private MBeanModel mBeanModel;
	public JMXBeanViewDialog(Shell shell, MBeanModel mBeanModel) {
		super(shell);
		this.mBeanModel = mBeanModel;		
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
		
		createMBeanDescription(composite);
		createMBeanAttributes(composite);
		createMBeanOperations(composite);
		
		setTitle(getDescription());		
		return parent;
	}

	protected void okPressed() {
		super.okPressed();
	}
	
	private void createMBeanDescription(Composite parent){
		Group group = new Group(parent, SWT.NULL);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout());
		group.setText("MBean Description");
		String description = "";
		try {
			description = mBeanModel.getConnection().getMBeanInfo(mBeanModel.getObjectName()).getDescription();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(description == null){	//maybe null
			description = "";
		}
		Label label = new Label(group, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText(description);
	}
	
	private void createMBeanAttributes(Composite parent){
		Group group = new Group(parent, SWT.NULL);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout());
		group.setText("MBean Attributes");
		TableViewer tableViewer = new TableViewer(group, SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.setLabelProvider(new MBeanTableLabelProvider());
		tableViewer.setContentProvider(new MBeanContentProvider(MBeanContentProvider.TYPE_ATTRIBUTE));
		TableColumn col = new TableColumn(tableViewer.getTable(),SWT.NONE);
		col.setWidth(200);
		col.setText("Name");
		
		col = new TableColumn(tableViewer.getTable(),SWT.NONE);
		col.setWidth(150);
		col.setText("Type");
		
		col = new TableColumn(tableViewer.getTable(),SWT.NONE);
		col.setWidth(80);
		col.setText("Access");
		
		col = new TableColumn(tableViewer.getTable(),SWT.NONE);
		col.setWidth(200);
		col.setText("Value");
		
		col = new TableColumn(tableViewer.getTable(),SWT.NONE);
		col.setWidth(200);
		col.setText("Description");
		
		tableViewer.setInput(mBeanModel);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				System.out.println(selection.getFirstElement());
//				MBeanOperationDialog dialog = new MBeanOperationDialog(getShell());
//				dialog.open();
			}
		});
	}
	
	private void createMBeanOperations(Composite parent){
		Group group = new Group(parent, SWT.NULL);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout());
		group.setText("MBean Operations");
		
		TableViewer tableViewer = new TableViewer(group, SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.setLabelProvider(new MBeanTableLabelProvider());
		tableViewer.setContentProvider(new MBeanContentProvider(MBeanContentProvider.TYPE_OPERATION));
		TableColumn col = new TableColumn(tableViewer.getTable(),SWT.NONE);
		col.setWidth(200);
		col.setText("Name");
		
		col = new TableColumn(tableViewer.getTable(),SWT.NONE);
		col.setWidth(150);
		col.setText("ReturnType");
		
		col = new TableColumn(tableViewer.getTable(),SWT.NONE);
		col.setWidth(200);
		col.setText("Description");
		
		col = new TableColumn(tableViewer.getTable(),SWT.NONE);
		col.setWidth(200);
		col.setText("Parameters");
		
		col = new TableColumn(tableViewer.getTable(),SWT.NONE);
		col.setWidth(200);
		col.setText("Description");
		
		tableViewer.setInput(mBeanModel);
		tableViewer.getTable().setLinesVisible(true);
		
		tableViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				Object select = selection.getFirstElement();
				if(select instanceof MBeanOperationInfo){
					MBeanOperationDialog dialog = new MBeanOperationDialog(getShell(), (MBeanOperationInfo)select);
					dialog.open();
				}
			}
		});
	}
	
	private String getDescription(){
		ObjectName objectName = mBeanModel.getObjectName();
		return "MBean: \t" + objectName.toString();
	}
}
