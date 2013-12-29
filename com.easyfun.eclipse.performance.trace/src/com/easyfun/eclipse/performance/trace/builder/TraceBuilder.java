package com.easyfun.eclipse.performance.trace.builder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.easyfun.eclipse.performance.trace.TraceUtil;
import com.easyfun.eclipse.performance.trace.model.AppTrace;
import com.easyfun.eclipse.performance.trace.model.BccMemTrace;
import com.easyfun.eclipse.performance.trace.model.CauTrace;
import com.easyfun.eclipse.performance.trace.model.DaoTrace;
import com.easyfun.eclipse.performance.trace.model.HttpTrace;
import com.easyfun.eclipse.performance.trace.model.ITrace;
import com.easyfun.eclipse.performance.trace.model.JdbcTrace;
import com.easyfun.eclipse.performance.trace.model.MdbTrace;
import com.easyfun.eclipse.performance.trace.model.MemTrace;
import com.easyfun.eclipse.performance.trace.model.SecMemTrace;
import com.easyfun.eclipse.performance.trace.model.SimpleParam;
import com.easyfun.eclipse.performance.trace.model.SvrTrace;
import com.easyfun.eclipse.performance.trace.model.TraceTypeEnum;
import com.easyfun.eclipse.performance.trace.model.WebTrace;
import com.easyfun.eclipse.performance.trace.model.WsTrace;
import com.easyfun.eclipse.util.StringUtil;
import com.easyfun.eclipse.util.TimeUtil;
import com.trilead.ssh2.SFTPv3DirectoryEntry;

/**
 * 
 * @author linzhaoming
 *
 * 2013-4-2
 *
 */
public class TraceBuilder implements IBuilder {

	/** srv操作次数*/
	private int svrCount = 0;

	/** dao操作次数*/
	private int daoCount = 0;

	/** memcached操作次数*/
	private int memCount = 0;

	/**secmem操作次数*/
	private int secMemCount = 0;

	/** cau操作次数*/
	private int cauCount = 0;

	/** jdbc操作次数*/
	private int jdbcCount = 0;

	/** mdb操作次数*/
	private int mdbCount = 0;

	/** http操作次数*/
	private int httpCount = 0;

	/** ws操作次数*/
	private int wsCount = 0;
	
	/** BCC操作次数*/
	private int bccCount = 0;
	
