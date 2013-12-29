package ftp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.easyfun.eclipse.component.ftp.FTPBean;
import com.easyfun.eclipse.util.ui.SWTUtil;

public class FTPWindow extends ApplicationWindow implements LocalDirectoryTableViewer.ILocalTableListener, RemoteDirectoryBrowser.IRemoteTableListener{
	/** 左侧目录表格 */
	private LocalDirectoryTableViewer localDirTableViewer;

	/** 右侧目录表格 */
	private RemoteDirectoryBrowser remoteDirTableViewer;

	private Text localPathText;

	private Text remotePathText;

	private StyledText logStyledText;

	private FTPConnectionDialog connectionDialog;

	/** 左侧表格Action(UPDir) */
	private Action localUpDirAction;

	private Action remoteUpDirAction;

	/** 本地目录浏览Action */
	private Action localBrowseDirAction;

	/** Connect Action*/
	private Action connectAction;

	private Action disconnectAction;

	private Action abboutAction;

	private Action exitAction;

	private FTPClient ftpClient;

	private FTPBean ftpBean;

	public FTPWindow(Shell parentShell) {
		super(parentShell);

		createActions();

		addStatusLine();
		addToolBar(SWT.FLAT);
		addMenuBar();

		ftpClient = new FTPClient();
		ftpClient.addProtocolCommandListener(new ProtocolCommandListener() {
			public void protocolCommandSent(ProtocolCommandEvent e) {
				logMessage("> " + e.getCommand(), false);
			}

			public void protocolReplyReceived(ProtocolCommandEvent e) {
				logMessage("< " + e.getMessage(), false);
			}
		});

	}

	private void createActions() {
		// Up(Local) Button
		localUpDirAction = new Action() {
			public void run() {
				if (localDirTableViewer.getInput() == null) {
					return;
				}
				File dir = ((File) localDirTableViewer.getInput()).getParentFile();
				if (dir != null) {
					localDirTableViewer.setInput(dir);
					localPathText.setText(dir.getPath());
				}
			}
		};
		localUpDirAction.setText("Up(Local)");
		localUpDirAction.setToolTipText("Up one level - local dir");
		localUpDirAction.setImageDescriptor(ImageDescriptor.createFromFile(null, ImageConstants.ICON_FTP_LOCAL_UP));

		// "Browse..." Button
		localBrowseDirAction = new Action() {
			public void run() {
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				String path = dialog.open();
				if (path == null) {
					return;
				}
				File file = new File(path);
				localDirTableViewer.setInput(file);
				localPathText.setText(file.getPath());
			}
		};
		localBrowseDirAction.setText("Browse...");
		localBrowseDirAction.setToolTipText("Browse local directory");
		localBrowseDirAction.setImageDescriptor(ImageDescriptor.createFromFile(null, ImageConstants.ICON_FTP_BROWSE));

		// "Connect" Button
		connectAction = new Action() {
			public void run() {
				if (connectionDialog == null) {
					connectionDialog = new FTPConnectionDialog(FTPWindow.this);
				}
				if (connectionDialog.open() == Dialog.OK) {
					ftpBean = connectionDialog.getFTPBean();
					if (ftpBean == null) {
						logError("Failed to get connection information.");
					} else {
						// connects to remote host.
						logMessage("Connecting to " + ftpBean.getHost(), true);
						try {
							ftpClient.connect(ftpBean.getHost(), ftpBean.getPort());
							if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
								throw new RuntimeException("FTP server refused connection.");
							}
							logMessage("Connected to " + ftpBean.getHost(), true);
						} catch (Exception e) {
							logError(e.toString());
							return;
						}
						try {
							// logins in.
							if (ftpClient.login(ftpBean.getUserName(), ftpBean.getPasswd())) {
								logMessage("Logged in as user: " + ftpBean.getUserName(), true);
							}
							// gets current working directory.
							remotePathText.setText(ftpClient.printWorkingDirectory());

							// Lists files.
							FTPFile[] files = ftpClient.listFiles();
							remoteDirTableViewer.setInput(files);
						} catch (IOException e1) {
							logError(e1.getMessage());
							try {
								ftpClient.disconnect();
							} catch (IOException e2) {
							}
						}
					}
				}
			}
		};
		connectAction.setText("Connect");
		connectAction.setToolTipText("Connect to remote host");
		connectAction.setImageDescriptor(ImageDescriptor.createFromFile(null, ImageConstants.ICON_FTP_CONNECT));

