package com.easyfun.eclipse.performance.trace.ui.trace;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.common.util.lang.StringUtil;
import com.easyfun.eclipse.performance.trace.model.AppTrace;
import com.easyfun.eclipse.performance.trace.model.HttpTrace;
import com.easyfun.eclipse.performance.trace.model.ITrace;
import com.easyfun.eclipse.performance.trace.ui.ITraceComposite;


/**
 * 展示HttpTrace
 * @author linzhaoming
 *
 * 2013-4-1
 *
 */
public class HttpTraceComposite extends Composite implements ITraceComposite{


	private Text createTimeText;
	private Text timeoutText;
	private Text urlText;
	private Text processMethodText;
	private Text statusCodeText;
	private Text useTimeText;
	private Text centerText;
	private Text codeText;
	private Text isSucccessText;
	private StyledText headerText;
	private StyledText paramText;
	private StyledText resultText;
	
	private AppTrace appTrace = null;

	public HttpTraceComposite(Composite parent, int style, AppTrace appTrace) {
		super(parent, style);
		init(this);
		
		this.appTrace = appTrace;
	}
	
	private void init(Composite parent){
		Label label = new Label(parent, SWT.NULL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("开始时间: ");
		
		createTimeText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		createTimeText.setLayoutData(gridData);
		createTimeText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("超时: ");
		
		timeoutText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		timeoutText.setLayoutData(gridData);
		timeoutText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("URL: ");
		
		urlText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 3;
		urlText.setLayoutData(gridData);
		urlText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("调用方式: ");
		
		processMethodText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		processMethodText.setLayoutData(gridData);
		processMethodText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("状态码: ");
		
		statusCodeText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		statusCodeText.setLayoutData(gridData);
		statusCodeText.setText("");
		
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("耗时(ms): ");
		
		useTimeText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		useTimeText.setLayoutData(gridData);
		useTimeText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("中心: ");
		
		centerText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		centerText.setLayoutData(gridData);
		centerText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("工号: ");
		
		codeText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		codeText.setLayoutData(gridData);
		codeText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("是否成功: ");
		
		isSucccessText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		isSucccessText.setLayoutData(gridData);
		isSucccessText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("header: ");
		
		headerText = new StyledText(parent, SWT.BORDER|SWT.READ_ONLY|SWT.V_SCROLL|SWT.H_SCROLL|SWT.WRAP);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.horizontalSpan =3;
		gridData.heightHint = 100;
		headerText.setLayoutData(gridData);
		headerText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("参数: ");
		
		paramText = new StyledText(parent, SWT.BORDER|SWT.READ_ONLY|SWT.V_SCROLL|SWT.H_SCROLL|SWT.WRAP);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.horizontalSpan =3;
		gridData.heightHint = 100;
		paramText.setLayoutData(gridData);
		paramText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("返回数据: ");
		
		resultText = new StyledText(parent, SWT.BORDER|SWT.READ_ONLY|SWT.V_SCROLL|SWT.H_SCROLL|SWT.WRAP);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan =3;
		resultText.setLayoutData(gridData);
		resultText.setText("");
	}
	
	public void update(ITrace trace){
		HttpTrace httpTrace = (HttpTrace)trace;
		
		createTimeText.setText(httpTrace.getCreateTime());
		timeoutText.setText(httpTrace.getTimeOut());
		urlText.setText(httpTrace.getUrl());
		processMethodText.setText(httpTrace.getProcessMethod());
		statusCodeText.setText(httpTrace.getStateCode());
		useTimeText.setText(httpTrace.getUseTime());
		centerText.setText(httpTrace.getCenter());
		codeText.setText(httpTrace.getCode());
		isSucccessText.setText(httpTrace.getSuccess());
		headerText.setText(httpTrace.getHeader());
		paramText.setText(httpTrace.getInParam());
		resultText.setText(httpTrace.getResult());
		
		Text[] text = new Text[]{createTimeText, timeoutText, urlText, processMethodText, statusCodeText, useTimeText, centerText, codeText, isSucccessText};
		
		String search = appTrace.getUiDesc().getSearchText();
		if (StringUtil.isNotEmpty(search)) {
			for (Text t : text) {
				if (t.getText().contains(search)) {
					t.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
				}
			}
			
			if(headerText.getText().contains(search)){
				headerText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
			}
			
			if(paramText.getText().contains(search)){
				paramText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
			}
			
			if(resultText.getText().contains(search)){
				resultText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
			}
		}
	}
}
