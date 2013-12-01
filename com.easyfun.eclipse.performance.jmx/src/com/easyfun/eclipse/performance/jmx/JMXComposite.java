package com.easyfun.eclipse.performance.jmx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.common.config.cfg.Item;
import com.easyfun.eclipse.common.view.item.pub.ItemComposite;
import com.easyfun.eclipse.performance.jmx.model.DomainModel;
import com.easyfun.eclipse.performance.jmx.model.MBeanModel;
import com.easyfun.eclipse.performance.jmx.util.JBOSSUtils;
import com.easyfun.eclipse.utils.lang.StringUtil;
import com.easyfun.eclipse.utils.model.tree.TreeViewerFactory;
import com.easyfun.eclipse.utils.ui.LayoutUtil;
import com.easyfun.eclipse.utils.ui.SWTUtil;

/**
 * JMX Composite
 * 
 * @author linzhaoming
 *
 * 2012-7-8
 *
 */
public class JMXComposite extends ItemComposite {
	
	private MBeanModel mbeanModel;
	
	/** MBean属性*/
	private Table attrsTable;
	/** MBean方法*/
	private Table opertable;
	/** MBean方法参数*/
	private Table paraTable;
	
	private TreeViewer treeViewer = null;
	private Label jmx_domainLabel;
	private Label jmxClassNamelabel;
	private Label jmx_displayNameModel;

	private Text urlText;

	public JMXComposite(Composite parent, int style, Item item) {	
		super(parent, style, item);
		this.setLayout(LayoutUtil.getNoMarginLayout());
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		init(this);
	}

