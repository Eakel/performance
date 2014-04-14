import java.io.FileInputStream;

/**
 * 根据Maic Number判断文件类型
 * http://en.wikipedia.org/wiki/Magic_number_(programming)
 * @author linzhaoming
 *
 * @Created 2014-4-14
 */
public class GetFileType {
	public static String bytesToHexString(byte[] src) {  
        StringBuilder stringBuilder = new StringBuilder();  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString();  
    }  
  
    public static void main(String[] args) throws Exception {  
        FileInputStream is = new FileInputStream("D://JDBCTask.class");  
        byte[] b = new byte[3];  
        is.read(b, 0, b.length);  
        System.out.println(bytesToHexString(b));  
        is.close();
    }  
}
