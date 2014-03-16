package com.easyfun.eclipse.component.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.easyfun.eclipse.component.sql.ui.DBFolder;
import com.easyfun.eclipse.component.sql.ui.DBNavigator;
import com.easyfun.eclipse.component.sql.ui.FolderType;

public class TreeHelper {
	public static DBNavigator getNavigator(){
		DBNavigator navigator = new DBNavigator();
		Connection conn = null;
		try {
			conn = DBHelper.getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet rs = metaData.getCatalogs();
			while(rs.next()){
				DBFolder folder = new DBFolder();
				folder.setTitle(rs.getString(1));
				folder.setVisible(true);
				folder.setFolderType(FolderType.Schema);
				navigator.addFolder(folder);
				handleDBFolder(folder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return navigator;
	}
	
	private static void handleDBFolder(DBFolder dbFolder){
		DBFolder folder = new DBFolder();
		folder.setTitle("Tables");
		folder.setVisible(true);
		folder.setParentFolder(dbFolder);
		folder.setFolderType(FolderType.DIR_Tables);
		dbFolder.addNode(folder);
		
		folder = new DBFolder();
		folder.setTitle("Views");
		folder.setVisible(true);
		folder.setParentFolder(dbFolder);
		folder.setFolderType(FolderType.DIR_Views);
		dbFolder.addNode(folder);
		
		folder = new DBFolder();
		folder.setTitle("Stored Procs");
		folder.setVisible(true);
		folder.setParentFolder(dbFolder);
		folder.setFolderType(FolderType.DIR_StoreProcs);
		dbFolder.addNode(folder);
		
		folder = new DBFolder();
		folder.setTitle("Functions");
		folder.setVisible(true);
		folder.setParentFolder(dbFolder);
		folder.setFolderType(FolderType.DIR_Functions);
		dbFolder.addNode(folder);
		
		folder = new DBFolder();
		folder.setTitle("Triggers");
		folder.setVisible(true);
		folder.setParentFolder(dbFolder);
		folder.setFolderType(FolderType.DIR_Triggers);
		dbFolder.addNode(folder);
		
		folder = new DBFolder();
		folder.setTitle("Events");
		folder.setVisible(true);
		folder.setParentFolder(dbFolder);
		folder.setFolderType(FolderType.DIR_Events);
		dbFolder.addNode(folder);
	}
	
	public static void handleDBItem(DBFolder dbFolder){
		DBFolder folder = new DBFolder();
		folder.setTitle("Columns");
		folder.setVisible(true);
		folder.setParentFolder(dbFolder);
		folder.setFolderType(FolderType.Columns);
		dbFolder.addNode(folder);
		
		folder = new DBFolder();
		folder.setTitle("Indexes");
		folder.setVisible(true);
		folder.setParentFolder(dbFolder);
		folder.setFolderType(FolderType.Indexes);
		dbFolder.addNode(folder);
	}
}
