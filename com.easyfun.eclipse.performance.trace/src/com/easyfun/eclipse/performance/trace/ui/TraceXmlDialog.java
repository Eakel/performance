package com.easyfun.eclipse.performance.trace.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.easyfun.eclipse.component.xml.XmlEditor;
import com.easyfun.eclipse.performance.trace.TraceActivator;
import com.easyfun.eclipse.performance.trace.TraceImageConstants;
import com.easyfun.eclipse.performance.trace.model.AppTrace;
import com.easyfun.eclipse.rcp.RCPUtil;

public class TraceXmlDialog extends TrayDialog {
	
	private AppTrace rootTrace;
	private SourceViewer sourceViewer;

	protected TraceXmlDialog(Shell parentShell, AppTrace rootTrace) {
		super(parentShell);
		this.rootTrace = rootTrace;
	}

	protected Control createDialogArea(Composite container) {
		final Composite parent = (Composite) super.createDialogArea(container);
		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));

		sourceViewer = XmlEditor.getSourceViewer(parent);		
		GridData data= new GridData(SWT.FILL, SWT.FILL, true, true);
		sourceViewer.getTextWidget().setLayoutData(data);
		sourceViewer.getDocument().set(rootTrace.getTraceContent(true));
		
		Composite c = new Composite(parent, SWT.NULL);
		c.setLayout(new GridLayout(3,false));
		c.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		
		final Button b1 = new Button(c, SWT.RADIO);
		b1.setLayoutData(new GridData());
		b1.setText("隐藏CDATA");
		b1.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				if(b1.getSelection()){
					sourceViewer.getDocument().set(rootTrace.getTraceContent(true));
				}
			}
		});
		b1.setSelection(true);
		
		final Button b2 = new Button(c, SWT.RADIO);
		b2.setLayoutData(new GridData());
		b2.setText("显示CDATA");
		b2.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				if(b2.getSelection()){
					sourceViewer.getDocument().set(rootTrace.getTraceContent(false));
				}
			}
		});
		
		final Button b3 = new Button(c, SWT.PUSH);
		b3.setLayoutData(new GridData());
		b3.setText("另存为...");
		b3.setImage(TraceActivator.getImageDescriptor(TraceImageConstants.ICON_TRACE_SAVE_PATH).createImage());
		b3.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				String content = rootTrace.getTraceContent(false);
				File file = RCPUtil.openSaveDialog(b3.getShell(), new String[]{"*.trc", "*.*"}, rootTrace.getUiDesc().getFileName());
				if(file == null){
					return;
				}
				
				try {
					OutputStream out  = new FileOutputStream(file);
					IOUtils.write(content, out);
					out.close();
				} catch (Exception e1) {
					RCPUtil.showError(b3.getShell(), "保存Trace文件出现异常: " + e1.getMessage());
				}
			}
		});
		
		return parent;
	}
	
	protected Point getInitialSize() {
		return new Point(1024, 800);
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("trace内容");
	}
}
