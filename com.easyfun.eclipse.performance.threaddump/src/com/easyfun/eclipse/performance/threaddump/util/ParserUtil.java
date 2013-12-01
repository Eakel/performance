package com.easyfun.eclipse.performance.threaddump.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.velocity.util.StringUtils;

import com.easyfun.eclipse.common.kv.KeyValue;
import com.easyfun.eclipse.utils.lang.StringUtil;

public class ParserUtil {
	/** 读取文件的内容*/
    public static List<String> readLines(InputStream input){
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        List<String> list = new ArrayList<String>();
        try {
			String line = reader.readLine();
			while (line != null) {
				//去除空白行
				if(StringUtil.isNotBlank(line)){
					list.add(line);	
				}
			    line = reader.readLine();
			}
		} catch (IOException e) {
			return new ArrayList<String>();
		}
        return list;
    }
    
	/**
	 * 
	 * @param callMarker ThreadDump中包含的字符串
	 * @param is	
	 * @return
	 */
	public static List<KeyValue> getOBDCallKeyValues(String callMarker, String callPrefix, InputStream is){
		List<String> list = ParserUtil.readLines(is);

		int obdCallMarkerPos = 0;
		Map<String, Integer> obdCallMap = new TreeMap<String, Integer>();
		for (int i = 0; i < list.size(); i++) {
			String str = list.get(i);
			if(str.contains(callMarker)){
				obdCallMarkerPos = i;
			}else if(obdCallMarkerPos >0 && i == obdCallMarkerPos +1){
				String intName = getOBDIntName(callPrefix, str);
				if(!obdCallMap.containsKey(intName)){
					obdCallMap.put(intName, 1);
				}else{
					Integer callTimes = (Integer)obdCallMap.get(intName);
					callTimes = Integer.valueOf(callTimes.intValue() +1);
					obdCallMap.put(intName, callTimes);
				}
			}
		}
		
		List<KeyValue> obdKVList = new ArrayList<KeyValue>();
		Iterator<Entry<String, Integer>> iter = obdCallMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Integer> entry = iter.next();
			KeyValue kv = new KeyValue(entry.getKey(), entry.getValue().toString());
			obdKVList.add(kv);			
		}
		
		return obdKVList;
	}

	
	private static String getOBDIntName(String callPrefix, String str){
		//查找接口名字，例如MPromoAccept.IPromoAcceptInt.query_schemePromoList
//		String str = "	at com.asiainfo.openboss.obd.MPromoAccept.IPromoAcceptInt.query_schemePromoList(IPromoAcceptInt.java:3800)";
//		String prefix = "com.asiainfo.openboss.obdbusi.acctmgnt";
		String remain = str.substring(str.indexOf(callPrefix) + callPrefix.length());	//去掉前缀的内容
		String strs[] = StringUtils.split(remain, ".");
		
		String[] interfaceName = new String[3];
		System.arraycopy(strs, 0, interfaceName, 0, 2);				//接口名称
		interfaceName[2] =  strs[2].substring(0, strs[2].indexOf("("));	//方法名称
		
		return interfaceName[0] + "." + interfaceName[1] + "." + interfaceName[2];
	}

}
