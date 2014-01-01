package com.easyfun.eclipse.performance.threaddump;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.part.ViewPart;

import com.easyfun.eclipse.component.file.FileFieldComposite;
import com.easyfun.eclipse.component.ftp.FTPFieldComposite;
import com.easyfun.eclipse.component.ftp.FTPHelper;
import com.easyfun.eclipse.component.ftp.FTPHostBean;
import com.easyfun.eclipse.component.kv.KeyValue;
import com.easyfun.eclipse.component.kv.KeyValueTableViewer;
import com.easyfun.eclipse.component.tree.TreeContentProvider;
import com.easyfun.eclipse.component.tree.TreeLabelProvider;
import com.easyfun.eclipse.component.tree.model.Directory;
import com.easyfun.eclipse.performance.threaddump.parser.FilterWrapper;
import com.easyfun.eclipse.performance.threaddump.parser.IThreadParser;
import com.easyfun.eclipse.performance.threaddump.parser.ParserType;
import com.easyfun.eclipse.performance.threaddump.parser.ThreadInfo;
import com.easyfun.eclipse.performance.threaddump.parser.ThreadParserFactory;
import com.easyfun.eclipse.rcp.ColumnViewerSorter;
import com.easyfun.eclipse.rcp.RCPUtil;
import com.easyfun.eclipse.util.IOUtil;

/**
 * @author linzhaoming
 *
 * 2011-5-8
 *
 */
public class ThreadDumpView extends ViewPart {
	/** ThreadDump内容 */
	private SourceViewer pureThreadDump;
	/** ODB前后台调用Table */
	private TableViewer obdCallTableViewer;
	/** 线程信息Table */
	private TableViewer threadInfoTableViewer;
	/** ThreadDump SyledText */
	private StyledText threadText;
	/** 总体概况 StyledText */
	private StyledText overViewText;

	private String keyword;

	/** 打开的ThreadDump文件或者FTP */
	private TreeViewer threadFileTreeViewer;
	private FileFieldComposite fileComposite;

	/** ThreadDump概括 */
	private Label threadDesc;

	private TabFolder threadTabFolder;
	private Combo parserCombo;

	public ThreadDumpView() {
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
		c1.setLayout(RCPUtil.getNoMarginLayout(2, false));
		c1.setLayoutData(new GridData());
		
		Composite c2 = new Composite(sup, SWT.NULL);
		c2.setLayout(RCPUtil.getNoMarginLayout());
		c2.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		sup.setWeights(new int[]{20,80});
	
		createLeftComposite(c1);

		threadTabFolder = new TabFolder(c2, SWT.NONE);
		threadTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		TabItem obdCallTabItem = new TabItem(threadTabFolder, SWT.NONE);
		obdCallTabItem.setText("前后台调用");
		obdCallTabItem.setControl(createOBDComposite(threadTabFolder));
		
		TabItem threadItem = new TabItem(threadTabFolder, SWT.NONE);
		threadItem.setText("线程概况");
		threadItem.setControl(createThreadInfoComposite(threadTabFolder));

		TabItem contentItem = new TabItem(threadTabFolder, SWT.NONE);
		contentItem.setText("ThreadDump");
		contentItem.setControl(createAllContentComposite(threadTabFolder));
	}
	
