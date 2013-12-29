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
import com.easyfun.eclipse.performance.trace.ui.ITraceComposite;
import com.easyfun.eclipse.util.lang.StringUtil;


/**
 * 展示AppTrace
 * @author linzhaoming
 *
 * 2013-4-1
 *
 */
public class AppTraceComposite extends Composite implements ITraceComposite{

	private Text createTimeText;
	private StyledText msgText;
	
	private AppTrace appTrace = null;

	public AppTraceComposite(Composite parent, int style, AppTrace appTrace) {
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
		gridData.horizontalSpan = 3;
		createTimeText.setLayoutData(gridData);
		createTimeText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("概要信息: ");
		
		msgText = new StyledText(parent, SWT.BORDER|SWT.READ_ONLY|SWT.V_SCROLL|SWT.H_SCROLL|SWT.WRAP);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 3;
		msgText.setLayoutData(gridData);
		msgText.setText("");
	}
	
	public void update(ITrace trace){
		AppTrace appTrace = (AppTrace)trace;
		createTimeText.setText(appTrace.getCreateTime());
		msgText.setText(appTrace.getMsg());
		
		Text[] text = new Text[]{createTimeText};
		
		String search = appTrace.getUiDesc().getSearchText();
		if (StringUtil.isNotEmpty(search)) {
			for (Text t : text) {
				if (t.getText().contains(search)) {
					t.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
				}
			}
		}
	}
}
