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
 * ��ָ�������ʽ�ж�ȡ�����ļ������ݣ������ǵ�ǰ�ļ��� ���ı��ļ��ı���
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
				AppFrameActivator.logException(new Exception("�ļ�����Ϊ��" + resource.getName() + "��ת����"));
				return true;
			} else {
				return setEncoding(monitor, resource);
			}
		}
	}

	public ContentEncodingJob(IStructuredSelection selection, String fromEncoding) {
		super("��" + fromEncoding + "��ʽ��ȡת���ļ����ݱ���");
		this.fromEncoding = fromEncoding;
		this.selection = selection;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("ת���ļ����ݱ���Ϊ...", IProgressMonitor.UNKNOWN);
		IResource[] resources = new IResource[selection.size()];
		try {
			System.arraycopy(selection.toArray(), 0, resources, 0, selection.size());
			new ResourceTraversal(resources, IResource.DEPTH_INFINITE, 0).accept(new ChangeContentEncodingVisitor(monitor));
		} catch (CoreException e) {
			AppFrameActivator.logException(e);
		}
		// ˢ��
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
					// �ļ�������Ҫת�룬����һ������/�趨/ճ���Ĺ���
					if (!file.isReadOnly()) {
						InputStream inputstream = file.getContents();
						String str = IOUtils.toString(inputstream, fromEncoding);
						ByteArrayInputStream byte_input = new ByteArrayInputStream(str.getBytes());
						file.setContents(byte_input, IFile.FORCE, monitor);
						AppFrameActivator.logInfo("�ļ�����Ϊ��" + file.getName() + ",����Ϊ��" + encoding);
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
