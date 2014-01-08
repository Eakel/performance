package com.easyfun.eclipse.performance.appframe.monitor.ui.common;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonServer;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;

/**
 * 代表Server名称列表
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-3
 */
public class ServerComposite extends Composite{
	Composite serverComposite = null;
	private List<Button> serverList = new ArrayList<Button>();
	
	private static boolean IS_MOCK = false;
	private Text selectNameText;
	
	public ServerComposite(Composite parent){
		super(parent, SWT.NULL);
		this.setLayoutData(new GridData());
		this.setLayout(new GridLayout(1, false));
	}
	
	public void initData(String serverType) {
		serverComposite = new Composite(this, SWT.NULL);
		serverComposite.setLayout(new GridLayout(4, false));
		serverComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite c1 = new Composite(this, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c1.setLayout(new GridLayout(5, false));
		
		Label label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("");
		
		Button button = new Button(c1, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("全选");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				selectAll();
			}
		});
		
		button = new Button(c1, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("全不选");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
					unSelectAll();
			}
		});
		
		label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("");
		
		label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("");
		
		//line 2
		Composite c2 = new Composite(this, SWT.NULL);
		c2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c2.setLayout(new GridLayout(5, false));
		
		label = new Label(c2, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("模糊名称：");
		
		selectNameText = new Text(c2, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.widthHint = 100;
		selectNameText.setLayoutData(gridData);
		selectNameText.setText("");
		
		button = new Button(c2, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("模糊选择");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				for (Button button : serverList) {
					if(button.getText().trim().contains(selectNameText.getText().trim())){
						button.setSelection(true);
					}else{
						button.setSelection(false);
					}
				}
			}
		});
		
		if(IS_MOCK){
			MonServer server = new MonServer();
			server.setName("crm-web-g1-srv1");
			Button button1 = new Button(serverComposite, SWT.CHECK);
			button1.setText("crm-web-g1-srv1");
			button1.setData(server);
			button1.setLayoutData(new GridData());
			button1.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					super.widgetSelected(e);
				}
			});
			serverList.add(button1);
			
			button1 = new Button(serverComposite, SWT.CHECK);
			button1.setText("crm-web-g1-srv9");
			button1.setData(server);
			button1.setLayoutData(new GridData());
			button1.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					super.widgetSelected(e);
				}
			});
			serverList.add(button1);
			return;
		}
		try {
			IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
			MonServer[] objMonServer = objIMonSV.getMonMbeanServerByServerType(serverType.trim());
			objMonServer[0].getName();
			objMonServer[0].getServerId();
			serverList.clear();
			for (MonServer server : objMonServer) {
				Button button1 = new Button(serverComposite, SWT.CHECK);
				button1.setText(server.getName());
				button1.setData(server);
				button1.setLayoutData(new GridData());
				button1.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						super.widgetSelected(e);
					}
				});
				serverList.add(button1);
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}
	
	public long[] getSelectId(){
		List<Long> list = new ArrayList<Long>();
		for (Button button : serverList) {
			if (button.getSelection()) {
				MonServer server = (MonServer) button.getData();
				list.add(server.getServerId());
			}
		}
		long[] result = new long[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}
	
	public void selectAll(){
		for (Button button : serverList) {
			button.setSelection(true);
		}
	}
	
	public void unSelectAll(){
		for (Button button : serverList) {
			button.setSelection(false);
		}
	}
}
