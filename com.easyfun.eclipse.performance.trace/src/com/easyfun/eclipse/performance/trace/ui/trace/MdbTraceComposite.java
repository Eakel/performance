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
import com.easyfun.eclipse.performance.trace.model.MdbTrace;
import com.easyfun.eclipse.performance.trace.ui.ITraceComposite;
import com.easyfun.eclipse.util.StringUtil;


/**
 * 展示MdbTrace
 * @author linzhaoming
 *
 * 2013-4-1
 *
 */
public class MdbTraceComposite extends Composite implements ITraceComposite{

	private Text beginTimeText;
	private Text centerText;
	private Text codeText;
	private Text isSuccessText;
	private StyledText inParamText;
	private Text hostText;
	private StyledText outParamText;
	private Text useTimeText;
	
	private AppTrace appTrace = null;

	public MdbTraceComposite(Composite parent, int style, AppTrace appTrace) {
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
		
		beginTimeText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		beginTimeText.setLayoutData(gridData);
		beginTimeText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("HOST: ");
		
		hostText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		hostText.setLayoutData(gridData);
		hostText.setText("");
		
		
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
		
		isSuccessText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		isSuccessText.setLayoutData(gridData);
		isSuccessText.setText("");

		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("传入参数: ");
		
		inParamText = new StyledText(parent, SWT.BORDER|SWT.READ_ONLY|SWT.V_SCROLL|SWT.H_SCROLL|SWT.WRAP);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 3;
		inParamText.setLayoutData(gridData);
		inParamText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("返回参数: ");
		
		outParamText = new StyledText(parent, SWT.BORDER|SWT.READ_ONLY|SWT.V_SCROLL|SWT.H_SCROLL|SWT.WRAP);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 3;
		outParamText.setLayoutData(gridData);
		outParamText.setText("");
	}
	
	public void update(ITrace trace){
		MdbTrace mdbTrace = (MdbTrace)trace;
		beginTimeText.setText(mdbTrace.getCreateTime());
		useTimeText.setText(mdbTrace.getUseTime());
		centerText.setText(mdbTrace.getCenter());
		codeText.setText(mdbTrace.getCode());
		isSuccessText.setText(mdbTrace.getSuccess());
		inParamText.setText(mdbTrace.getInParam());
		outParamText.setText(mdbTrace.getOutParam());
		hostText.setText(mdbTrace.getHost());
		
		Text[] text = new Text[]{beginTimeText, useTimeText, centerText, centerText, codeText, isSuccessText, isSuccessText, hostText};
		
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
