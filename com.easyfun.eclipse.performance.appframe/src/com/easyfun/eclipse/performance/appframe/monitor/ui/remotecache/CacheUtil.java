package com.easyfun.eclipse.performance.appframe.monitor.ui.remotecache;

import java.util.ArrayList;
import java.util.List;

public class CacheUtil {
	public static List<HostDesc> test() {
		List<HostDesc> list = new ArrayList<HostDesc>();
		list.add(new HostDesc(21, "CRM��Ʒ�ڴ�ͳ����Ϣ"));
		list.add(new HostDesc(22, "CRMȨ���ڴ�ͳ����Ϣ"));
		list.add(new HostDesc(23, "CRM·���ڴ�ͳ����Ϣ"));
		list.add(new HostDesc(111229, "CRMҵ�񲢷�����"));

		return list;
	}
}
