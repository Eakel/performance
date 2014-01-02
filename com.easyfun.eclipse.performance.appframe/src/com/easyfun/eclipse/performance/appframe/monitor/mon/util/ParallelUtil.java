package com.easyfun.eclipse.performance.appframe.monitor.mon.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.ai.appframe2.complex.hp.mbean.HotPatchMBean;
import com.ai.appframe2.complex.mbean.standard.cache.CacheMonitorMBean;
import com.ai.appframe2.complex.mbean.standard.cc.ClientControlMBean;
import com.ai.appframe2.complex.mbean.standard.datasource.UnclosedNewConnRuntime;
import com.ai.appframe2.complex.mbean.standard.datasource.UnclosedNewConneMonitorMBean;
import com.ai.appframe2.complex.mbean.standard.session.AppframeSessionMonitorMBean;
import com.ai.appframe2.complex.mbean.standard.sv.SVCurrentPoint;
import com.ai.appframe2.complex.mbean.standard.sv.SVMethodMonitorMBean;
import com.ai.secframe.mem.mbean.SecMemMonitorMBean;
import com.asiainfo.appframe.ext.memcached.mbean.MemcachedMonitorMBean;
import com.easyfun.eclipse.performance.appframe.monitor.mon.client.ClientProxy;
import com.easyfun.eclipse.performance.appframe.monitor.mon.common.service.ServiceFactory;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MidServerControl;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonPHost;
import com.easyfun.eclipse.performance.appframe.monitor.mon.po.MonServer;
import com.easyfun.eclipse.performance.appframe.monitor.mon.service.interfaces.IMonSV;

public final class ParallelUtil {
	public static String[] sort(String[] str) {
		HashMap map = new HashMap();
		for (int i = 0; i < str.length; i++) {
			map.put(new Integer(i), str[i]);
		}

		HashMap tmp = new HashMap();
		for (int i = 0; i < str.length; i++) {
			char[] ch = str[i].toCharArray();
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < ch.length; j++) {
				sb.append(ch[j]);
			}			
		}

