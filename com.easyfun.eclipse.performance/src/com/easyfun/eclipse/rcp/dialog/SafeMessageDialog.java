package com.easyfun.eclipse.rcp.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author linzhaoming
 *
 */
public class SafeMessageDialog {

	/**
	 * Opens the Question dialog in a runnable, keeps track of the return type
	 */
	private static class QuestionDialogRunnable implements Runnable {
		boolean result = false;
		int iResult = 0;
		private String title = "";
		private String message = "";
		private String[] buttonNames = null;
		int defaultButtonIndex = 0;
		
		public QuestionDialogRunnable(String title, String messasge){
			this(title, messasge, null, 0);
		}
		
		public QuestionDialogRunnable(String title, String messasge, String[] buttonNames, int defaultButtonIndex){
			this.title = title;
			this.message = messasge;
			this.buttonNames = buttonNames;
			this.defaultButtonIndex = defaultButtonIndex;
		}		 

		public void run() {
			if (buttonNames == null) {
				result = MessageDialog.openQuestion(getActiveShell(), title, message);
			} else {
				MessageDialog messageDialog = new MessageDialog(getActiveShell(), title, null,
						message, MessageDialog.QUESTION, buttonNames, defaultButtonIndex);
				iResult = messageDialog.open();
			}
		}
	};
	
	private static class QuestionDialogWithToggleRunnable implements Runnable{
		private int iResult = 0;
		private String titleFinal = "";
		private String messageFinal = "";		
		private IPreferenceStore store = null;
		private String key = null;		
		
		public QuestionDialogWithToggleRunnable(String title, String message, IPreferenceStore store, String key){
			this.titleFinal = title;
			this.messageFinal = message;
			this.store = store;
			this.key = key;
		}		
		
		public void run(){
			MessageDialogWithToggle dialog = MessageDialogWithToggle
            .openYesNoQuestion(getActiveShell(), titleFinal, messageFinal,  null, false , store, key);
			iResult = dialog.getReturnCode();			
		}	
	};

	/**
	 * Opens the Question dialog in a runnable, keeps track of the return type
	 */
	private static class InputDialogRunnable implements Runnable {
		private boolean bResult = false;
		private String value = "";
		private String title = "";
		private String message = "";
		
		public InputDialogRunnable(String title, String message, String value){
			this.value = value;
			this.message = message;
			this.title = title;
		}

		public void run() {
			InputDialog inputDialog = new InputDialog(getActiveShell(), title, message, value, null);
			bResult = inputDialog.open() == InputDialog.OK;
			value = inputDialog.getValue();
		}
	};
	
	/**
	 * 
	 * Opens the border layout manager dialog in a runnable, keeps track of the return type
	 */
	private static class BorderRunnable implements Runnable {
		private String layoutPosition = "";
		private String titleFinal = "";
		private String messageFinal = "";
		private String[] layoutPositions = null;
		
		public BorderRunnable(String title, String message, String[] layoutPositions){
			this.titleFinal = title;
			this.messageFinal = message;
			this.layoutPositions = layoutPositions;
		}

		public void run() {
			BorderLayoutPositionDialog borderLayoutPositionDialog = new BorderLayoutPositionDialog(
					getActiveShell(), titleFinal, messageFinal);

			borderLayoutPositionDialog.layoutPositionStrings = layoutPositions;
			// open the dialog and ask uer to pick the layout position
			int iResult = borderLayoutPositionDialog.open();
			if (iResult == BorderLayoutPositionDialog.OK) {
				layoutPosition = borderLayoutPositionDialog.value;
			}
		}
	};
	
	/**
	 * Opens the border layout manager dialog in a runnable, keeps track of the return type
	 */
	private static class BorderLayoutPositionDialog extends Dialog{
		private static final String BORDER_LAYOUT_DIALOG = "Border Layout Position";

		/** The message to display, or <code>null</code> if none. */
		private String message = null;
		
		/** The input value; the empty string by default.  */		
		private String value = "";//$NON-NLS-1$

		/** Layout Positions combo widget. */
		private Combo layoutPositions = null;

		String[] layoutPositionStrings = null;

		public BorderLayoutPositionDialog(Shell parentShell, String dialogTitle, String dialogMessage) {
			super(parentShell);
			message = dialogMessage;
		}

		protected void configureShell(Shell shell) {
			super.configureShell(shell);
			shell.setText(BORDER_LAYOUT_DIALOG);
		}

		protected Control createDialogArea(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			GridLayout layout = new GridLayout();
			layout.makeColumnsEqualWidth = true;
			layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
			layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
			layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
			layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
			composite.setLayout(layout);
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));
			
