package com.easyfun.eclipse.performance.appframe.webtool.charset;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.mapping.ResourceTraversal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.easyfun.eclipse.performance.appframe.AppFrameActivator;

/**
 * 从指定编码格式中读取设置文件的内容，并覆盖当前文件。 不改变文件的编码
 * 
 * @author linzhaoming
 * 
 *         2011-1-9
 * 
 */
public class ContentEncodingJob extends Job {

	private IStructuredSelection selection;
	private String fromEncoding;

	class ChangeContentEncodingVisitor implements IResourceVisitor {
		private IProgressMonitor monitor;

		public ChangeContentEncodingVisitor(IProgressMonitor monitor) {
			this.monitor = monitor;
		}

		public boolean visit(IResource resource) throws CoreException {
			if (monitor.isCanceled())
				return false;
			if (resource.getName().startsWith(".")) {
				AppFrameActivator.logException(new Exception("文件名称为：" + resource.getName() + "不转换！"));
				return true;
			} else {
				return setEncoding(monitor, resource);
			}
		}
	}

	public ContentEncodingJob(IStructuredSelection selection, String fromEncoding) {
		super("以" + fromEncoding + "格式读取转换文件内容编码");
		this.fromEncoding = fromEncoding;
		this.selection = selection;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("转换文件内容编码为...", IProgressMonitor.UNKNOWN);
		IResource[] resources = new IResource[selection.size()];
		try {
			System.arraycopy(selection.toArray(), 0, resources, 0, selection.size());
			new ResourceTraversal(resources, IResource.DEPTH_INFINITE, 0).accept(new ChangeContentEncodingVisitor(monitor));
		} catch (CoreException e) {
			AppFrameActivator.logException(e);
		}
		// 刷新
		for (IResource s : resources) {
			try {
				s.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			} catch (CoreException e) {
				AppFrameActivator.logException(e);
			}
		}
		monitor.done();
		return Status.OK_STATUS;
	}

	private boolean setEncoding(final IProgressMonitor monitor, IResource res) {
		if (res instanceof IFile) {
			IFile file = (IFile) res;
			monitor.subTask("Process file " + file.getName());
			String suffix = file.getFileExtension();
			if (suffix != null && !file.isLinked() && !file.isPhantom()) {
				try {
					String encoding = file.getCharset();
					// 文件内容需要转码，类似一个拷贝/设定/粘贴的过程
					if (!file.isReadOnly()) {
						InputStream inputstream = file.getContents();
						String str = IOUtils.toString(inputstream, fromEncoding);
						ByteArrayInputStream byte_input = new ByteArrayInputStream(str.getBytes());
						file.setContents(byte_input, IFile.FORCE, monitor);
						AppFrameActivator.logInfo("文件名称为：" + file.getName() + ",编码为：" + encoding);
					}
				} catch (Exception e) {
					AppFrameActivator.logException(e);
				}
			}
			return false;
		} else {
			return true;
		}
	}

}
