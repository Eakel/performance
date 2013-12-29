package com.easyfun.eclipse.performance.trace.ui.trace;

import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.common.ui.xml.XmlEditor;
import com.easyfun.eclipse.common.util.lang.StringUtil;
import com.easyfun.eclipse.performance.trace.model.AppTrace;
import com.easyfun.eclipse.performance.trace.model.ITrace;
import com.easyfun.eclipse.performance.trace.model.SecMemTrace;
import com.easyfun.eclipse.performance.trace.ui.ITraceComposite;

/**
 * 展示SecMemTrace
 * @author linzhaoming
 *
 * 2013-4-1
 *
 */
public class SecMemTraceComposite extends Composite implements ITraceComposite{

	private Text beginTimeText;
	private Text costText;
	private Text centerText;
	private Text codeText;
	private Text isSuccessText;
	private Text connTimeText;
	private Text resultText;
	private Text processMethodText;
	private Text hostText;
	private SourceViewer inParamViewer;
	
	private AppTrace appTrace = null;

	public SecMemTraceComposite(Composite parent, int style, AppTrace appTrace) {
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
		label.setText("获得连接时间: ");
		
		connTimeText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		connTimeText.setLayoutData(gridData);
		connTimeText.setText("");
		
		
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
		label.setText("结果条数: ");
		
		resultText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 1;
		resultText.setLayoutData(gridData);
		resultText.setText("");
		
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
		label.setText("调用方式: ");
		
		processMethodText = new Text(parent, SWT.BORDER|SWT.READ_ONLY);
		gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
		gridData.horizontalSpan = 3;
		processMethodText.setLayoutData(gridData);
		processMethodText.setText("");

		
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
		SecMemTrace secMemTrace = (SecMemTrace)trace;
		beginTimeText.setText(secMemTrace.getCreateTime());
		costText.setText(secMemTrace.getUseTime());
		centerText.setText(secMemTrace.getCenter());
		codeText.setText(secMemTrace.getCode());
		isSuccessText.setText(secMemTrace.getSuccess());
		processMethodText.setText(secMemTrace.getProcessMethod());
		connTimeText.setText(secMemTrace.getGetTime() + "");
		resultText.setText(secMemTrace.getResultCount());
		hostText.setText(secMemTrace.getHost());
		inParamViewer.getDocument().set(secMemTrace.getInParam());

		Text[] text = new Text[]{beginTimeText, costText, centerText, codeText, centerText, isSuccessText, isSuccessText, processMethodText, connTimeText, resultText,hostText };
		
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