			if (message != null) {
				Label label = new Label(composite, SWT.WRAP);
				label.setText(message);
				GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
						| GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
				data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
				label.setLayoutData(data);
				label.setFont(parent.getFont());
			}
			
			layoutPositions = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
			layoutPositions.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
			layoutPositions.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					value = layoutPositions.getText();					
				}
				public void widgetDefaultSelected(SelectionEvent e) {
					value = layoutPositions.getText();					
				}				
			});
					
			//Add all the layout positions
			for (int index = 0; index < layoutPositionStrings.length; index++){
				layoutPositions.add(layoutPositionStrings[index]);
			}
			
			//Set the default value as the first string.
			value = layoutPositionStrings[0];					
			
			//Select the first position
			layoutPositions.select(0);
			
			return composite;
		}		
	};

	/**
	 * Thread Safely opens and error message dialog
	 * @param title
	 * @param message
	 */
	public static void errorMessage(final String title, final String message){
		Display.getDefault().syncExec(new Runnable(){	
			public void run(){
				MessageDialog.openError(getActiveShell(), title, message);
			}
		});
	}
	
	/**
	 * Thread Safely opens and inforamtion message dialog.
	 * 
	 * @param title
	 * @param message
	 */
	public static void informationMessage(final String title, final String message){
		Display.getDefault().syncExec(new Runnable(){	
			public void run(){
				MessageDialog.openInformation(getActiveShell(), title, message);
			}
		});
	}

	/**
	 * Thread Safely opens and information message dialog
	 * @param title

	 * @param message
	 */
	public static boolean questionMessage(String title, String message) {
		QuestionDialogRunnable questionDialog = new QuestionDialogRunnable(title, message);
		Display.getDefault().syncExec(questionDialog);
		return questionDialog.result;
	}
	
	/**
	 * Thread Safely opens and information message dialog
	 * @param title
	 * @param message
	 * @param defaultButtonIndex
	 */
	public static int questionMessage(String title, String message, String[] buttonNames, int defaultButtonIndex) {
		QuestionDialogRunnable questionDialog = new QuestionDialogRunnable(title, message, buttonNames, defaultButtonIndex);
		Display.getDefault().syncExec(questionDialog);
		return questionDialog.iResult;
	}
	
	/**
	 * Thread Safely opens and information message dialog
	 * @param title
	 * @param message
	 * @param defaultButtonIndex
	 */
	public static int questionMessageWithToggle(String title, String message, IPreferenceStore store, String key) {
		QuestionDialogWithToggleRunnable questionDialogWithToggle = new QuestionDialogWithToggleRunnable(
				title, message, store, key);
		Display.getDefault().syncExec(questionDialogWithToggle);
		return questionDialogWithToggle.iResult;
	}

	/**
	 * Thread Safely opens and information message dialog
	 * @param title

	* @param message
	 */
	public static String inputMessage(String title, String message, String value) {
		InputDialogRunnable inputDialog = new InputDialogRunnable(title,message, value);
		Display.getDefault().syncExec(inputDialog);
		return (inputDialog.bResult == true ? inputDialog.value : null);
	}
	
	/**
	 * Thread Safely opens and error message dialog Asynchronously 
	 * @param title
	 * @param message
	 */
	public static void asyncErrorMessage(final String title, final String message){
		Display.getDefault().asyncExec(new Runnable(){	
			public void run(){
				MessageDialog.openError(getActiveShell(), title, message);
			}
		});
	}
	
	/**
	 * Thread Safely opens an information message dialog Asynchronously 
	 * @param title
	 * @param message
	 */
	public static void asyncInformationMessage(final String title, final String message){
		Display.getDefault().asyncExec(new Runnable(){	
			public void run(){
				MessageDialog.openInformation(getActiveShell(), title, message);
			}
		});
	}
	
	/**
	 * Thread Safely opens an information message dialog
	 * @param title

	 * @param message
	 */
	public static String asyncInputMessage(String title, String message, String value) {
		InputDialogRunnable inputDialog = new InputDialogRunnable(title, message, value);
		Display.getDefault().asyncExec(inputDialog);
		return (inputDialog.bResult ? inputDialog.value : null);
	}

	public static String borderPlacement(final String title, final String message, final String[] layoutPositions) {
		BorderRunnable borderRunnable = new BorderRunnable(title, message, layoutPositions);
		Display.getDefault().syncExec(borderRunnable);
		return borderRunnable.layoutPosition;
	}
	
	private static Shell getActiveShell(){
		return Display.getCurrent().getActiveShell();
	}
}
