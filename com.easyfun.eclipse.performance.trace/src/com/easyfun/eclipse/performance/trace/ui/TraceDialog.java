package com.easyfun.eclipse.performance.trace.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.easyfun.eclipse.component.tree.TreeContentProvider;
import com.easyfun.eclipse.performance.trace.model.AppTrace;
import com.easyfun.eclipse.performance.trace.model.ITrace;
import com.easyfun.eclipse.uiutil.RCPUtil;
import com.easyfun.eclipse.util.StringUtil;


/**
 * 显示Trace的Dialog
 * @author linzhaoming
 *
 * 2013-4-6
 *
 */
public class TraceDialog extends TrayDialog {
	
	private Composite rightComp = null;
	
	private TreeViewer traceTreeViewer;
	
	private String fileName;
	
	private AppTrace appTrace;
	
	
	public TraceDialog(Shell parentShell, AppTrace appTrace) {
		super(parentShell);
		this.appTrace = appTrace;
	}
	
	public TraceDialog(Shell parentShell, AppTrace appTrace, String fileName) {
		super(parentShell);
		this.appTrace = appTrace;
		this.fileName = fileName;
	}

	protected Control createDialogArea(final Composite container) {
		final Composite parent = (Composite) super.createDialogArea(container);
		parent.setLayout(new GridLayout(2, false));
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite leftComp = new Composite(parent, SWT.NULL);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		leftComp.setLayout(gridLayout);
		GridData gridData = new GridData(SWT.LEFT, SWT.FILL, false, true);
		gridData.widthHint = 400;
		leftComp.setLayoutData(gridData);
	    
	    Button expandAllButton = new Button(leftComp, SWT.NULL);
	    expandAllButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
	    expandAllButton.setText("全部展开");
	    
	    Button collapseAllButton = new Button(leftComp, SWT.NULL);
	    collapseAllButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
	    collapseAllButton.setText("全部关闭");
	    
	    Button statButton = new Button(leftComp, SWT.NULL);
		statButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		statButton.setText("统计信息");

	    Button createSqlButton = new Button(leftComp, SWT.NULL);
		createSqlButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		createSqlButton.setText("文件内容");
		
	    Label label = new Label(leftComp, SWT.NULL);
	    label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
	    label.setText("");
	    
		traceTreeViewer = new TreeViewer(leftComp, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		traceTreeViewer.setLabelProvider(new TraceTreeLabelProvider());
		traceTreeViewer.setContentProvider(new TreeContentProvider());
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 5;
		traceTreeViewer.getTree().setLayoutData(gridData);
		
	    statButton.addSelectionListener(new SelectionAdapter(){
	    	public void widgetSelected(SelectionEvent e) {
	    		List<AppTrace> list = (List<AppTrace>)traceTreeViewer.getInput();
	    		if(list != null){
	    			AppTrace appTrace = list.get(0);
	    			RCPUtil.showMessage(getShell(), appTrace.getMsg());
	    		}
	    	}
	    });
	    
	    expandAllButton.addSelectionListener(new SelectionAdapter(){
	    	public void widgetSelected(SelectionEvent e) {
	    		traceTreeViewer.expandAll();
	    	}
	    });
	    
	    collapseAllButton.addSelectionListener(new SelectionAdapter(){
	    	public void widgetSelected(SelectionEvent e) {
	    		traceTreeViewer.collapseAll();
	    	}
	    });
	    
		createSqlButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				TraceXmlDialog dialog = new TraceXmlDialog(getShell(), appTrace);
				dialog.open();
			}
		});
		
		
		rightComp = TraceUIFactory.createCompositeByType(parent, null, null);
		rightComp.setLayout(new GridLayout(4, false));
		rightComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		traceTreeViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event) {
				TreeSelection selection = (TreeSelection)event.getSelection();
				if(!(selection.getFirstElement() instanceof ITrace)){
					return;
				}
				ITrace trace = (ITrace)selection.getFirstElement();
				if(trace.isNode() == false){
					return;
				}
				boolean oldState = traceTreeViewer.getExpandedState(trace);
				traceTreeViewer.setExpandedState(trace, !oldState);
			}
		});
		
		traceTreeViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event) {
				TreeSelection selection = (TreeSelection)event.getSelection();
				if(!(selection.getFirstElement() instanceof ITrace)){
					return;
				}
				ITrace trace = (ITrace)selection.getFirstElement();
				refreshTraceComposite(parent, trace);
			}
		});
		
		initialize();

		return parent;
	}
	
	
	public void refreshTraceComposite(Composite parent, ITrace trace){
		rightComp.dispose();
		rightComp = TraceUIFactory.createCompositeByType(parent, trace, appTrace);
		rightComp.setLayout(new GridLayout(4, false));
		rightComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		parent.layout();
		
		if(rightComp.isDisposed() == false && rightComp instanceof ITraceComposite){
			((ITraceComposite)rightComp).update(trace);
		}
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	protected Point getInitialSize() {
		return new Point(1024, 800);
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		if(StringUtil.isNotBlank(fileName)){
			newShell.setText("trace信息: " + fileName);
		}else{
			newShell.setText("trace信息");	
		}
	}
	
	private void initialize(){
		try {
			List list = new ArrayList();
			list.add(appTrace);
			traceTreeViewer.setInput(list);
		} catch (Exception e) {
			e.printStackTrace();
			RCPUtil.showError(getShell(), "解析Trace文件错误：" + e.getMessage());
		}
	}
	
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
	}

}
