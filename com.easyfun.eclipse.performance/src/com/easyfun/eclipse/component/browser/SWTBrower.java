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
 * ���ڱ�ǩʽ�������
 * 
 * @author linzhaoming
 *
 * @Created 2014-1-2
 */
public class SWTBrower extends Composite{
	/** �������������*/
	private volatile String newUrl = null;
	/** ��ʾ��ǰҳ����ȫ����*/
	private volatile boolean isLoadCompleted = false;
	
	/** ��ʾ�µ�ҳ�����´����д� */
	private volatile boolean isOpenNewItem = false;

	/** ��ǰ��ǩ��*/
	private TabItem currentTabItem;

	/** ��ǰ���������*/
	private Browser currentBrowser;

	/** �������ҳ*/
	private String DEFAULT_HOME_URL = "http://10.3.3.213:28888/xzngcrm@crm-237/";

	/** ���˰�ť */
	private Button backButton;
	/** ��ǰ��ť */
	private Button forwardButton;
	/** ֹͣ��ť */
	private Button stopButton;
	/** ��ַ�� */
	private Combo addressCombo;
	/** ��ҳ�򿪽��ȱ���ҳ�浼������� */
	private ProgressBar statusBar;
	/** ������ҳ�򿪹�����ʾ */
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

		// createMenu();//û��ʵ��
		createToolComposite(parent);
		createBrowserComposite(parent);
		createStatusComposite(parent);

		refreshCurrentBrower(parent);
	}

	/** �����������ַ��*/
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
		gridData.widthHint = 300;// ��ʼ���
		gridData.minimumWidth = 50;// ������С���
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

	/** ���������Composite*/
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
				if (e.button == 3) {// �Ҽ�
					Menu menu = new Menu(parent);
					tabFolder.setMenu(menu);
					MenuItem closeItem = new MenuItem(menu, SWT.NONE);
					closeItem.setText("�رյ�ǰ��ǩ");
					closeItem.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							if (tabFolder.getItemCount() != 1) {// ����ֻ����һ����ǩ�������
								currentBrowser.dispose();
								currentTabItem.dispose();
								tabFolder.redraw();
							} else {// ֻ��һ����ǩ
								currentBrowser.setUrl(":blank");
								currentBrowser.setText("");
							}
						}
					});
					MenuItem closeAllMenuItem = new MenuItem(menu, SWT.NONE);
					closeAllMenuItem.setText("�ر����б�ǩ");
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
		defaultBrowser.setUrl(DEFAULT_HOME_URL);// ��ʾ�������ҳ
		tabFolder.setSelection(defaultTabItem);
	}

	/** ����״̬��*/
	private void createStatusComposite(Composite parent) {
		Composite statusComposite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);// ����trueʹ״̬�������Զ�ˮƽ����
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
				if (temp != currentTabItem) {// ��ֹ��ѡһ����ǩ��Ԥ����δ�����ͬ�¼�
					currentTabItem = temp;
					currentBrowser = (Browser) currentTabItem.getControl();
					backButton.setEnabled(currentBrowser.isBackEnabled());
					forwardButton.setEnabled(currentBrowser.isForwardEnabled());
				}
			}
		});

		backButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (currentBrowser.isBackEnabled()) {	// ���οɺ���
					currentBrowser.back();
					if(!currentBrowser.getUrl().equals("about:blank")){
						forwardButton.setEnabled(true);		// �´ο�ǰ����ǰ����ť����
					}
				}
				if (!currentBrowser.isBackEnabled()) {	// �´β��ɺ��ˣ����˰�ť������
					backButton.setEnabled(false);
				}
			}
		});
		
		forwardButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (currentBrowser.isForwardEnabled()) {	// ���ο�ǰ��
					currentBrowser.forward();
					backButton.setEnabled(true);			// ���˰�ť����
				}
				if (!currentBrowser.isForwardEnabled()) {	// �´β���ǰ����ǰ����ť������
					forwardButton.setEnabled(false);
				}
			}
		});
		
		stopButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				currentBrowser.stop();
			}
		});

		addressCombo.addKeyListener(new KeyAdapter() {		// �ֶ������ַ���󣬰��س���ת����Ӧ��ַ
					public void keyReleased(KeyEvent e) {
						if (e.keyCode == SWT.CR) {			// �س��������¼�
							currentBrowser.setUrl(addressCombo.getText());
						}
					}
				});

		currentBrowser.addLocationListener(new LocationAdapter() {
			public void changing(LocationEvent e) {	
				// ��ʾ�������ӵ�ַ�ı���
				if (isOpenNewItem == false) {		// �µ�ҳ����ͬһ��ǩ�д�
					backButton.setEnabled(true);	// ���˰�ť����,�˾��Ǻ��˰�ť�����ж����߼���ʼ��
				}
			}

			public void changed(LocationEvent e) {
				// �ҵ���ҳ�����ӵ�ַ
				addressCombo.setText(e.location);// �ı����ӵ�ַ��ʾ
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
				// ���¼����Ϸ�����ҳ��ĵ��������
				statusBar.setMaximum(e.total);		//e.total��ʾ���ʼҳ�浽����ҳ�����ֵ
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
				// ������һ�ε���ҳ��ʱ,
				// ��������changed�¼����һ�η���֮ǰ
			}
		});

		currentBrowser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent e) {
				if (isLoadCompleted == false) {
					statusLabel.setText(e.text);
				} else {
					newUrl = e.text;// ҳ�浼����ɣ���׽ҳ���Ͽ��ܴ򿪵�����
				}
			}
		});

		currentBrowser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent e) {
				parent.getShell().setText(e.title);
				if (e.title.length() > 3) {
					// ��ʾ��ǰҳ����ʾ�ַ��ڱ�ǩ��
					currentTabItem.setText(e.title.substring(0, 3) + "..");
				} else {
					currentTabItem.setText(e.title);
				}
				currentTabItem.setToolTipText(e.title);	// ��ǩ��ʾ��ʾ��
			}
		});

		currentBrowser.addOpenWindowListener(new OpenWindowListener() {
				//�ڵ�ǰҳ���д򿪵��������ҳ��
					public void open(WindowEvent e) {
						Browser newBrowser = new Browser(tabFolder, SWT.NONE);
						TabItem newTabItem = new TabItem(tabFolder, SWT.NONE);
						newTabItem.setControl(newBrowser);
						tabFolder.setSelection(newTabItem);
						// �´򿪵�ҳ���ǩ�ö�
						tabFolder.redraw();
						newBrowser.setUrl(newUrl);		// �±�ǩ�������µ����ӵ�ַ
						isOpenNewItem = true;			// �µ�ҳ�����µı�ǩ�д�
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