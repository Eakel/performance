package com.easyfun.eclipse.component.ftp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.performance.ImageConstants;
import com.easyfun.eclipse.performance.PerformanceActivator;
import com.easyfun.eclipse.performance.preferences.FTPPreferencePage;
import com.easyfun.eclipse.rcp.RCPUtil;

/**
 * ¶ÁÈ¡FTPµÄDialog
 * @author linzhaoming
 *
 * 2011-5-15
 *
 */
public class FTPFieldComposite extends Composite{
	private Text ftpDescText;
	private boolean display = true;
	
	List<IFTPchangeListener> listeners = new ArrayList<IFTPchangeListener>();
	
	public FTPFieldComposite(Composite parent, int style, boolean display){
		super(parent, style);
		this.display = display;
		init(this);
	}
	
	private void init(Composite ftpComposite){
		ftpComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		ftpComposite.setLayout(RCPUtil.getNoMarginLayout(2, false));

		final Button ftpButton = new Button(ftpComposite, SWT.NONE);
		GridData gridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
		gridData.widthHint = 70;
		ftpButton.setLayoutData(gridData);
		ftpButton.setText("FTP...");
		ftpButton.setImage(PerformanceActivator.getImageDescriptor(com.easyfun.eclipse.component.ftp.ui.FTPImageConstants.ICON_FTP).createImage());
		
		ftpButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				openFTPDialog();
			}
		});

		ftpDescText = new Text(ftpComposite, SWT.BORDER);
		ftpDescText.setEditable(false);
		
		if(display){
			ftpDescText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));	
		}else{
			ftpDescText.setVisible(false);
			gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
			gridData.widthHint = 0;			
			ftpDescText.setLayoutData(gridData);
		}
	}
	
	public void openFTPDialog(){
		RCPUtil.showPreferencPage(getShell(), FTPPreferencePage.PREF_ID);
		FTPHostBean ftpBean = FTPUtil.getSelectBean();
		if(ftpBean == null){
			return;
		}
		try {
			for (IFTPchangeListener listener : listeners) {
				listener.onFTPChange(ftpBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			RCPUtil.showError(getShell(), ex.getMessage());
		}
	}
	
	public void addFTPChangListener(IFTPchangeListener listener){
		listeners.add(listener);
	}
	
	/**
	 * FTP¼àÌýÆ÷
	 * @author linzhaoming
	 *
	 * 2011-5-15
	 *
	 */
	public static interface IFTPchangeListener{
		public void onFTPChange(FTPHostBean ftpBean);
	}
}
