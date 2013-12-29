package com.easyfun.eclipse.util.demo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author linzhaoming Create Date: 2010-11-30
 */
public class SWTTest {
	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setSize(200, 200);
		shell.setLayout(new GridLayout());
		shell.setText("≤‚ ‘ π”√");
		
		Button bu = new Button(shell, SWT.NULL);
		bu.setLayoutData(new GridData());
		bu.setText("Push");
		bu.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
			}
			
		});
		
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
