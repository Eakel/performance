package com.easyfun.eclipse.common.view.item.welcome;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;

/**
 * @author linzhaoming
 *
 * 2011-5-8
 *
 */
public class WelcomeView extends ViewPart {
	
	public static final String VIEW_ID = "com.easyfun.eclipse.performance.view.WelcomeView";
	private Label welcomeLabel;

	public WelcomeView() {
	}

	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());

		welcomeLabel = new Label(parent, SWT.NONE);
		
		welcomeLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		welcomeLabel.setText("Welcome to EasyFun\n(Created by Linzhaoming)");
	}

	@Override
	public void setFocus() {
		if (welcomeLabel != null) {
			welcomeLabel.setFocus();
		}
	}

	private Shell getShell(){
		return getSite().getShell();
	}

}