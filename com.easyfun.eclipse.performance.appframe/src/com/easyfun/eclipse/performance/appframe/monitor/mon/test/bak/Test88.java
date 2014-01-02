package com.easyfun.eclipse.performance.appframe.monitor.mon.test.bak;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

public class Test88 {
	public static void main(String[] args) throws Exception {
		TreeMap map = new TreeMap();
		map.put("HA5-ZW01", "10.96.18.131");
		map.put("HA5-ZW02", "10.96.18.132");
		map.put("HA5-ZW03", "10.96.18.133");
		map.put("HA5-ZW04", "10.96.18.134");
		map.put("HA5-YY13", "10.96.19.156");
		map.put("HA5-YY14", "10.96.19.157");
		map.put("HA5-JK05", "10.96.19.175");
		map.put("HA5-JK06", "10.96.19.176");
		map.put("HA5-JK07", "10.96.19.177");
		map.put("HA5-JK08", "10.96.19.178");
		map.put("HA5-JK09", "10.96.19.179");
		map.put("HA5-JK10", "10.96.19.180");
		map.put("HA5-JK13", "10.96.19.193");
		map.put("HA5-JK14", "10.96.19.194");
		map.put("HA5-JK17", "10.96.20.74");
		map.put("HA5-JK18", "10.96.20.75");
		map.put("HA5-JK15", "10.96.24.36");
		map.put("HA5-JK16", "10.96.24.37");
		map.put("HA5-JK03", "10.96.19.195");
		map.put("HA5-JK04", "10.96.19.196");
		map.put("HA5-JK11", "10.96.20.70");
		map.put("HA5-JK12", "10.96.20.71");

		long id = 3000000L;
		Set key = map.keySet();
		for (Iterator iter = key.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			String ip = (String) map.get(item);
			String[] tmp = StringUtils.split(ip, ".");
			int last = Integer.parseInt(tmp[(tmp.length - 1)]);

			if ((last >= 131) && (last <= 134)) {
				System.out.println("insert into mon_p_host values('" + item + "','" + ip + "','hnpay','{RC2}e1W1nMYgEk17',22,'U','" + item + "');");
				id += 1L;
				System.out.println("insert into mon_p_info values(" + id + ",'" + ip + "的ams_exe的/home文件系统','" + item + "',5006,10006,1,10006,'EXEC','AMS_EXE','U','" + item + "的/home文件系统');");
			}

			if ((last >= 156) && (last <= 157)) {
				System.out.println("insert into mon_p_host values('" + item + "','" + ip + "','product','{RC2}qdR520WBP8PnYXsG',22,'U','" + item + "');");
				id += 1L;
				System.out.println("insert into mon_p_info values(" + id + ",'" + ip + "的product的/home文件系统','" + item + "',5007,10007,1,10007,'EXEC','SO_PRODUCT','U','" + item + "的/home文件系统');");
			}

			if (((last >= 175) && (last <= 180)) || (last == 193) || (last == 194)) {
				System.out.println("insert into mon_p_host values('" + item + "','" + ip + "','ainter','{RC2}+x7bGDNl0V1hew==',22,'U','" + item + "');");
				id += 1L;
				System.out.println("insert into mon_p_info values(" + id + ",'" + ip + "的aiinter的ccs的/home文件系统','" + item + "',5008,10008,1,10008,'EXEC','INTER_CCS','U','" + item + "的/home文件系统');");
			}

			if ((last == 74) || (last == 75)) {
				System.out.println("insert into mon_p_host values('" + item + "','" + ip + "','ainter','{RC2}+x7bGDNl0V1hew==',22,'U','" + item + "');");
				id += 1L;
				System.out.println("insert into mon_p_info values(" + id + ",'" + ip + "的aiinter的other的/home文件系统','" + item + "',5008,10008,1,10008,'EXEC','INTER_OTHER','U','" + item + "的/home文件系统');");
			}

			if ((last == 36) || (last == 37)) {
				System.out.println("insert into mon_p_host values('" + item + "','" + ip + "','ainter','{RC2}+x7bGDNl0V1hew==',22,'U','" + item + "');");
				id += 1L;
				System.out.println("insert into mon_p_info values(" + id + ",'" + ip + "的aiinter的cmnet的/home文件系统','" + item + "',5008,10008,1,10008,'EXEC','INTER_CMNET','U','" + item + "的/home文件系统');");
			}

			if ((last == 195) || (last == 196)) {
				System.out.println("insert into mon_p_host values('" + item + "','" + ip + "','ainter','{RC2}+x7bGDNl0V1hew==',22,'U','" + item + "');");
				id += 1L;
				System.out.println("insert into mon_p_info values(" + id + ",'" + ip + "的aiinter的bank的/home文件系统','" + item + "',5008,10008,1,10008,'EXEC','INTER_BANK','U','" + item + "的/home文件系统');");
			}

			if ((last == 70) || (last == 71)) {
				System.out.println("insert into mon_p_host values('" + item + "','" + ip + "','ainter','{RC2}+x7bGDNl0V1hew==',22,'U','" + item + "');");
				id += 1L;
				System.out.println("insert into mon_p_info values(" + id + ",'" + ip + "的aiinter的charge的/home文件系统','" + item + "',5008,10008,1,10008,'EXEC','INTER_CHARGE','U','" + item + "的/home文件系统');");
			}
		}
	}
}