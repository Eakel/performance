package com.easyfun.eclipse.performance.other.views;

import java.util.List;

import org.apache.velocity.util.StringUtils;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.easyfun.eclipse.common.console.LogHelper;
import com.easyfun.eclipse.common.kv.KeyValueTableViewer;
import com.easyfun.eclipse.performance.other.memcached.MemcachedStatUtil;

/**
 * 获取Memcache信息
 * @author linzhaoming
 *
 * 2013-12-16
 *
 */
public class MemcachedView extends ViewPart {
	//TODO: 表格数显示多个Memcache情况
	private Text addrText;
	private KeyValueTableViewer tableViewer;

	public MemcachedView() {
	}

	public void createPartControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NULL);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		comp.setLayout(new GridLayout(4, false));
		
		Label label = new Label(comp, SWT.NULL);
		label.setText("地 址：");
		label.setLayoutData(new GridData());
		
		addrText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		addrText.setText("test");
		addrText.setToolTipText("提示信息");
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
			}
		});
		
		label = new Label(comp, SWT.NULL);
		label.setText("");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		String[] titles = new String[] { "Key", "Value" };
		int[] widths = new int[] { 150, 450 };
		tableViewer = new KeyValueTableViewer(comp, titles, widths);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 4;
		tableViewer.getTable().setLayoutData(gridData);
	}

	public void setFocus() {
		addrText.setFocus();
	}
	
	private void invoke(String str){
//		SWTUtil.showMessage(addrText.getShell(), text);
		String[] tmp = StringUtils.split(str, ",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tmp.length; i++) {
			try {
				String[] tmp2 = StringUtils.split(tmp[i], ":");
				List map = MemcachedStatUtil.getStat(tmp2[0].trim(), Integer.parseInt(tmp2[1]));
				tableViewer.setInput(map);
			} catch (Exception ex) {
				ex.printStackTrace();
				LogHelper.error("获取信息出错", ex);
			}
		}
	}
}