package com.easyfun.eclipse.performance.trace.item.trace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.easyfun.eclipse.component.ftp.FTPBean;
import com.easyfun.eclipse.performance.trace.item.TraceTreeEnum;


/**
 * [SFTP] TraceÄ¿Â¼½Úµã
 * 
 * @author linzhaoming
 *
 * 2013-4-12
 *
 * @param <T>
 */
public class SFtpTraceDirectory extends TraceDirectory<TraceTreeEnum>{
	
	private FTPBean ftpBean;
	
	public SFtpTraceDirectory(TraceTreeEnum type){
		super(type);
	}
	
	public List<TraceNode> getChildren() {
		if(initAll == true){
			List<TraceNode> retList = new ArrayList<TraceNode>();
			for (TraceNode traceNode : getRealChildren()) {
				if(traceNode.getAppTrace()!= null && traceNode.getAppTrace().isVisible()){
					retList.add(traceNode);
				}
			}
			return retList;
		}else{
			return getRealChildren();
		}
	}
	
	public String getDisplayName() {
		if(ftpBean == null){
			return "SFTP";
		}else{
			String path = "[" + ftpBean.getHost() + ":" + ftpBean.getPort() + "] " + ftpBean.getRemotePath();
			return "SFTP " + path + " (" + getChildren().size() + ")";
		}
	}
	
	public void sortTrace(int type){
		if(type == SORT_FILETIME){
			Collections.sort(children, new Comparator<TraceNode>(){
				public int compare(TraceNode o1, TraceNode o2) {
					//TODO: FTP
					return o1.getAppTrace().getCreateTime().compareTo(o2.getAppTrace().getCreateTime());
				}
			});
		}else{
			super.sortTrace(type);
		}
	}

	public FTPBean getFtpBean() {
		return ftpBean;
	}

	public void setFtpBean(FTPBean ftpBean) {
		this.ftpBean = ftpBean;
	}
	
}
