package com.easyfun.eclipse.performance.appframe.monitor.mon.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class StringNumberCompator implements Comparator {
	public static final String NUMBER_PATTERN = "(\\-?\\d+\\.\\d+)|(\\-?\\.\\d+)|(\\-?\\d+)";

	public int compare(Object o1, Object o2) {
		if ((o1 == null) || (o2 == null)) {
			return 0;
		}
		String str1 = (String) o1;
		String str2 = (String) o2;

		List split1 = split(str1);
		List split2 = split(str2);
		int diff = 0;

		for (int i = 0; (diff == 0) && (i < split1.size()) && (i < split2.size()); i++) {
			String token1 = (String) split1.get(i);
			String token2 = (String) split2.get(i);

			if ((token1.matches("(\\-?\\d+\\.\\d+)|(\\-?\\.\\d+)|(\\-?\\d+)")) && (token2.matches("(\\-?\\d+\\.\\d+)|(\\-?\\.\\d+)|(\\-?\\d+)"))) {
				diff = (int) Math.signum(Double.parseDouble(token1) - Double.parseDouble(token2));
			} else {
				diff = token1.compareToIgnoreCase(token2);
			}
		}
		if (diff != 0) {
			return diff;
		}

		return split1.size() - split2.size();
	}

	private List<String> split(String s) {
		List list = new ArrayList();
		Scanner scanner = new Scanner(s);
		int index = 0;
		String num = null;
		while ((num = scanner.findInLine("(\\-?\\d+\\.\\d+)|(\\-?\\.\\d+)|(\\-?\\d+)")) != null) {
			int indexOfNumber = s.indexOf(num, index);
			if (indexOfNumber > index) {
				list.add(s.substring(index, indexOfNumber));
			}
			list.add(num);
			index = indexOfNumber + num.length();
		}
		if (index < s.length()) {
			list.add(s.substring(index));
		}
		return list;
	}

	public boolean equals(Object obj) {
		return false;
	}

	public static void main(String[] args) throws Exception {
		String[] s = { "crm-web-g50-srv1", "crm-web-g50-srv2", "crm-web-g50-srv3", "crm-web-g50-srv4", "crm-web-g50-srv5", "crm-web-g50-srv6",
				"crm-web-g50-srv7", "crm-web-g50-srv8", "crm-web-g50-srv9", "crm-web-g51-srv1", "crm-web-g51-srv2", "crm-web-g51-srv3", "crm-web-g51-srv4",
				"crm-web-g51-srv5", "crm-web-g51-srv6", "crm-web-g51-srv7", "crm-web-g51-srv8", "crm-web-g51-srv9", "crm-web-g52-srv1", "crm-web-g52-srv2",
				"crm-web-g52-srv3", "crm-web-g52-srv4", "crm-web-g52-srv5", "crm-web-g52-srv6", "crm-web-g52-srv7", "crm-web-g52-srv8", "crm-web-g52-srv9",
				"crm-web-g53-srv1", "crm-web-g53-srv2", "crm-web-g53-srv3", "crm-web-g53-srv4", "crm-web-g53-srv5", "crm-web-g53-srv6", "crm-web-g53-srv7",
				"crm-web-g53-srv8", "crm-web-g53-srv9", "crm-web-g50-srv10", "crm-web-g50-srv11", "crm-web-g50-srv12", "crm-web-g50-srv13",
				"crm-web-g50-srv14", "crm-web-g50-srv15", "crm-web-g50-srv16", "crm-web-g51-srv10", "crm-web-g51-srv11", "crm-web-g51-srv12",
				"crm-web-g51-srv13", "crm-web-g51-srv14", "crm-web-g51-srv15", "crm-web-g51-srv16", "crm-web-g52-srv10", "crm-web-g52-srv11",
				"crm-web-g52-srv12", "crm-web-g52-srv13", "crm-web-g52-srv14", "crm-web-g52-srv15", "crm-web-g52-srv16", "crm-web-g53-srv10",
				"crm-web-g53-srv11", "crm-web-g53-srv12", "crm-web-g53-srv13", "crm-web-g53-srv14", "crm-web-g53-srv15", "crm-web-g53-srv16" };

		Arrays.sort(s, new StringNumberCompator());

		for (int i = 0; i < s.length; i++)
			System.out.println(s[i]);
	}
}