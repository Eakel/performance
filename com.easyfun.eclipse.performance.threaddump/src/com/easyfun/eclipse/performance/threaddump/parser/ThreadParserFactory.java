package com.easyfun.eclipse.performance.threaddump.parser;

import com.easyfun.eclipse.performance.threaddump.parser.bes8.BES8ThreadPaser;
import com.easyfun.eclipse.performance.threaddump.parser.mon.MonThreadPaser;
import com.easyfun.eclipse.performance.threaddump.parser.sun.SunThreadPaser;
import com.easyfun.eclipse.performance.threaddump.parser.webloigc.WebLogicThreadParser;

/**
 * 根据不同解析类型返回不同的解析器
 * @author linzhaoming
 *
 * 2013-11-17
 *
 */
public class ThreadParserFactory {
	
	private static BES8ThreadPaser bes8Parser = new BES8ThreadPaser();
	
	private static WebLogicThreadParser weblogicParser = new WebLogicThreadParser();
	
	private static MonThreadPaser defaultParser = new MonThreadPaser();
	
	private static SunThreadPaser sunParser = new SunThreadPaser();
	
	private static EmptyThreadParser emptyParser = new EmptyThreadParser();
	
	public static IThreadParser getThreadParser(ParserType parserType){
		switch (parserType) {
		case BES8:
			return bes8Parser;
		case Weblogic:
			return weblogicParser;
		case MON:
			return defaultParser;
		case SUN:
			return sunParser;
		default:
			return emptyParser;
		}
	}
}
