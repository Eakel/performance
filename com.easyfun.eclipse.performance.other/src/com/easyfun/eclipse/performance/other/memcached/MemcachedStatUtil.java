package com.easyfun.eclipse.performance.other.memcached;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.easyfun.eclipse.performance.other.memcached.model.MemModel;

public class MemcachedStatUtil {
	public static final byte[] BYTE_CRLF = { 13, 10 };
	public static final String SERVER_STATUS_END = "END";
	
	private static Properties p = new Properties();
	//TODO: 将描述放入文件中，达到可配置
	static {
		p.setProperty("curr_connections", "当前连接数量");
		p.setProperty("bytes", "内存对象字节数");
		p.setProperty("total_items", "当前对象数量");
		p.setProperty("cmd_set", "设置对象次数");
		p.setProperty("uptime", "Memcached运行时间");
		p.setProperty("get_hits", "hint查询次数");
		p.setProperty("limit_maxbytes", "限制内存字节数");
		p.setProperty("bytes_written", "输出字节数");
		p.setProperty("bytes_read", "输入字节数");
		p.setProperty("pid", "进程ID");
		p.setProperty("version", "Memcached版本");
		p.setProperty("pointer_size", "操作系统指针大小(32位系统一般是32bit,64就是64位操作系统)");
		p.setProperty("curr_items", "Memcached当前存储的内容数量(PCE为当前对象数量)");
		p.setProperty("threads", "当前线程数");
		p.setProperty("accepting_conns", "服务器是否达到过最大连接(0/1)");
		p.setProperty("cmd_flush", "Flush请求总数");
		p.setProperty("cmd_get", "查询请求总数");
		p.setProperty("get_misses", "查询成功未获取到数据的总次数");
		p.setProperty("rusage_user", "该进程累计的用户时间，单位：秒");
		p.setProperty("rusage_system", "该进程累计的系统时间，单位：秒");
		p.setProperty("total_connections", "Memcached运行以来接受的连接总数");
		p.setProperty("", "");
		p.setProperty("", "");
		p.setProperty("", "");
		p.setProperty("", "");
	}

	/** 执行stats命令，获取Memcache信息*/
	public static List<MemModel> getStat(String host, int port) throws Exception {
		HashMap rtn = new HashMap();
		List<MemModel> list = new ArrayList<MemModel>();
		Socket socket = null;
		try {
			socket = new Socket(host, port);
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			out.write("stats".getBytes());
			out.write(BYTE_CRLF);

			out.flush();

			rtn.put("host", socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
			String cmd = readLine(in);
			while (!"END".equals(cmd)) {
				String[] strs = StringUtils.split(cmd, " ");
				MemModel model = new MemModel(strs[1], strs[2]);
				String desc = getDescByKey(model.getKey());
				model.setDesc(desc);
				list.add(model);
				cmd = readLine(in);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (socket != null) {
				socket.close();
				socket = null;
			}
		}

		return list;
	}
	
	private static String getDescByKey(String key){
		if(p.containsKey(key)){
			return p.getProperty(key);
		}else{
			return "";
		}
	}

	private static String readLine(InputStream in) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		boolean eol = false;
		byte[] b = new byte[1];
		while (in.read(b, 0, 1) != -1) {
			if (b[0] == 13) {
				eol = true;
			} else {
				if ((eol) && (b[0] == 10)) {
					break;
				}
				eol = false;
			}

			bos.write(b, 0, 1);
		}

		if (bos.size() == 0) {
			return null;
		}
		return bos.toString().trim();
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getStat("10.96.19.157", 37777));
	}
}