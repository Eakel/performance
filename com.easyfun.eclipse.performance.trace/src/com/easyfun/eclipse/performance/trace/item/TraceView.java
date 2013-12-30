package com.easyfun.eclipse.performance.trace.item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import com.easyfun.eclipse.component.file.DirectoryFieldComposite;
import com.easyfun.eclipse.component.file.FileFieldComposite;
import com.easyfun.eclipse.component.ftp.FTPBean;
import com.easyfun.eclipse.component.ftp.FTPFieldComposite;
import com.easyfun.eclipse.component.ftp.FTPHelper;
import com.easyfun.eclipse.performance.navigator.console.LogHelper;
import com.easyfun.eclipse.performance.trace.SFtpClient;
import com.easyfun.eclipse.performance.trace.builder.TraceBuilder;
import com.easyfun.eclipse.performance.trace.item.trace.DirTraceDirectory;
import com.easyfun.eclipse.performance.trace.item.trace.FileTraceNode;
import com.easyfun.eclipse.performance.trace.item.trace.SFtpTraceNode;
import com.easyfun.eclipse.performance.trace.item.trace.TraceDirectory;
import com.easyfun.eclipse.performance.trace.item.trace.TraceNode;
import com.easyfun.eclipse.performance.trace.model.AppTrace;
import com.easyfun.eclipse.performance.trace.model.TraceTypeEnum;
import com.easyfun.eclipse.performance.trace.ui.TraceDialog;
import com.easyfun.eclipse.performance.trace.ui.TraceFileTreeLabelProvider;
import com.easyfun.eclipse.performance.trace.ui.TraceTreeContentProvider;
import com.easyfun.eclipse.rcp.RCPUtil;
import com.easyfun.eclipse.util.IOUtil;
import com.easyfun.eclipse.util.StringUtil;
import com.trilead.ssh2.SFTPv3DirectoryEntry;

/**
 * Trace Composite
 * 
 * @author linzhaoming
 *
 * 2013-4-7
 *
 */
public class TraceView extends ViewPart {
	
	private TreeViewer traceFileTreeViewer;
	
	private FileFieldComposite fileComposite;

	private DirectoryFieldComposite dirComposite;

	private Label fileTimeLabel;
	
	private Label fileLengthLabel;

	private Label traceTimeLabel;

	private Browser summaryText;

	private Label fileNameLabel;

	private FTPFieldComposite ftpComposite;

	private Text searchText;

	private Label parentLabel;
	
	private static final Log log = LogFactory.getLog(TraceView.class);

	public TraceView() {
	}

	public void createPartControl(Composite parent) {
		parent.setLayout(RCPUtil.getNoMarginLayout());
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		init(parent);
	}
	
	private void init(Composite parent){
		SashForm sup = new SashForm(parent, SWT.HORIZONTAL|SWT.SMOOTH);
		sup.setLayout(RCPUtil.getNoMarginLayout(2, false));
		sup.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite c1 = new Composite(sup, SWT.NULL);
		c1.setLayout(RCPUtil.getNoMarginLayout(3, false));
		c1.setLayoutData(new GridData());
		
		Composite c2 = new Composite(sup, SWT.NULL);
		GridLayout layout = RCPUtil.getNoMarginLayout();
		layout.numColumns = 2;
		c2.setLayout(layout);
		c2.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		sup.setWeights(new int[]{30,70});
	
		try {
			createLeftComposite(c1);
		} catch (Exception e) {
			e.printStackTrace();
			RCPUtil.showError(getShell(), e.getMessage());
		}
		
		Label label = new Label(c2, SWT.NULL);
		label.setText("所属类型：");
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		label.setLayoutData(gridData);
		
		parentLabel = new Label(c2, SWT.BORDER);
		parentLabel.setText("");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		parentLabel.setLayoutData(gridData);
		
		label = new Label(c2, SWT.NULL);
		label.setText("文件名：");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		label.setLayoutData(gridData);
		
		fileNameLabel = new Label(c2, SWT.BORDER);
		fileNameLabel.setText("");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		fileNameLabel.setLayoutData(gridData);
		
		label = new Label(c2, SWT.NULL);
		label.setText("文件大小：");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		label.setLayoutData(gridData);
		
		fileLengthLabel = new Label(c2, SWT.BORDER);
		fileLengthLabel.setText("");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		fileLengthLabel.setLayoutData(gridData);
		
		label = new Label(c2, SWT.NULL);
		label.setText("文件修改时间：");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		label.setLayoutData(gridData);
		
		fileTimeLabel = new Label(c2, SWT.BORDER);
		fileTimeLabel.setText("");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		fileTimeLabel.setLayoutData(gridData);
		
		label = new Label(c2, SWT.NULL);
		label.setText("TRACE时间：");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		label.setLayoutData(gridData);
		
		traceTimeLabel = new Label(c2, SWT.BORDER);
		traceTimeLabel.setText("");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		traceTimeLabel.setLayoutData(gridData);
		
		label = new Label(c2, SWT.NULL);
		label.setText("概括：");
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		label.setLayoutData(gridData);
		
		summaryText = new Browser(c2, SWT.BORDER);
		summaryText.setText("");
		gridData = new GridData(GridData.FILL_BOTH);
		summaryText.setLayoutData(gridData);
		
	}
	
