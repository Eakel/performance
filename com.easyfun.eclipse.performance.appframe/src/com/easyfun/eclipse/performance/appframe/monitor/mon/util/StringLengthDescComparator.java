package com.easyfun.eclipse.performance.appframe.monitor.mon.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class StringLengthDescComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		String s1 = (String) o1;
		String s2 = (String) o2;

		int rtn = 0;
		int i1 = s1.length();
		int i2 = s2.length();
		if (i1 == i2) {
			rtn = 0;
		} else if (i1 < i2) {
			rtn = 1;
		} else if (i1 > i2) {
			rtn = -1;
		}
		return rtn;
	}

	public boolean equals(Object obj) {
		return false;
	}

	public static void main(String[] args) throws Exception {
		List list = new ArrayList();

		for (int i = 0; i < 10001; i++) {
			list.add("str" + i + "str");
		}

		String[] a = (String[]) (String[]) list.toArray(new String[0]);
		Arrays.sort(a, new StringLengthDescComparator());
		System.out.println(a[0]);

		long start = System.currentTimeMillis();
		for (int j = 0; j < 10000; j++) {
			for (int i = 0; (i < a.length) && ("str0".indexOf(a[i]) == -1); i++)
				;
		}

		System.out.println("Time cost:" + (System.currentTimeMillis() - start) + ":ms");
	}
}