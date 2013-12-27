package ftp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;
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
import org.eclipse.swt.widgets.Table;

import com.easyfun.eclipse.common.util.TimeUtil;

/**
 * 远程目录TableViewer
 * 
 * @author linzhaoming
 *
 * 2013-12-27
 *
 */
public class RemoteDirectoryBrowser extends TableViewer {
	
	private List<IRemoteTableListener> listeners = new ArrayList<IRemoteTableListener>();
	
	public RemoteDirectoryBrowser(Composite parent){
		super(parent, SWT.FULL_SELECTION|SWT.BORDER);
		init();
	}

	private void init() {
		Table tableRemote = this.getTable();
		tableRemote.setHeaderVisible(true);
		tableRemote.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		TableViewerColumn tcFileName = new TableViewerColumn(this, SWT.LEFT);
		tcFileName.getColumn().setText("文件名");
		
		TableViewerColumn tcFileSize = new TableViewerColumn(this, SWT.NULL);
		tcFileSize.getColumn().setText("文件大小");
		
		TableViewerColumn tcFileType = new TableViewerColumn(this, SWT.NULL);
		tcFileType.getColumn().setText("文件类型");

		TableViewerColumn tcFileTime = new TableViewerColumn(this, SWT.NULL);
		tcFileTime.getColumn().setText("最近修改");

		tcFileName.getColumn().setWidth(180);		
		tcFileType.getColumn().setWidth(90);
		tcFileSize.getColumn().setWidth(80);
		tcFileTime.getColumn().setWidth(140);
		
		setContentProvider(new RemoteContentProvider());
		setLabelProvider(new RemoteTableLabelProvider());
		
		this.getTable().addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event event) {
				IStructuredSelection selection = (IStructuredSelection) RemoteDirectoryBrowser.this.getSelection();
				FTPFile file = (FTPFile) selection.getFirstElement();
				if (file != null && file.isDirectory()) {
					for (IRemoteTableListener l : listeners) {
						l.remotePathChange(file.getName());
					}
				}
			}
		});
	}
	
	private static class RemoteTableLabelProvider implements ITableLabelProvider{
		public Image getColumnImage(Object element, int columnIndex) {
			FTPFile ftpFile = ((FTPFile) element);
			 if (columnIndex == 0){
				 if(ftpFile.isDirectory()){
					 return ImageDescriptor.createFromFile(null, ImageConstants.ICON_DIRECTORY).createImage();
				 }else if(ftpFile.isFile()){
					 return DirectoryUtils.getImageByFile(ftpFile);
				 }else{
					 return ImageDescriptor.createFromFile(null, ImageConstants.ICON_FILE).createImage();
				 }
			 }
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			FTPFile ftpFile = ((FTPFile) element);
			switch (columnIndex) {
			case 0:
				return ftpFile.getName();
			case 1:
				if(ftpFile.getType() == FTPFile.DIRECTORY_TYPE){
					return "";
				}else{
					return ftpFile.getSize() + "";
				}
			case 2:
				switch(ftpFile.getType()){
				case FTPFile.FILE_TYPE:
					return "File";
				case FTPFile.DIRECTORY_TYPE:
					return "文件夹";
				default:
					return "";
				}
			case 3:
				return TimeUtil.getDateDisplayTime(ftpFile.getTimestamp().getTime());
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
	
	private static class RemoteContentProvider implements IStructuredContentProvider{
		public Object[] getElements(Object inputElement) {
			FTPFile[] files = (FTPFile[]) inputElement;
			return files;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}
	
	public void addRemoteTableListener(IRemoteTableListener listener){
		listeners.add(listener);
	}
	
	public void removeRemoteTableListener(IRemoteTableListener listener){
		listeners.remove(listener);
	}
	
	public static interface IRemoteTableListener{
		public void remotePathChange(String path);
	}

}