package com.easyfun.eclipse.performance.trace.item;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;

import com.easyfun.eclipse.common.navigator.console.LogHelper;
import com.easyfun.eclipse.performance.trace.item.trace.TraceDirectory;
import com.easyfun.eclipse.performance.trace.item.trace.TraceNode;
import com.easyfun.eclipse.performance.trace.model.AppTrace;
import com.easyfun.eclipse.performance.trace.model.TraceTypeEnum;

public class FilterSelectionListener extends SelectionAdapter {
	private TreeViewer treeViewer;

	public FilterSelectionListener(TreeViewer treeVierer) {
		this.treeViewer = treeVierer;
	}

	public void widgetSelected(SelectionEvent e) {
		try {
			MenuItem item = (MenuItem) e.getSource();
			if (item.getSelection() == false) {
				return;
			}

			TraceTypeEnum type = (TraceTypeEnum) item.getData();
			Object ele = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
			if (!(ele instanceof TraceDirectory)) {
				return;
			}
			
			TraceDirectory dir = (TraceDirectory) ele;
			FilterThread t = new FilterThread(treeViewer, dir, type);
			Display.getDefault().asyncExec(t);
		} catch (Exception e1) {
			e1.printStackTrace();
			LogHelper.error(e1);
		}
	}
	
	private static class FilterThread implements Runnable{
		
		private TraceDirectory dir;
		
		private TreeViewer treeViewer;
		
		private TraceTypeEnum type;
		
		public FilterThread(TreeViewer treeViewer, TraceDirectory dir, TraceTypeEnum type){
			this.treeViewer = treeViewer;
			this.dir = dir;
			this.type =type;
		}
		
		public void run() {
			// FTP先初始化所有Trace文件
			if(dir.getType().equals(TraceTreeEnum.DIR_FTP)){
				boolean isOK = false;
				try {
					isOK = TraceView.initFTPTrace(dir, treeViewer.getTree().getShell());
				} catch (Exception e) {
					LogHelper.error(e);
					return;
				}
				if(isOK == false){
					return;
				}
			}
			
			List<TraceNode> children = dir.getRealChildren();
			dir.setFilterType(type);

			if (type == null) { // 不排序
				for (TraceNode traceNode : children) {
					AppTrace appTrace = traceNode.getAppTrace();
					appTrace.setVisible(true);
				}
				treeViewer.refresh(dir);
				return;
			}

			switch (type) {
			case TYPE_DAO:
				for (TraceNode traceNode : children) {
					AppTrace appTrace = traceNode.getAppTrace();
					if (appTrace.getDaoCount() > 0) {
						appTrace.setVisible(true);
					} else {
						appTrace.setVisible(false);
					}
				}
				break;
			case TYPE_JDBC:
				for (TraceNode traceNode : children) {
					AppTrace appTrace = traceNode.getAppTrace();
					if (appTrace.getJdbcCount() > 0) {
						appTrace.setVisible(true);
					} else {
						appTrace.setVisible(false);
					}
				}
				break;
			case TYPE_MEM:
				for (TraceNode traceNode : children) {
					AppTrace appTrace = traceNode.getAppTrace();
					if (appTrace.getMemCount() > 0) {
						appTrace.setVisible(true);
					} else {
						appTrace.setVisible(false);
					}
				}
				break;
			case TYPE_CAU:
				for (TraceNode traceNode : children) {
					AppTrace appTrace = traceNode.getAppTrace();
					if (appTrace.getCauCount() > 0) {
						appTrace.setVisible(true);
					} else {
						appTrace.setVisible(false);
					}
				}
				break;
			case TYPE_SECMEM:
				for (TraceNode traceNode : children) {
					AppTrace appTrace = traceNode.getAppTrace();
					if (appTrace.getSecMemCount() > 0) {
						appTrace.setVisible(true);
					} else {
						appTrace.setVisible(false);
					}
				}
				break;
			case TYPE_BCC:
				for (TraceNode traceNode : children) {
					AppTrace appTrace = traceNode.getAppTrace();
					if (appTrace.getBccCount() > 0) {
						appTrace.setVisible(true);
					} else {
						appTrace.setVisible(false);
					}
				}
				break;
			case TYPE_HTTP:
				for (TraceNode traceNode : children) {
					AppTrace appTrace = traceNode.getAppTrace();
					if (appTrace.getHttpCount() > 0) {
						appTrace.setVisible(true);
					} else {
						appTrace.setVisible(false);
					}
				}
				break;
			case TYPE_WS:
				for (TraceNode traceNode : children) {
					AppTrace appTrace = traceNode.getAppTrace();
					if (appTrace.getWsCount() > 0) {
						appTrace.setVisible(true);
					} else {
						appTrace.setVisible(false);
					}
				}
				break;
			case TYPE_MDB:
				for (TraceNode traceNode : children) {
					AppTrace appTrace = traceNode.getAppTrace();
					if (appTrace.getMdbCount() > 0) {
						appTrace.setVisible(true);
					} else {
						appTrace.setVisible(false);
					}
				}
				break;
			default:
				break;
			}
			treeViewer.refresh(dir);
			
		}
	}
}