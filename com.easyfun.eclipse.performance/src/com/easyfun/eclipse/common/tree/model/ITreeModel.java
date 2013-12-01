package com.easyfun.eclipse.common.tree.model;

import java.util.Collection;


/**
 * ���ڵ�
 * @author linzhaoming
 * Create Date: 2010-8-12
 */
public interface ITreeModel<T> {
	
	/** ��ȡ���ӽڵ� */
	public Collection getChildren();
	
	/** ��ȡ���ڵ�*/
	public ITreeModel getParent();
	
	/** ��ȡ����ʵ�ʶ���*/
	public Object getInstance();
	
	/** �Ƿ�ΪĿ¼�ڵ�*/
	public boolean isDirectory();
	
	/** ��ȡ��ʾ�ڵ�����*/
	public String getDisplayName();
	
	public T getType();
	
}
