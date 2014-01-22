package com.easyfun.eclipse.performance.appframe.webtool.util.appf;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 用于数据库密码的加解密
 * 
 * @author linzhaoming
 * 
 * Created at 2014年1月22日
 */
public final class DESUtil {
	private static Cipher ecipher = null;
	private static Cipher dcipher = null;

	private static byte[] data = { 99, 12, 104, 101, 110, 122, 113, 64, 97, 115, 105, 97, 105, 110, 102, 111, 46, 99, 111, 109, 121, 97, 110, 103, 104, 117,
			97, 64, 97, 115, 105, 97, 105, 110, 102, 111, 46, 99, 111, 109, 99, 104, 101, 110, 122, 113, 64, 97, 115, 105, 97, 105, 110, 102, 111, 46, 99, 111,
			109, 121, 97, 110, 103, 104, 117, 97, 64, 97, 115, 105, 97, 105, 110, 102, 111, 46, 99, 111, 109, 99, 104, 101, 110, 122, 113, 64, 97, 115, 105,
			97, 105, 110, 102, 111, 46, 99, 111, 109, 121, 97, 110, 103, 104, 117, 97, 64, 97, 115, 105, 97, 105, 110, 102, 111, 46, 99, 111, 109, 123, 99,
			104, 101, 110, 122, 113, 64, 97, 115, 105, 97, 105, 110, 102, 111, 46, 99, 111, 109, 121, 97, 110, 103, 104, 117, 97, 64, 97, 115, 105, 97, 105,
			110, 102, 111, 46, 99, 111, 109 };

	private static SecretKey key = null;
	
	static {
		try {
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			DESKeySpec keySpec = new DESKeySpec(data, 128);
			key = keyFactory.generateSecret(keySpec);

			ecipher = Cipher.getInstance("DES");
			dcipher = Cipher.getInstance("DES");
			ecipher.init(1, key);
			dcipher.init(2, key);
		} catch (Exception ex) {
			throw new RuntimeException("初始化异常", ex);
		}
	}

	private DESUtil() throws Exception {
	}

	public static String encrypt(String str) throws Exception {
		byte[] utf8 = str.getBytes("UTF8");
		byte[] enc = ecipher.doFinal(utf8);
		return "{des}" + new BASE64Encoder().encode(enc);
	}

	public static String decrypt(String str) throws Exception {
		String rtn = null;
		if (str.startsWith("{des}")) {
			str = StringUtils.substring(str, 5);
			byte[] dec = new BASE64Decoder().decodeBuffer(str);
			byte[] utf8 = dcipher.doFinal(dec);
			return new String(utf8, "UTF8");
		}

		rtn = str;
		return rtn;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(encrypt("test"));
		//{des}9jFef4PJ+VlFafCxexTKHg==
		System.out.println(decrypt("{des}9jFef4PJ+VlFafCxexTKHg=="));
	}

}