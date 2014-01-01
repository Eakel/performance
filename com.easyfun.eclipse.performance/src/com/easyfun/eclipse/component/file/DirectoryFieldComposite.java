package com.easyfun.eclipse.component.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.performance.ImageConstants;
import com.easyfun.eclipse.performance.PerformanceActivator;
import com.easyfun.eclipse.performance.preferences.PreferenceConstants;
import com.easyfun.eclipse.performance.preferences.EasyFunPrefUtil;
import com.easyfun.eclipse.rcp.RCPUtil;

/**
 * 打开目录Composite
 * 
 * @author linzhaoming
 *
 * 2013-4-6
 *
 */
public class DirectoryFieldComposite extends Composite{
	private Text dirNameText;
	
	private boolean displayText = true;
	
	List<IDiectorychangeListener> listeners = new ArrayList<IDiectorychangeListener>();
	
	/** 保存的默认KEY*/
	private String prefKey = PreferenceConstants.EASYFUN_DIRECTORY;
	
	/** 窗口显示的提示语*/
	private String diagMsg = "请选择目录";
	
	public DirectoryFieldComposite(Composite parent, int style, boolean displayText){
		super(parent, style);
		this.displayText = displayText;
		init(this);
	}
	
	private void init(Composite p){
		p.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		p.setLayout(RCPUtil.getNoMarginLayout(2, false));

		final Button button = new Button(p, SWT.NONE);
		GridData gridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
		gridData.widthHint = 70;
		button.setLayoutData(gridData);
		button.setText("目 录...");
		button.setImage(PerformanceActivator.getImageDescriptor(ImageConstants.ICON_FOLDER_PATH).createImage());
		
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				openDirDialog();
			}
		});

		dirNameText = new Text(p, SWT.BORDER);
		dirNameText.setEditable(false);
		
		if(displayText == false){
			dirNameText.setVisible(false);
			gridData = new GridData(SWT.LEFT, SWT.TOP, true, false);
			gridData.widthHint = 0;
			dirNameText.setLayoutData(gridData);
		}else{
			dirNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));	
		}
	}
	
	public void openDirDialog(){
		DirectoryDialog dialog = new DirectoryDialog(getShell());		
		dialog.setText("选择目录");
		dialog.setMessage(diagMsg);
		
		String dir = EasyFunPrefUtil.getPreferenceStore().getString(prefKey);
		if(StringUtils.isNotEmpty(dir)){
			dialog.setFilterPath(dir);
		}
		
		String path = dialog.open();
		if(StringUtils.isNotEmpty(path)){
			File file = new File(path);
			if(file.exists() == false || file.isDirectory() == false){
				return;
			}					
			dirNameText.setText(file.getAbsolutePath());
			EasyFunPrefUtil.getPreferenceStore().setValue(prefKey, file.getAbsolutePath());
			
			for (IDiectorychangeListener listener : listeners) {
				listener.onDirChange(file);
			}
		}
	}
	
	public void addFileChangListener(IDiectorychangeListener listener){
		listeners.add(listener);
	}
	
	/** 保存的默认KEY*/
	public void setPreferenceKey(String prefKey){
		this.prefKey = prefKey;
	}
	
	/** 窗口显示的提示语*/
	public void setDiagMsg(String diagMsg){
		this.diagMsg = diagMsg;
	}
	
	/**
	 * 目录监听器
	 * @author linzhaoming
	 *
	 * 2011-5-15
	 *
	 */
	public static interface IDiectorychangeListener{
		public void onDirChange(File dirFile);
	}
}
