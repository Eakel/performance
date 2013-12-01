package com.easyfun.eclipse.performance.jmx.util;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The JNDI Setting Dialogs
 * @author linzhaoming Create Date: 2010-8-14
 */
public class JMXSetttingsDialog extends Dialog {

	public JMXSetttingsDialog(Shell shell) {
		super(shell);
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setSize(500, 400);
		newShell.setText("JMX Settings");
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout(2,false));
		
		GridData gridData = new GridData(SWT.RIGHT, SWT.TOP, false, false);

		Label label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		label.setText("Connection:");
		
		Text connectionText = new Text(composite, SWT.BORDER);
		connectionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		connectionText.setText("Untitled");
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		label.setText("Default Domain:");
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		label.setText("");
		
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		label.setText("MBean Number:");
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		label.setText("");
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		label.setText("Factory:");
		
		Text factoryText = new Text(composite, SWT.BORDER);
		factoryText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		factoryText.setText("org.jnp.interfaces.NamingContextFactory");
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		label.setText("Packages:");
		
		Text packagesText = new Text(composite, SWT.BORDER);
		packagesText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL|SWT.LEFT));
		packagesText.setText("org.jboss.naming:org.jnp.interfaces");
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		label.setText("URL:");
		
		Text urlText = new Text(composite, SWT.BORDER);
		urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		urlText.setText("jnp://localhost:1099");
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		label.setText("JNDI Binding:");
		
		Text contextText = new Text(composite, SWT.BORDER);
		contextText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		contextText.setText("");
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		label.setText("Principal:");
		
		Text principalText = new Text(composite, SWT.BORDER);
		principalText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		principalText.setText("");
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		label.setText("Credentials:");
		
		Text credentialText = new Text(composite, SWT.BORDER);
		credentialText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		credentialText.setText("");
		
		label = new Label(composite, SWT.NULL);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		label.setText("Service URL:");
		
		Text serviceURLText = new Text(composite, SWT.BORDER);
		serviceURLText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		serviceURLText.setText("");
		
		Button connectButton = new Button(composite, SWT.PUSH);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		connectButton.setLayoutData(gridData);
		connectButton.setText("Connect to JMX Server");
		connectButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				System.out.println("selected");
			}			
		});
		
		Button disconnectButton = new Button(composite, SWT.PUSH);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		disconnectButton.setLayoutData(gridData);
		disconnectButton.setText("Disconnect from JMX Server");
		disconnectButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				System.out.println("selected");
			}			
		});

		
		Button refreshButton = new Button(composite, SWT.PUSH);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		refreshButton.setLayoutData(gridData);
		refreshButton.setText("Refresh the connect of JMX Server");
		refreshButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				System.out.println("selected");
			}			
		});
		
		return composite;
	}
}
