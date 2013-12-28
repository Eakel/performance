package com.easyfun.eclipse.performance.trace.item.trace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.easyfun.eclipse.common.ui.tree.model.AbstractTreeModel;
import com.easyfun.eclipse.performance.trace.model.TraceTypeEnum;


/**
 * [文件、目录] Trace目录节点
 * 
 * @author linzhaoming
 *
 * 2013-4-12
 *
 * @param <T>
 */
public class TraceDirectory<T> extends AbstractTreeModel<T>{
	protected List<TraceNode> children = new ArrayList();
	
	public static final int SORT_TRACETIME = 0;
	public static final int SORT_FILESIZE = 1;
	public static final int SORT_FILETIME = 2;
	
	/** 是否已经初始化所有子节点*/
	protected boolean initAll = false;
	
	private TraceTypeEnum filterType;
	
	public TraceDirectory(T type){
		super(type);
	}
	
	public void setChildren(List children){
		this.children = children;
	}
	
	public void addChild(TraceNode node){
		this.children.add(node);
	}
	
	public List<TraceNode> getChildren() {
		List<TraceNode> retList = new ArrayList<TraceNode>();
		for (TraceNode traceNode : children) {
			if (traceNode.getAppTrace() != null && traceNode.getAppTrace().isVisible()) {
				retList.add(traceNode);
			}
		}
		return retList;
	}
	
	
	public List<TraceNode> getRealChildren() {
		return children;
	}

	public boolean isDirectory() {
		return true;
	}
	
	public void sortTrace(int type){
		switch (type) {
		case SORT_TRACETIME:	//Trace时间
			Collections.sort(children, new Comparator<TraceNode>(){
				public int compare(TraceNode o1, TraceNode o2) {
					int t1 = Integer.valueOf(o1.getAppTrace().getUseTime());
					int t2 = Integer.valueOf(o2.getAppTrace().getUseTime());
					return t2 - t1;
				}
			});
			break;
		case SORT_FILESIZE:	//文件大小
			Collections.sort(children, new Comparator<TraceNode>(){
				public int compare(TraceNode o1, TraceNode o2) {
					long i1 = o1.getAppTrace().getUiDesc().getFileSize();
					long i2 = o2.getAppTrace().getUiDesc().getFileSize();
					return (int)(i2 - i1);
				}
			});
			break;
		case SORT_FILETIME:	//文件修改时间
			Collections.sort(children, new Comparator<TraceNode>(){
				public int compare(TraceNode o1, TraceNode o2) {
					if(o1 instanceof FileTraceNode && o2 instanceof FileTraceNode){
						FileTraceNode node1 = (FileTraceNode)o1;
						FileTraceNode node2 = (FileTraceNode)o2;
						return (int)(node1.getNodeFile().lastModified() - node2.getNodeFile().lastModified());
					}
					//TODO: FTP
					return o1.getAppTrace().getCreateTime().compareTo(o2.getAppTrace().getCreateTime());
				}
			});
			break;
		default:
			break;
		}
	}

	public TraceTypeEnum getFilterType() {
		return filterType;
	}

	public void setFilterType(TraceTypeEnum filterType) {
		this.filterType = filterType;
	}
	
	public boolean isInitAll() {
		return initAll;
	}

	public void setInitAll(boolean initAll) {
		this.initAll = initAll;
	}
	
	
}