	/** 打开的文件 */
	private void createLeftComposite(Composite parent) throws Exception{
		fileComposite = new FileFieldComposite(parent, SWT.NONE, false);
		fileComposite.setLayoutData(new GridData());
		fileComposite.addFileChangListener(new FileFieldComposite.IFilechangeListener(){
			public void onFileChange(File file) {
				try {
					addFile(file);
				} catch (Exception e) {
					e.printStackTrace();
					RCPUtil.showError(getShell(), e.getMessage());
				}
			}
		});
		
		dirComposite = new DirectoryFieldComposite(parent, SWT.NONE, false);
		dirComposite.setDiagMsg("请选择包含Trace文件的文件夹");
		dirComposite.setLayoutData(new GridData());
		dirComposite.addFileChangListener(new DirectoryFieldComposite.IDiectorychangeListener(){
			public void onDirChange(File fileDir) {
				updateByDir(fileDir);
			}
		});

		ftpComposite = new FTPFieldComposite(parent, SWT.NONE, false);
		ftpComposite.setLayoutData(new GridData());
		ftpComposite.addFTPChangListener(new FTPFieldComposite.IFTPchangeListener(){
			public void onFTPChange(FTPBean ftpBean) {
				if(ftpBean != null){
					updateByFTP(ftpBean);
					TraceTreeMem.getFTPirectory().setFtpBean(ftpBean);
					traceFileTreeViewer.refresh(TraceTreeMem.getFTPirectory());
				}
			}
		});
		
		Composite c1 = new Composite(parent, SWT.NULL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		c1.setLayoutData(gridData);
		c1.setLayout(RCPUtil.getNoMarginLayout(2, false));
		
		Button searchButton = new Button(c1, SWT.NULL);
		searchButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		searchButton.setText("查 找");
		searchButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				handleSearch();
			}
		});
		
		searchText = new Text(c1, SWT.BORDER);		
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		searchText.setLayoutData(gridData);
		searchText.setText("");
		searchText.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.CR){
					handleSearch();
				}
			}
		});
		
		traceFileTreeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		traceFileTreeViewer.setLabelProvider(new TraceFileTreeLabelProvider());
		traceFileTreeViewer.setContentProvider(new TraceTreeContentProvider());
		
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 3;
		traceFileTreeViewer.getTree().setLayoutData(gridData);
		traceFileTreeViewer.setInput(TraceTreeMem.getTraceFileInput());
		traceFileTreeViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event) {
				TreeSelection selection = (TreeSelection)event.getSelection();
				if(!(selection.getFirstElement() instanceof TraceNode)){
					return;
				}
				TraceNode treeNode = (TraceNode)selection.getFirstElement();
				if(treeNode.getType() == TraceTreeEnum.NODE_FILE || treeNode.getType() == TraceTreeEnum.NODE_DIR){
					File file = ((FileTraceNode)treeNode).getNodeFile();
					try {
						InputStream is = new FileInputStream(file);
						if(treeNode.getAppTrace() == null){
							if(treeNode.getType() == TraceTreeEnum.NODE_FILE){
								AppTrace appTrace = TraceBuilder.parseTraceStream(is, file, "文件");
								treeNode.setAppTrace(appTrace);
							}else if(treeNode.getType() == TraceTreeEnum.NODE_DIR){
								AppTrace appTrace = TraceBuilder.parseTraceStream(is, file, "目录");
								treeNode.setAppTrace(appTrace);
							}
						}
						
						TraceDialog dia = new TraceDialog(getShell(), treeNode.getAppTrace(), file.getAbsolutePath());
						dia.open();
					} catch (Exception e) {
						e.printStackTrace();
						RCPUtil.showError(getShell(), "打开Trace文件出现错误:" + e.getMessage());
						return;
					}
				} else if(treeNode.getType() == TraceTreeEnum.NODE_FTP){
					try {
						if (treeNode.getAppTrace() == null) {
//							((SFtpTraceNode)treeNode).getFTPClient().intAppTraceByNode((SFtpTraceNode)treeNode);
//							intAppTraceByNode(treeNode);
							((SFtpTraceNode)treeNode).initAppTrace();
						}
						
						if (treeNode.getAppTrace() != null) {
							TraceDialog dia = new TraceDialog(getShell(), treeNode.getAppTrace(), "FTP");
							dia.open();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		traceFileTreeViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection)traceFileTreeViewer.getSelection();
				if (selection != null) {
					Object obj = selection.getFirstElement();
					if(obj instanceof TraceNode){
						TraceNode treeNode = (TraceNode)obj;
						if(treeNode.getType() == TraceTreeEnum.NODE_FILE || treeNode.getType() == TraceTreeEnum.NODE_DIR){
							File file = ((FileTraceNode)treeNode).getNodeFile();
							try {
								if(treeNode.getAppTrace() == null){
									if(treeNode.getType() == TraceTreeEnum.NODE_FILE){
										AppTrace appTrace = TraceBuilder.parseTraceStream(new FileInputStream(file), file, "文件");
										treeNode.setAppTrace(appTrace);
									}else if(treeNode.getType() == TraceTreeEnum.NODE_DIR){
										AppTrace appTrace = TraceBuilder.parseTraceStream(new FileInputStream(file), file, "目录");
										treeNode.setAppTrace(appTrace);
									}
								}
								updateUIByAppTrace(treeNode.getAppTrace());
							} catch (Exception e) {
								e.printStackTrace();
								RCPUtil.showError(getShell(), e.getMessage());
							}
						}else if(treeNode.getType() == TraceTreeEnum.NODE_FTP){
							try {
								if (treeNode.getAppTrace() == null) {
									((SFtpTraceNode)treeNode).initAppTrace();
									updateUIByAppTrace(treeNode.getAppTrace());
									traceFileTreeViewer.refresh(TraceTreeMem.getFTPirectory());
								} else {
									updateUIByAppTrace(treeNode.getAppTrace());
								}
							} catch (Exception e) {
								e.printStackTrace();
								RCPUtil.showError(getShell(), e.getMessage());
							}
						}
					}
				}
				
				createTreePopMenus(traceFileTreeViewer);
			}
		});
		
		traceFileTreeViewer.getTree().addMouseListener(new MouseAdapter(){
	        public void mouseDoubleClick(MouseEvent e) {
	        	IStructuredSelection select = (IStructuredSelection)traceFileTreeViewer.getSelection();
				Object obj = select.getFirstElement();
				if(obj instanceof TraceDirectory){
		            TreeItem item = traceFileTreeViewer.getTree().getItem(new Point(e.x, e.y));

		            if (item != null && item.getItemCount()>0 && item.getItem(0).getText().trim().length() == 0) {
		            	traceFileTreeViewer.expandToLevel(item.getData(), 1);
		                return;
		            }

		            if (item != null && item.getItemCount() > 0 && item.getItem(0).getText().trim().length() > 0) {
		                item.setExpanded(!item.getExpanded());
		            }			
				}
	        }
		});
	}
	
	private void createTreePopMenus(final TreeViewer treeViewer){
		IStructuredSelection selection = (IStructuredSelection)traceFileTreeViewer.getSelection();
		if (selection == null) {
			treeViewer.getTree().setMenu(null);
			return;
		}
		
		Object obj = selection.getFirstElement();
		Menu menu = new Menu(treeViewer.getTree());
		if(obj instanceof TraceDirectory){
			TraceDirectory<TraceTreeEnum> dir = (TraceDirectory<TraceTreeEnum>) obj;
			if (dir.getType().equals(TraceTreeEnum.DIR_FILE)) {
				MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
				menuItem.setText("文件...");
				menuItem.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						Object ele = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
						if (ele instanceof TraceDirectory) {
							TraceDirectory<TraceTreeEnum> dir = (TraceDirectory<TraceTreeEnum>) ele;
							if (dir.getType().equals(TraceTreeEnum.DIR_FILE)) {
								fileComposite.openFileDialog();
							} else {
								RCPUtil.showMessage(getShell(), "不支持非文件格式");
							}
						}
					}
				});
			} else if (dir.getType() == TraceTreeEnum.DIR_DIR) {
				MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
				menuItem.setText("目录...");
				menuItem.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						try {
							Object ele = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
							if (ele instanceof TraceDirectory) {
								TraceDirectory<TraceTreeEnum> dir = (TraceDirectory<TraceTreeEnum>) ele;
								if (dir.getType().equals(TraceTreeEnum.DIR_DIR)) {
									dirComposite.openDirDialog();
								} else {
									RCPUtil.showMessage(getShell(), "不支持非目录格式");
								}
							}
						} catch (Exception e1) {
							LogHelper.error(log, e1.getMessage());
						}
					}
				});
			} else if (dir.getType() == TraceTreeEnum.DIR_FTP) {
				MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
				menuItem.setText("FTP...");
				menuItem.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						Object ele = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
						if (ele instanceof TraceDirectory) {
							TraceDirectory<TraceTreeEnum> dir = (TraceDirectory<TraceTreeEnum>) ele;
							if (dir.getType().equals(TraceTreeEnum.DIR_FTP)) {
								ftpComposite.openFTPDialog();
							} else {
								RCPUtil.showMessage(getShell(), "不支持非目录格式");
							}
						}
					}
				});
				
				MenuItem disConnItem = new MenuItem(menu, SWT.PUSH);
				disConnItem.setText("断开FTP");
				disConnItem.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						Object ele = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
						if (ele instanceof TraceDirectory) {
							TraceDirectory<TraceTreeEnum> dir = (TraceDirectory<TraceTreeEnum>) ele;
							if (dir.getType().equals(TraceTreeEnum.DIR_FTP)) {
								ftpComposite.openFTPDialog();
							} else {
								RCPUtil.showMessage(getShell(), "不支持非目录格式");
							}
						}
					}
				});
			}

			MenuItem refreshMenuItem = new MenuItem(menu, SWT.PUSH);
			refreshMenuItem.setText("刷新");
			refreshMenuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					Object ele = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
					if (ele instanceof FileTraceNode) {
						FileTraceNode fileNode = (FileTraceNode) ele;
						try {
							addFile(fileNode.getNodeFile());
						} catch (Exception e1) {
							e1.printStackTrace();
							RCPUtil.showError(getShell(), e1.getMessage());
						}
					}else if(ele instanceof DirTraceDirectory){
						DirTraceDirectory dir = (DirTraceDirectory)ele;
						refreshDir(dir);
					}
				}
			});
			
			
			Menu sortMenu = new Menu(treeViewer.getTree().getShell(), SWT.DROP_DOWN);
			MenuItem sortMenuItem = new MenuItem(menu, SWT.CASCADE);
			sortMenuItem.setMenu(sortMenu);
			sortMenuItem.setText("排序");
			
			MenuItem traceCostMenuItem = new MenuItem(sortMenu, SWT.PUSH);
			traceCostMenuItem.setText("Trace时间");
			traceCostMenuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					Object ele = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
					if(ele instanceof TraceDirectory){
						TraceDirectory<TraceTreeEnum> dir = (TraceDirectory<TraceTreeEnum>)ele;
						dir.sortTrace(TraceDirectory.SORT_TRACETIME);
						traceFileTreeViewer.refresh(dir);
						traceFileTreeViewer.expandToLevel(dir, TreeViewer.ALL_LEVELS);
					}
				}
			});
			
			MenuItem traceTimeMenuItem = new MenuItem(sortMenu, SWT.PUSH);
			traceTimeMenuItem.setText("文件大小");
			traceTimeMenuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					Object ele = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
					if(ele instanceof TraceDirectory){
						TraceDirectory<TraceTreeEnum> dir = (TraceDirectory<TraceTreeEnum>)ele;
						dir.sortTrace(TraceDirectory.SORT_FILESIZE);
						traceFileTreeViewer.refresh(dir);
						traceFileTreeViewer.expandToLevel(dir, TreeViewer.ALL_LEVELS);
					}
				}
			});
			
			MenuItem traceFileSizeMenuItem = new MenuItem(sortMenu, SWT.PUSH);
			traceFileSizeMenuItem.setText("文件修改时间");
			traceFileSizeMenuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					Object ele = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
					if(ele instanceof TraceDirectory){
						TraceDirectory<TraceTreeEnum> dir = (TraceDirectory<TraceTreeEnum>)ele;
						dir.sortTrace(TraceDirectory.SORT_FILETIME);
						traceFileTreeViewer.refresh(dir);
						traceFileTreeViewer.expandToLevel(dir, TreeViewer.ALL_LEVELS);
					}
				}
			});
			
			
			Menu filterMenu = new Menu(treeViewer.getTree().getShell(), SWT.DROP_DOWN);
			final MenuItem filterMenuItem = new MenuItem(menu, SWT.CASCADE);
			filterMenuItem.setMenu(filterMenu);
			filterMenuItem.setText("过滤");
			filterMenuItem.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					System.out.println("S " + filterMenuItem.getSelection());
				}
			});
			
			
			FilterSelectionListener listener = new FilterSelectionListener(treeViewer);
			
			MenuItem noFilterIitem = new MenuItem(filterMenu, SWT.RADIO);
			noFilterIitem.setText("不过滤");
			noFilterIitem.setData(null);
			noFilterIitem.addSelectionListener(listener);
			
			MenuItem daoItem = new MenuItem(filterMenu, SWT.RADIO);
			daoItem.setText("dao");
			daoItem.setData(TraceTypeEnum.TYPE_DAO);
			daoItem.addSelectionListener(listener);
			
			MenuItem jdbcItem = new MenuItem(filterMenu, SWT.RADIO);
			jdbcItem.setText("jdbc");
			jdbcItem.setData(TraceTypeEnum.TYPE_JDBC);
			jdbcItem.addSelectionListener(listener);
			
			MenuItem cauItem = new MenuItem(filterMenu, SWT.RADIO);
			cauItem.setText("cau");
			cauItem.setData(TraceTypeEnum.TYPE_CAU);
			cauItem.addSelectionListener(listener);
			
			MenuItem memItem = new MenuItem(filterMenu, SWT.RADIO);
			memItem.setText("mem");
			memItem.setData(TraceTypeEnum.TYPE_MEM);
			memItem.addSelectionListener(listener);
			
			MenuItem secMemItem = new MenuItem(filterMenu, SWT.RADIO);
			secMemItem.setText("secmem");
			secMemItem.setData(TraceTypeEnum.TYPE_SECMEM);
			secMemItem.addSelectionListener(listener);
			
			MenuItem bccItem = new MenuItem(filterMenu, SWT.RADIO);
			bccItem.setText("bcc");
			bccItem.setData(TraceTypeEnum.TYPE_BCC);
			bccItem.addSelectionListener(listener);
			
			MenuItem httpItem = new MenuItem(filterMenu, SWT.RADIO);
			httpItem.setText("http");
			httpItem.setData(TraceTypeEnum.TYPE_HTTP);
			httpItem.addSelectionListener(listener);
			
			MenuItem wsItem = new MenuItem(filterMenu, SWT.RADIO);
			wsItem.setText("ws");
			wsItem.setData(TraceTypeEnum.TYPE_WS);
			wsItem.addSelectionListener(listener);
			
			MenuItem mdbItem = new MenuItem(filterMenu, SWT.RADIO);
			mdbItem.setText("mdb");
			mdbItem.setData(TraceTypeEnum.TYPE_MDB);
			mdbItem.addSelectionListener(listener);
			
			dir.getFilterType();
			if(dir.getFilterType() == null){
				noFilterIitem.setSelection(true);
			}else{
				switch (dir.getFilterType()) {
				case TYPE_DAO:
					daoItem.setSelection(true);
					break;
				case TYPE_JDBC:
					jdbcItem.setSelection(true);
					break;
				case TYPE_CAU:
					cauItem.setSelection(true);
					break;
				case TYPE_MEM:
					memItem.setSelection(true);
					break;
				case TYPE_SECMEM:
					secMemItem.setSelection(true);
					break;
				case TYPE_BCC:
					bccItem.setSelection(true);
					break;
				case TYPE_HTTP:
					httpItem.setSelection(true);
					break;
				case TYPE_WS:
					wsItem.setSelection(true);
					break;
				case TYPE_MDB:
					mdbItem.setSelection(true);
					break;
				default:
					break;
				}
			}
		}else if(obj instanceof TraceNode){
			if(((TraceNode) obj).getType() == TraceTreeEnum.NODE_FILE){
				MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
				menuItem.setText("移除");
				menuItem.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						Object ele = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
						if (ele instanceof FileTraceNode) {
							((FileTraceNode) ele).removeFileChild();
							treeViewer.setInput(TraceTreeMem.getTraceFileInput()); // 刷新
							try {
								updateUIByAppTrace(null);
							} catch (Exception e1) {
								e1.printStackTrace();
								RCPUtil.showError(getShell(), e1.getMessage());
							}
						}
					}
				});
			}
		}

		treeViewer.getTree().setMenu(menu);
	}


	/** 打开文件处理*/
	private void addFile(File file) throws Exception{
		if(file == null){
			return;
		}
		
		FileTraceNode node = new FileTraceNode(TraceTreeEnum.NODE_FILE, file);
		node.addFileChild();
		try {
			AppTrace appTrace = TraceBuilder.parseTraceStream(new FileInputStream(file), file, "文件");
			node.setAppTrace(appTrace);
			updateUIByAppTrace(appTrace);
		} catch (Exception e) {
			e.printStackTrace();
			RCPUtil.showError(getShell(), "Trace文件解析错误:\n" + e.getMessage());
			return;
		}
		
		traceFileTreeViewer.refresh(TraceTreeMem.getFileDirectory());
		traceFileTreeViewer.expandToLevel(TraceTreeMem.getFileDirectory(), TreeViewer.ALL_LEVELS);
	}
	
	/** 根据选择的目录更新*/
	private void updateByDir(File dirFile) {
		if (dirFile == null) {
			return;
		}

		try {
			DirTraceDirectory dir = TraceTreeMem.getDirDirectory();
			dir.setDirFile(dirFile);
			if(dir != null){
				refreshDir(dir);	
			}			
		} catch (Exception e) {
			e.printStackTrace();
			RCPUtil.showError(getShell(), e.getMessage());
		}
	}
	
	/** 根据AppTace更新右侧概要信息*/
	private void updateUIByAppTrace(AppTrace appTrace) throws Exception{
		if (appTrace == null) {
			parentLabel.setText("NULL");
			fileNameLabel.setText("NULL");
			fileLengthLabel.setText("NULL");
			fileTimeLabel.setText("NULL");
			traceTimeLabel.setText("NULL");
			return;
		}
		parentLabel.setText(appTrace.getUiDesc().getParentDesc());
		fileNameLabel.setText(appTrace.getUiDesc().getFileName());
		fileLengthLabel.setText(IOUtil.getDisplayFileSize(appTrace.getUiDesc().getFileSize()));
		fileTimeLabel.setText(appTrace.getUiDesc().getFileTime());
		traceTimeLabel.setText(appTrace.getUiDesc().getTraceTime());
		StringBuffer sb = new StringBuffer();
		sb.append("<b>").append(appTrace.getMsg()).append("</b>");
		sb.append("<table border='1' width='100%'>");
		sb.append(appTrace.getDisplay());
		sb.append("</table>");
		summaryText.setText(sb.toString());
	}
	
	/** 根据选择的FTPBean(FTP)更新*/
	private void updateByFTP(FTPBean bean) {
		try {
			if(bean.getFtpType() == FTPBean.TYPE_FTP){
				FTPHelper ftpClient = new FTPHelper(bean);
				ftpClient.connect();
				String [] strs = ftpClient.list();
				System.out.println(Arrays.asList(strs));
			}else if(bean.getFtpType() == FTPBean.TYPE_SFTP){
				SFtpClient sftpClient = new SFtpClient(bean);
				List<SFTPv3DirectoryEntry> list = sftpClient.list(bean.getRemotePath());
				
				TraceDirectory<TraceTreeEnum> dir = TraceTreeMem.getFTPirectory();
				
				if (dir != null) {
					dir.getChildren().clear();
					for (SFTPv3DirectoryEntry entry : list) {
						if(entry.filename.endsWith(".trc")){
							dir.addChild(new SFtpTraceNode(TraceTreeEnum.NODE_FTP, sftpClient, entry, bean));
						}
					}
				}
				
				traceFileTreeViewer.refresh(dir);
				traceFileTreeViewer.expandToLevel(dir, TreeViewer.ALL_LEVELS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			RCPUtil.showError(getShell(), e.getMessage());
		}
	}
	
	private void refreshDir(DirTraceDirectory dir) {
		if (dir == null) {
			return;
		}

		String[] suffix = new String[] { ".trc", ".xml" };
		File dirFile = dir.getDirFile();
		if (dirFile != null) {
			File[] childFiles = dirFile.listFiles((FilenameFilter) new SuffixFileFilter(suffix));
			dir.getRealChildren().clear();
			for (File file : childFiles) {
				TraceNode traceNode = new FileTraceNode(TraceTreeEnum.NODE_DIR, file);
				try {
					AppTrace appTrace = TraceBuilder.parseTraceStream(new FileInputStream(file), file, "目录");
					traceNode.setAppTrace(appTrace);
					dir.addChild(traceNode);
				} catch (Exception e) {
					e.printStackTrace();
					LogHelper.error(log, "文件[" + file.getPath() + "]trace解析异常", e);
				}

				// dir.sortTrace(TraceDirectory.SORT_FILETIME);
			}
			traceFileTreeViewer.refresh(dir);
			traceFileTreeViewer.expandToLevel(dir, TreeViewer.ALL_LEVELS);
		}
	}
	
	/** 查找关键字*/
	private void handleSearch(){
		try {
			String s = searchText.getText().trim();
			List<TraceDirectory<TraceTreeEnum>> dirs = TraceTreeMem.getTraceFileInput();
			for (TraceDirectory<TraceTreeEnum> dir : dirs) {
				List<TraceNode> children = dir.getRealChildren();
				
				if(dir.getType().equals(TraceTreeEnum.DIR_FTP)){
					boolean isOK = initFTPTrace(dir, getShell());
					if(isOK == false){
						return;
					}
				}
				
				dir.setFilterType(null);	//查找禁止过滤
				
				if (StringUtil.isEmpty(s)) {
					for (TraceNode traceNode : children) {
						AppTrace appTrace = traceNode.getAppTrace();
						appTrace.setVisible(true);
						appTrace.getUiDesc().setSearchText(null);
					}
				}else{
					for (TraceNode traceNode : children) {
						AppTrace appTrace = traceNode.getAppTrace();
						appTrace.getUiDesc().setSearchText(s);
						if(appTrace.getTraceContent().toUpperCase().contains(s.toUpperCase())){
							appTrace.setVisible(true);
						}else{
							appTrace.setVisible(false);	
						}					
					}
				}

				traceFileTreeViewer.refresh(dir);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/** 若Trace文件未初始化先初始化所有Trace文件*/
	public static boolean initFTPTrace(TraceDirectory<TraceTreeEnum> dir, Shell shell) throws Exception{
		if(dir.getType().equals(TraceTreeEnum.DIR_FTP) == false){
			return false;
		}
		List<TraceNode> children = dir.getRealChildren();
		// FTP先初始化所有Trace文件
		if (dir.isInitAll()==false && children.size() > 0) {
			boolean isOK = RCPUtil.showConfirm(shell, "执行该操作，需要先解析FTP目录下所有的Trace文件，请确认是否操作?");
			if(isOK == false){
				return false;
			}
			LogHelper.info(log, "[开始]初始化所有Trace文件: " + dir.getDisplayName());
			for (TraceNode traceNode : children) {
				if (traceNode.getAppTrace() == null) {
					LogHelper.info(log, "文件: " + traceNode.getDisplayName());
					((SFtpTraceNode)traceNode).initAppTrace();
				}
			}
			LogHelper.info(log, "[结束]初始化所有Trace文件: " + dir.getDisplayName());
			dir.setInitAll(true);
		}
		
		return true;
	}

	
	@Override
	public void setFocus() {
		
	}

	private Shell getShell(){
		return getSite().getShell();
	}

}