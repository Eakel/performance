package com.easyfun.eclipse.performance.awr.item;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.internal.dialogs.WorkbenchPreferenceDialog;

import com.easyfun.eclipse.common.config.cfg.Item;
import com.easyfun.eclipse.common.console.LogHelper;
import com.easyfun.eclipse.common.jdbc.ConnectionModel;
import com.easyfun.eclipse.common.util.DialogUtils;
import com.easyfun.eclipse.common.view.item.pub.ItemComposite;
import com.easyfun.eclipse.performance.awr.AWRUtil;
import com.easyfun.eclipse.performance.awr.model.AWRTableViewer;
import com.easyfun.eclipse.performance.awr.model.SnapShot;
import com.easyfun.eclipse.performance.awr.preferences.AwrPrefPage;
import com.easyfun.eclipse.performance.awr.preferences.AwrPrefUtil;
import com.easyfun.eclipse.utils.resource.FileUtil;
import com.easyfun.eclipse.utils.ui.SWTUtil;

/**
 * Oracle AWR
 * @author linzhaoming
 *
 * 2012-9-23
 *
 */
public class OracleAWRComposite extends ItemComposite {

	private ConnectionModel connectionModel;
	private TabFolder tabFolder;
	private AWRTableViewer awrTableViewer;
	
	private static String AWR_HTML_TITLE = "AWR报告(HTML)";
	private static String AWR_TEXT_TITLE = "AWR报告(Text)";
	
	private Browser awrBrowser;
	private StyledText awrText;
	
	private List<SnapShot> selectedSnaps;

	private Button exportButton;

	private Button genHTMLButton;

	private Button genTextButton;
	
