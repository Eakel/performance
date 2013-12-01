package com.easyfun.eclipse.performance;

/**
 * Interface defining the application's command IDs.
 * Key bindings can be defined for specific commands.
 * To associate an action with a command, use IAction.setActionDefinitionId(commandId).
 *
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds {

	/** 打开HTTP VIEW*/
    public static final String CMD_OPEN_HTTP = "com.easyfun.eclipse.performance.openHttp";
    
    /** 打开CONSOLE*/
    public static final String CMD_OPEN_CONSOLE = "com.easyfun.eclipse.performance.openConsole";
    
    
    public static final String CMD_OPEN_MESSAGE = "com.easyfun.eclipse.performance.openMessage";
    
    public static final String CMD_OPEN_ERRORLOG = "com.easyfun.eclipse.performance.openErrorLog";
    
}
