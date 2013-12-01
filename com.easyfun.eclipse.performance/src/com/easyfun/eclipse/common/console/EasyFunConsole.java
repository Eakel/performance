package com.easyfun.eclipse.common.console;

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

	final public static String CONSOLE_NAME = "EasyFun";

	private int addStrLen;

	static class DocChangeListener implements IDocumentListener {

		int lenBeforeChange;

		int expectedlen;

		final EasyFunConsole mvnConsole;

		final MyHyperLink mvnLink;

		public DocChangeListener(EasyFunConsole console, MyHyperLink link, int lenWanted) {
			mvnConsole = console;
			mvnLink = link;
			expectedlen = lenWanted;
		}

		public void documentAboutToBeChanged(DocumentEvent event) {
		}

		public void documentChanged(final DocumentEvent event) {
			int strLenAfterChange = event.getDocument().getLength();
			if (strLenAfterChange >= expectedlen) {
				Job job = new Job("MavenMessageConsole.addLinkToConsole") {
					public IStatus run(IProgressMonitor monitor) {
						try {
							mvnConsole.addHyperlink(mvnLink, expectedlen - mvnLink.getText().length(), mvnLink.getText().length());
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

	public void printToConsole(final String msg, final Color color) {
		final MessageConsoleStream stream = this.newMessageStream();
		this.activate();
		if (color != null)
			stream.setColor(color);
		addStrLen += msg.length() + 1;
		stream.println(msg); // "\n" will take length 1, so use print, not println
		try {
			stream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addLinkToConsole(final MyHyperLink link, Color color) {
		if (link.valid()) {
			try {
				final EasyFunConsole console = this;
				final int currentStrLen = addStrLen;
				printToConsole(link.getText(), color);
				console.getDocument().addDocumentListener(new DocChangeListener(console, link, currentStrLen + link.getText().length()));
			} catch (Exception e) {
			}
		}
	}

	public static EasyFunConsole getConsole() {
		IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
		IConsole[] curConsoles = manager.getConsoles();
		for (IConsole co : curConsoles) {
			if (co.getName().equals(CONSOLE_NAME))
				return (EasyFunConsole) co;
		}
		EasyFunConsole myConsole = new EasyFunConsole();
		manager.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}
}
