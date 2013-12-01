package com.easyfun.eclipse.performance;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.console.IConsoleConstants;

import com.easyfun.eclipse.common.action.CloseAllAction;
import com.easyfun.eclipse.common.action.OpenViewAction;
import com.easyfun.eclipse.common.action.ShowHideNavigatorViewAction;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    // Actions - important to allocate these only in makeActions, and then use them in the fill methods. 
	// This ensures that the actions aren't recreated when fillActionBars is called with FILL_PROXY.
	
	//File
    private IWorkbenchAction exitAction;		//Exit
    private IWorkbenchAction newWindowAction;	//Open in New Window
//    private OpenViewAction openHttpViewAction;		//Open Another Message View
    private IWorkbenchAction closeAllAction;
    
    //Window
	private IWorkbenchAction outlineAction;	
	private IWorkbenchAction toggleToolBarAction;
	private OpenViewAction openConsoleAction;
	private IWorkbenchAction preferencesAction;
	
	//Help
	private OpenViewAction openErrorLogViewAction;		//Error Log
	private IWorkbenchAction aboutAction;		//About

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
                
//        openHttpViewAction = new OpenViewAction(window, "HTTP", ICommandIds.CMD_OPEN_HTTP, UIConstants.VIEWID_HTTPCLIENT, true);
//        openHttpViewAction.setNeedMulti(true);
//        register(openHttpViewAction);
//        
        closeAllAction = new CloseAllAction();
//        register(closeAllAction);
        
        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        
		// Window
		outlineAction = new ShowHideNavigatorViewAction();
		register(outlineAction);
		
		toggleToolBarAction = ActionFactory.TOGGLE_COOLBAR.create(window);
		toggleToolBarAction.setText("Show/Hide ToolBar");
		register(toggleToolBarAction);
		
		openConsoleAction = new OpenViewAction(window, "Open Console", ICommandIds.CMD_OPEN_CONSOLE, IConsoleConstants.ID_CONSOLE_VIEW, false);
		openConsoleAction.setText("Open Console");
		register(openConsoleAction);
		
		preferencesAction = ActionFactory.PREFERENCES.create(window);
		register(preferencesAction);
		
		//Help
		openErrorLogViewAction = new OpenViewAction(window, "Open Console", ICommandIds.CMD_OPEN_ERRORLOG, "org.eclipse.pde.runtime.LogView", false);
		openErrorLogViewAction.setText("Error Log");
		register(openErrorLogViewAction);
		
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
    }
    
    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager windowMenu = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);        
        
        menuBar.add(fileMenu);
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(windowMenu);
        menuBar.add(helpMenu);
        
        // File
        fileMenu.add(newWindowAction);
        fileMenu.add(new Separator());
//        fileMenu.add(openHttpViewAction);
        fileMenu.add(new Separator());
        fileMenu.add(closeAllAction);
        fileMenu.add(exitAction);
        
        //Window
        windowMenu.add(outlineAction);        
        windowMenu.add(toggleToolBarAction);
        windowMenu.add(openConsoleAction);
        windowMenu.add(preferencesAction);
        
        // Help
        helpMenu.add(openErrorLogViewAction);
        helpMenu.add(aboutAction);
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
//        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
//        coolBar.add(new ToolBarContributionItem(toolbar, "main"));   
//        toolbar.add(openHttpViewAction);
    }
}