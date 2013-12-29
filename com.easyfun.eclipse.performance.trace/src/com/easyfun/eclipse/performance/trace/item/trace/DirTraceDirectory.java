package com.easyfun.eclipse.performance.trace.item.trace;

import java.io.File;

import com.easyfun.eclipse.common.util.lang.StringUtil;

/**
 * [Ŀ¼] TraceĿ¼�ڵ�
 * 
 * @author linzhaoming
 * 
 *         2013-4-12
 * 
 * @param <T>
 */
public class DirTraceDirectory<T> extends TraceDirectory<T> {

	private File dirFile = null;

	public DirTraceDirectory(T type) {
		super(type);
	}

	public String getDisplayName() {
		if (dirFile != null) {
			String path = dirFile.getAbsolutePath();
			if (StringUtil.isNotEmpty(path)) {
				return "Ŀ¼ " + path + " (" + getChildren().size() + ")";
			} else {
				return "Ŀ¼";
			}
		} else {
			return "Ŀ¼";
		}
	}

	public File getDirFile() {
		return dirFile;
	}

	public void setDirFile(File file) {
		this.dirFile = file;
	}

}
