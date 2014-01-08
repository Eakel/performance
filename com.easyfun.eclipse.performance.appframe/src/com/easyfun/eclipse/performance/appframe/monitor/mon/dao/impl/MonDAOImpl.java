package com.easyfun.eclipse.performance.appframe.monitor.mon.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;

import com.asiainfo.mon.util.MD5;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.session.SessionManager;
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
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.LineValue;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.MiscUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.TimeUtil;
import com.easyfun.eclipse.performance.appframe.monitor.mon.util.transform.ITransform;
import com.easyfun.eclipse.performance.appframe.webtool.util.appf.K;

public class MonDAOImpl implements IMonDAO {
	/** [MON_TREE] 获取根节点 TREE_ID=1*/
	public MonTree getRootTree() throws Exception {
		MonTree rtn = new MonTree();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_tree where tree_id = 1 and state='U'");
			rs = ptmt.executeQuery();
			int i = 0;
			if (rs.next()) {
				rtn.setTreeId(rs.getLong("TREE_ID"));
				rtn.setName(rs.getString("NAME"));
				rtn.setTreeType(rs.getString("TREE_TYPE"));
				rtn.setParentId(rs.getLong("PARENT_ID"));
				i++;
			}

			if (i != 1)
				throw new Exception("查询的记录数不唯一");
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return rtn;
	}

