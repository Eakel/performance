package com.easyfun.eclipse.component.sql.ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.dbutils.DbUtils;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.easyfun.eclipse.component.sql.DBHelper;
import com.easyfun.eclipse.component.sql.TreeHelper;


/**
 * µ¼º½Ê÷ContentProvider
 * @author linzhaoming
 *
 * 2013-12-2
 *
 */
public class DBContentProvider implements IStructuredContentProvider, ITreeContentProvider {

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		return getChildren(parent);
	}

	public Object getParent(Object child) {
		if (child instanceof DBItem) {
			return ((DBItem) child).getParentFolder();
		}
		return null;
	}

	public Object[] getChildren(Object parent) {
		if(parent instanceof DBNavigator){
			return ((DBNavigator)parent).getFolders().toArray();
		}
		if (parent instanceof DBFolder) {
			DBFolder folder = (DBFolder)parent;
			if(folder.getFolderType() == FolderType.DIR_Tables){
				String schema = folder.getParentFolder().getTitle();
				Connection connection= null;
				PreparedStatement pstm = null;
				ResultSet rs = null;
				try {
					connection = DBHelper.getConnection();
					pstm = connection.prepareStatement("select table_name from information_schema.Tables where table_schema=?");
					pstm.setString(1, schema);
					rs = pstm.executeQuery();
					while(rs.next()){
						DBFolder tableFolder = new DBFolder();
						tableFolder.setTitle(rs.getString(1));
						tableFolder.setVisible(true);
						tableFolder.setFolderType(FolderType.Table);
						tableFolder.setParentFolder(folder);
						folder.addNode(tableFolder);
						
						TreeHelper.handleDBItem(tableFolder);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					DbUtils.closeQuietly(connection, pstm, rs);
				}
			}else if(folder.getFolderType() == FolderType.Columns){
				String tableName = folder.getParentFolder().getTitle();
				String schema =  folder.getParentFolder().getParentFolder().getParentFolder().getTitle();
				
				Connection connection= null;
				PreparedStatement pstm = null;
				ResultSet rs = null;
				try {
					connection = DBHelper.getConnection();
					pstm = connection.prepareStatement("select column_name, column_type from information_schema.Columns where table_schema=? and table_name=?");
					pstm.setString(1, schema);
					pstm.setString(2, tableName);
					rs = pstm.executeQuery();
					while(rs.next()){
						DBItem item = new DBItem();
						item.setTitle(rs.getString(1) + ", " + rs.getString(2));
						item.setVisible(true);
						item.setParentFolder(folder);
						folder.addNode(item);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					DbUtils.closeQuietly(connection, pstm, rs);
				}
			}else if(folder.getFolderType() == FolderType.Indexes){
				String tableName = folder.getParentFolder().getTitle();
				String schema =  folder.getParentFolder().getParentFolder().getParentFolder().getTitle();
				
				Connection connection= null;
				PreparedStatement pstm = null;
				ResultSet rs = null;
				try {
					connection = DBHelper.getConnection();
					pstm = connection.prepareStatement("select index_name, column_name from information_schema.Statistics where table_schema=? and table_name=?");
					pstm.setString(1, schema);
					pstm.setString(2, tableName);
					rs = pstm.executeQuery();
					while(rs.next()){
						DBItem item = new DBItem();
						item.setTitle(rs.getString(1) + " (" + rs.getString(2) + ")");
						item.setVisible(true);
						item.setParentFolder(folder);
						folder.addNode(item);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					DbUtils.closeQuietly(connection, pstm, rs);
				}
			}
			
			return ((DBFolder) parent).getNodes().toArray();
		}
		return new Object[0];
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof DBFolder) {
//			DBFolder folder = (DBFolder)parent;
//			if(folder.getFolderType() == FolderType.DIR_Tables){
//				return true;
//			}
//			
//			if(folder.getFolderType() == FolderType.Columns){
//				return true;
//			}
//			return ((DBFolder) parent).getNodes().size() != 0;
			return true;
		}
		return false;
	}
}