package com.easyfun.eclipse.performance.appframe.monitor.mon.dao.interfaces;

import java.util.Date;
import java.util.HashMap;

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

public interface IMonDAO {
	/** [MON_TREE] ��ȡ���ڵ� TREE_ID=1*/
	public MonTree getRootTree() throws Exception;

	/** ����PARENT_ID��ȡ[MON_TREE]��¼, STATE='U' order by tree_id desc*/
	public MonTree[] getChildTree(long parentId) throws Exception;

	/** ����PARENT_TREE_ID��ȡ�����ӽڵ�[MON_NODE]*/
	public MonNode[] getChildNode(long parentTreeId) throws Exception;

	/** ����NODE_IDE��ȡ[MON_NODE]��¼��Ϣ*/
	public MonNode getMonNodeByNodeId(long nodeId) throws Exception;

	/** ����PAGE_ID��ȡ[MON_PAGE]��¼��Ϣ*/
	public MonPage getMonPageByPageId(long pageId) throws Exception;

	/** ����PAGE_ID��ȡ[MON_PAGE_TAB]��¼ order by relate_id asc*/
	public MonPageTabRelat[] getMonPageTabRelatByPageId(long pageId) throws Exception;

	/** ����TAB_ID��ȡ[MON_PAGE_TAB]��¼ state='U'*/
	public MonPageTab getMonPageTabByTabId(long tabId) throws Exception;

	/** ����[SERVER_ID]��ȡ[MON_SER]��¼*/
	public MonServer getMonMbeanServerByServerId(long serverId) throws Exception;

	/** ����SERVER_ID�б��ȡ[MON_SERVER]��¼�б���Ϣ*/
	public MonServer[] getMonServerByServerId(long[] serverId) throws Exception;

	/** ����SERVER_TYPE��ȡ�������õ�[MON_SERVER]�б�*/
	public MonServer[] getMonMbeanServerByServerType(String serverType) throws Exception;

	/** ����TAB_ID��ȡ[MON_CONTROL]��¼ STATE='U'*/
	public MonControl getMonControlByTabId(long tabId) throws Exception;

	public MonTemplate[] getMonTemplate() throws Exception;

	/** 
	 * �������±����Ϣ
	 * <li>mon_node
	 * <li>mon_page
	 * <li>mon_page_tab_relat
	 * <li>mon_mbean_server
	 * <li>mon_mbean_server_mapping
	 */
	public void addServer(long templateId, long parentTreeId, String nodeName, String serverName, String serverImpl, HashMap mapping) throws Exception;

	public void insertMonControlLog(long controlId, String script, Date startDate, Date endDate, String results) throws Exception;

	/** �����û�����ѯ[MON_USER]*/
	public MonUser getMonUser(String username) throws Exception;

	/** ��ѯ����[MON_P_INFO]�����ã�[MON_P_TIME.EXPR]
	 * <li>[MON_P_INFO.TIME_ID]=[MON_P_TIME.EXPR.TIME_ID]
	 * <li>[MON_P_INFO.THRESHOLD_ID]=MON_P_THRESHOLD.THRESHOLD_ID]
	 * <li>[MON_P_INFO.STATE='U'] [MON_P_TIME.STATE='U'] MON_P_THRESHOLD.STATE='U']
	 * */
	public MonPInfo[] getAllMonPInfo() throws Exception;

	/** ����EXEC_ID��ȡ��Ӧ��[MON_P_EXEC]��¼*/
	public MonPExec getMonPExecByExecId(long execId) throws Exception;

	/** [MON_L_RECORD]*/
	public void insertMonLRecord(MonLRecord paramMonLRecord) throws Exception;

	/** ����THRESHOLD_ID��ȡ[MON_P_THRESHOLD]��¼*/
	public MonPThreshold getMonPThresholdByThresholId(long thresholcId) throws Exception;

	/** ����HOSTNAME��ȡ[MON_P_HOST]��¼ STATE='U'*/
	public MonPHost getMonPHostByHostname(String hostname) throws Exception;

	/** 
	 * <li>[MON_W_DTL]
	 * <li>[MON_W_PERSON]
	 * */
	public String[] getPhonenumByInfoId(long infoId) throws Exception;

	/** [MON_W_TRIGGER]*/
	public void insertMonWTrigger(MonWTrigger paramMonWTrigger) throws Exception;

