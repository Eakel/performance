package com.easyfun.eclipse.performance.navigator.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.easyfun.eclipse.performance.navigator.cfg.model.Folder;
import com.easyfun.eclipse.performance.navigator.cfg.model.Item;
import com.easyfun.eclipse.performance.navigator.cfg.model.ItemWrapper;
import com.easyfun.eclipse.performance.navigator.cfg.model.Navigator;
import com.easyfun.eclipse.performance.navigator.console.LogHelper;

/**
 * 导航树助手，从配置config.xml获取数据
 * 
 * @author linzhaoming
 *
 * 2011-5-8
 *
 */
public class DefaultItemProvider{
	
	private static Navigator navigator = null;
	
	private static HashMap<Item, ItemWrapper> maps = new HashMap<Item, ItemWrapper>();
	
	/** 导航树[目录]扩展点*/
	private static final String navigatorFolderExtension = "com.easyfun.eclipse.performance.EasyFunNavigatorFolder";
	/** 导航树[节点]扩展点*/
	private static final String navigatorElementExtension = "com.easyfun.eclipse.performance.EasyFunNavigatorElement";
	
	public static Navigator getNavigator(){
		try {
			if (navigator == null) {
				navigator = new Navigator();
			} else{
				return navigator;
			}
			
			//增加扩展点的
			loadContributeFolders(navigator); 
			
			return navigator;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** 加载所有Folder扩展点定义*/
	private static Folder[] loadContributeFolders(Navigator navigator) {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		
		//找出所有的目录
		
		IConfigurationElement[] folderConfigList = reg.getConfigurationElementsFor(navigatorFolderExtension);
		
		List<Folder> rootFolderList = new ArrayList<Folder>();
		Set<String> folderKeysSet = new TreeSet<String>();
		
		List<Folder> otherFolders = new ArrayList<Folder>();
		
		//处理所有的Folder目录扩展点
	    for (int i = 0; i < folderConfigList.length; i++){
	    	IConfigurationElement folderElement = folderConfigList[i];
	    	if(folderKeysSet.contains(folderElement.getAttribute("id"))){
	    		continue;	//重复ID不增加
	    	}
	    	folderKeysSet.add(folderElement.getAttribute("id"));
	    	
	    	Folder folder = new Folder();
	    	folder.setId(folderElement.getAttribute("id"));
	    	folder.setTitle(folderElement.getAttribute("title"));
	    	folder.setType(folderElement.getAttribute("type"));
	    	folder.setParentFolderId(folderElement.getAttribute("folder"));
	    	
	    	if(StringUtils.isNotEmpty(folderElement.getAttribute("index"))){
	    		folder.setIndex(Integer.parseInt(folderElement.getAttribute("index")));
	    	}else{
	    		folder.setIndex(-1);
	    	}
	    	
	    	String visible = folderElement.getAttribute("visible");
	    	if(StringUtils.equalsIgnoreCase("false", visible)){
	    		folder.setVisible(false);
	    	}else{
	    		folder.setVisible(true);
	    	}
	    	
	    	if(StringUtils.isNotEmpty(folder.getParentFolderId())){
	    		otherFolders.add(folder);
	    	}else{
	    		rootFolderList.add(folder);	  
	    		folder.setNavigator(navigator);
	    		navigator.addFolder(folder);
	    	}
	    }
	    //处理所有的非根Folder
	    for (Folder folder : otherFolders) {
			String parentFolderId = folder.getParentFolderId();
			for (Folder rootFolder : rootFolderList) {
				if(StringUtils.equals(rootFolder.getId(), parentFolderId)){
					folder.setParentFolder(rootFolder);
					rootFolder.addNode(folder);
				}
			}
		}
	    
		
	  //处理所有的Item节点扩展点
	    IConfigurationElement[] elementConfigList = reg.getConfigurationElementsFor(navigatorElementExtension);
	    List<Item> itemList = new ArrayList<Item>();
	    Set<String> itemKeysSet = new TreeSet<String>();
	    for (int i = 0; i < elementConfigList.length; i++){
	    	IConfigurationElement itemElement = elementConfigList[i];
	    	LogHelper.debug(null, itemElement.getAttribute("id"));
	    	if(itemKeysSet.contains(itemElement.getAttribute("id"))){
	    		continue;	//重复ID不增加
	    	}
	    	itemKeysSet.add(itemElement.getAttribute("id"));
	    	
	    	Item item = new Item();
	    	item.setType(itemElement.getAttribute("type"));
	    	item.setTitle(itemElement.getAttribute("title"));
	    	
	    	if(StringUtils.isNotEmpty(itemElement.getAttribute("index"))){
	    		item.setIndex(Integer.parseInt(itemElement.getAttribute("index")));
	    	}else{
	    		item.setIndex(-1);
	    	}
	    	
	    	String visible = itemElement.getAttribute("visible");
	    	if(StringUtils.equalsIgnoreCase("false", visible)){
	    		item.setVisible(false);
	    	}else{
	    		item.setVisible(true);
	    	}
	    	
	    	item.setIcon(itemElement.getAttribute("icon"));
	    	String viewId = itemElement.getAttribute("viewId");
	    	item.setViewId(viewId);
	    	
	    	String pluginId = itemElement.getDeclaringExtension().getNamespaceIdentifier();
	    	item.setPluginId(pluginId);
	    	
	    	String folderId = itemElement.getAttribute("folder");
	    	
	    	//从根节点开始查找
	    	Folder belongFolder = null;
	    	for(int j=0; j<rootFolderList.size(); j++){
	    		if(StringUtils.equals(rootFolderList.get(j).getId(), folderId)){
	    			belongFolder = rootFolderList.get(j);
	    			item.setParentFolder(belongFolder);
	    			belongFolder.addNode(item);
	    			break;
	    		}
	    	}
	    	
	    	//从非根节点开始查找
	    	belongFolder = null;
			if (belongFolder == null) {
				for (int j = 0; j < otherFolders.size(); j++) {
					if (StringUtils.equals(otherFolders.get(j).getId(), folderId)) {
						belongFolder = otherFolders.get(j);
						item.setParentFolder(belongFolder);
						belongFolder.addNode(item);
						break;
					}
				}
			}
	    	
	    	itemList.add(item);
	    	
	    	ItemWrapper pair = new ItemWrapper(item);
			maps.put(item, pair);
			
			navigator.addtem(item);
	    }
	    
	    return rootFolderList.toArray(new Folder[0]);
	}
	
	public static ItemWrapper getNavigatorByType(Item item){
		return maps.get(item);
	}
	
}