		return null;
	}

	public static MidServerControl[] computeUrl(int threadcCount, int timeout, MidServerControl[] runnableTask) throws Exception {
		List rtn = new ArrayList();
		ExecutorService exec = Executors.newFixedThreadPool(threadcCount);
		try {
			CompletionService serv = new ExecutorCompletionService(exec);

			for (int i = 0; i < runnableTask.length; i++) {
				serv.submit(new UrlCallable(timeout, runnableTask[i]));
			}

			for (int index = 0; index < runnableTask.length; index++) {
				Future task = serv.poll(timeout + 5, TimeUnit.SECONDS);
				if (task == null) {
					runnableTask[index].setStatus("TIMEOUT");
					rtn.add(runnableTask[index]);
				} else {
					MidServerControl item = (MidServerControl) task.get();
					rtn.add(item);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (exec != null) {
				exec.shutdownNow();
			}
		}
		return (MidServerControl[]) rtn.toArray(new MidServerControl[0]);
	}

	public static String[] computeMidServer(int threadcCount, int timeout, MidServerTask[] objMidServerTask) throws Exception {
		List rtn = new ArrayList();
		ExecutorService exec = Executors.newFixedThreadPool(threadcCount);
		try {
			CompletionService serv = new ExecutorCompletionService(exec);
			for (int i = 0; i < objMidServerTask.length; i++) {
				serv.submit(new MidServerServerCallable(objMidServerTask[i]));
			}

			for (int index = 0; index < objMidServerTask.length; index++) {
				Future task = serv.poll(timeout + 5, TimeUnit.SECONDS);
				if (task == null) {
					rtn.add(objMidServerTask[index].serverName + "执行超时");
				} else {
					String item = (String) task.get();
					rtn.add(item);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (exec != null) {
				exec.shutdownNow();
			}
		}
		return  (String[]) rtn.toArray(new String[0]);
	}

	public static List getMemcachedLocalCacheInfo(int threadcCount, int timeout, long[] serverIds) throws Exception {
		List rtn = new ArrayList();
		ExecutorService exec = Executors.newFixedThreadPool(threadcCount);
		try {
			CompletionService serv = new ExecutorCompletionService(exec);

			IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
			MonServer[] objMonServer = objIMonSV.getMonServerByServerId(serverIds);

			for (int i = 0; i < objMonServer.length; i++) {
				serv.submit(new GetMemcachedLocalCacheCallable(objMonServer[i]));
			}

			for (int index = 0; index < objMonServer.length; index++) {
				Future task = serv.poll(timeout + 5, TimeUnit.SECONDS);
				if (task == null) {
					System.out.println(serverIds[index] + "执行超时");
				} else {
					Map item = (Map) task.get();
					rtn.add(item);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (exec != null) {
				exec.shutdownNow();
			}
		}
		return rtn;
	}
	
	public static List getSecMemLocalCacheInfo(int threadcCount, int timeout, long[] serverIds) throws Exception {
		List rtn = new ArrayList();
		ExecutorService exec = Executors.newFixedThreadPool(threadcCount);
		try {
			CompletionService serv = new ExecutorCompletionService(exec);

			IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
			MonServer[] objMonServer = objIMonSV.getMonServerByServerId(serverIds);

			for (int i = 0; i < objMonServer.length; i++) {
				serv.submit(new GetSecMemLocalCacheCallable(objMonServer[i]));
			}

			for (int index = 0; index < objMonServer.length; index++) {
				Future task = serv.poll(timeout + 5, TimeUnit.SECONDS);
				if (task == null) {
					System.out.println(serverIds[index] + "执行超时");
				} else {
					Map item = (Map) task.get();
					rtn.add(item);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (exec != null) {
				exec.shutdownNow();
			}
		}
		return rtn;
	}

	/**
	 * SVCurrentPoint.jsp写死了threadCount=30, timeout=10
	 */
	public static HashMap getSVCurrentPoint(int threadcCount, int timeout, long[] serverIds, String[] serverNames) throws Exception {
		HashMap rtn = new HashMap();
		ExecutorService exec = Executors.newFixedThreadPool(threadcCount);
		try {
			CompletionService serv = new ExecutorCompletionService(exec);

			for (int i = 0; i < serverIds.length; i++) {
				serv.submit(new GetSVCurrentPointCallable(serverIds[i]));
			}

			for (int index = 0; index < serverIds.length; index++) {
				Future task = serv.poll(timeout + 5, TimeUnit.SECONDS);
				if (task == null) {
					System.out.println(serverIds[index] + "执行超时");
				} else {
					SVCurrentPoint[] item = (SVCurrentPoint[]) task.get();
					rtn.put(serverNames[index], item);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (exec != null) {
				exec.shutdownNow();
			}
		}
		return rtn;
	}

	public static List getSessionUserInfo(int threadcCount, int timeout, long[] serverIds) throws Exception {
		List rtn = new ArrayList();
		ExecutorService exec = Executors.newFixedThreadPool(threadcCount);
		try {
			CompletionService serv = new ExecutorCompletionService(exec);
			for (int i = 0; i < serverIds.length; i++) {
				serv.submit(new GetSessionCallable(serverIds[i]));
			}

			for (int index = 0; index < serverIds.length; index++) {
				Future task = serv.poll(timeout + 5, TimeUnit.SECONDS);
				if (task == null) {
					System.out.println(serverIds[index] + "执行超时");
				} else {
					HashMap[] item = (HashMap[]) (HashMap[]) task.get();
					rtn.add(item);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (exec != null) {
				exec.shutdownNow();
			}
		}
		return rtn;
	}
	
	public static List getUnClosedNewConnection(int threadcCount, int timeout, long[] serverIds) throws Exception {
		List rtn = new ArrayList();
		ExecutorService exec = Executors.newFixedThreadPool(threadcCount);
		try {
			IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
			MonServer[] objMonServer = objIMonSV.getMonServerByServerId(serverIds);

			CompletionService serv = new ExecutorCompletionService(exec);

			for (int i = 0; i < objMonServer.length; i++) {
				serv.submit(new GetUnClosedNewConnectionCallable(objMonServer[i].getName(), objMonServer[i].getServerId()));
			}

			for (int index = 0; index < serverIds.length; index++) {
				Future task = serv.poll(timeout + 5, TimeUnit.SECONDS);
				if (task == null) {
					System.out.println(serverIds[index] + "执行超时");
				} else {
					HashMap item = (HashMap) task.get();
					rtn.add(item);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (exec != null) {
				exec.shutdownNow();
			}
		}
		return rtn;
	}


	public static ConnectApp[] getCurrentConnectAppCluster(MonServer[] objMonServer) throws Exception {
		List rtn = new ArrayList();
		for (int i = 0; i < objMonServer.length; i++) {
			ConnectApp objConnectApp = new ConnectApp();
			ClientControlMBean objClientControlMBean = null;
			try {
				objClientControlMBean = (ClientControlMBean) ClientProxy.getObject(objMonServer[i].getServerId(), ClientControlMBean.class);

				objConnectApp.currentAppCluster = objClientControlMBean.getCurrentAppCluster();
				Map map = objClientControlMBean.listGroups();
				objConnectApp.canSelectAppCluster = ((String[]) (String[]) map.keySet().toArray(new String[0]));
				objConnectApp.appClusterEnv = ((String[]) (String[]) map.values().toArray(new String[0]));
				objConnectApp.oldAppCluster = objClientControlMBean.getOldAppCluster();
				objConnectApp.isOk = true;
				objConnectApp.serverName = objMonServer[i].getName();
				objConnectApp.serverId = objMonServer[i].getServerId();
				rtn.add(objConnectApp);
			} catch (Throwable ex) {
				objConnectApp.serverName = objMonServer[i].getName();
				objConnectApp.oldAppCluster = "无法获得";
				objConnectApp.currentAppCluster = "无法获得";
				objConnectApp.canSelectAppCluster = new String[0];
				objConnectApp.appClusterEnv = new String[0];
				objConnectApp.isOk = false;
				objConnectApp.serverName = objMonServer[i].getName();
				objConnectApp.serverId = objMonServer[i].getServerId();

				rtn.add(objConnectApp);
				ex.printStackTrace();
			} finally {
				if (objClientControlMBean != null) {
					ClientProxy.destroyObject(objClientControlMBean);
				}
			}
		}
		return (ConnectApp[]) (ConnectApp[]) rtn.toArray(new ConnectApp[0]);
	}

	/** 根据SERVER_ID获取HotPatch列表*/
	public static List getHotPatchMap(int threadcCount, int timeout, long[] serverIds) throws Exception {
		List rtn = new ArrayList();
		ExecutorService exec = Executors.newFixedThreadPool(threadcCount);
		try {
			CompletionService serv = new ExecutorCompletionService(exec);

			IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
			MonServer[] objMonServer = objIMonSV.getMonServerByServerId(serverIds);

			for (int i = 0; i < objMonServer.length; i++) {
				serv.submit(new GetHotPatchCallable(objMonServer[i]));
			}

			for (int index = 0; index < objMonServer.length; index++) {
				Future task = serv.poll(timeout + 5, TimeUnit.SECONDS);
				if (task == null) {
					System.out.println(serverIds[index] + "执行超时");
				} else {
					Map item = (Map) task.get();
					rtn.add(item);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (exec != null) {
				exec.shutdownNow();
			}
		}
		return rtn;
	}

	/** 进行Hotpatch操作*/
	public static Map doHotPatch(int threadcCount, int timeout, long[] serverIds, byte[] classBytes, String[] classNames) throws Exception {
		Map rtn = new HashMap();
		ExecutorService exec = Executors.newFixedThreadPool(threadcCount);
		try {
			CompletionService serv = new ExecutorCompletionService(exec);

			IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
			MonServer[] objMonServer = objIMonSV.getMonServerByServerId(serverIds);

			for (int i = 0; i < objMonServer.length; i++) {
				serv.submit(new DoHotPatchCallable(objMonServer[i], classBytes, classNames));
			}

			for (int index = 0; index < objMonServer.length; index++) {
				Future task = serv.poll(timeout + 5, TimeUnit.SECONDS);
				if (task == null) {
					System.out.println(serverIds[index] + "执行超时");
					rtn.put(objMonServer[index].getName(), "超时");
				} else {
					Object obj = task.get();
					if ((obj instanceof String)) {
						rtn.put(objMonServer[index].getName(), "成功");
					} else if ((obj instanceof Throwable)) {
						rtn.put(objMonServer[index].getName(), "失败:" + ((Throwable) obj).getMessage());
					} else {
						rtn.put(objMonServer[index].getName(), obj);
					}
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (exec != null) {
				exec.shutdownNow();
			}
		}
		return rtn;
	}

	public static HashMap clearLocalCache(int threadcCount, int timeout, long[] serverId) throws Exception {
		HashMap map = new HashMap();
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		MonServer[] objMonServer = objIMonSV.getMonServerByServerId(serverId);

		ExecutorService exec = Executors.newFixedThreadPool(threadcCount);
		try {
			CompletionService serv = new ExecutorCompletionService(exec);

			for (int i = 0; i < serverId.length; i++) {
				serv.submit(new ClearLocalCacheCallable(serverId[i], objMonServer[i].getName()));
			}

			for (int index = 0; index < serverId.length; index++) {
				Future task = serv.poll(timeout + 5, TimeUnit.SECONDS);
				if (task != null) {
					HashMap tmp = (HashMap) task.get();
					map.putAll(tmp);
				} else {
					HashMap newMap = new HashMap();
					newMap.put(new Long(serverId[index]), objMonServer[index].getName() + "执行超时");
					map.putAll(newMap);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (exec != null) {
				exec.shutdownNow();
			}
		}
		return map;
	}
	
	public static HashMap clearSecMemLocalCache(int threadcCount, int timeout, long[] serverId) throws Exception {
		HashMap map = new HashMap();
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		MonServer[] objMonServer = objIMonSV.getMonServerByServerId(serverId);

		ExecutorService exec = Executors.newFixedThreadPool(threadcCount);
		try {
			CompletionService serv = new ExecutorCompletionService(exec);

			for (int i = 0; i < serverId.length; i++) {
				serv.submit(new ClearSecMemLocalCacheCallable(serverId[i], objMonServer[i].getName()));
			}

			for (int index = 0; index < serverId.length; index++) {
				Future task = serv.poll(timeout + 5, TimeUnit.SECONDS);
				if (task != null) {
					HashMap tmp = (HashMap) task.get();
					map.putAll(tmp);
				} else {
					HashMap newMap = new HashMap();
					newMap.put(new Long(serverId[index]), objMonServer[index].getName() + "执行超时");
					map.putAll(newMap);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (exec != null) {
				exec.shutdownNow();
			}
		}
		return map;
	}

	public static HashMap refreshCache(int threadcCount, int timeout, long[] serverId, String[] cacheIds) throws Exception {
		HashMap map = new HashMap();
		IMonSV objIMonSV = (IMonSV) ServiceFactory.getService(IMonSV.class);
		MonServer[] objMonServer = objIMonSV.getMonServerByServerId(serverId);

		ExecutorService exec = Executors.newFixedThreadPool(threadcCount);
		try {
			CompletionService serv = new ExecutorCompletionService(exec);
			for (int i = 0; i < serverId.length; i++) {
				serv.submit(new RefreshCacheCallable(serverId[i], objMonServer[i].getName(), cacheIds));
			}

			for (int index = 0; index < serverId.length; index++) {
				Future task = serv.poll(timeout + 5, TimeUnit.SECONDS);
				if (task != null) {
					HashMap tmp = (HashMap) task.get();
					map.putAll(tmp);
				} else {
					HashMap newMap = new HashMap();
					newMap.put(new Long(serverId[index]), objMonServer[index].getName() + "执行超时");
					map.putAll(newMap);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (exec != null) {
				exec.shutdownNow();
			}
		}
		return map;
	}

	/** threadcount写死为30, timeout写死为6*/
	public static int collectTraceInfo(int threadcCount, int timeout, MonPHost[] objMonPHost) throws Exception {
		int rtn = 0;
		ExecutorService exec = Executors.newFixedThreadPool(threadcCount);
		try {
			CompletionService serv = new ExecutorCompletionService(exec);
			for (int i = 0; i < objMonPHost.length; i++) {
				serv.submit(new CollectTraceInfoCallable(objMonPHost[i]));
			}

			for (int index = 0; index < objMonPHost.length; index++) {
				Future task = serv.poll(timeout + 5, TimeUnit.SECONDS);
				if (task == null) {
					continue;
				}
				Long item = (Long) task.get();
				rtn += item.intValue();
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (exec != null) {
				exec.shutdownNow();
			}
		}
		return rtn;
	}

	public static class ConnectApp {
		public boolean isOk = true;
		public String serverName;
		public long serverId;
		public String oldAppCluster;
		public String currentAppCluster;
		public String[] canSelectAppCluster;
		public String[] appClusterEnv;
	}

	/** 通过HotPatchMBean进行Hotpatch*/
	public static class DoHotPatchCallable implements Callable {
		private MonServer server;
		private byte[] classBytes;
		private String[] classNames;

		public DoHotPatchCallable(MonServer server, byte[] classBytes, String[] classNames) {
			this.server = server;
			this.classBytes = classBytes;
			this.classNames = classNames;
		}

		public Object call() throws Exception {
			HotPatchMBean objHotPatchMBean = null;
			try {
				objHotPatchMBean = (HotPatchMBean) ClientProxy.getObject(this.server.getServerId(), HotPatchMBean.class);
				objHotPatchMBean.hotPatch(this.classBytes, this.classNames);
				String str = "ok";
				return str;
			} catch (Throwable ex) {
				ex.printStackTrace();
				Throwable localThrowable1 = ex;
				return localThrowable1;
			} finally {
				if (objHotPatchMBean != null) {
					ClientProxy.destroyObject(objHotPatchMBean);
				}
			}
		}
	}

	/** 通过HotPatchBean的JMX异步获取Hotpatch信息*/
	public static class GetHotPatchCallable implements Callable {
		private MonServer server;

		public GetHotPatchCallable(MonServer server) {
			this.server = server;
		}

		public Object call() throws Exception {
			Map map = new HashMap();
			HotPatchMBean objHotPatchMBean = null;
			try {
				objHotPatchMBean = (HotPatchMBean) ClientProxy.getObject(this.server.getServerId(), HotPatchMBean.class);
				String[] tmp = objHotPatchMBean.getCurrentHotPatchClasses();
				map.put("SERVER_NAME", this.server.getName());
				map.put("HOTPATCH_LIST", tmp);
			} catch (Throwable ex) {
				map.put(this.server.getName(), null);
				ex.printStackTrace();
			} finally {
				if (objHotPatchMBean != null) {
					ClientProxy.destroyObject(objHotPatchMBean);
				}
			}
			return map;
		}
	}
	
	public static class GetSecMemLocalCacheCallable implements Callable {
		private MonServer server;

		public GetSecMemLocalCacheCallable(MonServer server) {
			this.server = server;
		}

		public Object call() throws Exception {
			Map rtn = new HashMap();
			SecMemMonitorMBean objSecMemMonitorMBean = null;
			try {
				objSecMemMonitorMBean = (SecMemMonitorMBean) ClientProxy.getObject(this.server.getServerId(), SecMemMonitorMBean.class);

				Map map = objSecMemMonitorMBean.getLocalCacheStats();
				rtn.put("SERVER_ID", new Long(this.server.getServerId()));
				rtn.put("NAME", this.server.getName());
				long hit = ((Long) map.get("STAT_HIT")).longValue();
				long miss = ((Long) map.get("STAT_MISS")).longValue();

				if (hit + miss <= 0L) {
					rtn.put("HIT_RATE", "0%");
				} else {
					rtn.put("HIT_RATE", String.valueOf(100L * hit / (hit + miss)) + "%");
				}

				rtn.put("SIZE", ((Long) map.get("STAT_SIZE")).toString());
				rtn.put("CURRENT_BYTE_SIZE", ((Long) map.get("STAT_CURRENT_BYTE_SIZE")).toString());
				rtn.put("LIMIT_BYTES", ((Long) map.get("STAT_LIMIT_BYTES")).toString());
				rtn.put("HIT", String.valueOf(hit));
				rtn.put("MISS", String.valueOf(miss));
				rtn.put("EVICT", ((Long) map.get("STAT_EVICT")).toString());
				rtn.put("OVERLOAD", ((Long) map.get("STAT_OVERLOAD")).toString());
				String time = new Timestamp(((Long) map.get("STAT_LAST_LOCAL_CACHE_CLEAR_TIME")).longValue()).toString();
				rtn.put("UPTIME", time);
				rtn.put("BUCKET", ((Long) map.get("STAT_BUCKET_COUNT")).toString());
				System.out.println("aaaaaaaa=" + rtn);
			} catch (Throwable ex) {
				ex.printStackTrace();
			} finally {
				if (objSecMemMonitorMBean != null) {
					ClientProxy.destroyObject(objSecMemMonitorMBean);
				}
			}
			return rtn;
		}
	}

	/** 通过MemcachedMonitorMBean的JMX异步获取Memcache信息*/
	public static class GetMemcachedLocalCacheCallable implements Callable {
		private MonServer server;

		public GetMemcachedLocalCacheCallable(MonServer server) {
			this.server = server;
		}

		public Object call() throws Exception {
			Map rtn = new HashMap();
			MemcachedMonitorMBean objMemcachedMonitorMBean = null;
			try {
				objMemcachedMonitorMBean = (MemcachedMonitorMBean) ClientProxy.getObject(this.server.getServerId(), MemcachedMonitorMBean.class);

				Map map = objMemcachedMonitorMBean.getLocalCacheStats();
				rtn.put("SERVER_ID", new Long(this.server.getServerId()));
				rtn.put("NAME", this.server.getName());
				long hit = ((Long) map.get("STAT_HIT")).longValue();
				long miss = ((Long) map.get("STAT_MISS")).longValue();

				if (hit + miss <= 0L) {
					rtn.put("HIT_RATE", "0%");
				} else {
					rtn.put("HIT_RATE", String.valueOf(100L * hit / (hit + miss)) + "%");
				}

				rtn.put("SIZE", ((Long) map.get("STAT_SIZE")).toString());
				rtn.put("CURRENT_BYTE_SIZE", ((Long) map.get("STAT_CURRENT_BYTE_SIZE")).toString());
				rtn.put("LIMIT_BYTES", ((Long) map.get("STAT_LIMIT_BYTES")).toString());
				rtn.put("HIT", String.valueOf(hit));
				rtn.put("MISS", String.valueOf(miss));
				rtn.put("EVICT", ((Long) map.get("STAT_EVICT")).toString());
				rtn.put("OVERLOAD", ((Long) map.get("STAT_OVERLOAD")).toString());
				String time = new Timestamp(((Long) map.get("STAT_LAST_LOCAL_CACHE_CLEAR_TIME")).longValue()).toString();
				rtn.put("UPTIME", time);
				rtn.put("BUCKET", ((Long) map.get("STAT_BUCKET_COUNT")).toString());
			} catch (Throwable ex) {
				ex.printStackTrace();
			} finally {
				if (objMemcachedMonitorMBean != null) {
					ClientProxy.destroyObject(objMemcachedMonitorMBean);
				}
			}
			return rtn;
		}
	}

	/** 通过SVMethodMonitorMBean的JMX异步获取服务方法监控信息*/
	public static class GetSVCurrentPointCallable implements Callable {
		private long serverId;

		public GetSVCurrentPointCallable(long serverId) {
			this.serverId = serverId;
		}

		public Object call() throws Exception {
			SVCurrentPoint[] objSVCurrentPoint = null;
			SVMethodMonitorMBean objSVMethodMonitorMBean = null;
			try {
				objSVMethodMonitorMBean = (SVMethodMonitorMBean) ClientProxy.getObject(this.serverId, SVMethodMonitorMBean.class);
				objSVCurrentPoint = objSVMethodMonitorMBean.fetchSVCurrentPoint();
			} catch (Throwable ex) {
				ex.printStackTrace();
			} finally {
				if (objSVMethodMonitorMBean != null) {
					ClientProxy.destroyObject(objSVMethodMonitorMBean);
				}
			}
			return objSVCurrentPoint;
		}
	}
	
	public static class GetUnClosedNewConnectionCallable implements Callable {
		private long serverId;
		private String serverName = null;

		public GetUnClosedNewConnectionCallable(String serverName, long serverId) {
			this.serverId = serverId;
			this.serverName = serverName;
		}

		public Object call() throws Exception {
			HashMap rtn = new HashMap();
			UnclosedNewConnRuntime[] objUnclosedNewConnRuntime = null;
			UnclosedNewConneMonitorMBean objUnclosedNewConneMonitorMBean = null;
			try {
				objUnclosedNewConneMonitorMBean = (UnclosedNewConneMonitorMBean) ClientProxy.getObject(this.serverId, UnclosedNewConneMonitorMBean.class);
				objUnclosedNewConnRuntime = objUnclosedNewConneMonitorMBean.getCurrentUnclosedNewConn();

				rtn.put("SERVER_NAME", this.serverName);
				rtn.put("UNCLOSED_LIST", objUnclosedNewConnRuntime);
				rtn.put("SERVER_ID", String.valueOf(this.serverId));
			} catch (Throwable ex) {
				ex.printStackTrace();
			} finally {
				if (objUnclosedNewConneMonitorMBean != null) {
					ClientProxy.destroyObject(objUnclosedNewConneMonitorMBean);
				}
			}
			return rtn;
		}
	}

	public static class GetSessionCallable implements Callable {
		private long serverId;

		public GetSessionCallable(long serverId) {
			this.serverId = serverId;
		}

		public Object call() throws Exception {
			HashMap[] objUserInfo = null;
			AppframeSessionMonitorMBean objAppframeSessionMonitorMBean = null;
			try {
				objAppframeSessionMonitorMBean = (AppframeSessionMonitorMBean) ClientProxy.getObject(this.serverId, AppframeSessionMonitorMBean.class);
				objUserInfo = objAppframeSessionMonitorMBean.fetchLogedUsers();

				if (objUserInfo != null) {
					for (int i = 0; i < objUserInfo.length; i++) {
						objUserInfo[i].put("SERVER_ID", new Long(this.serverId));
					}
				}
			} catch (Throwable ex) {
				ex.printStackTrace();
			} finally {
				if (objAppframeSessionMonitorMBean != null) {
					ClientProxy.destroyObject(objAppframeSessionMonitorMBean);
				}
			}
			return objUserInfo;
		}
	}
	
	public static class ClearSecMemLocalCacheCallable implements Callable {
		private long serverId;
		private String serverName;
		private String[] cacheIds;

		public ClearSecMemLocalCacheCallable(long serverId, String serverName) {
			this.serverId = serverId;
			this.serverName = serverName;
			this.cacheIds = this.cacheIds;
		}

		public Object call() throws Exception {
			HashMap map = new HashMap();
			SecMemMonitorMBean objSecMemMonitorMBean = null;
			try {
				objSecMemMonitorMBean = (SecMemMonitorMBean) ClientProxy.getObject(this.serverId, SecMemMonitorMBean.class);
				objSecMemMonitorMBean.clearLocalCache();
			} catch (Throwable ex) {
				map.put(new Long(this.serverId), this.serverName + ",刷新memcached local cache失败");
				ex.printStackTrace();
			} finally {
				if (objSecMemMonitorMBean != null) {
					ClientProxy.destroyObject(objSecMemMonitorMBean);
				}
			}

			return map;
		}
	}

	public static class ClearLocalCacheCallable implements Callable {
		private long serverId;
		private String serverName;
		private String[] cacheIds;

		public ClearLocalCacheCallable(long serverId, String serverName) {
			this.serverId = serverId;
			this.serverName = serverName;
			this.cacheIds = this.cacheIds;
		}

		public Object call() throws Exception {
			HashMap map = new HashMap();
			MemcachedMonitorMBean objMemcachedMonitorMBean = null;
			try {
				objMemcachedMonitorMBean = (MemcachedMonitorMBean) ClientProxy.getObject(this.serverId, MemcachedMonitorMBean.class);
				objMemcachedMonitorMBean.clearLocalCache();
			} catch (Throwable ex) {
				map.put(new Long(this.serverId), this.serverName + ",刷新memcached local cache失败");
				ex.printStackTrace();
			} finally {
				if (objMemcachedMonitorMBean != null) {
					ClientProxy.destroyObject(objMemcachedMonitorMBean);
				}
			}

			return map;
		}
	}

	public static class RefreshCacheCallable implements Callable {
		private long serverId;
		private String serverName;
		private String[] cacheIds;

		public RefreshCacheCallable(long serverId, String serverName, String[] cacheIds) {
			this.serverId = serverId;
			this.serverName = serverName;
			this.cacheIds = cacheIds;
		}

		public Object call() throws Exception {
			HashMap map = new HashMap();
			List l = new ArrayList();
			CacheMonitorMBean objCacheMonitorMBean = null;
			try {
				objCacheMonitorMBean = (CacheMonitorMBean) ClientProxy.getObject(this.serverId, CacheMonitorMBean.class);
				for (int j = 0; j < this.cacheIds.length; j++)
					try {
						objCacheMonitorMBean.forceRefresh(this.cacheIds[j]);
					} catch (Throwable ex) {
						ex.printStackTrace();
						l.add(this.cacheIds[j] + "失败");
					}
			} catch (Throwable ex) {
				ex.printStackTrace();
			} finally {
				if (objCacheMonitorMBean != null) {
					ClientProxy.destroyObject(objCacheMonitorMBean);
				}
			}

			if (!l.isEmpty()) {
				map.put(new Long(this.serverId), this.serverName + ",刷新cache失败列表," + StringUtils.join(l.iterator(), ","));
			}
			return map;
		}
	}

	/** 收集Trace的Callable*/
	public static class CollectTraceInfoCallable implements Callable {
		/** [MON_P_HOST]*/
		private MonPHost objMonPHost;

		public CollectTraceInfoCallable(MonPHost objMonPHost) {
			this.objMonPHost = objMonPHost;
		}

		public Object call() throws Exception {
			Long rtn = null;
			try {
				int c = SSHUtil2.collectTraceFile(this.objMonPHost.getIp(), (int) this.objMonPHost.getSshport(), this.objMonPHost.getUsername(), this.objMonPHost.getPassword());
				rtn = new Long(c);
			} catch (Exception ex) {
			}
			return rtn;
		}
	}

	public static class MidServerTask {
		public String ip;
		public int port;
		public String username;
		public String password;
		public String serverName;
		public String path;
		public String shell;
	}

	public static class MidServerServerCallable implements Callable {
		private ParallelUtil.MidServerTask objMidServerTask;

		public MidServerServerCallable(ParallelUtil.MidServerTask objMidServerTask) {
			this.objMidServerTask = objMidServerTask;
		}

		public Object call() throws Exception {
			String rtn = "";
			try {
				SSHUtil.ssh4Shell(this.objMidServerTask.ip, this.objMidServerTask.port, this.objMidServerTask.username, this.objMidServerTask.password,
						this.objMidServerTask.serverName, this.objMidServerTask.path, this.objMidServerTask.shell);
				rtn = this.objMidServerTask.serverName + "执行成功";
			} catch (Exception ex) {
				rtn = this.objMidServerTask.serverName + "执行失败:" + ex.getMessage();
			}
			return rtn;
		}
	}

	public static class UrlCallable implements Callable {
		private int timeout = 3;
		private MidServerControl item = null;

		public UrlCallable(int timeout, MidServerControl item) {
			this.timeout = timeout;
			this.item = item;
		}

		public Object call() throws Exception {
			try {
				String info = HttpUtil.curl(this.item.getUrl(), this.timeout);
				this.item.setStatus("OK");
				this.item.setInfo(info);
			} catch (Exception ex) {
				this.item.setStatus("EXCEPTION");
			}
			return this.item;
		}
	}
}