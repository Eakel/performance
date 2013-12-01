package com.easyfun.eclipse.performance.trace.model;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.easyfun.eclipse.common.tree.model.ITreeModel;
import com.easyfun.eclipse.performance.trace.builder.MyXMLWriter;
import com.easyfun.eclipse.performance.trace.ui.TraceUIDesc;


public class AppTrace extends DefaultTrace implements ITrace, ITreeModel {
	private List<ITrace> child = new ArrayList<ITrace>();
	private String finishTime;
	/** APP�����ĸ�Ҫ��Ϣ*/
	private String msg;
	
	/** Trace��XML Document��Ϣ*/
	private Document document;
	
	/** srv��������*/
	private int svrCount = 0;

	/** dao��������*/
	private int daoCount = 0;

	/** memcached��������*/
	private int memCount = 0;

	/**secmem��������*/
	private int secMemCount = 0;

	/** cau��������*/
	private int cauCount = 0;

	/** jdbc��������*/
	private int jdbcCount = 0;

	/** mdb��������*/
	private int mdbCount = 0;

	/** http��������*/
	private int httpCount = 0;

	/** ws��������*/
	private int wsCount = 0;
	
	/** BCC��������*/
	private int bccCount = 0;
	
	private String traceContent = null;
	
	private TraceUIDesc uiDesc = new TraceUIDesc();
	
	public String getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public void addChild(ITrace objITrace) {
		this.child.add(objITrace);
	}

	public List getChild() {
		return this.child;
	}

	public boolean isNode() {
		return (this.child != null) && (this.child.size() > 0);
	}
	
	/** APP�����ĸ�Ҫ��Ϣ*/
	public String getDisplay() {
		StringBuffer sb = new StringBuffer();
		sb.append("<tr><td>" + getType().toString().toLowerCase() + "</td><td>" + getCreateTime() + "</td></tr>");
		for (ITrace trace : child) {
			sb.append(trace.getDisplay());
		}
		return sb.toString();
	}
	
	/** APP�����ĸ�Ҫ��Ϣ*/
	public String getMsg() {
		return this.msg;
	}

	/** APP�����ĸ�Ҫ��Ϣ*/
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getSvrCount() {
		return svrCount;
	}

	public void setSvrCount(int svrCount) {
		this.svrCount = svrCount;
	}

	public int getDaoCount() {
		return daoCount;
	}

	public void setDaoCount(int daoCount) {
		this.daoCount = daoCount;
	}

	public int getMemCount() {
		return memCount;
	}

	public void setMemCount(int memCount) {
		this.memCount = memCount;
	}

	public int getSecMemCount() {
		return secMemCount;
	}

	public void setSecMemCount(int secMemCount) {
		this.secMemCount = secMemCount;
	}

	public int getJdbcCount() {
		return jdbcCount;
	}

	public void setJdbcCount(int jdbcCount) {
		this.jdbcCount = jdbcCount;
	}

	public int getMdbCount() {
		return mdbCount;
	}

	public void setMdbCount(int mdbCount) {
		this.mdbCount = mdbCount;
	}

	public int getHttpCount() {
		return httpCount;
	}

	public void setHttpCount(int httpCount) {
		this.httpCount = httpCount;
	}
	
	public int getCauCount() {
		return cauCount;
	}

	public void setCauCount(int cauCount) {
		this.cauCount = cauCount;
	}

	public int getWsCount() {
		return wsCount;
	}

	public void setWsCount(int wsCount) {
		this.wsCount = wsCount;
	}
	
	public int getBccCount() {
		return bccCount;
	}

	public void setBccCount(int bccCount) {
		this.bccCount = bccCount;
	}

	//ITreeModel��UI��Ϣ
	public String getTraceContent() {
		return getTraceContent(true);
	}
	
	public TraceUIDesc getUiDesc() {
		return uiDesc;
	}

	public void setUiDesc(TraceUIDesc uiDesc) {
		this.uiDesc = uiDesc;
	}

	public String getTraceContent(boolean omitCDATA) {
		if(omitCDATA){
			return traceContent;
		}else{
		    OutputFormat format = OutputFormat.createPrettyPrint();
		    format.setEncoding("UTF-8");
		   
		    StringWriter writer;
			try {
				writer = new StringWriter();
				XMLWriter xmlwriter = new MyXMLWriter(writer, format, omitCDATA);
				xmlwriter.write(document);
				return writer.toString();
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}
		}
	}

	public void setTraceDocument(Document document) {
		this.document = document;
		OutputFormat format = OutputFormat.createPrettyPrint();
	    format.setEncoding("UTF-8");
	   
	    StringWriter writer;
		try {
			writer = new StringWriter();
			XMLWriter xmlwriter = new MyXMLWriter(writer, format, true);
			xmlwriter.write(document);
			traceContent = writer.toString();
		} catch (IOException e) {
			e.printStackTrace();
			traceContent = "";
		}
	}

	public Collection getChildren() {
		return child;
	}

	public String getDisplayName() {
		return "trace " + getCreateTime() + getDisplayCost();
	}

	public Object getInstance() {
		return null;
	}

	public ITreeModel getParent() {
		return null;
	}

	public boolean isDirectory() {
		return true;
	}
	
}