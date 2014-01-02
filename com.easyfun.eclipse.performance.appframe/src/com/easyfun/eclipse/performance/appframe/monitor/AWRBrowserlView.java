package com.easyfun.eclipse.performance.appframe.monitor;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.easyfun.eclipse.component.browser.SWTBrower;

public class AWRBrowserlView extends ViewPart {

	public AWRBrowserlView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		final SWTBrower swtBrowser = new SWTBrower(parent, ":blank", false);
		final Browser currentBrowser = swtBrowser.getCurrentBrowser();
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
						swtBrowser.setStatusText(localtion);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
