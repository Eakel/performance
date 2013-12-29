package com.easyfun.eclipse.performance.socket.tcpserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.easyfun.eclipse.common.util.resource.FileUtil;
import com.easyfun.eclipse.common.util.ui.DialogUtils;
import com.easyfun.eclipse.performance.socket.common.SocketUtils;

/**
 * @author linzhaoming
 *
 * 2011-5-8
 *
 */
public class TCPSocketServerView extends ViewPart {
	
	private final String NEW_LINE = "\r\n";
	private Text sendField;
	private StyledText messagesField;
	private Text portField;
	private Text ipField;

	private Button connectButton;

	private Socket socket;
	private ServerSocket server;
	private SocketServer socketServer;
	private PrintWriter out;
	private Button sendButton;
	private Button saveButton;
	private Button clearwButton;
	private Button disconnectButton;
	private Group mainComposite;

	public TCPSocketServerView() {
	}

	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());
		initial(parent);
	}
	
	private void initial(Composite p) {
		p.setLayout(new GridLayout());
		Composite parent = new Composite(p, SWT.NULL);
		parent.setLayout(new GridLayout());
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Group connectToGroup = new Group(parent, SWT.NONE);
		connectToGroup.setText("Listen On");
		final GridData gd_connectToGroup = new GridData(SWT.FILL, SWT.CENTER, true, false);
		connectToGroup.setLayoutData(gd_connectToGroup);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		connectToGroup.setLayout(gridLayout);

		final Label ipAddressLabel = new Label(connectToGroup, SWT.NONE);
		ipAddressLabel.setText("IP Address");

		ipField = new Text(connectToGroup, SWT.BORDER);
		ipField.setText("127.0.0.1");
		final GridData gd_ipField = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		ipField.setLayoutData(gd_ipField);

		final Label portLabel = new Label(connectToGroup, SWT.NONE);
		portLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		portLabel.setText("Port");

		portField = new Text(connectToGroup, SWT.BORDER);
		portField.setText("21");
		final GridData gd_portField = new GridData(SWT.FILL, SWT.CENTER, true, false);
		portField.setLayoutData(gd_portField);

		final Button portButton = new Button(connectToGroup, SWT.NONE);
		portButton.setText("P&ort");

		connectButton = new Button(connectToGroup, SWT.NONE);
		connectButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				connect();
			}
		});
		connectButton.setText("Start Listening");

		mainComposite = new Group(parent, SWT.NONE);
		mainComposite.setText("Connected Client: < NONE >");
		mainComposite.setLayout(new GridLayout());
		final GridData gd_mainComposite = new GridData(SWT.FILL, SWT.FILL, true, true);
		mainComposite.setLayoutData(gd_mainComposite);

		final Label conversationLabel = new Label(mainComposite, SWT.NONE);
		final GridData gd_conversationLabel = new GridData(SWT.FILL, SWT.CENTER, true, false);
		conversationLabel.setLayoutData(gd_conversationLabel);
		conversationLabel.setText("Conversation with client");

		messagesField = new StyledText(mainComposite, SWT.READ_ONLY | SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL);
		final GridData gd_messagesField = new GridData(SWT.FILL, SWT.FILL, true, true);
		messagesField.setLayoutData(gd_messagesField);

		final Composite bottomComposite = new Composite(parent, SWT.NONE);
		bottomComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final GridLayout gridLayout_1 = new GridLayout();
		gridLayout_1.numColumns = 2;
		bottomComposite.setLayout(gridLayout_1);

		final Group sendGroup = new Group(bottomComposite, SWT.NONE);
		sendGroup.setText("Send");
		final GridData gd_sendGroup = new GridData(SWT.FILL, SWT.CENTER, true, false);
		sendGroup.setLayoutData(gd_sendGroup);
		final GridLayout gridLayout_2 = new GridLayout();
		gridLayout_2.numColumns = 4;
		sendGroup.setLayout(gridLayout_2);

		final Label messageLabel = new Label(sendGroup, SWT.NONE);
		messageLabel.setText("Message");

		sendField = new Text(sendGroup, SWT.BORDER);
		final GridData gd_sendField = new GridData(SWT.FILL, SWT.CENTER, true, false);
		sendField.setLayoutData(gd_sendField);

		sendButton = new Button(sendGroup, SWT.NONE);
		sendButton.setText("Send");

		disconnectButton = new Button(sendGroup, SWT.NONE);
		disconnectButton.setText("Disconnect");

		final Composite composite = new Composite(bottomComposite, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));
		composite.setLayout(new GridLayout());

		saveButton = new Button(composite, SWT.NONE);
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {				
				File file = DialogUtils.openSaveDialog(getShell(), new String[]{"*.txt", "*.*"}, "socketServer.txt");
				if(file != null){
					try {
						FileUtil.writeTextFile(messagesField.getText(), file);
					} catch (IOException ex) {
						DialogUtils.showMsg(getShell(), "" + ex.getMessage(), "Error saving to file..");
					}
				}
			}
		});
		saveButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		saveButton.setText("Sa&ve");

		clearwButton = new Button(composite, SWT.NONE);
		clearwButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				messagesField.setText("");
			}
		});
		clearwButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		clearwButton.setText("&Clear");
	}

	private void connect() {
		if (server != null) {
			stop();
			return;
		}
		final String ip = ipField.getText();
		final String port = portField.getText();
		if (ip == null || ip.equals("")) {
			DialogUtils.showMsg(getShell(), "No IP Address. Please enter IP Address", "Error connecting");
			ipField.setFocus();
			ipField.selectAll();
			return;
		}
		if (port == null || port.equals("")) {
			DialogUtils.showMsg(getShell(), "No Port number. Please enter Port number", "Error connecting");
			portField.setFocus();
			portField.selectAll();
			return;
		}

		BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
			public void run() {
				if (!SocketUtils.checkHost(ip)) {
					DialogUtils.showMsg(getShell(), "Bad IP Address", "Error connecting");
					ipField.setFocus();
					ipField.selectAll();
					return;
				}
				int portNo = 0;
				try {
					portNo = Integer.parseInt(port);
				} catch (Exception e) {
					DialogUtils.showMsg(getShell(), "Bad Port number. Please enter Port number", "Error connecting");
					portField.setFocus();
					portField.selectAll();
					return;
				}
				try {
					InetAddress bindAddr = null;
					if (!ip.equals("0.0.0.0"))
						bindAddr = InetAddress.getByName(ip);
					else
						bindAddr = null;
					server = new ServerSocket(portNo, 1, bindAddr);

					ipField.setEditable(false);
					portField.setEditable(false);

					connectButton.setText("&Stop Listening");
					connectButton.setToolTipText("Stop Listening");
				} catch (Exception e) {
					error(e.getMessage(), "Starting Server at " + portNo);
					return;
				}
				messagesField.setText("> Server Started on Port: " + portNo + NEW_LINE);
				append("> ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				socketServer = SocketServer.handle(TCPSocketServerView.this, server);
			}
		});
	}

	public synchronized void disconnect() {
		try {
			socketServer.setDesonnected(true);
		} catch (Exception e) {
		}
	}

	public synchronized void stop() {
		try {
			disconnect();
			socketServer.setStop(true);
		} catch (Exception e) {
		}
		server = null;
		ipField.setEditable(true);
		portField.setEditable(true);
		connectButton.setText("&Start Listening");
		connectButton.setToolTipText("Start Listening");
		append("> Server stopped");
		append("> ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

	public synchronized void setClientSocket(Socket s) {
		if (s == null) {
			out = null;
			socket = null;
			changeBorder(null);
			sendButton.setEnabled(false);
			sendField.setEditable(false);
			disconnectButton.setEnabled(false);
		} else {
			socket = s;
			changeBorder(" " + socket.getInetAddress().getHostName() + " ["
					+ socket.getInetAddress().getHostAddress() + "] ");
			sendButton.setEnabled(true);
			sendField.setEditable(true);
			disconnectButton.setEnabled(true);
		}
	}

	public void error(String error) {
		if (error == null || error.equals(""))
			return;
		DialogUtils.showMsg(getShell(), error, "Error");
	}

	public void error(String error, String heading) {
		if (error == null || error.equals(""))
			return;
		DialogUtils.showMsg(getShell(), error, heading);
	}

	public void append(String msg) {
		messagesField.append(msg + NEW_LINE);
	}

	public void appendnoNewLine(String msg) {
		messagesField.append(msg);
	}

	public void sendMessage(final String s) {
		BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
			public void run() {
				try {
					if (out == null) {
						out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
					}
					append("S: " + s);
					out.print(s + NEW_LINE);
					out.flush();
					sendField.setText("");
				} catch (Exception e) {
					DialogUtils.showMsg(getShell(), e.getMessage(), "Error Sending Message");
					disconnect();
				}
			}
		});
	}

	private void changeBorder(String ip) {
		if (ip == null || ip.equals(""))
			mainComposite.setText("Connected Client : < NONE >");
		else
			mainComposite.setText("Connected Client : < " + ip + " >");
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	private Shell getShell(){
		return getSite().getShell();
	}
	
	public Display getDisplay(){
		return getShell().getDisplay();
	}

}