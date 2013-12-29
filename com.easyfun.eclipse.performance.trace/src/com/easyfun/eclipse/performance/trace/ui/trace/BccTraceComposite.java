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
import com.easyfun.eclipse.performance.trace.model.BccMemTrace;
import com.easyfun.eclipse.performance.trace.model.ITrace;
import com.easyfun.eclipse.performance.trace.ui.ITraceComposite;
import com.easyfun.eclipse.util.lang.StringUtil;

/**
 * 展示MemTrace
 * @author linzhaoming
 *
 * 2013-4-1
 *
 */
public class BccTraceComposite extends Composite implements ITraceComposite{

	private Text createTimeText;
	private Text costText;
	private Text centerText;
	private Text codeText;
	private Text isSuccessText;
	private SourceViewer inParamViewer;
	private Text connTimeText;
	private Text resultText;
	private Text hostText;
	
	private AppTrace appTrace;

	public BccTraceComposite(Composite parent, int style, AppTrace appTrace) {
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
		label.setText("传入参数: ");
		
		inParamViewer = XmlEditor.getSourceViewer(parent);
		inParamViewer.getDocument().set("");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 3;
		inParamViewer.getTextWidget().setLayoutData(gridData);
	}
	
	public void update(ITrace trace){
		BccMemTrace bccTrace = (BccMemTrace)trace;
		createTimeText.setText(bccTrace.getCreateTime());
		costText.setText(bccTrace.getUseTime());
		centerText.setText(bccTrace.getCenter());
		codeText.setText(bccTrace.getCode());
		isSuccessText.setText(bccTrace.getSuccess());
		inParamViewer.getDocument().set(bccTrace.getInParam());
		connTimeText.setText(bccTrace.getGetTime());
		resultText.setText(bccTrace.getResultCount());
		hostText.setText(bccTrace.getHost());
		
		Text[] text = new Text[]{createTimeText, costText, centerText, codeText, isSuccessText, isSuccessText, connTimeText, resultText, hostText};
		
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
