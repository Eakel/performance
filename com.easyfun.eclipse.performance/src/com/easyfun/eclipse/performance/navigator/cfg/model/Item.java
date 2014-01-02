package com.easyfun.eclipse.performance.navigator.cfg.model;

/**
 * ���������ڵ� Item��չ�㶨�壬������������
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
public class Item {
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
	private Folder folder;
	
	/** ����Item��չ��������Plugin ID */
	private String pluginId;
	

	/** Item��չ������ ["title"]*/
	public String getTitle() {
		return title;
	}

	/** Item��չ������ ["title"]*/
	public void setTitle(String title) {
		this.title = title;
	}

	/** Item��չ������ ["type"]*/
	public String getType() {
		return type;
	}

	/** Item��չ������ ["type"]*/
	public void setType(String type) {
		this.type = type;
	}

	/** Item��չ������ ["visible"]*/
	public boolean isVisible() {
		return visible;
	}

	/** Item��չ������ ["visible"]*/
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/** Item��չ������ ["folder"]*/
	public Folder getFolder() {
		return folder;
	}

	/** Item��չ������ ["folder"]*/
	public void setFolder(Folder foler) {
		this.folder = foler;
	}

	/** Item��չ������ ["icon"]*/
	public String getIcon() {
		return icon;
	}

	/** Item��չ������ ["icon"]*/
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/** Item��չ������ ["viewId"]*/
	public String getViewId() {
		return viewId;
	}
	
	/** Item��չ������ ["viewId"]*/
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	/** ����Item��չ��������Plugin ID */
	public String getPluginId() {
		return pluginId;
	}

	/** ����Item��չ��������Plugin ID */
	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	/** Item��չ������ ["index"]*/
	public int getIndex() {
		return index;
	}

	/** Item��չ������ ["index"]*/
	public void setIndex(int index) {
		this.index = index;
	}
	
	public String toString() {
		return this.title;
	}
	
}
