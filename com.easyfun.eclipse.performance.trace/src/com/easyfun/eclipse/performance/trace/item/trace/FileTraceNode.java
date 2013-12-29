package com.easyfun.eclipse.performance.trace.item.trace;

import java.io.File;

import com.easyfun.eclipse.performance.trace.TraceUtil;
import com.easyfun.eclipse.performance.trace.item.TraceTreeEnum;
import com.easyfun.eclipse.performance.trace.item.TraceTreeMem;
import com.easyfun.eclipse.util.TimeUtil;
import com.easyfun.eclipse.util.resource.FileUtil;

/**
 * 文件 Trace树节点
 * 
 * @author linzhaoming
 * 
 *         2011-12-16
 * 
 * @param <T>
 */
public class FileTraceNode<T> extends TraceNode<T> {
	private File nodeFile;

	public FileTraceNode(T type, File nodeFile){
		super(type, nodeFile.getName());
		this.nodeFile = nodeFile;
	}

	public File getNodeFile() {
		return nodeFile;
	}

	public String getDisplayName() {
		String time = TimeUtil.getLongDisplayTime(nodeFile.lastModified());
		String cost = String.valueOf(TraceUtil.getCostByFileName(nodeFile.getName()));
		String display = "(" + cost + "ms) [" + time + "] (" + FileUtil.getDisplayFileSize(nodeFile.length()) + ") " + nodeFile.getName();
		return display;
	}
	
	/**
	 * 添加文件节点
	 * @param node
	 * @return true：添加成功，false：没有添加
	 */
	public boolean addFileChild() {
		boolean ret = false;
		FileTraceDirectory dir = TraceTreeMem.getFileDirectory();
		if (dir != null) {
			boolean exist = false; // 文件是否已经存在
			for (Object obj : dir.getRealChildren()) {
				TraceNode<TraceTreeEnum> element = (TraceNode<TraceTreeEnum>) obj;
				if (element.equals(this)) {
					exist = true;
					break;
				}
			}

			// TODO: 存在的时候，再刷新一遍
			if (exist == false) {
				dir.addChild(this);
				return true;
			} else {
				return false;
			}
		}
		ret = true;

		return ret;
	}
	
	/**
	 * 删除文件节点
	 * @param node
	 * @return true：添加成功，false：没有添加
	 */
	public boolean removeFileChild(){
		FileTraceDirectory ele = TraceTreeMem.getFileDirectory();
		boolean removed = false; // 文件是否已经存在
		for (Object obj : ele.getRealChildren()) {
			TraceNode<TraceTreeEnum> element = (TraceNode<TraceTreeEnum>) obj;
			if (element.equals(this)) {
				removed = ele.getRealChildren().remove(obj);
				break;
			}
		}
		return removed;
	}
}