	private static AppTrace parseTraceStream(InputStream is) throws Exception {
		InputStreamReader in = new InputStreamReader(is);
		if (in == null) {
			return null;
		}
		AppTrace trace = null;
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(in);
			Element root = document.getRootElement();
			if (root != null) {
				TraceBuilder builder = new TraceBuilder();
				trace = builder.parseAppTrace(root);
				trace.setTraceDocument(document);
				trace.setSvrCount(builder.svrCount);
				trace.setDaoCount(builder.daoCount);
				trace.setMemCount(builder.memCount);
				trace.setSecMemCount(builder.secMemCount);
				trace.setCauCount(builder.cauCount);
				trace.setJdbcCount(builder.jdbcCount);
				trace.setMdbCount(builder.mdbCount);
				trace.setHttpCount(builder.httpCount);
				trace.setWsCount(builder.wsCount);
				trace.setBccCount(builder.bccCount);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return trace;
	}
	
	/** 解析文件为AppTrace*/
	public static AppTrace parseTraceStream(InputStream is, File file, String parentDesc) throws Exception {
		AppTrace appTrace = parseTraceStream(is);		
		appTrace.getUiDesc().setFileName(file.getPath());
		appTrace.getUiDesc().setFileSize(file.length());
		appTrace.getUiDesc().setFileTime(TimeUtil.getLongDisplayTime(file.lastModified()));
		appTrace.getUiDesc().setTraceTime(appTrace.getCreateTime());
		appTrace.setUseTime(String.valueOf(TraceUtil.getCostByFileName(file.getName())));
		
		appTrace.getUiDesc().setParentDesc(parentDesc);
		
		return appTrace;
	}
	
	/** 解析FTP文件为AppTrace*/
	public static AppTrace parseTraceStream(InputStream is, SFTPv3DirectoryEntry entry, String parentDesc) throws Exception {
		AppTrace appTrace = parseTraceStream(is);
		//关闭文件流
		is.close();
		appTrace.getUiDesc().setFileName(entry.filename);
		appTrace.getUiDesc().setFileSize(entry.attributes.size);
		appTrace.getUiDesc().setFileTime(TimeUtil.getLongDisplayTime(entry.attributes.atime));
		appTrace.getUiDesc().setTraceTime(TimeUtil.getLongDisplayTime(entry.attributes.atime));
		appTrace.setUseTime(String.valueOf(TraceUtil.getCostByFileName(entry.filename)));
		
		appTrace.getUiDesc().setParentDesc(parentDesc);
		
		return appTrace;
	}
	

	/** 解析根 AppTrace, trace的根总是APP*/
	public AppTrace parseAppTrace(Element elemt) throws Exception {
		AppTrace appTrace = null;
		try {
			if (elemt != null) {
				appTrace = new AppTrace();
				appTrace.setId(elemt.attributeValue("id"));
				appTrace.setCreateTime(elemt.attributeValue("time"));
				appTrace.setType(TraceTypeEnum.TYPE_APP);
				
				Element webElement = elemt.element("web");
				if (webElement != null) {
					appTrace.addChild(parseWebTrace(webElement));
				}
				List svList = elemt.elements("srv");
				if ((svList != null) && (svList.size() > 0)) {
					for (int i = 0; i < svList.size(); i++) {
						Element svItem = (Element) svList.get(i);
						appTrace.addChild(parseSvrTrace(svItem));
					}
				}
				StringBuffer sb = new StringBuffer("");
				if (this.jdbcCount > 0) {
					sb.append(this.jdbcCount + "次jdbc操作\n");
				}
				if (this.memCount > 0) {
					sb.append(this.memCount + "次memcached操作\n");
				}
				if (this.mdbCount > 0) {
					sb.append(this.mdbCount + "次mdb操作\n");
				}
				if (this.svrCount > 0) {
					sb.append(this.svrCount + "次svr操作\n");
				}
				if (this.daoCount > 0) {
					sb.append(this.daoCount + "次dao操作\n");
				}
				if (this.secMemCount > 0) {
					sb.append(this.secMemCount + "次secmem操作\n");
				}
				if (this.cauCount > 0) {
					sb.append(this.cauCount + "次cau操作\n");
				}
				if (this.httpCount > 0) {
					sb.append(this.httpCount + "次http操作\n");
				}
				if (this.wsCount > 0) {
					sb.append(this.wsCount + "次ws操作\n");
				}
				if (this.bccCount > 0) {
					sb.append(this.bccCount + "次bcc操作\n");
				}
				appTrace.setMsg(sb.toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return appTrace;
	}
	
	/** 解析WebTrace*/
	public ITrace parseWebTrace(Element elemt) throws Exception {
		WebTrace webTrace = null;
		try {
			if (elemt != null) {
				webTrace = new WebTrace();
				webTrace.setId(elemt.attributeValue("id"));
				webTrace.setCreateTime(elemt.attributeValue("time"));
				webTrace.setName("Web");
				webTrace.setType(TraceTypeEnum.TYPE_WEB);
				Element urlElement = elemt.element("url");
				if (urlElement != null) {
					String txt = urlElement.getText();
					if (StringUtil.isNotBlank(txt)) {
						webTrace.setUrl(txt.trim());
					}
				}
				Element ciElement = elemt.element("ci");
				if (ciElement != null) {
					String txt = ciElement.getText();
					if (StringUtil.isNotBlank(txt)) {
						webTrace.setClientIp(txt);
					}
				}
				Element wiElement = elemt.element("wi");
				if (wiElement != null) {
					String txt = wiElement.getText();
					if (StringUtil.isNotBlank(txt)) {
						webTrace.setServerIp(txt);
					}
				}
				Element snElement = elemt.element("sn");
				if (snElement != null) {
					String txt = snElement.getText();
					if (StringUtil.isNotBlank(txt)) {
						webTrace.setServerName(txt);
					}
				}
				Element cdElement = elemt.element("code");
				if (cdElement != null) {
					String txt = cdElement.getText();
					if (StringUtil.isNotBlank(txt)) {
						webTrace.setCode(txt);
					}
				}
				Element orgElement = elemt.element("orgcode");
				if (orgElement != null) {
					String txt = orgElement.getText();
					if (StringUtil.isNotBlank(txt)) {
						webTrace.setOrgId(txt);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return webTrace;
	}
	
	/** 解析Svr Trace*/
	public ITrace parseSvrTrace(Element elemt) throws Exception {
		SvrTrace trace = null;
		try {
			if (elemt != null) {
				this.svrCount += 1;
				trace = new SvrTrace();
				trace.setId(elemt.attributeValue("id"));
				trace.setCreateTime(elemt.attributeValue("time"));
				trace.setType(TraceTypeEnum.TYPE_SVR);
				Element cnElement = elemt.element("cn");
				if (cnElement != null) {
					String clazz = cnElement.getText();
					if (StringUtil.isNotBlank(clazz)) {
						trace.setClassName(clazz.trim());
					}
				}
				Element mnElement = elemt.element("mn");
				if (mnElement != null) {
					String method = mnElement.getText();
					if (StringUtil.isNotBlank(method)) {
						trace.setMethodName(method);
					}
				}
				Element inElement = elemt.element("in");
				if (inElement != null) {
					trace.setInParam(getStringValue(inElement).trim());
				}
				Element aiElement = elemt.element("ai");
				if (aiElement != null) {
					trace.setAppIp(aiElement.getText());
				}
				Element snElement = elemt.element("sn");
				if (snElement != null) {
					trace.setAppServerName(snElement.getText());
				}
				Element cenElement = elemt.element("cen");
				if (cenElement != null) {
					trace.setCenter(cenElement.getText());
				}
				Element codeElement = elemt.element("code");
				if (codeElement != null) {
					trace.setCode(codeElement.getText());
				}
				Element sElement = elemt.element("s");
				if (sElement != null) {
					trace.setSuccess(sElement.getText());
				}
				Element etElement = elemt.element("et");
				if (etElement != null) {
					trace.setUseTime(etElement.getText());
				}

				List daoList = elemt.elements("dao");
				if ((daoList != null) && (daoList.size() > 0)) {
					for (int dc = 0; dc < daoList.size(); dc++) {
						Element daoItem = (Element) daoList.get(dc);
						trace.addChild(parseDaoTrace(daoItem));
					}
				}

				List srvList = elemt.elements("srv");
				if ((srvList != null) && (srvList.size() > 0)) {
					for (int i = 0; i < srvList.size(); i++) {
						trace.addChild(parseSvrTrace((Element) srvList.get(i)));
					}
				}
				
				List httpList = elemt.elements("http");
				for (int hc = 0; (httpList != null) && (hc < httpList.size()); hc++) {
					trace.addChild(parseHttpTrace((Element) httpList.get(hc)));
				}
				
				List wsList = elemt.elements("ws");
				for (int wc = 0; (wsList != null) && (wc < wsList.size()); wc++) {
					trace.addChild(parseWsTrace((Element) wsList.get(wc)));
				}
				
				List memList = elemt.elements("mem");
				for (int mc = 0; (memList != null) && (mc < memList.size()); mc++) {
					ITrace memTrace = parseMemTrace((Element) memList.get(mc));
					this.memCount += 1;
					memTrace.setName("Mem");
					trace.addChild(memTrace);
				}
				
				List secmemList = elemt.elements("secmem");
				for (int mc = 0; (secmemList != null) && (mc < secmemList.size()); mc++) {
					ITrace memTrace = parseSecMemTrace((Element) secmemList.get(mc));
					this.secMemCount += 1;
					memTrace.setName("Secmem");
					trace.addChild(memTrace);
				}
				
				List mdbList = elemt.elements("mdb");
				for (int mc = 0; (mdbList != null) && (mc < mdbList.size()); mc++) {
					ITrace mdbTrace = parseMdbTrace((Element) mdbList.get(mc));
					this.secMemCount += 1;
					mdbTrace.setName("Mdb");
					trace.addChild(mdbTrace);
				}
				
				List cauList = elemt.elements("cau");
				int mc = 0;
				if (cauList != null && cauList.size() > 0) {
					do {
						ITrace memTrace = parseCauMemTrace((Element) cauList.get(mc));
						this.cauCount += 1;
						memTrace.setName("Cau");
						trace.addChild(memTrace);

						mc++;
						if (cauList == null) {
							break;
						}
					} while (mc < cauList.size());
				}
				
				List bccList = elemt.elements("bccmem");
				for (int i = 0; (bccList != null) && (i < bccList.size()); i++) {
					ITrace bccTrace = parseBccTrace((Element) bccList.get(i));
					this.bccCount += 1;
					bccTrace.setName("Bcc");
					trace.addChild(bccTrace);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return trace;
	}

	/** 解析MDB Trace*/
	public ITrace parseMdbTrace(Element elemt) throws Exception {
		MdbTrace trace = null;
		try {
			if (elemt != null) {
				this.mdbCount += 1;
				trace = new MdbTrace();
				trace.setId(elemt.attributeValue("id"));
				trace.setName("Mdb");
				trace.setType(TraceTypeEnum.TYPE_MDB);
				trace.setCreateTime(elemt.attributeValue("time"));
				Element hostElem = elemt.element("host");
				if (hostElem != null) {
					((MdbTrace) trace).setHost(hostElem.getText());
				}
				Element cenElement = elemt.element("cen");
				if (cenElement != null) {
					((MdbTrace) trace).setCenter(cenElement.getText());
				}
				Element codeElem = elemt.element("code");
				if (codeElem != null) {
					((MdbTrace) trace).setCode(codeElem.getText());
				}
				Element inElement = elemt.element("in");
				if (inElement != null) {
					trace.setInParam(inElement.getStringValue().trim());
				}
				Element outElement = elemt.element("out");
				if (outElement != null) {
					trace.setOutParam(outElement.getStringValue().trim());
				}

				Element etElement = elemt.element("et");
				if (etElement != null) {
					((MdbTrace) trace).setUseTime(etElement.getText());
				}
				Element sElem = elemt.element("s");
				if (sElem != null)
					((MdbTrace) trace).setSuccess(sElem.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return trace;
	}

	/** 解析Mem Trace*/
	public ITrace parseMemTrace(Element elemt) throws Exception {
		MemTrace trace = null;
		try {
			if (elemt != null) {
				trace = new MemTrace();
				trace.setId(elemt.attributeValue("id"));
				trace.setCreateTime(elemt.attributeValue("time"));
				trace.setType(TraceTypeEnum.TYPE_MEM);
				Element hostElem = elemt.element("host");
				if (hostElem != null) {
					trace.setHost(hostElem.getText());
				}
				Element gtElem = elemt.element("gt");
				if (gtElem != null) {
					trace.setGetTime(gtElem.getText());
				}
				Element cenElement = elemt.element("cen");
				if (cenElement != null) {
					trace.setCenter(cenElement.getText());
				}
				Element codeElement = elemt.element("code");
				if (codeElement != null) {
					trace.setCode(codeElement.getText());
				}
				Element inElement = elemt.element("in");
				if (inElement != null) {
					trace.setInParam(getStringValue(inElement));
				}
				Element outElement = elemt.element("c");
				if (outElement != null) {
					trace.setResultCount(outElement.getText());
				}

				Element etElement = elemt.element("et");
				if (etElement != null) {
					trace.setUseTime(etElement.getText());
				}
				Element sElem = elemt.element("s");
				if (sElem != null) {
					trace.setSuccess(sElem.getText());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return trace;
	}
	
	/** 解析SecMem Trace*/
	public ITrace parseSecMemTrace(Element elemt) throws Exception {
		SecMemTrace trace = null;
		try {
			if (elemt != null) {
				trace = new SecMemTrace();
				trace.setId(elemt.attributeValue("id"));
				trace.setCreateTime(elemt.attributeValue("time"));
				trace.setType(TraceTypeEnum.TYPE_SECMEM);
				Element hostElem = elemt.element("host");
				if (hostElem != null) {
					trace.setHost(hostElem.getText());
				}
				Element gtElem = elemt.element("gt");
				if (gtElem != null) {
					trace.setGetTime(gtElem.getText());
				}
				Element cenElement = elemt.element("cen");
				if (cenElement != null) {
					trace.setCenter(cenElement.getText());
				}
				Element codeElement = elemt.element("code");
				if (codeElement != null) {
					trace.setCode(codeElement.getText());
				}
				Element inElement = elemt.element("in");
				if (inElement != null) {
					trace.setInParam(inElement.getStringValue().trim());
				}
				Element outElement = elemt.element("c");
				if (outElement != null) {
					trace.setResultCount(outElement.getText());
				}

				Element etElement = elemt.element("et");
				if (etElement != null) {
					trace.setUseTime(etElement.getText());
				}
				Element sElem = elemt.element("s");
				if (sElem != null) {
					trace.setSuccess(sElem.getText());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return trace;
	}
	
	/** 解析Cau Trace*/
	public ITrace parseCauMemTrace(Element elemt) throws Exception {
		CauTrace trace = null;
		try {
			if (elemt != null) {
				trace = new CauTrace();
				trace.setId(elemt.attributeValue("id"));
				trace.setCreateTime(elemt.attributeValue("time"));
				trace.setType(TraceTypeEnum.TYPE_CAU);
				Element hostElem = elemt.element("host");
				if (hostElem != null) {
					trace.setHost(hostElem.getText());
				}
				Element gtElem = elemt.element("gt");
				if (gtElem != null) {
					trace.setGetTime(gtElem.getText());
				}
				Element cenElement = elemt.element("cen");
				if (cenElement != null) {
					trace.setCenter(cenElement.getText());
				}
				Element codeElement = elemt.element("code");
				if (codeElement != null) {
					trace.setCode(codeElement.getText());
				}
				
				Element pmElem = elemt.element("pm");
				if (pmElem != null) {
					trace.setProcessMethod(pmElem.getText());
				}
				
				Element inElement = elemt.element("in");
				if (inElement != null) {
					trace.setInParam(inElement.getStringValue().trim());
				}
				Element outElement = elemt.element("c");
				if (outElement != null) {
					trace.setResultCount(outElement.getText());
				}

				Element etElement = elemt.element("et");
				if (etElement != null) {
					trace.setUseTime(etElement.getText());
				}
				Element sElem = elemt.element("s");
				if (sElem != null) {
					trace.setSuccess(sElem.getText());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return trace;
	}

	/** 解析Http Trace*/
	public ITrace parseHttpTrace(Element elemt) throws Exception {
		HttpTrace trace = null;
		try {
			if (elemt != null) {
				this.httpCount += 1;
				trace = new HttpTrace();
				trace.setId(elemt.attributeValue("id"));
				trace.setName("Http");
				trace.setType(TraceTypeEnum.TYPE_HTTP);
				trace.setCreateTime(elemt.attributeValue("time"));
				Element urlElem = elemt.element("url");
				if (urlElem != null) {
					trace.setUrl(urlElem.getText());
				}
				Element pmElem = elemt.element("pm");
				if (pmElem != null) {
					trace.setProcessMethod(pmElem.getText());
				}
				Element toElem = elemt.element("to");
				if (toElem != null) {
					trace.setTimeOut(toElem.getText());
				}
				Element scElem = elemt.element("sc");
				if (scElem != null) {
					trace.setStateCode(scElem.getText());
				}
				Element cenElement = elemt.element("cen");
				if (cenElement != null) {
					trace.setCenter(cenElement.getText());
				}
				Element codeElem = elemt.element("code");
				if (codeElem != null) {
					trace.setCode(codeElem.getText());
				}
				Element headElement = elemt.element("h");
				if (headElement != null) {
					trace.setHeader(headElement.getStringValue().trim());
				}
				Element qElement = elemt.element("q");
				if (qElement != null) {
					trace.setInParam(qElement.getStringValue().trim());
				}
				Element bElement = elemt.element("b");
				if (bElement != null) {
					trace.setResult(bElement.getStringValue().trim());
				}
				Element sElem = elemt.element("s");
				if (sElem != null) {
					trace.setSuccess(sElem.getText());
				}
				
				Element etElement = elemt.element("et");
				if (etElement != null) {
					trace.setUseTime(etElement.getText());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return trace;
	}

	/** 解析Ws Trace*/
	public ITrace parseWsTrace(Element elemt) throws Exception {
		WsTrace trace = null;
		try {
			if (elemt != null) {
				this.wsCount += 1;

				trace = new WsTrace();
				trace.setId(elemt.attributeValue("id"));
				trace.setName("Ws");
				trace.setType(TraceTypeEnum.TYPE_WS);
				trace.setCreateTime(elemt.attributeValue("time"));
				Element urlElem = elemt.element("url");
				if (urlElem != null) {
					trace.setUrl(urlElem.getText());
				}
				Element mnElem = elemt.element("mn");
				if (mnElem != null) {
					trace.setMethodName(mnElem.getText());
				}
				Element toElem = elemt.element("to");
				if (toElem != null) {
					trace.setTimeOut(toElem.getText());
				}
				Element cenElement = elemt.element("cen");
				if (cenElement != null) {
					trace.setCenter(cenElement.getText());
				}
				Element codeElem = elemt.element("code");
				if (codeElem != null) {
					trace.setCode(codeElem.getText());
				}
				Element inElement = elemt.element("in");
				if (inElement != null) {
					trace.setInParam(inElement.getStringValue().trim());
				}
				Element outElement = elemt.element("out");
				if (outElement != null) {
					trace.setOutParam(outElement.getStringValue().trim());
				}

				Element etElement = elemt.element("et");
				if (etElement != null) {
					trace.setUseTime(etElement.getText());
				}
				Element sElem = elemt.element("s");
				if (sElem != null) {
					trace.setSuccess(sElem.getText());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return trace;
	}

	/** 解析Jdbc Trace*/
	public ITrace parseJdbcTrace(Element elemt) throws Exception {
		JdbcTrace trace = null;
		try {
			if (elemt != null) {
				this.jdbcCount += 1;
				trace = new JdbcTrace();
				trace.setId(elemt.attributeValue("id"));
				trace.setCreateTime(elemt.attributeValue("time"));
				trace.setType(TraceTypeEnum.TYPE_JDBC);
				Element uElement = elemt.element("u");
				if (uElement != null) {
					trace.setUsername(uElement.getText().trim());
				}
				Element sqlElement = elemt.element("sql");
				if (sqlElement != null) {
					String sql = sqlElement.getText();
					if (StringUtil.isNotBlank(sql)) {
						trace.setSql(sql);
					}
				}
				Element tElement = elemt.element("t");
				if (tElement != null) {
					trace.setJdbcType(tElement.getText());
				}
				Element inElement = elemt.element("in");
				if (inElement != null) {
					trace.setInParam(getStringValue(inElement).trim());
				}
				Element etElement = elemt.element("et");
				if (etElement != null) {
					trace.setUseTime(etElement.getText());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return trace;
	}

	/** 解析Dao Trace*/
	public ITrace parseDaoTrace(Element elemt) throws Exception {
		DaoTrace trace = null;
		try {
			if (elemt != null) {
				this.daoCount += 1;
				trace = new DaoTrace();
				trace.setId(elemt.attributeValue("id"));
				trace.setCreateTime(elemt.attributeValue("time"));
				trace.setType(TraceTypeEnum.TYPE_DAO);
				Element cnElement = elemt.element("cn");
				if (cnElement != null) {
					trace.setClassName(cnElement.getText());
				}
				Element mnElement = elemt.element("mn");
				if (mnElement != null) {
					trace.setMethodName(mnElement.getText());
				}
				Element cenElement = elemt.element("cen");
				if (cenElement != null) {
					trace.setCenter(cenElement.getText());
				}
				Element succElement = elemt.element("s");
				if (succElement != null) {
					trace.setSuccess(succElement.getText());
				}
				Element etElement = elemt.element("et");
				if (etElement != null) {
					trace.setUseTime(etElement.getText());
				}
				List jdbcList = elemt.elements("jdbc");
				if ((jdbcList != null) && (jdbcList.size() > 0)) {
					for (int jc = 0; jc < jdbcList.size(); jc++) {
						Element jdbcItem = (Element) jdbcList.get(jc);
						trace.addChild(parseJdbcTrace(jdbcItem));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return trace;
	}
	
	/** 解析Mem Trace*/
	public ITrace parseBccTrace(Element elemt) throws Exception {
		BccMemTrace trace = null;
		try {
			if (elemt != null) {
				trace = new BccMemTrace();
				trace.setId(elemt.attributeValue("id"));
				trace.setCreateTime(elemt.attributeValue("time"));
				trace.setType(TraceTypeEnum.TYPE_BCC);
				Element hostElem = elemt.element("host");
				if (hostElem != null) {
					trace.setHost(hostElem.getText());
				}
				Element gtElem = elemt.element("gt");
				if (gtElem != null) {
					trace.setGetTime(gtElem.getText());
				}
				Element cenElement = elemt.element("cen");
				if (cenElement != null) {
					trace.setCenter(cenElement.getText());
				}
				Element codeElement = elemt.element("code");
				if (codeElement != null) {
					trace.setCode(codeElement.getText());
				}
				Element inElement = elemt.element("in");
				if (inElement != null) {
					trace.setInParam(getStringValue(inElement));
				}
				Element outElement = elemt.element("c");
				if (outElement != null) {
					trace.setResultCount(outElement.getText());
				}

				Element etElement = elemt.element("et");
				if (etElement != null) {
					trace.setUseTime(etElement.getText());
				}
				Element sElem = elemt.element("s");
				if (sElem != null) {
					trace.setSuccess(sElem.getText());
				}
				
				Element pmElem = elemt.element("pm");
				if (pmElem != null) {
					trace.setProcessMethod(pmElem.getText());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return trace;
	}

	public SimpleParam parseParam(Element elemt) throws Exception {
		SimpleParam param = null;
		try {
			if (elemt != null) {
				param = new SimpleParam();
				String index = elemt.attributeValue("s");
				if (StringUtil.isNotBlank(index)) {
					param.setIndex(index);
				}
				String type = elemt.attributeValue("t");
				if (StringUtil.isNotBlank(type)) {
					param.setType(type);
				}
				StringBuilder sb = new StringBuilder("");
				if (StringUtil.isNotBlank(type)) {
					sb.append("<" + type + ">");

					String paraData = elemt.attributeValue("v");
					sb.append(paraData);
					sb.append("</" + type + ">");
				} else {
					String paraData = elemt.getText();
					sb.append(paraData);
				}

				param.setPicture(sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return param;
	}
	
	
	private String getStringValue(Element element){
	    OutputFormat format = OutputFormat.createPrettyPrint();
	    format.setEncoding("UTF-8");
	    format.setNewlines(true);
	    format.setExpandEmptyElements(true);
	   
	    StringWriter writer;
		try {
			writer = new StringWriter();
//			XMLWriter xmlwriter = new XMLWriter(writer, format);
			XMLWriter xmlwriter = new MyXMLWriter(writer, format, true);	//去掉CDATA的现实
			xmlwriter.write(element);
			return writer.toString().trim();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}