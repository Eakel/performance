package com.easyfun.eclipse.performance.threaddump.parser.sun;

import org.eclipse.swt.graphics.Image;

import com.easyfun.eclipse.performance.threaddump.ImageConstants;
import com.easyfun.eclipse.performance.threaddump.ThreadDumpActivator;
import com.easyfun.eclipse.performance.threaddump.parser.IThreadState;


/**
 * 代表线程的状态(Default)
 * @author linzhaoming
 *
 * 2013-11-17
 *
 */
public class SunThreadState implements IThreadState{
	public String text;
	private Image image;
	
	private static Image unknownImage = ThreadDumpActivator.getImageDescriptor(ImageConstants.ICON_THREAD_DEFAULT).createImage();
	private static Image objectWaitImage = ThreadDumpActivator.getImageDescriptor(ImageConstants.ICON_THREAD_WAITING).createImage();	
	private static Image waitingImage = ThreadDumpActivator.getImageDescriptor(ImageConstants.ICON_THREAD_WAIT_ON_CONDITION).createImage();
	private static Image runnableImage = ThreadDumpActivator.getImageDescriptor(ImageConstants.ICON_THREAD_RUNNABLE).createImage();
	
	public static SunThreadState STATE_RUNNABLE = new SunThreadState("Runnable", runnableImage);
	public static SunThreadState STATE_WAITING_ON_CONDITION = new SunThreadState("WaitOnCondition", objectWaitImage);
	public static SunThreadState STATE_OBJECT_WAIT = new SunThreadState("Wait", waitingImage);
	
	public static SunThreadState STATE_UNKNOWN = new SunThreadState("Unknown", unknownImage);
	
	public SunThreadState(String text, Image image){
		this.text = text;
		this.image = image;
	}

	public String getText() {
		return text;
	}
	
	public Image getImage(){
		return image;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof SunThreadState) {
			SunThreadState state = (SunThreadState) obj;
			return text.equals(state.getText());
		} else {
			return super.equals(obj);
		}
	}

	public static SunThreadState getStateFromTitle(String str) {
		if (str.contains("Object.wait()")) {
			return SunThreadState.STATE_OBJECT_WAIT;
		} else if (str.contains("waiting on condition")) {
			return SunThreadState.STATE_WAITING_ON_CONDITION;
		} else if (str.contains("runnable")) {
			return SunThreadState.STATE_RUNNABLE;
		}
		return STATE_UNKNOWN;
	}
}
