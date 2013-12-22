package com.easyfun.eclipse.performance.appframe.util.appf;

/**
 * AppF的加解密类(V52),来源于com.ai.appf*.util.Encrypt
 * 
 * @author linzhaoming Create Date: 2010-8-9
 */
public class AppFEncrypt {

	public static final int pass1 = 10;
	public static final int pass2 = 1;

	public AppFEncrypt() {
	}

	public static String DoEncrypt(String str) {
		StringBuffer enStrBuff = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			int tmpch = str.charAt(i);
			tmpch ^= 1;
			tmpch ^= 10;
			enStrBuff.append(Integer.toHexString(tmpch));
		}

		return enStrBuff.toString().toUpperCase();
	}

	public static String DoDecrypt(String str) {
		String deStr = str.toLowerCase();
		StringBuffer deStrBuff = new StringBuffer();
		for (int i = 0; i < deStr.length(); i += 2) {
			String subStr = deStr.substring(i, i + 2);
			int tmpch = Integer.parseInt(subStr, 16);
			tmpch ^= 1;
			tmpch ^= 10;
			deStrBuff.append((char) tmpch);
		}

		return deStrBuff.toString();
	}

	public static void main(String args[]) {
		String source = "market";
		String s = DoEncrypt(source);
		System.out.println("de=" + s);
		source = DoDecrypt(s);
		System.out.println("en=" + source);
	}

}