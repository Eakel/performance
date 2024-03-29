package com.easyfun.eclipse.performance.navigator.cfg;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.digester.Digester;

import com.easyfun.eclipse.performance.navigator.cfg.model.Folder;
import com.easyfun.eclipse.performance.navigator.cfg.model.Item;
import com.easyfun.eclipse.performance.navigator.cfg.model.Navigator;
import com.easyfun.eclipse.performance.navigator.cfg.model.Node;
import com.easyfun.eclipse.performance.navigator.console.LogHelper;


/**
 * 配置文件config.xml读取
 * @author linzhaoming
 *
 * 2011-5-8
 *
 */
public class ConfigHelper {
	
	public static Navigator getNavigator() throws Exception{
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.xml");
		Digester digester = new Digester();
		digester.setValidating(false);
		
		
		digester.addObjectCreate("navigator", Navigator.class.getName());
		digester.addSetProperties("navigator");

		digester.addObjectCreate("navigator/folder", Folder.class.getName());
		digester.addSetProperties("navigator/folder");

		digester.addObjectCreate("navigator/folder/item", Item.class.getName());
		digester.addSetProperties("navigator/folder/item");
		
		digester.addSetNext("navigator/folder", "addFolder", Folder.class.getName());
		digester.addSetNext("navigator/folder/item", "addItem", Item.class.getName());

		//digester.addSetNext("navigator/folder", "setNavigator", Navigator.class.getName());
		//digester.addSetNext("navigator/folder/item", "setFolder", Folder.class.getName());

		Navigator navigator = (Navigator) digester.parse(input);
		
		//TODO:父子关系，可以自动解析的，有点问题，先人工设定
		List<Folder> folders = navigator.getFolders();
		for (Folder folder : folders) {
			List<Item> noes = folder.getItemNodes();
			for (Item item : noes) {
				if(item instanceof Item){
					item.setParentFolder(folder);
				}
			}
			folder.setNavigator(navigator);
		}
		
		return navigator;
	}
	
	public static void main(String[] args) throws Exception{
		Navigator navigator = getNavigator();
		LogHelper.info(null, navigator.toString());
	}
}
