package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.model.IDataGridModel;

public class TestModel implements IDataGridModel {
	private static List list = new ArrayList();

	static {
		for (int i = 0; i < 100000; i++) {
			TestBean bean = new TestBean();
			bean.setId(new Long(i + 100000000000L));
			bean.setCode(i + "±àÂë");
			bean.setName(i + "Ñî»ª");
			bean.setState("U");
			bean.setTime(new Date());
			list.add(bean);
		}
	}

	public void setRequest(HttpServletRequest request) throws Exception {
	}

	public void setCondition(HashMap condition) throws Exception {
	}

	public long count() throws Exception {
		return list.size();
	}

	public Object[] getData(long startRowIndex, long endRowIndex) throws Exception {
		List rtn = new ArrayList();

		if (list.size() < endRowIndex) {
			endRowIndex = list.size();
		}
		for (long i = startRowIndex; i < endRowIndex; i += 1L) {
			rtn.add(list.get((int) i));
		}

		return (Object[]) rtn.toArray(new Object[0]);
	}

}