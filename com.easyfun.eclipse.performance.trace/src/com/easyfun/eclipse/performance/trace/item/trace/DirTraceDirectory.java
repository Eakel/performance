package com.easyfun.eclipse.performance.trace.item.trace;

import java.io.File;

import com.easyfun.eclipse.performance.trace.item.TraceTreeEnum;
import com.easyfun.eclipse.util.StringUtil;

/**
 * [目录] Trace目录节点
 * 
 * @author linzhaoming
 * 
 *         2013-4-12
 * 
 * @param <T>
 */
public class DirTraceDirectory extends TraceDirectory<TraceTreeEnum> {

	private File dirFile = null;

	public DirTraceDirectory(TraceTreeEnum type) {
		super(type);
	}

	public String getDisplayName() {
		if (dirFile != null) {
			String path = dirFile.getAbsolutePath();
			if (StringUtil.isNotEmpty(path)) {
				return "目录 " + path + " (" + getChildren().size() + ")";
			} else {
				return "目录";
			}
		} else {
			return "目录";
		}
	}

	public File getDirFile() {
		return dirFile;
	}

	public void setDirFile(File file) {
		this.dirFile = file;
	}

}
