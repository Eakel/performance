package com.easyfun.eclipse.common.ui.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.utils.ui.LayoutUtil;

/**
 * 打开文件Composite 
 * @author linzhaoming
 *
 * 2011-5-15
 *
 */
public class FileFieldComposite extends Composite{
	private Text fileNameText;
	
	private boolean displayText = true;
	
	List<IFilechangeListener> listeners = new ArrayList<IFilechangeListener>();
	
	public FileFieldComposite(Composite parent, int style, boolean displayText){
		super(parent, style);
		this.displayText = displayText;
		init(this);
	}
	
	private void init(Composite p){
		p.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		p.setLayout(LayoutUtil.getNoMarginLayout(2, false));

		final Button button = new Button(p, SWT.NONE);
		GridData gridData = new GridData(SWT.LEFT, SWT.TOP, false, false);
		gridData.widthHint = 60;
		button.setLayoutData(gridData);
		button.setText("文件...");
		
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				openFileDialog();
			}
		});

		fileNameText = new Text(p, SWT.BORDER);
		fileNameText.setEditable(false);
		
		if(displayText == false){
			fileNameText.setVisible(false);
			gridData = new GridData(SWT.LEFT, SWT.TOP, true, false);
			gridData.widthHint = 0;
			fileNameText.setLayoutData(gridData);
		}else{
			fileNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));	
		}
	}
	
	public void openFileDialog(){
		FileDialog dialog = new FileDialog(getShell());
		String path = dialog.open();
		if(path != null){
			File file = new File(path);
			if(file.exists() == false || file.isDirectory() == true){
				return;
			}					
			fileNameText.setText(file.getAbsolutePath());
			
			for (IFilechangeListener listener : listeners) {
				listener.onFileChange(file);
			}
		}
	}
	
	public void addFileChangListener(IFilechangeListener listener){
		listeners.add(listener);
	}
	
	/**
	 * 文件监听器
	 * @author linzhaoming
	 *
	 * 2011-5-15
	 *
	 */
	public static interface IFilechangeListener{
		public void onFileChange(File file);
	}
}
