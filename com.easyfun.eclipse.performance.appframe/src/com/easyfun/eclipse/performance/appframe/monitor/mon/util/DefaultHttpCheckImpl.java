package com.easyfun.eclipse.performance.appframe.monitor.mon.util;

import java.util.Map;

public class DefaultHttpCheckImpl implements IInterCheck {
	
	public CheckStatus check(Map map) throws Exception {
		CheckStatus cs = new CheckStatus();
		try {
			String url = (String) map.get("URL");
			String timeout = (String) map.get("TIMEOUT");
			String info = HttpUtil.curl(url, Integer.parseInt(timeout));
			cs.setStatus("OK");
			cs.setInfo(info);
		} catch (Exception ex) {
			ex.printStackTrace();
			cs.setStatus("EXCEPTION");
		}
		return cs;
	}
}