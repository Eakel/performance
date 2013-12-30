package com.easyfun.eclipse.performance.navigator.console;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.easyfun.eclipse.performance.PerformanceActivator;

/**
 * 将日志写到控制台
 * 
 * @author linzhaoming
 *
 */
public class LogHelper {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	
	public static void debug(final Log log,final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				String msg = "[" + dateFormat.format(new Date()) + " DEBUG] " + " " + message;
				EasyFunConsole.printToConsole(msg, null);
				if(log == null){
					System.out.println(msg);
				}else{
					log.info(msg);
				}
			}

		});
	}
	
	public static void info(final Log log, final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				String msg = "[" + dateFormat.format(new Date()) + " INFO] " + message;
				EasyFunConsole.printToConsole(msg, new Color(null, 0, 0, 255));
				if(log == null){
					System.out.println(msg);
				}else{
					log.info(msg);
				}
			}
		});
	}

	public static void error(final Log log,final Throwable t) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				String msg = "[" + dateFormat.format(new Date()) + " DEGBUG] " + t.getMessage();
				EasyFunConsole.printToConsole(msg, new Color(null, 255, 0, 0));
				if (log == null) {
					t.printStackTrace();
				} else {
					log.error(t);
				}
			}
		});
	}

	public static void error(final Log log,final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			String msg = "[" + dateFormat.format(new Date()) + " ERROR]" + " " + message;
			public void run() {
				if (message.indexOf(PerformanceActivator.FILE) > -1 && message.indexOf(PerformanceActivator.LINE) > -1) {
					String url = (message.substring(0, message.indexOf(PerformanceActivator.LINE))).trim();
					int line = Integer.parseInt(message.substring(message.indexOf(PerformanceActivator.LINE) + 5).trim());
					EasyFunHyperLink hyperlink = new EasyFunHyperLink(msg, url, line - 1);
					EasyFunConsole.addLinkToConsole(hyperlink, new Color(null, 255, 0, 0));
					if (log == null) {
						System.err.println(msg);
					} else {
						log.error(msg);
					}
				} else {
					EasyFunConsole.printToConsole(msg, new Color(null, 255, 0, 0));
					if (log == null) {
						System.err.println(msg);
					} else {
						log.error(msg);
					}
				}
			}
		});
	}

	public static void error(final Log log,final String message, final Throwable t) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						String msg = "[" + dateFormat.format(new Date()) + " ERROR]" + " " + message;	
						msg = msg + " " + t.getClass().getName() + " " + t.getMessage();
						if (message.indexOf(PerformanceActivator.FILE) > -1 && message.indexOf(PerformanceActivator.LINE) > -1) {
							String url = (message.substring(0, message.indexOf(PerformanceActivator.LINE))).trim();
							int line = Integer.parseInt(message.substring(message.indexOf(PerformanceActivator.LINE) + 5).trim());
							EasyFunHyperLink hyperlink = new EasyFunHyperLink(msg, url, line - 1);
							EasyFunConsole.addLinkToConsole(hyperlink, new Color(null, 255, 0, 0));
							if (log == null) {
								System.err.println(msg);
							} else {
								log.error(msg);
							}
						} else {
							EasyFunConsole.printToConsole(msg, new Color(null, 255, 0, 0));
							log.error(msg);
						}
					}
				});
			}
		});
	}
}
