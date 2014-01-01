package com.easyfun.eclipse.performance.threaddump.parser.webloigc;

import org.eclipse.swt.graphics.Image;

import com.easyfun.eclipse.performance.threaddump.ThreadDumpImageConstants;
import com.easyfun.eclipse.performance.threaddump.ThreadDumpActivator;
import com.easyfun.eclipse.performance.threaddump.parser.IThreadState;


/**
 * 代表线程的状态(Default)
 * @author linzhaoming
 *
 * 2013-11-17
 *
 */
public class WebLogicThreadState implements IThreadState{
	public String text;
	private Image image;
	
	private static Image unknownImage = ThreadDumpActivator.getImageDescriptor(ThreadDumpImageConstants.ICON_THREAD_DEFAULT_PATH).createImage();
	private static Image objectWaitImage = ThreadDumpActivator.getImageDescriptor(ThreadDumpImageConstants.ICON_THREAD_WAITING_PATH).createImage();	
	private static Image waitingImage = ThreadDumpActivator.getImageDescriptor(ThreadDumpImageConstants.ICON_THREAD_WAIT_ON_CONDITION_PATH).createImage();
	private static Image runnableImage = ThreadDumpActivator.getImageDescriptor(ThreadDumpImageConstants.ICON_THREAD_RUNNABLE_PATH).createImage();
	
	
	public static WebLogicThreadState STATE_RUNNABLE = new WebLogicThreadState("Runnable", runnableImage);
	public static WebLogicThreadState STATE_WAITING_ON_CONDITION = new WebLogicThreadState("WaitOnCondition", objectWaitImage);
	public static WebLogicThreadState STATE_OBJECT_WAIT = new WebLogicThreadState("Wait", waitingImage);
	
	public static WebLogicThreadState STATE_UNKNOWN = new WebLogicThreadState("Unknown", unknownImage);
	
	public WebLogicThreadState(String text, Image image){
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
		if (obj instanceof WebLogicThreadState) {
			WebLogicThreadState state = (WebLogicThreadState) obj;
			return text.equals(state.getText());
		} else {
			return super.equals(obj);
		}
	}

	public static WebLogicThreadState getStateFromTitle(String str) {
		if(str.contains("Object.wait()")){
			return WebLogicThreadState.STATE_OBJECT_WAIT;
		}else if(str.contains("WAITING")){
			return  WebLogicThreadState.STATE_WAITING_ON_CONDITION;
		} else if(str.contains("waiting")){	//waiting for lock
			return WebLogicThreadState.STATE_WAITING_ON_CONDITION;
		}else if(str.contains("RUNNABLE")){
			return WebLogicThreadState.STATE_RUNNABLE;
		}	
		return WebLogicThreadState.STATE_UNKNOWN;
	}
}