		// "Disconnect" Button
		disconnectAction = new Action() {
			public void run() {
				try {
					ftpClient.logout();
					ftpClient.disconnect();
				} catch (Exception e) {
					logError(e.toString());
				}
			}
		};
		disconnectAction.setText("Disconnect");
		disconnectAction.setToolTipText("Disconnect from remote host");
		disconnectAction.setImageDescriptor(ImageDescriptor.createFromFile(null, ImageConstants.ICON_FTP_DISCONNECT));

		// "up(Remote)" Button
		remoteUpDirAction = new Action() {
			public void run() {
				try {
					if (ftpClient.changeToParentDirectory()) {
						remotePathText.setText(ftpClient.printWorkingDirectory());
						FTPFile[] files = ftpClient.listFiles();
						remoteDirTableViewer.setInput(files);
					}
				} catch (Exception e) {
					logError("FTP操作出错，出错信息为：" + e.getMessage(), e);
				}
			}
		};
		remoteUpDirAction.setText("Up(Remote)");
		remoteUpDirAction.setToolTipText("Up one level - remote dir");
		remoteUpDirAction.setImageDescriptor(ImageDescriptor.createFromFile(null, ImageConstants.ICON_FTP_SERVER_UP));

		//"About" Button
		abboutAction = new Action() {
			public void run() {
				SWTUtil.showMessage(getShell(), "FTP Client v1.0\nAll right reserved by Linzhaoming.");
			}
		};
		abboutAction.setText("About");
		abboutAction.setImageDescriptor(ImageDescriptor.createFromFile(null, ImageConstants.ICON_FTP_ABOUT));

