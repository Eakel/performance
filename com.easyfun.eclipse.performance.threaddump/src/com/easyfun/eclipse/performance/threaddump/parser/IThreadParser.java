package com.easyfun.eclipse.performance.threaddump.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.easyfun.eclipse.common.kv.KeyValue;

public interface IThreadParser {
	/** ������̨�ӿڵ������*/
	public List<KeyValue> getOBDCallKeyValues(InputStream is);
	
	/** �����߳����*/
	public List<ThreadInfo> getThreadInfos(InputStream is, ParserType parserType);
	
	/** ��ȡ�߳�����*/
	public String getThreadName(String line);
	
	/** ��ȡ�̷߳���*/
	public String getThreadMethod(String line);
	
	/** ��ȡ�߳�״̬ͼ��*/
	public Image getStateImage(IThreadState state);
	
	/** ��ȡ�߳�״̬*/
	public String getStateText(IThreadState state);
	
	/** ��ȡThreadDump�������*/
	public String getThreadDesc(List<ThreadInfo> threadInfos);
	
	public HashMap<String, FilterWrapper> getAllFilterMap();
	
}
