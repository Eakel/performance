package com.easyfun.eclipse.performance.trace.item;

import java.util.ArrayList;
import java.util.List;

import com.easyfun.eclipse.performance.trace.item.trace.DirTraceDirectory;
import com.easyfun.eclipse.performance.trace.item.trace.FileTraceDirectory;
import com.easyfun.eclipse.performance.trace.item.trace.SFtpTraceDirectory;
import com.easyfun.eclipse.performance.trace.item.trace.TraceDirectory;


/**
 * 用于保存打开的Trace文件
 * 
 * @author linzhaoming
 *
 * 2013-4-7
 *
 */
public class TraceTreeMem {
	private static List<TraceDirectory<TraceTreeEnum>> list = null;

	public static List<TraceDirectory<TraceTreeEnum>> getTraceFileInput(){
		if(list == null){
			init();
		}
		
		return list;
	}
	
	private static void init(){
		list = new ArrayList<TraceDirectory<TraceTreeEnum>>();
		
		TraceDirectory<TraceTreeEnum> fileDir = new FileTraceDirectory(TraceTreeEnum.DIR_FILE);	//文件
		list.add(fileDir);
		
		TraceDirectory<TraceTreeEnum> dirDir = new DirTraceDirectory(TraceTreeEnum.DIR_DIR);	//目录
		list.add(dirDir);
		
		TraceDirectory ftpDir = new SFtpTraceDirectory(TraceTreeEnum.DIR_FTP);	//FTP
		list.add(ftpDir);
	}
	
	/** 获取[文件]目录*/
	public static FileTraceDirectory getFileDirectory(){
		if(list == null){
			init();
		}
		
		return (FileTraceDirectory)list.get(0);
	}
	
	/** 获取[目录]目录*/
	public static DirTraceDirectory getDirDirectory(){
		if(list == null){
			init();
		}
		
		return (DirTraceDirectory)list.get(1);
	}
	
	/** 获取[FTP]目录*/
	public static SFtpTraceDirectory getFTPirectory(){
		if(list == null){
			init();
		}
		
		return (SFtpTraceDirectory)list.get(2);
	}
	
}