	/** 根据PARENT_ID获取[MON_TREE]记录, STATE='U' order by tree_id desc*/
	public MonTree[] getChildTree(long parentId) throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_tree where parent_id = ? and state='U' order by tree_id desc");
			ptmt.setLong(1, parentId);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MonTree obj = new MonTree();
				obj.setTreeId(rs.getLong("TREE_ID"));
				obj.setName(rs.getString("NAME"));
				obj.setTreeType(rs.getString("TREE_TYPE"));
				obj.setParentId(rs.getLong("PARENT_ID"));
				rtn.add(obj);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return (MonTree[]) (MonTree[]) rtn.toArray(new MonTree[0]);
	}

	/** 根据PARENT_TREE_ID获取所有子节点[MON_NODE]*/
	public MonNode[] getChildNode(long parentTreeId) throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_node where tree_parent_id = ? and state='U' order by node_id asc");
			ptmt.setLong(1, parentTreeId);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MonNode obj = new MonNode();
				obj.setNodeId(rs.getLong("NODE_ID"));
				obj.setName(rs.getString("NAME"));
				obj.setUrl(rs.getString("URL"));
				obj.setNodeImg(rs.getString("NODE_IMG"));
				obj.setNodeType(rs.getString("NODE_TYPE"));
				obj.setTreeParentId(rs.getLong("TREE_PARENT_ID"));
				rtn.add(obj);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return (MonNode[]) (MonNode[]) rtn.toArray(new MonNode[0]);
	}

	/** 根据NODE_IDE获取[MON_NODE]记录信息*/
	public MonNode getMonNodeByNodeId(long nodeId) throws Exception {
	    MonNode rtn = new MonNode();
	    Connection conn = null;
	    PreparedStatement ptmt = null;
	    ResultSet rs = null;
	    try {
	      conn = SessionManager.getSession().getConnection();
	      ptmt = conn.prepareStatement("select * from mon_node where node_id = ? and state='U'");
	      ptmt.setLong(1, nodeId);
	      rs = ptmt.executeQuery();
	      int i = 0;
	      if (rs.next()) {
	        rtn.setNodeId(rs.getLong("NODE_ID"));
	        rtn.setName(rs.getString("NAME"));
	        rtn.setUrl(rs.getString("URL"));
	        rtn.setNodeImg(rs.getString("NODE_IMG"));
	        rtn.setNodeType(rs.getString("NODE_TYPE"));
	        rtn.setTreeParentId(rs.getLong("TREE_PARENT_ID"));
	        rtn.setPageId(rs.getLong("PAGE_ID"));
	        i++;
	      }

	      if (i != 1)
	        throw new Exception("查询的记录数不唯一");
	    }
	    catch (Exception ex)
	    {
	      throw ex;
	    }
	    finally {
	      if (rs != null) {
	        rs.close();
	      }
	      if (ptmt != null) {
	        ptmt.close();
	      }
	      if (conn != null) {
	        conn.close();
	      }
	    }

	    return rtn;
	}

	/** 根据PAGE_ID获取[MON_PAGE]记录信息*/
	public MonPage getMonPageByPageId(long pageId) throws Exception {
	    MonPage rtn = new MonPage();
	    Connection conn = null;
	    PreparedStatement ptmt = null;
	    ResultSet rs = null;
	    try {
	      conn = SessionManager.getSession().getConnection();
	      ptmt = conn.prepareStatement("select * from mon_page where page_id = ? and state='U'");
	      ptmt.setLong(1, pageId);
	      rs = ptmt.executeQuery();
	      int i = 0;
	      if (rs.next()) {
	        rtn.setPageId(rs.getLong("PAGE_ID"));
	        rtn.setServerId(rs.getLong("SERVER_ID"));
	        rtn.setPageTitle(rs.getString("PAGE_TITLE"));
	        rtn.setPageUrl(rs.getString("PAGE_URL"));
	        i++;
	      }

	      if (i != 1)
	        throw new Exception("查询的记录数不唯一");
	    }
	    catch (Exception ex)
	    {
	      throw ex;
	    }
	    finally {
	      if (rs != null) {
	        rs.close();
	      }
	      if (ptmt != null) {
	        ptmt.close();
	      }
	      if (conn != null) {
	        conn.close();
	      }
	    }

	    return rtn;
	}

	/** 根据PAGE_ID获取[MON_PAGE_TAB]记录 order by relate_id asc*/
	public MonPageTabRelat[] getMonPageTabRelatByPageId(long pageId) throws Exception {
	    List rtn = new ArrayList();
	    Connection conn = null;
	    PreparedStatement ptmt = null;
	    ResultSet rs = null;
	    try {
	      conn = SessionManager.getSession().getConnection();
	      ptmt = conn.prepareStatement("select * from mon_page_tab_relat where page_id = ? and state='U' order by relat_id asc ");
	      ptmt.setLong(1, pageId);
	      rs = ptmt.executeQuery();
	      while (rs.next()) {
	        MonPageTabRelat obj = new MonPageTabRelat();
	        obj.setRelatId(rs.getLong("RELAT_ID"));
	        obj.setPageId(rs.getLong("PAGE_ID"));
	        obj.setTabId(rs.getLong("TAB_ID"));
	        rtn.add(obj);
	      }
	    }
	    catch (Exception ex) {
	      throw ex;
	    }
	    finally {
	      if (rs != null) {
	        rs.close();
	      }
	      if (ptmt != null) {
	        ptmt.close();
	      }
	      if (conn != null) {
	        conn.close();
	      }
	    }

	    return (MonPageTabRelat[])(MonPageTabRelat[])rtn.toArray(new MonPageTabRelat[0]);
	}

	/** 根据TAB_ID获取[MON_PAGE_TAB]记录 state='U'*/
	public MonPageTab getMonPageTabByTabId(long tabId) throws Exception {
		MonPageTab rtn = new MonPageTab();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_page_tab where tab_id = ? and state='U'");
			ptmt.setLong(1, tabId);
			rs = ptmt.executeQuery();
			int i = 0;
			if (rs.next()) {
				rtn.setTabId(rs.getLong("TAB_ID"));
				rtn.setTabTitle(rs.getString("TAB_TITLE"));
				rtn.setTabUrl(rs.getString("TAB_URL"));
				i++;
			}

			if (i != 1)
				throw new Exception("查询的记录数不唯一");
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return rtn;
	}

	/** 根据[SERVER_ID]获取[MON_SER]记录*/
	public MonServer getMonMbeanServerByServerId(long serverId) throws Exception {
		MonServer rtn = new MonServer();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_server where server_id = ? and state='U'");
			ptmt.setLong(1, serverId);
			rs = ptmt.executeQuery();
			int i = 0;
			if (rs.next()) {
				rtn.setServerId(rs.getLong("SERVER_ID"));
				rtn.setName(rs.getString("NAME"));
				rtn.setLocator(rs.getString("LOCATOR"));
				rtn.setServerType(rs.getString("SERVER_TYPE"));
				i++;
			}

			if(i==0){
				throw new Exception("找不到对应的记录, serverId=" + serverId);
			}else if (i != 1) {
				throw new Exception("查询的记录数不唯一");
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return rtn;
	}

	/** 根据NAME获取[MON_SERVER]记录*/
	public MonServer getMonServerByServerName(String serverName) throws Exception {
		MonServer obj = null;
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_server where name = ? ");
			ptmt.setString(1, serverName);
			rs = ptmt.executeQuery();
			if (rs.next()) {
				obj = new MonServer();
				obj.setServerId(rs.getLong("SERVER_ID"));
				obj.setName(rs.getString("NAME"));
				obj.setLocator(rs.getString("LOCATOR"));
				obj.setServerType(rs.getString("SERVER_TYPE"));
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return obj;
	}

	/** 根据SERVER_ID列表获取[MON_SERVER]记录列表信息*/
	public MonServer[] getMonServerByServerId(long[] serverId) throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();

			List l = new ArrayList();
			for (int i = 0; i < serverId.length; i++) {
				l.add(String.valueOf(serverId[i]));
			}

			ptmt = conn.prepareStatement("select * from mon_server where server_id in ( " + StringUtils.join(l.iterator(), ",") + " ) ");
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MonServer obj = new MonServer();
				obj.setServerId(rs.getLong("SERVER_ID"));
				obj.setName(rs.getString("NAME"));
				obj.setLocator(rs.getString("LOCATOR"));
				obj.setServerType(rs.getString("SERVER_TYPE"));
				rtn.add(obj);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return (MonServer[]) rtn.toArray(new MonServer[0]);
	}

	/** 根据SERVER_TYPE获取[MON_SERVER]列表*/
	public MonServer[] getMonMbeanServerByServerTypeOrderByServerName(String serverType) throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_server where server_type = ? and state='U' order by name");
			ptmt.setString(1, serverType);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MonServer obj = new MonServer();
				obj.setServerId(rs.getLong("SERVER_ID"));
				obj.setName(rs.getString("NAME"));
				obj.setLocator(rs.getString("LOCATOR"));
				obj.setServerType(rs.getString("SERVER_TYPE"));
				rtn.add(obj);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return (MonServer[]) rtn.toArray(new MonServer[0]);
	}

	/** 根据SERVER_TYPE获取所有配置的[MON_SERVER]列表*/
	public MonServer[] getMonMbeanServerByServerType(String serverType) throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_server where server_type = ? and state='U' order by server_id");
			ptmt.setString(1, serverType);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MonServer obj = new MonServer();
				obj.setServerId(rs.getLong("SERVER_ID"));
				obj.setName(rs.getString("NAME"));
				obj.setLocator(rs.getString("LOCATOR"));
				obj.setServerType(rs.getString("SERVER_TYPE"));
				rtn.add(obj);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return (MonServer[]) rtn.toArray(new MonServer[0]);
	}

	/** 根据TAB_ID获取[MON_CONTROL]记录 STATE='U'*/
	public MonControl getMonControlByTabId(long tabId) throws Exception {
		MonControl rtn = new MonControl();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_control where tab_id = ? and state='U'");
			ptmt.setLong(1, tabId);
			rs = ptmt.executeQuery();
			int i = 0;
			if (rs.next()) {
				rtn.setControlId(rs.getLong("CONTROL_ID"));
				rtn.setTabId(rs.getLong("TAB_ID"));
				rtn.setConnectType(rs.getString("CONNECT_TYPE"));
				rtn.setHost(rs.getString("HOST"));
				rtn.setPort(rs.getLong("PORT"));
				rtn.setScriptShowStatus(rs.getString("SCRIPT_SHOW_STATUS"));
				rtn.setScriptStop(rs.getString("SCRIPT_STOP"));
				rtn.setScirptStart(rs.getString("SCIRPT_START"));
				i++;
			}

			if (i != 1) {
				throw new Exception("查询的记录数不唯一");
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return rtn;
	}

	public MonTemplate[] getMonTemplate() throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select distinct a.template_id,a.template_name from mon_template a  where a.state='U'");
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MonTemplate obj = new MonTemplate();
				obj.setTemplateId(rs.getLong("TEMPLATE_ID"));
				obj.setTemplateName(rs.getString("TEMPLATE_NAME"));
				rtn.add(obj);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return (MonTemplate[]) rtn.toArray(new MonTemplate[0]);
	}

	public MonTemplate[] getMonTemplateByTemplateId(Connection conn, long templateId) throws Exception {
		List rtn = new ArrayList();
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement("select * from mon_template  where state='U' and template_id = ?");
			ptmt.setLong(1, templateId);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MonTemplate obj = new MonTemplate();
				obj.setTemplateId(rs.getLong("TEMPLATE_ID"));
				obj.setTabId(rs.getLong("TAB_ID"));
				rtn.add(obj);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
		}

		return (MonTemplate[]) rtn.toArray(new MonTemplate[0]);
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
		Connection conn = null;
		PreparedStatement ptmt = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("insert into mon_node (node_id,name,url,node_type,tree_parent_id,state,page_id)values(?,?,?,?,?,?,?)");
			long nodeId = getMonConfigNextId(conn);
			long pageId = getMonConfigNextId(conn);
			long serverId = getMonConfigNextId(conn);
			ptmt.setLong(1, nodeId);
			ptmt.setString(2, nodeName);
			ptmt.setString(3, "/mon/MonitorPage.jsp");
			ptmt.setString(4, "NODE");
			ptmt.setLong(5, parentTreeId);
			ptmt.setString(6, "U");
			ptmt.setLong(7, pageId);
			ptmt.execute();

			ptmt = conn.prepareStatement("insert into mon_page (page_id,server_id,page_title,state)values(?,?,?,?)");
			ptmt.setLong(1, pageId);
			ptmt.setLong(2, serverId);
			ptmt.setString(3, nodeName);
			ptmt.setString(4, "U");
			ptmt.execute();

			MonTemplate[] objMonTemplate = getMonTemplateByTemplateId(conn, templateId);
			if ((objMonTemplate != null) && (objMonTemplate.length != 0)) {
				ptmt = conn.prepareStatement("insert into mon_page_tab_relat (relat_id,page_id,tab_id,state)values(?,?,?,?)");
				for (int i = 0; i < objMonTemplate.length; i++) {
					ptmt.setLong(1, getMonConfigNextId(conn));
					ptmt.setLong(2, pageId);
					ptmt.setLong(3, objMonTemplate[i].getTabId());
					ptmt.setString(4, "U");
					ptmt.addBatch();
				}
				ptmt.executeBatch();
				ptmt.clearBatch();
			} else {
				throw new Exception("模板ID对应的数据条数为零");
			}

			ptmt = conn.prepareStatement("insert into mon_mbean_server (server_id,name,server_impl_class,state)values(?,?,?,?)");
			ptmt.setLong(1, serverId);
			ptmt.setString(2, serverName);
			ptmt.setString(3, serverImpl);
			ptmt.setString(4, "U");
			ptmt.execute();

			if (!mapping.isEmpty()) {
				ptmt = conn.prepareStatement("insert into mon_mbean_server_mapping (mapping_id,server_id,key,value,state)values(?,?,?,?,?)");
				Set keys = mapping.keySet();
				for (Iterator iter = keys.iterator(); iter.hasNext();) {
					String item = (String) iter.next();
					ptmt.setLong(1, getMonConfigNextId(conn));
					ptmt.setLong(2, serverId);
					ptmt.setString(3, item);
					ptmt.setString(4, (String) mapping.get(item));
					ptmt.setString(5, "U");
					ptmt.addBatch();
				}
				ptmt.executeBatch();
				ptmt.clearBatch();
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	public void insertMonControlLog(long controlId, String script, Date startDate, Date endDate, String results) throws Exception {
		Connection conn = null;
		PreparedStatement ptmt = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("insert into mon_control_log values(mon_control_log$seq.nextval,?,?,?,?,?)");
			ptmt.setLong(1, controlId);
			ptmt.setString(2, script);
			ptmt.setTimestamp(3, new Timestamp(startDate.getTime()));
			ptmt.setTimestamp(4, new Timestamp(endDate.getTime()));
			ptmt.setString(5, results);

			ptmt.execute();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null)
				conn.close();
		}
	}

	public long getMonConfigNextId(Connection conn) throws Exception {
		long rtn = 0L;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement("select MON_CONFIG$SEQ.nextval as a from dual");

			rs = ptmt.executeQuery();
			if (rs.next()) {
				rtn = rs.getLong("A");
			}

		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
		}
		return rtn;
	}

	/** 根据用户名查询[MON_USER]*/
	public MonUser getMonUser(String username) throws Exception {
		MonUser rtn = new MonUser();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select USER_ID,USERNAME,PASSWORD from MON_USER WHERE USERNAME=? AND STATE='U' ");
			ptmt.setString(1, username);
			rs = ptmt.executeQuery();
			int i = 0;
			if (rs.next()) {
				rtn.setUserId(rs.getLong("USER_ID"));
				rtn.setUsername(rs.getString("USERNAME"));
				rtn.setPassword(rs.getString("PASSWORD"));
				i++;
			}

			if (i != 1) {
				throw new Exception("查询的记录数不唯一");
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return rtn;
	}

	/** 查询所有[MON_P_INFO]表配置，[MON_P_TIME.EXPR]
	 * <li>[MON_P_INFO.TIME_ID]=[MON_P_TIME.EXPR.TIME_ID]
	 * <li>[MON_P_INFO.THRESHOLD_ID]=MON_P_THRESHOLD.THRESHOLD_ID]
	 * <li>[MON_P_INFO.STATE='U'] [MON_P_TIME.STATE='U'] MON_P_THRESHOLD.STATE='U']
	 * */
	public MonPInfo[] getAllMonPInfo() throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select a.*, b.expr from mon_p_info a,mon_p_time b,mon_p_threshold c where a.time_id = b.time_id and a.threshold_id = c.threshold_id and a.state='U' and b.state='U' and c.state='U'");
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MonPInfo objMonPInfo = new MonPInfo();
				objMonPInfo.setExpr(rs.getString("EXPR"));
				objMonPInfo.setInfoId(rs.getLong("INFO_ID"));
				objMonPInfo.setName(rs.getString("NAME"));
				objMonPInfo.setHostname(rs.getString("HOSTNAME"));
				objMonPInfo.setTypeId(rs.getLong("TYPE_ID"));
				objMonPInfo.setThresholdId(rs.getLong("THRESHOLD_ID"));

				objMonPInfo.setSplitRuleId(rs.getLong("SPLIT_RULE_ID"));
				objMonPInfo.setTimeId(rs.getLong("TIME_ID"));
				objMonPInfo.setType(rs.getString("TYPE"));
				objMonPInfo.setBusiArea(rs.getString("BUSI_AREA"));
				rtn.add(objMonPInfo);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return (MonPInfo[]) rtn.toArray(new MonPInfo[0]);
	}

	/** 根据EXEC_ID获取对应的[MON_P_EXEC]记录*/
	public MonPExec getMonPExecByExecId(long execId) throws Exception {
		MonPExec rtn = new MonPExec();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_p_exec WHERE exec_id=? AND STATE='U' ");
			ptmt.setLong(1, execId);
			rs = ptmt.executeQuery();
			int i = 0;
			if (rs.next()) {
				rtn.setExecId(rs.getLong("EXEC_ID"));
				rtn.setName(rs.getString("NAME"));
				rtn.setType(rs.getString("TYPE"));
				rtn.setExpr(rs.getString("EXPR"));
				i++;
			}

			if (i != 1) {
				throw new Exception("查询的记录数不唯一");
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return rtn;
	}

	/** 根据THRESHOLD_ID获取[MON_P_THRESHOLD]记录*/
	public MonPThreshold getMonPThresholdByThresholId(long thresholcId) throws Exception {
		MonPThreshold rtn = new MonPThreshold();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_p_threshold WHERE threshold_id=? AND STATE='U' ");
			ptmt.setLong(1, thresholcId);
			rs = ptmt.executeQuery();
			int i = 0;
			if (rs.next()) {
				rtn.setThresholdId(rs.getLong("THRESHOLD_ID"));
				rtn.setExpr1(rs.getString("EXPR1"));
				rtn.setExpr2(rs.getString("EXPR2"));
				rtn.setExpr3(rs.getString("EXPR3"));
				rtn.setExpr4(rs.getString("EXPR4"));
				rtn.setExpr5(rs.getString("EXPR5"));
				rtn.setExpr6(rs.getString("EXPR6"));
				rtn.setExpr7(rs.getString("EXPR7"));
				rtn.setExpr8(rs.getString("EXPR8"));
				i++;
			}

			if (i != 1) {
				throw new Exception("查询的记录数不唯一");
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return rtn;
	}

	/** 
	 * <li>[MON_W_DTL]
	 * <li>[MON_W_PERSON]
	 * */
	public String[] getPhonenumByInfoId(long infoId) throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select b.phonenum from mon_w_dtl a,mon_w_person b where a.person_id = b.person_id and a.info_id = ? and a.state='U' and b.state='U' ");

			ptmt.setLong(1, infoId);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				rtn.add(rs.getString("PHONENUM"));
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return (String[]) rtn.toArray(new String[0]);
	}

	/** 根据HOSTNAME获取[MON_P_HOST]记录 STATE='U'*/
	public MonPHost getMonPHostByHostname(String hostname) throws Exception {
		MonPHost rtn = new MonPHost();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_p_host WHERE hostname=? AND STATE='U' ");
			ptmt.setString(1, hostname);
			rs = ptmt.executeQuery();
			int i = 0;
			if (rs.next()) {
				rtn.setHostname(rs.getString("HOSTNAME"));
				rtn.setUsername(rs.getString("USERNAME"));
				rtn.setPassword(K.k_s(rs.getString("PASSWORD")));
				rtn.setIp(rs.getString("IP"));
				rtn.setSshport(rs.getLong("SSHPORT"));
				i++;
			}

			if (i != 1) {
				throw new Exception("查询的记录数不唯一");
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return rtn;
	}

	/** [MON_L_RECORD]*/
	public void insertMonLRecord(MonLRecord objMonLRecord) throws Exception {
		Connection conn = null;
		PreparedStatement ptmt = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("insert into mon_l_record_" + TimeUtil.getYYYYMM(objMonLRecord.getCreateDate())
							+ " (RECORD_ID,INFO_ID,HOSTNAME,INFO_NAME,INFO_VALUE,CREATE_DATE,IS_TRIGGER_WARN,BUSI_AREA,MON_TYPE,DONE_DATE,IP,WARN_LEVEL)values(?,?,?,?,?,?,?,?,?,?,?,?)");

			ptmt.setLong(1, objMonLRecord.getRecordId());
			ptmt.setLong(2, objMonLRecord.getInfoId());
			ptmt.setString(3, objMonLRecord.getHostname());
			ptmt.setString(4, objMonLRecord.getInfoName());
			ptmt.setString(5, objMonLRecord.getInfoValue());
			ptmt.setTimestamp(6, objMonLRecord.getCreateDate());
			ptmt.setString(7, objMonLRecord.getIsTriggerWarn());
			ptmt.setString(8, objMonLRecord.getBusiArea());
			ptmt.setString(9, objMonLRecord.getMonType());
			ptmt.setTimestamp(10, objMonLRecord.getDoneDate());
			ptmt.setString(11, objMonLRecord.getIp());
			ptmt.setString(12, objMonLRecord.getWarnLevel());
			ptmt.execute();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null)
				conn.close();
		}
	}

	/** [MON_W_TRIGGER]*/
	public void insertMonWTrigger(MonWTrigger objMonWTrigger) throws Exception {
		Connection conn = null;
		PreparedStatement ptmt = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("insert into mon_w_trigger (TRIGGER_ID,INFO_ID,INFO_NAME,PHONENUM,CONTENT,WARN_LEVEL,CREATE_DATE,STATE,IP,RECORD_ID)values(mon_w_trigger$SEQ.nextval,?,?,?,?,?,?,?,?,?)");
			ptmt.setLong(1, objMonWTrigger.getInfoId());
			ptmt.setString(2, objMonWTrigger.getInfoName());
			ptmt.setString(3, objMonWTrigger.getPhonenum());
			ptmt.setString(4, objMonWTrigger.getContent());
			ptmt.setString(5, objMonWTrigger.getWarnLevel());
			ptmt.setTimestamp(6, objMonWTrigger.getCreateDate());
			ptmt.setString(7, objMonWTrigger.getState());
			ptmt.setString(8, objMonWTrigger.getIp());
			ptmt.setLong(9, objMonWTrigger.getRecordId());
			ptmt.execute();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	/** 获取Sequence的下一个值*/
	public long getNextId(String seq) throws Exception {
		long rtn = -999L;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select " + seq + ".nextval from dual");
			rs = ptmt.executeQuery();
			if (rs.next()) {
				rtn = rs.getLong(1);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return rtn;
	}

	/** 根据TABLE_ID获取[MON_P_TABLE]记录*/
	public MonPTable getMonPTableByTableId(long tableId) throws Exception {
		MonPTable rtn = new MonPTable();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_p_table WHERE table_id = ? AND STATE='U' ");
			ptmt.setLong(1, tableId);
			rs = ptmt.executeQuery();
			int i = 0;
			if (rs.next()) {
				rtn.setTableId(rs.getLong("TABLE_ID"));
				rtn.setName(rs.getString("NAME"));
				rtn.setSql(rs.getString("SQL"));
				rtn.setDbAcctCode(rs.getString("DB_ACCT_CODE"));
				rtn.setDbUrlname(rs.getString("DB_URL_NAME"));
				i++;
			}

			if (i != 1) {
				throw new Exception("查询的记录数不唯一");
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return rtn;
	}

	/** 根据NAME获取[MON_DB_URL]记录*/
	public MonDbUrl getMonDbUrlByUrlName(String urlName) throws Exception {
		MonDbUrl rtn = new MonDbUrl();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_db_url WHERE name = ? AND STATE='U' ");
			ptmt.setString(1, urlName);
			rs = ptmt.executeQuery();
			int i = 0;
			if (rs.next()) {
				rtn.setName(rs.getString("NAME"));
				rtn.setUrl(rs.getString("URL"));
				i++;
			}

			if (i != 1)
				throw new Exception("查询的记录数不唯一");
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return rtn;
	}

	/** 根据DB_ACCT_CODE获取[MON_DB_ACCT]记录*/
	public MonDbAcct getMonDbAcctByDbAcctCode(String dbAcctCode) throws Exception {
		MonDbAcct rtn = new MonDbAcct();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_db_acct WHERE DB_ACCT_CODE = ? AND STATE='U' ");
			ptmt.setString(1, dbAcctCode);
			rs = ptmt.executeQuery();
			int i = 0;
			if (rs.next()) {
				rtn.setDbAcctCode(rs.getString("DB_ACCT_CODE"));
				rtn.setUsername(rs.getString("USERNAME"));
				rtn.setPassword(K.k_s(rs.getString("PASSWORD")));
				rtn.setConnMin(rs.getInt("CONN_MIN"));
				rtn.setConnMax(rs.getInt("CONN_MAX"));
				i++;
			}

			if (i != 1)
				throw new Exception("查询的记录数不唯一");
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return rtn;
	}

	/** 获取指定年月的[MON_L_RECORD]记录数量*/
	public long countMonLRecordByCondition(String condition, int[] yyyymm) throws Exception {
		long rtn = 0;
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			StringBuffer sb = new StringBuffer();

			sb.append("select count(0) from ( ");
			for (int i = 0; i < yyyymm.length; i++) {
				if (i != 0) {
					sb.append(" union all ");
				}
				sb.append(" select * from mon_l_record_" + yyyymm[i] + " where " + condition);
			}
			sb.append(" ) t");


			ptmt = conn.prepareStatement(sb.toString());
			rs = ptmt.executeQuery();
			if (rs.next()) {
				rtn = rs.getLong(1);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return rtn;
	}

	/** 根据条件和年月获取[MON_l_RECORD]的记录*/
	public MonLRecord[] getMonLRecordByCondition(String condition, int[] yyyymm, long startIndex, long endIndex) throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();

			StringBuffer sb = new StringBuffer();
			sb.append("select * from ( ");
			for (int i = 0; i < yyyymm.length; i++) {
				if (i != 0) {
					sb.append(" union all ");
				}
				sb.append(" select * from mon_l_record_" + yyyymm[i] + " where " + condition);
			}
			sb.append(" ) t  order by create_date desc ");

			String sql = getPagingSQL(sb.toString(), startIndex, endIndex);
			ptmt = conn.prepareStatement(sql);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MonLRecord objMonLRecord = new MonLRecord();
				objMonLRecord.setRecordId(rs.getLong("RECORD_ID"));
				objMonLRecord.setInfoId(rs.getLong("INFO_ID"));
				objMonLRecord.setHostname(rs.getString("HOSTNAME"));
				objMonLRecord.setIp(rs.getString("IP"));
				objMonLRecord.setMonType(rs.getString("MON_TYPE"));
				objMonLRecord.setInfoName(rs.getString("INFO_NAME"));
				objMonLRecord.setBusiArea(rs.getString("BUSI_AREA"));
				objMonLRecord.setIsTriggerWarn(rs.getString("IS_TRIGGER_WARN"));
				objMonLRecord.setInfoValue(rs.getString("INFO_VALUE"));
				objMonLRecord.setCreateDate(rs.getTimestamp("CREATE_DATE"));
				objMonLRecord.setDoneDate(rs.getTimestamp("DONE_DATE"));
				objMonLRecord.setWarnLevel(rs.getString("WARN_LEVEL"));
				rtn.add(objMonLRecord);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return (MonLRecord[]) rtn.toArray(new MonLRecord[0]);
	}

	/** 根据条件condition获取表mon_w_trigger_his的记录数*/
	public long countMonWTriggerByCondition(String condition, int yyyymm) throws Exception {
		long rtn = 0L;
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select count(0) from mon_w_trigger_his where  " + condition);
			rs = ptmt.executeQuery();
			if (rs.next()) {
				rtn = rs.getLong(1);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return rtn;
	}

	/** 根据条件获取记录列表[mon_w_trigger_his]*/
	public MonWTrigger[] getMonWTriggerByCondition(String condition, int yyyymm, long startIndex, long endIndex) throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			String sql = getPagingSQL("select * from mon_w_trigger_his where  " + condition + " order by create_date desc ", startIndex, endIndex);
			ptmt = conn.prepareStatement(sql);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MonWTrigger objMonWTrigger = new MonWTrigger();
				objMonWTrigger.setTriggerId(rs.getLong("TRIGGER_ID"));
				objMonWTrigger.setRecordId(rs.getLong("RECORD_ID"));
				objMonWTrigger.setInfoId(rs.getLong("INFO_ID"));
				objMonWTrigger.setIp(rs.getString("IP"));
				objMonWTrigger.setInfoName(rs.getString("INFO_NAME"));
				objMonWTrigger.setPhonenum(rs.getString("PHONENUM"));
				objMonWTrigger.setContent(rs.getString("CONTENT"));
				objMonWTrigger.setWarnLevel(rs.getString("WARN_LEVEL"));
				objMonWTrigger.setCreateDate(rs.getTimestamp("CREATE_DATE"));
				objMonWTrigger.setDoneDate(rs.getTimestamp("DONE_DATE"));
				rtn.add(objMonWTrigger);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return (MonWTrigger[]) rtn.toArray(new MonWTrigger[0]);
	}

	/** 获取[MON_P_GRP]的所有记录 state='U' order by sort_by asc*/
	public MonPGrp[] getAllMonPGrp() throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_p_grp where state='U' order by sort_by asc");
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MonPGrp objMonPGrp = new MonPGrp();
				objMonPGrp.setGrpId(rs.getLong("GRP_ID"));
				objMonPGrp.setName(rs.getString("NAME"));
				objMonPGrp.setParentGrpId(rs.getLong("PARENT_GRP_ID"));
				objMonPGrp.setTransformClass(rs.getString("TRANSFORM_CLASS"));
				objMonPGrp.setSortBy(rs.getLong("SORT_BY"));
				objMonPGrp.setShowType(rs.getString("SHOW_TYPE"));

				rtn.add(objMonPGrp);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return (MonPGrp[]) rtn.toArray(new MonPGrp[0]);
	}

	/** 根据GRP_ID获取[MON_P_INFO]记录*/
	public MonPInfo[] getMonPInfoByGrpId(long grpId) throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_p_info where grp_id = ? and  state='U'");
			ptmt.setLong(1, grpId);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MonPInfo objMonPInfo = new MonPInfo();
				objMonPInfo.setInfoId(rs.getLong("INFO_ID"));
				objMonPInfo.setName(rs.getString("NAME"));
				rtn.add(objMonPInfo);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return (MonPInfo[]) rtn.toArray(new MonPInfo[0]);
	}

	/** 将 MON_L_RECORD的内容之后为HashMap */
	public HashMap getMonLRecord4Image(long[] infoId, String transformClass, Date startDate, Date endDate) throws Exception {
		HashMap map = new HashMap();
		int[] yyyymm = TimeUtil.computeYYYYMM(startDate, endDate);
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ITransform objITransform = (ITransform) Class.forName(transformClass.trim()).newInstance();
			conn = SessionManager.getSession().getConnection();

			List list = new ArrayList();
			for (int i = 0; i < infoId.length; i++) {
				list.add(String.valueOf(infoId[i]));
			}

			StringBuffer sb = new StringBuffer();
			if(MiscUtil.getDBDialect().equalsIgnoreCase("Oracle")){
				sb.append("select info_name,info_value,create_date from ( ");
				for (int i = 0; i < yyyymm.length; i++) {
					if (i != 0) {
						sb.append(" union all ");
					}
					sb.append(" select * from mon_l_record_" + yyyymm[i] + " where info_id in ( " + StringUtils.join(list.iterator(), ",")
							+ ") and create_date >= to_date('" + TimeUtil.format(startDate)
							+ "','yyyy-mm-dd hh24:mi:ss') and create_date <= to_date('" + TimeUtil.format(endDate) + "','yyyy-mm-dd hh24:mi:ss')");
				}
				sb.append(" )  order by create_date asc ");
			} else if(MiscUtil.getDBDialect().equalsIgnoreCase("MySQL")){
				//MySQL中：每个派生出来的表都必须有一个自己的别名
				sb.append("select info_name,info_value,create_date from ( ");
				for (int i = 0; i < yyyymm.length; i++) {
					if (i != 0) {
						sb.append(" union all ");
					}
					sb.append(" select * from mon_l_record_" + yyyymm[i] + " where info_id in ( " + StringUtils.join(list.iterator(), ",")
							+ ") and UNIX_TIMESTAMP(create_date) >= UNIX_TIMESTAMP('" + TimeUtil.format(startDate)
							+ "') and UNIX_TIMESTAMP(create_date) <= UNIX_TIMESTAMP('" + TimeUtil.format(endDate) + "'");
				}
				sb.append(" )) t order by create_date asc ");
			} else{
				Log.error("未识别的Dialect: " + MiscUtil.getDBDialect());
				return new HashMap();
			}


			ptmt = conn.prepareStatement(sb.toString());
			rs = ptmt.executeQuery();

			while (rs.next()) {
				Date createDate = rs.getTimestamp("CREATE_DATE");
				String infoName = rs.getString("INFO_NAME");
				String infoValue = rs.getString("INFO_VALUE");

				String lineName = objITransform.getLineName(infoName, infoValue);
				LineValue value = objITransform.getLineValue(createDate, infoValue);
				if (map.containsKey(lineName)) {
					List tmp = (List) map.get(lineName);
					tmp.add(value);
					map.put(lineName, tmp);
				} else {
					List tmp = new ArrayList();
					tmp.add(value);
					map.put(lineName, tmp);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return map;
	}

	/** 根据条件查询[MON_P_INFO的记录数]*/
	public long countMonPInfoByCondition(String condition) throws Exception {
		long rtn = 0L;
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select count(0) from mon_p_info where  " + condition);
			rs = ptmt.executeQuery();
			if (rs.next()) {
				rtn = rs.getLong(1);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return rtn;
	}

	/** 根据条件condition获取[MON_P_INFO]*/
	public MonPInfo[] getMonPInfoByCondition(String condition, long startIndex, long endIndex) throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();

			String sql = getPagingSQL("select * from mon_p_info where  " + condition + " and state='U' order by info_id asc ", startIndex, endIndex);
			ptmt = conn.prepareStatement(sql);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MonPInfo objMonPInfo = new MonPInfo();
				objMonPInfo.setInfoId(rs.getLong("INFO_ID"));
				objMonPInfo.setName(rs.getString("NAME"));
				objMonPInfo.setHostname(rs.getString("HOSTNAME"));
				objMonPInfo.setTypeId(rs.getLong("TYPE_ID"));
				objMonPInfo.setThresholdId(rs.getLong("THRESHOLD_ID"));

				objMonPInfo.setSplitRuleId(rs.getLong("SPLIT_RULE_ID"));
				objMonPInfo.setTimeId(rs.getLong("TIME_ID"));
				objMonPInfo.setType(rs.getString("TYPE"));
				objMonPInfo.setBusiArea(rs.getString("BUSI_AREA"));
				objMonPInfo.setBusiArea(rs.getString("BUSI_AREA"));
				objMonPInfo.setGrpId(rs.getLong("GRP_ID"));
				rtn.add(objMonPInfo);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return (MonPInfo[]) (MonPInfo[]) rtn.toArray(new MonPInfo[0]);
	}

	/** 根据GRP_ID获取表[MON_P_GRP]的记录*/
	public MonPGrp getMonPGrpByGrpId(long grpId) throws Exception {
		MonPGrp rtn = new MonPGrp();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_p_grp where state='U' and grp_id = ? ");
			ptmt.setLong(1, grpId);
			rs = ptmt.executeQuery();
			int i = 0;
			if (rs.next()) {
				rtn.setGrpId(rs.getLong("GRP_ID"));
				rtn.setName(rs.getString("NAME"));
				rtn.setParentGrpId(rs.getLong("PARENT_GRP_ID"));
				rtn.setTransformClass(rs.getString("TRANSFORM_CLASS"));
				rtn.setSortBy(rs.getLong("SORT_BY"));
				rtn.setShowType(rs.getString("SHOW_TYPE"));
				i++;
			}

			if (i != 1) {
				throw new Exception("查询的记录数不唯一");
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return rtn;
	}

	/** 获取分页SQL*/
	private String getPagingSQL(String realSQL, long startIndex, long endIndex) {
		if(MiscUtil.getDBDialect().equalsIgnoreCase("Oracle")){
			StringBuffer pagingSelect = new StringBuffer();
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
			pagingSelect.append(realSQL);
			pagingSelect.append(" ) row_ where rownum <= " + endIndex + " ) where rownum_ > " + startIndex);
			return pagingSelect.toString();
		}else if(MiscUtil.getDBDialect().equalsIgnoreCase("MySQL")){
//			StringBuffer pagingSelect = new StringBuffer();
//			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
//			pagingSelect.append(realSQL);
//			pagingSelect.append(" ) row_ where rownum <= " + endIndex + " ) where rownum_ > " + startIndex);
//			return pagingSelect.toString();
			
			StringBuffer str = new StringBuffer(realSQL);
			if ((startIndex < 0) && (endIndex >= 0)) {
				str.append(" limit ").append("0").append(",").append(endIndex);
			} else if ((endIndex < 0) && (startIndex >= 0)) {
				if (startIndex > 0) {
					str.append(" limit ").append(startIndex - 1).append(",").append("18446744073709551615");
				} else {
					str.append(" limit ").append(startIndex).append(",").append("18446744073709551615");
				}

			} else if (endIndex < startIndex) {
				str.append(" limit ").append("0").append(",").append("0");
			} else {
				long offset = endIndex - startIndex + 1;
				long tmpStart = 0;
				if (startIndex > 0) {
					tmpStart = startIndex - 1;
				} else {
					tmpStart = 0;
				}
				str.append(" limit ").append(tmpStart).append(",").append(offset);
			}
			
			return str.toString();
		}else{
			Log.error("未识别的Dialect: " + MiscUtil.getDBDialect() + "直接返回 " + realSQL);
			return realSQL;
		}

	}

	/** 根据条件获取[MON_C_SERVER]的记录数*/
	public long countMidServerServerControlByCondition(String condition) throws Exception {
		long rtn = 0L;
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select count(0) from mon_c_server where  " + condition);
			rs = ptmt.executeQuery();
			if (rs.next()) {
				rtn = rs.getLong(1);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return rtn;
	}

	/** 根据条件condition，根据条件[MON_C_SERVER] order by url desc*/
	public MidServerControl[] getMidServerServerControlByCondition(String condition, long startIndex, long endIndex) throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();

			String sql = getPagingSQL("select * from mon_c_server where  " + condition + " order by url desc ", startIndex, endIndex);
			ptmt = conn.prepareStatement(sql);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MidServerControl objMidServerServerControl = new MidServerControl();
				objMidServerServerControl.setUrl(rs.getString("URL"));
				objMidServerServerControl.setHostname(rs.getString("HOSTNAME"));
				objMidServerServerControl.setServerName(rs.getString("SERVER_NAME"));
				objMidServerServerControl.setPfPath(rs.getString("PF_PATH"));
				objMidServerServerControl.setStopExecId(rs.getLong("STOP_EXEC_ID"));
				objMidServerServerControl.setStartExecId(rs.getLong("START_EXEC_ID"));
				objMidServerServerControl.setGrpName(rs.getString("GRP_NAME"));
				rtn.add(objMidServerServerControl);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return (MidServerControl[]) rtn.toArray(new MidServerControl[0]);
	}

	/**分组列表 查询所有的[MON_C_SERVER.GRP_NAME]字段distinct列表*/
	public String[] getAllMidServerControlGrp() throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select distinct grp_name from mon_c_server where state='U' order by grp_name");
			rs = ptmt.executeQuery();
			while (rs.next())
				rtn.add(rs.getString("GRP_NAME"));
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return (String[]) (String[]) rtn.toArray(new String[0]);
	}

	/** 根据EXEC_ID 获取表[mon_c_exec]记录*/
	public MonCExec getMonCExecByExecId(long execId) throws Exception {
		MonCExec rtn = new MonCExec();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_c_exec WHERE exec_id=? AND STATE='U' ");
			ptmt.setLong(1, execId);
			rs = ptmt.executeQuery();
			int i = 0;
			if (rs.next()) {
				rtn.setExecId(rs.getLong("EXEC_ID"));
				rtn.setName(rs.getString("NAME"));
				rtn.setExecType(rs.getString("EXEC_TYPE"));

				StringBuffer sb = new StringBuffer();
				String expr1 = rs.getString("EXPR1");
				if (!StringUtils.isBlank(expr1)) {
					sb.append(expr1);
				}
				String expr2 = rs.getString("EXPR2");
				if (!StringUtils.isBlank(expr2)) {
					sb.append(expr2);
				}
				String expr3 = rs.getString("EXPR3");
				if (!StringUtils.isBlank(expr3)) {
					sb.append(expr3);
				}
				String expr4 = rs.getString("EXPR4");
				if (!StringUtils.isBlank(expr4)) {
					sb.append(expr4);
				}
				String expr5 = rs.getString("EXPR5");
				if (!StringUtils.isBlank(expr5)) {
					sb.append(expr5);
				}
				String expr6 = rs.getString("EXPR6");
				if (!StringUtils.isBlank(expr6)) {
					sb.append(expr6);
				}
				String expr7 = rs.getString("EXPR7");
				if (!StringUtils.isBlank(expr7)) {
					sb.append(expr7);
				}
				String expr8 = rs.getString("EXPR8");
				if (!StringUtils.isBlank(expr8)) {
					sb.append(expr8);
				}

				rtn.setExpr(sb.toString());
				i++;
			}

			if (i != 1) {
				throw new Exception("查询的记录数不唯一");
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return rtn;
	}

	/** 根据SERVER_NAME获取记录[MON_C_SERVER]*/
	public MidServerControl getMidServerServerControlByServerName(String serverName) throws Exception {
		MidServerControl objMidServerServerControl = null;
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_c_server where server_name = ? and state='U'");
			ptmt.setString(1, serverName);
			rs = ptmt.executeQuery();
			int i = 0;
			if (rs.next()) {
				objMidServerServerControl = new MidServerControl();
				objMidServerServerControl.setUrl(rs.getString("URL"));
				objMidServerServerControl.setHostname(rs.getString("HOSTNAME"));
				objMidServerServerControl.setServerName(rs.getString("SERVER_NAME"));
				objMidServerServerControl.setPfPath(rs.getString("PF_PATH"));
				objMidServerServerControl.setStopExecId(rs.getLong("STOP_EXEC_ID"));
				objMidServerServerControl.setStartExecId(rs.getLong("START_EXEC_ID"));
				objMidServerServerControl.setGrpName(rs.getString("GRP_NAME"));
				i++;
			}

			if (i != 1) {
				throw new Exception("查询的记录数不唯一");
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return objMidServerServerControl;
	}

	/** 获取[MON_P_HOST]记录, 根据[MON_P_HOST] [MON_C_SERVER]*/
	public MonPHost[] getMonPHostForTrace() throws Exception {
		List list = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select * from mon_p_host where state='U' and hostname in (select distinct hostname from mon_c_server where grp_name like '%APP%' and state='U') ");
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MonPHost rtn = new MonPHost();
				rtn.setHostname(rs.getString("HOSTNAME"));
				rtn.setUsername(rs.getString("USERNAME"));
				rtn.setPassword(K.k_s(rs.getString("PASSWORD")));
				rtn.setIp(rs.getString("IP"));
				rtn.setSshport(rs.getLong("SSHPORT"));
				list.add(rtn);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return (MonPHost[]) list.toArray(new MonPHost[0]);
	}
	
	/** 检查HotPatch密码 [MON_HOTPATCH_PASSWORD]*/
	public boolean checkHotPatchPassword(String password) throws Exception {
		boolean rtn = false;
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			long count = 0L;
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("select count(*) from mon_hotpatch_password where  password = ? and state='U'");
			MD5 md5 = new MD5();
			String encrypt = md5.getMD5ofStr(password);
			ptmt.setString(1, encrypt);
			rs = ptmt.executeQuery();
			if (rs.next()) {
				count = rs.getLong(1);
			}

			if (count > 0L) {
				rtn = true;
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return rtn;
	}
	
	public void insertMonOpLog(MonOpLog objMonOpLog) throws Exception {
		Connection conn = null;
		PreparedStatement ptmt = null;
		try {
			conn = SessionManager.getSession().getConnection();
			if(MiscUtil.getDBDialect().equalsIgnoreCase("Oracle")){
				ptmt = conn.prepareStatement("insert into mon_op_log (OP_LOG_ID,CODE,IP,CLASS_NAME,METHOD_NAME,URL,OP_DATE)values(mon_op_log$seq.nextval,?,?,?,?,?,sysdate)");
			}else if(MiscUtil.getDBDialect().equalsIgnoreCase("MySQL")){
				ptmt = conn.prepareStatement("insert into mon_op_log (CODE,IP,CLASS_NAME,METHOD_NAME,URL,OP_DATE)values(?,?,?,?,?,now())");
			}else{
				Log.error("无法识别的Dialec: " + MiscUtil.getDBDialect());
				return;
			}

			ptmt.setString(1, objMonOpLog.getCode());
			ptmt.setString(2, objMonOpLog.getIp());
			ptmt.setString(3, objMonOpLog.getClassName());
			ptmt.setString(4, objMonOpLog.getMethodName());
			ptmt.setString(5, objMonOpLog.getUrl());
			ptmt.execute();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	/** 
	 * <li>[MON_GROUP_ACCESS_RELAT]
	 * <li>[MON_GROUP]
	 * */
	public MonAccess[] getMonAccessByUsername(String username) throws Exception {
		List rtn = new ArrayList();
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn
					.prepareStatement("select e.* from mon_group_access_relat a, mon_group b ,mon_user c,mon_user_group_relat d,mon_access e where a.group_id = b.group_id and a.access_id = e.access_id and c.user_id = d.user_id and a.group_id = d.group_id and a.state='U' and b.state='U' and c.state='U' and d.state='U' and e.state='U' and c.username = ?");
			ptmt.setString(1, username);
			rs = ptmt.executeQuery();
			while (rs.next()) {
				MonAccess obj = new MonAccess();
				obj.setAccessId(rs.getLong("ACCESS_ID"));
				obj.setClassName(rs.getString("CLASS_NAME"));

				if (!StringUtils.isBlank(obj.getClassName())) {
					obj.setClassName(obj.getClassName().trim());
				}

				obj.setMethodName(rs.getString("METHOD_NAME"));
				if (!StringUtils.isBlank(obj.getMethodName())) {
					obj.setMethodName(obj.getMethodName().trim());
				}

				rtn.add(obj);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return (MonAccess[]) rtn.toArray(new MonAccess[0]);
	}

	public void updatePwdByUsername(String username, String newPwd) throws Exception {
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			conn = SessionManager.getSession().getConnection();
			ptmt = conn.prepareStatement("update mon_user set password = ? where username = ? and state='U'");
			ptmt.setString(1, "{RC2}" + K.j(newPwd));
			ptmt.setString(2, username);
			ptmt.execute();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
}