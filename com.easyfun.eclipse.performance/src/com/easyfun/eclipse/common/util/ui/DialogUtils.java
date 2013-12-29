package com.easyfun.eclipse.common.util.ui;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class DialogUtils {
	
	public static final String MESSAGE_ERROR = "ERROR";
	public static final String MESSAGE_MESSAGE = "MESSAGE";
	public static final String MESSAGE_CONFIRM = "CONFIRM";
	
	public static void showMsg(Shell shell, String msg){
		MessageDialog.openInformation(shell, MESSAGE_MESSAGE, msg);
	}
	
	public static void showMsg(Shell shell, String msg, String title){
		MessageDialog.openInformation(shell, title, msg);
	}
	
	public static void showError(Shell shell, String msg){
		MessageDialog.openError(shell, MESSAGE_ERROR, msg);
	}
	
	public static boolean showConfirm(Shell shell, String msg){
		return MessageDialog.openQuestion(shell, MESSAGE_CONFIRM, msg);
	}
	
	public static boolean showConfirm(Shell shell, String msg, String title){
		return MessageDialog.openQuestion(shell, title, msg);
	}
	
	/**
	 * 打开文件保存对话框
	 * @param shell
	 * @param extensions 运行的文件扩展名，例子：new String[]{"*.txt", "*.*"}
	 * @param initFileName 初始的文件名 例子threadDump.txt
	 * @return 可以写入为返回的File，否则为null
	 */
	public static File openSaveDialog(Shell shell, String[] extensions, String initFileName){
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setFilterExtensions(extensions);
		dialog.setFileName(initFileName);
		String path = dialog.open();
		if(path == null){
			return null;
		}
		File file = new File(path);
		if(file.exists()){
			boolean result = DialogUtils.showConfirm(shell, "文件名已存在，确认覆盖?");
			if(result == false){
				return null;
			}
		}
		return file;
	}
}
