package com.easyfun.eclipse.performance.appframe.monitor.mon.service.impl;

import java.util.Date;
import java.util.HashMap;

import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.dao.interfaces.IMonDAO;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MidServerControl;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonAccess;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonCExec;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonControl;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonDbAcct;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonDbUrl;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonLRecord;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonNode;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonOpLog;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPExec;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPGrp;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPHost;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPInfo;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPTable;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPThreshold;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPage;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPageTab;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPageTabRelat;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonServer;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonTemplate;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonTree;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonUser;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonWTrigger;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.ParallelUtil;

public class MonSVImpl implements IMonSV {
	/** [MON_TREE] 获取根节点 TREE_ID=1*/
	public MonTree getRootTree() throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getRootTree();
	}

	/** 根据PARENT_ID获取[MON_TREE]记录, STATE='U' order by tree_id desc*/
	public MonTree[] getChildTree(long parentId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getChildTree(parentId);
	}

	/** 根据PARENT_TREE_ID获取所有子节点[MON_NODE]*/
	public MonNode[] getChildNode(long parentTreeId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getChildNode(parentTreeId);
	}

	/** 根据NODE_IDE获取[MON_NODE]记录信息*/
	public MonNode getMonNodeByNodeId(long nodeId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonNodeByNodeId(nodeId);
	}

	/** 根据PAGE_ID获取[MON_PAGE]记录信息*/
	public MonPage getMonPageByPageId(long nodeId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonPageByPageId(nodeId);
	}

	/** 根据PAGE_ID获取[MON_PAGE_TAB]记录 order by relate_id asc*/
	public MonPageTabRelat[] getMonPageTabRelatByPageId(long pageId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonPageTabRelatByPageId(pageId);
	}

	/** 根据TAB_ID获取[MON_PAGE_TAB]记录 state='U'*/
	public MonPageTab getMonPageTabByTabId(long tabId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonPageTabByTabId(tabId);
	}

	/** [MON_PAGE_TAB] */
	public MonPageTab[] getMonPageTabByPageId(long pageId) throws Exception {
		MonPageTabRelat[] objMonPageTabRelat = getMonPageTabRelatByPageId(pageId);
		MonPageTab[] rtn = new MonPageTab[objMonPageTabRelat.length];
		for (int i = 0; i < rtn.length; i++) {
			rtn[i] = getMonPageTabByTabId(objMonPageTabRelat[i].getTabId());
		}
		return rtn;
	}

	/** 根据[SERVER_ID]获取[MON_SER]记录*/
	public MonServer getMonServerByServerId(long serverId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonMbeanServerByServerId(serverId);
	}

	/** 根据SERVER_ID列表获取[MON_SERVER]记录列表信息*/
	public MonServer[] getMonServerByServerId(long[] serverId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonServerByServerId(serverId);
	}

	/** 根据SERVER_TYPE获取所有配置的[MON_SERVER]列表*/
	public MonServer[] getMonMbeanServerByServerType(String serverType) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonMbeanServerByServerType(serverType);
	}

	/** 根据TAB_ID获取[MON_CONTROL]记录 STATE='U'*/
	public MonControl getMonControlByTabId(long tabId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonControlByTabId(tabId);
	}

	public MonTemplate[] getMonTemplate() throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonTemplate();
	}

	/** 
	 * 插入以下表的信息
	 * <li>mon_node
	 * <li>mon_page
	 * <li>mon_page_tab_relat
	 * <li>mon_mbean_server
	 * <li>mon_mbean_server_mapping
	 */
	public void addServer(long templateId, long parentTreeId, String nodeName, String serverName, String serverImpl, HashMap mapping) throws Exception {
		((IMonDAO) ServiceFactory.getService(IMonDAO.class)).addServer(templateId, parentTreeId, nodeName, serverName, serverImpl, mapping);
	}

	public void insertMonControlLog(long controlId, String script, Date startDate, Date endDate, String results) throws Exception {
		((IMonDAO) ServiceFactory.getService(IMonDAO.class)).insertMonControlLog(controlId, script, startDate, endDate, results);
	}

	/** 根据用户名查询[MON_USER]*/
	public MonUser getMonUser(String username) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonUser(username);
	}

	/** 查询所有[MON_P_INFO]表配置，[MON_P_TIME.EXPR]
	 * <li>[MON_P_INFO.TIME_ID]=[MON_P_TIME.EXPR.TIME_ID]
	 * <li>[MON_P_INFO.THRESHOLD_ID]=MON_P_THRESHOLD.THRESHOLD_ID]
	 * <li>[MON_P_INFO.STATE='U'] [MON_P_TIME.STATE='U'] MON_P_THRESHOLD.STATE='U']
	 * */
	public MonPInfo[] getAllMonPInfo() throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getAllMonPInfo();
	}

	/** 根据EXEC_ID获取对应的[MON_P_EXEC]记录*/
	public MonPExec getMonPExecByExecId(long execId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonPExecByExecId(execId);
	}

	/** [MON_L_RECORD]*/
	public void insertMonLRecord(MonLRecord objMonLRecord) throws Exception {
		((IMonDAO) ServiceFactory.getService(IMonDAO.class)).insertMonLRecord(objMonLRecord);
	}

	/** 根据THRESHOLD_ID获取[MON_P_THRESHOLD]记录*/
	public MonPThreshold getMonPThresholdByThresholId(long thresholcId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonPThresholdByThresholId(thresholcId);
	}

	/** 
	 * <li>[MON_W_DTL]
	 * <li>[MON_W_PERSON]
	 * */
	public String[] getPhonenumByInfoId(long infoId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getPhonenumByInfoId(infoId);
	}

	/** [MON_W_TRIGGER]*/
	public void insertMonWTrigger(MonWTrigger objMonWTrigger) throws Exception {
		((IMonDAO) ServiceFactory.getService(IMonDAO.class)).insertMonWTrigger(objMonWTrigger);
	}

	/** 根据HOSTNAME获取[MON_P_HOST]记录 STATE='U'*/
	public MonPHost getMonPHostByHostname(String hostname) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonPHostByHostname(hostname);
	}

	/** 获取Sequence的下一个值*/
	public long getNextId(String seq) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getNextId(seq);
	}

	/** 根据DB_ACCT_CODE获取[MON_DB_ACCT]记录*/
	public MonDbAcct getMonDbAcctByDbAcctCode(String dbAcctCode) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonDbAcctByDbAcctCode(dbAcctCode);
	}

	/** 根据NAME获取[MON_DB_URL]记录*/
	public MonDbUrl getMonDbUrlByUrlName(String urlName) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonDbUrlByUrlName(urlName);
	}

	/** 根据TABLE_ID获取[MON_P_TABLE]记录*/
	public MonPTable getMonPTableByTableId(long tableId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonPTableByTableId(tableId);
	}

	/** 获取指定年月的[MON_L_RECORD]记录数量*/
	public long countMonLRecordByCondition(String condition, int[] yyyymm) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).countMonLRecordByCondition(condition, yyyymm);
	}

	/** 根据条件和年月获取[MON_l_RECORD]的记录*/
	public MonLRecord[] getMonLRecordByCondition(String condition, int[] yyyymm, long startIndex, long endIndex) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonLRecordByCondition(condition, yyyymm, startIndex, endIndex);
	}

	/** 根据条件condition获取表mon_w_trigger_his的记录数*/
	public long countMonWTriggerByCondition(String condition, int yyyymm) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).countMonWTriggerByCondition(condition, yyyymm);
	}

	/** 根据条件获取记录列表[mon_w_trigger_his]*/
	public MonWTrigger[] getMonWTriggerByCondition(String condition, int yyyymm, long startIndex, long endIndex) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonWTriggerByCondition(condition, yyyymm, startIndex, endIndex);
	}

	/** 将 MON_L_RECORD的内容转换为HashMap */
	public HashMap getMonLRecord4Image(long[] infoId, String transformClass, Date startDate, Date endDate) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonLRecord4Image(infoId, transformClass, startDate, endDate);
	}

	/** 根据GRP_ID获取[MON_P_INFO]记录*/
	public MonPInfo[] getMonPInfoByGrpId(long grpId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonPInfoByGrpId(grpId);
	}

	/** 获取[MON_P_GRP]的所有记录 state='U' order by sort_by asc*/
	public MonPGrp[] getAllMonPGrp() throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getAllMonPGrp();
	}

	public MonPInfo[] getMonPInfoByCondition(String condition, long startIndex, long endIndex) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonPInfoByCondition(condition, startIndex, endIndex);
	}

	/** 根据条件查询[MON_P_INFO的记录数]*/
	public long countMonPInfoByCondition(String condition) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).countMonPInfoByCondition(condition);
	}

	/** 根据GRP_ID获取表[MON_P_GRP]的记录*/
	public MonPGrp getMonPGrpByGrpId(long grpId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonPGrpByGrpId(grpId);
	}

	/** 根据条件获取[MON_C_SERVER]的记录数*/
	public long countMidServerServerControlByCondition(String condition) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).countMidServerServerControlByCondition(condition);
	}

	/** 根据条件condition，根据条件[MON_C_SERVER] order by url desc*/
	public MidServerControl[] getMidServerServerControlByCondition(String condition, int threadCount, int timeout, long startIndex, long endIndex) throws Exception {
		MidServerControl[] rtn = null;
		rtn = ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMidServerServerControlByCondition(condition, startIndex, endIndex);

		return ParallelUtil.computeUrl(threadCount, timeout, rtn);
	}

	/**分组列表 查询所有的[MON_C_SERVER.GRP_NAME]字段distinct列表*/
	public String[] getAllMidServerControlGrp() throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getAllMidServerControlGrp();
	}

	/** 根据EXEC_ID 获取表[mon_c_exec]记录*/
	public MonCExec getMonCExecByExecId(long execId) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonCExecByExecId(execId);
	}

	/** 根据SERVER_NAME获取记录[MON_C_SERVER]*/
	public MidServerControl getMidServerServerControlByServerName(String serverName) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMidServerServerControlByServerName(serverName);
	}

	/** 获取[MON_P_HOST]记录, 根据[MON_P_HOST] [MON_C_SERVER]*/
	public MonPHost[] getMonPHostForTrace() throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonPHostForTrace();
	}

	/** 根据SERVER_TYPE获取[MON_SERVER]列表*/
	public MonServer[] getMonMbeanServerByServerTypeOrderByServerName(String serverType) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonMbeanServerByServerTypeOrderByServerName(serverType);
	}

	/** 根据NAME获取[MON_SERVER]记录*/
	public MonServer getMonServerByServerName(String serverName) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonServerByServerName(serverName);
	}
	
	/** 检查HotPatch密码 [MON_HOTPATCH_PASSWORD]*/
	public boolean checkHotPatchPassword(String password) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).checkHotPatchPassword(password);
	}
	
	public void insertMonOpLog(MonOpLog objMonOpLog) throws Exception {
		((IMonDAO) ServiceFactory.getService(IMonDAO.class)).insertMonOpLog(objMonOpLog);
	}

	/** 
	 * <li>[MON_GROUP_ACCESS_RELAT]
	 * <li>[MON_GROUP]
	 * */
	public MonAccess[] getMonAccessByUsername(String username) throws Exception {
		return ((IMonDAO) ServiceFactory.getService(IMonDAO.class)).getMonAccessByUsername(username);
	}

	public void updatePwdByUsername(String username, String newPwd) throws Exception {
		((IMonDAO) ServiceFactory.getService(IMonDAO.class)).updatePwdByUsername(username, newPwd);
	}
}