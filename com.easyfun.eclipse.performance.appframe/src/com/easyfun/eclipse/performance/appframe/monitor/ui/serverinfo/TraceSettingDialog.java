package com.easyfun.eclipse.performance.appframe.monitor.ui.serverinfo;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ai.appframe2.complex.mbean.standard.trace.AppTraceMonitorMBean;
import com.ai.appframe2.complex.mbean.standard.trace.WebTraceMonitorMBean;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MidServerControl;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonServer;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.rcp.RCPUtil;

public class TraceSettingDialog extends Dialog {
	
	private static Log log = LogFactory.getLog(TraceSettingDialog.class);
	
	private Combo enableCombo;
	private Text codeText;
	private Text urlText;
	private Text clientIPText;
	private Text durationText;
	private Text classNameText;
	private Text methodNameText;
	
	public static int WEB_TYPE = 0;
	
	public static int APP_TYPE = 1;
	
	private int type = WEB_TYPE;
	
	private MidServerControl[] controls;

	public TraceSettingDialog(Shell shell, MidServerControl[] controls, int type) {
		super(shell);
		this.controls = controls;
		this.type = type;
	}

	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);
		
		Label label = new Label(comp, SWT.NULL);
		label.setLayoutData(new GridData());
		if(this.type == APP_TYPE){
			label.setText("设置APP的Trace");
		}else if(this.type == WEB_TYPE){
			label.setText("设置WEB的Trace");
		}

		Composite c1 = new Composite(comp, SWT.NULL);
		c1.setLayout(new GridLayout(2, false));
		c1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("是否打开全局trace：");

		enableCombo = new Combo(c1, SWT.READ_ONLY);
		enableCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		enableCombo.add("关闭");
		enableCombo.add("打开");
		enableCombo.select(0);

		label = new Label(c1, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("工号：");

		codeText = new Text(c1, SWT.BORDER);
		codeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		if(type == WEB_TYPE){
			label = new Label(c1, SWT.NULL);
			label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			label.setText("url：");

			urlText = new Text(c1, SWT.BORDER);
			urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			label = new Label(c1, SWT.NULL);
			label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			label.setText("客户端IP：");

			clientIPText = new Text(c1, SWT.BORDER);
			clientIPText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			label = new Label(c1, SWT.NULL);
			label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			label.setText("持续时间(秒)：");

			durationText = new Text(c1, SWT.BORDER);
			durationText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		} else if(type == APP_TYPE){
			label = new Label(c1, SWT.NULL);
			label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			label.setText("类名(全称)：");

			classNameText = new Text(c1, SWT.BORDER);
			classNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			label = new Label(c1, SWT.NULL);
			label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			label.setText("方法名：");

			methodNameText = new Text(c1, SWT.BORDER);
			methodNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));	
		}


		Composite c2 = new Composite(comp, SWT.NULL);
		c2.setLayout(new GridLayout(1, false));
		c2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button button = new Button(c2, SWT.PUSH);
		button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		button.setText("设置TRACE");
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
			}
		});

		return comp;
	}

	protected void okPressed() {
		for (MidServerControl control : controls) {
			List list = new ArrayList();
			IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
			if(type == APP_TYPE){
				try {
					MonServer objMonServer = objIMonSV.getMonServerByServerName(control.getServerName());
					if (objMonServer != null) {
						AppTraceMonitorMBean objAppTraceMonitor = null;
						try {
							objAppTraceMonitor = (AppTraceMonitorMBean) ClientProxy.getObject(objMonServer.getServerId(), AppTraceMonitorMBean.class);
							String enable = enableCombo.getItem(enableCombo.getSelectionIndex());
							if (enable.equalsIgnoreCase("1")) {
								objAppTraceMonitor.enable(codeText.getText().trim(), classNameText.getText().trim(), methodNameText.getText().trim());
							} else if (enable.equalsIgnoreCase("0")) {
								objAppTraceMonitor.disable();
							} else {
								throw new Exception("无法识别的类型:" + enable);
							}
	
							list.add(control.getServerName());
						} catch (Exception ex) {
							log.error("异常", ex);
						} finally {
							if (objAppTraceMonitor != null) {
								ClientProxy.destroyObject(objAppTraceMonitor);
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				RCPUtil.showMessage(getShell(), StringUtils.join(list.iterator(), "\n"));
				
				this.close();
			}else if(type == WEB_TYPE){
				try {
					MonServer objMonServer = objIMonSV.getMonServerByServerName(control.getServerName());
					if (objMonServer != null) {
						WebTraceMonitorMBean objWebTraceMonitorMBean = null;
						try {
							objWebTraceMonitorMBean = (WebTraceMonitorMBean) ClientProxy.getObject(objMonServer.getServerId(), WebTraceMonitorMBean.class);
							String enable = enableCombo.getItem(enableCombo.getSelectionIndex());
							if (enable.equalsIgnoreCase("打开")) {
								objWebTraceMonitorMBean.enable(codeText.getText().trim(), urlText.getText().trim(), 
										clientIPText.getText().trim(), Integer.parseInt(durationText.getText().trim()));
							} else if (enable.equalsIgnoreCase("关闭")) {
								objWebTraceMonitorMBean.disable();
							} else {
								throw new Exception("无法识别的类型:" + enable);
							}

							list.add(control.getServerName());
						} catch (Exception ex) {
							log.error("异常", ex);
						} finally {
							if (objWebTraceMonitorMBean != null)
								ClientProxy.destroyObject(objWebTraceMonitorMBean);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				RCPUtil.showMessage(getShell(), StringUtils.join(list.iterator(), "\n"));
				this.close();
			}
		}
	}
}
