package com.easyfun.eclipse.performance.appframe.monitor.mon.test.bak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Test99 {
	public static void main(String[] args) throws Exception {
		HashMap map = new HashMap();
		map.put("鹤壁", "1");
		map.put("郑州", "1");
		map.put("漯河", "1");
		map.put("许昌", "1");
		map.put("洛阳", "2");
		map.put("新乡", "2");
		map.put("开封", "2");
		map.put("驻马店", "2");
		map.put("安阳", "3");
		map.put("信阳", "3");
		map.put("南阳", "3");
		map.put("济源", "3");
		map.put("焦作", "3");
		map.put("濮阳", "4");
		map.put("三门峡", "4");
		map.put("周口", "4");
		map.put("商丘", "4");
		map.put("平顶山", "4");

		List list = new ArrayList();
		list.add("郑州");
		list.add("商丘");
		list.add("安阳");
		list.add("新乡");
		list.add("许昌");
		list.add("平顶山");
		list.add("信阳");
		list.add("南阳");
		list.add("开封");
		list.add("洛阳");
		list.add("焦作");
		list.add("濮阳");
		list.add("周口");
		list.add("漯河");
		list.add("驻马店");
		list.add("三门峡");
		list.add("鹤壁");
		Set key = map.keySet();

		for (Iterator iter2 = list.iterator(); iter2.hasNext();) {
			String a = (String) iter2.next();
			for (Iterator iter = key.iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				if (a.equalsIgnoreCase(item)) {
					String b = (String) map.get(item);
					if (b.equalsIgnoreCase("1")) {
						System.out.println("HNZW1-HNZW2");
						break;
					}
					if (b.equalsIgnoreCase("3")) {
						System.out.println("HNZW1-HNZW2");
						break;
					}
					if (b.equalsIgnoreCase("2")) {
						System.out.println("HNZW2-HNZW1");
						break;
					}
					if (!b.equalsIgnoreCase("4"))
						break;
					System.out.println("HNZW2-HNZW1");
					break;
				}
			}
		}
	}
}