		//"Exit" Button
		exitAction = new Action() {
			public void run() {
				if (!SWTUtil.showQuestion(getShell(), "Are you sure you want to exit?"))
					return;
				try {
					ftpClient.disconnect();
				} catch (Exception e) {
				}
				close();
			}
		};
		exitAction.setText("Exit");
		exitAction.setImageDescriptor(ImageDescriptor.createFromFile(null, ImageConstants.ICON_FTP_CLOSE));
	}

	private void dragNDropSupport() {
		// --- Drag source ---

		// Allows text to be moved only.
		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		final DragSource dragSource = new DragSource(remoteDirTableViewer.getControl(), operations);

		// Data should be transfered in plain text format.
		Transfer[] formats = new Transfer[] { TextTransfer.getInstance() };
		dragSource.setTransfer(formats);

		dragSource.addDragListener(new DragSourceListener() {
			public void dragStart(DragSourceEvent event) {
				System.out.println("DND starts");
				// disallows DND if no remote file is selected.
				IStructuredSelection selection = (IStructuredSelection) remoteDirTableViewer.getSelection();
				FTPFile file = (FTPFile) selection.getFirstElement();
				if (file == null || file.isDirectory()) {
					event.doit = false;
				}
			}

			public void dragSetData(DragSourceEvent event) {
				// Provides the text data.
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					IStructuredSelection selection = (IStructuredSelection) remoteDirTableViewer.getSelection();
					FTPFile file = (FTPFile) selection.getFirstElement();
					if (file == null || file.isDirectory()) {
						event.doit = false;
					} else {
						event.data = file.getName();
					}
				}
			}

			public void dragFinished(DragSourceEvent event) {
			}
		});

		remoteDirTableViewer.getControl().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				dragSource.dispose();
			}
		});

		// --- Drop target ---
		final DropTarget dropTarget = new DropTarget(localDirTableViewer.getControl(), operations);

		dropTarget.setTransfer(formats);

		dropTarget.addDropListener(new DropTargetListener() {
			public void dragEnter(DropTargetEvent event) {
			}

			public void dragLeave(DropTargetEvent event) {
			}

			public void dragOperationChanged(DropTargetEvent event) {
			}

			public void dragOver(DropTargetEvent event) {
			}

			public void drop(DropTargetEvent event) {
				if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
					String text = (String) event.data;
					File target = new File((File) localDirTableViewer.getInput(), text);
					if (target.exists()) {
						if (!SWTUtil.showQuestion(getShell(), "Overwrite file " + target + "?")) {
							return;
						}
					}

					try {
						FileOutputStream stream = new FileOutputStream(target);
						if (ftpClient.retrieveFile(text, stream)) {
							logMessage("File retrieved successfully.", true);
							// refreshes the file list.
							localDirTableViewer.refresh();
						} else {
							logError("Failed to retrieve file: " + text);
						}

						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			public void dropAccept(DropTargetEvent event) {
			}
		});

		localDirTableViewer.getControl().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				dropTarget.dispose();
			}
		});
	}

	protected MenuManager createMenuManager() {
		MenuManager bar = new MenuManager();

		MenuManager menuFile = new MenuManager("&File");
		menuFile.add(connectAction);
		menuFile.add(disconnectAction);
		menuFile.add(new Separator());
		menuFile.add(exitAction);

		MenuManager menuLocal = new MenuManager("&Local");
		menuLocal.add(localBrowseDirAction);
		menuLocal.add(localUpDirAction);

		MenuManager menuRemote = new MenuManager("&Remote");
		menuRemote.add(remoteUpDirAction);

		MenuManager menuHelp = new MenuManager("&Help");
		menuHelp.add(abboutAction);

		bar.add(menuFile);
		bar.add(menuLocal);
		bar.add(menuRemote);
		bar.add(menuHelp);
		bar.updateAll(true);

		return bar;
	}

	public static void addAction(ToolBarManager manager, Action action, boolean displayText) {
		if (!displayText) {
			manager.add(action);
			return;
		} else {
			ActionContributionItem item = new ActionContributionItem(action);
			item.setMode(ActionContributionItem.MODE_FORCE_TEXT);
			manager.add(item);
		}
	}

	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager manager = super.createToolBarManager(style);

		addAction(manager, connectAction, true);
		addAction(manager, disconnectAction, true);

		manager.add(new Separator());

		addAction(manager, localBrowseDirAction, true);
		addAction(manager, localUpDirAction, true);

		manager.add(new Separator());

		addAction(manager, remoteUpDirAction, true);

		manager.add(new Separator());

		addAction(manager, abboutAction, true);
		addAction(manager, exitAction, true);

		manager.update(true);

		return manager;
	}
	
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new FillLayout());

		// the vertical sashform.
		SashForm verticalForm = new SashForm(composite, SWT.VERTICAL);

		// the horizontal sashform.
		SashForm horizontalForm = new SashForm(verticalForm, SWT.HORIZONTAL);

		// Local dir browser.
		Composite compositeLocalDir = new Composite(horizontalForm, SWT.NULL);
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 1;
		gridLayout.verticalSpacing = 1;
		compositeLocalDir.setLayout(gridLayout);

		Group compositeLocalDirTop = new Group(compositeLocalDir, SWT.NULL);
		compositeLocalDirTop.setText("本地站点");
		GridLayout gridLayout2 = new GridLayout(4, false);
		gridLayout2.marginHeight = 0;
		compositeLocalDirTop.setLayout(gridLayout2);
		compositeLocalDirTop.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label label = new Label(compositeLocalDirTop, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		label.setText("Path: ");

		localPathText = new Text(compositeLocalDirTop, SWT.BORDER);
		localPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		localPathText.setText("");
		localPathText.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13){	//Enter键盘
					initLocalTable(localPathText.getText().trim());
				}
			}
		});
		
		localPathText.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent e) {
				localPathText.selectAll();
			}
		});

		Button button = new Button(compositeLocalDirTop, SWT.PUSH);
		button.setText(localUpDirAction.getText());
		button.setImage(localUpDirAction.getImageDescriptor().createImage());
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				localUpDirAction.run();
			}
		});

		Button buttonBrowseLocalDir = new Button(compositeLocalDirTop, SWT.PUSH);
		buttonBrowseLocalDir.setText(localBrowseDirAction.getText());
		buttonBrowseLocalDir.setImage(localBrowseDirAction.getImageDescriptor().createImage());
		buttonBrowseLocalDir.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				localBrowseDirAction.run();
			}
		});

		localDirTableViewer = new LocalDirectoryTableViewer(compositeLocalDir);
		localDirTableViewer.addLocalTableListener(this);

		Menu parenMenu = new Menu(localDirTableViewer.getTable());
		MenuItem menuItem = new MenuItem(parenMenu, SWT.CASCADE);
		menuItem.setText("上传");
		menuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) localDirTableViewer.getSelection();
				File file = (File) selection.getFirstElement();
				try {
					InputStream in = new FileInputStream(file);
					//上传文件  
					ftpClient.storeFile(file.getName(),in);  
					in.close();
					
					remoteDirTableViewer.refresh();	//TODO:刷新
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
			}
		});
		
		menuItem = new MenuItem(parenMenu, SWT.CASCADE);
		menuItem.setText("&打开");
		menuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) localDirTableViewer.getSelection();
				File file = (File) selection.getFirstElement();
				if (file != null && file.exists()) {
					try {
						Runtime.getRuntime().exec("explorer " + file.getPath());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		localDirTableViewer.getTable().setMenu(parenMenu);

		// Remote directory browser.
		Composite compositeRemoteDir = new Composite(horizontalForm, SWT.NULL);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 1;
		gridLayout.verticalSpacing = 1;
		compositeRemoteDir.setLayout(gridLayout);

		Group compositeRemoteDirTop = new Group(compositeRemoteDir, SWT.NULL);
		compositeRemoteDirTop.setText("远程站点");
		gridLayout2 = new GridLayout(3, false);
		gridLayout2.marginHeight = 0;
		compositeRemoteDirTop.setLayout(gridLayout2);
		compositeRemoteDirTop.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		label = new Label(compositeRemoteDirTop, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		label.setText("Path: ");

		remotePathText = new Text(compositeRemoteDirTop, SWT.BORDER);
		remotePathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		remotePathText.setText("");
		
		remotePathText.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13){	//Enter键盘
					initRemoteTable(remotePathText.getText().trim());
				}
			}
		});
		
		remotePathText.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent e) {
				remotePathText.selectAll();
			}
		});

		Button buttonUpRemoteDir = new Button(compositeRemoteDirTop, SWT.PUSH);
		buttonUpRemoteDir.setText(remoteUpDirAction.getText());
		buttonUpRemoteDir.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				remoteUpDirAction.run();
			}
		});
		remoteDirTableViewer = new RemoteDirectoryBrowser(compositeRemoteDir);
		remoteDirTableViewer.addRemoteTableListener(this);
		
		Menu remoteParenMenu = new Menu(localDirTableViewer.getTable());
		MenuItem remoteMenuItem = new MenuItem(remoteParenMenu, SWT.CASCADE);
		remoteMenuItem.setText("下载");
		remoteMenuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) remoteDirTableViewer.getSelection();
				FTPFile ftpFile = (FTPFile) selection.getFirstElement();
				try {
					String toFileName = FilenameUtils.concat(((File)localDirTableViewer.getInput()).getPath(), ftpFile.getName());
					OutputStream out = new FileOutputStream(toFileName);
					//下载文件  
					ftpClient.retrieveFile(ftpFile.getName(),out);  
					out.close();
					
					localDirTableViewer.refresh();	//TODO:刷新
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
			}
		});
		
		remoteMenuItem = new MenuItem(remoteParenMenu, SWT.CASCADE);
		remoteMenuItem.setText("刷新");
		remoteMenuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {	
				localDirTableViewer.refresh();
			}
		});
		
		remoteDirTableViewer.getTable().setMenu(remoteParenMenu);

		// the log box.
		logStyledText = new StyledText(verticalForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY);
		
		Menu popMenu = new Menu(logStyledText.getShell(), SWT.POP_UP);
		MenuItem copyItem = new MenuItem(popMenu, SWT.PUSH);
		copyItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				final Clipboard cb = new Clipboard(logStyledText.getShell().getDisplay()); 
				String rtfData = "{\\rtf1 \\b\\i " + logStyledText.getText() + "}"; 
				TextTransfer textTransfer = TextTransfer.getInstance(); 
				RTFTransfer rtfTransfer = RTFTransfer.getInstance(); 
				Transfer[] types = new Transfer[] { textTransfer, rtfTransfer }; 
				cb.setContents(new Object[] { logStyledText.getText(),rtfData}, types); 
			}
		});
		copyItem.setText("复制到剪切板");
		
		MenuItem clearItem = new MenuItem(popMenu, SWT.PUSH);
		clearItem.setText("全部清除");
		clearItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				logStyledText.setText("");
			}
		});
		
		logStyledText.setMenu(popMenu);

		// resize sashform children.
		verticalForm.setWeights(new int[] { 4, 1 });

		// adding drag and drop support.
		dragNDropSupport();

		getToolBarControl().setBackground(new Color(getShell().getDisplay(), 230, 230, 230));

		getShell().setImage(new Image(getShell().getDisplay(), ImageConstants.ICON_FTP));
		getShell().setText("FTP Client v1.0");
		
		File rootFile = File.listRoots()[1];
		
		initLocalTable(rootFile);

		return composite;
	}
	
	private void initLocalTable(String path){
		File rootFile = new File(path);
		initLocalTable(rootFile);
	}
	
	private void initLocalTable(File rootFile){
		if(!rootFile.exists()){
			SWTUtil.showError(getShell(), "\"" + rootFile.getPath() + "\"" + "不存在或者无法访问");
			return;
		}
		
		if(!rootFile.isDirectory()){
			SWTUtil.showError(getShell(), "\"" + rootFile.getPath() + "\"" + "不是目录");
			return;
		}
		localDirTableViewer.setInput(rootFile);
		localPathText.setText(rootFile.getPath());
	}
	
	private void initRemoteTable(String path){
		try {
			if (ftpClient.changeWorkingDirectory(path)) {
				remotePathText.setText(ftpClient.printWorkingDirectory());
				FTPFile[] files = ftpClient.listFiles();
				remoteDirTableViewer.setInput(files);
			}
		} catch (Exception e) {
			logError("FTP操作出错，出错信息为：" + e.getMessage(), e);
		}
	}

	private void logMessage(String message, boolean showInStatusBar) {
		StyleRange styleRange1 = new StyleRange();
		styleRange1.start = logStyledText.getCharCount();
		styleRange1.length = message.length();
		styleRange1.foreground = getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN);
		styleRange1.fontStyle = SWT.NORMAL;

		logStyledText.append(message + "\r\n");
		logStyledText.setStyleRange(styleRange1);
		logStyledText.setSelection(logStyledText.getCharCount());

		if (showInStatusBar) {
			setStatus(message);
		}
	}
	
	private void logError(String message, Throwable t) {
		t.printStackTrace();
		
		StyleRange styleRange1 = new StyleRange();
		styleRange1.start = logStyledText.getCharCount();
		styleRange1.length = message.length();
		styleRange1.foreground = getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_RED);
		styleRange1.fontStyle = SWT.NORMAL;

		logStyledText.append(message + "\r\n");
		logStyledText.setStyleRange(styleRange1);
		logStyledText.setSelection(logStyledText.getCharCount());
	}

	private void logError(String message) {
		StyleRange styleRange1 = new StyleRange();
		styleRange1.start = logStyledText.getCharCount();
		styleRange1.length = message.length();
		styleRange1.foreground = getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_RED);
		styleRange1.fontStyle = SWT.NORMAL;

		logStyledText.append(message + "\r\n");
		logStyledText.setStyleRange(styleRange1);
		logStyledText.setSelection(logStyledText.getCharCount());
	}
	
	public void localPathChange(String path) {
		localPathText.setText(path);	
	}

	public void remotePathChange(String path) {
		try {
			ftpClient.changeWorkingDirectory(path);
			remotePathText.setText(ftpClient.printWorkingDirectory());
			remoteDirTableViewer.setInput(ftpClient.listFiles());
		} catch (IOException e) {
			logError(e.toString());
		}
	}

	public static void main(String[] args) {
		ApplicationWindow window = new FTPWindow(null);
		window.setBlockOnOpen(true);

		window.open();
		Display.getCurrent().dispose();
	}
}