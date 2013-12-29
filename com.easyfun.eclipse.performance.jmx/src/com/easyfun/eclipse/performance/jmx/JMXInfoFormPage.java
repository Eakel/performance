package com.easyfun.eclipse.performance.jmx;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import com.easyfun.eclipse.performance.jmx.model.MBeanModel;
import com.easyfun.eclipse.util.ui.SWTUtil;

/**
 * FormPage for the tomcat services
 * 
 * @author linzhaoming Create Date: 2010-4-10
 */
public class JMXInfoFormPage extends FormPage {
	
	private FormToolkit toolkit;	
	private ScrolledForm form;
	
	private MBeanModel mbeanModel;
	
	private Table attrsTable;
	
	private Table opertable;
	
	private Table paraTable;
	
	public JMXInfoFormPage(FormEditor editor, String id, String title, MBeanModel mbeanModel) {
		super(editor, id, title);
		this.mbeanModel = mbeanModel;
	}

	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		
		this.toolkit = managedForm.getToolkit();
		this.form = managedForm.getForm();
		
		Composite formComposite = form.getBody();
		formComposite.setLayout(new GridLayout());
		
		createInfoSection(formComposite);	
		
		createAttrsSection(formComposite);
		
		createOpersSection(formComposite);
		
		fillAttrbiteData();
		
		initOpersData();
	}

	private void createInfoSection(Composite parent) {
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR);
		section.setText("MBean Name");
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Composite composite = toolkit.createComposite(section);
		section.setClient(composite);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout(2, false));

		Label label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("Domain Name");
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText(mbeanModel.getObjectName().getDomain());
		SWTUtil.setBold(label);
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("Type");
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData());
		String dispName = mbeanModel.getDisplayName();
		int index = mbeanModel.getObjectName().getDomain().length();
		//:type=长度为6 
		label.setText(dispName.substring(index + 6));
		
		Hashtable hash = mbeanModel.getObjectName().getKeyPropertyList();
		
		
//		System.out.println(mbeanModel.getObjectName().);
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("MBean Class Name");
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData());
		
		//Get MBean Java Class Name
		MBeanServerConnection conn = mbeanModel.getConnection();		
		try {
			Set set = conn.queryMBeans(mbeanModel.getObjectName(), null);
			Iterator iter = set.iterator();
			ObjectInstance instance = null;
			while (iter.hasNext()) {
				instance = (ObjectInstance) iter.next();
				break;
			}
			
			String className = "";
			if(instance != null){
				className = instance.getClassName();
			}

			label.setText(className);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SWTUtil.setBold(label);
		
		toolkit.paintBordersFor(composite);
	}
	
	/** Attributes Section*/
	private void createAttrsSection(Composite parent) {
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR|ExpandableComposite.TWISTIE);
		section.setText("All Attributes");
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite notesComposite = toolkit.createComposite(section);
		section.setClient(notesComposite);
		notesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		notesComposite.setLayout(new GridLayout());
		section.setExpanded(true);
		
		attrsTable = new Table(notesComposite, SWT.BORDER|SWT.FULL_SELECTION);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 50;
		attrsTable.setLayoutData(gridData);
		attrsTable.setHeaderVisible(true);
		
		TableColumn col = new TableColumn(attrsTable, SWT.NULL);
		col.setText("Name");
		col.setWidth(100);		
		
		col = new TableColumn(attrsTable, SWT.NULL);
		col.setText("Type");
		col.setWidth(200);
		
		col = new TableColumn(attrsTable, SWT.NULL);
		col.setText("Access");
		col.setWidth(100);
		
		col = new TableColumn(attrsTable, SWT.NULL);
		col.setText("Value");
		col.setWidth(200);
		
		col = new TableColumn(attrsTable, SWT.NULL);
		col.setText("Description");
		col.setWidth(300);
		
		toolkit.paintBordersFor(notesComposite);
	}
	
	/** Operations Section*/
	private void createOpersSection(Composite parent){
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR|ExpandableComposite.TWISTIE);
		section.setText("All Operations");
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		section.setExpanded(true);
		
		Composite composite = toolkit.createComposite(section);
		section.setClient(composite);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout());
		
		toolkit.paintBordersFor(composite);
		
		opertable = new Table(composite, SWT.BORDER|SWT.FULL_SELECTION);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 50;
		opertable.setLayoutData(gridData);
		opertable.setHeaderVisible(true);
		
		paraTable = new Table(composite, SWT.BORDER|SWT.FULL_SELECTION);
		gridData = new GridData(GridData.FILL_BOTH);
//		gridData.heightHint = 50;
		opertable.setLayoutData(gridData);
		opertable.setHeaderVisible(true);
		
		TableColumn col = new TableColumn(opertable, SWT.NULL);
		col.setText("Return Type");
		col.setWidth(150);		
		
		col = new TableColumn(opertable, SWT.NULL);
		col.setText("Name");
		col.setWidth(300);
		
		col = new TableColumn(opertable, SWT.NULL);
		col.setText("Parameters");
		col.setWidth(100);
		
		col = new TableColumn(opertable, SWT.NULL);
		col.setText("Description");
		col.setWidth(100);
		
		
		toolkit.paintBordersFor(composite);
	}
	
	/** 初始化MBean Attribute的数据*/
	private void fillAttrbiteData(){
		try {
			MBeanServerConnection conn = mbeanModel.getConnection();
			MBeanInfo beanInfo = conn.getMBeanInfo(mbeanModel.getObjectName());
			MBeanAttributeInfo[] attrs = beanInfo.getAttributes();
			for(MBeanAttributeInfo attr: attrs){
				TableItem tableItem= new TableItem(attrsTable, SWT.NONE);
				tableItem.setText(0, attr.getName());
				tableItem.setText(1, attr.getType());
				StringBuffer sb = new StringBuffer();
				if(attr.isReadable()){
					sb.append("R");
				}
				if(attr.isWritable()){
					sb.append("W");
				}
				tableItem.setText(2, sb.toString());
				String value = "";
				try {
					value = conn.getAttribute(mbeanModel.getObjectName(), attr.getName()).toString();
				} catch (Exception e) {
					value = e.getMessage();
					e.printStackTrace();
				}
				tableItem.setText(3, value);	//TODO:
				tableItem.setText(4, attr.getDescription());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initOpersData(){
		try {
			MBeanServerConnection conn = mbeanModel.getConnection();
			MBeanInfo beanInfo = conn.getMBeanInfo(mbeanModel.getObjectName());
			MBeanOperationInfo[] opers = beanInfo.getOperations();
			for(MBeanOperationInfo oper: opers){
				TableItem tableItem= new TableItem(opertable, SWT.NONE);
				tableItem.setText(0, oper.getName());
				tableItem.setText(1, oper.getReturnType());
//				tableItem.setText(2, "");	//TODO:
				tableItem.setText(3, oper.getDescription());
				MBeanParameterInfo[] params = oper.getSignature();
				StringBuffer sb = new StringBuffer();
				for(MBeanParameterInfo para: params){
					if(sb.length() >0){
						sb.append(",");
						sb.append(para.getType()).append(" ").append(para.getName());
					}else{
						sb.append("(").append(para.getType()).append(" ").append(para.getName());
					}
				}
				if(sb.length() >0){
					sb.append("");
				}
				tableItem.setText(2, sb.toString());	//TODO:
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
