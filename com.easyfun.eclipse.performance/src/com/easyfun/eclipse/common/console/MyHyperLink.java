package com.easyfun.eclipse.common.console;

import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class MyHyperLink implements IHyperlink {
	String text;
	int linenum;
	URL url;

	public MyHyperLink(String text, String urlStr, int line) {
		try {
			this.text = text;
			this.url = new URL(urlStr);
			this.linenum = line;
		} catch (Exception e) {
			this.url = null;
		}
	}

	public boolean valid() {
		return this.text != null && this.url != null;
	}

	public String getText() {
		return this.text;
	}

	public void linkActivated() {
		try {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (window == null || window.getActivePage() == null) {
				PlatformUI.getWorkbench().getBrowserSupport().createBrowser("TestConsole").openURL(url);
				return;
			}
			IWorkbenchPage page = window.getActivePage();
			if (ResourcesPlugin.getWorkspace().getRoot() == null) {
				PlatformUI.getWorkbench().getBrowserSupport().createBrowser("TestConsole").openURL(url);
				return;
			}

			IPath path = Path.fromOSString(url.getPath());
			if (!path.isAbsolute()) {
				path = path.makeAbsolute().setDevice(ResourcesPlugin.getWorkspace().getRoot().getLocation().getDevice());
			}

			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
			if (file == null || !file.exists()) {
				if (path != null && !path.isAbsolute() && path.segmentCount() > 1) {
					path = path.removeFirstSegments(1);
					file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				}
			}

			if (file == null || !file.exists()) {
				PlatformUI.getWorkbench().getBrowserSupport().createBrowser("TestConsole").openURL(url);
				return;
			}
			IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();
			if (registry == null) {
				return;
			}
			IEditorDescriptor descriptor = null;
			try {
				IContentDescription contentDescription = file.getContentDescription();
				descriptor = registry.getDefaultEditor(path.lastSegment(), contentDescription != null ? contentDescription.getContentType() : null);
			} catch (CoreException e) {
			}

			if (descriptor == null) {
				descriptor = registry.findEditor(IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID);
			}

			if (descriptor == null) {
				return;
			}
			IEditorInput input = new FileEditorInput(file);
			try {
				IEditorPart editor = page.openEditor(input, descriptor.getId());
				if (editor != null && (editor instanceof ITextEditor)) {
					ITextEditor tmp = (ITextEditor) editor;
					IDocumentProvider provider = tmp.getDocumentProvider();
					IDocument document = provider.getDocument(editor.getEditorInput());
					int start = document.getLineOffset(linenum);
					tmp.selectAndReveal(start, 0);
					tmp.setHighlightRange(start, 0, true);
					page.activate(editor);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {

		}
	}

	public void linkEntered() {
	}

	public void linkExited() {
	}

	public int getLinenum() {
		return linenum;
	}

	public void setLinenum(int linenum) {
		this.linenum = linenum;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public void setText(String text) {
		this.text = text;
	}

}
