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
	 * ���ļ�����Ի���
	 * @param shell
	 * @param extensions ���е��ļ���չ�������ӣ�new String[]{"*.txt", "*.*"}
	 * @param initFileName ��ʼ���ļ��� ����threadDump.txt
	 * @return ����д��Ϊ���ص�File������Ϊnull
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
			boolean result = DialogUtils.showConfirm(shell, "�ļ����Ѵ��ڣ�ȷ�ϸ���?");
			if(result == false){
				return null;
			}
		}
		return file;
	}
}
