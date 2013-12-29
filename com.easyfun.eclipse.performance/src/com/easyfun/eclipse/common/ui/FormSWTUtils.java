/**
 * 
 */
package com.easyfun.eclipse.common.ui;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author linzhaoming
 *
 * Jan 3, 2008
 */
public class FormSWTUtils {
	
	/**
	 * 
	 * @return the created Text control
	 */
	public static Text createNameValueTextComposite(FormToolkit toolkit, Composite parent, String label, String value, int style) {
		Composite composite = toolkit.createComposite(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(composite);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 3;
		composite.setLayout(layout);
		Label l = toolkit.createLabel(composite, label, SWT.NONE);
		l.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		l.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		Text text;
		if ((SWT.READ_ONLY & style) == SWT.READ_ONLY) {
			text = new Text(composite, style);
			toolkit.adapt(text, true, true);
			text.setText(value);
		} else {
			text = toolkit.createText(composite, value, style);
		}
		return text;
	}
	
	public static void createNameValueLabelComposite(FormToolkit toolkit, Composite parent, String label, String value, int style) {
		Composite composite = toolkit.createComposite(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(composite);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 3;
		composite.setLayout(layout);
		toolkit.createLabel(composite, label, SWT.NONE).setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		
		toolkit.createLabel(composite,  value, SWT.NONE).setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
	}
}
