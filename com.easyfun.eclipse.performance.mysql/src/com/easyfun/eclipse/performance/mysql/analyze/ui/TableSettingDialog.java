package com.easyfun.eclipse.performance.mysql.analyze.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.easyfun.eclipse.performance.mysql.analyze.model.TableSettingBean;

/**
 * 过滤设定窗口
 * 
 * @author linzhaoming
 *
 * 2013-3-28
 *
 */
public class TableSettingDialog extends Dialog {

	private StyledText filterStyledText;
	
	private String prop;
	
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public TableSettingDialog(Shell parentShell, String prop) {
		super(parentShell);		
		if(prop == null){
			StringBuffer sb = new StringBuffer();
			sb.append("#用户名列表，多个用分号隔开").append("\n");
			sb.append("schemes=base;channel;sec_new;res;party;prodcfg;product;market;report;so1;so2;so3;so4;ams1;ams2;ams3;ams4").append("\n");
			sb.append("#过滤表名字").append("\n");
			sb.append("#so1.filter=%SMS%").append("\n");
			sb.append("#so2.filter=%SMS%").append("\n");
			sb.append("#base.filter=%SMS%").append("\n");
			sb.append("#生成文件名字").append("\n");
			sb.append("fileName=tableAnayyse_0505.xls").append("\n");
			sb.append("#是否打开日志").append("\n");
			sb.append("log=true");
			prop = sb.toString();
		}
		this.prop = prop;
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		container.setLayout(gridLayout);

		final Label schemasLabel = new Label(container, SWT.NONE);
		schemasLabel.setText("用户名列表");

		filterStyledText = new StyledText(container, SWT.BORDER);
		filterStyledText.setText(prop);
		filterStyledText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				prop = filterStyledText.getText().trim();
			}
		});
		filterStyledText.setToolTipText("过滤表名字");
		final GridData gd_filterStyledText = new GridData(SWT.FILL, SWT.FILL, true, true);
		filterStyledText.setLayoutData(gd_filterStyledText);
		
		return container;
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 375);
	}
	
	public String getProp(){
		return prop;
	}
	
	private void init(TableSettingBean bean){
		if(bean == null){
			bean = new TableSettingBean();
		}
		filterStyledText.setText(bean.getFilters());
	}

}
