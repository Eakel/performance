package com.easyfun.eclipse.performance.threaddump.parser;

import java.util.List;



/**
 * Represents a threadInfo in ThreadDump
 * @author linzhaoming
 *
 * 2011-4-16
 *
 */
public class ThreadInfo implements Comparable<ThreadInfo>{
	private String title;
	private List<String> content;
	private IThreadState state = null;

	private ParserType parserType;
	
	private String name;
	
	private String method;
	
	public ThreadInfo(String title, ParserType parserType) {
		this.title = title;
		this.parserType = parserType;
		IThreadParser parser = ThreadParserFactory.getThreadParser(parserType);
		name = parser.getThreadName(title);
		method = parser.getThreadMethod(title);
	}

	public List<String> getContent() {
		return content;
	}

	public void setContent(List<String> content) {
		this.content = content;
	}

	public int compareTo(ThreadInfo o) {
		return title.compareTo(o.title);
	}
	
	public String getName(){
		return name;
	}
	
	public String getMethod(){
		return method;
	}
	
	public IThreadState getState() {
		return state;
	}

	public void setState(IThreadState state) {
		this.state = state;
	}
	
	public ParserType getParserType(){
		return parserType;
	}

	public String getDisplayText(){
		StringBuffer sb = new StringBuffer(title);
		sb.append("\n");
		if(content != null){
			for (String str : content) {
				sb.append(str).append("\n");
			}
		}
		return sb.toString();
	}

}
