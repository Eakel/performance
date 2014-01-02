package com.easyfun.eclipse.component.browser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * 基于标签式的浏览器
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-2
 */
public class SWTBrower extends Composite{
	/** 最新输入的链接*/
	private volatile String newUrl = null;
	/** 表示当前页面完全导入*/
	private volatile boolean isLoadCompleted = false;
	
	/** 表示新的页面在新窗口中打开 */
	private volatile boolean isOpenNewItem = false;

	/** 当前标签项*/
	private TabItem currentTabItem;

	/** 当前功能浏览器*/
	private Browser currentBrowser;

	/** 浏览器首页*/
	private String DEFAULT_HOME_URL = "http://10.3.3.213:28888/xzngcrm@crm-237/";

	/** 后退按钮 */
	private Button backButton;
	/** 向前按钮 */
	private Button forwardButton;
	/** 停止按钮 */
	private Button stopButton;
	/** 地址栏 */
	private Combo addressCombo;
	/** 网页打开进度表，即页面导入情况栏 */
	private ProgressBar statusBar;
	/** 最终网页打开过程显示 */
	private Label statusLabel;
	private TabFolder tabFolder;
	
	public SWTBrower(Composite parent){
		super(parent, SWT.NULL);
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		initial(this);
	}

	protected void initial(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		parent.setLayout(gridLayout);

		// createMenu();//没有实现
		createToolComposite(parent);
		createBrowserComposite(parent);
		createStatusComposite(parent);

		refreshCurrentBrower(parent);
	}

	/** 创建浏览器地址栏*/
	private void createToolComposite(Composite parent) {
		Composite toolComposite = new Composite(parent, SWT.BORDER);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.heightHint = 30;
		gridData.widthHint = 549;
		toolComposite.setLayoutData(gridData);
		toolComposite.setLayout(new GridLayout(8, false));
		
		backButton = new Button(toolComposite, SWT.NONE);
		backButton.setLayoutData(new GridData(50, SWT.DEFAULT));
		backButton.setText("Back");
		
		forwardButton = new Button(toolComposite, SWT.NONE);
		forwardButton.setLayoutData(new GridData(60, SWT.DEFAULT));
		forwardButton.setText("Forward");

		addressCombo = new Combo(toolComposite, SWT.BORDER);
		gridData = new GridData(SWT.FILL, SWT.LEFT, true, false);
		gridData.widthHint = 300;// 起始宽度
		gridData.minimumWidth = 50;// 设置最小宽度
		addressCombo.setLayoutData(gridData);
		
		Button goButton = new Button(toolComposite, SWT.NONE);
		goButton.setLayoutData(new GridData(25, SWT.DEFAULT));
		goButton.setText("Go");
		
		stopButton = new Button(toolComposite, SWT.NONE);
		stopButton.setLayoutData(new GridData(40, SWT.DEFAULT));
		stopButton.setText("Stop");
		
		Label label = new Label(toolComposite, SWT.SEPARATOR | SWT.VERTICAL);
		label.setLayoutData(new GridData(2, 17));
	}