	/**
	 * Create the composite
	 * @param parent
	 * @param style
	 */
	public OracleAWRComposite(Composite parent, int style, Item item) {
		super(parent, style, item);
		setLayout(new GridLayout());

		final Composite topComposite = new Composite(this, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(4, false);
		topComposite.setLayout(gridLayout);

		final Button dbButton = new Button(topComposite, SWT.NONE);
		dbButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				WorkbenchPreferenceDialog dialog = WorkbenchPreferenceDialog.createDialogOn(getShell(), AwrPrefPage.ID);
			    dialog.showOnly(new String[] { AwrPrefPage.ID });
			    dialog.open();
			}
		});
		dbButton.setText("数据库设定");

		final Button anaButton = new Button(topComposite, SWT.NONE);
		anaButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				connectionModel = AwrPrefUtil.getConnectionModel();
				try {
					Connection conn = connectionModel.getRefreshConnection();
					DatabaseMetaData metaData = conn.getMetaData();
					LogHelper.debug("使用的用户名为:" + metaData.getUserName());
					initByConnection(conn);
					exportButton.setEnabled(false);
					genHTMLButton.setEnabled(true);
					genTextButton.setEnabled(true);
				} catch (Exception ex) {
					SWTUtil.showError(getShell(), ex.getMessage());
					LogHelper.error("获取AWR日志失败", ex);
					exportButton.setEnabled(false);
					genHTMLButton.setEnabled(false);
					genTextButton.setEnabled(false);
				}
			}
		});
		anaButton.setText("获取AWR日志");
		
		genHTMLButton = new Button(topComposite, SWT.NONE);
		genHTMLButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				try {
					String tabTitle = AWR_HTML_TITLE;
					Iterator iter = ((IStructuredSelection)awrTableViewer.getSelection()).iterator();
					selectedSnaps = new ArrayList();
					while(iter.hasNext()){
						SnapShot snap = (SnapShot)iter.next();
						selectedSnaps.add(snap);
					}
					
					if(selectedSnaps.size() <2){
						SWTUtil.showMessage(getShell(), "需要选择至少两条记录");
						return;
					}
					
					Connection conn = connectionModel.getRefreshConnection();
					
					//找出最小和最大的编号
					long min = ((SnapShot)selectedSnaps.get(0)).getSnapId();
					long max = ((SnapShot)selectedSnaps.get(0)).getSnapId();
					
					for(int i=0; i<selectedSnaps.size(); i++){
						SnapShot obj = (SnapShot)selectedSnaps.get(i);
						if(obj.getSnapId() > max){
							max = obj.getSnapId();
						}
						
						if(obj.getSnapId() < min){
							min = obj.getSnapId();
						}
					}
					
					SnapShot snap = (SnapShot)selectedSnaps.get(0);
					
					String str = AWRUtil.genAWRHtml(conn, selectedSnaps, min, max, snap.getDbId(), snap.getInstanceNumber());
					
					TabItem[] tabItems = tabFolder.getItems();
					
					for(int i=1; i<tabItems.length; i++){						
						if(tabItems[i].getText().equals(tabTitle)){
							tabItems[i].dispose();
						}
					}
					
					final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
					tabItem.setText(tabTitle);
					
					awrBrowser = new Browser(tabFolder, SWT.NULL);
				    awrBrowser.setText(str);
				    tabItem.setControl(awrBrowser);
				    tabFolder.setSelection(tabFolder.getItems().length -1);
				    exportButton.setEnabled(true);
				} catch (Exception e1) {
					e1.printStackTrace();
					exportButton.setEnabled(false);
					SWTUtil.showError(getShell(), e1.getMessage());
				}
			}
		});
		genHTMLButton.setText("生成AWR(HTML)");
		
		genTextButton = new Button(topComposite, SWT.NONE);
		genTextButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				try {
					String tabTitle = AWR_TEXT_TITLE;
					Iterator iter = ((IStructuredSelection)awrTableViewer.getSelection()).iterator();
					List selectedSnaps = new ArrayList();
					while(iter.hasNext()){
						SnapShot snap = (SnapShot)iter.next();
						selectedSnaps.add(snap);
					}
					
					if(selectedSnaps.size() <2){
						SWTUtil.showMessage(getShell(), "需要选择至少两条记录");
						return;
					}
					
					Connection conn = connectionModel.getRefreshConnection();
					
					//找出最小和最大的编号
					long min = ((SnapShot)selectedSnaps.get(0)).getSnapId();
					long max = ((SnapShot)selectedSnaps.get(0)).getSnapId();
					
					for(int i=0; i<selectedSnaps.size(); i++){
						SnapShot obj = (SnapShot)selectedSnaps.get(i);
						if(obj.getSnapId() > max){
							max = obj.getSnapId();
						}
						
						if(obj.getSnapId() < min){
							min = obj.getSnapId();
						}
					}
					
					SnapShot snap = (SnapShot)selectedSnaps.get(0);
					
					String str = AWRUtil.genAWRText(conn, selectedSnaps, min, max, snap.getDbId(), snap.getInstanceNumber());
					
					TabItem[] tabItems = tabFolder.getItems();
					for(int i=1; i<tabItems.length; i++){
						if(tabItems[i].getText().equals(tabTitle)){
							tabItems[i].dispose();
						}
					}
					
					final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
					tabItem.setText(tabTitle);
					
					awrText = new StyledText(tabFolder, SWT.V_SCROLL|SWT.H_SCROLL);
				    awrText.setText(str);
				    tabItem.setControl(awrText);
				    
				    tabFolder.setSelection(tabFolder.getItems().length -1);
				    exportButton.setEnabled(true);
				} catch (Exception e1) {
					e1.printStackTrace();
					exportButton.setEnabled(false);
					SWTUtil.showError(getShell(), e1.getMessage());
				}
			}
		});
		genTextButton.setText("生成AWR(TEXT)");
		
		exportButton = new Button(topComposite, SWT.NONE);
		exportButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					exportUIResult(tabFolder);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		exportButton.setText("导出...");
		

		final Composite mainComposite = new Composite(this, SWT.NONE);
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		mainComposite.setLayout(new GridLayout());

		tabFolder = new TabFolder(mainComposite, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		init();
	}
	
	private AWRTableViewer createNewTab(TabFolder tabFolder, String tabName, String[] titles){
		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(tabName);

		awrTableViewer = new AWRTableViewer(tabFolder, titles);
		tabItem.setControl(awrTableViewer.getTable());
		return awrTableViewer;
	}
	
	private void initByConnection(Connection conn) throws Exception{
		long begin = System.currentTimeMillis();
		List snaps = AWRUtil.getSnaps(conn);
		
		LogHelper.info("读取AWR记录的数据库时间：" + (System.currentTimeMillis() - begin) + " ms");
		updateResult(snaps);
	}
	
	private void updateResult(List snaps) throws Exception{
		awrTableViewer.setInput(snaps);
	}
	
	/**
	 * 将TabFolder的内容导出
	 * @param tabFolder
	 * @throws Exception
	 */
	private void exportUIResult(TabFolder tabFolder) throws Exception{
		long begin = System.currentTimeMillis();
		
		if(! ((awrBrowser != null && awrBrowser.isVisible())) || (awrText != null && awrText.isVisible())){
			SWTUtil.showMessage(getShell(), "请先生成AWR报告，再导出");
			return;
		}
		
		long min = ((SnapShot)selectedSnaps.get(0)).getSnapId();
		long max = ((SnapShot)selectedSnaps.get(0)).getSnapId();
		
		String beginDate = selectedSnaps.get(0).getBeginDate();
		String endDate = selectedSnaps.get(0).getEndDate();
		
		for(int i=0; i<selectedSnaps.size(); i++){
			SnapShot obj = (SnapShot)selectedSnaps.get(i);
			if(obj.getSnapId() > max){
				max = obj.getSnapId();
			}
			
			if(obj.getSnapId() < min){
				min = obj.getSnapId();
			}
		}
		
		String fileName = selectedSnaps.get(0).getInstanceName() + "_" + beginDate +  "-" + endDate;
		
		//导出HTML
		if(awrBrowser != null && awrBrowser.isVisible()){
			File file = DialogUtils.openSaveDialog(getShell(), new String[]{"*.html", "*.*"}, fileName + ".html");
			if(file == null){
				return;
			}
			
			FileUtil.writeTextFile(awrBrowser.getText(), file);
		}
		
		//导出文本
		if(awrText != null && awrText.isVisible()){
			File file = DialogUtils.openSaveDialog(getShell(), new String[]{"*.txt", "*.*"}, fileName + ".txt");
			if(file == null){
				return;
			}
			
			FileUtil.writeTextFile(awrText.getText(), file);
		}

		// 写入数据并关闭文件
		
		System.out.println("写入文件时间: " + (System.currentTimeMillis() -begin)  + " ms");
	}
	
	private void init(){
		List list = new ArrayList();
		String[] titles = new String[]{"SNAP_ID", "BIGIN_DATE", "END_DATE", "DBID", "INSTANCE_NUMBER", "INSTANCE_NAME", "VERSION", "GET_HOST_NAME"};
		AWRTableViewer tableViewer = createNewTab(tabFolder, "AWR报告", titles);
		tableViewer.setInput(list);
		
		exportButton.setEnabled(false);
		genHTMLButton.setEnabled(false);
		genTextButton.setEnabled(false);
	}

	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
