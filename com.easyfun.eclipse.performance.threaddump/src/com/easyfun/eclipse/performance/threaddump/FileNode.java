package com.easyfun.eclipse.performance.threaddump;

import java.io.File;

import com.easyfun.eclipse.component.tree.model.Node;
import com.easyfun.eclipse.performance.threaddump.parser.ParserType;

/**
 * 文件树节点
 * @author linzhaoming
 *
 * 2011-12-16
 *
 * @param <T>
 */
public class FileNode<T> extends  Node<T>{
	private File file;
	
	private ParserType parserType;
	
	public FileNode(T type, ParserType parserType, File file, String name){
		super(type, name);
		this.parserType = parserType;
		this.file = file;
	}
	
	public File getFile(){
		return file;
	}
	
	public ParserType getParserType(){
		return parserType;
	}
	
	public String getDisplayName() {
		if(file != null && file.exists()){
			return file.getName() + " [" + file.getPath() + "]";
		}
		return super.getDisplayName();
	}
}
