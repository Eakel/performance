package com.easyfun.eclipse.performance.mysql.analyze.jdbc;
//package com.easyfun.eclipse.component.oracle.jdbc;
//
///**
// * 连接工厂类
// * 
// * @author linzhaoming
// * Create Date: 2010-11-27
// */
//public class ConnectionFactory {
//	private static ConnectionModel model = null;
//	private static boolean isInitial = false;
//	
//	public static ConnectionModel getConnectionModel(){
//		if(isInitial == false){
//			initConnectionModel();
//			isInitial = true;
//		}
//		return model;
//	}
//	
//	public static void initConnectionModel(){
//		model = new ConnectionModel();
//	}
//	
//	public static void refreshConnectionModel(String driver, String name, String password,
//			String url){
//		model = new ConnectionModel(driver, name, password, url);
//	}
//}
