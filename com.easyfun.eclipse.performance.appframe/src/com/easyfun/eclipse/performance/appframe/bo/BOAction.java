package com.easyfun.eclipse.performance.appframe.bo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.easyfun.eclipse.performance.appframe.bo.editors.BOCommentEditor;
import com.easyfun.eclipse.performance.appframe.bo.editors.StringEditorInput;
import com.easyfun.eclipse.performance.appframe.bo.editors.StringStorage;
import com.easyfun.eclipse.uiutil.SWTUtil;

/**
 * 获取BO文件的注释
 * 
 * @author linzhaoming
 *
 * 2012-3-20
 *
 */
public class BOAction implements IObjectActionDelegate {
	
	private Shell shell = null;
	private IStructuredSelection selection;
	
	IWorkbenchPart part;
	
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		part = targetPart;
		shell = targetPart.getSite().getShell();
	}

	public void run(IAction action) {
		try {
			List allCommentList = new ArrayList();	
			Iterator<IFile> iter = selection.iterator();
			while(iter.hasNext()){
				IFile file = iter.next();
				InputStream is = file.getContents();
				Element ele = null;
				try {
					ele = parseXml(is);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Element sysboEle = (Element)ele.elements("sysbo").get(0);
				String dataSource = sysboEle.attributeValue("datasource");
				String tableRemark = sysboEle.attributeValue("remark");
				
				Element mappingEle = (Element)sysboEle.elements("mapingenty").get(0);
				if(mappingEle.attributeValue("type").equals("query")){	//忽略查询BO
					System.out.println("Ignore QBO: " + file);
					continue;
				}
				
				String tableName = mappingEle.getStringValue();
				if(tableName.startsWith("{") && tableName.endsWith("}")){
					//TODO: 处理分表的情况 {CM_BUSI_LOG_DTL}，只给基表加注释
					tableName = tableName.substring(1, tableName.length() -1);
					System.out.println("分表：" + tableName);
				}
				if(StringUtils.isNotEmpty(dataSource)){
					tableName = dataSource + "." + tableName;
				}
				
				allCommentList.add("--comment of table " + tableName);

				if(StringUtils.isNotEmpty(tableRemark)){
					allCommentList.add("comment on table " + tableName + " is '" + tableRemark + "';");
				}
				
				
				Element attrlistEle = (Element)sysboEle.elements("attrlist").get(0);
				List attrList = attrlistEle.elements("attr");
				for(int j=0; j<attrList.size(); j++){
					Element attrEle = (Element)attrList.get(j);
					String colName = attrEle.attributeValue("name");
					String colComment = attrEle.attributeValue("remark");
					allCommentList.add("comment on column " + tableName + "." + colName +" is '" + colComment + "';");
				}
			}
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<allCommentList.size(); i++){
				sb.append(allCommentList.get(i)).append("\r\n");
			}
			StringStorage storage = new StringStorage(sb.toString());
			StringEditorInput editorInput = new StringEditorInput(storage);
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				IDE.openEditor(page, editorInput, BOCommentEditor.EDITOR_ID);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		} catch (CoreException e) {
			e.printStackTrace();
			SWTUtil.showError(shell, "处理出错:" + e.getMessage());
		}
		
	}
	
	public void selectionChanged(IAction action, ISelection selection) {
		if(selection != null && selection instanceof IStructuredSelection){
			this.selection = (IStructuredSelection) selection;
			action.setEnabled(!this.selection.isEmpty());
		}
	}
	
	public static Element parseXml(InputStream in) throws Exception {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(in);
		return document.getRootElement();
	}
}
