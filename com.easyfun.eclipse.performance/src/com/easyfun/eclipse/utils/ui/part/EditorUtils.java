/**
 * 
 */
package com.easyfun.eclipse.utils.ui.part;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;

import com.easyfun.eclipse.performance.PerformanceActivator;

/**
 * @author linzhaoming
 *
 * Dec 27, 2007
 */
public class EditorUtils {
	public static void openTextEditorForFile(String fileName){
		IPath path=new Path(fileName);
		final IFileStore fileStore = EFS.getLocalFileSystem().getStore(path);
		if (fileStore.fetchInfo().exists()) {
			final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				PerformanceActivator.getDefault().getWorkbench().getProgressService().run(
						false, false, new IRunnableWithProgress() {
							public void run(IProgressMonitor monitor)
									throws InvocationTargetException,
									InterruptedException {
								try {
									IDE.openEditorOnFileStore(page, fileStore);
									IEditorInput editorInput=new FileStoreEditorInput(fileStore);
									IDE.openEditor(page, editorInput, "org.eclipse.ui.DefaultTextEditor");
								} catch (PartInitException e) {
									e.printStackTrace();
								}
							}

						});

			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}	
}
