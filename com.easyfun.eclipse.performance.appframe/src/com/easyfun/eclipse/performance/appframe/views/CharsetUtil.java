package com.easyfun.eclipse.performance.appframe.views;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author linzhaoming
 *
 * 2011-1-9
 *
 */
public class CharsetUtil {

	/**
	 * Converts encoded &#92;uxxxx to unicode chars and changes special saved
	 * chars to their original forms
	 */
	public static String loadConvert(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
	
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	/** 中文转为Unicode*/
	public static String decodeUnicode(final String dataStr) throws Exception{
		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();
		while (start > -1) {
			end = dataStr.indexOf("\\u", start + 2);
			String charStr = "";
			if (end == -1) {
				if(dataStr.length() > start + 2){
					charStr = dataStr.substring(start + 2, dataStr.length());
				}else{
					charStr = "";
				}
			} else {
				if(end > start + 2){
					charStr = dataStr.substring(start + 2, end);
				}else{
					charStr = "";
				}
			}
			char letter = ' ';
			if(StringUtils.isEmpty(charStr)){
				letter = ' '; // 16进制parse整形字符串。
			}else{
				letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
			}
			buffer.append(new Character(letter).toString());
			start = end;
		}
		return buffer.toString();
	}
}
