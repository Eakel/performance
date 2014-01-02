package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.util;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.ftl.Column;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.ftl.DataGrid;
import com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.tag.model.IDataGridModel;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

public final class FtlProcess {
	private static transient Log log = LogFactory.getLog(FtlProcess.class);

	public static final FtlExtendInstruction ftlExtend = new FtlExtendInstruction();

	private static Configuration config = null;

	static {
		config = new Configuration();
		config.setClassicCompatible(false);
		config.setClassForTemplateLoading(FtlProcess.class, "/ui/ftl");
		config.setNumberFormat("0.##########################");
	}

	public static void process(HttpServletRequest request, Writer writer, DataGrid dataGrid, int page, long rowCount, String uuid) throws Exception {
		Template template = config.getTemplate(dataGrid.getFtl().trim());

		SimpleHash hash = new SimpleHash();

		hash.put("FTL_TABLE_ID", dataGrid.getTableid());
		hash.put("FTL_WIDTH", dataGrid.getWidth());
		hash.put("FTL_HEIGHT", dataGrid.getHeight());
		hash.put("FTL_SHOW_ROWNUM", dataGrid.isRownum());
		hash.put("FTL_UUID", uuid);

		if (!StringUtils.isBlank(dataGrid.getSelect())) {
			hash.put("FTL_IS_SELECT", true);
			hash.put("FTL_SELECT", dataGrid.getSelect());
		} else {
			hash.put("FTL_IS_SELECT", false);
		}

		List headerList = new ArrayList();
		for (Iterator iter = dataGrid.getColumnList().iterator(); iter.hasNext();) {
			Object item = iter.next();
			if ((item instanceof Column)) {
				headerList.add((Column) item);
			}
		}
		hash.put("FTL_LIST_HEADER", headerList);

		Object[] data = null;
		long iPageSize = dataGrid.getPagesize().longValue();
		long iCurPage = 0L;
		long iLastPage = 0L;
		long rowCountTmp = 0L;

		if (page >= 0) {
			IDataGridModel objModel = (IDataGridModel) Class.forName(dataGrid.getModel()).newInstance();
			objModel.setRequest(request);
			objModel.setCondition(dataGrid.getCondition());

			if (rowCount <= 0L) {
				rowCountTmp = objModel.count();
			} else {
				rowCountTmp = rowCount;
			}

			long pages = rowCountTmp / iPageSize;
			if (rowCountTmp % iPageSize > 0) {
				pages += 1;
			}
			iCurPage = page;
			iLastPage = pages;
			data = objModel.getData(page * iPageSize, (page + 1) * iPageSize);
		}

		hash.put("FTL_LIST_DATA", data);
		hash.put("FTL_ROW_COUNT", String.valueOf(rowCountTmp));
		hash.put("FTL_PAGESIZE", String.valueOf(iPageSize));
		hash.put("FTL_CURPAGE", String.valueOf(iCurPage));
		hash.put("FTL_LASTPAGE", String.valueOf(iLastPage));

		hash.put("FTL_EXTEND", ftlExtend);

		template.setLocale(Locale.CHINESE);
		template.setEncoding("GBK");
		template.process(hash, writer);
	}

}