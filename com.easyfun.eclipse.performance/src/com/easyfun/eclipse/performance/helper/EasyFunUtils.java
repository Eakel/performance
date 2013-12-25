package com.easyfun.eclipse.performance.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.console.IConsoleConstants;

import com.easyfun.eclipse.common.view.item.navigator.ItemNavigationView;
import com.easyfun.eclipse.common.view.item.welcome.WelcomeView;

public class EasyFunUtils {
	
	private static List<String> ignoreViews = new ArrayList<String>();
	
	static{
		ignoreViews.add(ItemNavigationView.VIEW_ID);
		ignoreViews.add(IConsoleConstants.ID_CONSOLE_VIEW);
		ignoreViews.add(WelcomeView.VIEW_ID);
	}
	
	/** 返回不进行控制的ViewID列表*/
	public static List<String> getUncontrolViews(){
		return ignoreViews;
	}
}
