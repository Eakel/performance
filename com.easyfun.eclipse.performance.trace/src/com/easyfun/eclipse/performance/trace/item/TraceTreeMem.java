package com.easyfun.eclipse.performance.trace.item;

import java.util.ArrayList;
import java.util.List;

import com.easyfun.eclipse.performance.trace.item.trace.DirTraceDirectory;
import com.easyfun.eclipse.performance.trace.item.trace.FileTraceDirectory;
import com.easyfun.eclipse.performance.trace.item.trace.SFtpTraceDirectory;
import com.easyfun.eclipse.performance.trace.item.trace.TraceDirectory;


/**
 * ���ڱ���򿪵�Trace�ļ�
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
		
		TraceDirectory<TraceTreeEnum> fileDir = new FileTraceDirectory(TraceTreeEnum.DIR_FILE);	//�ļ�
		list.add(fileDir);
		
		TraceDirectory<TraceTreeEnum> dirDir = new DirTraceDirectory(TraceTreeEnum.DIR_DIR);	//Ŀ¼
		list.add(dirDir);
		
		TraceDirectory ftpDir = new SFtpTraceDirectory(TraceTreeEnum.DIR_FTP);	//FTP
		list.add(ftpDir);
	}
	
	/** ��ȡ[�ļ�]Ŀ¼*/
	public static FileTraceDirectory getFileDirectory(){
		if(list == null){
			init();
		}
		
		return (FileTraceDirectory)list.get(0);
	}
	
	/** ��ȡ[Ŀ¼]Ŀ¼*/
	public static DirTraceDirectory getDirDirectory(){
		if(list == null){
			init();
		}
		
		return (DirTraceDirectory)list.get(1);
	}
	
	/** ��ȡ[FTP]Ŀ¼*/
	public static SFtpTraceDirectory getFTPirectory(){
		if(list == null){
			init();
		}
		
		return (SFtpTraceDirectory)list.get(2);
	}
	
}
