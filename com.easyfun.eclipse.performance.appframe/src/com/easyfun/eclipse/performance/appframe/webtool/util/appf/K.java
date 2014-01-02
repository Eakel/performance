package com.easyfun.eclipse.performance.appframe.webtool.util.appf;


/**
 * RC2加解密 V55版本
 * <li>静态字段固定为密钥
 * @author linzhaoming
 * 
 * Created at 2011-2-26
 */
public final class K {
	/** RC2加解密的密钥*/
	private static byte[] key = { 97, 105, 95, 110, 106, 95, 114, 100 };
	
	private K() throws Exception {
	}

	/** 直接加密*/
	public static String j(String plain) throws Exception {
		RC2 rc2 = new RC2();
		return rc2.encrypt_rc2_array_base64(plain.getBytes(), key);
	}

	/**直接解密，调用RC2.decrypt_rc2_array_base64()，输入不能包含RC2{} */
	public static String k(String cipher) throws Exception {
		RC2 rc2 = new RC2();
		return rc2.decrypt_rc2_array_base64(cipher.getBytes(), key);
	}

	/**
	 * 解密方法: <b>很多类调用</b> 
	 * 以RC{2}开头的才会做特别处理，否则直接返回
	 *  <li>bug:如果中间包含{RC2}，逻辑有问题
	 * */
	public static String k_s(String cipher) throws Exception {
		String rtn = null;
		if ((cipher != null) && (cipher.lastIndexOf("{RC2}") != -1)) {
			rtn = k(cipher.substring(5));
		} else {
			rtn = cipher;
		}
		return rtn;
	}

	public static void main(String[] args) throws Exception {
		String a = j("jhasdhadasadsadsas123ad");
		System.out.println(a);
		String b = k(a);
		System.out.println(b);
	}

	public static void main2(String[] args) throws Exception {
		String a = k_s("{RC2}RcAeFXsjJHfGNA==");
		System.out.println(a);
	}
}