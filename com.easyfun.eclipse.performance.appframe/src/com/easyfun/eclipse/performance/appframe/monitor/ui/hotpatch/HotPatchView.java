package com.easyfun.eclipse.performance.appframe.monitor.ui.hotpatch;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ParallelUtil;
import com.easyfun.eclipse.performance.appframe.monitor.ui.common.ServerComposite;
import com.easyfun.eclipse.rcp.ColumnViewerSorter;
import com.easyfun.eclipse.rcp.RCPUtil;


/**
 * 
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-2
 */
public class HotPatchView extends Composite{
	
	private static Log log = LogFactory.getLog(HotPatchView.class);
	
	private TableViewer tableViewer;

	private ServerComposite serverComposite;

	public HotPatchView(Composite parent){
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
		
		initData();
	}
	
	private void initData(){
		try {
			serverComposite.initData("CRM-WEB");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initControl(Composite parent){
		Composite top = new Composite(parent, SWT.NULL);
		top.setLayout(new GridLayout(5, true));
		
		createTopComposite(top);
		
		Composite content = new Composite(parent, SWT.NULL);
		content.setLayout(new GridLayout(1, false));
		content.setLayoutData(new GridData(GridData.FILL_BOTH));
		createContent(content);		
	}
	
	private void createTopComposite(Composite parent){
		serverComposite = new ServerComposite(parent);
	}
	
	private void createContent(Composite parent){
		Composite c1 = new Composite(parent, SWT.NULL);
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		c1.setLayout(new GridLayout(2, false));
		
		Button button = new Button(c1, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("拷贝到剪切板");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sb = new StringBuffer();
				List<HotPatchModel> list = (List<HotPatchModel>)tableViewer.getInput();
				sb.append("服务器").append("\t").append("HotPatch列表").append("\n");
				for (HotPatchModel model : list) {
					sb.append(model.getServerName()).append("\t").append(model.getHotpatchList()).append("\n");
				}
				
				String content = sb.toString();
				
				RCPUtil.copyToClipboard(getShell(), content);
			}
		});
		
		button = new Button(c1, SWT.PUSH);
		button.setLayoutData(new GridData());
		button.setText("查  询");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				long[] tmpIds = serverComposite.getSelectId();
				
				try {
					List rtn = ParallelUtil.getHotPatchMap(20, 8, tmpIds);
					List<HotPatchModel> list = new ArrayList<HotPatchModel>();
					for (Iterator iter = rtn.iterator(); iter.hasNext();) {
						Map map = (Map) iter.next();
						HotPatchModel model = new HotPatchModel();
						try {
							model.setServerName((String) map.get("SERVER_NAME"));
							String[] tmp2 = (String[]) map.get("HOTPATCH_LIST");
							model.setHotpatchList(StringUtils.join(tmp2, ";\r\n"));
						} catch (Throwable ex) {
							log.error("异常", ex);
						}
						list.add(model);
					}
					
					tableViewer.setInput(list);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NULL);
		column.getColumn().setText("服务器");
		column.getColumn().setWidth(300);
		column.getColumn().setMoveable(true);
		column.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof HotPatchModel) {
					HotPatchModel model = (HotPatchModel) element;
					return model.getServerName();
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof HotPatchModel && e2 instanceof HotPatchModel){
					HotPatchModel c1 = (HotPatchModel) e1;
					HotPatchModel c2 = (HotPatchModel) e2;
					return c1.getServerName().compareTo(c2.getServerName());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
		
		TableViewerColumn value = new TableViewerColumn(tableViewer, SWT.NULL);
		value.getColumn().setText("HotPatch列表");
		value.getColumn().setWidth(300);
		value.getColumn().setMoveable(true);
		value.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof HotPatchModel) {
					HotPatchModel model = (HotPatchModel) element;
					return model.getHotpatchList();
				} else {
					return element.toString();
				}
			}
		});
		
		new ColumnViewerSorter(tableViewer, column) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				if(e1 instanceof HotPatchModel && e2 instanceof HotPatchModel){
					HotPatchModel c1 = (HotPatchModel) e1;
					HotPatchModel c2 = (HotPatchModel) e2;
					return c1.getHotpatchList().compareTo(c2.getHotpatchList());
				}else{
					return e1.hashCode() - e2.hashCode();
				}
			}
		};
	}
	
	public static void main(String[] args) throws Exception{
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		
		new HotPatchView(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
