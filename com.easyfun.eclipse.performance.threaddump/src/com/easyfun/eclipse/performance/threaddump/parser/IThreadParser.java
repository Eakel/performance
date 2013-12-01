package com.easyfun.eclipse.performance.threaddump.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.easyfun.eclipse.common.kv.KeyValue;

public interface IThreadParser {
	/** 解析后台接口调用情况*/
	public List<KeyValue> getOBDCallKeyValues(InputStream is);
	
	/** 解析线程情况*/
	public List<ThreadInfo> getThreadInfos(InputStream is, ParserType parserType);
	
	/** 获取线程名称*/
	public String getThreadName(String line);
	
	/** 获取线程方法*/
	public String getThreadMethod(String line);
	
	/** 获取线程状态图标*/
	public Image getStateImage(IThreadState state);
	
	/** 获取线程状态*/
	public String getStateText(IThreadState state);
	
	/** 获取ThreadDump描述情况*/
	public String getThreadDesc(List<ThreadInfo> threadInfos);
	
	public HashMap<String, FilterWrapper> getAllFilterMap();
	
}
