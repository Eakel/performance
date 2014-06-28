package com.easyfun.eclipse.performance.helper;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;

import com.easyfun.eclipse.performance.navigator.WelcomeView;
import com.easyfun.eclipse.rcp.IDEHelper;
import com.easyfun.eclipse.rcp.RCPUtil;

/**
 * ������ͼ�ĸ���״̬�¼�
 * 
 * @author linzhaoming
 *
 * 2013-12-25
 *
 */
public class EasyFunPartListener implements IPartListener {
	public void partActivated(IWorkbenchPart part) {
	}
	
	public void partBroughtToTop(IWorkbenchPart part) {
	}
	
	public void partClosed(IWorkbenchPart part) {
		final IWorkbenchPage page = part.getSite().getPage();
		IViewReference[] iViewReferences = page.getViewReferences();
		boolean isExist = false;	//�Ƿ���һ������View��
		for (IViewReference viewReference : iViewReferences) {
			if(!EasyFunUtils.getUncontrolViews().contains(viewReference.getId())){
				isExist = true;
				break;
			}
		}
		
		//û����ͼ�򿪣��Զ���WelComeView
		if(isExist == false){
			try {
				//�����йر�WelcomeView����
				if(part instanceof IViewPart){
					if(part.getSite().getId().equals(WelcomeView.VIEW_ID)){
						//
					}else{
						Display display = IDEHelper.getActiveWindow().getShell().getDisplay();
						display.asyncExec(new Runnable() {
							public void run() {
								try {
									IWorkbenchPage activePage = IDEHelper.getActivePage();
									if(activePage !=null){
										activePage.showView(WelcomeView.VIEW_ID, null, IWorkbenchPage.VIEW_CREATE);
									}
								} catch (PartInitException e) {
									e.printStackTrace();
								}
							}
						});
					}
				}else{
					page.showView(WelcomeView.VIEW_ID);
				}
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void partDeactivated(IWorkbenchPart part) {
	}
	
	public void partOpened(IWorkbenchPart part) {
	}
}
