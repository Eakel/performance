package com.easyfun.eclipse.common.util.lang;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 
 * @author linzhaoming
 *
 */
public class StringUtil {

    /**
     * Concat two arrays of Strings,
     * part2 is appended to part1
     */
    public static String[] concat(String[] part1, String[] part2) {
        String[] full = new String[part1.length + part2.length];
        System.arraycopy(part1, 0, full, 0, part1.length);
        System.arraycopy(part2, 0, full, part1.length, part2.length);
        return full;
    }

    /**
     * See StringTokenizer for delim parameter format 
     */
    public static String[] cutString(String str, String delim) {
        ArrayList<String> strings = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(str, delim);
        while (tokenizer.hasMoreTokens()) {
            strings.add(tokenizer.nextToken());
        }

        return (String[]) strings.toArray(new String[0]);
    }

    public static String capitalizeFirstChar(String word) {
        if (word != null && word.length() > 0) {
            return word.substring(0, 1).toUpperCase() + word.substring(1);
        }
        return word;
    }

    /**
     * Sample1: ObjectDataSource -> Object Data Source
     * Sample2: OOObject -> O O Object
     * @param text
     * @return
     * @author lchen
     */
    public static String spaceWordsByUpperCase(String text) {
        if (text != null) {
            StringBuilder sb = new StringBuilder();
            char[] chars = text.toCharArray();
            for (char c : chars) {
                if (c >= 65 && c <= 90 && sb.length() > 0){
                    sb.append(' ');
                }
                sb.append(c);
            }
            return sb.toString();
        }
        return text;
    }

    /**
     * Sample1: submit_date -> submit date
     * Sample2: _submit_date -> submit date
     * Sample3: ____submit____date________ -> submit date
     * @param text
     * @return
     */
    public static String spaceWordsByUnderline(String text) {
        if (text != null) {
            StringBuilder builder = new StringBuilder(text);
            // delete extra underlines
            int index = builder.indexOf("__");
            while (index >= 0) {
                builder.deleteCharAt(index);
                index = builder.indexOf("__");
            }
            // replace underline to space
            for (int i = builder.length() - 1; i >= 0; i--) {
                char c = builder.charAt(i);
                if (c == '_') {
                    if (i == builder.length() - 1)
                        builder.deleteCharAt(i);
                    else if (i == 0)
                        builder.deleteCharAt(i);
                    else
                        builder.replace(i, i + 1, " ");
                }
            }
            return builder.toString();
        }
        return text;
    }

    /**
     * Sample1: S E S submit date -> S E S Submit Date
     * @param text
     * @return
     */
    public static String capitalizeAllFirstChar(String text) {
        if (text != null) {
            StringBuilder builder = new StringBuilder(text);
            for (int i = 0, count = builder.length(); i < count; i++) {
                char c = builder.charAt(i);
                if (i == 0) {
                    if (c >= 97 && c <= 122) {
                        builder.deleteCharAt(i);
                        builder.insert(i, Character.toUpperCase(c));
                    }
                } else if (c == ' ' && i < count - 1) {
                    int j = i + 1;
                    char r = builder.charAt(j);
                    if (r >= 97 && r <= 122) {
                        builder.deleteCharAt(j);
                        builder.insert(j, Character.toUpperCase(r));
                    }
                }
            }
            return builder.toString();
        }
        return text;
    }

    /**
     * Sample: ObjectDataSource -> objectDataSource
     * @param word
     * @return
     * @author lchen
     */
    public static String lowercaseFirstChar(String word) {
        if (word != null && word.length() > 0) {
            return word.substring(0, 1).toLowerCase() + word.substring(1);
        }
        return word;
    }    
    
    public static boolean isNotBlank(String str){
    	return StringUtils.isNotBlank(str);
    }
    
    public static boolean isNotEmpty(String str){
    	return StringUtils.isNotEmpty(str);
    }
    
    public static boolean isEmpty(String str){
    	return StringUtils.isEmpty(str);
    }
    
    public static boolean isBlank(String str){
    	return StringUtils.isBlank(str);
    }
    
    public static boolean isNumeric(String str){
    	return StringUtils.isNumeric(str);
    }
    
    public static boolean isNumber(String str){
    	return NumberUtils.isNumber(str);
    }
    
    public static String substringBetween(String str1, String str2){
    	return StringUtils.substringBetween(str1, str2);
    }
    
    public static String[] split(String str1, String str2){
    	return StringUtils.split(str1, str2);
    }
    
//    /**
//     * Sample: unicode -> UTF-8
//     * @param input
//     * @return byte[]
//     * @author clin
//     * @throws UnsupportedEncodingException 
//     */
//    public static byte[] changeToUTF8Bytes(String input) {
//    	if(input != null){
//    		String _fileEncoding = Constants.PERFERED_FILE_ENCODING;
//    		try {
//				return input.getBytes(_fileEncoding);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//    	}
//    	
//    	return null;
//    }
}
