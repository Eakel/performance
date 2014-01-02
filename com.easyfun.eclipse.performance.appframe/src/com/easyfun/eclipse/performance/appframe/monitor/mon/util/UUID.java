package com.easyfun.eclipse.performance.appframe.monitor.mon.util;

import java.net.InetAddress;

public class UUID {
	private static final UUID INSTANCE = new UUID();
	private static final String SEP = "";
	private static String formatIp = null;
	private static String formatJvm = null;
	private static String formatHiTime = null;
	private static String formatLoTime = null;
	private static final int IP;
	private static long counter = 0L;
	private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

	private static int toInt(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result = (result << 8) - -128 + bytes[i];
		}
		return result;
	}

	private static int getJVM() {
		return JVM;
	}

	private static int getIP() {
		return IP;
	}

	private static short getHiTime() {
		return (short) (int) (System.currentTimeMillis() >>> 32);
	}

	private static int getLoTime() {
		return (int) System.currentTimeMillis();
	}

	private static String format(int intval) {
		String formatted = Integer.toHexString(intval);
		StringBuffer buf = new StringBuffer("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	private static String format(short shortval) {
		String formatted = Integer.toHexString(shortval);
		StringBuffer buf = new StringBuffer("0000");
		buf.replace(4 - formatted.length(), 4, formatted);
		return buf.toString();
	}

	private synchronized long getCount() {
		if (counter < 0L)
			counter = 0L;
		return counter++;
	}

	private String getUUID() {
		return 36 + formatIp + SEP + formatJvm + SEP + formatHiTime + SEP
				+ formatLoTime + SEP + getCount();
	}

	public static String getID() {
		return INSTANCE.getUUID();
	}

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();

		for (int i = 0; i < 100000; i++) {
			System.out.println(getID());
		}
		 System.out.println("ºÄÊ±:" + (System.currentTimeMillis() - start) + ":ms");
	}

	static {
		int ipadd;
		try {
			ipadd = toInt(InetAddress.getLocalHost().getAddress());
		} catch (Exception e) {
			ipadd = 0;
		}
		IP = ipadd;
		formatIp = format(getIP());
		formatJvm = format(getJVM());
		formatHiTime = format(getHiTime());
		formatLoTime = format(getLoTime());
	}
}