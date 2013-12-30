package com.easyfun.eclipse.performance.navigator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;

import com.easyfun.eclipse.performance.ImageConstants;
import com.easyfun.eclipse.performance.PerformanceActivator;
import com.easyfun.eclipse.performance.navigator.cfg.model.Folder;
import com.easyfun.eclipse.performance.navigator.cfg.model.Item;
import com.easyfun.eclipse.performance.navigator.cfg.model.ItemWrapper;
import com.easyfun.eclipse.performance.navigator.helper.DefaultItemProvider;

/**
 * 左侧导航树View
 * 
 * @author linzhaoming
 * 
 * 2011-4-16
 *
 */
public class ItemNavigationView extends ViewPart {
	public static String VIEW_ID = "com.easyfun.eclipse.performance.view.ItemNavigationView";
	private TreeViewer viewer;
	
	protected static Log log = LogFactory.getLog(ItemNavigationView.class);
	
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new ItemNavigatorViewContentProvider());
		viewer.setLabelProvider(new ItemNavigatorViewLabelProvider());
		viewer.setInput(DefaultItemProvider.getNavigator());
		viewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection select = (IStructuredSelection)event.getSelection();
				Object obj = select.getFirstElement();
				if(obj instanceof Item){
					Item item = (Item)obj;
					IWorkbenchPage page = getSite().getWorkbenchWindow().getActivePage();
					if (page != null) {
						ItemWrapper pair = DefaultItemProvider.getNavigatorByType(item);
						pair.getHelper().onDbClk(page, item);
					}
				}
			}
		});
		
		//TreeViewer实现双击展开、关闭
		//TODO:图标更改
		viewer.getTree().addMouseListener(new MouseAdapter(){
	        public void mouseDoubleClick(MouseEvent e) {
	        	IStructuredSelection select = (IStructuredSelection)viewer.getSelection();
				Object obj = select.getFirstElement();
				if(obj instanceof Folder){
		            TreeItem item = viewer.getTree().getItem(new Point(e.x, e.y));

		            if (item != null && item.getItems().length > 0 && item.getItem(0).getText().trim().length() == 0) {
						viewer.expandToLevel(item.getData(), 1);
						return;
					}

		            if (item != null && item.getItemCount() > 0 && item.getItem(0).getText().trim().length() > 0) {
		                item.setExpanded(!item.getExpanded());
		            }			
				}
	        }
		});
		
		Menu parenMenu = new Menu(viewer.getTree());
		MenuItem item = new MenuItem(parenMenu, SWT.DROP_DOWN);
		item.setText("Expland All");
		item.setImage(PerformanceActivator.getImageDescriptor(ImageConstants.EXPLANDALL_ICONS).createImage());
		item.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				viewer.expandAll();
			}
		});
		
		item = new MenuItem(parenMenu, SWT.DROP_DOWN);
		item.setText("Collapse All");
		item.setImage(PerformanceActivator.getImageDescriptor(ImageConstants.COLAPSEALL_ICONS).createImage());
		item.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				viewer.collapseAll();
			}
		});
		
		viewer.getTree().setMenu(parenMenu);
		
		initializeToolBar();
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private void initializeToolBar() {
		IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
	}
}