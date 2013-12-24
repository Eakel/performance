package com.easyfun.eclipse.performance.awr;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;

/**
 * @author linzhaoming
 *
 * 2011-5-8
 *
 */
public class AWRView extends ViewPart {

	public AWRView() {
	}

	public void createPartControl(Composite parent) {

	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	private Shell getShell(){
		return getSite().getShell();
	}

}