package com.easyfun.eclipse.performance.navigator.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.easyfun.eclipse.performance.navigator.cfg.model.Item;
import com.easyfun.eclipse.performance.navigator.helper.ItemComposite;

/**
 * 简介作用的Composite
 * @author linzhaoming
 * 
 * @deprecated 已经不再使用
 *
 * 2011-5-8
 *
 */
public class WelcomeComposite extends ItemComposite {

	/**
	 * Create the composite
	 * @param parent
	 * @param style
	 */
	public WelcomeComposite(Composite parent, int style, Item item) {
		super(parent, style, item);
		setLayout(new GridLayout());

		final Label label = new Label(this, SWT.NONE);
		
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		label.setText("Welcome to EasyFun\n(Created by Linzhaoming)");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
