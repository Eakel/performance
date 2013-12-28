package com.easyfun.eclipse.performance.threaddump.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.easyfun.eclipse.common.ui.kv.KeyValue;
import com.easyfun.eclipse.performance.threaddump.util.ParserUtil;

public abstract class AbstractThreadParser implements IThreadParser{
	/** OBD����ThreadDump��Ψһ��ʶ*/
	private static final String OBD_CALL_MARKER = "com.asiainfo.openboss.obsystem.obdclient.CobdClientObject.call_server";
	/** ���ú�̨�����������ʼ*/
	private static final String OBD_PREFIX = "com.asiainfo.openboss.obd.";
	
	public List<KeyValue> getOBDCallKeyValues(InputStream is) {
		return ParserUtil.getOBDCallKeyValues(OBD_CALL_MARKER, OBD_PREFIX, is);
	}
	
	protected abstract boolean isThreadBeginMark(String line);
	
	protected abstract void handleThreadState(String str, ThreadInfo threadInfo);
	
	public List<ThreadInfo> getThreadInfos(InputStream is, ParserType parserType) {	
//		LogHelper.debug("");
//		LogHelper.debug("��ʼ����, Marker=" + getBeginMarker());
		long t1 = System.currentTimeMillis();
		List<String> list = ParserUtil.readLines(is);
		long t2 = System.currentTimeMillis();
//		LogHelper.debug("��ȡ�ļ�cost=" + (t2 - t1));
		
		boolean marker = false;
		ThreadInfo currentThread = null;	//�����ĵ�ǰthread
		List<String> children = new ArrayList<String>();
		List<ThreadInfo> result = new ArrayList<ThreadInfo>();
		
		for (String str : list) {
//			LogHelper.debug("����: " + str);
//			if(str.trim().startsWith(getBeginMarker())){
			if(isThreadBeginMark(str)){
				marker = true;
				
				//�߳����¿�ʼ����֮ǰ�����������ݱ������ǰ�̵߳�������
				if(currentThread != null){
					List<String> dest = new ArrayList<String>(children.size());
					for (String s : children) {
						dest.add(s);
					}
					currentThread.setContent(dest);
				}
				children = new ArrayList<String>();
				
				ThreadInfo info = new ThreadInfo(str.trim(), parserType);
				currentThread = info;
				
				handleThreadState(str, info);
				result.add(info);
			}else if(marker == true){
				children.add(str);
			}
		}
		long t3 = System.currentTimeMillis();
//		LogHelper.debug("�����ļ�ʱ��cost=" + (t3 - t2));
//		Collections.sort(result);
//		LogHelper.error("��������, total cost=" + (t3 - t1) +  ", Marker=" + getBeginMarker());
		return result;
	}

}
