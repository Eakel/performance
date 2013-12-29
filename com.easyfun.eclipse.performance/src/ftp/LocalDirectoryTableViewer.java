package ftp;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.easyfun.eclipse.common.util.TimeUtil;
import com.easyfun.eclipse.common.util.ui.ColumnViewerSorter;

/**
 * 本地目录TableViewer
 * 
 * @author linzhaoming
 *
 * 2013-12-27
 *
 */
public class LocalDirectoryTableViewer extends TableViewer {
	
	private List<ILocalTableListener> listeners = new ArrayList<ILocalTableListener>();
	
	public LocalDirectoryTableViewer(Composite compositeLocalDir) {
		super(compositeLocalDir, SWT.FULL_SELECTION|SWT.BORDER);
		init();
	}

	private void init() {
		TableViewerColumn tcFileName = new TableViewerColumn(this, SWT.NULL);
		tcFileName.getColumn().setText("文件名");
		new ColumnViewerSorter(this, tcFileName) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				File file1 = (File) e1;
				File file2 = (File) e2;
				if(file1.isDirectory() && file2.isDirectory()){
					//都是目录，根据目录名字排序
					return file1.getName().compareTo(file2.getName());
				}else if(file1.isDirectory() && !file2.isDirectory()){
					//目录在前面
					return 1;
				}else if(file1.isDirectory() && file2.isDirectory()){
					//目录在前面
					return -1;
				}else if(!file1.isDirectory() && file2.isDirectory()){
					return 1;
				}else if(file1.isFile() && file2.isFile()){
					//都是文件，根据目录名字排序
					return file1.getName().compareTo(file2.getName());
				}else{
					return file1.getName().compareTo(file2.getName());
				}
			}
		};

		TableViewerColumn tcFileSize = new TableViewerColumn(this, SWT.NULL);
		tcFileSize.getColumn().setText("文件大小");
		new ColumnViewerSorter(this, tcFileSize) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				File file1 = (File) e1;
				File file2 = (File) e2;
				return (int)file1.length() - (int)file2.length();
			}
		};
		
		TableViewerColumn tcFileType = new TableViewerColumn(this, SWT.NULL);
		tcFileType.getColumn().setText("文件类型");
		new ColumnViewerSorter(this, tcFileType) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				File file1 = (File) e1;
				File file2 = (File) e2;
				if(file1.isDirectory() && file2.isDirectory()){
					//都是目录，根据目录名字排序
					return file1.getName().compareTo(file2.getName());
				}else if(file1.isDirectory() && !file2.isDirectory()){
					//目录在前面
					return 1;
				}else if(file1.isDirectory() && file2.isDirectory()){
					//目录在前面
					return -1;
				}else if(!file1.isDirectory() && file2.isDirectory()){
					return 1;
				}else if(!file1.isDirectory() && !file2.isDirectory()){
					//都是文件，根据目录名字排序
					return file1.getName().compareTo(file2.getName());
				}else{
					return file1.getName().compareTo(file2.getName());					
				}
			}
		};

		TableViewerColumn tcFileTime = new TableViewerColumn(this, SWT.NULL);
		tcFileTime.getColumn().setText("最近修改");
		new ColumnViewerSorter(this, tcFileTime) {
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				File file1 = (File) e1;
				File file2 = (File) e2;
				return (int)file1.lastModified() - (int)file2.lastModified();
			}
		};

		tcFileName.getColumn().setWidth(180);		
		tcFileType.getColumn().setWidth(90);
		tcFileSize.getColumn().setWidth(80);
		tcFileTime.getColumn().setWidth(140);
		
		getTable().setHeaderVisible(true);

		getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		setContentProvider(new LocalTableContentProvider());
		setLabelProvider(new LocalTableLabelProvider());
		
		this.getTable().addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				IStructuredSelection selection = (IStructuredSelection) LocalDirectoryTableViewer.this.getSelection();
				File file = (File) selection.getFirstElement();
				if (file != null && file.isDirectory()) {
					LocalDirectoryTableViewer.this.setInput(file);
					for (ILocalTableListener l : listeners) {
						l.localPathChange(file.getPath());
					}
				}
			}
		});
	}
	
	private static class LocalTableLabelProvider implements ITableLabelProvider{
		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 0) {
				if(element instanceof File){
					File file = (File)element;
					if(file.exists()){
						if(file.isDirectory()){
							return ImageDescriptor.createFromFile(null, ImageConstants.ICON_DIRECTORY).createImage();
						}else if(file.isFile()){
							return DirectoryUtils.getImageByFile(file);
						}
					}
				}
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return ((File) element).getName();
			case 1:
				if(((File) element).isDirectory()){
					return "";
				}else{
					return ((File) element).length() + "";
				}
			case 2:
			if(element instanceof File){
				File file = (File)element;
				if(file.exists()){
					if(file.isDirectory()){
						return "文件夹";
					}else if(file.isFile()){
						return "File";
					}
				}
			}
			case 3:
				return TimeUtil.getDateDisplayTime(new Date(((File) element).lastModified()));
			default:
				return "";
			}
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}
	}
	
	private static class LocalTableContentProvider implements IStructuredContentProvider{
		public Object[] getElements(Object inputElement) {
			File dir = (File) inputElement;
			File[] files = dir.listFiles();
			List<File> retList = new ArrayList<File>();
			for (File file : files) {
				if(!DirectoryUtils.isIgnore(file.getName())){
					retList.add(file);
				}
			}
			return retList.toArray();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}
	
	public void addLocalTableListener(ILocalTableListener listener){
		listeners.add(listener);
	}
	
	public void removeLocalTableListener(ILocalTableListener listener){
		listeners.remove(listener);
	}
	
	public static interface ILocalTableListener{
		public void localPathChange(String path);
	}

}