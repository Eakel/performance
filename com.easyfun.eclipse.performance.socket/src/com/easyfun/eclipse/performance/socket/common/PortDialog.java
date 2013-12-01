package com.easyfun.eclipse.performance.socket.common;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.easyfun.eclipse.performance.socket.table.PortLabelProvider;
import com.easyfun.eclipse.performance.socket.table.PortModel;


/**
 * A JFace dialog for set/change the password.
 * 
 * @author zhaoming
 * 
 * Jan 23, 2008
 */
public class PortDialog extends Dialog {
	
    public static final int UDP = 1;
    public static final int TCP = 2;

	private String port;
	private TableViewer tableViewer;
	
	String title = "Standard TCP Port";
	
	public PortDialog(Shell shell, int type) {
		super(shell);
		if(type==TCP) {
            title = ("Standard TCP Port");
//            model = new PortModel();
        } else {
        	title = ("Select UDP port");
//            model = new PortModel();
        }
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Standard TCP Port");
	}

	protected Control createDialogArea(Composite composite) {
		Composite parent = new Composite(composite, SWT.NULL);
		GridLayout layout1 = new GridLayout(1, false);
		parent.setLayout(layout1);		
				
		Table table = new Table(parent, SWT.BORDER|SWT.FULL_SELECTION);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 300;			
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TableColumn col1 = new TableColumn(table, SWT.NULL);
		col1.setText("Port No");
		col1.setWidth(80);
		
		TableColumn col2 = new TableColumn(table, SWT.NULL);
		col2.setText("Use");
		col2.setWidth(150);
		
		TableColumn col3 = new TableColumn(table, SWT.NULL);
		col3.setText("Description");
		col3.setWidth(250);
		
		tableViewer = new TableViewer(table);
		
		tableViewer.setColumnProperties(new String[]{"tt", "dd"});
		
		tableViewer.setLabelProvider(new PortLabelProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(PortModel.createModel("udpports.txt"));	
		
		tableViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event) {
				Object obj = ((IStructuredSelection)tableViewer.getSelection()).getFirstElement();
				PortModel model = (PortModel)obj;
				PortDialog.this.port = model.getPort();
				PortDialog.this.close();
			}
		});

		return parent;
	}
	
	protected void okPressed() {
		Object obj = ((IStructuredSelection)tableViewer.getSelection()).getFirstElement();
		if(obj == null){
			return;
		}
		PortModel model = (PortModel)obj;
		this.port = model.getPort();
		super.okPressed();
	}
	
	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}
	
}