	/** ��ȡSequence����һ��ֵ*/
	public long getNextId(String seq) throws Exception;

	/** ����DB_ACCT_CODE��ȡ[MON_DB_ACCT]��¼*/
	public MonDbAcct getMonDbAcctByDbAcctCode(String paramString) throws Exception;

	/** ����NAME��ȡ[MON_DB_URL]��¼*/
	public MonDbUrl getMonDbUrlByUrlName(String urlName) throws Exception;

	/** ����TABLE_ID��ȡ[MON_P_TABLE]��¼*/
	public MonPTable getMonPTableByTableId(long tableId) throws Exception;

	/** ��ȡָ�����µ�[MON_L_RECORD]��¼����*/
	public long countMonLRecordByCondition(String condition, int[] yyyymm) throws Exception;

	/** �������������»�ȡ[MON_l_RECORD]�ļ�¼*/
	public MonLRecord[] getMonLRecordByCondition(String condition, int[] yyyymm, long startIndex, long endIndex) throws Exception;

	/** ��������condition��ȡ��mon_w_trigger_his�ļ�¼��*/
	public long countMonWTriggerByCondition(String condition, int yyyymm) throws Exception;

	/** ����������ȡ��¼�б�[mon_w_trigger_his]*/
	public MonWTrigger[] getMonWTriggerByCondition(String condition, int yyyymm, long startIndex, long endIndex) throws Exception;

	/** �� MON_L_RECORD������֮��ΪHashMap */
	public HashMap getMonLRecord4Image(long[] infoId, String transformClass, Date startDate, Date endDate) throws Exception;

	/** ����GRP_ID��ȡ[MON_P_INFO]��¼*/
	public MonPInfo[] getMonPInfoByGrpId(long grpId) throws Exception;

	/** ��ȡ[MON_P_GRP]�����м�¼ state='U' order by sort_by asc*/
	public MonPGrp[] getAllMonPGrp() throws Exception;

	/** ��������condition��ȡ[MON_P_INFO]*/
	public MonPInfo[] getMonPInfoByCondition(String condition, long startIndex, long endIndex) throws Exception;

	/** ����������ѯ[MON_P_INFO�ļ�¼��]*/
	public long countMonPInfoByCondition(String condition) throws Exception;

	/** ����GRP_ID��ȡ��[MON_P_GRP]�ļ�¼*/
	public MonPGrp getMonPGrpByGrpId(long grpId) throws Exception;

	/** ����������ȡ[MON_C_SERVER]�ļ�¼��*/
	public long countMidServerServerControlByCondition(String condition) throws Exception;

	/** ��������condition����������[MON_C_SERVER] order by url desc*/
	public MidServerControl[] getMidServerServerControlByCondition(String condition, long startIndex, long endIndex) throws Exception;

	/**�����б� ��ѯ���е�[MON_C_SERVER.GRP_NAME]�ֶ�distinct�б�*/
	public String[] getAllMidServerControlGrp() throws Exception;

	/** ����EXEC_ID ��ȡ��[mon_c_exec]��¼*/
	public MonCExec getMonCExecByExecId(long execId) throws Exception;

	/** ����SERVER_NAME��ȡ��¼[MON_C_SERVER]*/
	public MidServerControl getMidServerServerControlByServerName(String serverName) throws Exception;

	/** ��ȡ[MON_P_HOST]��¼, ����[MON_P_HOST] [MON_C_SERVER]*/
	public MonPHost[] getMonPHostForTrace() throws Exception;

	/** ����SERVER_TYPE��ȡ[MON_SERVER]�б�*/
	public MonServer[] getMonMbeanServerByServerTypeOrderByServerName(String serverType) throws Exception;

	/** ����NAME��ȡ[MON_SERVER]��¼*/
	public MonServer getMonServerByServerName(String serverName) throws Exception;

	/** ���HotPatch���� [MON_HOTPATCH_PASSWORD]*/
	public boolean checkHotPatchPassword(String password) throws Exception;
	
	public abstract void insertMonOpLog(MonOpLog objMonOpLog) throws Exception;

	/** 
	 * <li>[MON_GROUP_ACCESS_RELAT]
	 * <li>[MON_GROUP]
	 * */
	public MonAccess[] getMonAccessByUsername(String username) throws Exception;

	public void updatePwdByUsername(String username, String newPwd) throws Exception;
}