	/** 左侧Composite */
	private void createLeftComposite(Composite parent){
		Label label = new Label(parent, SWT.NULL);
		label.setText("ThreadDump类型：");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		
		parserCombo = new Combo(parent, SWT.READ_ONLY);
		parserCombo.setLayoutData(new GridData());
		parserCombo.setItems(new String[]{"MON", "BES8", "WebLogic", "SUN"});
		parserCombo.select(0);
		
		fileComposite = new FileFieldComposite(parent, SWT.NONE, false);
		fileComposite.setLayoutData(new GridData());
		fileComposite.addFileChangListener(new FileFieldComposite.IFilechangeListener(){
			public void onFileChange(File file) {
				updateUIByFile(file, getSelectParserType());
			}
		});

		final FTPFieldComposite ftpComposite = new FTPFieldComposite(parent, SWT.NONE, false);
		ftpComposite.setLayoutData(new GridData());
		ftpComposite.addFTPChangListener(new FTPFieldComposite.IFTPchangeListener(){
			public void onFTPChange(FTPHostBean ftpBean) {
				InputStream is = null;
				try {
					is = FTPHelper.getInputStream(ftpBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(is != null){
					updateByInputStream(is, getSelectParserType());
				}
			}
		});

		threadFileTreeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		threadFileTreeViewer.setLabelProvider(new TreeLabelProvider());
		threadFileTreeViewer.setContentProvider(new TreeContentProvider());
		
		threadFileTreeViewer.getTree().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2,1));
		threadFileTreeViewer.setInput(ThreadFileMem.getThreadFileInput());
		threadFileTreeViewer.addDoubleClickListener(new IDoubleClickListener(){
			//文件打开双击处理
			public void doubleClick(DoubleClickEvent event) {
				TreeSelection selection = (TreeSelection)event.getSelection();
				if(!(selection.getFirstElement() instanceof FileNode)){
					return;
				}
				FileNode fileNode = (FileNode)selection.getFirstElement();
				File file = fileNode.getFile();
				InputStream is = null;
				try {
					is = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					RCPUtil.showError(getShell(), "文件名不存在" + e.getMessage());
					return;
				}
				updateByInputStream(is, fileNode.getParserType());
			}
		});
		
		createTreePopMenus(threadFileTreeViewer);
	}
	
	/** 前后台调用TabItem*/
	private Control createOBDComposite(Composite parent){
		String[] colNames = new String[]{"后台接口名称", "调用次数"}; 
		obdCallTableViewer = new KeyValueTableViewer(parent, colNames);
		return obdCallTableViewer.getControl();
	}

