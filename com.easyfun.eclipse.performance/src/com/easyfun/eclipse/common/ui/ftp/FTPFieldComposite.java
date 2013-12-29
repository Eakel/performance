package com.easyfun.eclipse.common.ui.ftp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.common.util.ui.DialogUtils;
import com.easyfun.eclipse.common.util.ui.PreferenceUtil;
import com.easyfun.eclipse.common.util.ui.SWTUtil;
import com.easyfun.eclipse.performance.preferences.PreferenceConstants;

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
		ftpComposite.setLayout(SWTUtil.getNoMarginLayout(2, false));

		final Button ftpButton = new Button(ftpComposite, SWT.NONE);
		GridData gridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
		gridData.widthHint = 60;
		ftpButton.setLayoutData(gridData);
		ftpButton.setText("FTP...");
		
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
		FTPBean ftpBean = new FTPBean();
		IPreferenceStore store = PreferenceUtil.getPreferenceStore();
		String ftpDesc = store.getString(PreferenceConstants.EASYFUN_FTP);
		if (StringUtils.isNotEmpty(ftpDesc)) {
			String[] strs = ftpDesc.split(",");
			// "localhost,21,jvm.log,0,linzm,pass"
			if (strs.length == 6) {
				try {
					ftpBean.setHost(strs[0]);
					ftpBean.setPort(Integer.valueOf(strs[1]));
					ftpBean.setFilePath(strs[2]);
					ftpBean.setFtpType(Integer.valueOf(strs[3]));
					ftpBean.setUserName(strs[4]);
					ftpBean.setPasswd(strs[5]);
				} catch (Exception e) {
					e.printStackTrace();
					ftpBean = new FTPBean();
				}
			}
		}
		FTPDialog dialog = new FTPDialog(getShell(), ftpBean);
		if (dialog.open() == Dialog.OK) {
			ftpBean = dialog.getFTPBean();
			ftpDescText.setText(ftpBean.getFTPDesc());
			String ftpDesc2 = ftpBean.getHost() + "," + ftpBean.getPort() + "," + ftpBean.getFilePath() + "," + ftpBean.getFtpType() + "," + ftpBean.getUserName()
					+ "," + ftpBean.getPasswd();
			store.setValue(PreferenceConstants.EASYFUN_FTP, ftpDesc2);
			try {
				for (IFTPchangeListener listener : listeners) {
					listener.onFTPChange(ftpBean);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				DialogUtils.showError(getShell(), ex.getMessage());
			}
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
		public void onFTPChange(FTPBean ftpBean);
	}
}
