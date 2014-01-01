package com.easyfun.eclipse.performance.jmx;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.easyfun.eclipse.performance.jmx.model.MBeanModel;

/**
 * ´ú±íMBean Input
 * @author zhaoming
 * 
 *         Dec 27, 2007
 */
public class JMXFormEditorInput implements IEditorInput {
	private String summary;
	private MBeanModel mbeanModel;

	public JMXFormEditorInput(MBeanModel mbeanModel) {
		this.mbeanModel = mbeanModel;
		this.summary =  "Summary of " + mbeanModel.getDisplayName();
	}

	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return JmxActivator.getImageDescriptor(JMXImageConstants.ICON_TASK_PATH);
	}

	public String getName() {
		return mbeanModel.getObjectName().getCanonicalName();
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return summary;
	}

	public MBeanModel getMbeanModel() {
		return mbeanModel;
	}

	public void setMbeanModel(MBeanModel mbeanModel) {
		this.mbeanModel = mbeanModel;
	}

	public Object getAdapter(Class adapter) {
		if (adapter == IEditorInput.class) {
			return this;
		}
		return null;
	}
}
