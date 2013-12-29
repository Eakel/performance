
package com.easyfun.eclipse.rcp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * @author linzhaoming
 *
 */
public class IDEHelper {
    protected static IProject activeProject;
    
    /** 获取当前ActiveWindow*/
    public static IWorkbenchWindow getActiveWindow(){
    	return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    }

    /***
	 * Get the active workbench page
	 * @return null if not found.
	 */
	public static IWorkbenchPage getActivePage() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null){
			return null;
		}

		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window == null){
			return null;
		}

		return window.getActivePage();
	}
	
	/** Get the active editor
	 * @return The active editor
	 */
	public static IEditorPart getActiveEditor() {
		// make sure the objects are still valid.
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null){
			return null;
		}

		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
		if (workbenchWindow == null){
			return null;
		}

		IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
		if (workbenchPage == null){
			return null;
		}

		return workbenchPage.getActiveEditor();
	}

	/**
	 * Get the actively selected project.  
	 * We will first get the active page then if no  editors are open, then get the view 
	 * @param project
	 * @return
	 */
	public static IProject getActiveProject() {
		// get the project from the selected project.
		// we are trying to get the project from the active project first
		// if we fail then go to the active editor then go through all the
		// work bench parts and get the selection.
		IProject project = null;

		// this client action wasn't doing its job lets try to
		// do it ourselfes.
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench != null) {
			IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
			if (activeWindow != null) {
				IWorkbenchPage activePage = activeWindow.getActivePage();
				if (activePage != null) {
					IWorkbenchPart workbenchPart = activePage.getActivePart();
					// If the active workbench part is an editor
					// get the project by try to get the file associated with the editor
					if (workbenchPart instanceof IEditorPart) {
						project = getProjectFromEditor(workbenchPart);
					} else {
						// if the active part is not an editor
						// get the selection provider for the site and get the resource from the selection provider.
						project = getProjectFromView(workbenchPart);
					}
					// get the editor and workbench part
					if (project == null) {
						// get the active editor
						IEditorPart editorPart = activePage.getActiveEditor();
						// get the project from the editor.
						if (editorPart != null) {
							project = getProjectFromEditor(editorPart);
						}
						// if we didn't find any project go through all the views
						if (project == null) {
							IViewReference[] viewReferences = activePage.getViewReferences();
							for (int index = 0; index < viewReferences.length; index++) {
								// get the selected project from the view.
								project = getProjectFromView(viewReferences[index].getPart(false));
								// if we found a project then stop.
								if (project != null){
									break;
								}
							}
						}
					}
				}
			}
		}
		return project;
	}
	
	public static void closeAllEditors(IProject project) {
		// Cycle through the editors and close any that are from this project.
		// IProject matchingProject is the project associated with the editor
		// we are looking at.
		IProject matchingProject = null;

		// Look through all of the editors in all of the pages in all of the
		// workbench windows
		// for files that come from this project.
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();

		for (int i = 0; i < windows.length; i++) {
			IWorkbenchPage[] pages = windows[i].getPages();
			for (int j = 0; j < pages.length; j++) {
				IEditorReference[] editors = pages[j].getEditorReferences();
				for (int k = 0; k < editors.length; k++) {
					// Get the file that this editor is editing.
					IEditorPart editor = editors[k].getEditor(false);
					IEditorInput editorInput = editor.getEditorInput();
					if (editorInput instanceof IFileEditorInput) {
						// Check if the file is from the project.
						IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
						IFile file = fileEditorInput.getFile();
						// get the project for this file
						matchingProject = file.getProject();
					}

					if (project.equals(matchingProject)) {
						// If the project from the editor is the same as the
						// deleted project
						// close the window.
						final IWorkbenchPage finalWorkbenchPage = pages[j];
						final IEditorPart finalEditor = editor;
						Display display = Display.getDefault();
						display.asyncExec(new Runnable() {
							public void run() {
								finalWorkbenchPage.closeEditor(finalEditor, false);
							}
						});
					}
				}
			}
		}
	}

	/**
	 * Get the project from the supplied view by getting the
	 * the selection proview and seeing if the selection
	 * is a resource.  If it is then get the project from 
	 * the resource.
	 * @param workbenchPart
	 * @return
	 */
	private static IProject getProjectFromView(IWorkbenchPart workbenchPart) {
		IProject project = null;

		// Make sure we have a workbenchpart
		if (workbenchPart == null)
			return project;

		// Make sure we have a site
		IWorkbenchPartSite site = workbenchPart.getSite();
		if (site == null)
			return project;

		ISelectionProvider selectionProvider = site.getSelectionProvider();
		if (selectionProvider != null) {
			// get the selection object from the selection provider
			// and see it is a selection provider.
			ISelection selection = selectionProvider.getSelection();
			if (selection instanceof StructuredSelection) {
				StructuredSelection structuredSelection = (StructuredSelection) selection;
				Object object = structuredSelection.getFirstElement();
				if (object instanceof IResource) {
					IResource resource = (IResource) object;
					project = resource.getProject();
				} else if (object instanceof IJavaElement) {
					IJavaElement resource = (IJavaElement) object;
					project = resource.getJavaProject().getProject();
				}
			}
		}
		return project;
	}

	/**
	 * get the project from the editor
	 * @param project
	 * @param workbenchPart
	 * @return
	 */
	private static IProject getProjectFromEditor(IWorkbenchPart workbenchPart) {
		// Get the input editor.
		IEditorPart editorPart = (IEditorPart) workbenchPart;
		IProject project = null;
		IEditorInput editorInput = editorPart.getEditorInput();
		if (editorInput instanceof IFileEditorInput) {
			IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
			IFile file = fileEditorInput.getFile();
			// Get the project for this file
			project = file.getProject();
		}

		return project;
	}
	

	/**
	 * Returns the project for a given structured selection.
	 *
	 */
	public static IProject toProject(IStructuredSelection selection) {
		// fix the directory so if it equals the project set it to the
		// web application root directory.
		Object selectedObject = selection.getFirstElement();
		IProject project = null;
		// if the object is a project
		if (selectedObject instanceof IProject) {
			project = (IProject) selectedObject;

		} else if (selectedObject instanceof IResource) {
			// see if the resource is a direct child of the
			IResource resource = (IResource) selectedObject;
			project = resource.getProject();
		}

		return project;
	}
	
	public static IEditorPart getOpenEditor(IFile file) {
		// Cycle through the editors and find the one that matches the
		// supplied file.

		// Look through all of the editors in all of the pages in all of the
		// workbench windows for files that come from this project.
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			IWorkbenchPage[] pages = windows[i].getPages();
			for (int j = 0; j < pages.length; j++) {
				IEditorReference[] editors = pages[j].getEditorReferences();
				for (int k = 0; k < editors.length; k++) {
					// Get the file that this editor is editing.
					IEditorPart editor = editors[k].getEditor(false);
					if (editor != null) {
						IEditorInput editorInput = editor.getEditorInput();
						if (editorInput instanceof IFileEditorInput) {
							// Check if the file is from the project.
							IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
							IFile editorFile = fileEditorInput.getFile();
							if (editorFile.equals(file) == true) {
								return editor;
							}
						}
					}
				}
			}
		}

		return null;
	}
	
	/**
	 * Get all open editors for the supplied project.
	 * @param project
	 * @return
	 */
	public static List<IEditorPart> getAllOpenEditors( final IProject project) {
		final List<IEditorPart> list = new ArrayList<IEditorPart>();
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				// Cycle through the editors and close any that are from this  project.
				// IProject matchingProject is the project associated with the
				// editor we are looking at.

				// Look through all of the editors in all of the pages in all of
				// the workbench windows for files that come from this project.
				IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
				for (int i = 0; i < windows.length; i++) {
					IWorkbenchPage[] pages = windows[i].getPages();
					for (int j = 0; j < pages.length; j++) {
						IEditorReference[] editors = pages[j].getEditorReferences();
						for (int k = 0; k < editors.length; k++) {
							// Get the file that this editor is editing.
							IEditorPart editor = editors[k].getEditor(false);
							if (editor != null) {
								IEditorInput editorInput = editor.getEditorInput();
								if (editorInput instanceof IFileEditorInput) {
									// Check if the file is from the project.
									IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
									IFile file = fileEditorInput.getFile();
									// get the project for this file
									if (project.equals(file.getProject()) == true) {
										list.add(editor);
									}
								}
							}
						}
					}
				}
			}
		});

		return list;
	}


	/**
     * get the actively selected project. We will first get the active page then
     * if no editors are open, then get the view
     */
	public static ISelection getActiveSelection() {
		ISelection selection = null;
		// this client action wasn't doing its job lets try to do it ourselfes.
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench != null) {
			IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
			if (activeWindow != null) {
				IWorkbenchPage activePage = activeWindow.getActivePage();
				if (activePage != null) {
					IWorkbenchPart workbenchPart = activePage.getActivePart();

					// if the active workbench part is an editor get the project by try to get the file associated with
					// the editor make sure we have a site
					IWorkbenchPartSite site = workbenchPart.getSite();
					if (site == null){
						return selection;
					}

					ISelectionProvider selectionProvider = site.getSelectionProvider();
					if (selectionProvider != null) {
						// get the selection object from the selection provider
						// and see it is a selection provider.
						selection = selectionProvider.getSelection();
					}
					if (selection == null && workbenchPart instanceof IEditorPart) {
						// if the active part is an editor and the
						IViewReference[] viewReferences = activePage.getViewReferences();
						for (int index = 0; index < viewReferences.length; index++) {
							if (viewReferences[index].getId().equals("org.eclipse.ui.views.ContentOutline") == true) {
								selectionProvider = viewReferences[index].getPart(false).getSite().getSelectionProvider();
								if (selectionProvider != null) {
									selection = selectionProvider.getSelection();
									break;
								}
							}
						}
					}
				}
			}
		}
		return selection;
	}
	
	/**
	 * Given the supplied name find the IProject that corresponds to it.
	 * @param projectName
	 * @return
	 */
	public static IProject getProjectByName(String projectName) {
		IWorkspace myWorkspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = myWorkspace.getRoot();
		IProject[] projects = workspaceRoot.getProjects();

		for (int i = 0; i < projects.length; i++) {
			IProject project = projects[i];
			if (project.isAccessible() == true && projectName.equals(project.getName()) == true) {
				return projects[i];
			}
		}

		return null;
	}
	
	public static void refreshJavaClasspath(IProject project) {
		// this will cause the class path to get updated and the plugins libararies will be loaded properly.
		try {
			IJavaProject javaProject = JavaCore.create(project);
			javaProject.setRawClasspath(javaProject.getRawClasspath(), javaProject.getOutputLocation(), new NullProgressMonitor());
		} catch (JavaModelException javaModelException) {
			javaModelException.printStackTrace();
		}
	}  
}