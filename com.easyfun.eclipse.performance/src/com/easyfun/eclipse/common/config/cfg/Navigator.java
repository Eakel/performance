package com.easyfun.eclipse.common.config.cfg;

import java.util.ArrayList;
import java.util.List;

public class Navigator {
	private List list = new ArrayList();
	
	public void addFolder(Folder cache) {
		this.list.add(cache);
	}

	public Folder[] getFolders() {
		List<Folder> retList = new ArrayList<Folder>();
		for(int i=0; i<this.list.size(); i++){
			Folder folder = (Folder)this.list.get(i);
			if(("true").equalsIgnoreCase(folder.getVisible())){
				retList.add((Folder)this.list.get(i));
			}
		}

		return retList.toArray(new Folder[0]);
	}
}
