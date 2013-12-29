package com.easyfun.eclipse.performance;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.easyfun.eclipse.performance.helper.IConstants;
import com.easyfun.eclipse.performance.navigator.action.CloseAllAction;
import com.easyfun.eclipse.performance.navigator.action.OpenViewAction;
import com.easyfun.eclipse.performance.navigator.action.ShowHideNavigatorViewAction;
import com.easyfun.eclipse.utils.common.ImageConstants;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    // Actions - important to allocate these only in makeActions, and then use them in the fill methods. 
	// This ensures that the actions aren't recreated when fillActionBars is called with FILL_PROXY.
	
	//File
    private IWorkbenchAction exitAction;				//"Exit"
    private IWorkbenchAction newWindowAction;			//"Open in New Window"
    private IWorkbenchAction closeAllAction;			//"Cose All"
    
    //Window
	private IWorkbenchAction nagivatorAction;			//Open/Close Navigator
	private IWorkbenchAction toggleToolBarAction;		//Show/Hide Toolbar
	private OpenViewAction openConsoleAction;			//"Open Console"
	private IWorkbenchAction preferencesAction;			//"Preferences"
	
	//Help
	private OpenViewAction openErrorLogViewAction;		//"Error Log"
	private IWorkbenchAction aboutAction;				//"About"

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    protected void makeActions(final IWorkbenchWindow window) {
        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when the window is closed.

        newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
        register(newWindowAction);
                
        closeAllAction = new CloseAllAction();
        register(closeAllAction);
        
        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        
		// Window
		nagivatorAction = new ShowHideNavigatorViewAction();
		register(nagivatorAction);
		
		toggleToolBarAction = ActionFactory.TOGGLE_COOLBAR.create(window);
		toggleToolBarAction.setText("ToolBar");
		register(toggleToolBarAction);
		
		openConsoleAction = new OpenViewAction(window, "Console", IConstants.CMD_OPEN_CONSOLE, IConstants.CONSOLE_VIEW_ID, false);
		openConsoleAction.setText("Console");
		openConsoleAction.setViewImageDescriptor(ImageConstants.CONSOLE_ICONS);
		register(openConsoleAction);
		
		openErrorLogViewAction = new OpenViewAction(window, "Error Log", IConstants.CMD_OPEN_ERRORLOG, IConstants.ERROR_LOG_VIEW_ID, false);
		openErrorLogViewAction.setText("Error Log");
		openErrorLogViewAction.setViewImageDescriptor(ImageConstants.ERRORLOG_ICONS);
		register(openErrorLogViewAction);
		
		preferencesAction = ActionFactory.PREFERENCES.create(window);
		register(preferencesAction);
		
		//Help
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
    }
    
    protected void fillMenuBar(IMenuManager menuBar) {
    	//不使用标准ID，避免受其他插件影响，引入没用的菜单
        MenuManager fileMenu = new MenuManager("&File", "fileID");			//IWorkbenchActionConstants.M_FILE
        MenuManager windowMenu = new MenuManager("&Window", "windowID");	//IWorkbenchActionConstants.M_WINDOW
        MenuManager helpMenu = new MenuManager("&Help", "helpID");        	//IWorkbenchActionConstants.M_HELP
        
        menuBar.add(fileMenu);
        // Add a group marker indicating where action set menus will appear.
//        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));	//导出单独运行的时候多了空格
        menuBar.add(windowMenu);
        menuBar.add(helpMenu);
        
        // File
        fileMenu.add(newWindowAction);
        fileMenu.add(new Separator());
        fileMenu.add(closeAllAction);
        fileMenu.add(exitAction);
        
        //Window
        windowMenu.add(nagivatorAction);        
        windowMenu.add(toggleToolBarAction);
        windowMenu.add(openConsoleAction);
        windowMenu.add(openErrorLogViewAction);
        windowMenu.add(preferencesAction);
        
        // Help
        helpMenu.add(aboutAction);
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));   
        toolbar.add(openConsoleAction);        
        toolbar.add(nagivatorAction);
        toolbar.add(closeAllAction);
//        toolbar.add(openErrorLogViewAction);
    }
}
