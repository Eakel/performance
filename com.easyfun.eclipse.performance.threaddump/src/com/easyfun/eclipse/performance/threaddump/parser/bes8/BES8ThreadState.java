package com.easyfun.eclipse.performance.threaddump.parser.bes8;

import org.eclipse.swt.graphics.Image;

import com.easyfun.eclipse.performance.threaddump.ThreadDumpImageConstants;
import com.easyfun.eclipse.performance.threaddump.ThreadDumpActivator;
import com.easyfun.eclipse.performance.threaddump.parser.IThreadState;


/**
 * �����̵߳�״̬(Default)
 * @author linzhaoming
 *
 * 2013-11-17
 *
 */
public class BES8ThreadState implements IThreadState{
	public String text;
	private Image image;
	
	private static Image unknownImage = ThreadDumpActivator.getImageDescriptor(ThreadDumpImageConstants.ICON_THREAD_DEFAULT_PATH).createImage();
	private static Image objectWaitImage = ThreadDumpActivator.getImageDescriptor(ThreadDumpImageConstants.ICON_THREAD_WAITING_PATH).createImage();	
	private static Image waitingImage = ThreadDumpActivator.getImageDescriptor(ThreadDumpImageConstants.ICON_THREAD_WAIT_ON_CONDITION_PATH).createImage();
	private static Image runnableImage = ThreadDumpActivator.getImageDescriptor(ThreadDumpImageConstants.ICON_THREAD_RUNNABLE_PATH).createImage();
	
	public static BES8ThreadState STATE_RUNNABLE = new BES8ThreadState("Runnable", runnableImage);
	public static BES8ThreadState STATE_WAITING_ON_CONDITION = new BES8ThreadState("WaitOnCondition", objectWaitImage);
	public static BES8ThreadState STATE_OBJECT_WAIT = new BES8ThreadState("Wait", waitingImage);
	
	public static BES8ThreadState STATE_UNKNOWN = new BES8ThreadState("Unknown", unknownImage);
	
	public BES8ThreadState(String text, Image image){
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
		if (obj instanceof BES8ThreadState) {
			BES8ThreadState state = (BES8ThreadState) obj;
			return text.equals(state.getText());
		} else {
			return super.equals(obj);
		}
	}
	
	public static BES8ThreadState getStateFromTitle(String str) {
		if (str.contains("Object.wait()")) {
			return BES8ThreadState.STATE_OBJECT_WAIT;
		} else if (str.contains("waiting on condition")) {
			return BES8ThreadState.STATE_WAITING_ON_CONDITION;
		} else if (str.contains("runnable")) {
			return BES8ThreadState.STATE_RUNNABLE;
		}
		return STATE_UNKNOWN;
	}

}