	private void init(Composite parent){
		Composite top = new Composite(parent, SWT.NULL);
		top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		top.setLayout(LayoutUtil.getNoMarginLayout(3, false));
		
		final Button connButton = new Button(top, SWT.NULL);
		connButton.setLayoutData(new GridData());
		connButton.setText("Connect");
		connButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				processConnect();
			}
		});
		
		ComboViewer com = new ComboViewer(top);
		com.getCombo().setLayoutData(new GridData());
		com.setContentProvider(new ArrayContentProvider());
		List list = new ArrayList();
		list.add("JBOSS");
		com.setInput(list);
		com.getCombo().select(0);
		
		urlText = new Text(top, SWT.BORDER);
		urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		urlText.setText("localhost:1099");
		urlText.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13){	//Enter键盘
					processConnect();
				}
			}
		});
		
		
		SashForm sup = new SashForm(parent, SWT.HORIZONTAL|SWT.SMOOTH);
		sup.setLayout(LayoutUtil.getNoMarginLayout());
		sup.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite c1 = new Composite(sup, SWT.NULL);
		c1.setLayout(LayoutUtil.getNoMarginLayout(2, false));
		GridData gridData = new GridData();
		gridData.widthHint = 150;
		c1.setLayoutData(gridData);
		
		Composite c2 = new Composite(sup, SWT.NULL);
		c2.setLayout(LayoutUtil.getNoMarginLayout());
		c2.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		sup.setWeights(new int[]{20,80});
		
		createLeftComposite(c1);

		TabFolder tabFolder = new TabFolder(c2, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		TabItem obdCallTabItem = new TabItem(tabFolder, SWT.NONE);
		obdCallTabItem.setText("MBean基本属性");
		obdCallTabItem.setControl(createJMXBasicControl(tabFolder));
		
		TabItem threadItem = new TabItem(tabFolder, SWT.NONE);
		threadItem.setText("MBean方法");
		threadItem.setControl(createOpersControl(tabFolder));
	}

	/** 左侧MBean菜单列表*/
	public void createLeftComposite(Composite composite) {
		Composite filterComp = new Composite(composite, SWT.NULL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		filterComp.setLayoutData(gridData);
		filterComp.setLayout(LayoutUtil.getNoMarginLayout(2, false));
		
		final Button filterButton = new Button(filterComp, SWT.NULL);
		filterButton.setLayoutData(new GridData());
		filterButton.setText("Filter");
		filterButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				//处理过滤
				SWTUtil.showMessage(getShell(), "TODO:");
			}
		});
		
		Text filterText = new Text(filterComp, SWT.BORDER);
		filterText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		filterText.setText("");
		filterText.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13){	//Enter键盘
					processConnect();
				}
			}
		});
		
		treeViewer = TreeViewerFactory.createTreeViewer(composite);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		treeViewer.getTree().setLayoutData(gridData);
		
		treeViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				Object select = selection.getFirstElement();
				if(select instanceof MBeanModel){
					JMXComposite.this.mbeanModel = (MBeanModel)select;
					JMXComposite.this.fillData();
				}else {
					System.out.println(select.getClass());
					System.out.println(treeViewer.getExpandedState(select));
					if(select instanceof DomainModel){
						if(treeViewer.getExpandedState(select)){
							treeViewer.setExpandedState(select,false);
						}else{
							treeViewer.setExpandedState(select,true);
						}
					}
				}
			}
		});
	}
	
	/** MBean基本属性Tab*/
	private Composite createJMXBasicControl(Composite parent){
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(2, false));	

		Label label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("Domain Name");
		
		jmx_domainLabel = new Label(composite, SWT.NULL);
		jmx_domainLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jmx_domainLabel.setText("");
		SWTUtil.setBold(jmx_domainLabel);
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("Type");
		
		jmx_displayNameModel = new Label(composite, SWT.NULL);
		jmx_displayNameModel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jmx_displayNameModel.setText("");
		SWTUtil.setBold(jmx_displayNameModel);
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("MBean Class Name");
		
		jmxClassNamelabel = new Label(composite, SWT.NULL);
		jmxClassNamelabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jmxClassNamelabel.setText("");
		SWTUtil.setBold(jmxClassNamelabel);
		
		
		attrsTable = new Table(composite, SWT.BORDER|SWT.FULL_SELECTION);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
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
		
		return composite;
	}
	
	/** MBean方法 Tab*/
	private Composite createOpersControl(Composite parent){
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(1, false));	
		
		opertable = new Table(composite, SWT.BORDER|SWT.FULL_SELECTION);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 50;
		opertable.setLayoutData(gridData);
		opertable.setHeaderVisible(true);
		
		Label label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setText("参数列表");
		SWTUtil.setBold(label);
		
		paraTable = new Table(composite, SWT.BORDER|SWT.FULL_SELECTION);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 200;
		paraTable.setLayoutData(gridData);
		paraTable.setHeaderVisible(true);
		
		opertable.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				paraTable.removeAll();
				TableItem[] items = opertable.getSelection();
				if (items != null && items.length ==1){
					TableItem item = items[0];
					MBeanParameterInfo[] params = (MBeanParameterInfo[])item.getData();
					if(params != null && params.length >0){
						for (MBeanParameterInfo param : params) {
							TableItem paraItem= new TableItem(paraTable, SWT.NONE);
							paraItem.setText(0, param.getName());
							paraItem.setText(1, param.getType());
							paraItem.setText(2, param.getDescription());
						}
					}
				}
			}
		});
		
		
		//方法表格
		TableColumn operCol = new TableColumn(opertable, SWT.NULL);
		operCol.setText("Return Type");
		operCol.setWidth(150);		
		
		operCol = new TableColumn(opertable, SWT.NULL);
		operCol.setText("Name");
		operCol.setWidth(300);
		
		operCol = new TableColumn(opertable, SWT.NULL);
		operCol.setText("Parameters");
		operCol.setWidth(100);
		
		operCol = new TableColumn(opertable, SWT.NULL);
		operCol.setText("Description");
		operCol.setWidth(100);
		
		//参数表格
		TableColumn paraCol = new TableColumn(paraTable, SWT.NULL);
		paraCol.setText("Name");
		paraCol.setWidth(150);		
		
		paraCol = new TableColumn(paraTable, SWT.NULL);
		paraCol.setText("Type");
		paraCol.setWidth(300);

		paraCol = new TableColumn(paraTable, SWT.NULL);
		paraCol.setText("Description");
		paraCol.setWidth(200);

		return composite;
	}
	
	/* 数据相关的方法 **/
	
	private void fillData(){
		fillMBeanInfoData();
		fillMBeanOpersData();
	}
	
	/** MBean基本属性、Attribute*/
	private void fillMBeanInfoData(){
		jmx_domainLabel.setText(mbeanModel.getObjectName().getDomain());
		
		String dispName = mbeanModel.getDisplayName();
		int index = mbeanModel.getObjectName().getDomain().length();
		//:type=长度为6 
		jmx_displayNameModel.setText(dispName.substring(index + 6));
		
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

			jmxClassNamelabel.setText(className);

			MBeanInfo beanInfo = conn.getMBeanInfo(mbeanModel.getObjectName());
			MBeanAttributeInfo[] attrs = beanInfo.getAttributes();
			
			attrsTable.removeAll();
			
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
	
	/** MBean方法*/
	private void fillMBeanOpersData(){
		try {
			MBeanServerConnection conn = mbeanModel.getConnection();
			MBeanInfo beanInfo = conn.getMBeanInfo(mbeanModel.getObjectName());
			MBeanOperationInfo[] opers = beanInfo.getOperations();
			opertable.removeAll();
			
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
				tableItem.setData(params);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void processConnect() {
		String url = urlText.getText().trim();
		if(StringUtil.isEmpty(url)){
			SWTUtil.showError(getShell(), "URL地址不能为空");
			urlText.setFocus();
			return;
		}
		
		try {
			MBeanServerConnection beanServer = JBOSSUtils.getJBOSSMBeanServer(url);
			treeViewer.setInput(JBOSSUtils.getDomains(beanServer, null));
			treeViewer.setColumnProperties(new String[]{"a", "b"});
		} catch (Exception e) {
			e.printStackTrace();
			SWTUtil.showError(getShell(), "连接JMX出现错误：" + e.getMessage());
			treeViewer.setInput(null);
			urlText.setFocus();
		}
	}

}
