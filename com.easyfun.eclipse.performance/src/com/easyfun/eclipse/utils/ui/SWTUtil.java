package com.easyfun.eclipse.utils.ui;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * Help class to redesign the control UI.
 * 
 * @author linzhaoming
 * 
 */
public class SWTUtil {
	/**
	 * Hide or show the vertical scroll bar based on the text height within this text control. <br>
	 * 
	 * The text control should be created like this: <br>
	 * 
	 * <code>
	 * Text text = new Text(parent, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
	 * </code>
	 * 
	 * @param text A text control
	 */
	public static void autoHideVerticalScrollBar(Text text) {
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text text = (Text) e.widget;
				ScrollBar bar = text.getVerticalBar();
				if (bar != null) {
					int total = text.getLineCount() * text.getLineHeight();
					int limit = text.getSize().y - text.getBorderWidth() * 2;
					bar.setVisible(total > limit);
				}
			}
		});
		text.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Text text = (Text) e.widget;
				ScrollBar bar = text.getVerticalBar();
				if (bar != null) {
					int total = text.getLineCount() * text.getLineHeight();
					int limit = text.getSize().y - text.getBorderWidth() * 2;
					bar.setVisible(total > limit);
				}
			}
		});
	}
	
	public static void autoHideVerticalScrollBar(StyledText text) {
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				StyledText text = (StyledText) e.widget;
				ScrollBar bar = text.getVerticalBar();
				if (bar != null) {
					bar.setVisible(bar.getMaximum() > 1);
				}
			}
		});
		text.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				StyledText text = (StyledText) e.widget;
				ScrollBar bar = text.getVerticalBar();
				if (bar != null) {
					bar.setVisible(bar.getMaximum() > 1);
				}
			}
		});
	}	

	/**
	 * Get table height according to the rows
	 */
	public static int getTableHeightHint(Table table, int rows) {
		if (table.getFont().equals(JFaceResources.getDefaultFont()))
			table.setFont(JFaceResources.getDialogFont());
		int result = table.getItemHeight() * rows + table.getHeaderHeight();
		if (table.getLinesVisible())
			result += table.getGridLineWidth() * (rows - 1);
		return result;
	}

	/**
	 * Returns a width hint for a button control.
	 */
	public static int getButtonWidthHint(Button button) {
		button.setFont(JFaceResources.getDialogFont());
		PixelConverter converter = new PixelConverter(button);
		int widthHint = converter.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		return Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
	}

	/**
	 * Sets width and height hint for the button control. <b>Note:</b> This is a NOP if the
	 * button's layout data is not an instance of <code>GridData</code>.
	 * 
	 * @param button the button for which to set the dimension hint
	 */
	public static void setButtonDimensionHint(Button button) {
		Assert.isNotNull(button);
		Object gd = button.getLayoutData();
		if (gd instanceof GridData) {
			((GridData) gd).widthHint = getButtonWidthHint(button);
			((GridData) gd).horizontalAlignment = GridData.FILL;
		}
	}

	/**
	 * Add a spacer and set a height hint to it.
	 * 
	 * @param parent
	 * @param height
	 */
	public static void addSpacer(Composite parent, int height) {
		Label label = new Label(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = (height <= 0) ? 5 : height;
		label.setLayoutData(gd);
	}
	
	/**
	 * Set the font to bold style.
	 * 
	 * @param label
	 */
	public static void setBold(Label label) {
		label.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));
	}
	
	/**Get the Bold Font from JFace Registry.
	 * 
	 * <p> Note: Don't dispose the Font manually, just allocate to the Control.
	 * <p> Example: Label.setFont(SWTUtil.getBoldFont()).
	 * @return
	 */
	public static Font getBoldFont(){
		return JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);
	}
	
	/**
	 * Center the shell relative to parent. 
	 * <p> Depends on the parent of the shell.
	 *  <li>Not Null, center it relative to parent.</li>
	 *  <li>Null, center it relative to monitor.</li>
	 * 
	 * @param shell
	 */
	public static void centerShell(Shell shell){
		if(shell==null || shell.isDisposed()){
			return;
		}
		Composite parent=shell.getParent();
		Rectangle bounds=null;
		if(parent==null){
			bounds=shell.getDisplay().getBounds();
			return;
		}else{
			if(parent instanceof Shell){
				bounds=((Shell)parent).getBounds();
			}
		}
		if(bounds!=null){
			Rectangle rect = shell.getBounds ();
			int x = bounds.x + (bounds.width - rect.width) / 2;
			int y = bounds.y + (bounds.height - rect.height) / 2;
			shell.setLocation (x, y);
		}
	}
	
	public static void showMessage(Shell shell, String message) {
		if(shell == null){
			shell = IDEHelper.getActivePage().getActivePart().getSite().getShell();
		}
		MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.YES);
		box.setMessage(message);
		box.open();
	}

	public static boolean showQuestion(Shell shell, String message) {
		if(shell == null){
			shell = IDEHelper.getActivePage().getActivePart().getSite().getShell();
		}
		MessageBox box = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		box.setMessage(message);
		int ret = box.open();
		
		if (ret == SWT.YES){
			return true;
		}
		return false;
	}

	public static void showError(Shell shell, String message) {
		if(shell == null){
			shell = IDEHelper.getActivePage().getActivePart().getSite().getShell();
		}
		MessageBox box = new MessageBox(shell, SWT.ICON_ERROR | SWT.YES);
		box.setMessage(message);
		box.open();
	}
}
