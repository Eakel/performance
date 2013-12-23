package com.easyfun.eclipse.performance.other.memcached;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.velocity.util.StringUtils;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.easyfun.eclipse.common.console.LogHelper;
import com.easyfun.eclipse.performance.other.memcached.model.MemModel;
import com.easyfun.eclipse.performance.other.memcached.model.MemTableViewer;
import com.easyfun.eclipse.performance.other.memcached.prefs.OtherPrefUtil;

/**
 * 获取Memcache信息
 * @author linzhaoming
 *
 * 2013-12-16
 *
 */
public class MemcachedView extends ViewPart {
	private Text addrText;
	private MemTableViewer tableViewer;
	private ListViewer listViewer;
	private Map<String, List<MemModel>> valuesMap;

	public MemcachedView() {
	}

	public void createPartControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NULL);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		comp.setLayout(new GridLayout(4, false));
		
		Label label = new Label(comp, SWT.NULL);
		label.setText("地 址：");
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		
		addrText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		OtherPrefUtil.getPreferenceStore().getBoolean("");
//		addrText.setText("localhost:11211");
		addrText.setText(OtherPrefUtil.getMemcachedUrl());
		addrText.setToolTipText("输入格式为：ip1:port1,ip2:port2");
		GridData gridData1 = new GridData(SWT.LEFT, SWT.TOP, false, false);
		gridData1.widthHint = 500;
		addrText.setLayoutData(gridData1);
		addrText.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				if(e.keyCode == 13){	//Enter键盘
					invoke(addrText.getText().trim());
				}
			}
		});
		
		Button button = new Button(comp, SWT.PUSH);
		button.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		button.setText("Invoke");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				invoke(addrText.getText().trim());
				OtherPrefUtil.setMemcachedUrl(addrText.getText().trim());
			}
		});
		
		label = new Label(comp, SWT.NULL);
		label.setText("");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		listViewer = new ListViewer(comp, SWT.BORDER);
		gridData1 = new GridData(GridData.FILL_VERTICAL);
		gridData1.widthHint=200;
		listViewer.getList().setLayoutData(gridData1);
		listViewer.setContentProvider(new ArrayContentProvider());
		listViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection sel = (IStructuredSelection)listViewer.getSelection();
				String select = (String)sel.getFirstElement();
				List<MemModel> list = valuesMap.get(select);
				tableViewer.setInput(list);
			}
		});
		
		Menu menu = new Menu(listViewer.getList());
		listViewer.getList().setMenu(menu);
		
		MenuItem item1 = new MenuItem(menu, SWT.PUSH);
		item1.setText("stats");
		item1.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				try {
					IStructuredSelection sel = (IStructuredSelection)listViewer.getSelection();
					String select = (String)sel.getFirstElement();
					String[] tmp = StringUtils.split(select, ":");
					String host = tmp[0];
					int port = Integer.parseInt(tmp[1]);
					List<MemModel> items = MemcachedStatUtil.getStat(host, port);
					tableViewer.setInput(items);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		MenuItem item2 = new MenuItem(menu, SWT.PUSH);
		item2.setText("stats items");
		item2.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				try {
					IStructuredSelection sel = (IStructuredSelection)listViewer.getSelection();
					String select = (String)sel.getFirstElement();
					String[] tmp = StringUtils.split(select, ":");
					String host = tmp[0];
					int port = Integer.parseInt(tmp[1]);
					List<MemModel> items = MemcachedStatUtil.getStatItems(host, port);
					tableViewer.setInput(items);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		MenuItem item3 = new MenuItem(menu, SWT.PUSH);
		item3.setText("stats slabs");
		item3.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				try {
					IStructuredSelection sel = (IStructuredSelection)listViewer.getSelection();
					String select = (String)sel.getFirstElement();
					String[] tmp = StringUtils.split(select, ":");
					String host = tmp[0];
					int port = Integer.parseInt(tmp[1]);
					List<MemModel> items = MemcachedStatUtil.getStatSlabs(host, port);
					tableViewer.setInput(items);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		MenuItem item4 = new MenuItem(menu, SWT.PUSH);
		item4.setText("stats sizes");
		item4.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				try {
					IStructuredSelection sel = (IStructuredSelection)listViewer.getSelection();
					String select = (String)sel.getFirstElement();
					String[] tmp = StringUtils.split(select, ":");
					String host = tmp[0];
					int port = Integer.parseInt(tmp[1]);
					List<MemModel> items = MemcachedStatUtil.getStatSizes(host, port);
					tableViewer.setInput(items);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		tableViewer = new MemTableViewer(comp);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 3;
		tableViewer.getTable().setLayoutData(gridData);
	}

	public void setFocus() {
		addrText.setFocus();
	}
	
	private void invoke(String str){
		String[] tmp = StringUtils.split(str, ",");
		Set<String> addr = new TreeSet<String>();
		valuesMap = new HashMap<String, List<MemModel>>();
		Object first = null;
		for (int i = 0; i < tmp.length; i++) {
			try {
				String[] tmp2 = StringUtils.split(tmp[i], ":");
				List list = MemcachedStatUtil.getStat(tmp2[0].trim(), Integer.parseInt(tmp2[1]));				
				addr.add(tmp[i]);
				valuesMap.put(tmp[i], list);
				if(i==0){
					first = tmp[i];
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				LogHelper.error("获取 [" + tmp[i] + "] 的Memcached信息出错", ex);
			}
		}
		listViewer.setInput(valuesMap.keySet());
		if(first != null){
			listViewer.setSelection(new StructuredSelection(first));
		}
	}
}