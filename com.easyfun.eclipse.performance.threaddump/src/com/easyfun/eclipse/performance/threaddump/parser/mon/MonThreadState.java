package com.easyfun.eclipse.performance.threaddump.parser.mon;

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
public class MonThreadState implements IThreadState{
	public String text;
	private Image image;
	
	private static Image unkwnownImage = ThreadDumpActivator.getImageDescriptor(ImageConstants.ICON_THREAD_DEFAULT).createImage();
	private static Image objectWaitImage = ThreadDumpActivator.getImageDescriptor(ImageConstants.ICON_THREAD_WAITING).createImage();	
	private static Image waitingImage = ThreadDumpActivator.getImageDescriptor(ImageConstants.ICON_THREAD_WAIT_ON_CONDITION).createImage();
	private static Image runnableImage = ThreadDumpActivator.getImageDescriptor(ImageConstants.ICON_THREAD_RUNNABLE).createImage();
	
	public static MonThreadState STATE_RUNNABLE = new MonThreadState("RUNNABLE", runnableImage);	
	public static MonThreadState STATE_WAITING_ON_CONDITION = new MonThreadState("TIMED_WAITING", objectWaitImage);
	public static MonThreadState STATE_OBJECT_WAIT = new MonThreadState("WAITING", waitingImage);
	public static MonThreadState STATE_BLOCK = new MonThreadState("BLOCKED", waitingImage); // TODO
	
	public static MonThreadState STATE_UNKNOWN = new MonThreadState("Unknown", unkwnownImage);
	
	public MonThreadState(String text, Image image){
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
		if (obj instanceof MonThreadState) {
			MonThreadState state = (MonThreadState) obj;
			return text.equals(state.getText());
		} else {
			return super.equals(obj);
		}
	}

	public static MonThreadState getStateFromTitle(String str) {
		if(str.contains("WAITING")){
			return MonThreadState.STATE_OBJECT_WAIT;
		}else if(str.contains("TIMED_WAITING")){
			return MonThreadState.STATE_WAITING_ON_CONDITION;
		}else if(str.contains("RUNNABLE")){
			return MonThreadState.STATE_RUNNABLE;
		}else if(str.contains("BLOCKED")){
			return MonThreadState.STATE_BLOCK;
		}	
		
		return MonThreadState.STATE_UNKNOWN;
	}
}
