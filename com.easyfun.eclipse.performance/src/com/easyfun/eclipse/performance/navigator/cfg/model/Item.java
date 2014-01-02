package com.easyfun.eclipse.performance.navigator.cfg.model;

/**
 * 代表导航树节点 Item扩展点定义，包括以下属性
 * <li>title 显示在导航树上的名称
 * <li>type	类型
 * <li>visible 是否可视，为false不可视(忽略大小写)，其它都可视
 * <li>icon	icon
 * <li>viewId	viewId
 * <li>index	index
 * <li>folder	父节点的Folder ID
 * 
 * @author linzhaoming
 *
 * 2013-12-1
 *
 */
public class Item {
	/** Item扩展点属性 ["title"] */
	private String title;
	/** Item扩展点属性 ["type"] */
	private String type;
	/** Item扩展点属性 ["visible"] */
	private boolean visible;
	/** Item扩展点属性 ["icon"] */
	private String icon;
	/** Item扩展点属性 ["viewId"] */
	private String viewId;
	/** Item扩展点属性 ["index"] */
	private int index;
	/** Item扩展点属性 ["folder"] */
	private Folder folder;
	
	/** 定义Item扩展点所处的Plugin ID */
	private String pluginId;
	

	/** Item扩展点属性 ["title"]*/
	public String getTitle() {
		return title;
	}

	/** Item扩展点属性 ["title"]*/
	public void setTitle(String title) {
		this.title = title;
	}

	/** Item扩展点属性 ["type"]*/
	public String getType() {
		return type;
	}

	/** Item扩展点属性 ["type"]*/
	public void setType(String type) {
		this.type = type;
	}

	/** Item扩展点属性 ["visible"]*/
	public boolean isVisible() {
		return visible;
	}

	/** Item扩展点属性 ["visible"]*/
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/** Item扩展点属性 ["folder"]*/
	public Folder getFolder() {
		return folder;
	}

	/** Item扩展点属性 ["folder"]*/
	public void setFolder(Folder foler) {
		this.folder = foler;
	}

	/** Item扩展点属性 ["icon"]*/
	public String getIcon() {
		return icon;
	}

	/** Item扩展点属性 ["icon"]*/
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/** Item扩展点属性 ["viewId"]*/
	public String getViewId() {
		return viewId;
	}
	
	/** Item扩展点属性 ["viewId"]*/
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	/** 定义Item扩展点所处的Plugin ID */
	public String getPluginId() {
		return pluginId;
	}

	/** 定义Item扩展点所处的Plugin ID */
	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	/** Item扩展点属性 ["index"]*/
	public int getIndex() {
		return index;
	}

	/** Item扩展点属性 ["index"]*/
	public void setIndex(int index) {
		this.index = index;
	}
	
	public String toString() {
		return this.title;
	}
	
}
