package com.easyfun.eclipse.performance.trace.ui.trace;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.performance.trace.model.AppTrace;
import com.easyfun.eclipse.performance.trace.model.DaoTrace;
import com.easyfun.eclipse.performance.trace.model.ITrace;
import com.easyfun.eclipse.performance.trace.ui.ITraceComposite;
import com.easyfun.eclipse.util.lang.StringUtil;


/**
 * 展示DaoTrace
 * @author linzhaoming
 *
 * 2013-4-1
 *
 */
public class DaoTraceComposite extends Composite implements ITraceComposite{

	private Text createTimeText;
	private Text classNameText;
	private Text methodNameText;
	private Text costText;
	private Text centerText;
	private Text isSuccessText;
	
	private AppTrace appTrace = null;

	public DaoTraceComposite(Composite parent, int style, AppTrace appTrace) {
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
		label.setText("类名: ");
		
		classNameText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 3;
		classNameText.setLayoutData(gridData);
		classNameText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("方法名: ");
		
		methodNameText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 3;
		methodNameText.setLayoutData(gridData);
		methodNameText.setText("");
		
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("耗时(ms): ");
		
		costText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		costText.setLayoutData(gridData);
		costText.setText("");
		
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
		label.setText("是否成功: ");
		
		isSuccessText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		isSuccessText.setLayoutData(gridData);
		isSuccessText.setText("");
		
	}
	
	public void update(ITrace trace){
		DaoTrace daoTrace = (DaoTrace)trace;
		createTimeText.setText(daoTrace.getCreateTime());
		classNameText.setText(daoTrace.getClassName());
		methodNameText.setText(daoTrace.getMethodName());
		costText.setText(daoTrace.getUseTime());
		centerText.setText(daoTrace.getCenter());
		isSuccessText.setText(daoTrace.getSuccess());
		
		Text[] text = new Text[]{createTimeText, classNameText, methodNameText, costText, centerText, isSuccessText};
		
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
