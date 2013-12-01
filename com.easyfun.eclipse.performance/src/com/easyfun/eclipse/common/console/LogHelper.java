package com.easyfun.eclipse.common.console;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.easyfun.eclipse.performance.PerformanceActivator;

public class LogHelper {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private static Log log = LogFactory.getLog(EasyFunConsole.class);
	
	public static void debug(final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				String msg = "[" + dateFormat.format(new Date()) + " DEBUG] " + " " + message;
				EasyFunConsole.getConsole().printToConsole(msg, null);
				log.debug(msg);
			}

		});
	}

	public static void info(final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				String msg = "[" + dateFormat.format(new Date()) + " INFO] " + message;
				EasyFunConsole.getConsole().printToConsole(msg, new Color(null, 0, 0, 255));
//				log.info(msg);
			}
		});
	}

	public static void error(final Throwable t) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				String msg = "[" + dateFormat.format(new Date()) + " DEGBUG] " + t.getMessage();
				EasyFunConsole.getConsole().printToConsole(msg, new Color(null, 255, 0, 0));
				t.printStackTrace();
			}
		});
	}

	public static void error(final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			String msg = "[" + dateFormat.format(new Date()) + " ERROR]" + " " + message;
			public void run() {
				if (message.indexOf(PerformanceActivator.FILE) > -1 && message.indexOf(PerformanceActivator.LINE) > -1) {
					String url = (message.substring(0, message.indexOf(PerformanceActivator.LINE))).trim();
					int line = Integer.parseInt(message.substring(message.indexOf(PerformanceActivator.LINE) + 5).trim());
					MyHyperLink hyperlink = new MyHyperLink(msg, url, line - 1);
					EasyFunConsole.getConsole().addLinkToConsole(hyperlink, new Color(null, 255, 0, 0));
//					log.error(msg);
				} else {
					EasyFunConsole.getConsole().printToConsole(msg, new Color(null, 255, 0, 0));
//					log.error(msg);
				}
			}
		});
	}

	public static void error(final String message, final Throwable t) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {
					String msg = "[" + dateFormat.format(new Date()) + " ERROR]" + " " + message;
					public void run() {
						if (message.indexOf(PerformanceActivator.FILE) > -1 && message.indexOf(PerformanceActivator.LINE) > -1) {
							String url = (message.substring(0, message.indexOf(PerformanceActivator.LINE))).trim();
							int line = Integer.parseInt(message.substring(message.indexOf(PerformanceActivator.LINE) + 5).trim());
							MyHyperLink hyperlink = new MyHyperLink(msg, url, line - 1);
							EasyFunConsole.getConsole().addLinkToConsole(hyperlink, new Color(null, 255, 0, 0));
//							log.error(msg);
						} else {
							EasyFunConsole.getConsole().printToConsole(msg, new Color(null, 255, 0, 0));
//							log.error(msg);
						}
					}
				});
			}
		});
	}
}
