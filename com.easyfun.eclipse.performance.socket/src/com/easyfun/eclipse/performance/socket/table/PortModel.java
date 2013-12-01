package com.easyfun.eclipse.performance.socket.table;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 
 * @author linzhaoming
 * Create Date: 2010-12-14
 */
public class PortModel {
	private String port;
	private String use;
	private String description;
	
	public PortModel() {
	}
	
	public PortModel(String port, String use, String description){
		this.port = port;
		this.use = use;
		this.description = description;
	}
	
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUse() {
		return use;
	}

	public void setUser(String use) {
		this.use = use;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		return "Item " + port + use + description;
	}

	
	
	public static List createModel(String fileName){
		String fileContent = "";
		try {
			ClassLoader cl = PortModel.class.getClassLoader();
			InputStream inputStream = cl.getResourceAsStream(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			StringBuffer sb = new StringBuffer();
			String s;
			while ((s = in.readLine()) != null) {
				sb.append(s);
				sb.append("\n");
			}
			in.close();
			fileContent = sb.toString();
		} catch (IOException e) {
            fileContent ="80\tWeb\tNothing";
            PortModel model = new PortModel("80", "Web", "Nothing");            
            System.err.println("Error reading : "+fileName+" : "+e);
            ArrayList list = new ArrayList();
            list.add(model);
            return list;
		}
        
        StringTokenizer st1 = new StringTokenizer(fileContent,"\n");
        int count = st1.countTokens();
        String[][] data = new String[count][3];
        int col=0;
        
        List list = new ArrayList();
		for (int row = 0; st1.hasMoreTokens(); row++) {
			PortModel model = new PortModel();
			list.add(model);
			StringTokenizer st2 = new StringTokenizer(st1.nextToken(), "\t");
			for (col = 0; st2.hasMoreTokens(); col++) {
				if(col ==0){
					model.setPort(st2.nextToken());
				}else if(col ==1){
					model.setUser(st2.nextToken());
				}else if(col == 2){
					model.setDescription(st2.nextToken());
				}
			}
		}
		
		return list;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(createModel("udpports.txt"));
	}
}