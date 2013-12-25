package com.easyfun.eclipse.performance.mysql.analyze.pref;

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
 * MySQL表名过滤器
 * @author linzhaoming
 *
 * 2013-12-23
 *
 */
public class MySQLTableFilterPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	
	public static final String PREF_ID = "com.easyfun.eclipse.performance.mysql.preferences.MySQLTableFilter";
	private StyledText text;

	public MySQLTableFilterPreferencePage() {
	}

	public MySQLTableFilterPreferencePage(String title) {
		super(title);
	}

	public MySQLTableFilterPreferencePage(String title, ImageDescriptor image) {
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
		text.setText(MySQLPrefUtil.getTableFilter());
		text.addFocusListener(new FocusAdapter(){
			public void focusLost(FocusEvent e) {
				IPreferenceStore store = MySQLPrefUtil.getPreferenceStore();
				store.setValue(MySQLPrefConstants.MYSQL_TABLE_PREFIX_FILTER, text.getText());
			}
		});
		return text;
	}

	public void init(IWorkbench workbench) {
		
	}

	protected void performDefaults() {
		IPreferenceStore store = MySQLPrefUtil.getPreferenceStore();
		String default1 = store.getDefaultString(MySQLPrefConstants.MYSQL_TABLE_PREFIX_FILTER);
		text.setText(default1);
		super.performDefaults();
	}
	

}
