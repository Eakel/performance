package com.easyfun.eclipse.performance.appframe.monitor.mon.test.bak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Test99 {
	public static void main(String[] args) throws Exception {
		HashMap map = new HashMap();
		map.put("�ױ�", "1");
		map.put("֣��", "1");
		map.put("���", "1");
		map.put("���", "1");
		map.put("����", "2");
		map.put("����", "2");
		map.put("����", "2");
		map.put("פ���", "2");
		map.put("����", "3");
		map.put("����", "3");
		map.put("����", "3");
		map.put("��Դ", "3");
		map.put("����", "3");
		map.put("���", "4");
		map.put("����Ͽ", "4");
		map.put("�ܿ�", "4");
		map.put("����", "4");
		map.put("ƽ��ɽ", "4");

		List list = new ArrayList();
		list.add("֣��");
		list.add("����");
		list.add("����");
		list.add("����");
		list.add("���");
		list.add("ƽ��ɽ");
		list.add("����");
		list.add("����");
		list.add("����");
		list.add("����");
		list.add("����");
		list.add("���");
		list.add("�ܿ�");
		list.add("���");
		list.add("פ���");
		list.add("����Ͽ");
		list.add("�ױ�");
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