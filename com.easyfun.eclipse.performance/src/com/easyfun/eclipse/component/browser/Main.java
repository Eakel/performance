package com.easyfun.eclipse.component.browser;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.easyfun.eclipse.rcp.RCPUtil;

public class Main {
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			shell.setSize(649, 448);
			shell.setText("浏览器");
			GridLayout gridLayout = RCPUtil.getNoMarginLayout();
			gridLayout.horizontalSpacing = 0;
			gridLayout.verticalSpacing = 0;
			shell.setLayout(gridLayout);
			
			final SWTBrower window = new SWTBrower(shell, "http://10.3.3.213:28888/xzngcrm@crm-237/", true);
			final Browser currentBrowser = window.getCurrentBrowser();
			currentBrowser.addLocationListener(new LocationAdapter(){
				public void changing(LocationEvent e) {
					//额外处理AWR类型的文件
					try {
						String localtion = e.location;
						if (localtion.endsWith(".html.gz")) {
							GetMethod getMethod = new GetMethod(localtion);
							int timeout = 2000;
							HttpClient client = new HttpClient();
							client.setTimeout(timeout);
							client.executeMethod(getMethod);
							byte[] bytes = getMethod.getResponseBody();
							GZIPInputStream stream = new GZIPInputStream(new ByteArrayInputStream(bytes));
							List<String> readLines = IOUtils.readLines(stream);
							StringBuffer sb = new StringBuffer();
							for (String str : readLines) {
								sb.append(str);
							}
							currentBrowser.setText(sb.toString());
//							statusLabel.setText("读取AWR文件完成");
							window.setStatusText(localtion);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
