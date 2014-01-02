package com.easyfun.eclipse.performance.navigator.cfg.model;

/**
 * 代表导航树节点 Item/Folder扩展点定义，包括以下属性
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
public abstract class Node {
	private String id ;
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
	private Folder parentFolder;
	
	/** 定义Item扩展点所处的Plugin ID */
	private String pluginId;
	
	/** Folder扩展点属性 ["folder"]*/
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
