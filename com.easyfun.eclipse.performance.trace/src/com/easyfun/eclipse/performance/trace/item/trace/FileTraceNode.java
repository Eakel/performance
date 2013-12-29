package com.easyfun.eclipse.performance.trace.item.trace;

import java.io.File;

import com.easyfun.eclipse.performance.trace.TraceUtil;
import com.easyfun.eclipse.performance.trace.item.TraceTreeEnum;
import com.easyfun.eclipse.performance.trace.item.TraceTreeMem;
import com.easyfun.eclipse.util.TimeUtil;
import com.easyfun.eclipse.util.resource.FileUtil;

/**
 * �ļ� Trace���ڵ�
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
	 * ����ļ��ڵ�
	 * @param node
	 * @return true����ӳɹ���false��û�����
	 */
	public boolean addFileChild() {
		boolean ret = false;
		FileTraceDirectory dir = TraceTreeMem.getFileDirectory();
		if (dir != null) {
			boolean exist = false; // �ļ��Ƿ��Ѿ�����
			for (Object obj : dir.getRealChildren()) {
				TraceNode<TraceTreeEnum> element = (TraceNode<TraceTreeEnum>) obj;
				if (element.equals(this)) {
					exist = true;
					break;
				}
			}

			// TODO: ���ڵ�ʱ����ˢ��һ��
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
	 * ɾ���ļ��ڵ�
	 * @param node
	 * @return true����ӳɹ���false��û�����
	 */
	public boolean removeFileChild(){
		FileTraceDirectory ele = TraceTreeMem.getFileDirectory();
		boolean removed = false; // �ļ��Ƿ��Ѿ�����
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