	/** 线程概况TabItem*/
	private Control createThreadInfoComposite(Composite tabFolder){
		SashForm threadSashForm = new SashForm(tabFolder, SWT.VERTICAL);
		threadSashForm.setLayout(new GridLayout(1, false));
		threadSashForm.setLayoutData(new GridData());
		
		
		SashForm sashForm = new SashForm(threadSashForm, SWT.HORIZONTAL); 
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sashForm.setLayout(new GridLayout(2, false));		
		threadInfoTableViewer = new TableViewer(sashForm, SWT.FULL_SELECTION);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
		gridData.widthHint = 300;
		threadInfoTableViewer.getTable().setLayoutData(gridData);
		threadInfoTableViewer.getTable().setHeaderVisible(true);
		threadInfoTableViewer.getTable().setLinesVisible(true);
		threadInfoTableViewer.setContentProvider(new ArrayContentProvider());
		threadInfoTableViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			public void selectionChanged(SelectionChangedEvent event) {
				Object obj = ((IStructuredSelection)event.getSelection()).getFirstElement();
				if(obj instanceof ThreadInfo){
					threadText.setText(((ThreadInfo)obj).getDisplayText());
				}
			}
		});
		
		threadInfoTableViewer.setSorter(new ViewerSorter(){
			public int compare(Viewer viewer, Object e1, Object e2) {
				return super.compare(viewer, e1, e2);
			}
		});
		
		//过滤表格
		
		TableViewerColumn col1 = new TableViewerColumn(threadInfoTableViewer, SWT.NULL);
		col1.getColumn().setText("状态");
		col1.getColumn().setWidth(50);
		col1.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				ThreadInfo threadInfo = (ThreadInfo)element;
				IThreadParser parser = ThreadParserFactory.getThreadParser(threadInfo.getParserType());
				return parser.getStateText(threadInfo.getState());
			}
			
			public Image getImage(Object element) {
				ThreadInfo threadInfo = (ThreadInfo)element;
				IThreadParser parser = ThreadParserFactory.getThreadParser(threadInfo.getParserType());
				return parser.getStateImage(threadInfo.getState());
			}
		});
		
		new ColumnViewerSorter(threadInfoTableViewer, col1) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				ThreadInfo p1 = (ThreadInfo) e1;
				ThreadInfo p2 = (ThreadInfo) e2;
				
				return p1.getState().toString().compareTo(p2.getState().toString());
			}
		};
		
		TableViewerColumn col2 = new TableViewerColumn(threadInfoTableViewer, SWT.NULL);
		col2.getColumn().setText("Name");
		col2.getColumn().setWidth(300);
		col2.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				ThreadInfo keyValue = (ThreadInfo)element;
				return keyValue.getName();
			}
		});
		
		new ColumnViewerSorter(threadInfoTableViewer, col2) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				ThreadInfo p1 = (ThreadInfo) e1;
				ThreadInfo p2 = (ThreadInfo) e2;
				
				return p1.getName().compareTo(p2.getName());
			}
		};
		
		TableViewerColumn col3 = new TableViewerColumn(threadInfoTableViewer, SWT.NULL);
		col3.getColumn().setText("Method");
		col3.getColumn().setWidth(300);
		col3.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				ThreadInfo keyValue = (ThreadInfo)element;
				return keyValue.getMethod();
			}
		});
		
		new ColumnViewerSorter(threadInfoTableViewer, col3) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				ThreadInfo p1 = (ThreadInfo) e1;
				ThreadInfo p2 = (ThreadInfo) e2;
				
				return p1.getMethod().compareTo(p2.getMethod());
			}
		};
				
		threadText = new StyledText(sashForm, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		threadText.setLayoutData(new GridData(GridData.FILL_BOTH));
		sashForm.setWeights(new int[]{60,40});
		
		Composite composite = new Composite(threadSashForm, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(1, false));
		
		threadDesc = new Label(composite, SWT.NULL);
		threadDesc.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		threadSashForm.setWeights(new int[]{90, 10});
		
		//tooltip
		final Table table = threadInfoTableViewer.getTable();
		final ToolTip tooltip = new ToolTip(table.getShell(), SWT.NULL);
		threadInfoTableViewer.getTable().addMouseTrackListener(
				new MouseTrackAdapter() {
					public void mouseHover(MouseEvent event) {
						Point pt = new Point(event.x, event.y);
						int index = table.getTopIndex();
						while (index < table.getItemCount()) {
							TableItem item = table.getItem(index);
							for (int i = 0; i < 1; i++) {	//只有第一列才显示tooltip
								Rectangle rect = item.getBounds(i);
								if (rect.contains(pt)) {
									ThreadInfo thread = (ThreadInfo)item.getData();
									//default类型由于已经显示，不显示tooltip
									if(thread.getParserType() != ParserType.MON){
										tooltip.setText(thread.getState().toString());
										tooltip.setVisible(true);	
									}
								}
							}
							index++;
						}
					}
					
					public void mouseExit(MouseEvent e) {
						tooltip.setVisible(false);
					}
				});
		
		return threadSashForm;
	}
	
	/**
	 * 创建表格右键菜单
	 * 
	 * @param threadInfoTableViewer
	 */
	private void createPopupMenus(final TableViewer threadInfoTableViewer, ParserType parserType){
		//表格右键菜单 begin
		Menu menu = new Menu(threadInfoTableViewer.getTable());
		
		Menu filterMenu = new Menu(threadInfoTableViewer.getTable().getShell(), SWT.DROP_DOWN);
		MenuItem filterMenuItem = new MenuItem(menu, SWT.CASCADE);
		filterMenuItem.setMenu(filterMenu);
		filterMenuItem.setText("&Filters");
		
		IThreadParser parser = ThreadParserFactory.getThreadParser(parserType);
		HashMap<String, FilterWrapper> filterMap = parser.getAllFilterMap();
		for(String key: filterMap.keySet()){
			final FilterWrapper filterWrapper = filterMap.get(key);
			MenuItem menuItem = new MenuItem(filterMenu, SWT.PUSH);
			menuItem.setText(key);
			menuItem.setImage(filterWrapper.getImage());
			menuItem.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					threadInfoTableViewer.setFilters(filterWrapper.getFilters());
				}
			});
		}
		
		threadInfoTableViewer.getTable().setMenu(menu);
		
		//表格右键菜单 end
	}
	
	private void createTreePopMenus(final TreeViewer treeViewer){
		Menu menu = new Menu(treeViewer.getTree());
		
		MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("Open...");
		menuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				Object ele = ((IStructuredSelection)treeViewer.getSelection()).getFirstElement();
				if(ele instanceof Directory){
					ThreadFileItem item = (ThreadFileItem)((Directory)ele).getType();
					if(item.getType().equals(ThreadFileEnum.FILE)){
						fileComposite.openFileDialog();
					}else{
						RCPUtil.showMessage(getShell(), "非文件格式暂时还不支持");
					}
				}
			}
		});
		
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("Delete");
		menuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				Object ele = ((IStructuredSelection)treeViewer.getSelection()).getFirstElement();
				if(ele instanceof FileNode){
					ThreadFileMem.removeChild((FileNode<ThreadFileEnum>)ele);
					treeViewer.setInput(ThreadFileMem.getThreadFileInput());	//刷新
					updateByInputStream(null, ParserType.MON);
				}
			}
		});
		
		menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("Refresh");
		menuItem.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				Object obj = e.getSource();
				Object ele = ((IStructuredSelection)treeViewer.getSelection()).getFirstElement();
				System.out.println(obj + ", " + obj.getClass());
				if(ele instanceof FileNode){
					FileNode file = (FileNode)ele;					
					updateUIByFile(file.getFile(), file.getParserType());
				}
			}
		});

		
		treeViewer.getTree().setMenu(menu);
		
		//表格右键菜单 end
	}
	
	/** 文本TabItem*/
	private Control createAllContentComposite(Composite composite){
		Composite parent = new Composite(composite, SWT.NULL);
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		parent.setLayout(new GridLayout(2,false));
		final Text keywordText = new Text(parent, SWT.SINGLE | SWT.BORDER);
		keywordText.setToolTipText("按Enter键搜索ThreadDump");
		keywordText.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				if(e.keyCode == 13){	//Enter键盘
					startSearch(keywordText.getText());
				}
			}
		});
		keywordText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		    
		Button button = new Button(parent, SWT.PUSH);
		button.setText("Search");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				startSearch(keywordText.getText());
			}
		});
		
		
		CompositeRuler ruler = new CompositeRuler();
		pureThreadDump = new SourceViewer(parent, ruler, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		pureThreadDump.getControl().setLayoutData(gridData);
		LineNumberRulerColumn rulerColumn = new LineNumberRulerColumn();
		rulerColumn.setForeground(new Color(getDisplay(), new RGB(0, 0,255)));
		rulerColumn.setBackground(new Color(getDisplay(), new RGB(0, 255,255)));
		ruler.addDecorator(0, rulerColumn);
		pureThreadDump.setEditable(false);
		
		pureThreadDump.setDocument(new Document(""));
		
		Menu menu = new Menu(pureThreadDump.getTextWidget());
		MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText("导出为文件...");
		menuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				File file = RCPUtil.openSaveDialog(getShell(), new String[]{"*.txt", "*.*"}, "threadDump.txt");
				if(file == null){
					return;
				}
				try {
					IOUtil.copy(new ByteArrayInputStream(pureThreadDump.getDocument().get().getBytes()), new FileOutputStream(file));
				} catch (Exception e1) {
					e1.printStackTrace();
					RCPUtil.showError(getShell(), "文件保存错误");
				}
			}
		});
		pureThreadDump.getTextWidget().setMenu(menu);
		
		pureThreadDump.getTextWidget().addLineStyleListener(new LineStyleListener() {
			public void lineGetStyle(LineStyleEvent event) {
				if (keyword == null || keyword.length() == 0) {
					event.styles = new StyleRange[0];
					return;
				}

				String line = event.lineText;
				int cursor = -1;

				LinkedList list = new LinkedList();
				while ((cursor = line.indexOf(keyword, cursor + 1)) >= 0) {
					list.add(getHighlightStyle(getShell(), event.lineOffset + cursor, keyword.length()));
				}
				event.styles = (StyleRange[]) list.toArray(new StyleRange[list.size()]);
			}
		});

		return parent;
	}
	
	  private StyleRange getHighlightStyle(Shell shell, int startOffset, int length) {
		StyleRange styleRange = new StyleRange();
		styleRange.start = startOffset;
		styleRange.length = length;
		styleRange.background = shell.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
		return styleRange;
	}
	
	/** 开始搜索*/
	private void startSearch(String str){
		keyword = str;
		pureThreadDump.getTextWidget().redraw();
	}
	
	/** 打开文件处理*/
	private void updateUIByFile(File file, ParserType parserType){
		if(file == null){
			return;
		}
		
//		LogHelper.error("开始解析文件：" + file.getAbsolutePath());

		FileNode node = new FileNode(ThreadFileEnum.FILE, parserType, file, file.getPath());
		ThreadFileMem.addChild(node);
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			RCPUtil.showError(getShell(), "文件名不存在" + e.getMessage());
			return;
		}
		updateByInputStream(is, parserType);
		threadFileTreeViewer.setInput(ThreadFileMem.getThreadFileInput());
		threadFileTreeViewer.expandAll(); // TODO：只展开相关的节点，并select当前打开节点
		
		//打开文件后，设置Tab页默认为线程概况
		threadTabFolder.setSelection(1);

		//设置第一个为选中
		List<ThreadInfo> threadInfos = (List<ThreadInfo>)threadInfoTableViewer.getInput();
		if(threadInfos != null && threadInfos.size() >0){
			TableItem item = threadInfoTableViewer.getTable().getItem(0);
			threadInfoTableViewer.setSelection(new StructuredSelection(item.getData()), true);
		}
	}
	
	/** 根据InputStream更新UI信息*/
	private void updateByInputStream(InputStream is, ParserType parserType) {
		threadText.setText("");
		if(is == null){	//为空清除控件内容
			obdCallTableViewer.setInput(null);
			threadInfoTableViewer.setInput(null);
			Document doc = new Document("");
			pureThreadDump.setDocument(doc);
			if(overViewText != null){
				overViewText.setText("");
			}
			return;
		}
		StringWriter sw = new StringWriter();
		try {
			if(parserType == ParserType.MON){
				IOUtil.copy(is, sw, "UTF8");
			}else{
				IOUtil.copy(is, sw);	
			}
		} catch (IOException e) {
			RCPUtil.showError(getShell(), "读取内容错误" + e.getMessage());
			e.printStackTrace();
		}
		String content = sw.toString();
		
		IThreadParser threadParser = ThreadParserFactory.getThreadParser(parserType);
		
		threadInfoTableViewer.setFilters(new ViewerFilter[0]);
		
		//后台接口
		List<KeyValue> keyValues = null;
		keyValues = threadParser.getOBDCallKeyValues(new ByteArrayInputStream(content.getBytes()));
		obdCallTableViewer.setInput(keyValues);

		// 线程情况
		List<ThreadInfo> threadInfos = null;
		threadInfos = threadParser.getThreadInfos(new ByteArrayInputStream(content.getBytes()), parserType);
		threadInfoTableViewer.setInput(threadInfos);
		
		updateThreadUI(parserType);
		
		//ThreadDump内容
		Document doc = new Document(content);
		pureThreadDump.setDocument(doc);

		threadDesc.setText(threadParser.getThreadDesc(threadInfos));
	}
	
	/** 根据不同解析类型更改表格个列的宽度*/
	private void updateThreadUI(ParserType parserType){
		TableColumn[] cols = threadInfoTableViewer.getTable().getColumns();
		switch(parserType){
			case BES8:
				cols[0].setWidth(50);
				cols[1].setWidth(300);
				cols[2].setWidth(300);
				createPopupMenus(threadInfoTableViewer, parserType);
				break;
			case Weblogic:
				cols[0].setWidth(50);
				cols[1].setWidth(300);
				cols[2].setWidth(300);
				createPopupMenus(threadInfoTableViewer, parserType);
				break;
			case MON:
				cols[0].setWidth(100);
				cols[1].setWidth(300);
				cols[2].setWidth(300);
				createPopupMenus(threadInfoTableViewer, parserType);
				break;
			case SUN:
				cols[0].setWidth(50);
				cols[1].setWidth(300);
				cols[2].setWidth(300);
				createPopupMenus(threadInfoTableViewer, parserType);
				break;
		}
	}
	
	/** 获取当前选择的ParserType*/
	private ParserType getSelectParserType(){
		int index = parserCombo.getSelectionIndex();
		switch (index) {
		case 0:
			return ParserType.MON;
		case 1:
			return ParserType.BES8;
		case 2:
			return  ParserType.Weblogic;
		case 3:
			return ParserType.SUN;
		default:
			return ParserType.MON;
		}
	}

	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	private Shell getShell(){
		return getSite().getShell();
	}
	
	private Display getDisplay(){
		return getSite().getShell().getDisplay();
	}

}