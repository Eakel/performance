package com.easyfun.eclipse.performance.appframe.monitor.ui.realtime;
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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp.ActionComposite;
import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp.BasicComposite;
import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp.CacheComposite;
import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp.JVMComposite;
import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp.SQLComposite;
import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp.ServiceComposite;
import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp.SessionComposite;
import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp.SystemComposite;


/**
 * 实时监控
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-2
 */
public class RealTimeView extends Composite{

	private JVMComposite jvmComp;
	private BasicComposite basiceComp;
	private ServiceComposite serviceComp;
	private SQLComposite sqlComp;
	private CacheComposite cacheComp;
	private SystemComposite systemComp;
	private ActionComposite actionComp;
	private SessionComposite sessionComp;

	public RealTimeView(Composite parent){
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
		
		initData();
	}
	
	private void initData(){
	}
	
	private void initControl(Composite parent){
		//TODO: for test
		Button button = new Button(parent, SWT.PUSH);
		button.setText("获取");
		button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				//crm-web-g1-srv1=11001 crm-app-g1-c0b0-srv1=21001
				long serverId = 21001;
				try {
					//WEB
//					jvmComp.initData(serverId);
//					cacheComp.initData(serverId);
//					systemComp.initData(serverId);
//					actionComp.initData(serverId);
//					sessionComp.initData(serverId);
					
					//APP
					jvmComp.initData(serverId);
					basiceComp.initData(serverId);
					serviceComp.initData(serverId);
					sqlComp.initData(serverId);
					cacheComp.initData(serverId);
					systemComp.initData(serverId);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		TabFolder tabFolder = new TabFolder(parent, SWT.NULL);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridData gridData = new GridData(GridData.FILL_BOTH);
		
		jvmComp = new JVMComposite(tabFolder);
		jvmComp.setLayoutData(gridData);
		TabItem item = new TabItem(tabFolder ,SWT.NULL);
		item.setControl(jvmComp);
		item.setText("JVM基本");
		
		basiceComp = new BasicComposite(tabFolder);
		basiceComp.setLayoutData(gridData);
		item = new TabItem(tabFolder ,SWT.NULL);
		item.setControl(basiceComp);
		item.setText("AppFrame基本");
		
		
		serviceComp = new ServiceComposite(tabFolder);
		serviceComp.setLayoutData(gridData);
		item = new TabItem(tabFolder ,SWT.NULL);
		item.setControl(serviceComp);
		item.setText("AppFrame服务");
		
		sqlComp = new SQLComposite(tabFolder);
		sqlComp.setLayoutData(gridData);
		item = new TabItem(tabFolder ,SWT.NULL);
		item.setControl(sqlComp);
		item.setText("AppFrameSQL");
		
		cacheComp = new CacheComposite(tabFolder);
		cacheComp.setLayoutData(gridData);
		item = new TabItem(tabFolder ,SWT.NULL);
		item.setControl(cacheComp);
		item.setText("AppFrameCache");
		
		systemComp = new SystemComposite(tabFolder);
		systemComp.setLayoutData(gridData);
		item = new TabItem(tabFolder ,SWT.NULL);
		item.setControl(systemComp);
		item.setText("AppFrameSystem");
		
		actionComp = new ActionComposite(tabFolder);
		actionComp.setLayoutData(gridData);
		item = new TabItem(tabFolder ,SWT.NULL);
		item.setControl(actionComp);
		item.setText("AppFrameAction");
		
		sessionComp = new SessionComposite(tabFolder);
		sessionComp.setLayoutData(gridData);
		item = new TabItem(tabFolder ,SWT.NULL);
		item.setControl(sessionComp);
		item.setText("AppFrameSession");
		
	}
	
	public static void main(String[] args) throws Exception{
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		
		new RealTimeView(shell);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
