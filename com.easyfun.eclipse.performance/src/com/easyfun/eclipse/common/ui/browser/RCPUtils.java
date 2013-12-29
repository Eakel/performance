/**
 * 
 */
package com.easyfun.eclipse.common.ui.browser;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.bindings.keys.formatting.IKeyFormatter;
import org.eclipse.jface.bindings.keys.formatting.KeyFormatterFactory;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.keys.WorkbenchKeyboard;

/**
 * @author zhaoming
 *
 * Dec 27, 2007
 */
@SuppressWarnings("restriction")
public class RCPUtils {
	/**
	 * If the plugin org.eclipse.ui.browser is available, will open the inline browser in the editor area.
	 * <p> Other will open the default browser.
	 */
	public static void openBrowser(String url){
		try {
			IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
			IWebBrowser browser = support.createBrowser(
					IWorkbenchBrowserSupport.LOCATION_BAR | IWorkbenchBrowserSupport.NAVIGATION_BAR, "myid", "my name", "my tooltip");
			browser.openURL(new URL(url));
		} catch (PartInitException ex) {
			ex.printStackTrace();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void showKeyAssisShell(){
		Workbench workbench=(Workbench)PlatformUI.getWorkbench();
		WorkbenchKeyboard workbenchKeyboard=new WorkbenchKeyboard(workbench);
		workbenchKeyboard.openMultiKeyAssistShell();
	}
	
	public static void showKeyAssisShell(IKeyFormatter keyFormatter){
		IKeyFormatter prevFormatter=KeyFormatterFactory.getDefault();
		KeyFormatterFactory.setDefault(keyFormatter);
		Workbench workbench=(Workbench)PlatformUI.getWorkbench();
		WorkbenchKeyboard workbenchKeyboard=new WorkbenchKeyboard(workbench);
		workbenchKeyboard.openMultiKeyAssistShell();
		KeyFormatterFactory.setDefault(prevFormatter);
	}
}
