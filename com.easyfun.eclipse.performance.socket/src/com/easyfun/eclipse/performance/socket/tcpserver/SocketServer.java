package com.easyfun.eclipse.performance.socket.tcpserver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Socket服务器线程
 * 
 * @author linzhaoming
 * 
 * 2011-5-15
 * 
 */
public class SocketServer extends Thread {

	private static SocketServer socketServer = null;
	public Socket socket = null;
	public ServerSocket server = null;
	private TCPSocketServerView parent;
	private BufferedInputStream in;
	private boolean desonnected = false;
	private boolean stop = false;

	public synchronized void setDesonnected(boolean cr) {
		if (socket != null && cr == true) {
			try {
				socket.close();
			} catch (Exception e) {
				System.err.println("Error closing clinet : setDesonnected : " + e);
			}
		}
		desonnected = cr;
	}

	public synchronized void setStop(boolean cr) {
		stop = cr;
		if (server != null && cr == true) {
			try {
				server.close();
			} catch (Exception e) {
				System.err.println("Error closing server : setStop : " + e);
			}
		}
	}

	public SocketServer(TCPSocketServerView parent, ServerSocket s) {
		super("SocketServer");
		this.parent = parent;
		server = s;
		setStop(false);
		setDesonnected(false);
		start();
	}

	public static synchronized SocketServer handle(TCPSocketServerView parent, ServerSocket serverSocket) {
		if (socketServer == null) {
			socketServer = new SocketServer(parent, serverSocket);
		} else {
			if (socketServer.server != null) {
				try {
					socketServer.setDesonnected(true);
					socketServer.setStop(true);
					if (socketServer.socket != null)
						socketServer.socket.close();
					if (socketServer.server != null)
						socketServer.server.close();
				} catch (Exception e) {
					parent.error(e.getMessage());
				}
			}
			socketServer.server = null;
			socketServer.socket = null;
			socketServer = new SocketServer(parent, serverSocket);
		}
		return socketServer;
	}

	public void run() {
		while (!stop) {
			try {
				socket = server.accept();
			} catch (Exception e) {
				if (!stop) {
					parent.error(e.getMessage(), "Error acception connection");
					stop = true;
				}
				continue;
			}
			startServer();
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception e) {
					System.err.println("Erro closing client socket : " + e);
				}
				socket = null;
				parent.getDisplay().syncExec(new Runnable() {
					public void run() {
						parent.setClientSocket(socket);
					}
				});
			}
		}
	}

	private void startServer() {
		parent.getDisplay().syncExec(new Runnable() {
			public void run() {
				parent.setClientSocket(socket);
			}
		});

		InputStream is = null;
		appendParent(parent, "> New Client: " + socket.getInetAddress().getHostAddress());
		try {
			is = socket.getInputStream();
			in = new BufferedInputStream(is);
		} catch (IOException e) {
			appendParent(parent, "> Cound't open input stream on Clinet " + e.getMessage());
			setDesonnected(true);
			return;
		}

		String rec = null;
		while (true) {
			rec = null;
			try {
				rec = readInputStream(in);
			} catch (final Exception e) {
				setDesonnected(true);
				if (!desonnected) {
					parent.error(e.getMessage(), "Lost Client conection");
					appendParent(parent, "> Server lost Client conection.");
				} else {
					appendParent(parent, "> Server closed Client conection.");
				}
				break;
			}

			if (rec != null) {
				final String recStr = rec;
				parent.getDisplay().syncExec(new Runnable() {
					public void run() {
						parent.appendnoNewLine(recStr);
					}
				});
			} else {
				setDesonnected(true);
				appendParent(parent, "> Client closed conection.");
				break;
			}
		}
	}

	private void appendParent(final TCPSocketServerView parent, final String msg) {
		parent.getDisplay().syncExec(new Runnable() {
			public void run() {
				parent.append(msg);
			}
		});
	}

	private static String readInputStream(BufferedInputStream in) throws IOException {
		String data = "";
		int s = in.read();
		if (s == -1)
			return null;
		data += "" + (char) s;
		int len = in.available();
		System.out.println("Len got : " + len);
		if (len > 0) {
			byte[] byteData = new byte[len];
			in.read(byteData);
			data += new String(byteData);
		}
		return data;
	}
}
