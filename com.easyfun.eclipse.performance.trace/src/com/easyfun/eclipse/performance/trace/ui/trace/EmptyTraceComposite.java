package com.easyfun.eclipse.performance.trace.ui.trace;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.easyfun.eclipse.performance.trace.model.AppTrace;
import com.easyfun.eclipse.performance.trace.model.ITrace;
import com.easyfun.eclipse.performance.trace.ui.ITraceComposite;


/**
 * 展示MemTrace
 * @author linzhaoming
 *
 * 2013-4-1
 *
 */
public class EmptyTraceComposite extends Composite implements ITraceComposite{


	private Label label;
	
	private AppTrace appTrace = null;

	public EmptyTraceComposite(Composite parent, int style, AppTrace appTrace) {
		super(parent, style);
		init(this);
		
		this.appTrace = appTrace;
	}
	
	private void init(Composite parent){
		label = new Label(parent, SWT.NULL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.RIGHT;
		label.setLayoutData(gridData);
		label.setText("请选择左侧树显示详细内容");
	}
	
	public void update(ITrace trace){

	}
}
