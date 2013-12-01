package com.easyfun.eclipse.performance.trace.ui.trace;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.performance.trace.model.AppTrace;
import com.easyfun.eclipse.performance.trace.model.ITrace;
import com.easyfun.eclipse.performance.trace.model.WsTrace;
import com.easyfun.eclipse.performance.trace.ui.ITraceComposite;
import com.easyfun.eclipse.utils.lang.StringUtil;


/**
 * 展示WsTrace
 * @author linzhaoming
 *
 * 2013-4-1
 *
 */
public class WsTraceComposite extends Composite implements ITraceComposite{


	private Text createTimeText;
	private Text timeoutText;
	private Text urlText;
	private Text methodNameText;
	private Text userTimeText;
	private Text centerText;
	private Text codeText;
	private Text isSucccessText;
	private StyledText inParamText;
	private StyledText outParamText;
	
	private AppTrace appTrace = null;

	public WsTraceComposite(Composite parent, int style, AppTrace appTrace) {
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
		gridData.horizontalSpan = 1;
		urlText.setLayoutData(gridData);
		urlText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("方法名: ");
		
		methodNameText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		methodNameText.setLayoutData(gridData);
		methodNameText.setText("");
		
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("耗时(ms): ");
		
		userTimeText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		userTimeText.setLayoutData(gridData);
		userTimeText.setText("");
		
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
		label.setText("传入参数: ");
		
		inParamText = new StyledText(parent, SWT.BORDER|SWT.READ_ONLY|SWT.V_SCROLL|SWT.H_SCROLL|SWT.WRAP);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.horizontalSpan =3;
		gridData.heightHint = 100;
		inParamText.setLayoutData(gridData);
		inParamText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("返回参数: ");
		
		outParamText = new StyledText(parent, SWT.BORDER|SWT.READ_ONLY|SWT.V_SCROLL|SWT.H_SCROLL|SWT.WRAP);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan =3;
		outParamText.setLayoutData(gridData);
		outParamText.setText("");
	}
	
	public void update(ITrace trace){
		WsTrace wsTrace = (WsTrace)trace;
		
		createTimeText.setText(wsTrace.getCreateTime());
		timeoutText.setText(wsTrace.getTimeOut());
		urlText.setText(wsTrace.getUrl());
		methodNameText.setText(wsTrace.getMethodName());
		userTimeText.setText(wsTrace.getUseTime());
		centerText.setText(wsTrace.getCenter());
		codeText.setText(wsTrace.getCode());
		isSucccessText.setText(wsTrace.getSuccess());
		inParamText.setText(wsTrace.getInParam());
		outParamText.setText(wsTrace.getOutParam());
		
		Text[] text = new Text[]{createTimeText, timeoutText, urlText, methodNameText, userTimeText, centerText, codeText, isSucccessText};
		
		String search = appTrace.getUiDesc().getSearchText();
		if (StringUtil.isNotEmpty(search)) {
			for (Text t : text) {
				if (t.getText().contains(search)) {
					t.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
				}
			}
			
			if(inParamText.getText().contains(search)){
				inParamText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
			}
			
			if(outParamText.getText().contains(search)){
				outParamText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
			}
		}
	}
}
