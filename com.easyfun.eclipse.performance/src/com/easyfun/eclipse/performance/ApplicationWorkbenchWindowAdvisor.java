package com.easyfun.eclipse.performance;

import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    /** ���ڴ�֮ǰ*/
    public void preWindowOpen() {
    	int height = Display.getCurrent().getBounds().height;
    	int width = Display.getCurrent().getBounds().width;
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(width, height));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(false);
        
        //��һЩ��ʼ������
        PreferenceManager mgr = PlatformUI.getWorkbench().getPreferenceManager();
        mgr.remove("org.eclipse.help.ui.browsersPreferencePage");
        mgr.remove("org.eclipse.update.internal.ui.preferences.MainPreferencePage");
    }
    
    public void postWindowOpen() {
//    	super.postWindowOpen();
    }

    /** �رմ���ǰ*/
    public boolean preWindowShellClose() {
    	MessageBox msgBox = new MessageBox(new Shell(), SWT.YES|SWT.NO|SWT.ICON_QUESTION);
        msgBox.setText("�˳�ϵͳ");
        msgBox.setMessage("ȷ���˳�ϵͳ?");
        if(msgBox.open()==SWT.YES){
            return true;
        }
        return false; 
    }
    
}