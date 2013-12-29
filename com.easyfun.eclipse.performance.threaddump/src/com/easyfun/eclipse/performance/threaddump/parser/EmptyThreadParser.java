package com.easyfun.eclipse.performance.threaddump.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.graphics.Image;

import com.easyfun.eclipse.component.kv.KeyValue;

public class EmptyThreadParser implements IThreadParser {

	public List<KeyValue> getOBDCallKeyValues(InputStream is) {
		return new ArrayList<KeyValue>();
	}

	public List<ThreadInfo> getThreadInfos(InputStream is, ParserType parsetType) {
		return new ArrayList<ThreadInfo>();
	}
	
	public String getThreadName(String line) {
		return line;
	}

	public String getThreadMethod(String line){
		return "";
	}
	
	public Image getStateImage(IThreadState state) {
		return null;
	}

	public String getStateText(IThreadState state) {
		return "";
	}
	
	public String getThreadDesc(List<ThreadInfo> threadInfos){
		return "";
	}
	
	public HashMap<String, FilterWrapper> getAllFilterMap(){
		return new HashMap<String, FilterWrapper>();
	}
	
	public ViewerFilter getAllFilters(){
		return null;
	}
}
