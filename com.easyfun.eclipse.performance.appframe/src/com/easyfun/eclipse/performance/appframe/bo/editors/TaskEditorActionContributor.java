//package com.easyfun.littletools.bo.editors;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.eclipse.core.runtime.CoreException;
//import org.eclipse.core.runtime.IConfigurationElement;
//import org.eclipse.core.runtime.IExtension;
//import org.eclipse.core.runtime.IExtensionPoint;
//import org.eclipse.core.runtime.IExtensionRegistry;
//import org.eclipse.core.runtime.Platform;
//import org.eclipse.jface.action.IMenuManager;
//import org.eclipse.jface.action.MenuManager;
//import org.eclipse.jface.viewers.ISelectionChangedListener;
//import org.eclipse.jface.viewers.SelectionChangedEvent;
//import org.eclipse.ui.IEditorPart;
//import org.eclipse.ui.PlatformUI;
//import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
//
//import com.easyfun.eclipse.mybase.MybaseActivator;
//import com.easyfun.eclipse.mybase.actions.common.editors.EditorAction;
//
///**
// * 
// * @author zhaoming
// * 
// * Jan 14, 2008
// */
//public class TaskEditorActionContributor extends
//		MultiPageEditorActionBarContributor implements
//		ISelectionChangedListener {
//	// private FontAction fontAction;
//	// private ColorAction colorAction;
//
//	private List<EditorAction> editorActions = new ArrayList<EditorAction>();
//
//	public TaskEditorActionContributor() {
//		// fontAction=new
//		// FontAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
//		// colorAction=new
//		// ColorAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
//		initActions();
//		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(TaskEditor.EDITOR_ID,
//		// fontAction);
//		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().addSelectionListener(TaskEditor.EDITOR_ID,
//		// colorAction);
//	}
//
//	private void initActions() {
//		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
//		IExtensionPoint point = extensionRegistry.getExtensionPoint(
//				MybaseActivator.PLUGIN_ID, "editorActions");
//		System.out.println(point.getExtensions().length);
//		for (IExtension extension : point.getExtensions()) {
//			IConfigurationElement[] elements = extension
//					.getConfigurationElements();
//			for (IConfigurationElement element : elements) {
//				System.out.println(element.getAttribute("id"));
//				try {
//					EditorAction action = (EditorAction) element
//							.createExecutableExtension("class");
//					editorActions.add(action);
//					PlatformUI.getWorkbench().getActiveWorkbenchWindow()
//							.getSelectionService().addSelectionListener(
//									TaskEditor.EDITOR_ID, action);
//				} catch (CoreException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//
//	public void selectionChanged(SelectionChangedEvent event) {
//		System.out.println(event.getSelection());	
//	}
//	
//	
//	public void setActivePage(IEditorPart activeEditor) {
//
//	}
//
//	
//	public void contributeToMenu(IMenuManager menuManager) {
//		// Text
//		MenuManager textMenuManager = new MenuManager("&Text");
//		menuManager.add(textMenuManager);
//		for (EditorAction action : editorActions) {
//			textMenuManager.add(action);
//		}
//
//	}
//
//	
//	public void dispose() {
//		for (EditorAction action : editorActions) {
//			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
//					.getSelectionService().removeSelectionListener(action);
//		}
//		super.dispose();
//	}
//}