	/** 创建浏览器Composite*/
	private void createBrowserComposite(final Composite parent) {
		Composite browserComposite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 273;
		browserComposite.setLayoutData(gridData);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		browserComposite.setLayout(gridLayout);
		
		tabFolder = new TabFolder(browserComposite, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 312;
		gridData.widthHint = 585;
		tabFolder.setLayoutData(gridData);

		tabFolder.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				if (e.button == 3) {// 右键
					Menu menu = new Menu(parent);
					tabFolder.setMenu(menu);
					MenuItem closeItem = new MenuItem(menu, SWT.NONE);
					closeItem.setText("关闭当前标签");
					closeItem.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							if (tabFolder.getItemCount() != 1) {// 不是只存在一个标签的情况下
								currentBrowser.dispose();
								currentTabItem.dispose();
								tabFolder.redraw();
							} else {// 只有一个标签
								currentBrowser.setUrl(":blank");
								currentBrowser.setText("");
							}
						}
					});
					MenuItem closeAllMenuItem = new MenuItem(menu, SWT.NONE);
					closeAllMenuItem.setText("关闭所有标签");
					closeAllMenuItem.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							parent.getShell().close();
						}
					});
				}
			}
		});

		TabItem defaultTabItem = new TabItem(tabFolder, SWT.NONE);
		Browser defaultBrowser = new Browser(tabFolder, SWT.NONE);
		defaultTabItem.setControl(defaultBrowser);
		defaultBrowser.setUrl(DEFAULT_HOME_URL);// 显示浏览器首页
		tabFolder.setSelection(defaultTabItem);
	}

	/** 创建状态栏*/
	private void createStatusComposite(Composite parent) {
		Composite statusComposite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);// 参数true使状态栏可以自动水平伸缩
		gridData.heightHint = 20;
		gridData.widthHint = 367;
		statusComposite.setLayoutData(gridData);
		
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginBottom = 5;
		statusComposite.setLayout(gridLayout);
		
		statusLabel = new Label(statusComposite, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gridData.heightHint = 13;
		gridData.widthHint = 525;
		statusLabel.setLayoutData(gridData);
		
		statusBar = new ProgressBar(statusComposite, SWT.BORDER | SWT.SMOOTH);
		statusBar.setLayoutData(new GridData(80, 12));
		statusBar.setVisible(false);
	}

	private void refreshCurrentBrower(final Composite parent) {
		backButton.setEnabled(false);
		forwardButton.setEnabled(false);

		currentTabItem = tabFolder.getItem(tabFolder.getSelectionIndex());
		currentBrowser = (Browser) currentTabItem.getControl();

		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TabItem temp = (TabItem) e.item;
				if (temp != currentTabItem) {// 防止重选一个标签，预防多次触发相同事件
					currentTabItem = temp;
					currentBrowser = (Browser) currentTabItem.getControl();
					backButton.setEnabled(currentBrowser.isBackEnabled());
					forwardButton.setEnabled(currentBrowser.isForwardEnabled());
				}
			}
		});

		backButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (currentBrowser.isBackEnabled()) {	// 本次可后退
					currentBrowser.back();
					if(!currentBrowser.getUrl().equals("about:blank")){
						forwardButton.setEnabled(true);		// 下次可前进，前进按钮可用
					}
				}
				if (!currentBrowser.isBackEnabled()) {	// 下次不可后退，后退按钮不可用
					backButton.setEnabled(false);
				}
			}
		});
		
		forwardButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (currentBrowser.isForwardEnabled()) {	// 本次可前进
					currentBrowser.forward();
					backButton.setEnabled(true);			// 后退按钮可用
				}
				if (!currentBrowser.isForwardEnabled()) {	// 下次不可前进，前进按钮不可用
					forwardButton.setEnabled(false);
				}
			}
		});
		
		stopButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				currentBrowser.stop();
			}
		});

		addressCombo.addKeyListener(new KeyAdapter() {		// 手动输入地址栏后，按回车键转到相应网址
					public void keyReleased(KeyEvent e) {
						if (e.keyCode == SWT.CR) {			// 回车键触发事件
							currentBrowser.setUrl(addressCombo.getText());
						}
					}
				});

		currentBrowser.addLocationListener(new LocationAdapter() {
			public void changing(LocationEvent e) {	
				// 表示超级链接地址改变了
				if (isOpenNewItem == false) {		// 新的页面在同一标签中打开
					backButton.setEnabled(true);	// 后退按钮可用,此句是后退按钮可用判定的逻辑开始点
				}
			}

			public void changed(LocationEvent e) {
				// 找到了页面链接地址
				addressCombo.setText(e.location);// 改变链接地址显示
				if (isOpenNewItem == true) {
					isOpenNewItem = false;
				}
			}
		});
		
		currentBrowser.addTraverseListener(new TraverseListener(){
			public void keyTraversed(TraverseEvent e) {
				System.out.println(e);
			}
		});

		currentBrowser.addProgressListener(new ProgressAdapter() {
			public void changed(ProgressEvent e) {	
				// 本事件不断发生于页面的导入过程中
				statusBar.setMaximum(e.total);		//e.total表示从最开始页面到最终页面的数值
				statusBar.setSelection(e.current);
				if (e.current != e.total) {
					isLoadCompleted = false;
					statusBar.setVisible(true);
				} else {
					isLoadCompleted = true;
					statusBar.setVisible(false);
				}
			}

			public void completed(ProgressEvent arg0) {
				// 发生在一次导入页面时,
				// 本监听器changed事件最后一次发生之前
			}
		});

		currentBrowser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent e) {
				if (isLoadCompleted == false) {
					statusLabel.setText(e.text);
				} else {
					newUrl = e.text;// 页面导入完成，捕捉页面上可能打开的链接
				}
			}
		});

		currentBrowser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent e) {
				parent.getShell().setText(e.title);
				if (e.title.length() > 3) {
					// 显示当前页面提示字符在标签上
					currentTabItem.setText(e.title.substring(0, 3) + "..");
				} else {
					currentTabItem.setText(e.title);
				}
				currentTabItem.setToolTipText(e.title);	// 标签显示提示符
			}
		});

		currentBrowser.addOpenWindowListener(new OpenWindowListener() {
				//在当前页面中打开点击的链接页面
					public void open(WindowEvent e) {
						Browser newBrowser = new Browser(tabFolder, SWT.NONE);
						TabItem newTabItem = new TabItem(tabFolder, SWT.NONE);
						newTabItem.setControl(newBrowser);
						tabFolder.setSelection(newTabItem);
						// 新打开的页面标签置顶
						tabFolder.redraw();
						newBrowser.setUrl(newUrl);		// 新标签中设置新的链接地址
						isOpenNewItem = true;			// 新的页面在新的标签中打开
						e.browser = newBrowser;
						currentBrowser.getShell().getDisplay().syncExec(new Runnable() {
							public void run() {
								refreshCurrentBrower(parent);
							}
						});
					}
				});

		currentBrowser.addCloseWindowListener(new CloseWindowListener() {
			public void close(WindowEvent e) {
				currentBrowser.dispose();
			}
		});
	}
	
	public Browser getCurrentBrowser(){
		return currentBrowser;
	}
	
	public void setStatusText(String msg){
		statusLabel.setText(msg);
	}
}