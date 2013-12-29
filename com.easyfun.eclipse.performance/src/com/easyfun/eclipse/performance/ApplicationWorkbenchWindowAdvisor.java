package com.easyfun.eclipse.performance;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
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

import com.easyfun.eclipse.common.util.ui.IDEHelper;
import com.easyfun.eclipse.performance.helper.EasyFunPartListener;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    /** 窗口打开之前*/
    public void preWindowOpen() {
    	int height = Display.getCurrent().getBounds().height;
    	int width = Display.getCurrent().getBounds().width;
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(width, height));
        configurer.setShowCoolBar(true);		//显示工具栏
        configurer.setShowStatusLine(false);	//不显示状态栏
        
        //做一些初始化工作，删除没用的Preference Page
        PreferenceManager mgr = PlatformUI.getWorkbench().getPreferenceManager();
        mgr.remove("org.eclipse.help.ui.browsersPreferencePage");
        mgr.remove("org.eclipse.update.internal.ui.preferences.MainPreferencePage");	//Install/Update
        mgr.remove("org.eclipse.debug.ui.DebugPreferencePage");							//Run/Debug
        mgr.remove("org.eclipse.ui.preferencePages.Workbench");							//General
        mgr.remove("org.eclipse.jdt.ui.preferences.JavaBasePreferencePage");			//Java
        mgr.remove("org.eclipse.team.ui.TeamPreferences");								//Team
        mgr.remove("org.apache.log4j.viewer.prefers.LogViewerPreferencePage");			//Log Viewer
    }
    
    public void postWindowOpen() {
//    	super.postWindowOpen();
    	 // 删除菜单栏中不用的项  
		IMenuManager mm = getWindowConfigurer().getActionBarConfigurer().getMenuManager();
		mm.remove("org.eclipse.search.menu");
		mm.remove("org.eclipse.ui.run");
		mm.remove("navigate");
		mm.update(true);  
		
		//删除工具栏中不用的项
        ICoolBarManager coolbar = getWindowConfigurer().getActionBarConfigurer().getCoolBarManager();
		coolbar.remove("org.eclipse.search.searchActionSet");							//search[File search]
		coolbar.remove("org.eclipse.ui.edit.text.actionSet.annotationNavigation");		// annotation
		coolbar.remove("org.eclipse.ui.edit.text.actionSet.navigation");				//last
		// edit location
		coolbar.update(true); 
		
		IDEHelper.getActivePage().addPartListener(new EasyFunPartListener());
    }

    /** 关闭窗口前*/
    public boolean preWindowShellClose() {
    	MessageBox msgBox = new MessageBox(new Shell(), SWT.YES|SWT.NO|SWT.ICON_QUESTION);
        msgBox.setText("退出系统");
        msgBox.setMessage("确定退出系统?");
        if(msgBox.open()==SWT.YES){
            return true;
        }
        return false; 
    }
    
}
