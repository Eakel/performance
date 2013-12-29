package com.easyfun.eclipse.performance.trace.ui.trace;

import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.component.xml.XmlEditor;
import com.easyfun.eclipse.performance.trace.model.AppTrace;
import com.easyfun.eclipse.performance.trace.model.ITrace;
import com.easyfun.eclipse.performance.trace.model.SvrTrace;
import com.easyfun.eclipse.performance.trace.ui.ITraceComposite;
import com.easyfun.eclipse.util.lang.StringUtil;

/**
 * 展示SvrTrace
 * @author linzhaoming
 *
 * 2013-4-1
 *
 */
public class SvrTraceComposite extends Composite implements ITraceComposite{

	private Text createTimeText;
	private Text classNameText;
	private Text methodNameText;
	private Text costText;
	private Text centerText;
	private Text codeText;
	private Text isSuccessText;
	private Text appIPText;
	private Text appServerNameText;
	private SourceViewer inParamViewer;
	private AppTrace appTrace = null;

	public SvrTraceComposite(Composite parent, int style, AppTrace appTrace) {
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
		label.setText("app的IP: ");
		
		appIPText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		appIPText.setLayoutData(gridData);
		appIPText.setText("");
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("app服务器: ");
		
		appServerNameText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		appServerNameText.setLayoutData(gridData);
		appServerNameText.setText("");
		
		
		label = new Label(parent, SWT.NULL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("传入参数: ");
		
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 3;
		inParamViewer = XmlEditor.getSourceViewer(parent);
		inParamViewer.getDocument().set("");
		inParamViewer.getTextWidget().setLayoutData(gridData);
	}
	
	public void update(ITrace trace){
		SvrTrace srvTrace = (SvrTrace)trace;
		createTimeText.setText(srvTrace.getCreateTime());
		classNameText.setText(srvTrace.getClassName());
		methodNameText.setText(srvTrace.getMethodName());
		costText.setText(srvTrace.getUseTime());
		centerText.setText(srvTrace.getCenter());
		codeText.setText(srvTrace.getCode());
		isSuccessText.setText(srvTrace.getSuccess());
		appIPText.setText(srvTrace.getAppIp());
		appServerNameText.setText(srvTrace.getAppServerName());
		inParamViewer.getDocument().set(srvTrace.getInParam());
		
		Text[] text = new Text[]{classNameText, classNameText, methodNameText, costText, centerText, codeText, isSuccessText, appIPText, appServerNameText};
		
		String search = appTrace.getUiDesc().getSearchText();
		if (StringUtil.isNotEmpty(search)) {
			for (Text t : text) {
				if (t.getText().contains(search)) {
					t.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
				}
			}
			
			if(inParamViewer.getDocument().get().contains(search)){
				inParamViewer.getTextWidget().setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
			}
		}
		
	}
}
