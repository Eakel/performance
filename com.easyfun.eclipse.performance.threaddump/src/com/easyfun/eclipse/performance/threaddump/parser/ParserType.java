package com.easyfun.eclipse.performance.threaddump.parser;

/**
 * 解析类型枚举定义
 * 
 * @author linzhaoming
 *
 * 2013-11-15
 *
 */
public enum ParserType {
	MON("MON"),
	BES8("BES8"),
	Weblogic("Weblogic"),	
	SUN("SUN");
	
	private String str;
	
	private ParserType(String str){
		this.str = str;
	}
	
	public String toString() {
		return str;
	}
	
	public static ParserType getParserEnumBy(String type){
		if("BES8".equals(type)){
			return BES8;
		}else if("Weblogic".equals(type)){
			return Weblogic;
		} else if("Default".equals(type)){
			return MON;
		} else if("SUN".equals(type)){
			return SUN;
		}else{
			return MON;
		}
	}
}
