package com.easyfun.eclipse.performance.appframe.webtool.util.appf;


/**
 * RC2�ӽ��� V55�汾
 * <li>��̬�ֶι̶�Ϊ��Կ
 * @author linzhaoming
 * 
 * Created at 2011-2-26
 */
public final class K {
	/** RC2�ӽ��ܵ���Կ*/
	private static byte[] key = { 97, 105, 95, 110, 106, 95, 114, 100 };
	
	private K() throws Exception {
	}

	/** ֱ�Ӽ���*/
	public static String j(String plain) throws Exception {
		RC2 rc2 = new RC2();
		return rc2.encrypt_rc2_array_base64(plain.getBytes(), key);
	}

	/**ֱ�ӽ��ܣ�����RC2.decrypt_rc2_array_base64()�����벻�ܰ���RC2{} */
	public static String k(String cipher) throws Exception {
		RC2 rc2 = new RC2();
		return rc2.decrypt_rc2_array_base64(cipher.getBytes(), key);
	}

	/**
	 * ���ܷ���: <b>�ܶ������</b> 
	 * ��RC{2}��ͷ�ĲŻ����ر�������ֱ�ӷ���
	 *  <li>bug:����м����{RC2}���߼�������
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