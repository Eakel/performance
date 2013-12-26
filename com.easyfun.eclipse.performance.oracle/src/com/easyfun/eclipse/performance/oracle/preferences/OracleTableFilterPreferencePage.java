package com.easyfun.eclipse.performance.oracle.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * 表名过滤器
 * @author linzhaoming
 *
 * 2013-3-28
 *
 */
public class OracleTableFilterPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	
	public static final String ID = "com.easyfun.eclipse.performance.oracle.preferencePages.OracleTableFilter";
	private StyledText text;

	public OracleTableFilterPreferencePage() {
	}

	public OracleTableFilterPreferencePage(String title) {
		super(title);
	}

	public OracleTableFilterPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData());
		label.setText("说明：\n  schemes=so1;so2 配置需要扫描的用户名列表 以分号间隔\n" + "  so1.filter 配置需要扫描的表名,支持%配置\n" + "  so1.mode  配置需要扫描的线程数量");
		
		text = new StyledText(composite, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		text.setText(OraclePrefUtil.getTableFilter());
		text.addFocusListener(new FocusAdapter(){
			public void focusLost(FocusEvent e) {
				IPreferenceStore store = OraclePrefUtil.getPreferenceStore();
				store.setValue(OraclePrefConstants.ORACLE_TABLE_PREFIX_FILTER, text.getText());
			}
		});
		return text;
	}

	public void init(IWorkbench workbench) {
		
	}

	protected void performDefaults() {
		IPreferenceStore store = OraclePrefUtil.getPreferenceStore();
		String default1 = store.getDefaultString(OraclePrefConstants.ORACLE_TABLE_PREFIX_FILTER);
		text.setText(default1);
		super.performDefaults();
	}
	

}
