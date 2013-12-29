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
//			if(navigator == null){
//				navigator = ConfigHelper.getNavigator();
//			}
//			Folder[] folders = navigator.getFolders();
			
			if (navigator == null) {
				// navigator = ConfigHelper.getNavigator();
				navigator = new Navigator();
			} else{
				return navigator;
			}
			
			//增加扩展点的
			Folder[] folders = loadContributeFolders(); 
			for (Folder folder : folders) {
				List<Item> items = null;
				try {
					items = folder.getItems();
				} catch (Exception e) {
					e.printStackTrace();
				}
				for (Item item : items) {					
					ItemWrapper pair = new ItemWrapper(item);
					maps.put(item, pair);
				}
			}
			
			for (Folder folder : folders) {
				if(("true").equalsIgnoreCase(folder.getVisible())){
					navigator.addFolder(folder);
					folder.setNavigator(navigator);
				}
			}
			
			return navigator;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Folder[] loadContributeFolders() {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		
		//找出所有的目录
		
		IConfigurationElement[] folderConfigList = reg.getConfigurationElementsFor(navigatorFolderExtension);
		
		List<Folder> folderList = new ArrayList<Folder>();
		Set<String> folderKeysSet = new TreeSet<String>();
		
		//处理目录
	    for (int i = 0; i < folderConfigList.length; i++){
	    	if(folderKeysSet.contains(folderConfigList[i].getAttribute("id"))){
	    		//重复ID不增加
	    		continue;
	    	}
	    	folderKeysSet.add(folderConfigList[i].getAttribute("id"));
//	    	
//	    	ConfigurationElementHandle hanlder = (ConfigurationElementHandle)folderConfigList[i];
//	    	hanlder.getContributor().
	    	
	    	Folder folder = new Folder();
	    	folder.setId(folderConfigList[i].getAttribute("id"));
	    	folder.setTitle(folderConfigList[i].getAttribute("title"));
	    	folder.setType(folderConfigList[i].getAttribute("type"));
	    	
	    	if(StringUtils.isNotEmpty(folderConfigList[i].getAttribute("index"))){
	    		folder.setIndex(Integer.parseInt(folderConfigList[i].getAttribute("index")));
	    	}else{
	    		folder.setIndex(-1);
	    	}
	    	
	    	String visible = folderConfigList[i].getAttribute("visible");
	    	if(StringUtils.equalsIgnoreCase("false", visible)){
	    		folder.setVisible("false");
	    	}else{
	    		folder.setVisible("true");
	    	}
	    	folderList.add(folder);
	    }
		
		
		//找出所有的目录节点
	    IConfigurationElement[] elementConfigList = reg.getConfigurationElementsFor(navigatorElementExtension);
	    List<Item> itemList = new ArrayList<Item>();
	    Set<String> itemKeysSet = new TreeSet<String>();
	    for (int i = 0; i < elementConfigList.length; i++){
	    	LogHelper.debug(null, elementConfigList[i].getAttribute("id"));
	    	if(itemKeysSet.contains(elementConfigList[i].getAttribute("id"))){
	    		//重复ID不增加
	    		continue;
	    	}
	    	itemKeysSet.add(elementConfigList[i].getAttribute("id"));
	    	
	    	Item item = new Item();
	    	item.setComposite(elementConfigList[i].getAttribute("compositeClass"));
	    	item.setHelper(elementConfigList[i].getAttribute("itemHelper"));
	    	item.setType(elementConfigList[i].getAttribute("type"));
	    	item.setTitle(elementConfigList[i].getAttribute("title"));
	    	
	    	if(StringUtils.isNotEmpty(elementConfigList[i].getAttribute("index"))){
	    		item.setIndex(Integer.parseInt(elementConfigList[i].getAttribute("index")));
	    	}else{
	    		item.setIndex(-1);
	    	}
	    	
	    	String visible = elementConfigList[i].getAttribute("visible");
	    	if(StringUtils.equalsIgnoreCase("false", visible)){
	    		item.setVisible("false");
	    	}else{
	    		item.setVisible("true");
	    	}
	    	
	    	item.setIcon(elementConfigList[i].getAttribute("icon"));
	    	String viewId = elementConfigList[i].getAttribute("viewId");
	    	item.setViewId(viewId);
	    	if(StringUtils.isNotEmpty(viewId)){
	    		item.setHelper(OpenViewItemHelper.class.getName());
	    	}
	    	
	    	
	    	String pluginId = elementConfigList[i].getDeclaringExtension().getNamespaceIdentifier();
	    	item.setPluginId(pluginId);
	    	
	    	String folderId = elementConfigList[i].getAttribute("folder");
	    	Folder belongFolder = null; //TODO: 加上默认，容错处理
	    	for(int j=0; j<folderList.size(); j++){
	    		if(StringUtils.equals(folderList.get(j).getId(), folderId)){
	    			belongFolder = folderList.get(j);
	    			item.setFolder(belongFolder);
	    			belongFolder.addItem(item);
	    			break;
	    		}
	    	}
	    	
	    	itemList.add(item);
	    }
	    
	    return folderList.toArray(new Folder[0]);
	}
	
	public static ItemWrapper getNavigatorByType(Item item){
		return maps.get(item);
	}
	
}
