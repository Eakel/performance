package com.easyfun.eclipse.performance.navigator.console;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class EasyFunConsole extends MessageConsole {

	private final static String CONSOLE_NAME = "EasyFun";
	
	private static final String JOB_NAME = "EasyFunConsole.addLinkToConsole";

	private int addStrLen;

	private static class DocChangeListener implements IDocumentListener {

		private int expectedlen;

		private final EasyFunConsole console;

		private final EasyFunHyperLink hyperLink;

		public DocChangeListener(EasyFunConsole console, EasyFunHyperLink hyperLink, int lenWanted) {
			this.console = console;
			this.hyperLink = hyperLink;
			this.expectedlen = lenWanted;
		}

		public void documentAboutToBeChanged(DocumentEvent event) {
		}

		public void documentChanged(final DocumentEvent event) {
			int strLenAfterChange = event.getDocument().getLength();
			if (strLenAfterChange >= expectedlen) {
				Job job = new Job(JOB_NAME) {
					public IStatus run(IProgressMonitor monitor) {
						try {
							console.addHyperlink(hyperLink, expectedlen - hyperLink.getText().length(), hyperLink.getText().length());
						} catch (Exception e) {
						}

						return Status.OK_STATUS;
					}
				};
				job.schedule();
				event.getDocument().removeDocumentListener(this);
			}
		}
	}

	public EasyFunConsole() {
		super(CONSOLE_NAME, null);
		addStrLen = 0;
	}

	public void clearConsole() {
		super.clearConsole();
		this.addStrLen = 0;
	}

	public static void printToConsole(final String msg, final Color color) {
		EasyFunConsole console = getConsole();
		if(console == null){
			return;
		}
		
		final MessageConsoleStream stream = console.newMessageStream();
		console.activate();
		if (color != null) {
			stream.setColor(color);
		}
		console.addStrLen += msg.length() + 1;
		stream.println(msg); // "\n" will take length 1, so use print, not println
		try {
			stream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addLinkToConsole(final EasyFunHyperLink link, Color color) {
		EasyFunConsole console = getConsole();
		if(console == null){
			return;
		}
		if (link.valid()) {
			try {
				final int currentStrLen = console.addStrLen;
				printToConsole(link.getText(), color);
				console.getDocument().addDocumentListener(new DocChangeListener(console, link, currentStrLen + link.getText().length()));
			} catch (Exception e) {
			}
		}
	}

	private static EasyFunConsole getConsole() {
		ConsolePlugin consolePlugin = ConsolePlugin.getDefault();
		if(consolePlugin == null){
			return null;
		}
		
		IConsoleManager manager = consolePlugin.getConsoleManager();
		IConsole[] curConsoles = manager.getConsoles();
		for (IConsole co : curConsoles) {
			if (co.getName().equals(CONSOLE_NAME)) {
				return (EasyFunConsole) co;
			}
		}
		EasyFunConsole myConsole = new EasyFunConsole();
		manager.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}
}
