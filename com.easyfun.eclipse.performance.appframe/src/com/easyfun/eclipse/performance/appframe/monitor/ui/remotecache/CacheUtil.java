package com.easyfun.eclipse.performance.appframe.monitor.ui.remotecache;

import java.util.ArrayList;
import java.util.List;

public class CacheUtil {
	public static List<HostDesc> test() {
		List<HostDesc> list = new ArrayList<HostDesc>();
		list.add(new HostDesc(21, "CRM产品内存统计信息"));
		list.add(new HostDesc(22, "CRM权限内存统计信息"));
		list.add(new HostDesc(23, "CRM路由内存统计信息"));
		list.add(new HostDesc(111229, "CRM业务并发控制"));

		return list;
	}
}
