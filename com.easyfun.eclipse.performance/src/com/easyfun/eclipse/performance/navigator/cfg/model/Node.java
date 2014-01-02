package com.easyfun.eclipse.performance.navigator.cfg.model;

/**
 * ���������ڵ� Item/Folder��չ�㶨�壬������������
 * <li>title ��ʾ�ڵ������ϵ�����
 * <li>type	����
 * <li>visible �Ƿ���ӣ�Ϊfalse������(���Դ�Сд)������������
 * <li>icon	icon
 * <li>viewId	viewId
 * <li>index	index
 * <li>folder	���ڵ��Folder ID
 * 
 * @author linzhaoming
 *
 * 2013-12-1
 *
 */
public abstract class Node {
	private String id ;
	/** Item��չ������ ["title"] */
	private String title;
	/** Item��չ������ ["type"] */
	private String type;
	/** Item��չ������ ["visible"] */
	private boolean visible;
	/** Item��չ������ ["icon"] */
	private String icon;
	/** Item��չ������ ["viewId"] */
	private String viewId;
	/** Item��չ������ ["index"] */
	private int index;
	/** Item��չ������ ["folder"] */
	private Folder parentFolder;
	
	/** ����Item��չ��������Plugin ID */
	private String pluginId;
	
	/** Folder��չ������ ["folder"]*/
	private String parentFolderId;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Folder getParentFolder() {
		return parentFolder;
	}

	public void setParentFolder(Folder parentFolder) {
		this.parentFolder = parentFolder;
	}
	
	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}
	
	public String getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(String parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	public abstract boolean isFolder();

}
