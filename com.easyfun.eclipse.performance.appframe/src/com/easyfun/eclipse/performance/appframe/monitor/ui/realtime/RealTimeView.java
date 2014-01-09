package com.easyfun.eclipse.performance.appframe.monitor.ui.realtime;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp.ActionComposite;
import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp.BasicComposite;
import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp.CacheComposite;
import com.easyfun.eclipse.performance.appframe.monitor.ui.realtime.comp.InvokeModel;
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
	private Text jmxUrlText;

	public RealTimeView(Composite parent){
		super(parent, SWT.NULL);
		this.setLayout(new GridLayout(1, false));
		initControl(this);
	}
	
	private void initControl(Composite parent){
		//TODO: for test
		Composite comp = new Composite(parent, SWT.NULL);
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		comp.setLayout(new GridLayout(3, false));
		Button button = new Button(comp, SWT.PUSH);
		button.setText("获取");
		button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		
		Label label = new Label(comp, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("JMX地址：");
		
		jmxUrlText = new Text(comp, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.widthHint = 400;
		jmxUrlText.setLayoutData(gridData);
		jmxUrlText.setText("");
		
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				//crm-web-g1-srv1=11001 crm-app-g1-c0b0-srv1=21001
				long serverId = 21001;
				InvokeModel model = new InvokeModel();
				if(StringUtils.isNotEmpty(jmxUrlText.getText().trim())){
					model.setJmxUrl(jmxUrlText.getText().trim());
				}else{
					model.setServerId(serverId);
				}
				try {
					//WEB
//					jvmComp.initData(serverId);
//					cacheComp.initData(serverId);
//					systemComp.initData(serverId);
//					actionComp.initData(serverId);
//					sessionComp.initData(serverId);
					
					//APP
					jvmComp.initData(model);
					basiceComp.initData(model);
					serviceComp.initData(model);
					sqlComp.initData(model);
					cacheComp.initData(model);
					systemComp.initData(model);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		TabFolder tabFolder = new TabFolder(parent, SWT.NULL);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		gridData = new GridData(GridData.FILL_BOTH);
		
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
